package br.cefetmg.lsi.bimasco.persistence;


import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.List;
import java.util.UUID;

@Entity(defaultKeyspace = "d_optimas")
public class RegionState {

    @PartitionKey
    private UUID id;

    @PartitionKey(value = 1)
    private String name;

    @ClusteringColumn
    private long time;

    @ClusteringColumn(1)
    private UUID bestSolution;

    private double mean;

    private double variance;

    private List<UUID> solutions;

    private long numSolutions;


    public RegionState() {

    }

    public RegionState(String name, long time, UUID bestSolution, double mean, double variance, List<UUID> solutions,
                       long numSolutions) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.time = time;
        this.bestSolution = bestSolution;
        this.mean = mean;
        this.variance = variance;
        this.solutions = solutions;
        this.numSolutions = numSolutions;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public UUID getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(UUID bestSolution) {
        this.bestSolution = bestSolution;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public List<UUID> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<UUID> solutions) {
        this.solutions = solutions;
    }

    public long getNumSolutions() {
        return numSolutions;
    }

    public void setNumSolutions(long numSolutions) {
        this.numSolutions = numSolutions;
    }

    @Override
    public String toString() {
        return "RegionState{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", bestSolution=" + bestSolution +
                ", mean=" + mean +
                ", variance=" + variance +
                ", solutions=" + solutions +
                ", numSolutions=" + numSolutions +
                '}';
    }
}
