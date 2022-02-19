package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.AgentState;
import br.cefetmg.lsi.bimasco.persistence.SolutionState;
import br.cefetmg.lsi.bimasco.persistence.dao.AgentStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.SolutionStateDAO;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AgentSolutionsOverTimeExtractor implements Extractor<SolutionState> {

    private String agentName;

    private AgentStateDAO agentStateDAO;

    private SolutionStateDAO solutionStateDAO;

    public AgentSolutionsOverTimeExtractor(String agentName, AgentStateDAO agentStateDAO, SolutionStateDAO solutionStateDAO) {
        this.agentName = agentName;
        this.agentStateDAO = agentStateDAO;
        this.solutionStateDAO = solutionStateDAO;
    }

    @Override
    public List<SolutionState> getData() {

        List<AgentState> agentStates = agentStateDAO.findByPersistentId(agentName).all();

        List<UUID> ids = agentStates.stream().map(AgentState::getProducedSolution)
                .collect(Collectors.toList());
        List<SolutionState> solutionStates = solutionStateDAO.findByIds(ids).all();
        solutionStates.sort(Comparator.comparing(SolutionState::getTime));
        return solutionStates;
    }

    @Override
    public String formatDataToCsv(SolutionState solutionState) {
        return String.format("%d, %f, %s, \"%s\"", solutionState.getTime(), solutionState.getFunctionValue(),
                solutionState.getAgent(), solutionState.getValues());
    }

    @Override
    public String getFileName() {
        return agentName + "-solutionsOverTime";
    }
}
