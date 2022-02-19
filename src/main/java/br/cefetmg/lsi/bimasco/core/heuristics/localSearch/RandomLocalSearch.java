package br.cefetmg.lsi.bimasco.core.heuristics.localSearch;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import org.apache.log4j.Logger;

import java.util.Map;

import static java.lang.String.format;

//TODO: Change elements name

public class RandomLocalSearch extends LocalSearch {

    private SolutionAnalyser solutionAnalyser;

    private Integer iterationsLocalSearchCount = 0;

    private static Logger logger = Logger.getLogger(RandomLocalSearch.class);

    public RandomLocalSearch(Problem problem, Map<String, Object> parameters){
        super(problem, parameters);
        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        iterationsLocalSearchCount = (Integer)
                parameters.getOrDefault(DefaultMetaHeuristicParametersKeySupported.LOCAL_SEARCH_ITERATIONS_KEY, 0);

    }

    @Override
    public Solution search(Solution solution, Context context) {
        logger.debug(format("Starting local search with neighbor %s", neighbor));

        Solution currentSolution, neighborSolution;

        Solution bestSolution;
        logger.debug(format("Using solution modifier %s", problem.getProblemSettings().getType() + neighbor));

        int iteracoes = 0;

        bestSolution = (Solution) solution.clone();
        bestSolution.evaluate(context);
        currentSolution = (Solution) bestSolution.clone();

        while (iteracoes < iterationsLocalSearchCount){
            logger.debug(format("Local search iteration %d", iteracoes));
            logger.debug(format("Current solution: %s", currentSolution));

            neighborSolution = neighbor.modify(currentSolution, null, null, 1);

            logger.debug(format("Neighbor found %s", neighborSolution.toString()));

            neighborSolution.evaluate(context);

            if (neighborSolution.isViable(context)) {
                bestSolution = solutionAnalyser.getBestSolution(neighborSolution, bestSolution);
                currentSolution = bestSolution;
            }

            iteracoes++;
        }

        return bestSolution;
    }
}
