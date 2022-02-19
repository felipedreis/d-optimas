package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

import java.util.List;

public class RealSwapPerformsMotion implements PerformsMotion<FunctionSolutionElement, FunctionSolution>  {
    @Override
    public FunctionSolution movement(FunctionSolution solution, List<Integer> position,
                                     List<FunctionSolutionElement> values) {
        if (position == null || position.size() < 2)
            throw new IllegalArgumentException("Expected at least 2 positions to swap");

        int i, j;
        i = position.get(0);
        j = position.get(1);

        FunctionSolution moved = (FunctionSolution) solution.clone();
        FunctionSolutionElement v1, v2;
        v1 = moved.getSolutionsVector().get(i);
        v2 = moved.getSolutionsVector().get(j);

        moved.setElement(i, v2);
        moved.setElement(j, v1);

        return moved;
    }
}
