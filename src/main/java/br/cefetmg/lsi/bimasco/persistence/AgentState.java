package br.cefetmg.lsi.bimasco.persistence;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity(defaultKeyspace = "d_optimas")
public class AgentState {
    @PartitionKey
    private UUID id;
    @ClusteringColumn(1)
    private String persistentId;
    @ClusteringColumn
    private String problemName;
    private String algorithmName;
    @ClusteringColumn(2)
    private long time;
    private long timestampStart;
    private long timestampEnd;
    private long functionEvaluations;
    private UUID producedSolution;


    public AgentState(){}

    public AgentState(String problemName,
                      String persistentId,
                      String algorithmName,
                      long time,
                      long timestampStart,
                      long timestampEnd,
                      long functionEvaluations,
                      UUID producedSolution) {
        id = UUID.randomUUID();
        this.persistentId = persistentId;
        this.problemName = problemName;
        this.algorithmName = algorithmName;
        this.time = time;
        this.timestampStart = timestampStart;
        this.timestampEnd = timestampEnd;
        this.functionEvaluations = functionEvaluations;
        this.producedSolution = producedSolution;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(long timestampStart) {
        this.timestampStart = timestampStart;
    }

    public long getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(long timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public UUID getProducedSolution() {
        return producedSolution;
    }

    public void setProducedSolution(UUID producedSolution) {
        this.producedSolution = producedSolution;
    }

    public long getFunctionEvaluations() {
        return functionEvaluations;
    }

    public void setFunctionEvaluations(long functionEvaluations) {
        this.functionEvaluations = functionEvaluations;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    @Override
    public String toString() {
        return "AgentState{" +
                "id=" + id +
                ", persistentId='" + persistentId + '\'' +
                ", algorithmName='" + algorithmName + '\'' +
                ", time=" + time +
                ", timestampStart=" + timestampStart +
                ", timestampEnd=" + timestampEnd +
                ", producedSolution=" + producedSolution +
                '}';
    }
}
