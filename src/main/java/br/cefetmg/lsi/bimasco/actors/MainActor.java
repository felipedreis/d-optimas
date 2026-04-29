package br.cefetmg.lsi.bimasco.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.sharding.ClusterSharding;
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
import java.util.Optional;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;

public class MainActor extends AbstractActor {

    private static final Logger logger = LoggerFactory.getLogger(MainActor.class);

    private SimulationSettings settings;

    private ActorRef simulationActor;
    private ActorRef benchmarkActor;

    private Server grpcServer;

    private final int grpcPort;

    public MainActor() {
        this(8080);
    }

    public MainActor(int grpcPort) {
        this.grpcPort = grpcPort;
    }

    @Override
    public void preStart() {
        logger.info("Main actor starting on port {}", grpcPort);
        simulationActor = context().actorOf(Props.create(SimulationActor.class), "manager");
        benchmarkActor = context().actorOf(Props.create(BenchmarkActor.class), "benchmark");

        ActorRef agentShard = ClusterSharding.get(context().system()).startProxy(
                "agents",
                Optional.empty(),
                Messages.agentMessageExtractor);

        ActorRef regionShard = ClusterSharding.get(context().system()).startProxy(
                "regions",
                Optional.empty(),
                Messages.regionMessageExtractor);

        grpcServer = ServerBuilder.forPort(grpcPort)
                .addService(new AgentService(simulationActor, agentShard))
                .addService(new RegionService(simulationActor, regionShard))
                .addService(new BenchmarkService(benchmarkActor))
                .addService(new SimulationService(simulationActor, self()))
                .build();

        try {
            grpcServer.start();
            logger.info("GRPC server started on port {}", grpcPort);
        } catch (IOException ex) {
            logger.error("Couldn't start grpc endpoint", ex);
        }
    }

    @Override
    public void postStop() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SimulationSettings.class, this::setup)
                .match(ConfigureSimulation.class, this::onConfigureSimulation)
                .match(SimulationReady.class, this::onReady)
                .match(SimulationStopped.class, this::onSimulationStopped)
                .build();
    }

    private void onConfigureSimulation(ConfigureSimulation configure) {
        logger.info("Forwarding configuration to manager");
        this.settings = configure.settings;
        simulationActor.forward(configure, context());
        if (settings.isBenchmark()) {
            benchmarkActor.forward(configure, context());
        }
    }

    private void setup(SimulationSettings settings) {
        logger.info("Setting up the simulation manager");
        this.settings = settings;
        simulationActor.tell(new ConfigureSimulation(settings), self());
        if (settings.isBenchmark()) {
            benchmarkActor.tell(new ConfigureSimulation(settings), self());
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
