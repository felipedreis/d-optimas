package br.cefetmg.lsi.bimasco.api;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import br.cefetmg.lsi.bimasco.actors.BenchmarkState;
import br.cefetmg.lsi.bimasco.actors.Messages;
import br.cefetmg.lsi.bimasco.actors.SimulationState;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ApiUtils {

    public static CompletableFuture<SimulationState> getSimulationStateCompletableFuture(ActorRef simulationActor) {
        CompletableFuture<Object> t = Patterns.ask(simulationActor, new Messages.GetState(), Duration.ofSeconds(1))
                .toCompletableFuture();

        return t.thenApply(o ->
                (SimulationState) o
            );
    }

    public static CompletableFuture<BenchmarkState> getBenchmarkStateCompletableFuture(ActorRef benchmarkActor) {
        CompletableFuture<Object> t = Patterns.ask(benchmarkActor, new Messages.GetState(), Duration.ofSeconds(1))
                .toCompletableFuture();

        return t.thenApply(o ->
            (BenchmarkState) o
        );
    }

}
