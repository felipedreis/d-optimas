package br.cefetmg.lsi.bimasco.api;

import akka.actor.ActorRef;
import io.grpc.stub.StreamObserver;

public class BenchmarkService extends BenchmarkServiceGrpc.BenchmarkServiceImplBase {

    private ActorRef benchmarkActor;

    public BenchmarkService(ActorRef benchmarkActor){
        this.benchmarkActor = benchmarkActor;
    }

    @Override
    public void statBenchmark(StatBenchmarkRequest request, StreamObserver<StatBenchmarkResponse> responseObserver) {

        if (benchmarkActor == null)
            responseObserver.onNext(StatBenchmarkResponse.newBuilder()
                    .build());

        ApiUtils.getBenchmarkStateCompletableFuture(benchmarkActor).thenAccept(
                benchmarkState -> {
                    responseObserver.onNext(StatBenchmarkResponse.newBuilder()
                                    .setSuite(benchmarkState.getSuite().getName())
                                    .setEvaluations(benchmarkState.getEvaluations())
                                    .setEvaluationBudget(benchmarkState.getEvaluationBudget())
                                    .setProblemId(benchmarkState.getProblem().getId())
                                    .setTotalProblems(benchmarkState.getSuite().getNumberOfProblems())
                                    .setEvaluatedProblems(benchmarkState.getProblemCounter())
                            .build());
                    responseObserver.onCompleted();
                }
        );
        super.statBenchmark(request, responseObserver);
    }
}
