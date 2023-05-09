package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopCondition;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopConditionHelper;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.ModifiesSolutionCollections;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.ModifiesSolutionCollectionsHelper;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class DE extends MetaHeuristic {

    List<Solution> population;
    StopCondition stopCondition;
    RandomDataGenerator rdg;
    SolutionAnalyser solutionAnalyser;

    private double probC;       // cross probability
    private double scaleFactor; // F

    private long startTime;
    private long endTime;
    private Solution bestSolution;

    private ModifiesSolutionCollections deSum;

    private int populationSize;

    private static final Logger logger = Logger.getLogger(DE.class);

    public DE(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {
        metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        stopCondition = StopConditionHelper.buildStopCondition(metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY).toString(),
                problem);
        populationSize = (Integer) metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY);
        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        probC = (Double) metaHeuristicParameters.getOrDefault("C", 0);
        scaleFactor = (Double) metaHeuristicParameters.getOrDefault("F", 0);
        deSum = ModifiesSolutionCollectionsHelper
                .buildModifiesSolutionCollection(metaHeuristicParameters
                                .get(DefaultMetaHeuristicParametersKeySupported.DE_SUM).toString(), problem);
        rdg = new RandomDataGenerator();
    }

    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {
        int popSize = externalSolution.size();
        population = externalSolution;
        Solution r1, r2, r3;
        Object f0 = problem.getLimit();

        int iteration = 0, iterationWI = 0;
        long time = 0;
        bestSolution = population.get(0);

        getStopWatch().reset();
        getStopWatch().start();

        while (!stopCondition.isSatisfied(f0, getStopWatch().getTime(), iteration, iterationWI, metaHeuristicParameters)){
            iteration++;
            iterationWI++;

            logger.debug(format("Iteration %d", iteration));
            List<Solution> offspring = new ArrayList<>();

            for (int i = 0; i < popSize; ++i) {
                logger.debug(format("Constructing offspring %d", i));

                Object [] sample = rdg.nextSample(population, 3);

                r1 = (Solution) sample[0];
                r2 = (Solution) sample[1];
                r3 = (Solution) sample[2];

                logger.debug(format("Chosen solutions: \nr1: %s\nr2: %s\nr3: %s\n", r1, r2, r3));

                Solution solution = deSum(population.get(i), r1, r2, r3);

                solution.evaluate(context);
                offspring.add(solution);
                logger.debug(format("Produced solution: %s", solution));
            }

            List<Solution> nextPopulation = new ArrayList<>();
            logger.debug("Selecting vectors for the next population");
            for (int i = 0; i < popSize; ++i) {
                Solution offspringSolution = offspring.get(i);
                Solution nextSolution;
                if (!offspringSolution.isViable(context)) {
                    nextSolution = population.get(i);
                    logger.debug(format("Offspring %s not viable, selecting %s", offspringSolution, nextSolution));
                } else if (solutionAnalyser.compare(offspring.get(i), population.get(i)) < 0) {
                    logger.debug(format("Selecting %d from offspring population", i));
                    nextSolution = offspring.get(i);
                } else {
                    logger.debug(format("Selecting %d from previous population", i));
                    nextSolution = population.get(i);
                }

                if (solutionAnalyser.compare(nextSolution, bestSolution) < 0) {
                    f0 = nextSolution.evaluate(context);
                    bestSolution = nextSolution;
                    iterationWI = 0;
                }

                nextPopulation.add(nextSolution);
            }
            logger.debug(format("Iteration %d best solution: %s", iteration, bestSolution));

            population = nextPopulation;
        }

        getStopWatch().stop();

        return List.of(bestSolution);
    }

    private Solution deSum (Solution s, Solution r1, Solution r2, Solution r3) {
        List<Solution> result = deSum.modify(List.of(s, r1, r2, r3), Map.of("probC", probC, "scaleFactor", scaleFactor), 1);
        return result.get(0);
    }


    @Override
    public List<Solution> getPartialSolutions() {
        return null;
    }

    public int getPopulationSize() {
        return populationSize;
    }
}
