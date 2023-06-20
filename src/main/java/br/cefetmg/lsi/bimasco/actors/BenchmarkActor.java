package br.cefetmg.lsi.bimasco.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.cefetmg.lsi.bimasco.coco.*;
import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.data.DataExtractionBatch;
import br.cefetmg.lsi.bimasco.data.ExtractorsConfig;
import br.cefetmg.lsi.bimasco.persistence.DOptimasMapper;
import br.cefetmg.lsi.bimasco.persistence.DatabaseHelper;
import coco.CocoJNI;
import org.agrona.collections.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

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

    // 3x10^6 is the number used in C-DEEPSO evaluation on large benchmark problems
    private long evaluationsBudget = 100_000;//3_000_000;

    private boolean runningSimulation;

    private ExtractorsConfig extractorsConfig;

    public BenchmarkActor(ActorRef simulationActor, String name){
        this.simulationActor = simulationActor;
        this.name = name;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        init();
    }
    private void init() {
        try {
            suite = new Suite(name, name, "");
            observer = new Observer(name, "");

            coCOBenchmark = new CoCOBenchmark(suite, observer);

            DOptimasMapper mapper = DatabaseHelper.getMapper();
            extractorsConfig = new ExtractorsConfig(mapper.agentStateDAO(), mapper.regionStateDAO(), mapper.solutionStateDAO(),
                    mapper.globalStateDAO(), mapper.messageStateDAO(), mapper.memoryStateDAO());

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
            logger.error("Couldn't get next problem", ex);
            stopBenchmark();
            context().parent().tell(new Terminate(), self());
        }
    }

    private void handleEvaluate(Evaluate evaluate) {
        logger.debug("Handling evaluate of " + evaluate);
        if (!runningSimulation) {
            logger.warn("Can't evaluate function when simulation is not running");
            sender().tell(new EvaluateResult(), self());
        } else {
            try {
                double[] y = CocoJNI.cocoEvaluateFunction(benchmarkProblem.getPointer(), evaluate.x);
                logger.debug("Evaluation result: f({}) = {}", evaluate, y);
                sender().tell(new EvaluateResult(y), self());
                evaluations++;
                checkStopCondition();
            } catch (Exception ex) {
                logger.error("Couldn't evaluate the request " + evaluate, ex);
            }
        }
    }

    private void checkStopCondition() {
        if (evaluations == evaluationsBudget) {
            logger.info("Stopping simulation due to evaluation limit reached");
            simulationActor.tell(new StopSimulation(), self());
            runningSimulation = false;
        }
    }

    private void startBenchmark(StartSimulation startSimulation){
        logger.info("Simulation ready to start");
        nextProblem();
        if (benchmarkProblem != null)
            startSimulation();
    }

    private void startSimulation() {
        if (!runningSimulation) {
            runningSimulation = true;
            evaluations = 0;
            StartSimulation startSimulation = new StartSimulation(benchmarkProblem);
            simulationActor.tell(startSimulation, self());
            logger.debug("Sending message to simulationActor {}", startSimulation);
        } else {
            logger.warn("Simulation is already running");
        }
    }

    private void handleSimulationEnd(SimulationStopped stopped) {
        logger.info("Simulation has stopped, handling the start of next problem");
        extractData();
        nextProblem();
        startSimulation();
    }

    private void extractData() {
        DataExtractionBatch extractionBatch = new DataExtractionBatch( "data/bbob/" + benchmarkProblem.getId(),
                extractorsConfig.extractors());

        try {
            extractionBatch.prepareDataPath();
            extractionBatch.run();
        } catch (IOException ex) {
            logger.error("Couldn't create extraction path for problem " + benchmarkProblem, ex);
        }
    }

    private void stopBenchmark() {
        try {
            coCOBenchmark.finalizeBenchmark();
        } catch (Exception ex) {
            logger.error("Couldn't finalize the benchmark correctly", ex);
        }
    }

}
