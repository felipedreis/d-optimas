package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.SolutionState;
import br.cefetmg.lsi.bimasco.persistence.dao.SolutionStateDAO;

import java.util.*;
import java.util.stream.Collectors;

public class BestGlobalSolutionOverTimeExtractor extends Extractor<SolutionState> {

    private SolutionStateDAO solutionStateDAO;

    public BestGlobalSolutionOverTimeExtractor(String problem, SolutionStateDAO solutionStateDAO) {
        super(problem);
        this.solutionStateDAO = solutionStateDAO;
    }

    @Override
    public List<SolutionState> getData() {

        List<SolutionState> solutionStates = solutionStateDAO.findByProblem(problem).all();
        Map<Long, Optional<SolutionState>> filtered = solutionStates.parallelStream()
                .collect(Collectors.groupingBy(SolutionState::getTime,
                        Collectors.minBy(Comparator.comparing(SolutionState::getFunctionValue))));

        return filtered.values().stream()
                .map(solutionState -> solutionState.orElse(null))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(SolutionState::getTime))
                .collect(Collectors.toList());
    }

    @Override
    public String formatDataToCsv(SolutionState solutionState) {
        return String.format("%d, %f, %s, \"%s\"", solutionState.getTime(), solutionState.getFunctionValue(),
                solutionState.getAgent(), solutionState.getValues());
    }

    @Override
    public String getFileName() {
        return "globalBestSolutionOverTime";
    }
}
