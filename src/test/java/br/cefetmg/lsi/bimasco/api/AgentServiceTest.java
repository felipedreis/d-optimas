package br.cefetmg.lsi.bimasco.api;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.sharding.ClusterSharding;
import akka.testkit.javadsl.TestKit;
import br.cefetmg.lsi.bimasco.actors.MainActor;
import br.cefetmg.lsi.bimasco.actors.Messages;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import br.cefetmg.lsi.bimasco.api.AgentServiceGrpc;
import br.cefetmg.lsi.bimasco.api.DescribeAgentRequest;
import br.cefetmg.lsi.bimasco.api.DescribeAgentResponse;
import br.cefetmg.lsi.bimasco.api.SimulationServiceGrpc;
import br.cefetmg.lsi.bimasco.api.StartSimulationRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentServiceTest {
    private static ActorSystem system;
    private static Server server;
    private static ManagedChannel channel;
    private static AgentServiceGrpc.AgentServiceBlockingStub agentBlockingStub;
    private static SimulationServiceGrpc.SimulationServiceBlockingStub simulationBlockingStub;
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
                "  cluster.sharding.distributed-data.durable.keys = []\n" +
                "}";

        system = ActorSystem.create("TestSystem", ConfigFactory.parseString(akkaConfig).withFallback(ConfigFactory.load()));

        Cluster cluster = Cluster.get(system);
        cluster.join(cluster.selfAddress());

        String agentConfig = TestConfigHelper.buildAgentConfig("TestGRASP", 1, "GRASP",
                false, true, 1000, 0.5,
                Map.of(
                        "maxIterations", 20,
                        "maxIterationsSM", 5,
                        "time", 100.0,
                        "f0", -1.7723,
                        "alpha", 0.001,
                        "stopConditionName", "MaxIterations",
                        "neighborsListName", "Step",
                        "localSearchName", "Random",
                        "localSearchNeighbor", "RealRandom",
                        "candidatesListName", "Function"
                ));

        String configString = TestConfigHelper.buildSimulationConfig("Test", 60, 1, "results",
                TestConfigHelper.buildProblemConfig("Function", "Function", false,
                        "br.cefetmg.lsi.bimasco.core.problems.FunctionProblem", "Function",
                        List.of(
                                List.of("Michalewicz"), List.of(2), List.of(0.1),
                                List.of(0.000001, 0.000001), List.of(0.0, 3.141592), List.of(0.0, 3.141592)
                        )),
                TestConfigHelper.buildRegionConfig(10, 1, 10),
                List.of(agentConfig));

        Config config = ConfigFactory.parseString(configString);
        SimulationSettings settings = new SimulationSettings(config);

        ActorRef mainActor = system.actorOf(Props.create(MainActor.class), "main");
        mainActor.tell(settings, ActorRef.noSender());

        // Wait for SimulationActor to be created by MainActor
        int retries = 0;
        while (simulationActor == null && retries < 10) {
            try {
                simulationActor = system.actorSelection("/user/main/manager").resolveOne(Duration.ofSeconds(1)).toCompletableFuture().join();
            } catch (Exception e) {
                try { Thread.sleep(1000); } catch (InterruptedException ex) {}
                retries++;
            }
        }
        assertNotNull(simulationActor, "SimulationActor should be created by MainActor");

        ActorRef agentShard = ClusterSharding.get(system).startProxy(
                "agents",
                Optional.empty(),
                Messages.agentMessageExtractor);

        server = ServerBuilder.forPort(0)
                .addService(new AgentService(simulationActor, agentShard))
                .addService(new SimulationService(simulationActor))
                .build()
                .start();

        channel = ManagedChannelBuilder.forAddress("localhost", server.getPort())
                .usePlaintext()
                .build();

        agentBlockingStub = AgentServiceGrpc.newBlockingStub(channel);
        simulationBlockingStub = SimulationServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public static void teardown() throws InterruptedException {
        if (channel != null) channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        if (server != null) server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        if (system != null) TestKit.shutdownActorSystem(system);
    }

    @Test
    public void testDescribeAgent() throws InterruptedException {
        // 1. Start simulation to ensure agents are created
        simulationBlockingStub.startSimulation(StartSimulationRequest.newBuilder().build());

        // 2. Describe agent-0 (with retry)
        DescribeAgentRequest request = DescribeAgentRequest.newBuilder()
                .setAgentId("agent-0")
                .build();

        DescribeAgentResponse response = null;
        int retries = 0;
        while (response == null && retries < 20) {
            try {
                DescribeAgentResponse temp = agentBlockingStub.describeAgent(request);
                // Wait until it has a best solution
                if (temp.hasBestSolution()) {
                    response = temp;
                } else {
                    System.out.println("Retry " + retries + ": No best solution yet");
                    Thread.sleep(1000);
                    retries++;
                }
            } catch (Exception e) {
                System.out.println("Retry " + retries + " failed: " + e.getMessage());
                Thread.sleep(1000);
                retries++;
            }
        }

        assertNotNull(response, "Response should not be null after retries");
        assertEquals("agent-0", response.getAgentId());
        assertEquals("GRASP", response.getHeuristic());
        assertEquals(1000, response.getLifetime());
        assertEquals(0.5, response.getMemoryTax(), 0.0001);
        assertTrue(response.getCurrentTime() >= 0);
        assertTrue(response.getStartTime() >= 0);
        assertNotNull(response.getMemoryMap());
        assertTrue(response.hasBestSolution());
        assertNotNull(response.getBestSolution().getId());
    }
}
