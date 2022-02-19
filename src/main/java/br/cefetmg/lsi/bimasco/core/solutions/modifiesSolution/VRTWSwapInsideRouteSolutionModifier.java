package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.solutions.VehicleRoutingTimeWindowSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.VRTWElement;
import br.cefetmg.lsi.bimasco.core.solutions.motion.PerformsMotionHelper;
import br.cefetmg.lsi.bimasco.core.solutions.motion.VRTWSwapPerformsMotion;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.log4j.Logger;

import java.util.List;

import static java.lang.String.format;

public class VRTWSwapInsideRouteSolutionModifier
        extends SolutionModifier<VRTWElement, VehicleRoutingTimeWindowSolution> {

    private static Logger logger = Logger.getLogger(VRTWSwapInsideRouteSolutionModifier.class);

    VRTWSwapPerformsMotion swap;
    RandomDataGenerator rnd;

    public VRTWSwapInsideRouteSolutionModifier() {
        swap = (VRTWSwapPerformsMotion) PerformsMotionHelper.buildPerformsMotion("VRTWSwap");
        rnd = new RandomDataGenerator();
    }

    @Override
    public VehicleRoutingTimeWindowSolution modify(VehicleRoutingTimeWindowSolution solution, List<Integer> position,
                                                   List<VRTWElement> parameter, Integer motionsCount) {

        VehicleRoutingTimeWindowSolution modified = (VehicleRoutingTimeWindowSolution) solution.clone();

        for (int i = 0; i < motionsCount; ++i) {
            Integer [] indexes = choseRoute(modified);
            int routeBegin, routeEnd;
            routeBegin = indexes[0];
            routeEnd = indexes[1];

            modified = swapInsideRoute(modified, routeBegin, routeEnd);
        }

        return modified;
    }

    private Integer[] choseRoute(VehicleRoutingTimeWindowSolution solution) {
        List<Integer> routes = solution.getRoutes();
        int routeIndex, routeBegin, routeEnd;

        if (routes.size() <= 2)
            return new Integer[] {routes.get(0) + 1, routes.get(1) - 1};

        do {
            routeIndex = rnd.nextInt(0, routes.size() - 2);

            routeBegin = routes.get(routeIndex);
            routeEnd = routes.get(routeIndex + 1) - 1;

            logger.debug(format("Chose the %d-th route that starts on index %d and finishes on index %d",
                    routeIndex, routeBegin, routeEnd));

        } while (routeEnd - routeBegin == 1); //routes of size 1 cannot be

        return new Integer[] {routeBegin, routeEnd};
    }

    private VehicleRoutingTimeWindowSolution swapInsideRoute(VehicleRoutingTimeWindowSolution solution,
                                                             int routeBegin, int routeEnd) {
        int cityA, cityB;
        cityA = rnd.nextInt(routeBegin + 1, routeEnd);

        do {
            cityB = rnd.nextInt(routeBegin + 1, routeEnd);
        } while (cityB == cityA);

        return swap.movement(solution, List.of(cityA, cityB), List.of());
    }
}
