package br.cefetmg.lsi.bimasco.actors;

import akka.actor.ActorRef;
import akka.cluster.sharding.ShardRegion;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.RegionSettings;
import org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.util.*;

public class Messages {
    public static final MessageExtractor agentMessageExtractor = new MessageExtractor("agent");
    public static final MessageExtractor regionMessageExtractor = new MessageExtractor("region");
    public static final int Nobody = Integer.MIN_VALUE;
    public static final int Everybody = -0x3f3f3f3f;




    public static class SetLeader implements Serializable {
        final ActorRef leader;
        public SetLeader(ActorRef leader) {
            this.leader = leader;
        }
    }

    public static class AgentRegister extends AbstractMessage {
        public final ActorRef sender;

        public AgentRegister(int id, ActorRef sender) {
            super(id, Messages.Nobody);
            this.sender = sender;
        }
    }

    public static class CreateAgent extends AbstractMessage {
        final ActorRef agentsShard;
        final ActorRef regionsShard;
        final ActorRef leader;
        final AgentSettings settings;

        public CreateAgent(int receiverId, ActorRef agentsShard, ActorRef regionsShard, ActorRef leader, AgentSettings settings) {
            super(0, receiverId);
            this.agentsShard = agentsShard;
            this.regionsShard = regionsShard;
            this.leader = leader;
            this.settings = settings;
        }
    }

    public static class RegionRegister extends AbstractMessage {
        final SummaryStatistics stats;
        public RegionRegister(int senderId, SummaryStatistics stats) {
            super(senderId, Messages.Nobody);
            this.stats = stats;
        }
    }

    public static class CreateRegion extends AbstractMessage{
        final RegionSettings settings;
        final List<Solution>  initialSolutions;
        final long time;

        final Problem problem;

        public CreateRegion(int receiverId, RegionSettings settings, long time, Problem problem){
            super(0, receiverId);
            this.settings = settings;
            initialSolutions = new ArrayList<>();
            this.time = time;
            this.problem = problem;
        }

        public CreateRegion(int receiverId, RegionSettings settings, long time, List<Solution> initialSolutions, Problem problem) {
            super(0, receiverId);
            this.settings = settings;
            this.initialSolutions = initialSolutions;
            this.time = time;
            this.problem = problem;
        }

        public CreateRegion(int receiverId, long time, Problem problem) {
            super(0, receiverId);
            this.settings = null;
            this.problem = problem;
            this.time = time;
            initialSolutions = List.of();
        }
    }

    public static class RegionRelease extends  AbstractMessage {
        public RegionRelease(int senderId) {
            super(senderId, Messages.Nobody);
        }
    }

    public static class SolutionRequest extends AbstractMessage {
        final int counter;

        public SolutionRequest(int senderId, int receiverId, int counter) {
            super(senderId, receiverId);
            this.counter = counter;
        }
    }

    public static class SolutionResponse extends AbstractMessage {
        final List<Solution> solutions;

        public SolutionResponse(int senderId, int receiverId, List<Solution> solutions) {
            super(senderId, receiverId);
            this.solutions = solutions;
        }
    }

    public static class UpdateRegionSummary extends AbstractMessage {
        final SummaryStatistics summary;

        public UpdateRegionSummary(int senderId, SummaryStatistics summary) {
            super(senderId, Messages.Nobody);
            this.summary = summary;
        }
    }

    public static class UpdateGlobalSummary extends AbstractMessage {
        final StatisticalSummary summary;
        final long time;
        final List<Integer> regionIds;

        public UpdateGlobalSummary(int receiverId, StatisticalSummary summary, long time, List<Integer> regionIds) {
            super(Messages.Nobody, receiverId);
            this.summary = summary;
            this.time = time;
            this.regionIds = regionIds;
        }
    }

    public static class MergeRequest extends AbstractMessage {
        final SummaryStatistics summary;

        final MultivariateSummaryStatistics searchSpaceSummary;

        public MergeRequest(int senderId, int receiverId, SummaryStatistics summary,
                            MultivariateSummaryStatistics searchSpaceSummary) {
            super(senderId, receiverId);
            this.summary = summary;
            this.searchSpaceSummary = searchSpaceSummary;
        }
    }

    public static class MergeResponse extends AbstractMessage {
        public MergeResponse(int senderId, int receiverId) {
            super(senderId, receiverId);
        }
    }

    public static class MergeResult extends AbstractMessage {
        final List<Solution> solutions;

        public MergeResult(int senderId, int receiverId, List<Solution> solutions) {
            super(senderId, receiverId);
            this.solutions = solutions;
        }
    }

    public static class AgentRelease extends AbstractMessage {
        public AgentRelease(int  senderId) {
            super(senderId, Messages.Nobody);
        }
    }

    public static class SolutionResult implements Serializable{
        final List<Solution> solutions;

        public SolutionResult(List<Solution> solutions) {
            this.solutions = solutions;
        }
    }

    public static class RegionSplit extends AbstractMessage {
        final List<Solution> lower;
        final List<Solution> higher;

        public RegionSplit(int senderId, List<Solution> lower, List<Solution> higher) {
            super(senderId, Messages.Nobody);
            this.lower = lower;
            this.higher = higher;
        }
    }

    public static class ExtractData implements Serializable {
        final SolutionAnalyser analyser;
        final String path;

        public ExtractData(SolutionAnalyser analyser, String path) {
            this.analyser = analyser;
            this.path = path;
        }
    }

    public static class Evaluate implements Serializable {
        public final double[] x;

        public Evaluate(double[] x) {
            this.x = x;
        }

        @Override
        public String toString() {
            return "Evaluate{" +
                    "x=" + Arrays.toString(x) +
                    '}';
        }
    }

    public static class EvaluateResult implements Serializable {
        public final Optional<double[]> y;
        public EvaluateResult(double [] y) {
            this.y = Optional.of(y);
        }

        public EvaluateResult() {
            this.y = Optional.empty();
        }
    }

    public static class StartSimulation implements Serializable {

        public final Optional<Problem> problem;

        public StartSimulation(Problem problem) {
            this.problem = Optional.of(problem);
        }

        public StartSimulation(){
            problem = Optional.empty();
        }

        @Override
        public String toString() {
            return "StartSimulation{" +
                    "problem=" + problem +
                    '}';
        }
    }

    public static class SimulationReady implements Serializable {}

    public static class StopSimulation implements Serializable {}
    public static class SimulationStopped implements Serializable {
        public final List<ActorRef> nodes;

        public SimulationStopped() {
            this(List.of());
        }
        public SimulationStopped(List<ActorRef> nodes) {
            this.nodes = nodes;
        }
    }

    public static class GetState implements Serializable {}

    public static class Terminate implements Serializable {}
}

class MessageExtractor implements ShardRegion.MessageExtractor {
    public final String entity;
    public final int numberOfShards;
    public MessageExtractor(String entity) {
        this.entity = entity;
        this.numberOfShards = 10;
    }

    @Override
    public String entityId(Object message) {
        if (message instanceof AbstractMessage) {
            AbstractMessage abstractMessage = (AbstractMessage) message;
            return String.format("%s-%d", entity, abstractMessage.receiverId);
        }
        return null;
    }

    @Override
    public Object entityMessage(Object message) {
        return message;
    }

    @Override
    public String shardId(Object message) {
        long shardId;

        if (message instanceof AbstractMessage) {
            AbstractMessage abstractMessage = (AbstractMessage) message;
            shardId = abstractMessage.receiverId % numberOfShards;
        } else {
            shardId = 0;
        }

        return String.valueOf(shardId);
    }
}

abstract class AbstractMessage implements Serializable {
    final UUID messageId;
    final int senderId;
    final int receiverId;

    public AbstractMessage(int senderId, int receiverId) {
        messageId = UUID.randomUUID();
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}