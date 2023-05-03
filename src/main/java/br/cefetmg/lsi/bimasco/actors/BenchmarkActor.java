package br.cefetmg.lsi.bimasco.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.cefetmg.lsi.bimasco.coco.*;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import com.typesafe.config.Config;
import org.apache.log4j.Logger;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;

public class BenchmarkActor extends AbstractActor {

    private static Logger logger = Logger.getLogger(BenchmarkActor.class);

    private BenchmarkProblem benchmarkProblem;

    private CoCOBenchmark coCOBenchmark;

    private Suite suite;
    private Observer observer;

    private Config config;

    private ActorRef simulationActor;

    private int evaluations;

    private boolean runningSimulation;

    public BenchmarkActor(){
    }

    private void init() {
        try {

            suite = new Suite("", "", "");
            observer = new Observer("", "");

            coCOBenchmark = new CoCOBenchmark(suite, observer);
        } catch (Exception ex) {

        }
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Evaluate.class, this::handleEvaluate)
                .build();
    }

    private void nextProblem() {
        try {
            benchmarkProblem = coCOBenchmark.getNextProblem();
            if (benchmarkProblem != null) {
                benchmarkProblem.setBenchmarkActor(self());
                evaluations = 0;
            } else {
                stopBenchmark();
                context().parent().tell(new Terminate(), self());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleEvaluate(Evaluate evaluate) {
        if (!runningSimulation) {
            sender().tell(new EvaluateResult(null), self());
        } else {
            double[] y = CocoJNI.cocoEvaluateFunction(benchmarkProblem.getPointer(), evaluate.x);
            sender().tell(new EvaluateResult(y), self());
            evaluations++;
        }
    }

    private void checkStopCondition() {
        if (evaluations == benchmarkProblem.getEvaluations()) {
            simulationActor.tell(new StopSimulation(), self());
            runningSimulation = false;
        }
    }

    private void startBenchmark(){
        nextProblem();
        startSimulation();
    }

    private void startSimulation() {
        simulationActor.tell(new StartSimulation(benchmarkProblem), self()); //TODO start simulation
    }

    private void handleSimulationEnd() {
        nextProblem();
        startSimulation();
    }

    private void stopBenchmark() {
        try {
            coCOBenchmark.finalizeBenchmark();
        } catch (Exception ex) {

        }
    }
}
