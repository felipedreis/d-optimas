package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.RegionState;
import br.cefetmg.lsi.bimasco.persistence.SolutionState;
import br.cefetmg.lsi.bimasco.persistence.dao.RegionStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.SolutionStateDAO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RegionBestSolutionExtractor implements Extractor<SolutionState> {

    private String regionName;

    private RegionStateDAO regionStateDAO;

    private SolutionStateDAO solutionStateDAO;

    public RegionBestSolutionExtractor(String regionName, RegionStateDAO regionStateDAO, SolutionStateDAO solutionStateDAO) {
        this.regionName = regionName;
        this.regionStateDAO = regionStateDAO;
        this.solutionStateDAO = solutionStateDAO;
    }

    @Override
    public List<SolutionState> getData() {
        List<RegionState> regionStates = regionStateDAO.findByName(regionName).all();

        List<SolutionState> states = regionStates.stream()
                .map(regionState -> solutionStateDAO.findById(regionState.getBestSolution(), regionName))
                .sorted(Comparator.comparing(SolutionState::getTime))

                .collect(Collectors.toList());

        return states;
    }

    @Override
    public String formatDataToCsv(SolutionState solutionState) {
        return String.format("%d, %f, %s, \"%s\"", solutionState.getTime(), solutionState.getFunctionValue(),
                solutionState.getAgent(), solutionState.getValues());
    }

    @Override
    public String getFileName() {
        return regionName + "-bestSolutionOverTime";
    }
}
