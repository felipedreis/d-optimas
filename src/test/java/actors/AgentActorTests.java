package actors;

public class AgentActorTests {
    /*
    static ActorSystem system;
    private SimulationSettingsMock simulationSettingsMock;
    private AgentSettings agentSettings;
    private TestActorRef<AgentActor> ref;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @Before
    public void givenSimulationSettings() {
        simulationSettingsMock = ConfigFactory.create(SimulationSettingsMock.class);
        agentSettings = simulationSettingsMock.agents()[1];
    }

    @Test
    public void whenAgentActorIsCreated() throws Exception {

        final Props props0 = AgentActor.props(simulationSettingsMock, simulationSettingsMock.agents()[0], "SId");
        ref = TestActorRef.create(system, props0, "agentActorTestA");

        final Props props1 = AgentActor.props(simulationSettingsMock, simulationSettingsMock.agents()[1], "SId");
        ref = TestActorRef.create(system, props1, "agentActorTestB");

        final Props props2 = AgentActor.props(simulationSettingsMock, simulationSettingsMock.agents()[2], "SId");
        ref = TestActorRef.create(system, props2, "agentActorTestC");

        final Props props3 = AgentActor.props(simulationSettingsMock, simulationSettingsMock.agents()[3], "SId");
        ref = TestActorRef.create(system, props3, "agentActorTestD");

        final Props props4 = AgentActor.props(simulationSettingsMock, simulationSettingsMock.agents()[4], "SId");
        ref = TestActorRef.create(system, props4, "agentActorTestE");

        //For state tests
        //final TestActorRef<AgentActor> ref = TestActorRef.create(system, props, "agentActorTest");
        //ref.receive("say42");
        //final AgentActor actor = ref.underlyingActor();

        //For caller tests
        //final Future<Object> future = akka.pattern.Patterns.ask(ref, "say42", 3000);
        //assertTrue(future.isCompleted());
        //assertEquals(42, Await.result(future, Duration.Zero()));
    }

    @Test
    public void whenAgentActorReceiveABootstrapStimuli() throws Exception {
        final Props props = AgentActor.props(simulationSettingsMock, agentSettings, "SId");
        final TestActorRef<AgentActor> ref = TestActorRef.create(system, props, "agentActorTest2");

        final Future<Object> future = akka.pattern.Patterns.ask(ref,
                buildExternalStimulus(null, Stimulus.StimulusInformation.BOOTSTRAP),
                4000);

        final AgentActor actor = ref.underlyingActor();
//        assertEquals(1, actor.getAgent().getMemory().getTimeLine().getEmittedStimuli().size());
    }

    @Test
    public void whenAgentActorReceiveABootstrapStimuli1() throws Exception {

        new JavaTestKit(system) {
            {
                final JavaTestKit probe = new JavaTestKit(system);

                final Props props = AgentActor.props(simulationSettingsMock, agentSettings, "SId");

                final ActorRef regionsRouter = system.actorOf(
                        RegionsManagerActor.props(
                                new RegionSettings[]{simulationSettingsMock.region()}, "SId"),
                        "regions");

                final ActorRef agentsRouter = system.actorOf(
                        AgentsManagerActor.props(
                                simulationSettingsMock, "SId"),
                        "agents");


                //ref.tell(buildExternalStimulus(null, Stimulus.StimulusInformation.BOOTSTRAP), ActorRef.noSender());

                //probe.expectMsgClass(duration("1 seconds"), ExternalStimulus.class);
            }};
    }


    @Test
    public void whenAgentActorReceiveAAcknowledgeStimuli() throws Exception {
        final Props props = AgentActor.props(simulationSettingsMock, agentSettings, "SId");
        final TestActorRef<AgentActor> ref = TestActorRef.create(system, props, "agentActorTest4");

        ref.receive(buildInternalStimulus(Stimulus.StimulusInformation.CHANGE_MY_STATE));
        final AgentActor actor = ref.underlyingActor();
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    private InternalStimulus buildInternalStimulus(Stimulus.StimulusInformation stimulusInformation){
        InternalStimulus internalStimulusMessage = new InternalStimulus();
        internalStimulusMessage.setInformation(stimulusInformation);
        return internalStimulusMessage;
    }

    private ExternalStimulus buildExternalStimulus(Payload payload, Stimulus.StimulusInformation stimulusInformation){
        ExternalStimulus externalStimulus = new ExternalStimulus(payload);
        externalStimulus.setInformation(stimulusInformation);
        return externalStimulus;
    }
    */
}
