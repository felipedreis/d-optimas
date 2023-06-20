package br.cefetmg.lsi.bimasco.persistence;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.List;
import java.util.UUID;

@Entity(defaultKeyspace = "d_optimas")
public class SolutionState {

    @PartitionKey
    private UUID id;

    private String problemName;

    @ClusteringColumn(value = 1)
    private String agent;

    @ClusteringColumn(value = 2)
    private String region;

    @ClusteringColumn
    private long time;

    private List<Double> values;

    private double functionValue;

    public SolutionState(){

    }

    public SolutionState(UUID id,
                         String problemName,
                         String agent,
                         String region,
                         long time,
                         List<Double> values,
                         double functionValue) {
        this.id = id;
        this.problemName = problemName;
        this.agent = agent;
        this.region = region;
        this.time = time;
        this.values = values;
        this.functionValue = functionValue;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public double getFunctionValue() {
        return functionValue;
    }

    public void setFunctionValue(double functionValue) {
        this.functionValue = functionValue;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    @Override
    public String toString() {
        return "SolutionState{" +
                "id=" + id +
                ", agent='" + agent + '\'' +
                ", region='" + region + '\'' +
                ", time=" + time +
                ", values=" + values +
                ", functionValue=" + functionValue +
                '}';
    }
}
