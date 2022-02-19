package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.BinaryPartitionNumberSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.BinaryPartitionNumberElementSolution;

import java.util.List;

//TODO: change elements name
public class BinaryPartitionChangePerformsMotion
        implements PerformsMotion<BinaryPartitionNumberElementSolution, BinaryPartitionNumberSolution> {

    @Override
    public BinaryPartitionNumberSolution movement(BinaryPartitionNumberSolution solucao, List<Integer> posicao,
                                                   List<BinaryPartitionNumberElementSolution> parametro){
        int index = posicao.get(0);
        BinaryPartitionNumberElementSolution value = solucao.getSolutionsVector().get(index);
        BinaryPartitionNumberSolution moved = (BinaryPartitionNumberSolution) solucao.clone();

        if (value.isPresent()) {
            moved.setElement(index, new BinaryPartitionNumberElementSolution(false));
        } else {
            moved.setElement(index, new BinaryPartitionNumberElementSolution(true));
        }

        return moved;
    }
}
