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
import java.util.stream.IntStream;

public class AgentService extends AgentServiceGrpc.AgentServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);

    private ActorRef simulationActor;
    private ActorRef agentShard;

    public AgentService(ActorRef simulationActor, ActorRef agentShard) {
        this.simulationActor = simulationActor;
        this.agentShard = agentShard;
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
        logger.info("Doptimas API describeAgent request {}", request);
        String agentId = request.getAgentId();
        int id = Integer.parseInt(agentId.split("-")[1]);

        Patterns.ask(agentShard, new Messages.GetState(id), Duration.ofSeconds(5))
                .toCompletableFuture()
                .thenAccept(obj -> {
                    if (obj instanceof Messages.DetailedAgentState) {
                        Messages.DetailedAgentState state = (Messages.DetailedAgentState) obj;
                        DescribeAgentResponse.Builder builder = DescribeAgentResponse.newBuilder()
                                .setAgentId(state.agentId)
                                .setLifetime(state.lifetime)
                                .setStartTime(state.startTime)
                                .setCurrentTime(state.currentTime)
                                .setCompleteExecutions(state.completeExecutions)
                                .setRequiredSolutions(state.requiredSolutions)
                                .setHeuristic(state.heuristic)
                                .setMemoryTax(state.memoryTax)
                                .putAllMemory(state.qTable.entrySet().stream()
                                        .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)));

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
                        responseObserver.onError(new RuntimeException("Unexpected response from agent: " + obj));
                    }
                }).exceptionally(ex -> {
                    responseObserver.onError(ex);
                    return null;
                });
    }
}
