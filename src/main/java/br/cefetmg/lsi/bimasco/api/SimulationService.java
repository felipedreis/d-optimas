package br.cefetmg.lsi.bimasco.api;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import br.cefetmg.lsi.bimasco.actors.Messages;
import br.cefetmg.lsi.bimasco.actors.SimulationState;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class SimulationService extends SimulationServiceGrpc.SimulationServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(SimulationService.class);

    private final ActorRef simulationActor;
    private final ActorRef mainActor;

    public SimulationService(ActorRef simulationActor, ActorRef mainActor) {
        this.simulationActor = simulationActor;
        this.mainActor = mainActor;
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

        if (request.getConfigurationFileContent() != null && !request.getConfigurationFileContent().isEmpty()) {
            try {
                logger.info("Received configuration content, updating simulation");
                Config config = ConfigFactory.parseString(request.getConfigurationFileContent());
                SimulationSettings settings = new SimulationSettings(config);
                mainActor.tell(new Messages.ConfigureSimulation(settings), ActorRef.noSender());
            } catch (Exception ex) {
                logger.error("Error parsing configuration", ex);
                responseObserver.onError(ex);
                return;
            }
        }

        // Wait for SimulationStarted confirmation before getting state
        Patterns.ask(simulationActor, new Messages.StartSimulation(), Duration.ofSeconds(5))
                .toCompletableFuture()
                .thenCompose(ack -> getSimulationState())
                .thenAccept(state -> {
                    StatSimulationResponse response = StatSimulationResponse.newBuilder()
                            .setStatus(mapStatus(state.status))
                            .setMessage("Simulation started")
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }).exceptionally(ex -> {
                    logger.error("Error starting simulation", ex);
                    responseObserver.onError(ex);
                    return null;
                });
    }

    @Override
    public void stopSimulation(StopSimulationRequest request, StreamObserver<StatSimulationResponse> responseObserver) {
        logger.info("Doptimas API stopSimulation request");

        // Wait for SimulationStopped confirmation before getting state
        Patterns.ask(simulationActor, new Messages.StopSimulation(), Duration.ofSeconds(5))
                .toCompletableFuture()
                .thenCompose(ack -> getSimulationState())
                .thenAccept(state -> {
                    StatSimulationResponse response = StatSimulationResponse.newBuilder()
                            .setStatus(mapStatus(state.status))
                            .setMessage("Simulation stopped")
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }).exceptionally(ex -> {
                    logger.error("Error stopping simulation", ex);
                    responseObserver.onError(ex);
                    return null;
                });
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
