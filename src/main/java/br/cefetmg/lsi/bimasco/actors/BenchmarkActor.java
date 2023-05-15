package br.cefetmg.lsi.bimasco.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.cefetmg.lsi.bimasco.coco.*;
import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import coco.CocoJNI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static br.cefetmg.lsi.bimasco.actors.Messages.*;

public class BenchmarkActor extends AbstractActor {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkActor.class);

    private BenchmarkProblem benchmarkProblem;

    private CoCOBenchmark coCOBenchmark;

    private Suite suite;
    private Observer observer;

    private String name;

    private ActorRef simulationActor;

    private int evaluations;

    private boolean runningSimulation;

    public BenchmarkActor(ActorRef simulationActor, String name){
        this.simulationActor = simulationActor;
        this.name = name;
        init();
    }

    private void init() {
        try {

            suite = new Suite(name, name, "");
            observer = new Observer(name, "");

            coCOBenchmark = new CoCOBenchmark(suite, observer);
        } catch (Exception ex) {
            logger.error("Failed to initialize benchmark", ex);
        }
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Evaluate.class, this::handleEvaluate)
                .match(StartSimulation.class, this::startBenchmark)
                .match(SimulationStopped.class, this::handleSimulationEnd)
                .build();
    }

    private void nextProblem() {
        try {
            logger.info("Getting next benchmark problem");
            benchmarkProblem = coCOBenchmark.getNextProblem();
            if (benchmarkProblem != null) {
                logger.debug("Benchmark problem selected " + benchmarkProblem);
                benchmarkProblem.setBenchmarkActor(self());
                evaluations = 0;
            } else {
                logger.info("No next benchmark problem, terminating the simulation");
                stopBenchmark();
                context().parent().tell(new Terminate(), self());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleEvaluate(Evaluate evaluate) {
        logger.info("Handling evaluate of " + evaluate);
        if (!runningSimulation) {
            sender().tell(new EvaluateResult(null), self());
        } else {
            try {
                double[] y = CocoJNI.cocoEvaluateFunction(benchmarkProblem.getPointer(), evaluate.x);
                sender().tell(new EvaluateResult(y), self());
                evaluations++;
                checkStopCondition();
            } catch (Exception ex) {
                logger.error("Couldn't evaluate the request " + evaluate, ex);
            }
        }
    }

    private void checkStopCondition() {
        if (evaluations == benchmarkProblem.getEvaluations()) {
            simulationActor.tell(new StopSimulation(), self());
            runningSimulation = false;
        }
    }

    private void startBenchmark(StartSimulation startSimulation){
        logger.info("Simulation started " + startSimulation);
        nextProblem();
        startSimulation();
    }

    private void startSimulation() {
        simulationActor.tell(new StartSimulation(benchmarkProblem), self());
    }

    private void handleSimulationEnd(SimulationStopped stopped) {
        nextProblem();
        startSimulation();
    }

    private void stopBenchmark() {
        try {
            coCOBenchmark.finalizeBenchmark();
        } catch (Exception ex) {
            logger.error("Couldn't finalize the benchmark correctly", ex);
        }
    }
}
