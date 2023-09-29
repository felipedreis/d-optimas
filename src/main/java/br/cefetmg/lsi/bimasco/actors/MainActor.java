package br.cefetmg.lsi.bimasco.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import br.cefetmg.lsi.bimasco.api.AgentService;
import br.cefetmg.lsi.bimasco.api.BenchmarkService;
import br.cefetmg.lsi.bimasco.api.RegionService;
import br.cefetmg.lsi.bimasco.api.SimulationService;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;

public class MainActor extends AbstractActor {

    private static final Logger logger = LoggerFactory.getLogger(MainActor.class);

    private SimulationSettings settings;

    private ActorRef simulationActor;
    private ActorRef benchmarkActor;

    private Server grpcServer;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SimulationSettings.class, this::setup)
                .match(SimulationReady.class, this::onReady)
                .match(SimulationStopped.class, this::onSimulationStopped)
                .build();
    }

    private void setup(SimulationSettings settings) {
        logger.info("Setting up the simulation manager");
        this.settings = settings;
        if (settings.isBenchmark()) {
            simulationActor = context().actorOf(Props.create(SimulationActor.class, settings), "manager");
            benchmarkActor = context().actorOf(Props.create(BenchmarkActor.class, simulationActor, settings.getName()), "benchmark");

        } else {
            simulationActor = context().actorOf(Props.create(SimulationActor.class, settings), "manager");
        }

        grpcServer = ServerBuilder.forPort(8080)
                .addService(new AgentService(simulationActor))
                .addService(new RegionService(simulationActor))
                .addService(new BenchmarkService(benchmarkActor))
                .addService(new SimulationService())
                .build();

        try {
            grpcServer.start();
        } catch (IOException ex) {
            logger.error("Couldn't start grpc endpoint", ex);
        }
        logger.info("GRPC started");
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
