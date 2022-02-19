package br.cefetmg.lsi.bimasco.settings;

import java.io.Serializable;
import com.typesafe.config.Config;

import java.util.UUID;

public class RegionSettings implements Serializable {

    private UUID id;
    private UUID simulationId;
    private int minRegions;
    private int maxRegions;
    private int minSolutionsToSplit;

    public RegionSettings (Config config){
        id = UUID.randomUUID();
        minSolutionsToSplit = config.getInt("minSolutionsToSplit");
        minRegions = config.getInt("minRegions");
        maxRegions = config.getInt("maxRegions");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(UUID simulationId) {
        this.simulationId = simulationId;
    }

    public int getMinSolutionsToSplit() {
        return minSolutionsToSplit;
    }

    public void setMinSolutionsToSplit(int minSolutionsToSplit) {
        this.minSolutionsToSplit = minSolutionsToSplit;
    }

    public int getMinRegions() {
        return minRegions;
    }

    public void setMinRegions(int minRegions) {
        this.minRegions = minRegions;
    }

    public int getMaxRegions() {
        return maxRegions;
    }

    public void setMaxRegions(int maxRegions) {
        this.maxRegions = maxRegions;
    }
}
