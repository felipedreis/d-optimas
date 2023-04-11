package br.cefetmg.lsi.bimasco.actors;

import akka.actor.*;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import akka.cluster.sharding.ShardRegion;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.persistence.DatabaseHelper;
import br.cefetmg.lsi.bimasco.persistence.GlobalState;
import br.cefetmg.lsi.bimasco.persistence.dao.GlobalStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.MessageStateDAO;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.RegionSettings;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;
import static akka.cluster.ClusterEvent.*;
import static java.lang.String.format;

public class SimulationActor extends AbstractActor implements MessagePersister {
    Logger logger = Logger.getLogger(SimulationActor.class);

    private static final String ENTITY_ID = "leader";

    private List<ActorRef> nodes;
    private  ActorRef [] agents;
    private  ActorRef [] regions;
    private boolean amILeader;
    private ActorRef leader;
    private Cluster cluster;

    private SimulationSettings simulationSettings;
    private RegionSettings regionSettings;
    private ActorRef agentShard;
    private ActorRef regionShard;

    private boolean simulationStarted;

    private List<Integer> regionsIds;
    private List<Integer> agentsIds;
    private List<Integer> regionUsedIds;
    private List<Integer> agentsUsedIds;

    private SummaryStatistics[] regionsSummary;
    private StatisticalSummary globalStatistics;

    private UniformRealDistribution randDouble;

    private GlobalStateDAO globalStateDAO;
    private MessageStateDAO messageStateDAO;

    private long time;

    public SimulationActor(SimulationSettings settings){
        regionSettings = settings.getRegion();
        agents = new ActorRef[100];
        regions = new ActorRef[regionSettings.getMaxRegions()];
        regionsSummary = new SummaryStatistics[regionSettings.getMaxRegions()];
        globalStatistics = new SummaryStatistics();
        nodes = new ArrayList<>();
        simulationSettings = settings;
        amILeader = false;
        simulationStarted = false;

        regionsIds = new ArrayList<>();
        agentsIds = new ArrayList<>();
        regionUsedIds = new ArrayList<>();
        agentsUsedIds = new ArrayList<>();

        for (int i = 0; i < regionSettings.getMaxRegions(); ++i) {
            regionsIds.add(i);
        }

        for (int i = 0; i < simulationSettings.getNumberOfAgents(); ++i) {
            agentsIds.add(i);
        }

        randDouble = new UniformRealDistribution();
        time = 0;
    }


    @Override
    public void preStart() {
        logger.info("Simulation manager started");
        cluster = Cluster.get(context().system());
        logger.info("Joining cluster");
        cluster.subscribe(self(), ClusterEvent.MemberUp.class, ClusterEvent.LeaderChanged.class);
        logger.info("Subscribed to Member events");
        ClusterShardingSettings shardSettings = ClusterShardingSettings.create(context().system());

        agentShard = ClusterSharding.get(context().system())
                .start("agents", Props.create(AgentActor.class, simulationSettings), shardSettings,
                        Messages.agentMessageExtractor);

        regionShard = ClusterSharding.get(context().system())
                .start("regions", Props.create(RegionActor.class, simulationSettings), shardSettings,
                        Messages.regionMessageExtractor);

        globalStateDAO = DatabaseHelper.getMapper().globalStateDAO();
        messageStateDAO = DatabaseHelper.getMapper().messageStateDAO();
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(self());
        logger.info("Simulation manager stopped");
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CurrentClusterState.class, state -> {
                    for (Member member : state.getMembers()) {
                        if (member.status().equals(MemberStatus.up())) {
                            onNewMember(member);
                        }
                    }
                    if (state.leader().nonEmpty())
                        onLeaderChanged(state.getLeader());
                })
                .match(MemberUp.class, memberUp -> onNewMember(memberUp.member()))
                .match(LeaderChanged.class, leaderChanged -> onLeaderChanged(leaderChanged.getLeader()))
                .match(AgentRegister.class, this::onAgentRegister)
                .match(AgentRelease.class, this::onAgentRelease)
                .match(RegionRegister.class, this::onRegionRegister)
                .match(SolutionResult.class, this::onSolutionResult)
                .match(SolutionRequest.class, this::onSolutionRequest)
                .match(RegionSplit.class, this::onRegionSplit)
                .match(MergeRequest.class, this::onMergeRequest)
                .match(RegionRelease.class, this::onRegionRelease)
                .match(UpdateRegionSummary.class, this::onUpdateRegionSummary)
                .match(Terminate.class, this::terminate)
                .build();
    }

    private void terminate(Terminate t){
        logger.info("Terminating simulation");
        System.exit(0);
    }

    private void increaseTime(){
        time++;
        logger.info(format("Time increased: %d", time));
        if (time >= simulationSettings.getExecutionTime()) {
            logger.info(format("Simulation time exceeded the execution time %d", simulationSettings.getExecutionTime()));
            sendToAllNodes(new Terminate());
        }
    }

    @Deprecated
    void stopSimulation() {
        logger.info("Stopping simulation");
        regionShard.tell(ShardRegion.gracefulShutdownInstance(), self());
        agentShard.tell(ShardRegion.gracefulShutdownInstance(), self());
    }


    private void onRegionRelease(RegionRelease release) {
        releaseId(release.senderId);
        regions[release.senderId] = null;
        regionsSummary[release.senderId] = null;
        calculateGlobalParameters();
    }

    private void releaseId(Integer id) {
        regionsIds.add(id);
        regionUsedIds.remove(id);
    }

    private void onUpdateRegionSummary(UpdateRegionSummary update) {
        persistMessage(received(update, time, ENTITY_ID));
        regionsSummary[update.senderId] = update.summary;
        calculateGlobalParameters();
    }

    private void calculateGlobalParameters() {
        logger.debug("Calculating global parameters");

        increaseTime();
        List<SummaryStatistics> regionsStats = Arrays.stream(regionsSummary)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < regionsSummary.length; ++i) {
            SummaryStatistics s = regionsSummary[i];
            if (s != null) {
                buff.append(format("%d, %f, %f, %f\n",
                        i, s.getMean(), s.getStandardDeviation(), StatisticsHelper.variationRate(s)));
            }
        }
        globalStatistics = AggregateSummaryStatistics.aggregate(regionsStats);

        updateRegions();
        updateAgents();
        persist(state());

        logger.debug("Region statistics:  \n" + buff.toString());
        logger.debug(format("Global statistics changed(%d): Mean = %f, Std = %f, Rate = %f", time, globalStatistics.getMean(),
                globalStatistics.getStandardDeviation(), StatisticsHelper.variationRate(globalStatistics)));
    }

    /**
     * Due to asynchronous messaging, an id may be null even if its certainly in use.
     */
    private void updateRegions() {
        logger.debug("Updating global summary on all regions");
        for (int i : regionUsedIds) {
            ActorRef region = regions[i];
            if (region != null) {
                UpdateGlobalSummary updateGlobalSummary = new UpdateGlobalSummary(i, globalStatistics, time, regionUsedIds);
                logger.debug(format("Sending global update %s to %s", updateGlobalSummary, region));
                region.tell(updateGlobalSummary, self());
                persistMessage(sent(updateGlobalSummary, time, ENTITY_ID));
            }
        }
    }

    /**
     * Due to asynchronous messaging, an id may be null even if its certainly in use.
     */
    private void updateAgents() {
        logger.debug("Updating global summary on all agents");
        for (int i : agentsUsedIds) {
            ActorRef agent = agents[i];
            if (agent != null) {
                UpdateGlobalSummary updateGlobalSummary = new UpdateGlobalSummary(i, globalStatistics, time, regionUsedIds);
                agent.tell(updateGlobalSummary, self());
            }
        }
    }

    /**
     *
     * @param request
     */
    private void onSolutionRequest(SolutionRequest request) {
        logger.debug(format("Forwarding solution request from agent-%d to all regions", request.senderId));
        for (ActorRef region : regions)  {
            if (region != null)
                region.forward(request, context());
        }
    }

    /**
     *
     * @param regionSplit
     */
    private void onRegionSplit(RegionSplit regionSplit) {
        logger.info(format("Region %s split", sender().path().name()));

        persistMessage(received(regionSplit, time, ENTITY_ID));

        if (!regionSplit.lower.isEmpty()) {
            Optional<Integer> optionalLowerId = nextRegionId();
            optionalLowerId.ifPresentOrElse(id -> {
                createRegion(id, regionSplit.lower);
                logger.info(format("Created region-%d with lower solutions", id));
            }, () -> {
                logger.info("No region ids available, running a roulette to chose a region");
                int regionId = regionRoulette();
                ActorRef region = regions[regionId];
                region.tell(new SolutionResponse(Messages.Nobody, regionId, regionSplit.higher), self());
            });
        }

        if (!regionSplit.higher.isEmpty()){
            Optional<Integer> optionalHeigherId = nextRegionId();
            optionalHeigherId.ifPresentOrElse(id -> {
                createRegion(id, regionSplit.higher);
                logger.info(format("Created region-%d with higher solutions", id));
            }, () -> {
                logger.info("No region ids available, running a roulette to chose a region");
                int regionId = regionRoulette();
                ActorRef region = regions[regionId];
                region.tell(new SolutionResponse(Messages.Nobody, regionId, regionSplit.higher), self());
            });
        }

    }

    /**
     *
     * @param id
     * @param solutions
     */
    private void createRegion(Integer id, List<Solution> solutions) {
        logger.info(format("Creating region-%d", id));
        regionShard.tell(new CreateRegion(id, simulationSettings.getRegion(), time, solutions), self());
    }

    /**
     *
     * @param request
     */
    private void onMergeRequest(MergeRequest request) {
        logger.info(format("Forwarding merge request from region-%d to all other regions", request.senderId));
        for (ActorRef region : regions) {
            if (region != null && region != sender()) {
                region.forward(request, context());
            }
        }
    }

    /**
     *
     * @param result
     */
    private void onSolutionResult(SolutionResult result) {
        if (result.solutions.isEmpty())
            return;

        logger.info("Handling a solution result request from " + sender().path().name());

        if (regionUsedIds.size() < regionSettings.getMinRegions()) {
            Optional<Integer> id = nextRegionId();
            id.ifPresent(integer -> createRegion(integer, result.solutions));
        } else {
            logger.info("Running a roulette to choose a region");
            int regionId = regionRoulette();
            ActorRef region = regions[regionId];
            region.tell(new SolutionResponse(0, regionId, result.solutions), self());
        }
    }

    private int regionRoulette() {
        int i, regionId;

        double [] probabilities = new double[regionUsedIds.size()];
        int [] ids = new int[regionUsedIds.size()];
        double sum = 0;

        for (i = 0; i < probabilities.length; ++i) {
            regionId = regionUsedIds.get(i);
            ids[i] = regionId;
            SummaryStatistics regionStats = regionsSummary[regionId];
            probabilities[i] =  regionStats != null ? StatisticsHelper.variationRate(regionStats) : 0;
            probabilities[i] = 1/probabilities[i];
            sum += probabilities[i];
        }

        if (ids.length == 1)
            return ids[0];

        probabilities[0] /= sum;
        for (i = 1; i < probabilities.length; ++i) {
            probabilities[i] /= sum;
            probabilities[i] += probabilities[i - 1];
        }

        logger.info("Region ids: " + Arrays.toString(ids));
        logger.info("Accumulated probabilities: " + Arrays.toString(probabilities));

        if (Double.isInfinite(sum) || Double.isNaN(sum)) {
            do {
                i = (int) (randDouble.sample() * regionUsedIds.size());
                regionId = ids[i];
            } while (regions[regionId] == null);

            logger.info("Sum is negative. Random region chosen");
        } else {
            double prob = randDouble.sample();
            logger.info("Sum of probabilities: " + sum);
            logger.info("Sorted probability: " + prob);

            for (i = 0; i < probabilities.length; ++i) {
                if (prob <= probabilities[i])
                    break;
            }
            regionId = ids[i];
        }

        logger.info("Region choose " + regionId);
        return regionId;
    }

    /**
     * Register the actor corresponding to a region on the proper hash table. If the current actor is the leader
     * it notifies all other nodes of this new region. This way, eventually all simulation managers have track
     * of all regions.
     * @param register the message containing the id of the region
     */
    private void onRegionRegister(RegionRegister register) {
        logger.info("Registering " + sender().path().name() + " for broadcasting");
        regions[register.senderId] = sender();
        regionsSummary[register.senderId] = register.stats;
        calculateGlobalParameters();
        updateRegions();
        updateAgents();

        if (amILeader)
            forwardToAllNodes(register);
    }

    /**
     * Register the actor corresponding to an agent on the proper has table. If the current actor is the leader, it
     * notifies all other nodes of this new agent. Same way, eventually all simulation managers have track of all
     * agents.
     * @param register the message containing the agent key
     */
    private void onAgentRegister(AgentRegister register) {
        logger.info("Registering " + sender().path().name() + " for broadcasting");
        agents[register.senderId] = sender();
        if (amILeader)
            forwardToAllNodes(register);
    }

    /**
     *
     * @param agentRelease
     */
    private void onAgentRelease(AgentRelease agentRelease) {
        agents[agentRelease.senderId] = null;
        agentsUsedIds.remove(Integer.valueOf(agentRelease.senderId));
        agentsIds.add(agentRelease.senderId);
    }

    /**
     * Handle the cluster event of a new member. It resolve the actor address of the member and add it to the list of
     * nodes such that all members know each other. When the leader realizes that all members are up, it starts the
     * simulation.
     * @param member message containing the new member's address
     */
    void onNewMember(Member member) {
        Address memberAddress = member.address();
        logger.info("adding new member to cluster " + memberAddress.toString());
        ActorSelection newMemberSelection = context().system()
                .actorSelection(memberAddress.toString() + "/user/manager");

        CompletionStage<ActorRef> future = newMemberSelection.resolveOne(Duration.ofSeconds(1));
        future.thenAccept(nodes::add).thenRun(() -> {
            logger.info(format("Member %s added. Nodes %d/%d", memberAddress.toString(), nodes.size(), simulationSettings.getNodes()));
            if (nodes.size() == simulationSettings.getNodes()) {
                logger.info("Simulation will be started in 10 seconds");
                context().system().scheduler().scheduleOnce(Duration.ofSeconds(10), this::startSimulation, context().dispatcher());
            }
        });
    }

    /**
     * Eventually if the leader changes it's necessary to updated al regions and agents reference for the leader, since
     * it is the responsible for broadcasting solutions. It also update the leadership on cluster nodes.
     * @param leaderAddress message containing the leader address
     */
    void onLeaderChanged(Address leaderAddress) {
        Address selfAddress = cluster.selfMember().address();

        logger.info("Actor " + selfAddress + " updating leader to " + leaderAddress);

        ActorSelection leaderSelection = context().system()
                .actorSelection(leaderAddress.toString());

        if (selfAddress.equals(leaderAddress)) {
            logger.info("I am the leader");
            amILeader = true;
            leader = self();

            if (simulationStarted) {
                logger.info("Updating running agents");
                for (ActorRef agent : agents) {
                    if (agent != null) {
                        logger.info(format("Updating agent %s", agent.toString()));
                        agent.tell(new SetLeader(self()), self());
                    }
                }

                for (ActorRef region : regions) {
                    if (region != null) {
                        logger.info(format("Updating region %s", region.toString()));
                        region.tell(new SetLeader(self()), self());
                    }
                }
            }

        } else {
            logger.info("I am not the leader");
            amILeader = false;
            CompletionStage<ActorRef> future = leaderSelection.resolveOne(Duration.ofSeconds(1));
            future.thenAccept(actorRef -> leader = actorRef).handle((aVoid, throwable) -> {
                throwable.printStackTrace();
                return aVoid;
            });
        }
    }

    /**
     * It creates the agents and the regions. The entities are responsible for answering registering to the leader
     * for broadcasting.
     */
    void startSimulation() {
        logger.info("Starting the simulation");

        if (amILeader) {
            Optional<Integer> id;

            for (AgentSettings agentSettings : simulationSettings.getAgents()) {
                for (int i = 0; i < agentSettings.getCount(); ++i) {
                    id = nextAgentId();

                    if (id.isPresent()) {
                        Integer agentId = id.get();
                        agentShard.tell(new CreateAgent(agentId, agentShard, regionShard, self(), agentSettings), self());
                        logger.info("Constructing agent-" + agentId);
                    }

                }
            }
        }

        simulationStarted = true;
    }

    private Optional<Integer> nextRegionId() {
        Optional<Integer> ret = nextId(regionsIds);
        ret.ifPresent(regionUsedIds::add);
        return ret;
    }

    private Optional<Integer> nextAgentId() {
        Optional<Integer> ret = nextId(agentsIds);
        ret.ifPresent(agentsUsedIds::add);
        return ret;
    }

    private Optional<Integer> nextId(List<Integer> idList) {
        if (!idList.isEmpty()) {
            int id = idList.remove(0);
            return Optional.of(id);
        }
        return Optional.empty();
    }


    void sendToAllNodes(Object message) {
        for (ActorRef node : nodes) {
            logger.info(format("Sending message %s to node %s", message, node));
            node.tell(message, self());
        }
    }

    void forwardToAllNodes(Object message) {
        for (ActorRef node : nodes) {
            if (node.equals(self())) {
                logger.info("Skipping self address");
                continue;
            }

            logger.info(format("Forwarding message %s to node %s", message, node));
            node.forward(message, context());
        }
    }

    void persist(GlobalState obj) {
        globalStateDAO.save(obj);
    }

    public GlobalState state(){
        return new GlobalState(time, globalStatistics.getMean(), globalStatistics.getVariance(), globalStatistics.getN(),
                regionUsedIds.size(), regionUsedIds);
    }

    @Override
    public MessageStateDAO messageStateDao() {
        return messageStateDAO;
    }
}
