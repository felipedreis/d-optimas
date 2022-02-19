package core.problems;

public class FunctionProblemsTests {
    /*
    private FunctionSolution functionSolutionMock;
    private FunctionProblem problemMock;
    //Or create a manually simulationSettings
    private SimulationSettingsMock simulationSettingsMock;

    @Before
    public void givenSimulationSettings() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        simulationSettingsMock = ConfigFactory.create(SimulationSettingsMock.class);
    }

    @Test
    public void whenFunctionProblemIsInitializedWithBuilder() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        problemMock = (FunctionProblem)Problem.buildProblem(simulationSettingsMock.problem());

        assertNotNull(problemMock);
        assertNotNull(problemMock.getProblemSettings());
        assertProblemSettings();
    }

    @Test
    public void whenFunctionProblemIsInitializedWithoutBuilder(){
        problemMock = new FunctionProblem();
        problemMock.setProblemSettings(simulationSettingsMock.problem());
        problemMock.initialize();

        assertProblemSettings();
    }

    //TODO: Create test to check if problemData has all necessaries parameters

    @After
    public void then() throws ClassNotFoundException {

        String functionName = problemMock.getProblemSettings().getProblemData().get(0).get(0);

        Class functionClass = Class.forName(String.format(BimascoClassPath.PROBLEMS_FUNCTIONS, functionName));
        Type []type = functionClass.getGenericInterfaces();
        assertTrue(problemMock.getFunction().getClass() == functionClass);

        int variablesCount = Integer.parseInt(problemMock.getProblemSettings().getProblemData().get(1).get(0));
        assertEquals(2, variablesCount);

        double step = Double.parseDouble(problemMock.getProblemSettings().getProblemData().get(2).get(0));
        assertEquals(102.4, step, 0.01);

        ArrayList<String> initialPoint = problemMock.getProblemSettings().getProblemData().get(3);
        assertEquals("0.0", initialPoint.get(0));
        assertEquals("0.0", initialPoint.get(1));

        if(problemMock.getProblemSettings().getMax()){
            assertEquals(-FunctionProblem.DEFAULT_UPPER_BOUND, problemMock.getLimit());
        } else{
            assertEquals(FunctionProblem.DEFAULT_UPPER_BOUND, problemMock.getLimit());
        }

        //Think about domain variables
    }

    private void assertProblemSettings() {
        assertEquals(problemMock.getProblemSettings().getClassPath(), simulationSettingsMock.problem().getClassPath());
        assertEquals(problemMock.getProblemSettings().getName(), simulationSettingsMock.problem().getName());
        assertEquals(problemMock.getProblemSettings().getSolutionAnalyserName(), simulationSettingsMock.problem().getSolutionAnalyserName());
        assertEquals(problemMock.getProblemSettings().getType(), simulationSettingsMock.problem().getType());
        assertEquals(problemMock.getProblemSettings().getMax(), simulationSettingsMock.problem().getMax());
        assertEquals(problemMock.getProblemSettings().getProblemData(), simulationSettingsMock.problem().getProblemData());
    }
    */
}
