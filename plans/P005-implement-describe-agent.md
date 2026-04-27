# P005 - Implement describeAgent in AgentService

## Context
The `AgentService.describeAgent` method in `src/main/java/br/cefetmg/lsi/bimasco/api/AgentService.java` is currently unimplemented. Users cannot retrieve detailed metrics for individual agents.

## Goals
- Implement the `describeAgent` gRPC method.
- Update `AgentActor` to provide its internal state.
- Ensure all required metrics (lifetime, startTime, currentTime, completeExecutions, bestSolution, and memory) are returned.

## Technical Approach

### 1. Update Messages in `Messages.java`
- Modify `GetState` to extend `AbstractMessage` so it can be handled by `MessageExtractor`.
- Add `DetailedAgentState` class to carry the state data from the actor to the service.

### 2. Update `AgentActor.java`
- Add `startTime` field and initialize it in `onStartSimulation`.
- Update `onGetState` to return `DetailedAgentState`.
- `DetailedAgentState` should include:
    - `lifetime`: from `agentSettings`
    - `startTime`: the time when simulation started for this agent.
    - `currentTime`: `globalTime`.
    - `completeExecutions`: `agent.getContext().getInvocations().get()`.
    - `bestSolution`: `agent.getContext().getBestSolution()`.
    - `memory`: from `agent.getQLearningMemory().getQTable()`.
    - `memoryTax`: `agent.getAgentSettings().getMemoryTax()`.
    - `requiredSolutions`: `agent.getSolutionsCount()`.
    - `heuristic`: `agent.getAgentSettings().getMetaHeuristicName()`.

### 3. Implement `AgentService.java`
- Implement `describeAgent` method.
- Parse `agentId` (e.g., "agent-0") to get the numeric ID.
- Use `Patterns.ask` to send `GetState` to `agentShard`.
- Convert `DetailedAgentState` to `DescribeAgentResponse`.

## Tasks
- [x] Create `DetailedAgentState` in `Messages.java` and update `GetState`.
- [x] Update `AgentActor` to track `startTime` and handle `GetState` returning `DetailedAgentState`.
- [x] Implement `AgentService.describeAgent`.
- [x] Add tests to verify the implementation.

## Dependencies
- gRPC and Protobuf (already in place).
- Akka actors and sharding (already in place).

## Design Decisions
- `GetState` needs to be an `AbstractMessage` to be easily sent to a specific agent through the shard region without having to know the direct `ActorRef`.
- `DetailedAgentState` is a plain Java object (Serializable) to facilitate communication between the actor and the service.

## Verification Plan

### Test Cases
1. **AgentActor State:**
    - Test that `AgentActor` responds to `GetState` with a `DetailedAgentState` containing correct values.
2. **AgentService describeAgent:**
    - Test that `AgentService.describeAgent` correctly communicates with the actor and returns the expected gRPC response.

### Automated Tests
- Create `src/test/java/br/cefetmg/lsi/bimasco/actors/AgentActorTest.java` (if not exists) or add to it.
- Create `src/test/java/br/cefetmg/lsi/bimasco/api/AgentServiceTest.java` (if not exists) or add to it.
