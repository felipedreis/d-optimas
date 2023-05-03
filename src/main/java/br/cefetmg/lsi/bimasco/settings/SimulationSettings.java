package br.cefetmg.lsi.bimasco.settings;

import com.typesafe.config.Config;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationSettings implements Serializable {

    private final String name;

    private final Boolean hasCooperation;

    private final long executionTime;

    private final int nodes;

    private final ProblemSettings problem;

    private final RegionSettings region;

    private final List<AgentSettings> agents;

    private final int numberOfAgents;

    private final String extractPath;

    private final boolean benchmark;

    public SimulationSettings(Config config) {
        config = config.getConfig("simulation");

        name = config.getString("name");
        hasCooperation = config.getBoolean("hasCooperation");

        problem = new ProblemSettings(config.getConfig("problem"));
        region = new RegionSettings(config.getConfig("region"));
        agents = config.getConfigList("agents").stream()
                .map(AgentSettings::new)
                .collect(Collectors.toList());

        numberOfAgents = agents.stream().map(AgentSettings::getCount)
                .reduce(0, Integer::sum);

        executionTime = config.getLong("executionTime");
        extractPath = config.getString("extractPath");
        nodes = config.getInt("nodes");

        benchmark = config.hasPath("benchmark") ? config.getBoolean("benchmark") : false ;
    }

    public String getName() {
        return name;
    }

    public Boolean getHasCooperation() {
        return hasCooperation;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public ProblemSettings getProblem() {
        return problem;
    }

    public RegionSettings getRegion() {
        return region;
    }

    public List<AgentSettings> getAgents() {
        return agents;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public int getNodes() {
        return nodes;
    }

    public String getExtractPath() {
        return extractPath;
    }

    public boolean isBenchmark() {
        return benchmark;
    }
}
