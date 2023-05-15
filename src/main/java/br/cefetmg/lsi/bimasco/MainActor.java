package br.cefetmg.lsi.bimasco;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import br.cefetmg.lsi.bimasco.actors.BenchmarkActor;
import br.cefetmg.lsi.bimasco.actors.SimulationActor;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;

public class MainActor extends AbstractActor {

    private static final Logger logger = LoggerFactory.getLogger(MainActor.class);

    private SimulationSettings settings;

    private ActorRef simulationActor;
    private ActorRef benchmarkActor;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SimulationSettings.class, this::setup)
                .match(SimulationReady.class, this::onReady)
                .match(SimulationStopped.class, this::onSimulationStopped)
                .build();
    }

    public void setup(SimulationSettings settings) {
        logger.info("Setting up the simulation manager");
        this.settings = settings;
        if (settings.isBenchmark()) {
            simulationActor = context().actorOf(Props.create(SimulationActor.class, settings), "manager");
            benchmarkActor = context().actorOf(Props.create(BenchmarkActor.class, simulationActor, settings.getName()), "benchmark");

        } else {
            simulationActor = context().actorOf(Props.create(SimulationActor.class, settings), "manager");
        }
    }

    private void onReady(SimulationReady ready)  {
        logger.info("Starting simulation");
        if (settings.isBenchmark())
            benchmarkActor.tell(new StartSimulation(), self());
        else
            simulationActor.tell(new StartSimulation(), self());
    }

    private void onSimulationStopped(SimulationStopped stopped){
        logger.info("Stopping all nodes");
        stopped.nodes.forEach(node -> node.tell(new Terminate(), self()));
    }
}
