package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MichalewiczFunctionSolutionTest {

    FunctionProblem problem;

    FunctionSolution solution;

    @BeforeEach
    public void init() {
        ProblemSettings problemSettings = new ProblemSettings();
        problemSettings.setName("Function");
        problemSettings.setSolutionAnalyserName("Function");
        problemSettings.setMax(false);
        problemSettings.setProblemData(List.of(
                List.of("Michalewicz"),
                List.of(2),
                List.of(0.1),
                List.of(0.000001, 0.000001),
                List.of(0.0, 3.141592),
                List.of( 0.0, 3.141592)
        ));

        problem = new FunctionProblem();
        problem.setProblemSettings(problemSettings);
        problem.initialize();
    }

    @Test
    public void testNotViable() {
        solution = new FunctionSolution(problem);

        solution.addElement(new FunctionSolutionElement(5));
        solution.addElement(new FunctionSolutionElement(5));

        assertFalse(solution.isViable(null));
    }

    @Test
    public void testViable() {
        solution = new FunctionSolution(problem);
        solution.addElement(new FunctionSolutionElement(3));
        solution.addElement(new FunctionSolutionElement(3));

        assertTrue(solution.isViable(null));
    }

    @Test
    public void testEvaluateMinima() {
        final double minimaValue = -1.7723;
        solution = new FunctionSolution(problem);
        solution.addElement(new FunctionSolutionElement(2.20));
        solution.addElement(new FunctionSolutionElement(1.57));

        assertEquals(minimaValue, solution.evaluate(null), 1e-4);
    }

}
