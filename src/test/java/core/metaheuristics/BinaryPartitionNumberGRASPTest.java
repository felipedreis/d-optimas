package core.metaheuristics;

import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.GRASP;
import br.cefetmg.lsi.bimasco.core.problems.BinaryPartitionNumberProblem;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class BinaryPartitionNumberGRASPTest {
    private BinaryPartitionNumberProblem problem;

    private AgentSettings agentSettings;

    private GRASP grasp;

    @BeforeEach
    void init() {
        ProblemSettings problemSettings = new ProblemSettings();
        problemSettings.setName("BinaryPartitionNumber");
        problemSettings.setProblemData(List.of(
                List.of(),
                List.of(),
                List.of(10),
                List.of(),
                List.of(2),
                List.of(),
                List.of(23528), List.of(79348), List.of(9936), List.of(41503), List.of(59207),
                List.of(7341), List.of(62668), List.of(12646), List.of(30479), List.of(24622)
        ));
        problem = new BinaryPartitionNumberProblem();

        problem.setProblemSettings(problemSettings);
        problem.initialize();

        agentSettings = new AgentSettings();

        agentSettings.setMetaHeuristicParameters(
                Map.of("maxIterations", 10,
                "alpha", 1.0,
                "stopConditionName", "MaxIterations",
                "localSearchName", "Random",
                "localSearchNeighbor", "BinaryPartitionRandomChange",
                "localSearchIterations", 10,
                "candidatesListName", "BinaryPartition")
        );
        grasp = new GRASP(problem);
        grasp.configureMetaHeuristic(agentSettings);
    }

    @Test
    public void testWithBinaryPartition() {

        Set<Solution> results = new HashSet<>();

        for (int i = 0; i < 10; ++i)
            results.addAll(grasp.runMetaHeuristic(Collections.emptyList(), null));

        assertTrue(results.size() > 1);

        results.forEach(System.out::println);

    }

}
