package actors;

public class RegionActorTests {
/*
    static ActorSystem system;
    private SimulationSettingsMock simulationSettingsMock;
    private RegionSettings regionSettings;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @Before
    public void givenSimulationSettings() {
        simulationSettingsMock = ConfigFactory.create(SimulationSettingsMock.class);
        regionSettings = simulationSettingsMock.region();
    }

    @Test
    public void whenRegionActorIsCreated() throws Exception {
        final Props props = RegionActor.props(regionSettings, "SId");
        final TestActorRef<RegionActor> ref = TestActorRef.create(system, props, "regionActorTest1");
    }

    @Test
    public void whenRegionActorReceiveAAcknowledgeStimuli() throws Exception {
        final Props props = RegionActor.props(regionSettings, "SId");
        final TestActorRef<RegionActor> ref = TestActorRef.create(system, props, "agentActorTest2");

        ref.receive(buildInternalStimulus());
        final RegionActor actor = ref.underlyingActor();
    }

    private InternalStimulus buildInternalStimulus(){
        InternalStimulus internalStimulusMessage = new InternalStimulus();
        internalStimulusMessage.setInformation(Stimulus.StimulusInformation.CHANGE_MY_STATE);
        return internalStimulusMessage;
    }
    */
}
