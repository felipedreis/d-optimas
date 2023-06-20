package br.cefetmg.lsi.bimasco.actors;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.SnapshotOffer;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.agents.Agent;
import br.cefetmg.lsi.bimasco.messages.InternalStimulus;
import br.cefetmg.lsi.bimasco.messages.Stimulus;
import br.cefetmg.lsi.bimasco.persistence.AgentState;
import br.cefetmg.lsi.bimasco.persistence.DatabaseHelper;
import br.cefetmg.lsi.bimasco.persistence.MemoryState;
import br.cefetmg.lsi.bimasco.persistence.dao.AgentStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.MemoryStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.MessageStateDAO;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.join;

class AgentActorSnapshot implements Serializable {
    final Agent agent;
    final ActorRef agentsShard;
    final ActorRef regionsShard;
    final ActorRef leader;

    public AgentActorSnapshot(Agent agent, ActorRef agentsShard, ActorRef regionsShard, ActorRef leader) {
        this.agent = agent;
        this.agentsShard = agentsShard;
        this.regionsShard = regionsShard;
        this.leader = leader;
    }
}

//TODO: Create a Default Bimasco Actor
//TODO: Check if all sent stimuli are saved on memory
public class AgentActor extends AbstractPersistentActor implements Serializable, MessagePersister {

    private static final Logger logger = LoggerFactory.getLogger(AgentActor.class);

    private Agent agent;
    private SimulationSettings simulationSettings;
    private ActorRef agentsShard;
    private ActorRef regionsShard;
    private int id;

    private AgentStateDAO agentStateDAO;
    private MessageStateDAO messageStateDAO;
    private MemoryStateDAO memoryStateDAO;

    private ActorRef leader;

    private long lifetime;

    private long globalTime;

    private long globalSolutions;

    private Cancellable internalTask;

    private boolean agentStarted;

    public AgentActor(){
    }

    public AgentActor(SimulationSettings simulationSettings) {
        super();
        this.simulationSettings = simulationSettings;
    }

    @Override
    public void preStart()  {
        String[] tokens = self().path().name().split("-");
        id = Integer.parseInt(tokens[1]);

        agentStateDAO = DatabaseHelper.getMapper().agentStateDAO();
        messageStateDAO = DatabaseHelper.getMapper().messageStateDAO();
        memoryStateDAO = DatabaseHelper.getMapper().memoryStateDAO();
    }

    @Override
    public Receive createReceiveRecover() {
        return receiveBuilder()
                .match(SnapshotOffer.class, state -> {
                    logger.info("Restoring state of " + persistenceId());
                    AgentActorSnapshot snapshot = (AgentActorSnapshot) state.snapshot();

                    agent = snapshot.agent;
                    leader = snapshot.leader;
                    agentsShard = snapshot.agentsShard;
                    regionsShard = snapshot.regionsShard;
                    lifetime = agent.getAgentSettings().getLifetime();

                    leader.tell(new AgentRegister(id, self()), self());
                })
                .build();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SolutionRequest.class, this::onSolutionRequest)
                .match(SolutionResponse.class, this::onSolutionResponse)
                .match(CreateAgent.class, this::onCreateAgent)
                .match(InternalStimulus.class, this::onInternalStimulus)
                .match(UpdateGlobalSummary.class, this::onUpdateGlobalSummary)
                .match(StartSimulation.class, this::onStartSimulation)
                .match(StopSimulation.class, this::onStopSimulation)
                .matchAny((any) -> logger.info("Not handling " + any))
                .build();
    }

    private void onStartSimulation(StartSimulation startSimulation) {
        logger.info("Handling start simulation " + startSimulation);
        startSimulation.problem.ifPresent(agent::reset);
        agentStarted = true;
        globalSolutions = 0;
        startInternalTask();
    }

    private void onStopSimulation(StopSimulation stopSimulation) {
        internalTask.cancel();
        agentStarted = false;
        sender().tell(new SimulationStopped(), self());
    }

    private void startInternalTask() {
        InternalStimulus internalStimulusMessage = new InternalStimulus();
        internalStimulusMessage.setInformation(Stimulus.StimulusInformation.CHANGE_MY_STATE);

        internalTask = context().system().scheduler().schedule(
                Duration.create(10, TimeUnit.MILLISECONDS),
                Duration.create(5, TimeUnit.SECONDS),
                self(),
                internalStimulusMessage,
                context().system().dispatcher(),
                self());
    }

    private void onNewLeader(SetLeader leadership) {
        leader = leadership.leader;
        saveSnapshot(new AgentActorSnapshot(agent, agentsShard, regionsShard, leader));
        logger.info("Updating leader to " + leader.path());
    }

    @Deprecated
    private void onSolutionRequest(SolutionRequest request){
        persistMessage(received(agent.getProblem().toString(), request, globalTime, persistenceId()));

        if (agent.getAgentSettings().getConstructorMetaHeuristic()  || simulationSettings.getHasCooperation()) {
            List<Solution> solutions = Collections.emptyList();
            SolutionResponse response = new SolutionResponse(id, request.senderId, solutions);
            sender().tell(response, self());
        }
    }

    private void onSolutionResponse(SolutionResponse response)  {
        logger.info(persistenceId() + " processing SolutionResponse from " + sender().path().name());
        persistMessage(received(agent.getProblem().toString(), response, globalTime, persistenceId()));
        processSolutionList(response.senderId, response.solutions);
    }

    private void initialize(CreateAgent c) {
        logger.info("Running initialize for " + persistenceId());

        agentsShard = c.agentsShard;
        regionsShard = c.regionsShard;
        leader = c.leader;
        agent = new Agent(simulationSettings, c.settings);
        lifetime = c.settings.getLifetime();
    }

    private Object onCreateAgent(CreateAgent createAgent){
        logger.info("{} {}", sender(), createAgent);
        initialize(createAgent);
        sender().tell(new AgentRegister(id, self()), self());
        saveSnapshot(new AgentActorSnapshot(agent, agentsShard, regionsShard, leader));
        return null;
    }

    private void onInternalStimulus(InternalStimulus internalStimulus) {
        if (agent == null) {
            logger.info(format("Agent %s not proper initialized yet", persistenceId()));
            return;
        }

        if (!agentStarted) {
            logger.info("Agent {} not started",persistenceId());
            return;
        }

        logger.info(format("Processing internal stimulus for %s", persistenceId()));

        if (agent.getAgentSettings().getConstructorMetaHeuristic()) {
            processSolutionList(Messages.Nobody, Collections.emptyList());
        } else {

            // If there's no enough solutions in the whole system to feed this agent, avoid to send useless requests
            if (globalSolutions < agent.getSolutionsCount())
                return;

            Optional<Integer> regionOptional = agent.chooseRegion();

            if (regionOptional.isPresent()) {
                regionsShard.tell(new SolutionRequest(id, regionOptional.get(), agent.getSolutionsCount()), self());
                logger.info(join("Sending solution request to region ", regionOptional.get()));

                MemoryState memoryState = agent.getMemoryState();
                memoryState.setAgent(persistenceId());
                memoryState.setTime(globalTime);
                memoryState.setMemoryStatus(MemoryState.MEMORY_STATUS_USED);
                memoryState.setChosenRegion(regionOptional.get());
                persistMemoryState(memoryState);
            } else {
                leader.tell(new SolutionRequest(id, Messages.Nobody, agent.getSolutionsCount()), self());
                logger.info("Sending solution request to the leader");
            }
        }
    }

    private void processSolutionList(int region, List<Solution> solutions) {
        List<Solution> result;
        logger.info(format("Processing the list of %d solutions", solutions.size()));

        long begin = System.currentTimeMillis();
        result = agent.processMetaHeuristic(solutions, region);
        long end = System.currentTimeMillis();

        if (result != null) {
            for (int i = 0; i < result.size(); ++i) {
                result.get(i).setAgent(persistenceId());
                persistAgentState(agentState(begin, end, agent.getContext().getInvocations().get(), result.get(i).getId()));
            }

            MemoryState memoryState = agent.getMemoryState();
            memoryState.setAgent(persistenceId());
            memoryState.setTime(globalTime);
            memoryState.setMemoryStatus(MemoryState.MEMORY_STATUS_UPDATED);
            persistMemoryState(memoryState);

            saveSnapshot(new AgentActorSnapshot(agent, agentsShard, regionsShard, leader));
            logger.info(format("Sending a broadcast of %d solutions to leader %s", result.size(), leader.toString()));
            leader.tell(new SolutionResult(result), self());
        }
    }

    private AgentState agentState(long begin, long end, long functionEvaluations, UUID solutionId) {
        return new AgentState(
                agent.getProblem().toString(),
                persistenceId(),
                agent.getAgentSettings().getMetaHeuristicName(),
                globalTime,
                begin,
                end,
                functionEvaluations,
                solutionId
        );
    }

    private void persistAgentState(AgentState state){
        agentStateDAO.save(state);
    }

    private void persistMemoryState(MemoryState memoryState) {
        memoryStateDAO.save(memoryState);
    }

    private void onUpdateGlobalSummary(UpdateGlobalSummary update) {
        logger.info(format("Updating global status on %s", persistenceId()));
        globalTime = update.time;
        globalSolutions = update.summary.getN();

        agent.updateMemory(update.regionIds);

        if (update.time > lifetime) {
            internalTask.cancel();
            logger.info(format("%s stopped due to lifetime", persistenceId()));
        }
    }

    public Agent getAgent() {
        return agent;
    }

    @Override
    public String persistenceId() {
        return self().path().name();
    }

    @Override
    public MessageStateDAO messageStateDao() {
        return messageStateDAO;
    }
}
