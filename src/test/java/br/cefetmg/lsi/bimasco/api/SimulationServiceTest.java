package br.cefetmg.lsi.bimasco.api;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.testkit.javadsl.TestKit;
import br.cefetmg.lsi.bimasco.actors.SimulationActor;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import br.cefetmg.lsi.bimasco.api.SimulationServiceGrpc;
import br.cefetmg.lsi.bimasco.api.StatSimulationRequest;
import br.cefetmg.lsi.bimasco.api.StatSimulationResponse;
import br.cefetmg.lsi.bimasco.api.SimulationStatus;
import br.cefetmg.lsi.bimasco.api.StartSimulationRequest;
import br.cefetmg.lsi.bimasco.api.StopSimulationRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationServiceTest {
    private static ActorSystem system;
    private static Server server;
    private static ManagedChannel channel;
    private static SimulationServiceGrpc.SimulationServiceBlockingStub blockingStub;
    private static ActorRef simulationActor;

    @BeforeAll
    public static void setup() throws IOException {
        String akkaConfig = "akka {\n" +
                "  actor.provider = \"cluster\"\n" +
                "  remote.artery {\n" +
                "    canonical.hostname = \"127.0.0.1\"\n" +
                "    canonical.port = 0\n" +
                "  }\n" +
                "  persistence {\n" +
                "    journal.plugin = \"akka.persistence.journal.inmem\"\n" +
                "    snapshot-store.plugin = \"akka.persistence.snapshot-store.local\"\n" +
                "    snapshot-store.local.dir = \"target/snapshots\"\n" +
                "  }\n" +
                "  cluster.sharding.state-store-mode = \"ddata\"\n" +
                "}";
        
        system = ActorSystem.create("TestSystem", ConfigFactory.parseString(akkaConfig).withFallback(ConfigFactory.load()));

        Cluster cluster = Cluster.get(system);
        cluster.join(cluster.selfAddress());

        String configString = TestConfigHelper.buildDefaultConfig();
        Config config = ConfigFactory.parseString(configString);
        SimulationSettings settings = new SimulationSettings(config);

        simulationActor = system.actorOf(Props.create(SimulationActor.class, settings), "simulationActor");

        server = ServerBuilder.forPort(0)
                .addService(new SimulationService(simulationActor))
                .build()
                .start();

        channel = ManagedChannelBuilder.forAddress("localhost", server.getPort())
                .usePlaintext()
                .build();

        blockingStub = SimulationServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public static void teardown() throws InterruptedException {
        if (channel != null) channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        if (server != null) server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        if (system != null) TestKit.shutdownActorSystem(system);
    }

    @Test
    public void testSimulationLifecycle() throws InterruptedException {
        // Wait for Cluster to form and SimulationActor to become leader
        boolean isLeader = false;
        int retries = 0;
        while (!isLeader && retries < 10) {
            Thread.sleep(1000);
            StatSimulationResponse statResponse = blockingStub.statSimulation(StatSimulationRequest.newBuilder().build());
            if (statResponse.getStatus() != SimulationStatus.UNRECOGNIZED) {
                isLeader = true;
            }
            retries++;
        }

        // 1. Check initial status
        StatSimulationResponse statResponse = blockingStub.statSimulation(StatSimulationRequest.newBuilder().build());
        assertEquals(SimulationStatus.STOPPED, statResponse.getStatus());
        
        // 2. Start simulation
        statResponse = blockingStub.startSimulation(StartSimulationRequest.newBuilder().build());
        assertEquals(SimulationStatus.STARTED, statResponse.getStatus());

        // 3. Check status
        statResponse = blockingStub.statSimulation(StatSimulationRequest.newBuilder().build());
        assertEquals(SimulationStatus.STARTED, statResponse.getStatus());

        // 4. Stop simulation
        statResponse = blockingStub.stopSimulation(StopSimulationRequest.newBuilder().build());
        assertEquals(SimulationStatus.STOPPED, statResponse.getStatus());

        // 5. Check status
        statResponse = blockingStub.statSimulation(StatSimulationRequest.newBuilder().build());
        assertEquals(SimulationStatus.STOPPED, statResponse.getStatus());
    }
}
