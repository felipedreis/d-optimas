package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.solutions.BinaryPartitionNumberSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.BinaryPartitionNumberElementSolution;
import br.cefetmg.lsi.bimasco.core.solutions.motion.BinaryPartitionSwapPerformsMotion;
import br.cefetmg.lsi.bimasco.core.solutions.motion.PerformsMotionHelper;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BinaryPartitionRandomSwapSolutionModifier extends
        SolutionModifier<BinaryPartitionNumberElementSolution, BinaryPartitionNumberSolution> {

    private BinaryPartitionSwapPerformsMotion swapMotion;

    private RandomDataGenerator rnd;

    public BinaryPartitionRandomSwapSolutionModifier(Map<String, Object> parameters) {
        super(parameters);
        swapMotion = (BinaryPartitionSwapPerformsMotion)
                PerformsMotionHelper.buildPerformsMotion("BinaryPartitionSwap");
        rnd = new RandomDataGenerator();
    }

    @Override
    public BinaryPartitionNumberSolution modify(BinaryPartitionNumberSolution solution, List<Integer> position,
                                                List<BinaryPartitionNumberElementSolution> parameter, Integer motionsCount) {
        BinaryPartitionNumberSolution result = solution;

        for (int i = 0; i < motionsCount; ++i) {
            int index1, index2;

            index1 = rnd.nextInt(0, solution.getProblem().getDimension());
            do {
                index2 = rnd.nextBinomial(0, solution.getProblem().getDimension());
            } while (index2 == index1);

            result = swapMotion.movement(result, List.of(index1, index2), Collections.emptyList());
        }
        return result;
    }
}
