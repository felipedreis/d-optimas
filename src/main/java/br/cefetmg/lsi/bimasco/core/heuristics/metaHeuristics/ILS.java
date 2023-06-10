package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.localSearch.LocalSearch;
import br.cefetmg.lsi.bimasco.core.heuristics.localSearch.LocalSearchHelper;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopCondition;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopConditionHelper;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifier;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifierHelper;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.ModifiesSolutionCollections;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.ModifiesSolutionCollectionsHelper;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.core.utils.SolutionManipulationKeys;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.SolutionManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

//TODO: Rename elements
public class ILS extends MetaHeuristic {

    private static Logger logger = LoggerFactory.getLogger(ILS.class);
    public static int c_TimeDivisor = 1000;
    public Integer iteration;
    private Solution currentSolution;
    private Solution bestSolution;

    private Integer maxIterations;
    private Integer disturbLevel;
    private String localSearchName;
    private String stopConditionName;
    private String chooseInitialSolution;
    private SolutionAnalyser solutionAnalyser;
    private String neighborLS;
    private String disturb;
    private double initialTime;
    private double finalTime;
    private double time;
    private Object limit;
    // Inclusao: Verificar como isso pode modificar a questão da alterações do parametros do agente
    private LocalSearch localSearch;
    private StopCondition stopCondition;
    private ModifiesSolutionCollections modifiesSolutionCollections;

    private SolutionModifier solutionDisturb;


    public ILS(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {

        metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        iteration = 0;

        maxIterations = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 0);
        disturbLevel = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.DISTURB_LEVEL_KEY, 0);
        stopConditionName = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, "");
        localSearchName = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.LOCAL_SEARCH_NAME_KEY, "");

        Map<String, SolutionManipulation> solutionManipulation = agentSettings.getSolutionManipulation();

        neighborLS = solutionManipulation
                .getOrDefault(SolutionManipulationKeys.NEIGHBOR_LOCAL_SEARCH_NAME_KEY, new SolutionManipulation()).getType();
        disturb = solutionManipulation
                .getOrDefault(SolutionManipulationKeys.DISTURB_KEY, new SolutionManipulation()).getType();
        chooseInitialSolution = solutionManipulation
                .getOrDefault(SolutionManipulationKeys.CHOOSE_INITIAL_SOLUTION_KEY, new SolutionManipulation()).getType();

        limit = problem.getLimit();

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        if (problem.getProblemSettings().getName() != null) {
            currentSolution = Solution.buildSolution(problem);
        }

        localSearch = LocalSearchHelper.buildLocalSearch(localSearchName, problem, metaHeuristicParameters);
        stopCondition = StopConditionHelper.buildStopCondition(stopConditionName, problem);
        modifiesSolutionCollections = ModifiesSolutionCollectionsHelper
                .buildModifiesSolutionCollection(chooseInitialSolution, problem);
        solutionDisturb = SolutionModifierHelper
                .buildModifiesSolution(disturb, problem, metaHeuristicParameters);

        time = 0;
        finalTime = 0;
        initialTime = 0;
    }

    @SuppressWarnings("empty-statement")
    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {

        if (externalSolution == null){
            logger.debug("Cannot run this method with an empty list");
            return Collections.emptyList();
        }
        if (externalSolution.isEmpty()) {
            logger.debug("Cannot run this method with an empty list");
            return Collections.emptyList();
        }

        double initialTime = 0;
        double finalTime = 0;
        double end = 0;

        initialTime = System.currentTimeMillis();

        currentSolution = (Solution) modifiesSolutionCollections.modify(externalSolution, metaHeuristicParameters, 1).get(0);
        bestSolution = localSearch.search(currentSolution, context);

        logger.debug(format("Starting ILS execution at %f", initialTime));
        bestSolution = metodo_ILS(context);
        finalTime = System.currentTimeMillis();
        end = ((finalTime - initialTime) / c_TimeDivisor);

        logger.debug(format("Stopped ILS execution at %f, duration of %f", initialTime, end));
        logger.debug(format("Produced solution: %s", bestSolution));

        System.gc();
        ArrayList<Solution> bestSolutionsList = new ArrayList<>();
        bestSolutionsList.add(bestSolution);

        return (bestSolutionsList);

    }

    public Solution metodo_ILS(Context context) {
        int divisor = 0;
        int nivel = 2;
        int nivelAux = 0;
        Object f0 = limit;
        int iteracoes = 0;
        int iteracoesSM = 0;

        currentSolution = bestSolution;

        getStopWatch().reset();
        getStopWatch().start();

        while (!stopCondition.isSatisfied(f0, getStopWatch().getTime(), iteracoes, iteracoesSM, metaHeuristicParameters)) {//&& (!getAgentPaused()) ){
            iteration++;
            logger.debug(format("Iteration %d", iteracoes));
            logger.debug(format("Current solution: %s", currentSolution));


            Solution perturbedSolution = solutionDisturb(currentSolution, nivel);
            logger.debug(format("Disturbed solution: %s", perturbedSolution));
            Solution localSearchSolution = localSearch.search(perturbedSolution, context);

            localSearchSolution.evaluate(context);

            if (localSearchSolution.isViable(context)) {

                bestSolution = solutionAnalyser.getBestSolution(localSearchSolution, bestSolution);

                if (bestSolution.equals(localSearchSolution)) {
                    nivel = 2;
                    nivelAux = -1;

                    logger.debug("Updating best solution");
                    currentSolution = localSearchSolution;
                    bestSolution = currentSolution;
                    iteracoesSM = -1;
                }
            }

            if (nivel < disturbLevel) {
                if (nivelAux < 6) {
                    nivelAux++;
                } else {
                    nivel++;
                    nivelAux = 0;
                    //iteracoes++;
                    iteracoesSM++;
                }
            } else {
                nivel = 2;
                nivelAux = 0;
                //iteracoes++;
                iteracoesSM++;
            }

            iteracoes++;
            
            /*
             * Chama o Garbage Colector no máximo a cada décimo de iterações
             * executadas, sendo o mínimo de 2 iterações para ciclos muito
             * pequenos.
             */
            if (divisor == 0) {
                divisor = maxIterations / 10;
            }
            if (iteracoes % (Math.max(divisor, 2)) == 0) {
                System.gc();
            }
            finalTime = System.currentTimeMillis();
            time = ((finalTime - initialTime) / c_TimeDivisor);
        }
        getStopWatch().stop();
        return bestSolution;
    }

    public Solution solutionDisturb(Solution solucao, Integer nivelPertubacao) {
        logger.debug(format("Disturbing solution with %s", solutionDisturb.getClass().getSimpleName()));
        Solution novaSolucao = solutionDisturb.modify((Solution) solucao.clone(), null, null, nivelPertubacao);
        return novaSolucao;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    private void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public Integer getDisturbLevel() {
        return disturbLevel;
    }

    private void setDisturbLevel(int disturbLevel) {
        this.disturbLevel = disturbLevel;
    }

    public Solution getCurrentSolution() {
        return currentSolution;
    }

    public void setCurrentSolution(Solution currentSolution) {
        this.currentSolution = currentSolution;
    }

    public SolutionAnalyser getSolutionAnalyser() {
        return solutionAnalyser;
    }

    public void setSolutionAnalyser(SolutionAnalyser solutionAnalyser) {
        this.solutionAnalyser = solutionAnalyser;
    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
        //return bestSolutionsList;
    }
}
