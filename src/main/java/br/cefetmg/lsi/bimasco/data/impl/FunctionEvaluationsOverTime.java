package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.AgentState;
import br.cefetmg.lsi.bimasco.persistence.dao.AgentStateDAO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionEvaluationsOverTime implements Extractor<AgentState> {

    private AgentStateDAO agentStateDAO;

    public FunctionEvaluationsOverTime(AgentStateDAO agentStateDAO) {
        this.agentStateDAO = agentStateDAO;
    }

    @Override
    public List<AgentState> getData() {
        List<AgentState> agentStates = agentStateDAO.findAll().all();
        return agentStates.stream()
                .sorted(Comparator.comparing(AgentState::getTime))
                .collect(Collectors.toList());
    }

    @Override
    public String formatDataToCsv(AgentState agentState) {
        return String.format("%d, %s, %d", agentState.getTime(), agentState.getPersistentId(), agentState.getFunctionEvaluations());
    }

    @Override
    public String getFileName() {
        return "functionEvaluationsOverTime";
    }
}
