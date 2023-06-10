package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.solutions.BinaryPartitionNumberSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.BinaryPartitionNumberElementSolution;
import br.cefetmg.lsi.bimasco.core.solutions.motion.BinaryPartitionChangePerformsMotion;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BinaryPartitionRandomChangeSolutionModifier
        extends SolutionModifier<BinaryPartitionNumberElementSolution, BinaryPartitionNumberSolution> {

    private RandomDataGenerator rnd;

    private BinaryPartitionChangePerformsMotion motion;

    public BinaryPartitionRandomChangeSolutionModifier(Map<String, Object> parameters) {
        super(parameters);
        rnd = new RandomDataGenerator();
        motion = new BinaryPartitionChangePerformsMotion();
    }

    @Override
    public BinaryPartitionNumberSolution modify(BinaryPartitionNumberSolution solution, List<Integer> position,
                                                List<BinaryPartitionNumberElementSolution> parameter, Integer motionsCount) {
        BinaryPartitionNumberSolution result = solution;

        for (int i = 0; i < motionsCount; ++i) {
            int index = rnd.nextInt(0, solution.getProblem().getDimension() - 1);
            result = motion.movement(result, List.of(index), Collections.emptyList());
        }
        return result;
    }
}
