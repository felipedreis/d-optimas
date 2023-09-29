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
import java.util.stream.IntStream;

public class AgentService extends AgentServiceGrpc.AgentServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);

    private ActorRef simulationActor;

    public AgentService(ActorRef simulationActor) {
        this.simulationActor = simulationActor;
    }

    private CompletableFuture<SimulationState> getSimulationState() {
        return ApiUtils.getSimulationStateCompletableFuture(simulationActor);
    }

    @Override
    public void listAgents(ListAgentRequest request, StreamObserver<ListAgentResponse> responseObserver) {
        logger.info("Doptimas API listAgents request {}", request);
        getSimulationState().thenAccept(state -> {
            List<String> metaheuristics = state.settings.getAgents().stream()
                    .flatMap( agentSettings ->
                                    IntStream.range(0, agentSettings.getCount())
                                            .boxed()
                                            .map(x -> agentSettings.getMetaHeuristicName()))
                    .collect(Collectors.toList());

            Map<String, String> agentPaths = state.agents.stream()
                    .collect(Collectors.toMap(ref -> ref.path().name(), ref -> ref.path().toString()));

            List<Agent> agents = IntStream.range(0, state.settings.getNumberOfAgents())
                    .boxed()
                    .map(i -> {
                            String agentName = "agent-" + i;
                            return Agent.newBuilder()
                                        .setName(agentName)
                                        .setMetaheuristic(metaheuristics.get(i))
                                        .setPath(agentPaths.get(agentName))
                                        .build();
                        }
                    ).collect(Collectors.toList());

            responseObserver.onNext(ListAgentResponse.newBuilder().addAllAgents(agents).build());
            responseObserver.onCompleted();
        }).join();
    }

    @Override
    public void describeAgent(DescribeAgentRequest request, StreamObserver<DescribeAgentResponse> responseObserver) {
        super.describeAgent(request, responseObserver);
    }
}
