package core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.VehicleRoutingTimeWindowSolution;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Disabled
public class VehicleRoutingTimeWindowSolutionTest {

    private SimulationSettings settings;
    Problem problem;
    Solution solution;

    public VehicleRoutingTimeWindowSolutionTest() {
        settings = new SimulationSettings(ConfigFactory.load("vrtwSolution.conf"));
    }

    @BeforeEach
    public void testCreation(){
        problem = Problem.buildProblem(settings.getProblem());
        solution = Solution.buildSolution(problem);
    }

    @Test
    public void testFunctionValue() {
        Object value1, value2;
        solution.generateInitialSolution();
        assertNotNull(value1 = solution.getFunctionValue());
        solution.generateInitialSolution();
        assertNotNull(value2 = solution.getFunctionValue());
        assertNotEquals(value1, value2);
    }

    @RepeatedTest(10)
    public void testViability() {
        solution.generateInitialSolution();
        assumingThat(solution.isViable(null), () ->
            assertTrue((Double) solution.getFunctionValue() < 10000,
                    "Solution " + solution.getSolutionsVector() + " value is " + solution.getFunctionValue())
        );
    }

    @Test
    public void testRemoveShifting() {
        solution.generateInitialSolution();
        VehicleRoutingTimeWindowSolution s = (VehicleRoutingTimeWindowSolution) solution;
        int index = 1;
        solution.removeElementShifting(index);
        assertTrue(solution.getSolutionsVector().size() == problem.getDimension());

    }

    @Test
    public void testAddShifting() {

    }
}
