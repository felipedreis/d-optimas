package br.cefetmg.lsi.bimasco.api;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import br.cefetmg.lsi.bimasco.actors.Messages;
import br.cefetmg.lsi.bimasco.actors.SimulationState;
import io.grpc.stub.StreamObserver;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class RegionService extends RegionServiceGrpc.RegionServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    private final ActorRef simulationActor;
    private final ActorRef regionShard;

    public RegionService(ActorRef simulationActor, ActorRef regionShard) {
        this.simulationActor = simulationActor;
        this.regionShard = regionShard;
    }

    private CompletableFuture<SimulationState> getSimulationState() {
        return ApiUtils.getSimulationStateCompletableFuture(simulationActor);
    }

    @Override
    public void listRegions(ListRegionsRequest request, StreamObserver<ListRegionsResponse> responseObserver) {
        logger.info("DOptimas API listRegions request {}", request);
        getSimulationState().thenAccept(state -> {
            Map<String, String> regionPaths = state.regions.stream()
                    .collect(Collectors.toMap(actorRef -> actorRef.path().name(), actorRef -> actorRef.path().toString()));

            List<Region> regions = regionPaths.keySet().stream()
                    .map(regionName -> {
                        StatisticalSummary regionStats = state.regionStats.get(regionName);
                        return Region.newBuilder()
                                .setName(regionName)
                                .setPath(regionPaths.get(regionName))
                                .setNumberOfSolutions(regionStats.getN())
                                .build();
                    }).collect(Collectors.toList());

            responseObserver.onNext(ListRegionsResponse.newBuilder().addAllRegions(regions).build());
            responseObserver.onCompleted();
        }).join();
    }


    @Override
    public void describeRegion(DescribeRegionRequest request, StreamObserver<DescribeRegionResponse> responseObserver) {
        logger.info("Doptimas API describeRegion request {}", request);
        String regionId = request.getRegionId();
        int id = Integer.parseInt(regionId.split("-")[1]);

        Patterns.ask(regionShard, new Messages.GetState(id), Duration.ofSeconds(5))
                .toCompletableFuture()
                .thenAccept(obj -> {
                    if (obj instanceof Messages.DetailedRegionState) {
                        Messages.DetailedRegionState state = (Messages.DetailedRegionState) obj;
                        DescribeRegionResponse.Builder builder = DescribeRegionResponse.newBuilder()
                                .setRegionId(state.regionId)
                                .setStartedTime(state.startedTime)
                                .setCurrentTime(state.currentTime)
                                .setStarted(state.started)
                                .setNumberOfSolutions(state.numberOfSolutions)
                                .addObjectiveFunctionAverage(state.average)
                                .addObjectiveFunctionStd(state.std);

                        if (state.bestSolution != null) {
                            List<Double> x = DoubleStream.of(state.bestSolution.toDoubleArray()).boxed().collect(Collectors.toList());
                            List<Double> y = List.of(state.bestSolution.getFunctionValue().doubleValue());

                            builder.setBestSolution(Solution.newBuilder()
                                    .setId(state.bestSolution.getId().toString())
                                    .addAllX(x)
                                    .addAllY(y)
                                    .build());
                        }

                        responseObserver.onNext(builder.build());
                        responseObserver.onCompleted();
                    } else {
                        responseObserver.onError(new RuntimeException("Unexpected response from region: " + obj));
                    }
                }).exceptionally(ex -> {
                    responseObserver.onError(ex);
                    return null;
                });
    }

}
