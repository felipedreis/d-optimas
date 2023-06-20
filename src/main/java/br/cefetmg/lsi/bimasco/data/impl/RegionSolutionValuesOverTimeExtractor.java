package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.RegionState;
import br.cefetmg.lsi.bimasco.persistence.SolutionState;
import br.cefetmg.lsi.bimasco.persistence.dao.RegionStateDAO;
import br.cefetmg.lsi.bimasco.persistence.dao.SolutionStateDAO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegionSolutionValuesOverTimeExtractor extends Extractor <Pair<Long,List<SolutionState>>> {
    private String regionName;
    private RegionStateDAO regionStateDAO;
    private SolutionStateDAO solutionStateDAO;

    public RegionSolutionValuesOverTimeExtractor(String problem,
                                                 String regionName,
                                                 RegionStateDAO regionStateDAO,
                                                 SolutionStateDAO solutionStateDAO) {
        super(problem);
        this.regionName = regionName;
        this.regionStateDAO = regionStateDAO;
        this.solutionStateDAO = solutionStateDAO;
    }

    @Override
    public List<Pair<Long,List<SolutionState>>> getData() {
        List<RegionState> regionStates = regionStateDAO.findByProblemAndName(problem, regionName).all();

        List<Pair<Long, List<SolutionState>>> solutionsOverTime = new ArrayList<>();
        for (RegionState regionState : regionStates) {
            List<SolutionState> solutions = solutionStateDAO.findByIds(regionState.getSolutions()).all();
            solutionsOverTime.add(Pair.of(regionState.getTime(), solutions));
        }

        return solutionsOverTime;
    }

    @Override
    public String formatDataToCsv(Pair<Long,List<SolutionState>> solutionState) {

        String values = solutionState.getValue().stream()
                .map(SolutionState::getFunctionValue)
                .map(Objects::toString)
                .collect(Collectors.joining(", "));


        return String.format("%d, \"%s\"", solutionState.getKey(), values);
    }

    @Override
    public String getFileName() {
        return regionName + "-solutionValuesOverTime";
    }
}
