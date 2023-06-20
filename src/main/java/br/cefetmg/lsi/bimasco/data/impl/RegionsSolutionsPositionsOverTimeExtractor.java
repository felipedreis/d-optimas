package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.RegionState;
import br.cefetmg.lsi.bimasco.persistence.SolutionState;
import br.cefetmg.lsi.bimasco.persistence.dao.RegionStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.SolutionStateDAO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegionsSolutionsPositionsOverTimeExtractor extends Extractor<Pair<Long, List<SolutionState>>> {

    private String regionName;
    private RegionStateDAO regionStateDAO;
    private SolutionStateDAO solutionStateDAO;

    public RegionsSolutionsPositionsOverTimeExtractor(String problem,
                                                      String regionName,
                                                      RegionStateDAO regionStateDAO,
                                                      SolutionStateDAO solutionStateDAO) {
        super(problem);
        this.regionName = regionName;
        this.regionStateDAO = regionStateDAO;
        this.solutionStateDAO = solutionStateDAO;
    }

    @Override
    public List<Pair<Long, List<SolutionState>>> getData() {
        List<RegionState> regionStates = regionStateDAO.findByProblemAndName(problem, regionName).all();
        return regionStates.stream()
                .sorted(Comparator.comparing(RegionState::getTime))
                .map(regionState -> {
                    List<SolutionState> solutionStates = solutionStateDAO.findByIds(regionState.getSolutions()).all();
                    return Pair.of(regionState.getTime(), solutionStates);
                })
                .collect(Collectors.toList());
    }

    @Override
    public String formatDataToCsv(Pair<Long, List<SolutionState>> regionStateListPair) {
        String solutions = regionStateListPair.getValue()
                .stream().map(SolutionState::getValues)
                .map(Objects::toString)
                .collect(Collectors.joining(","));
        return String.format("%d, \"[%s]\"", regionStateListPair.getKey(), solutions);
    }

    @Override
    public String getFileName() {
        return regionName + "-solutionPositions";
    }
}
