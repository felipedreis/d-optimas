package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.BinaryPartitionNumberSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.BinaryPartitionNumberElementSolution;

import java.util.List;

//TODO: change elements name
public class BinaryPartitionChangePerformsMotion
        implements PerformsMotion<BinaryPartitionNumberElementSolution, BinaryPartitionNumberSolution> {

    @Override
    public BinaryPartitionNumberSolution movement(BinaryPartitionNumberSolution solution, List<Integer> position,
                                                   List<BinaryPartitionNumberElementSolution> parameters){
        int index = position.get(0);
        BinaryPartitionNumberElementSolution value = solution.getSolutionsVector().get(index);
        BinaryPartitionNumberSolution moved = (BinaryPartitionNumberSolution) solution.clone();

        if (value.isPresent()) {
            moved.setElement(index, new BinaryPartitionNumberElementSolution(false));
        } else {
            moved.setElement(index, new BinaryPartitionNumberElementSolution(true));
        }

        return moved;
    }
}
