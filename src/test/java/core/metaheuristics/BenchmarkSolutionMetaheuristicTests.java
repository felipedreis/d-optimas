package core.metaheuristics;

import br.cefetmg.lsi.bimasco.coco.CoCOBenchmark;
import br.cefetmg.lsi.bimasco.coco.Observer;
import br.cefetmg.lsi.bimasco.coco.Suite;
import br.cefetmg.lsi.bimasco.core.AgentContext;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.DE;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.GA;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.GRASP;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.PSO;
import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.BenchmarkSolution;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import br.cefetmg.lsi.bimasco.settings.SolutionManipulation;
import coco.CocoJNI;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BenchmarkSolutionMetaheuristicTests {

    private BenchmarkProblem benchmarkProblem;

    private GRASP grasp;

    private GA ga;

    private PSO pso;

    private DE de;

    private AgentSettings graspSettings;

    private AgentSettings gaSettings;

    private AgentSettings psoSettings;

    private AgentSettings deSettings;

    private RandomDataGenerator rnd;

    private AgentContext context;

    CoCOBenchmark benchmark;


    private static CoCOBenchmark getBenchmark() {
        try {
            Suite suite = new Suite("bbob", "bbob", "");
            Observer observer = new Observer("bbob", "");
            return new CoCOBenchmark(suite, observer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    @BeforeEach
    public void init() {

        rnd = new RandomDataGenerator();

        ProblemSettings problemSettings = new ProblemSettings();
        problemSettings.setName("Benchmark");
        problemSettings.setSolutionAnalyserName("Benchmark");
        problemSettings.setMax(false);

        benchmark = getBenchmark();
        try {
            benchmarkProblem = benchmark.getNextProblem();
            benchmarkProblem.setEvaluator(x -> CocoJNI.cocoEvaluateFunction(benchmarkProblem.getPointer(), x));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        graspSettings = new AgentSettings();
        graspSettings.setMetaHeuristicParameters(
                Map.of("maxIterations", 10,
                        "alpha", 0.5,
                        "stopConditionName", "MaxIterations",
                        "localSearchName", "Random",
                        "localSearchNeighbor", "BenchmarkRandom",
                        "localSearchIterations", 10,
                        "step", 0.1,
                        "candidatesListName", "Benchmark")
        );

        gaSettings = new AgentSettings();
        gaSettings.setMetaHeuristicParameters(
                Map.of("maxIterations", 5,
                        "maxIterationsWI", 5,
                        "time", 60.0,
                        "f0", 0.5,
                        "stopConditionName", "MaxIterations",
                        "step", 0.1,
                        "parentsSize", 2,
                        "populationSize", 5,
                        "mutationTax", 0.2,
                        "crossoverTax", 0.7)
        );

        gaSettings.setSolutionManipulation(
                Map.of(
                        "chooseInitialSolution", new SolutionManipulation("ChooseRandomK"),
                        "nextPopulationChoice", new SolutionManipulation("ChooseRandomK"),
                        "parentsChoice", new SolutionManipulation("ChooseRandomK"),
                        "mutationChoice", new SolutionManipulation("BenchmarkRandom"),
                        "crossoverChoice",  new SolutionManipulation("BinaryCrossover")
                )
        );

        psoSettings = new AgentSettings();
        psoSettings.setMetaHeuristicParameters(
                Map.of( DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, "MaxIterations",
                        "maxIterations", 500,
                        DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY, 30,
                        DefaultMetaHeuristicParametersKeySupported.C1, 0.15,
                        DefaultMetaHeuristicParametersKeySupported.C2, 0.15,
                        DefaultMetaHeuristicParametersKeySupported.INITIAL_VELOCITY, "PSOBenchmark",
                        DefaultMetaHeuristicParametersKeySupported.VELOCITY, "PSOVelocityCalcBenchmarkProblem",
                        DefaultMetaHeuristicParametersKeySupported.POSITION, "PSOPositionCalcBenchmarkProblem")
        );


        deSettings = new AgentSettings();
        deSettings.setMetaHeuristicParameters(
                Map.of(
                        DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, "MaxIterations",
                        DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 50,
                        DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY, 30,
                        "C",  0.2,
                        "F", 0.5,
                        DefaultMetaHeuristicParametersKeySupported.DE_SUM, "DEBenchmarkProblemSum"
                )
        );

        grasp = new GRASP(benchmarkProblem);
        ga = new GA(benchmarkProblem);
        pso = new PSO(benchmarkProblem);
        de = new DE(benchmarkProblem);

        grasp.configureMetaHeuristic(graspSettings);
        ga.configureMetaHeuristic(gaSettings);
        pso.configureMetaHeuristic(psoSettings);
        de.configureMetaHeuristic(deSettings);
    }

    @AfterEach
    public void closeTest() throws Exception{
        benchmark.finalizeBenchmark();
    }

    @Test
    public void testGrasp() {
        Set<Solution> results = new HashSet<>();

        for (int i = 0; i < 10; ++i)
            results.addAll(grasp.runMetaHeuristic(Collections.emptyList(), context));

        assertTrue(results.size() > 1);

        results.forEach(System.out::println);
    }

    @Test
    public void testGA() {

        List<Solution> solutions = getRandomSolutionList(ga.getPopulationSize());

        List<Solution> result = ga.runMetaHeuristic(solutions, context);

        assertFalse(result.isEmpty());
    }

    @Test
    public void testPSO(){
        List<Solution> solutions = getRandomSolutionList(pso.getPopulationSize());

        List<Solution> result = pso.runMetaHeuristic(solutions, context);

        assertFalse(result.isEmpty());
    }

    @Test
    public void testDE(){
        List<Solution> solutions = getRandomSolutionList(de.getPopulationSize());

        List<Solution> result = de.runMetaHeuristic(solutions, context);

        assertFalse(result.isEmpty());
    }

    private List<Solution> getRandomSolutionList(int populationSize) {

        List<Solution> solutions = new ArrayList<>();

        for (int i = 0; i < populationSize; ++i) {
            BenchmarkSolution functionSolution = new BenchmarkSolution(benchmarkProblem);

            IntStream.range(0, benchmarkProblem.getDimension())
                    .mapToDouble(n -> rnd.nextUniform(benchmarkProblem.getSmallestValueOfInterest(n), benchmarkProblem.getLargestValueOfInterest(n)))
                    .forEach(d -> functionSolution.addElement(new FunctionSolutionElement(d)));
            functionSolution.evaluate(context);
            solutions.add(functionSolution);
        }

        return solutions;
    }

}
