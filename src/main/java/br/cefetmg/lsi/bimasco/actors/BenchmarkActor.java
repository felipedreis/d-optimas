package br.cefetmg.lsi.bimasco.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.cefetmg.lsi.bimasco.coco.*;
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
            benchmarkProblem.setBenchmarkActor(self());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleEvaluate(Evaluate evaluate) {
        double [] y = CocoJNI.cocoEvaluateFunction(benchmarkProblem.getPointer(), evaluate.x);
        sender().tell(new EvaluateResult(y), self());
    }

    private void startBenchmark(){
        nextProblem();
        startSimulation();
    }

    private void startSimulation() {
        simulationActor.tell(null, self()); //TODO start simulation
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
