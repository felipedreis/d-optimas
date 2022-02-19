package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;

import java.util.List;

public class NSGA2 extends MetaHeuristic {

    private double crossoverProbability;

    private double mutationProbability;


    public NSGA2(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {

    }

    @Override
    public List<Solution> runMetaHeuristic(List<Solution> solutions, Context context){
        return null;
    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
    }
}
