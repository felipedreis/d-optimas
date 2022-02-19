package core.regions;

import br.cefetmg.lsi.bimasco.core.regions.Region;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;

import java.util.ArrayList;
import java.util.UUID;

public class RegionTests {
/*
    private Region regionMock;
    private FunctionProblem problemMock;
    //Or create a manually simulationSettings
    private SimulationSettingsMock simulationSettingsMock;

    @Before
    public void givenSimulationSettingsAndRegionInstances() {
        simulationSettingsMock = ConfigFactory.create(SimulationSettingsMock.class);
        regionMock = new Region(simulationSettingsMock.region(), "SId");

        problemMock = new FunctionProblem();
        problemMock.setProblemSettings(simulationSettingsMock.problem());
        problemMock.initialize();

        assertNotNull(simulationSettingsMock);
        assertNotNull(simulationSettingsMock.region());
        assertNotNull(regionMock);

    }

    @Test
    public void whenRegionAddASolution() {

        double initialTotalSumOfSolutions = simulationSettingsMock.region().getTotalSumOfSolutions();

        FunctionSolution s1 = new FunctionSolution(problemMock);
        s1.initialize();

        ArrayList<Solution> solutions = new ArrayList<Solution>();
        solutions.add(s1);

        regionMock.addSolutionsCollection(solutions, new UUID(0L, 0L));

        //Check total sum and centroid of region

        assertNotNull(regionMock.getSolutionList());
        assertEquals(solutions.size(), regionMock.getSolutionList().size());
        assertEquals((Double)s1.getFunctionValue(), regionMock.getRegionSettings().getMinValueFitnessFunction(), 0.1);
        assertEquals(initialTotalSumOfSolutions + (Double) s1.getFunctionValue(), regionMock.getRegionSettings().getTotalSumOfSolutions(), 0.1);

        double expectedCentroid = regionMock.getRegionSettings().getTotalSumOfSolutions() / regionMock.getSolutionList().size();

        assertEquals(expectedCentroid, regionMock.getRegionSettings().getCentroid(), 0.1);
    }

    @Test
    public void whenRegionAddSolutionsCollection() {

        double initialTotalSumOfSolutions = simulationSettingsMock.region().getTotalSumOfSolutions();

        FunctionSolution s1 = new FunctionSolution(problemMock);
        FunctionSolution s2 = new FunctionSolution(problemMock);
        s1.initialize();
        s2.initialize();

        ArrayList<Solution> solutions = new ArrayList<Solution>();
        solutions.add(s1);
        solutions.add(s2);

        regionMock.addSolutionsCollection(solutions, new UUID(0L, 0L));

        //Check total sum and centroid of region

        assertNotNull(regionMock.getSolutionList());
        assertEquals(solutions.size(), regionMock.getSolutionList().size());

        double totalSumOfSolutions = initialTotalSumOfSolutions
                + (Double) s1.getFunctionValue()
                + (Double) s2.getFunctionValue();

        assertEquals(totalSumOfSolutions, regionMock.getRegionSettings().getTotalSumOfSolutions(), 0.1);

        double expectedCentroid = regionMock.getRegionSettings().getTotalSumOfSolutions() / regionMock.getSolutionList().size();

        assertEquals(expectedCentroid, regionMock.getRegionSettings().getCentroid(), 0.1);

    }

    @After
    public void then() {

    }
    */
}
