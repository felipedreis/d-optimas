package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Element;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.localSearch.LocalSearch;
import br.cefetmg.lsi.bimasco.core.heuristics.localSearch.LocalSearchHelper;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopCondition;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopConditionHelper;
import br.cefetmg.lsi.bimasco.core.problems.candidatesList.CandidatesList;
import br.cefetmg.lsi.bimasco.core.problems.candidatesList.CandidatesListHelper;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class GRASP extends MetaHeuristic {

    public static int c_TimeDivisor = 1000;

    //TODO: Change this of location to reuse on every part of the code

    private Integer maxIterations;
    private Double alpha;
    private String candidatesListName;
    private SolutionAnalyser solutionAnalyser;
    private double initialTime;
    private double finalTime;
    private double time;
    private Object limit;

    // Inclusao: Verificar como isso pode modificar a questão da alterações do parametros do agente
    private CandidatesList<Problem, Element> candidatesList;
    private LocalSearch localSearch;
    private StopCondition stopCondition;
    private Integer iteracao;

    private static Logger logger = Logger.getLogger(GRASP.class);

    public GRASP(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {

        iteracao = 0;
        metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        maxIterations = (Integer) metaHeuristicParameters
                .get(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY);
        alpha = (Double) metaHeuristicParameters
                .get(DefaultMetaHeuristicParametersKeySupported.ALPHA_KEY);
        candidatesListName = (String) metaHeuristicParameters
                .get(DefaultMetaHeuristicParametersKeySupported.CANDIDATES_LIST_NAME_KEY);
        String stopConditionName = (String) metaHeuristicParameters
                .get(DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY);
        String localSearchName = (String) metaHeuristicParameters
                .get(DefaultMetaHeuristicParametersKeySupported.LOCAL_SEARCH_NAME_KEY);

        limit = problem.getLimit();

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);

        Solution auxiliarySolution = null;
        if (problem.getProblemSettings().getName() != null) {
            auxiliarySolution = Solution.buildSolution(problem);
        } else {
            System.out.print("ERRO");
        }

        candidatesList = CandidatesListHelper.buildCandidatesList(candidatesListName, problem);
        localSearch = LocalSearchHelper.buildLocalSearch(localSearchName, problem, metaHeuristicParameters);

        stopCondition = StopConditionHelper.buildStopCondition(stopConditionName, problem);
        time = 0;
        finalTime = 0;
        initialTime = 0;
    }

    @SuppressWarnings("empty-statement")
    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {
        double initialTime = 0;
        double finalTime = 0;
        double end = 0;

        initialTime = System.currentTimeMillis();

        logger.debug(format("Starting GRASP execution at %f", initialTime));
        Solution solution = this.internalExecutionGRASP(context);
        finalTime = System.currentTimeMillis();
        end = ((finalTime - initialTime) / c_TimeDivisor);
        logger.debug(format("Stopped GRASP execution at %f, duration of %f",finalTime, end));
        logger.debug(format("Produced solution: %s", solution));
        System.gc();

        List<Solution> solutions = new ArrayList<>();
        solutions.add(solution);

        return solutions;
    }

    public Solution internalExecutionGRASP(Context context)  {
        int divisor = 0;
        Solution solucaoBuscaLocal = null;
        Solution solucaoConstrucao = null;

        Object sBL = null;
        Object f0 = this.limit;
        Object mS = f0;
        int iteracoes = 0;
        int iteracoesSM = 0;

        this.initialTime = System.currentTimeMillis();

        Solution solution = Solution.buildSolution(problem);

        while (!this.stopCondition.isSatisfied(f0, this.time, iteracoes, iteracoesSM, this.metaHeuristicParameters)) {
            this.iteracao++;
            logger.debug(format("Iteration %d", iteracoes));
            solucaoConstrucao = construcao();
            logger.debug(format("Constructed solution: %s", solucaoConstrucao));

            logger.debug("Starting Local Search phase");
            solucaoBuscaLocal = localSearch.search(solucaoConstrucao, context);
            logger.debug(format("Refined solution: %s", solucaoBuscaLocal));

            if (solucaoBuscaLocal.isViable(context)) {
                logger.debug(format("Solution %s is viable", solucaoBuscaLocal));
                solucaoBuscaLocal.evaluate(context);
                solution = solutionAnalyser.getBestSolution(solucaoBuscaLocal, solution);

                if (solution == solucaoBuscaLocal) {
                    logger.debug("Updating best");
                    mS = solution.evaluate(context);
                    f0 = mS;
                    iteracoesSM = -1;
                }
            } else {
                logger.debug("Generate inviable solution");
            }

            logger.debug(format("Best solution found at iteration %d: %s", iteracoes, solution));
            iteracoes++;
            iteracoesSM++;

            if (divisor == 0)
                divisor = this.maxIterations / 10;
            if (iteracoes % (divisor < 2 ? 2 : divisor) == 0)
                System.gc();

            this.finalTime = System.currentTimeMillis();
            this.time = ((this.finalTime - this.initialTime) / c_TimeDivisor);

        }

        this.finalTime = System.currentTimeMillis();
        this.time = ((this.finalTime - this.initialTime) / c_TimeDivisor);
        solution.setExecutionTime(this.time);

        return solution;
    }

    public Solution construcao() {

        logger.debug("Construction phase");
        List<Element> candidates;
        List<Element> restricted;

        Solution<Element, ?, ?> solution;

        solution = Solution.buildSolution(problem);

        candidates = candidatesList.getCandidates();
        logger.debug(format("Candidate list generated: %s", candidatesList.toString()));

        while (!candidates.isEmpty()) {
            Map<Element, Number> adaptiveFunctionValues = candidates.stream()
                    .filter(el -> solution.adaptiveFunctionValue(el) != null)
                    .collect(Collectors.toMap(Function.identity(), solution::adaptiveFunctionValue, (a, b) -> a));

            Optional<Number> optMaxAdaptiveValue = adaptiveFunctionValues.values().stream()
                    .max(solutionAnalyser::compareFunctionValue);

            Optional<Number> optMinAdaptiveValue = adaptiveFunctionValues.values().stream()
                    .min(solutionAnalyser::compareFunctionValue);

            if (optMaxAdaptiveValue.isEmpty() || optMinAdaptiveValue.isEmpty())
                break;

            double gt_min = optMinAdaptiveValue.get().doubleValue(),
                    gt_max = optMaxAdaptiveValue.get().doubleValue();

            restricted = adaptiveFunctionValues.entrySet().stream()
                    .filter(entry -> entry.getValue().doubleValue() <= gt_min + alpha * (gt_max - gt_min))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (restricted.isEmpty())
                break;

            Element e = restricted.get(rnd.nextInt(0, restricted.size() - 1));
            logger.debug(format("Inserting element: %s", e));
            solution.addElement(e);
            candidates.remove(e);

        }

        return solution;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(Integer maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public List<Solution> getPartialSolutions() {

        return null;
    }
}
