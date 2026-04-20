package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopCondition;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopConditionHelper;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifier;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifierHelper;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.ModifiesSolutionCollections;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.ModifiesSolutionCollectionsHelper;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.SolutionsCollectionUtils;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.core.utils.SolutionManipulationKeys;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.SolutionManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.lang.String.format;

public class GA extends MetaHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(GA.class);
    public static int C_TIME_DIVISOR = 1000;

    private List<Solution> population;
    private List<Solution> nextPopulationPool;
    private Solution bestIterationSolution;
    private String parentsChoice;
    private String crossover;
    private String mutationChoice;
    private String nextPopulationChoice;
    private String initialSolutionChoice;
    private SolutionAnalyser solutionAnalyser;
    private Integer maxIterations;
    private Integer populationSize;
    private Integer numParents;
    private Double mutationTax;
    private Double crossoverTax;

    private String stopConditionName;
    private double initialTimeMs;
    private double finalTimeMs;
    private double totalTime;
    private Object targetFitness;
    private StopCondition stopCondition;
    private ModifiesSolutionCollections initialSolutionModifier;
    private ModifiesSolutionCollections selection;
    private ModifiesSolutionCollections reproduction;
    private ModifiesSolutionCollections nextPopulationSelector;
    private SolutionModifier mutation;


    public GA(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {
        // This information is passed interactively through the graphical interface (BIMASCO)
        Solution auxiliarySolution;

        population = new ArrayList<>();
        nextPopulationPool = new ArrayList<>();

        metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        maxIterations = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, null);

        populationSize = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY, null);

        numParents = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.PARENTS_SIZE_KEY, null);

        stopConditionName = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, null);

        crossoverTax = (Double) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.CROSSOVER_TAX_KEY, null);


        mutationTax = (Double) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MUTATION_TAX_KEY, null);


        parentsChoice = agentSettings.getSolutionManipulation()
                .getOrDefault(SolutionManipulationKeys.PARENTS_CHOICE_KEY, new SolutionManipulation("", true)).getType();

        crossover = agentSettings.getSolutionManipulation()
                .getOrDefault(SolutionManipulationKeys.CROSSOVER_CHOICE_KEY, new SolutionManipulation("", true)).getType();
        mutationChoice = agentSettings.getSolutionManipulation().
                getOrDefault(SolutionManipulationKeys.MUTATION_CHOICE_KEY, new SolutionManipulation("", true)).getType();

        if (agentSettings.getSolutionManipulation().containsKey(SolutionManipulationKeys.NEXT_POPULATION_CHOICE_KEY)) {
            nextPopulationChoice = agentSettings.getSolutionManipulation()
                    .get(SolutionManipulationKeys.NEXT_POPULATION_CHOICE_KEY).getType();
        } else {
            nextPopulationChoice = agentSettings.getSolutionManipulation()
                    .getOrDefault(SolutionManipulationKeys.CHOOSE_INITIAL_SOLUTION_KEY, new SolutionManipulation("", false)).getType();
        }

        initialSolutionChoice = agentSettings.getSolutionManipulation()
                .get(SolutionManipulationKeys.CHOOSE_INITIAL_SOLUTION_KEY).getType();

        targetFitness = problem.getLimit();

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);

        bestIterationSolution =  Solution.buildSolution(problem);

        stopCondition = StopConditionHelper.buildStopCondition(stopConditionName, problem);

        initialSolutionModifier = ModifiesSolutionCollectionsHelper.buildModifiesSolutionCollection(initialSolutionChoice, problem);
        selection = ModifiesSolutionCollectionsHelper.buildModifiesSolutionCollection(parentsChoice, problem);
        mutation = SolutionModifierHelper.buildModifiesSolution(mutationChoice, problem, metaHeuristicParameters);
        reproduction =
                ModifiesSolutionCollectionsHelper.buildModifiesSolutionCollection(crossover, problem);
        nextPopulationSelector = ModifiesSolutionCollectionsHelper.buildModifiesSolutionCollection(nextPopulationChoice, problem);

        totalTime = 0;
        finalTimeMs = 0;
        initialTimeMs = 0;
    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
    }


    @SuppressWarnings("empty-statement")
    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, final Context context) {
        if (externalSolution == null) {
            logger.debug("Cannot process an empty list");
            return Collections.emptyList();
        }

        if (externalSolution.size() < populationSize) {
            logger.debug(format("Cannot process a list size of %d, expected population size is ", externalSolution.size(), populationSize));
            return Collections.emptyList();
        }

        double initialTime;
        double finalTime;
        double end;

        initialTime = System.currentTimeMillis();
        logger.debug(format("Starting AG execution at %f", initialTime));
        logger.debug(format("Initial population: %s", externalSolution));

        population = SolutionsCollectionUtils.copyValues(externalSolution, problem);

        bestIterationSolution = population.get(0);
        bestIterationSolution.evaluate(context);

        for (Solution solution : population) {
            solution.evaluate(context);
            bestIterationSolution = solutionAnalyser.getBestSolution(bestIterationSolution, solution);
        }

        geneticAlgorithm(context);

        finalTime = System.currentTimeMillis();
        end = ((finalTime - initialTime) / C_TIME_DIVISOR);

        System.gc();
        logger.debug(format("Stopping AG execution at %f with duration of %f", finalTime, end));
        logger.debug(format("Best solution found %s", bestIterationSolution));

        return List.of(bestIterationSolution);
    }

    private void geneticAlgorithm(Context context) {

        Object f0 = targetFitness;

        List<Solution> parents;
        List<Solution> offsprings;
        Random rand = new Random();

        int offspringCounter;
        double crossoverChance;
        double mutationChance;
        int iterations = 0;
        int iterationsWithoutImprovement = 0;


        getStopWatch().reset();
        getStopWatch().start();

        while (!stopCondition.isSatisfied(f0, stopWatch.getTime(), iterations, iterationsWithoutImprovement, metaHeuristicParameters)) {
            logger.debug(format("Iteration %d", iterations));
            nextPopulationPool = new ArrayList<>();
            offspringCounter = 0;

            while (offspringCounter < populationSize) {
                parents = SolutionsCollectionUtils.copyValues(parentsChoice(), problem);
                crossoverChance = rand.nextDouble();

                logger.debug(format("Fathers chosen %s", parents));

                if (crossoverChance < crossoverTax) {
                    offsprings = SolutionsCollectionUtils.copyValues(crossover(parents), problem);
                    logger.debug(format("Offsprings %s", offsprings));

                    for (Solution offspring : offsprings) {
                        mutationChance = rand.nextDouble();

                        Solution mutated;

                        if (mutationChance < mutationTax) {
                            mutated = mutation(offspring);
                        } else {
                            mutated = offspring;
                        }

                        mutated.evaluate(context);
                        logger.debug(format("Mutated offspring: %s", mutated));

                        if (mutated.isViable(context)) {
                            nextPopulationPool.add(mutated);
                            offspringCounter++;
                        }
                    }
                }
            }
            Optional<Solution> bestSolutionOptional = solutionAnalyser.findBestSolution(nextPopulationPool);

            if (bestSolutionOptional.isPresent()) {
                bestIterationSolution = bestSolutionOptional.get();
                iterationsWithoutImprovement = -1;
            }

            population.clear();
            population = SolutionsCollectionUtils.copyValues(nextPopulation(nextPopulationPool), problem);
            logger.debug(format("Next population: %s", population));
            iterations++;
            iterationsWithoutImprovement++;


        }

        getStopWatch().stop();

        bestIterationSolution.setExecutionTime(getStopWatch().getTime());
    }

    private List<Solution> parentsChoice() {
        return SolutionsCollectionUtils.copyValues(selection.modify(population, metaHeuristicParameters, numParents), problem);
    }

    private Solution mutation(Solution solution) {
        logger.debug(format("Mutating %s", solution));
        return mutation.modify(solution, null, null, 1);
    }

    private List<Solution> crossover(List<Solution> parents) {
        logger.debug("Crossing fathers");
        return SolutionsCollectionUtils.copyValues(reproduction.modify(parents, metaHeuristicParameters, 2), problem);
    }

    private List<Solution> nextPopulation(List<Solution> population) {
        logger.debug("generating next population");
        return SolutionsCollectionUtils.copyValues(nextPopulationSelector.modify(population, metaHeuristicParameters, populationSize), problem);
    }

    public SolutionAnalyser getSolutionAnalyser() {
        return solutionAnalyser;
    }

    public void setSolutionAnalyser(SolutionAnalyser solutionAnalyser) {
        this.solutionAnalyser = solutionAnalyser;
    }

    public Integer getPopulationSize() {
        return this.populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    private void setMaxIterations(int parseInt) {
        this.maxIterations = parseInt;
    }

    public Integer getNumParents() {
        return numParents;
    }

    public void setNumParents(Integer numParents) {
        this.numParents = numParents;
    }

    public void setMaxIterations(Integer maxIterations) {
        this.maxIterations = maxIterations;
    }

    public Double getMutationTax() {
        return this.mutationTax;
    }

    public void setMutationTax(Double mutationTax) {
        this.mutationTax = mutationTax;
    }

    public Double getCrossoverTax() {
        return this.crossoverTax;
    }

    public void setCrossoverTax(Double crossoverTax) {
        this.crossoverTax = crossoverTax;
    }

    public String getParentsChoice() {
        return this.parentsChoice;
    }

    public void setParentsChoice(String parentsChoice) {
        this.parentsChoice = parentsChoice;
    }

    public String getMutationChoice() {
        return this.mutationChoice;
    }

    public void setMutationChoice(String mutationChoice) {
        this.mutationChoice = mutationChoice;
    }

    public String getCrossover() {
        return this.crossover;
    }

    public void setCrossover(String crossover) {
        this.crossover = crossover;
    }

    public String getNextPopulationChoice() {
        return this.nextPopulationChoice;
    }

    public void setNextPopulationChoice(String nextPopulationChoice) {
        this.nextPopulationChoice = nextPopulationChoice;
    }
}
