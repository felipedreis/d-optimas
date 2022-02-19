package br.cefetmg.lsi.bimasco.persistence;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity(defaultKeyspace = "d_optimas")
public class GlobalState implements Serializable {

    @PartitionKey
    private UUID id;

    @ClusteringColumn
    private long time;

    private double mean;

    private double variance;

    private long solutions;

    private int regions;

    private List<Integer> regionIds;

    public GlobalState(){

    }

    public GlobalState(long time, double mean, double variance, long solutions, int regions, List<Integer> regionIds) {
        this.id = UUID.randomUUID();
        this.time = time;
        this.mean = mean;
        this.variance = variance;
        this.solutions = solutions;
        this.regions = regions;
        this.regionIds = regionIds;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public long getSolutions() {
        return solutions;
    }

    public void setSolutions(long solutions) {
        this.solutions = solutions;
    }

    public int getRegions() {
        return regions;
    }

    public void setRegions(int regions) {
        this.regions = regions;
    }

    public List<Integer> getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(List<Integer> regionIds) {
        this.regionIds = regionIds;
    }

    @Override
    public String toString() {
        return "GlobalState{" +
                "id=" + id +
                ", time=" + time +
                ", mean=" + mean +
                ", variance=" + variance +
                ", solutions=" + solutions +
                ", regions=" + regions +
                ", regionIds=" + regionIds +
                '}';
    }
}
