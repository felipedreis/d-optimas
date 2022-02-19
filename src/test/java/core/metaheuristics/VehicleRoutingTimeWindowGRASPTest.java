package core.metaheuristics;

import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.GRASP;
import br.cefetmg.lsi.bimasco.core.problems.VehicleRoutingTimeWindowProblem;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class VehicleRoutingTimeWindowGRASPTest {
    private VehicleRoutingTimeWindowProblem vehicleRoutingTimeWindowProblem;

    private GRASP grasp;

    @BeforeEach
    public void init() {
        ProblemSettings problemSettings = new ProblemSettings();
        problemSettings.setName("VehicleRoutingTimeWindow");
        problemSettings.setMax(false);
        problemSettings.setProblemData(List.of(
            List.of(10, 200, 1000, 1000, 1000),
            List.of(1,40,50,0,0,1236,0),
            List.of(2,45,68,10,912,967,90),
            List.of(3,45,70,30,825,870,90),
            List.of(4,42,66,10,65,146,90),
            List.of(5,42,68,10,727,782,90),
            List.of(6,42,65,10,15,67,90),
            List.of(7,40,69,20,621,702,90),
            List.of(8,40,66,20,170,225,90),
            List.of(9,38,68,20,255,324,90),
            List.of(10,38,70,10,534,605,90)
        ));

        vehicleRoutingTimeWindowProblem = new VehicleRoutingTimeWindowProblem();
        vehicleRoutingTimeWindowProblem.setProblemSettings(problemSettings);

        vehicleRoutingTimeWindowProblem.initialize();

        AgentSettings agentSettings = new AgentSettings();
        agentSettings.setMetaHeuristicParameters(
                Map.of("maxIterations", 10,
                        "alpha", 0.5,
                        "stopConditionName", "MaxIterations",
                        "localSearchName", "Random",
                        "localSearchNeighbor", "VRTWSwapInsideRoute",
                        "localSearchIterations", 10,
                        "candidatesListName", "VRTW")
        );

        grasp = new GRASP(vehicleRoutingTimeWindowProblem);
        grasp.configureMetaHeuristic(agentSettings);
    }

    @Test
    public void test() {

        Set<Solution> results = new HashSet<>();

        for (int i = 0; i < 10; ++i) {
            List<Solution> solution = grasp.runMetaHeuristic(Collections.emptyList(), null);
            results.addAll(solution);
            System.out.println(solution.get(0));
        }
        assertTrue(results.size() > 1);

        results.forEach(System.out::println);

    }

}
