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
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

import java.util.*;

import static java.lang.String.format;

public class GA extends MetaHeuristic {

    public static int C_TIME_DIVISOR = 1000;

    private List<Solution> population;
    private List<Solution> proxPopulacao;
    private Solution bestIterationSolution;
    private String escolhaPais;
    private String crossover;
    private String mutacao;
    private String escolhaProxPopulacao;
    private String escolheSolucaoInicial;
    private SolutionAnalyser solutionAnalyser;
    private Integer maxIterations;
    private Integer populationSize;
    private Integer numPais;
    private Double mutationTax;
    private Double crossoverTax;

    private String stopConditionName;
    private double tempoInicial;
    private double tempoFinal;
    private double tempo;
    private Object limite;
    private StopCondition stopCondition;
    private ModifiesSolutionCollections solucaoInicial;
    private ModifiesSolutionCollections selection;
    private ModifiesSolutionCollections reproduction;
    private ModifiesSolutionCollections nextPopulation;
    private SolutionModifier mutation;

    private static Logger logger = Logger.getLogger(GA.class);

    public GA(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {
        // Tais informaçoes sao passadas interativamente atraves da interface grafica (BIMASCO)
        Solution solucaoAux;

        population = new ArrayList<>();
        proxPopulacao = new ArrayList<>();

        metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        maxIterations = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, null);

        populationSize = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY, null);

        numPais = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.PARENTS_SIZE_KEY, null);

        stopConditionName = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, null);

        crossoverTax = (Double) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.CROSSOVER_TAX_KEY, null);


        mutationTax = (Double) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MUTATION_TAX_KEY, null);


        escolhaPais = agentSettings.getSolutionManipulation()
                .getOrDefault(SolutionManipulationKeys.PARENTS_CHOICE_KEY, new SolutionManipulation("", true)).getType();

        crossover = agentSettings.getSolutionManipulation()
                .getOrDefault(SolutionManipulationKeys.CROSSOVER_CHOICE_KEY, new SolutionManipulation("", true)).getType();
        mutacao = agentSettings.getSolutionManipulation().
                getOrDefault(SolutionManipulationKeys.MUTATION_CHOICE_KEY, new SolutionManipulation("", true)).getType();

        if (agentSettings.getSolutionManipulation().containsKey(SolutionManipulationKeys.NEXT_POPULATION_CHOICE_KEY)) {
            escolhaProxPopulacao = agentSettings.getSolutionManipulation()
                    .get(SolutionManipulationKeys.NEXT_POPULATION_CHOICE_KEY).getType();
        } else {
            escolhaProxPopulacao = agentSettings.getSolutionManipulation()
                    .getOrDefault(SolutionManipulationKeys.CHOOSE_INITIAL_SOLUTION_KEY, new SolutionManipulation("", false)).getType();
        }

        escolheSolucaoInicial = agentSettings.getSolutionManipulation()
                .get(SolutionManipulationKeys.CHOOSE_INITIAL_SOLUTION_KEY).getType();

        limite = problem.getLimit();

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);

        bestIterationSolution =  Solution.buildSolution(problem);

        stopCondition = StopConditionHelper.buildStopCondition(stopConditionName, problem);

        solucaoInicial = ModifiesSolutionCollectionsHelper.buildModifiesSolutionCollection(escolheSolucaoInicial, problem);
        selection = ModifiesSolutionCollectionsHelper.buildModifiesSolutionCollection(escolhaPais, problem);
        mutation = SolutionModifierHelper.buildModifiesSolution(mutacao, problem);
        reproduction =
                ModifiesSolutionCollectionsHelper.buildModifiesSolutionCollection(crossover, problem);
        nextPopulation = ModifiesSolutionCollectionsHelper.buildModifiesSolutionCollection(escolhaProxPopulacao, problem);

        tempo = 0;
        tempoFinal = 0;
        tempoInicial = 0;
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

        Object f0 = limite;

        List<Solution> pais;
        List<Solution> offsprings;
        Random rand = new Random();

        int offspringCounter;
        double permCrossover;
        double permMutacao;
        int iteracoes = 0;
        int iteracoesSM = 0;


        getStopWatch().reset();
        getStopWatch().start();

        while (!stopCondition.isSatisfied(f0, stopWatch.getTime(), iteracoes, iteracoesSM, metaHeuristicParameters)) {
            logger.debug(format("Iteration %d", iteracoes));
            proxPopulacao = new ArrayList<>();
            proxPopulacao = SolutionsCollectionUtils.copyValues(population, problem);
            offspringCounter = 0;

            while (offspringCounter < populationSize) {
                pais = SolutionsCollectionUtils.copyValues(parentsChoice(), problem);
                permCrossover = rand.nextDouble();

                logger.debug(format("Fathers chosen %s", pais));

                if (permCrossover < crossoverTax) {
                    offsprings = SolutionsCollectionUtils.copyValues(crossover(pais), problem);
                    logger.debug(format("Offsprings %s", offsprings));

                    for (Solution offspring : offsprings) {
                        permMutacao = rand.nextDouble();

                        Solution mutated;

                        if (permMutacao < mutationTax) {
                            mutated = mutation(offspring);
                        } else {
                            mutated = offspring;
                        }

                        mutated.evaluate(context);
                        logger.debug(format("Mutated offspring: %s", mutated));

                        if (mutated.isViable(context)) {
                            proxPopulacao.add(mutated);
                            offspringCounter++;
                        }
                    }
                }
            }
            Optional<Solution> bestSolutionOptional = solutionAnalyser.findBestSolution(proxPopulacao);

            if (bestSolutionOptional.isPresent()) {
                bestIterationSolution = bestSolutionOptional.get();
                iteracoesSM = -1;
            }

            population.clear();
            population = SolutionsCollectionUtils.copyValues(nextPopulation(proxPopulacao), problem);
            logger.debug(format("Next population: %s", population));
            iteracoes++;
            iteracoesSM++;


        }

        getStopWatch().stop();

        bestIterationSolution.setExecutionTime(getStopWatch().getTime());
    }

    private List<Solution> parentsChoice() {
        return SolutionsCollectionUtils.copyValues(selection.modify(population, metaHeuristicParameters, numPais), problem);
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
        return SolutionsCollectionUtils.copyValues(nextPopulation.modify(population, metaHeuristicParameters, populationSize), problem);
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

    public Integer getNumPais() {
        return maxIterations;
    }

    public void setNumPais(Integer numPais) {
        this.numPais = numPais;
    }

    public void setMaxIteracoes(Integer maxIteracoes) {
        this.maxIterations = maxIteracoes;
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

    public String getEscolharPais() {
        return this.escolhaPais;
    }

    public void setEscolherPais(String nomeEscolhaPais) {
        this.escolhaPais = nomeEscolhaPais;
    }

    public String getMutacao() {
        return this.mutacao;
    }

    public void setMutacao(String nomeMutacao) {
        this.mutacao = nomeMutacao;
    }

    public String getCrossover() {
        return this.crossover;
    }

    public void setCrossover(String nomeCrossover) {
        this.crossover = nomeCrossover;
    }

    public String getProximaPopulacao() {
        return this.escolhaProxPopulacao;
    }

    public void setProximaPopulacao(String nomeProximaPopulacao) {
        this.escolhaProxPopulacao = nomeProximaPopulacao;
    }
}
