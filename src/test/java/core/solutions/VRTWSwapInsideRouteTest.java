package core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifier;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifierHelper;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class VRTWSwapInsideRouteTest  {

    private SimulationSettings settings;
    Problem problem;
    Solution solution;
    SolutionModifier modifier;

    public VRTWSwapInsideRouteTest() {
        settings = new SimulationSettings(ConfigFactory.load("vrtwSolution.conf"));
    }

    @BeforeEach
    public void config() {
        problem = Problem.buildProblem(settings.getProblem());
        solution = Solution.buildSolution(problem);
        modifier = SolutionModifierHelper.buildModifiesSolution("VRTWSwapInsideRoute", problem);
    }

    @RepeatedTest(100)
    public void test() {
        solution.generateInitialSolution();

        System.out.println(solution);
        Solution s = modifier.modify(solution, null, null, 0);
        assertNotEquals(s, solution, "Modifier should return a different instance always");
        assertEquals(s.getFunctionValue(), solution.getFunctionValue(),
                "A 0 motions modification should return the same solution");

        System.out.println(solution);
        s = modifier.modify(solution, null, null, 1);
        assertNotEquals(s.getFunctionValue(), solution.getFunctionValue(),
                "One motion should change the function value");

        assertThrows(NullPointerException.class,
                () -> modifier.modify(null, null, null, 0));
    }

    @Test
    public void testSingleRoute() {
        solution.setSolutionsVector(List.of(0, 5, 4, 7, 6, 10, 9, 3, 1, 2, 8, 0));
        assertDoesNotThrow(() -> modifier.modify(solution, null, null, 100));
    }
}
