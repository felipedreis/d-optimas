package br.cefetmg.lsi.bimasco.api;

import io.grpc.stub.StreamObserver;

public class SimulationService extends SimulationServiceGrpc.SimulationServiceImplBase {
    @Override
    public void statSimulation(StatSimulationRequest request, StreamObserver<StatSimulationResponse> responseObserver) {
        super.statSimulation(request, responseObserver);
    }

    @Override
    public void startSimulation(StartSimulationRequest request, StreamObserver<StatSimulationResponse> responseObserver) {
        super.startSimulation(request, responseObserver);
    }

    @Override
    public void stopSimulation(StopSimulationRequest request, StreamObserver<StatSimulationResponse> responseObserver) {
        super.stopSimulation(request, responseObserver);
    }
}
