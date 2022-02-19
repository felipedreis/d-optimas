package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.VehicleRoutingTimeWindowSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.VRTWElement;

import java.util.List;

public class VRTWChangePerformsMotion implements PerformsMotion<VRTWElement, VehicleRoutingTimeWindowSolution> {
    
    @Override
    public VehicleRoutingTimeWindowSolution movement(VehicleRoutingTimeWindowSolution solution, List<Integer> position,
                                                     List<VRTWElement> parameters){
        PerformsMotion.super.movement(solution, position, parameters);

        if (position == null || position.size() < 2)
            throw new IllegalArgumentException("Change motion expects at least 2 positions");

        int pos1 = position.get(0);
        int pos2 = position.get(1);
        VRTWElement value = solution.getSolutionsVector().get(pos1);

        VehicleRoutingTimeWindowSolution moved = (VehicleRoutingTimeWindowSolution) solution.clone();
        moved.removeElementShifting(pos1);
        moved.addElement(pos2, value);

        return moved;
    }
}
