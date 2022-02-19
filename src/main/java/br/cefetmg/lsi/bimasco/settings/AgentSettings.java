package br.cefetmg.lsi.bimasco.settings;

import com.typesafe.config.Config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO: Think about keep lifeTime configuration
public class AgentSettings implements Serializable {

    private UUID simulationId;
    private UUID id;
    private int count;
    public String name;
    private String metaHeuristicName;
    private long lifetime;

    private double memoryTax;

    private Boolean isPopulationMetaHeuristic;
    private Boolean isConstructorMetaHeuristic;

    private Map<String, Object> metaHeuristicParameters;
    private Map<String, SolutionManipulation> solutionManipulation;

    public AgentSettings() {
        id = UUID.randomUUID();
    }

    public AgentSettings(Config config) {
        id = UUID.randomUUID();
        count = config.getInt("count");
        name = config.getString("name");
        metaHeuristicName = config.getString("metaHeuristicName");
        isPopulationMetaHeuristic = config.getBoolean("isPopulationMetaHeuristic");
        isConstructorMetaHeuristic = config.getBoolean("isConstructorMetaHeuristic");
        lifetime = config.hasPath("lifetime") ? config.getLong("lifetime") : Long.MAX_VALUE;
        memoryTax = config.hasPath("memoryTax") ? config.getDouble("memoryTax") : 0;

        metaHeuristicParameters =  new HashMap<>();
        Config  metaHeuristicConfig = config.getConfig("metaHeuristicParameters");
        Set<String> keys = metaHeuristicConfig.entrySet().stream()
                .map(entry -> entry.getKey().split("\\.")[0])
                .collect(Collectors.toSet());

        for (String key : keys){
            Object value = metaHeuristicConfig.getAnyRef(key);
            metaHeuristicParameters.put(key, value);
        }

        solutionManipulation =  new HashMap<>();

        if (config.hasPath("solutionManipulation")) {
            Config solutionManipulationConfig = config.getConfig("solutionManipulation");
            keys = solutionManipulationConfig.entrySet().stream()
                    .map(entry -> entry.getKey().split("\\.")[0])
                    .collect(Collectors.toSet());

            for (String key : keys) {
                String type = solutionManipulationConfig.getString(key);
                solutionManipulation.put(key, new SolutionManipulation(type, true));
            }
        }
    }

    public UUID getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(UUID simulationId) {
        this.simulationId = simulationId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetaHeuristicName() {
        return metaHeuristicName;
    }

    public void setMetaHeuristicName(String metaHeuristicName) {
        this.metaHeuristicName = metaHeuristicName;
    }

    public long getLifetime() {
        return lifetime;
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }

    public double getMemoryTax() {
        return memoryTax;
    }

    public void setMemoryTax(double memoryTax) {
        this.memoryTax = memoryTax;
    }

    public Boolean getPopulationMetaHeuristic() {
        return isPopulationMetaHeuristic;
    }

    public void setPopulationMetaHeuristic(Boolean populationMetaHeuristic) {
        isPopulationMetaHeuristic = populationMetaHeuristic;
    }

    public Boolean getConstructorMetaHeuristic() {
        return isConstructorMetaHeuristic;
    }

    public void setConstructorMetaHeuristic(Boolean constructorMetaHeuristic) {
        isConstructorMetaHeuristic = constructorMetaHeuristic;
    }

    public Map<String, Object> getMetaHeuristicParameters() {
        return metaHeuristicParameters;
    }

    public void setMetaHeuristicParameters(Map<String, Object> metaHeuristicParameters) {
        this.metaHeuristicParameters = metaHeuristicParameters;
    }

    public Map<String, SolutionManipulation> getSolutionManipulation() {
        return solutionManipulation;
    }

    public void setSolutionManipulation(Map<String, SolutionManipulation> solutionManipulation) {
        this.solutionManipulation = solutionManipulation;
    }
}
