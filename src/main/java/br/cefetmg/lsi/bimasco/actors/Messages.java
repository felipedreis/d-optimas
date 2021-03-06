package br.cefetmg.lsi.bimasco.actors;

import akka.actor.ActorRef;
import akka.cluster.sharding.ShardRegion;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.RegionSettings;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Messages {
    public static final MessageExtractor agentMessageExtractor = new MessageExtractor("agent");
    public static final MessageExtractor regionMessageExtractor = new MessageExtractor("region");
    public static final int Nobody = Integer.MIN_VALUE;
    public static final int Everybody = -0x3f3f3f3f;
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
        return "AbstractMessage{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}

class SetLeader implements Serializable {
    final ActorRef leader;
    public SetLeader(ActorRef leader) {
        this.leader = leader;
    }
}

class AgentRegister extends AbstractMessage {
    public AgentRegister(int id) {
        super(id, Messages.Nobody);
    }
}

class CreateAgent extends AbstractMessage {
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

class RegionRegister extends AbstractMessage {
    final SummaryStatistics stats;
    public RegionRegister(int senderId, SummaryStatistics stats) {
        super(senderId, Messages.Nobody);
        this.stats = stats;
    }
}

class CreateRegion extends AbstractMessage{
    final RegionSettings settings;
    final List<Solution>  initialSolutions;
    final long time;

    public CreateRegion(int receiverId, RegionSettings settings, long time){
        super(0, receiverId);
        this.settings = settings;
        initialSolutions = new ArrayList<>();
        this.time = time;
    }

    public CreateRegion(int receiverId, RegionSettings settings, long time, List<Solution> initialSolutions) {
        super(0, receiverId);
        this.settings = settings;
        this.initialSolutions = initialSolutions;
        this.time = time;
    }
}

class RegionRelease extends  AbstractMessage {
    public RegionRelease(int senderId) {
        super(senderId, Messages.Nobody);
    }
}

class SolutionRequest extends AbstractMessage {
    final int counter;

    public SolutionRequest(int senderId, int receiverId, int counter) {
        super(senderId, receiverId);
        this.counter = counter;
    }
}

class SolutionResponse extends AbstractMessage {
    final List<Solution> solutions;

    public SolutionResponse(int senderId, int receiverId, List<Solution> solutions) {
        super(senderId, receiverId);
        this.solutions = solutions;
    }
}

class UpdateRegionSummary extends AbstractMessage {
    final SummaryStatistics summary;

    public UpdateRegionSummary(int senderId, SummaryStatistics summary) {
        super(senderId, Messages.Nobody);
        this.summary = summary;
    }
}

class UpdateGlobalSummary extends AbstractMessage {
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

class MergeRequest extends AbstractMessage {
    final SummaryStatistics summary;

    public MergeRequest(int senderId, int receiverId, SummaryStatistics summary) {
        super(senderId, receiverId);
        this.summary = summary;
    }
}

class MergeResponse extends AbstractMessage {
    public MergeResponse(int senderId, int receiverId) {
        super(senderId, receiverId);
    }
}

class MergeResult extends AbstractMessage {
    final List<Solution> solutions;

    public MergeResult(int senderId, int receiverId, List<Solution> solutions) {
        super(senderId, receiverId);
        this.solutions = solutions;
    }
}

class AgentRelease extends AbstractMessage {
    public AgentRelease(int  senderId) {
        super(senderId, Messages.Nobody);
    }
}

class SolutionResult implements Serializable{
    final List<Solution> solutions;

    public SolutionResult(List<Solution> solutions) {
        this.solutions = solutions;
    }
}

class RegionSplit extends AbstractMessage {
    final List<Solution> lower;
    final List<Solution> higher;

    public RegionSplit(int senderId, List<Solution> lower, List<Solution> higher) {
        super(senderId, Messages.Nobody);
        this.lower = lower;
        this.higher = higher;
    }
}

class ExtractData implements Serializable {
    final SolutionAnalyser analyser;
    final String path;

    public ExtractData(SolutionAnalyser analyser, String path) {
        this.analyser = analyser;
        this.path = path;
    }
}

class Terminate implements Serializable {}