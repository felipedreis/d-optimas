package core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class FunctionSolutionTests {

    private FunctionProblem functionProblem;
    private Solution functionSolution;

    private SimulationSettings simulationSettings;

    public FunctionSolutionTests() {
        simulationSettings = new SimulationSettings(ConfigFactory.load("functionSolutionTest"));
    }

    @BeforeEach
    public void givenSimulationSettings() {
        functionProblem = (FunctionProblem) Problem.buildProblem(simulationSettings.getProblem());
        functionSolution = Solution.buildSolution(functionProblem);
    }

    @Test
    public void whenFunctionSolutionAreInitialized() {
        assertEquals((Integer)functionSolution.getSolutionsVector().size(), functionProblem.getDimension());

    }

    @Test
    public void whenSolutionChanges() {
        Number oldFunctionValue = functionSolution.getFunctionValue();
        functionSolution.setElement(0, new FunctionSolutionElement(1.));

        assertAll("Viability check and lazy objective function calculation",
            () -> assertTrue(functionSolution.isChanged()),
            () -> assertTrue(functionSolution.isViable(null)),
            () -> assertFalse(functionSolution.isChanged()),
            () -> assertNotEquals(functionSolution.getFunctionValue(), oldFunctionValue)
        );

        functionSolution.setElement(0, new FunctionSolutionElement(1.5));
        assertAll("Lazy function value calculation",
                () -> assertTrue(functionSolution.isChanged()),
                () -> assertNotEquals(functionSolution.getFunctionValue(), oldFunctionValue),
                () -> assertFalse(functionSolution.isChanged()));
    }

    @Test
    public void whenSolutionChangesImproperly() {
        assertThrows(IllegalArgumentException.class, () -> functionSolution.setElement(2, new FunctionSolutionElement(1)));
        assertThrows(IllegalArgumentException.class, () -> functionSolution.removeElement(2));
        functionSolution.removeElement(0);
        assertThrows(NullPointerException.class, () -> functionSolution.getFunctionValue());
    }
}
