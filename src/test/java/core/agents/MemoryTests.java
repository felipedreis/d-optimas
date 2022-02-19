package core.agents;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.agents.Memory;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryTests {
    /*
    private ProblemSettings problemSettingsMock;
    private boolean hasCooperation;
    private Memory memory;

    @Before
    public void givenProblemSettingsInstance() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        SimulationSettingsMock simulationSettingsMock = ConfigFactory.create(SimulationSettingsMock.class);
        this.problemSettingsMock = simulationSettingsMock.problem();
        this.hasCooperation = simulationSettingsMock.hasCooperation();
    }

    @Before
    public void andInitializeMemoryInstance(){
        this.memory = new Memory(problemSettingsMock, hasCooperation);
    }

    @Test
    public void whenAddSolutionList() throws IllegalAccessException, ClassNotFoundException, InstantiationException {

        FunctionProblem problem = (FunctionProblem) Problem.buildProblem(problemSettingsMock);
        FunctionSolution functionSolution1 = new FunctionSolution(problem);
        functionSolution1.initialize();

        ArrayList<Solution> solutions = new ArrayList<Solution>();
        solutions.add(functionSolution1);

        this.memory.addSolutions(solutions, new UUID(1L, 1L));
        assertEquals(functionSolution1, this.memory.getBestSolution());

        assertEquals(functionSolution1.getFunctionValue().toString(), this.memory.getTimeLine().getGeneratedSolutions().get(0)[0]);

    }
    */

}
