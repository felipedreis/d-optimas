package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.BinaryPartitionNumberSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.BinaryPartitionNumberElementSolution;

import java.util.List;

public class BinaryPartitionSwapPerformsMotion
		implements PerformsMotion<BinaryPartitionNumberElementSolution, BinaryPartitionNumberSolution> {

    @Override
    public BinaryPartitionNumberSolution movement(BinaryPartitionNumberSolution solution, List<Integer> position,
												  List<BinaryPartitionNumberElementSolution> parameters){
    	PerformsMotion.super.movement(solution, position, parameters);

		int index1 = position.get(0);
		int index2 = position.get(1);

		//Integer value1 = solution.getSolutionsVector().get(index1);
		//Integer value2 = solution.getSolutionsVector().get(index2);
		BinaryPartitionNumberSolution moved = (BinaryPartitionNumberSolution) solution.clone();

		//moved.setElement(index1, value2);
		//moved.setElement(index2, value1);

		return moved;
    }
}
