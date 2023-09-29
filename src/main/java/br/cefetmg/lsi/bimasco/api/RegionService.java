package br.cefetmg.lsi.bimasco.api;

import akka.actor.ActorRef;
import br.cefetmg.lsi.bimasco.actors.SimulationState;
import io.grpc.stub.StreamObserver;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RegionService extends RegionServiceGrpc.RegionServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    private ActorRef simulationActor;

    public RegionService(ActorRef simulationActor) {
        this.simulationActor = simulationActor;
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
        super.describeRegion(request, responseObserver);
    }

}
