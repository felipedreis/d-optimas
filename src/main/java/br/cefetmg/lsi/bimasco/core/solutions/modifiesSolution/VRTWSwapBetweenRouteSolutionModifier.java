package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.solutions.VehicleRoutingTimeWindowSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.VRTWElement;
import br.cefetmg.lsi.bimasco.core.solutions.motion.PerformsMotion;
import br.cefetmg.lsi.bimasco.core.solutions.motion.PerformsMotionHelper;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.List;

public class VRTWSwapBetweenRouteSolutionModifier
        extends SolutionModifier<VRTWElement, VehicleRoutingTimeWindowSolution> {

    private PerformsMotion swapMotion;
    private RandomDataGenerator rnd;

    public VRTWSwapBetweenRouteSolutionModifier() {
        swapMotion = PerformsMotionHelper.buildPerformsMotion("");
        rnd = new RandomDataGenerator();
    }

    @Override
    public VehicleRoutingTimeWindowSolution modify(VehicleRoutingTimeWindowSolution solution, List<Integer> position,
                                                   List<VRTWElement> parameter, Integer motionsCount) {
        List<Integer> routes = solution.getRoutes();

        for (int i = 0; i < motionsCount; ++i) {
            
        }


        return null;
    }
}
