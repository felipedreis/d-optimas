package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.BinaryPartitionNumberSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.BinaryPartitionNumberElementSolution;

import java.util.List;

public class BinaryPartitionSwapPerformsMotion
		implements PerformsMotion<BinaryPartitionNumberElementSolution, BinaryPartitionNumberSolution> {

    @Override
    public BinaryPartitionNumberSolution movement(BinaryPartitionNumberSolution solucao, List<Integer> posicao,
												  List<BinaryPartitionNumberElementSolution> parametro){
    	PerformsMotion.super.movement(solucao, posicao, parametro);

		int index1 = posicao.get(0);
		int index2 = posicao.get(1);

		//Integer value1 = solucao.getSolutionsVector().get(index1);
		//Integer value2 = solucao.getSolutionsVector().get(index2);
		BinaryPartitionNumberSolution moved = (BinaryPartitionNumberSolution) solucao.clone();

		//moved.setElement(index1, value2);
		//moved.setElement(index2, value1);

		return moved;
    }
}
