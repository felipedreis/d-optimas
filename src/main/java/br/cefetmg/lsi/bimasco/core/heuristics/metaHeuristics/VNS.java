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
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.SolutionsCollectionUtils;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.core.utils.SolutionManipulationKeys;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.SolutionManipulation;

import java.util.ArrayList;
import java.util.List;

//TODO: Rename elements
public class VNS extends MetaHeuristic {

    public static int c_TimeDivisor = 1000;
    private Solution bestSolution;
    private Solution currentSolution;
    private Integer maxIterations;
    private Integer maxNeighbors;
    private String localSearchName;
    private String stopConditionName;
    private String modifiesSolutionCollectionName;
    private SolutionAnalyser solutionAnalyser;
    private String neighbor;
    private String neighborLS;
    private double initialTime;
    private double endTime;
    private double time;
    private Object limit;
    // Inclusao: Verificar como isso pode modificar a questão da alterações do parametros do agente
    private LocalSearch localSearch;
    private StopCondition stopCondition;
    private ModifiesSolutionCollections modifiesSolutionCollection;

    public VNS(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {

        this.metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        this.maxIterations = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 0);
        this.maxNeighbors = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_NEIGHBORHOOD_KEY, 0);
        this.localSearchName = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.LOCAL_SEARCH_NAME_KEY, "");
        this.stopConditionName = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, "");
        this.neighborLS = agentSettings.getSolutionManipulation()
                .getOrDefault(SolutionManipulationKeys.NEIGHBOR_LOCAL_SEARCH_NAME_KEY, new SolutionManipulation()).getType();
        this.neighbor = agentSettings.getSolutionManipulation()
                .getOrDefault(SolutionManipulationKeys.NEIGHBOR_KEY, new SolutionManipulation()).getType();
        this.modifiesSolutionCollectionName = agentSettings.getSolutionManipulation()
                .getOrDefault(SolutionManipulationKeys.CHOOSE_INITIAL_SOLUTION_KEY, new SolutionManipulation()).getType();

        limit = this.problem.getLimit();

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);

        if (problem.getProblemSettings().getName() != null) {
            currentSolution = Solution.buildSolution(problem);
        }

        localSearch = LocalSearchHelper.buildLocalSearch(localSearchName, problem, metaHeuristicParameters);
        stopCondition = StopConditionHelper.buildStopCondition(stopConditionName, problem);
        modifiesSolutionCollection = ModifiesSolutionCollectionsHelper
                .buildModifiesSolutionCollection(modifiesSolutionCollectionName, problem);

        time = 0;
        endTime = 0;
        initialTime = 0;
    }


    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {
        if (externalSolution != null) {
            double initialTime = 0;
            double finalTime = 0;
            double end = 0;

            initialTime = System.currentTimeMillis();

            List<Solution> colSolucoes = new ArrayList<Solution>();

            colSolucoes = SolutionsCollectionUtils.copyValues(externalSolution, problem);


            //currentSolution = modifiesSolutionCollection.modify(colSolucoes, this.metaHeuristicParameters, 1).get(0);

            bestSolution = (Solution)currentSolution.clone();

            metodo_VNS(context);

            finalTime = System.currentTimeMillis();
            end = ((finalTime - initialTime) / c_TimeDivisor);

            System.gc();

            return List.of(this.bestSolution);
        } else {
            return null;
        }
    }

    public void metodo_VNS(Context context) {
        int divisor = 0;
        SolutionModifier vizinho = SolutionModifierHelper
                .buildModifiesSolution(problem.getProblemSettings().getType() + this.neighbor, this.problem);

        int vizinhanca = 2;
        Object sBL;
        Object mS = bestSolution.getFunctionValue();
        Object msInicial = bestSolution.getFunctionValue();
        Object f0 = this.limit;
        int iteracoes = 0;
        int iteracoesSM = 0;

        this.initialTime = System.currentTimeMillis();

        while (!this.stopCondition.isSatisfied(f0, this.time, iteracoes, iteracoesSM, this.metaHeuristicParameters)) {//&& (!this.getAgentPaused()) ){
            Solution solucaoAux = (Solution) currentSolution.clone();

            Solution solucaoVizinha = vizinho.modify(solucaoAux, null, null, vizinhanca);
            Solution solucaoBuscaLocal = localSearch.search(solucaoVizinha, context);

            if (solucaoBuscaLocal.isViable(context)) {
                sBL = solucaoBuscaLocal.getFunctionValue();

                /*if (sBL.equals(this.solutionAnalyser.getBest(mS, sBL))) {
                    bestSolution = solucaoBuscaLocal;
                    mS = this.bestSolution.getFunctionValue();
                    f0 = mS;
                    vizinhanca = 1;
                    iteracoesSM = -1;

                    this.currentSolution = bestSolution;
                }*/

                if (vizinhanca <= this.maxNeighbors) {
                    vizinhanca = 2;
                } else {
                    vizinhanca++;
                }
            }

            iteracoes++;
            iteracoesSM++;

            /*
             * Chama o Garbage Colector no máximo a cada décimo de iterações
             * executadas, sendo o mínimo de 2 iterações para ciclos muito
             * pequenos.
             */
            if (divisor == 0)
                divisor = ((int) (this.maxIterations / 10));
            if (iteracoes % (divisor < 2 ? 2 : divisor) == 0)
                System.gc();


            this.endTime = System.currentTimeMillis();
            this.time = ((this.endTime - this.initialTime) / c_TimeDivisor);
        }

        System.err.println("VNS - modifiesSolutionCollection: " + msInicial);
        System.err.println("VNS - time: " + this.time);
        System.err.println("VNS - maxIterations: " + iteracoes);
        System.err.println("VNS - maxIteracoesSM: " + iteracoesSM);

        this.endTime = System.currentTimeMillis();
        this.time = ((this.endTime - this.initialTime) / c_TimeDivisor);
    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
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

    public Integer getMaxIterations() {
        return maxIterations;
    }

    private void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public Integer getMaxNeighbors() {
        return maxNeighbors;
    }

    public void setMaxNeighbors(Integer maxNeighbors) {
        this.maxNeighbors = maxNeighbors;
    }

}
