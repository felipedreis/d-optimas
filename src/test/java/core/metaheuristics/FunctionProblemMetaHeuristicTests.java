package core.metaheuristics;

import br.cefetmg.lsi.bimasco.core.AgentContext;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.DE;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.GA;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.GRASP;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.PSO;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import br.cefetmg.lsi.bimasco.settings.SolutionManipulation;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FunctionProblemMetaHeuristicTests {
    private FunctionProblem functionProblem;

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

    @BeforeEach
    public void init() {
        rnd = new RandomDataGenerator();

        ProblemSettings problemSettings = new ProblemSettings();
        problemSettings.setName("Function");
        problemSettings.setSolutionAnalyserName("Function");
        problemSettings.setMax(false);
        problemSettings.setProblemData(List.of(
                List.of("EggHolder"),
                List.of(2),
                List.of(102.4),
                List.of(0.000001, 0.000001),
                List.of(-512.000001, 512.000001 ),
                List.of(-512.000001, 512.000001 )
        ));

        functionProblem = new FunctionProblem();
        functionProblem.setProblemSettings(problemSettings);

        functionProblem.initialize();

        graspSettings = new AgentSettings();
        graspSettings.setMetaHeuristicParameters(
                Map.of("maxIterations", 10,
                        "alpha", 0.5,
                        "stopConditionName", "MaxIterations",
                        "localSearchName", "Random",
                        "localSearchNeighbor", "RealRandom",
                        "localSearchIterations", 10,
                        "candidatesListName", "Function")
        );

        gaSettings = new AgentSettings();
        gaSettings.setMetaHeuristicParameters(
                Map.of("maxIterations", 5,
                        "maxIterationsWI", 5,
                        "time", 60.0,
                        "f0", 0.5,
                        "stopConditionName", "MaxIterations",
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
                    "mutationChoice", new SolutionManipulation("RealRandom"),
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
                        DefaultMetaHeuristicParametersKeySupported.INITIAL_VELOCITY, "PSOFunction",
                        DefaultMetaHeuristicParametersKeySupported.VELOCITY, "PSOVelocityCalcFunctionProblem",
                        DefaultMetaHeuristicParametersKeySupported.POSITION, "PSOPositionCalcFunctionProblem")
        );


        deSettings = new AgentSettings();
        deSettings.setMetaHeuristicParameters(
                Map.of(
                        DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, "MaxIterations",
                        DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 50,
                        DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY, 30,
                        "C",  0.2,
                        "F", 0.5,
                        DefaultMetaHeuristicParametersKeySupported.DE_SUM, "DEFunctionProblemSum"
                )
        );

        grasp = new GRASP(functionProblem);
        ga = new GA(functionProblem);
        pso = new PSO(functionProblem);
        de = new DE(functionProblem);

        grasp.configureMetaHeuristic(graspSettings);
        ga.configureMetaHeuristic(gaSettings);
        pso.configureMetaHeuristic(psoSettings);
        de.configureMetaHeuristic(deSettings);
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
            FunctionSolution functionSolution = new FunctionSolution(functionProblem);

            IntStream.range(0, functionProblem.getDimension())
                    .mapToDouble(n -> rnd.nextUniform(-500, 500))
                    .forEach(d -> functionSolution.addElement(new FunctionSolutionElement(d)));
            functionSolution.evaluate(context);
            solutions.add(functionSolution);
        }

        return solutions;
    }

}
