package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.VehicleRoutingTimeWindowSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.VRTWElement;

import java.util.List;

public class VRTWSwapPerformsMotion implements PerformsMotion<VRTWElement, VehicleRoutingTimeWindowSolution> {
    @Override
    public VehicleRoutingTimeWindowSolution movement(VehicleRoutingTimeWindowSolution solution, List<Integer> position,
                                                     List<VRTWElement> values) {
        if (position == null || position.size() < 2)
            throw new IllegalArgumentException("Swap movement needs at least 2 positions to swap");

        int pos1 = position.get(0);
        int pos2 = position.get(1);

        VehicleRoutingTimeWindowSolution moved = (VehicleRoutingTimeWindowSolution) solution.clone();
        moved.setElement(pos1, solution.getSolutionsVector().get(pos2));
        moved.setElement(pos2, solution.getSolutionsVector().get(pos1));

        return moved;
    }
}
