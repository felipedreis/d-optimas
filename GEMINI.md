# d-optimas (Bimasco) Project Context

## Project Overview
**d-optimas** is a distributed optimization framework based on the Actor model. It allows for the execution of various metaheuristics (such as Genetic Algorithms, Particle Swarm Optimization, Differential Evolution, GRASP, and Iterative Local Search) on a wide range of optimization problems.

### Core Technologies
- **Language:** Java 21 (JDK 21)
- **Concurrency & Distribution:** [Akka](https://akka.io/) (Cluster, Persistence, Remote, Sharding)
- **Database:** [Apache Cassandra](https://cassandra.apache.org/) (used for journaling and state persistence)
- **Serialization & RPC:** [gRPC](https://grpc.io/) and [Protocol Buffers](https://developers.google.com/protocol-buffers)
- **Native Integration:** [COCO](https://github.com/numbbo/coco) (Comparing Continuous Optimizers) via JNI for benchmarking.
- **Analysis:** Python scripts for processing results and generating plots.
- **Infrastructure:** Docker and Ansible for deployment and orchestration.

### Architecture
- **Agents:** Individual actors implementing a metaheuristic.
- **Regions:** Actors representing a search space partition where agents interact.
- **MainActor:** Coordinates the simulation based on a configuration file.
- **Persistence:** All agent states and simulation data are persisted to Cassandra, enabling recovery and offline analysis.

---

## Building and Running

### Prerequisites
- JDK 21
- Maven 3.x
- Cassandra (typically run via Docker)
- Native COCO libraries (located in `native/`)

### Building
To build the project and create a deployable "shaded" JAR:
```bash
./mvnw clean package
```
The resulting JAR will be in `target/d-optimas-2.10-SNAPSHOT-deploy.jar`.

### Running a Simulation
Simulations are driven by HOCON configuration files (found in `experiments/` or `src/main/resources/simulation.conf`).

```bash
java -Djava.library.path=./native/ \
     -jar target/d-optimas-2.10-SNAPSHOT-deploy.jar \
     -host <CASSANDRA_IP> \
     -config <PATH_TO_SIMULATION_CONF>
```

### Data Extraction
To extract simulation results from Cassandra for analysis:
```bash
java -jar target/d-optimas-2.10-SNAPSHOT-deploy.jar \
     -host <CASSANDRA_IP> \
     -config <PATH_TO_SIMULATION_CONF> \
     -extract
```
The plans should be extensive and tracked by the version control. 

When we pass for the implementation, we should have a test-drivevn development
approach. Propose the test classes and the test cases first, then we progress 
to the implementation. 

Tests should be
### Running with Docker
A `docker-compose.yml` is provided in the `docker/` directory to spin up a Cassandra cluster and a d-optimas node.
```bash
cd docker
docker-compose up
```

---
## Workflow

Before starting any implementation, create a plan document in the `plans/` directory. Use the naming convention `P###-<name>.md` (e.g., `P001-fix-ga-bugs.md`). The plan should describe:
- What the feature/fix does
- Tasks to complete
- Dependencies and prerequisites
- Design decisions, architectural considerations, and trade-offs
- Use diagrams (UML, flowcharts) where appropriate
- Update the plan during implementation to reflect completed steps and any changes.
- Avoid significant plan changes without proper consideration.
- The plans should be extensive and tracked by the version control. 

After plan, we enter in development mode. This are the rules: 

- When we pass for the implementation, we should have a test-drivevn development
approach
- Propose the test classes and the test cases first, I have to approve them first,  then we progress 
    to the implementation. 
- After the test is approved and implemented we progress to the implementation of the component
- Tests should be meaningful, and test edge cases and happy paths. 
- We should avoid mocking stuff
- Complex scenarios should be tested in a functional test 

### GitHub Integration (`gh` CLI)
We use the GitHub CLI for managing the project's lifecycle. 

#### Creating Issues
When creating issues, use a detailed body that includes context, technical approach, classes to be touched, and verifiable acceptance criteria.
```bash
gh issue create --title "[Category] Title" --body "### Context
[Describe the problem or feature background]

### Technical Approach
[Describe the high-level strategy and detailed steps]

### Classes to be modified
* [Reference the classes that will be touched]

### Acceptance Criteria
* [List verifiable criteria that can be checked by AI or automated tests]"
```

Common operations:
- **List issues:** `gh issue list`
- **View issue & comments:** `gh issue view <number> --comments`
- **Close issue:** `gh issue close <number>`
- **Comment on issue:** `gh issue comment <number> --body "..."`

### Code Analysis & Discovery (GitNexus)
Always use GitNexus tools to perform structured analysis of the codebase before proposing changes or creating issues.
- **Querying:** Use `mcp_gitnexus_query` to find concepts or execution flows.
- **Context:** Use `mcp_gitnexus_context` for a 360-degree view of a symbol (callers, callees, properties).
- **Impact:** Use `mcp_gitnexus_impact` to analyze the blast radius of a change.
- **Structural Discovery:** Use `mcp_gitnexus_cypher` for complex relationship queries.
- **API Impact:** Use `mcp_gitnexus_api_impact` before modifying any API route handler.

## Development Conventions

- **Commit Message Standards:**
    - Always mention the relevant issue number in the commit message (e.g., `Refactor agent state #42`).
    - Prefer concise, descriptive messages that explain the *why*.
- **Coding Style:** Follows standard Java conventions. Prefers composition and the Actor model for state management.
- **Configuration:** Uses TypeSafe Config (HOCON). Main settings are in `src/main/resources/application.conf` (Akka) and `simulation.conf`.
- **Testing:** Uses JUnit 5. Tests often require the native library path:
  ```bash
  mvn test -Djava.library.path=./native/
  ```
- **Database Schema:** The Cassandra schema is defined in `src/main/resources/cassandra/db.cql`.
- **Contribution Guidelines:** (Referenced in `Readme.md`)
    - Favor **convention over configuration**.
    - The database should only store events; logic belongs in the actors.
    - Standard Java coding conventions.
    - Avoid static operators where possible.

## Directory Structure Highlights
- `src/main/java`: Core logic (actors, heuristics, problems, persistence).
- `src/main/proto`: gRPC service and message definitions.
- `native/`: JNI bindings for the COCO framework.
- `experiments/`: Library of simulation configuration files.
- `analysis/`: Python scripts for post-simulation data processing.
- `ansible/`: Playbooks for automated deployment.
- `docker/`: Docker Compose and Dockerfile for containerization.
