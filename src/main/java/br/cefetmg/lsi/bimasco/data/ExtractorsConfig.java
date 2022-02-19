package br.cefetmg.lsi.bimasco.data;

import br.cefetmg.lsi.bimasco.data.impl.*;
import br.cefetmg.lsi.bimasco.persistence.AgentState;
import br.cefetmg.lsi.bimasco.persistence.RegionState;
import br.cefetmg.lsi.bimasco.persistence.dao.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtractorsConfig {

    private AgentStateDAO agentStateDAO;
    private RegionStateDAO regionStateDAO;
    private SolutionStateDAO solutionStateDAO;
    private GlobalStateDAO globalStateDAO;
    private MessageStateDAO messageStateDAO;
    private MemoryStateDAO memoryStateDAO;

    public ExtractorsConfig(AgentStateDAO agentStateDAO, RegionStateDAO regionStateDAO,
                            SolutionStateDAO solutionStateDAO, GlobalStateDAO globalStateDAO,
                            MessageStateDAO messageStateDAO, MemoryStateDAO memoryStateDAO) {
        this.agentStateDAO = agentStateDAO;
        this.regionStateDAO = regionStateDAO;
        this.solutionStateDAO = solutionStateDAO;
        this.globalStateDAO = globalStateDAO;
        this.messageStateDAO = messageStateDAO;
        this.memoryStateDAO = memoryStateDAO;
    }

    public List<Extractor<?>> extractors() {
        ArrayList<Extractor<?>> extractors = new ArrayList<>() {{
            addAll(agentSolutionsOverTimeExtractors());
            add(bestSolutionOverTimeExtractor());
            add(globalStatesOverTime());
            addAll(regionBestSolutionExtractors());
            add(mergeSplitOverTime());
            addAll(regionSolutionValuesOvertimeExtractors());
            addAll(regionSolutionPositionsOverTime());
            addAll(agentMemoryProbabilitiesOverTime());
            add(functionEvaluationsOverTime());
        }};

        return extractors;
    }

    public List<Extractor<?>> agentSolutionsOverTimeExtractors() {
        Set<String> agentNames = getAgentNames();

        return agentNames.stream()
                .map(agent -> new AgentSolutionsOverTimeExtractor(agent, agentStateDAO, solutionStateDAO))
                .collect(Collectors.toList());
    }

    public Extractor<?> bestSolutionOverTimeExtractor() {
        return new BestGlobalSolutionOverTimeExtractor(solutionStateDAO);
    }

    public Extractor<?> globalStatesOverTime() {
        return new GlobalStatisticsOverTimeExtractor(globalStateDAO);
    }

    public Extractor<?> mergeSplitOverTime() {
        return new MergeSplitOverTimeExtractor(messageStateDAO);
    }

    public List<Extractor<?>> regionBestSolutionExtractors() {
        Set<String> regionNames = getRegionNames();

        return regionNames.stream()
                .map(region -> new RegionBestSolutionExtractor(region, regionStateDAO, solutionStateDAO))
                .collect(Collectors.toList());
    }

    public List<Extractor<?>> regionSolutionValuesOvertimeExtractors() {
        Set<String> regionNames = getRegionNames();
        return regionNames.stream()
                .map(region -> new RegionSolutionValuesOverTimeExtractor(region, regionStateDAO, solutionStateDAO))
                .collect(Collectors.toList());
    }

    public List<Extractor<?>> regionSolutionPositionsOverTime() {
        Set<String> regionNames = getRegionNames();

        return regionNames.stream()
                .map(region -> new RegionsSolutionsPositionsOverTimeExtractor(region, regionStateDAO, solutionStateDAO))
                .collect(Collectors.toList());
    }

    public List<Extractor<?>> agentMemoryProbabilitiesOverTime() {
        Set<String> agentNames = getAgentNames();

        return agentNames.stream()
                .map(agent -> new AgentMemoryProbabilitiesOverTime(memoryStateDAO, agent))
                .collect(Collectors.toList());
    }
    public Extractor<?> functionEvaluationsOverTime() {
        return new FunctionEvaluationsOverTime(agentStateDAO);
    }

    private Set<String> getRegionNames() {

        return regionStateDAO.findAll().all()
                .stream()
                .map(RegionState::getName)
                .collect(Collectors.toSet());
    }

    private Set<String> getAgentNames() {
        return agentStateDAO.findAll().all()
                .stream()
                .map(AgentState::getPersistentId)
                .collect(Collectors.toSet());
    }
}
