package br.cefetmg.lsi.bimasco.core.heuristics.localSearch;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifier;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifierHelper;
import br.cefetmg.lsi.bimasco.core.solutions.neighborsList.NeighborsList;
import br.cefetmg.lsi.bimasco.core.solutions.neighborsList.NeighborsListHelper;

import java.util.ArrayList;
import java.util.Map;

//TODO: Change elements name
public class BetterNeighborhoodLocalSearch extends LocalSearch {

    private Solution bestSolution;

    public BetterNeighborhoodLocalSearch(Problem problem, Map<String, Object> parameters) {
        super(problem, parameters);
    }

    @Override
    public Solution search(Solution solution, Context context) {

        Solution currentSolution;
        Solution auxiliarySolution;

        SolutionAnalyser solutionAnalyser;
        SolutionModifier solutionModifier;
        NeighborsList neighborsList;

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        solutionModifier = SolutionModifierHelper.buildModifiesSolution(problem.getProblemSettings().getType() + neighbor, this.problem);


        bestSolution = Solution.buildSolution(problem);

        neighborsList = NeighborsListHelper.buildNeighborsList(problem.getProblemSettings().getType() + neighbor);

        // o procedimento começa aqui...
        boolean otimoLocal = true;
        ArrayList<ArrayList<Object>> neighborsVector = new ArrayList<ArrayList<Object>>();
        Object sV = null;
        Object mS = null;

        bestSolution = (Solution) solution.clone();
        mS = bestSolution.evaluate(context);
        currentSolution = (Solution) solution.clone();

        while (otimoLocal) {
            otimoLocal = false;
            currentSolution = (Solution) bestSolution.clone();
            //neighborsVector = neighborsList.getNeighborsList(currentSolution, neighbor);

            for (ArrayList<Object> neighbors : neighborsVector) {
                Solution neighborsSolution = solutionModifier.modify(currentSolution, neighbors, null, 0);


                if (neighborsSolution.isViable(context)) {
                    bestSolution = solutionAnalyser.getBestSolution(neighborsSolution, bestSolution);
                }
            }
        }

        return bestSolution;
    }
}
