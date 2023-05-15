package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.VehicleRoutingTimeWindowProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.VRTWElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class VehicleRoutingTimeWindowSolution extends Solution <VRTWElement, Double, VehicleRoutingTimeWindowProblem> {

    private static final Logger logger = LoggerFactory.getLogger(VehicleRoutingTimeWindowSolution.class);

    public VehicleRoutingTimeWindowSolution(Problem problem) {
        super(problem);
    }

    public VehicleRoutingTimeWindowSolution(VehicleRoutingTimeWindowSolution s) {
        super(s);
    }

    public int compareTo(Solution o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initialize() {
        functionValue = Double.POSITIVE_INFINITY;
    }

    @Override
    public void generateInitialSolution() {

    }

    @Override
    public Double adaptiveFunctionValue(VRTWElement value) {
        double totalDistance = 0.0;

        List<VRTWElement> cities = new ArrayList<>();
        cities.add(new VRTWElement(0));
        cities.addAll(solutionsVector);

        if (cities.get(cities.size() - 1).getCity() == 0 && value.getCity() == 0)
            return null;

        cities.add(value);

        int routesCount = 0;


        for (int i = 0; i < cities.size() - 1; ++i) {
            VRTWElement client, nextClient;
            client = cities.get(i);
            nextClient = cities.get(i + 1);

            if (client == null || nextClient == null)
                continue;
            int idxClient = client.getCity();
            int idxNextClient = nextClient.getCity();

            if (idxClient == 0)
                routesCount += 1;

            totalDistance += getProblem().getDistances()[idxClient][idxNextClient];
        }

        return routesCount * totalDistance;
    }

    @Override
    protected void objectiveFunction() {
        functionValue = 0.0;

        if (solutionsVector.isEmpty())
            return;

        double truckLoad = 0, truckPenalty= 0;
        double time = 0, timePenalty = 0;
        int routesCount = 0;


        List<VRTWElement> cities = new ArrayList<>();
        cities.add(new VRTWElement(0));
        cities.addAll(solutionsVector);

        if (cities.get(cities.size() - 1).getCity() != 0)
            cities.add(new VRTWElement(0));

        for (int i = 0; i < cities.size() - 1; ++i) {
            VRTWElement client, nextClient;
            client = cities.get(i);
            nextClient = cities.get(i + 1);

            if (client == null || nextClient == null)
                continue;

            int idxClient = client.getCity();
            int idxNextClient = nextClient.getCity();

            functionValue += getProblem().getDistances()[idxClient][idxNextClient];
            truckLoad += getProblem().getClientDemands()[idxNextClient];
            // time arrival is greater than the time the client closes, so it should pay a penalty
            if (time > getProblem().getTimeWindow()[idxNextClient][1])
                timePenalty += time - getProblem().getTimeWindow()[idxNextClient][1];
            // time arrival is lesser thant the time the client opens, so it should wait the diff between arrival and opening
            if (time < getProblem().getTimeWindow()[idxNextClient][0])
                time += getProblem().getTimeWindow()[idxNextClient][0] - time;

            time += getProblem().getServiceTime()[idxNextClient];

            if (idxNextClient == 0) {
                if (truckLoad > getProblem().getTruckCapacity())
                    truckPenalty += Math.abs(truckLoad - getProblem().getTruckCapacity());

                truckLoad = 0;
                routesCount += 1;
                time = 0;
            }
        }

        logger.debug(format("Truck penalty: %s", truckPenalty));
        logger.debug(format("Routes penalty: %s", routesCount));
        logger.debug(format("Time penalty: %s", timePenalty));
        logger.debug(format("Function value before penalties is: %s", functionValue));

        functionValue += getProblem().getTruckPenaltyWeight() * truckPenalty +
                getProblem().getRoutesPenaltyWeight() * routesCount +
                getProblem().getTimePenaltyWeight() * timePenalty;
    }

    @Override
    protected void checkViability() {
        double truckLoad = 0;
        double time = 0;
        viable = true;

        List<VRTWElement> cities = getFullRoute();

        for (int i = 0; i < cities.size() - 1 && viable; ++i) {
            VRTWElement client, nextClient;
            client = cities.get(i);
            nextClient = cities.get(i + 1);

            if (client == null || nextClient == null)
                continue;

            int idxClient = client.getCity();
            int idxNextClient = nextClient.getCity();

            if (idxClient == 0) {
                //if(truckLoad > getProblem().getTruckCapacity())
                //    viable = false;

                truckLoad = 0;
                time = 0;
            }

            truckLoad += getProblem().getClientDemands()[idxNextClient];

            //if (time > getProblem().getTimeWindow()[idxNextClient][1])
            //    viable = false;

            if (time < getProblem().getTimeWindow()[idxNextClient][0])
                time += getProblem().getTimeWindow()[idxNextClient][0] - time;

            time += getProblem().getServiceTime()[idxNextClient];
        }
    }

    private List<VRTWElement> getFullRoute() {

        List<VRTWElement> cities = new ArrayList<>();
        cities.add(new VRTWElement(0));
        cities.addAll(solutionsVector);

        if (cities.get(cities.size() - 1).getCity() != 0)
            cities.add(new VRTWElement(0));

        return cities;
    }

    public List<Integer> getRoutes() {
        List<Integer> routes = new ArrayList<>();

        routes.add(-1);

        for(int i = 0; i < getSolutionsVector().size(); ++i) {
            if (getSolutionsVector().get(i) == null)
                break;

            if (getSolutionsVector().get(i).getCity() == 0)
                routes.add(i);
        }

        if (getSolutionsVector().lastIndexOf(new VRTWElement(0)) != getSolutionsVector().size() - 1)
            routes.add(getSolutionsVector().size());

        return routes;
    }

    @Override
    public Object clone() {
        return new VehicleRoutingTimeWindowSolution(this);
    }
}