package core.metaheuristics;

import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.GA;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import br.cefetmg.lsi.bimasco.settings.SolutionManipulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GATest {
    private GA ga;
    private FunctionProblem problem;

    @BeforeEach
    public void setup() {
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

        problem = new FunctionProblem();
        problem.setProblemSettings(problemSettings);
        problem.initialize();

        ga = new GA(problem);

        AgentSettings settings = new AgentSettings();
        settings.setMetaHeuristicParameters(
                Map.of(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 100,
                        DefaultMetaHeuristicParametersKeySupported.PARENTS_SIZE_KEY, 2,
                        DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY, 20,
                        DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, "MaxIterations",
                        DefaultMetaHeuristicParametersKeySupported.CROSSOVER_TAX_KEY, 0.8,
                        DefaultMetaHeuristicParametersKeySupported.MUTATION_TAX_KEY, 0.1)
        );

        settings.setSolutionManipulation(
                Map.of(
                        "chooseInitialSolution", new SolutionManipulation("ChooseRandomK"),
                        "nextPopulationChoice", new SolutionManipulation("ChooseRandomK"),
                        "parentsChoice", new SolutionManipulation("ChooseRandomK"),
                        "mutationChoice", new SolutionManipulation("RealRandom"),
                        "crossoverChoice",  new SolutionManipulation("BinaryCrossover")
                )
        );

        ga.configureMetaHeuristic(settings);
    }

    @Test
    public void testGetNumPais() {
        assertEquals(2, ga.getNumPais());
    }

    @Test
    public void testGetMaxIterations() {
        assertEquals(100, ga.getMaxIterations());
    }
}
