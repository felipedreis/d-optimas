package br.cefetmg.lsi.bimasco.api;

import akka.actor.ActorRef;
import br.cefetmg.lsi.bimasco.actors.Messages;
import br.cefetmg.lsi.bimasco.actors.SimulationState;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class SimulationService extends SimulationServiceGrpc.SimulationServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(SimulationService.class);

    private final ActorRef simulationActor;

    public SimulationService(ActorRef simulationActor) {
        this.simulationActor = simulationActor;
    }

    private CompletableFuture<SimulationState> getSimulationState() {
        return ApiUtils.getSimulationStateCompletableFuture(simulationActor);
    }

    @Override
    public void statSimulation(StatSimulationRequest request, StreamObserver<StatSimulationResponse> responseObserver) {
        logger.info("Doptimas API statSimulation request");
        getSimulationState().thenAccept(state -> {
            StatSimulationResponse response = StatSimulationResponse.newBuilder()
                    .setStatus(mapStatus(state.status))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }).exceptionally(ex -> {
            logger.error("Error getting simulation state", ex);
            responseObserver.onError(ex);
            return null;
        });
    }

    @Override
    public void startSimulation(StartSimulationRequest request, StreamObserver<StatSimulationResponse> responseObserver) {
        logger.info("Doptimas API startSimulation request");
        simulationActor.tell(new Messages.StartSimulation(), ActorRef.noSender());
        StatSimulationResponse response = StatSimulationResponse.newBuilder()
                .setStatus(SimulationStatus.STARTED)
                .setMessage("Simulation start requested")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void stopSimulation(StopSimulationRequest request, StreamObserver<StatSimulationResponse> responseObserver) {
        logger.info("Doptimas API stopSimulation request");
        simulationActor.tell(new Messages.StopSimulation(), ActorRef.noSender());
        StatSimulationResponse response = StatSimulationResponse.newBuilder()
                .setStatus(SimulationStatus.STOPPED)
                .setMessage("Simulation stop requested")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private SimulationStatus mapStatus(SimulationState.Status status) {
        if (status == null) return SimulationStatus.UNRECOGNIZED;
        switch (status) {
            case READY: return SimulationStatus.READY;
            case STARTED: return SimulationStatus.STARTED;
            case STOPPED: return SimulationStatus.STOPPED;
            case FINISHED: return SimulationStatus.FINISHED;
            case FAILED: return SimulationStatus.FAILED;
            default: return SimulationStatus.UNRECOGNIZED;
        }
    }
}
