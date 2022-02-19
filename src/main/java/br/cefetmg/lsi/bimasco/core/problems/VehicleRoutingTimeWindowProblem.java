/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.lsi.bimasco.core.problems;

import br.cefetmg.lsi.bimasco.core.Problem;

import java.util.ArrayList;
import java.util.List;

public class VehicleRoutingTimeWindowProblem extends Problem {

    private int clients;
    private int truckCapacity;
    private Integer timePenaltyWeight;
    private Integer truckPenaltyWeight;
    private Integer routesPenaltyWeight;
    private Integer[][] timeWindow;
    private Integer[][] clientPositions;
    private Double[][] distances;
    private Integer[] clientDemands;
    private Integer[] serviceTime;

    public VehicleRoutingTimeWindowProblem(){
        super();
    }

    @Override
    public void initialize() {

        List<List> dadosProblema = this.getProblemSettings().getProblemData();

        clients = (int) dadosProblema.get(0).get(0);
        truckCapacity = (int) dadosProblema.get(0).get(1);
        truckPenaltyWeight = (int) dadosProblema.get(0).get(2);
        timePenaltyWeight = (int) dadosProblema.get(0).get(3);
        routesPenaltyWeight = (int) dadosProblema.get(0).get(4);
        clientPositions = new Integer[clients + 1][2];
        timeWindow = new Integer[clients + 1][2];
        serviceTime = new Integer[clients + 1];
        clientDemands = new Integer[clients + 1];
        distances = new Double[clients + 1][clients + 1];

        clientPositions[0][0] = 0;
        clientPositions[0][1] = 0;
        serviceTime[0] = 0;
        timeWindow[0][0] = 0;
        timeWindow[0][1] = Integer.MAX_VALUE;
        clientDemands[0] = 0;

        for(int i = 1; i < dadosProblema.size(); i++){
            int client = (int) dadosProblema.get(i).get(0);
            clientPositions[client][0] = (int) dadosProblema.get(i).get(1);
            clientPositions[client][1] = (int) dadosProblema.get(i).get(2);

            clientDemands[client] = (int) dadosProblema.get(i).get(3);

            timeWindow[client][0] = (int) dadosProblema.get(i).get(4);
            timeWindow[client][1] = (int) dadosProblema.get(i).get(5);

            serviceTime[client] = (int) dadosProblema.get(i).get(6);

        }

        double valX;
        double valY;

        for(int i = 0; i <= clients; i++){
            for(int j = 0; j <= clients; j++){
                if (i == j)
                    distances[i][j] = Double.MAX_VALUE;
                else {
                    valX = Math.pow(clientPositions[i][0] - clientPositions[j][0], 2);
                    valY = Math.pow(clientPositions[i][1] - clientPositions[j][1], 2);
                    distances[i][j] = Math.sqrt(valX + valY);
                }
            }
        }

        dimension = 2 * clients;
    }

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckCapacity(int truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public Integer getTimePenaltyWeight() {
        return timePenaltyWeight;
    }

    public void setTimePenaltyWeight(Integer timePenaltyWeight) {
        this.timePenaltyWeight = timePenaltyWeight;
    }

    public Integer getTruckPenaltyWeight() {
        return truckPenaltyWeight;
    }

    public void setTruckPenaltyWeight(Integer truckPenaltyWeight) {
        this.truckPenaltyWeight = truckPenaltyWeight;
    }

    public Integer getRoutesPenaltyWeight() {
        return routesPenaltyWeight;
    }

    public void setRoutesPenaltyWeight(Integer routesPenaltyWeight) {
        this.routesPenaltyWeight = routesPenaltyWeight;
    }

    public Integer[][] getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(Integer[][] timeWindow) {
        this.timeWindow = timeWindow;
    }

    public Integer[][] getClientPositions() {
        return clientPositions;
    }

    public void setClientPositions(Integer[][] clientPositions) {
        this.clientPositions = clientPositions;
    }

    public Double[][] getDistances() {
        return distances;
    }

    public void setDistances(Double[][] distances) {
        this.distances = distances;
    }

    public Integer[] getClientDemands() {
        return clientDemands;
    }

    public void setClientDemands(Integer[] clientDemands) {
        this.clientDemands = clientDemands;
    }

    public Integer[] getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Integer[] serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public Object getFitnessFunction(List<Object> element) {
        return null;
    }

    @Override
    public Integer getSolutionElementsCount() {
        return getDimension();
    }

    @Override
    public double getStep() {
        return 0;
    }

    @Override
    public Object getLimit() {
        return 0;
    }

    @Override
    public ArrayList<Object> getParameters() {
        return null;
    }
}