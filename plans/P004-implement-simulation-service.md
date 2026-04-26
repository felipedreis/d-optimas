# P004 - Implement SimulationService for simulation control

The `SimulationService` is currently a skeleton. This plan outlines the implementation of its methods (`statSimulation`, `startSimulation`, `stopSimulation`) and the necessary changes in `SimulationActor` and `MainActor`.

## Tasks

1.  [x] **Enhance `SimulationState` and `SimulationActor` to track status**:
    *   Add `SimulationStatus` to `SimulationState`.
    *   Update `SimulationActor` to correctly update and report its status.
2.  [x] **Update `MainActor`**:
    *   Pass `simulationActor` reference to `SimulationService` constructor.
3.  [x] **Implement `SimulationService`**:
    *   Implement `statSimulation` by querying `SimulationActor`.
    *   Implement `startSimulation` by sending `StartSimulation` message.
    *   Implement `stopSimulation` by sending `StopSimulation` message.
4.  [x] **Verification**:
    *   Create functional tests to verify the simulation lifecycle via GRPC.
    *   **Bonus**: Updated `AgentActor`, `RegionActor`, and `MessagePersister` to handle missing Cassandra connections, enabling functional testing without a database.

## Design Decisions

*   **Status Mapping**:
    *   `READY`: Agents created, `simulationStarted = false`.
    *   `STARTED`: `simulationStarted = true`.
    *   `STOPPED`: After `StopSimulation` or if simulation reached its time limit.
*   **Communication**: Use Akka's `Patterns.ask` for request-response interactions between the GRPC service and the actors.

## Classes to be modified

*   `br.cefetmg.lsi.bimasco.actors.SimulationState`
*   `br.cefetmg.lsi.bimasco.actors.SimulationActor`
*   `br.cefetmg.lsi.bimasco.api.SimulationService`
*   `br.cefetmg.lsi.bimasco.actors.MainActor`

## Testing Strategy

*   Create `SimulationServiceTest.java`.
*   The test will start the Akka system and the GRPC server.
*   It will use a GRPC client to call `statSimulation`, `startSimulation`, and `stopSimulation`.
*   Assert that the status transitions correctly.
