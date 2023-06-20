package br.cefetmg.lsi.bimasco.actors;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.SnapshotOffer;
import br.cefetmg.lsi.bimasco.core.Element;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.regions.Region;
import br.cefetmg.lsi.bimasco.messages.InternalStimulus;
import br.cefetmg.lsi.bimasco.messages.Stimulus;
import br.cefetmg.lsi.bimasco.persistence.DatabaseHelper;
import br.cefetmg.lsi.bimasco.persistence.RegionState;
import br.cefetmg.lsi.bimasco.persistence.SolutionState;
import br.cefetmg.lsi.bimasco.persistence.dao.MessageStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.RegionStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.SolutionStateDAO;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;
import static java.lang.String.format;

//TODO: Change element's name
//TODO: create a Interface to pass regions over the akka

class RegionActorSnapshot implements Serializable {
    final Region region;
    final ActorRef leader;
    final StatisticalSummary globalSummary;

    public RegionActorSnapshot(Region region, ActorRef leader, StatisticalSummary globalSummary) {
        this.region = region;
        this.leader = leader;
        this.globalSummary = globalSummary;

    }
}

public class RegionActor extends AbstractPersistentActor implements Serializable, MessagePersister {

    private static final Logger logger = LoggerFactory.getLogger(RegionActor.class.getSimpleName());

    private Problem problem;

    private Region region;

    private SimulationSettings settings;

    private int id;

    private ActorRef leader;

    private StatisticalSummary globalSummary;

    private Cancellable internalTask;

    private long time;

    private boolean regionStarted;

    private RegionStateDAO regionStateDAO;

    private SolutionStateDAO solutionStateDAO;

    private MessageStateDAO messageStateDAO;

    public RegionActor(SimulationSettings settings) {
        super();
        this.settings = settings;
        regionStateDAO = DatabaseHelper.getMapper().regionStateDAO();
        solutionStateDAO = DatabaseHelper.getMapper().solutionStateDAO();
        messageStateDAO = DatabaseHelper.getMapper().messageStateDAO();
    }

    @Override
    public void preStart() {
        String [] tokens = self().path().name().split("-");
        id = Integer.parseInt(tokens[1]);

        logger.info("Starting " + persistenceId());
    }

    @Override
    public void postStop() {
        logger.info("Stopping " + persistenceId());
        if (internalTask != null && !internalTask.isCancelled())
            internalTask.cancel();
    }

    @Override
    public Receive createReceiveRecover() {
        return receiveBuilder()
                .match(SnapshotOffer.class, state -> {
                    logger.info("Restoring state of " + persistenceId());
                    RegionActorSnapshot snapshot = (RegionActorSnapshot) state.snapshot();
                    region = snapshot.region;
                    leader = snapshot.leader;
                    globalSummary = snapshot.globalSummary;
                })
                .build();
    }

    @Override
    public Receive createReceive() {
        return  receiveBuilder()
                .match(CreateRegion.class, this::onCreate)
                .match(SetLeader.class, this::onLeaderChanged)
                .match(SolutionResponse.class, this::onSolutionResponse)
                .match(SolutionRequest.class, this::onSolutionRequest)
                .match(MergeRequest.class, this::onMergeRequest)
                .match(MergeResponse.class, this::onMergeResponse)
                .match(MergeResult.class, this::onMergeResult)
                .match(InternalStimulus.class, this::onInternalStimulus)
                .match(UpdateGlobalSummary.class, this::onUpdateGlobalSummary)
                .match(StopSimulation.class, this::onStopSimulation)
                .matchAny(any -> logger.info("Not handling " + any.getClass()))
                .build();
    }

    private void initialize(CreateRegion config) {
        problem = config.problem;
        time = config.time;
        region = new Region(id, config.problem, config.settings);
        leader = sender();
        addSolutions(config.initialSolutions);

        sender().tell(new RegionRegister(id, region.getSummary()), self());

        InternalStimulus internalStimulusMessage = new InternalStimulus();
        internalStimulusMessage.setInformation(Stimulus.StimulusInformation.CHANGE_MY_STATE);

        internalTask = this.getContext().system().scheduler().schedule(
                Duration.create(10, TimeUnit.SECONDS),
                Duration.create(30, TimeUnit.SECONDS),
                self(),
                internalStimulusMessage,
                context().system().dispatcher(),
                self());

        regionStarted = true;
    }

    private void onCreate(CreateRegion config) {
        logger.info("Handling create region " + persistenceId());
        initialize(config);
        saveSnapshot(snapshot());
    }

    private void onLeaderChanged(SetLeader msg) {
        leader = sender();
        logger.info("Updating leader to " + leader.path());
        saveSnapshot(snapshot());
    }

    private void onStopSimulation(StopSimulation stopSimulation) {
        internalTask.cancel();
        region.clear();
        regionStarted = false;
        sender().tell(new SimulationStopped(), self());
    }


    private void onUpdateGlobalSummary(UpdateGlobalSummary update) {
        persistMessage(received(problem.toString(), update, time, persistenceId()));
        globalSummary = update.summary;
        time = update.time;
        logger.debug(format("Updated global summary %s", update));
    }

    private  void onSolutionRequest(SolutionRequest request) {
        logger.info("Region " + persistenceId() +" handling solution request from " + sender().path().name());
        persistMessage(received(problem.toString(), request, time, persistenceId()));

        if (region.getSolutionList().size() > request.counter) {
            List<Solution> solutions = region.getRandomSolutions(request.counter);
            SolutionResponse response = new SolutionResponse(id, request.receiverId, solutions);
            sender().tell(response, self());

            persistMessage(sent(problem.toString(), response, time, persistenceId()));
        }
    }

    private  void onSolutionResponse(SolutionResponse response) {
        logger.info("Region " + persistenceId() + " handling solution response from " + sender().path().name());
        persistMessage(received(problem.toString(), response, time, persistenceId()));
        addSolutions(response.solutions);
    }

    private void onMergeRequest(MergeRequest request) {
        logger.info("Region " + persistenceId() + " handling merge request from " + sender().path().name());
        persistMessage(received(problem.toString(), request, time, persistenceId()));

        EuclideanDistance distanceCalculator = new EuclideanDistance();

        StatisticalSummary merged = AggregateSummaryStatistics.aggregate(List.of(region.getSummary(), request.summary));
        double distance = distanceCalculator.compute(region.getSearchSpaceSummary().getMean(),
                request.searchSpaceSummary.getMean());

        if (StatisticsHelper.variationRate(merged) < StatisticsHelper.variationRate(region.getSummary())) {
            MergeResponse response = new MergeResponse(id, request.senderId);
            sender().tell(response, self());

            persistMessage(sent(problem.toString(), response, time, persistenceId()));
        }
    }

    private void onMergeResponse(MergeResponse response) {
        persistMessage(received(problem.toString(), response, time, persistenceId()));

        if (region != null) {
            logger.info("Region " + persistenceId() + " handling a merge response from " + sender().path().name());
            MergeResult mergeResult = new MergeResult(id, response.senderId, region.getSolutionList());
            RegionRelease release = new RegionRelease(id);

            sender().tell(mergeResult, self());
            leader.tell(release, self());

            persistMessage(sent(problem.toString(), mergeResult, time, persistenceId()));
            persistMessage(sent(problem.toString(), release, time, persistenceId()));

            stopRegion();
        }
    }

    private void stopRegion() {
        internalTask.cancel();
        region = null;
        globalSummary = null;
        saveSnapshot(snapshot());
    }

    private void onMergeResult(MergeResult result) {
        logger.info("Region " + persistenceId() + " merging solutions from " + sender().path().name());
        persistMessage(received(problem.toString(), result, time, persistenceId()));

        addSolutions(result.solutions);
    }

    private void addSolutions(List<Solution> solutions) {
        region.addSolutionsCollection(solutions);
        saveSnapshot(snapshot());

        for (Solution s : solutions) {
            persistSolution(solutionState(s));
        }

        persistRegion(regionState());

        UpdateRegionSummary updateSummary = new UpdateRegionSummary(id, region.getSummary());
        leader.tell(updateSummary, self());
        persistMessage(sent(problem.toString(), updateSummary, time, persistenceId()));
    }

    private void onInternalStimulus(InternalStimulus stimulus) {
        if (region == null) {
            logger.info("Region {} not initialized yet", persistenceId());
            return;
        }

        if (!regionStarted) {
            logger.info("Region {} not started", persistenceId());
        }

        if (globalSummary == null) {
            logger.info("Global statistics not available yet at {}", persistenceId());
            return;
        }

        if (StatisticsHelper.variationRate(globalSummary) < StatisticsHelper.variationRate(region.getSummary())) {
            List<List<Solution>> splitResult = region.split();

            if (!splitResult.isEmpty()) {
                RegionSplit split = new RegionSplit(id, splitResult.get(0), splitResult.get(1));
                leader.tell(split, self());

                persistMessage(sent(problem.toString(), split, time, persistenceId()));
                saveSnapshot(snapshot());

                logger.info("Region " + persistenceId() + " split. Staying with the lower solutions");
            } else {
                MergeRequest mergeRequest = new MergeRequest(id, Messages.Everybody, region.getSummary(),
                        region.getSearchSpaceSummary());
                leader.tell(mergeRequest, self());
                persistMessage(sent(problem.toString(), mergeRequest, time, persistenceId()));
            }
        }
    }

    private RegionActorSnapshot snapshot() {
        return new RegionActorSnapshot(region, leader, globalSummary);
    }


    private RegionState regionState() {
        List<UUID> solutionsIds = region.getSolutionList().stream().map(Solution::getId)
                .collect(Collectors.toList());
        return new RegionState(
                problem.toString(),
                persistenceId(),
                time,
                region.getBestSolution().getId(),
                region.getSummary().getMean(),
                region.getSummary().getVariance(),
                solutionsIds,
                region.getSummary().getN());
    }

    private SolutionState solutionState(Solution<?,?,?> s) {
        List<Double> values = s.getSolutionsVector()
                .stream()
                .map(Element::toDoubleValue)
                .collect(Collectors.toList());

        return new SolutionState(
                s.getId(),
                problem.toString(),
                s.getAgent(),
                persistenceId(),
                time,
                values,
                s.getFunctionValue().doubleValue()
        );
    }

    void persistRegion(RegionState s) {
        regionStateDAO.save(s);
    }

    void persistSolution(SolutionState s) {
        solutionStateDAO.save(s);
    }

    @Override
    public MessageStateDAO messageStateDao() {
        return messageStateDAO;
    }

    @Override
    public String persistenceId() {
        return self().path().name();
    }

}
