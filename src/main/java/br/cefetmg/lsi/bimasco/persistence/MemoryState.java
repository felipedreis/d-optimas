package br.cefetmg.lsi.bimasco.persistence;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class MemoryState {

    public static final String MEMORY_STATUS_UPDATED = "UPDATED";
    public static final String MEMORY_STATUS_USED = "USED";

    @PartitionKey
    private UUID id;

    @ClusteringColumn
    private long time;

    @ClusteringColumn(1)
    private String agent;

    private List<Integer> regions;

    private List<Double> probabilities;

    private UUID bestSolutionId;

    private String memoryStatus;

    private Integer chosenRegion;

    public MemoryState(){}

    public MemoryState(String agent, List<Integer> regions, List<Double> probabilities, UUID bestSolutionId) {
        id = UUID.randomUUID();
        this.agent = agent;
        this.regions = regions;
        this.probabilities = probabilities;
        this.bestSolutionId = bestSolutionId;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public List<Integer> getRegions() {
        return regions;
    }

    public void setRegions(List<Integer> regions) {
        this.regions = regions;
    }

    public List<Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(List<Double> probabilities) {
        this.probabilities = probabilities;
    }

    public UUID getBestSolutionId() {
        return bestSolutionId;
    }

    public void setBestSolutionId(UUID bestSolutionId) {
        this.bestSolutionId = bestSolutionId;
    }

    public String getMemoryStatus() {
        return memoryStatus;
    }

    public void setMemoryStatus(String memoryStatus) {
        this.memoryStatus = memoryStatus;
    }

    public Integer getChosenRegion() {
        return chosenRegion;
    }

    public void setChosenRegion(Integer chosenRegion) {
        this.chosenRegion = chosenRegion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryState that = (MemoryState) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MemoryState{" +
                "id=" + id +
                ", time=" + time +
                ", agent='" + agent + '\'' +
                ", regions=" + regions +
                ", probabilities=" + probabilities +
                ", bestSolutionId=" + bestSolutionId +
                ", memoryStatus=" + memoryStatus +
                '}';
    }
}
