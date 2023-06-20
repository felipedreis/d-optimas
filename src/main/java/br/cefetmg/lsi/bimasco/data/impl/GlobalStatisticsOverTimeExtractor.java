package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.GlobalState;
import br.cefetmg.lsi.bimasco.persistence.dao.GlobalStateDAO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalStatisticsOverTimeExtractor extends Extractor<GlobalState> {

    private GlobalStateDAO globalStateDAO;
    public GlobalStatisticsOverTimeExtractor(String problem, GlobalStateDAO globalStateDAO) {
        super(problem);
        this.globalStateDAO = globalStateDAO;
    }

    @Override
    public List<GlobalState> getData() {
        List<GlobalState> globalStates = globalStateDAO.findByProblem(problem).all();
        globalStates.sort(Comparator.comparing(GlobalState::getTime));
        return globalStates;
    }

    @Override
    public String formatDataToCsv(GlobalState globalState) {
        String regionsIds = globalState.getRegionIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        return String.format("%d, %f, %f, %d, \"[%s]\"", globalState.getTime(), globalState.getMean(),
                globalState.getVariance(), globalState.getSolutions(), regionsIds);
    }

    @Override
    public String getFileName() {
        return "globalStatisticsOverTime";
    }
}
