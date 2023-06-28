package br.cefetmg.lsi.bimasco.actors;

import akka.actor.ActorRef;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimulationState {

    public final String problemName;

    public final long time;

    public final SimulationSettings settings;

    public final List<ActorRef> agents;

    public final List<ActorRef> regions;

    public final Map<String, StatisticalSummary> regionStats;

    public final StatisticalSummary globalStats;

    public SimulationState(String problemName,
                           long time,
                           SimulationSettings settings,
                           StatisticalSummary globalStats,
                           List<ActorRef> agents,
                           List<ActorRef> regions,
                           Map<String, StatisticalSummary> regionStats) {
        this. problemName = problemName;
        this.time = time;
        this.settings = settings;
        this.agents = agents;
        this.regions = regions;
        this.regionStats = regionStats;
        this.globalStats = globalStats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulationState state = (SimulationState) o;
        return time == state.time && Objects.equals(problemName, state.problemName) && Objects.equals(agents, state.agents) && Objects.equals(regions, state.regions) && Objects.equals(regionStats, state.regionStats) && Objects.equals(globalStats, state.globalStats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(problemName, time, agents, regions, regionStats, globalStats);
    }

    @Override
    public String toString() {
        return "SimulationState{" +
                "problemName='" + problemName + '\'' +
                ", time=" + time +
                ", agents=" + agents +
                ", regions=" + regions +
                ", regionStats=" + regionStats +
                ", globalStats=" + globalStats +
                '}';
    }
}
