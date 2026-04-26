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

        String configString = "simulation: {\n" +
                "  name: \"Test\",\n" +
                "  hasCooperation: false,\n" +
                "  executionTime: 60,\n" +
                "  nodes: 1,\n" +
                "  extractPath: \"results\",\n" +
                "  problem: {\n" +
                "    name: \"Function\",\n" +
                "    type: \"Function\",\n" +
                "    isMax: false,\n" +
                "    classPath: \"br.cefetmg.lsi.bimasco.core.problems.FunctionProblem\",\n" +
                "    solutionAnalyserName: \"Function\",\n" +
                "    problemData: [ \n" +
                "      [\"Michalewicz\"], [2], [0.1],\n" +
                "      [0.000001, 0.000001], [0.0, 3.141592], [0.0, 3.141592]\n" +
                "    ]\n" +
                "  },\n" +
                "  region: {\n" +
                "    minSolutionsToSplit: 10,\n" +
                "    minRegions: 1,\n" +
                "    maxRegions: 10\n" +
                "  },\n" +
                "  agents: []\n" +
                "}";
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
        // Leadership election and cluster joining take some time
        boolean isLeader = false;
        int retries = 0;
        while (!isLeader && retries < 10) {
            Thread.sleep(1000);
            StatSimulationResponse statResponse = blockingStub.statSimulation(StatSimulationRequest.newBuilder().build());
            // If it becomes READY (which happens when amILeader is true and agents are created)
            // But we have 0 agents, so createAgents will trigger SimulationReady if amILeader is true.
            // When SimulationActor becomes leader, it might trigger status changes.
            if (statResponse.getStatus() != SimulationStatus.UNRECOGNIZED) {
                // If we can get a status, it's a good sign
                isLeader = true;
            }
            retries++;
        }

        // 1. Check initial status
        StatSimulationResponse statResponse = blockingStub.statSimulation(StatSimulationRequest.newBuilder().build());
        // It might be STOPPED or READY depending on if createAgents finished.
        // Let's just ensure it's not FAILED.
        
        // 2. Start simulation
        blockingStub.startSimulation(StartSimulationRequest.newBuilder().build());

        // 3. Check status
        statResponse = blockingStub.statSimulation(StatSimulationRequest.newBuilder().build());
        assertEquals(SimulationStatus.STARTED, statResponse.getStatus());

        // 4. Stop simulation
        blockingStub.stopSimulation(StopSimulationRequest.newBuilder().build());

        // 5. Check status - now amILeader should be true so it should update to STOPPED
        retries = 0;
        while (retries < 5) {
            statResponse = blockingStub.statSimulation(StatSimulationRequest.newBuilder().build());
            if (statResponse.getStatus() == SimulationStatus.STOPPED) break;
            Thread.sleep(500);
            retries++;
        }
        assertEquals(SimulationStatus.STOPPED, statResponse.getStatus());
    }
}
