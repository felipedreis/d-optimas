package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class EggholderFunctionSolutionTest {

    FunctionProblem problem;

    FunctionSolution solution;

    @BeforeEach
    public void init() {
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
    }

    @Test
    public void testIsViable() {
        solution = new FunctionSolution(problem);

        solution.addElement(new FunctionSolutionElement(1000));
        solution.addElement(new FunctionSolutionElement(1000));

        assertFalse(solution.isViable(null));

    }

}