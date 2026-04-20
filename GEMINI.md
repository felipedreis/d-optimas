# d-optimas (Bimasco) Project Context

## Active Plans
- [P001-fix-ga-bugs.md](plans/P001-fix-ga-bugs.md) - Fix GA Bugs (Completed)

## Project Overview
**d-optimas** is a distributed optimization framework based on the Actor model. It allows for the execution of various metaheuristics (such as Genetic Algorithms, Particle Swarm Optimization, Differential Evolution, GRASP, and Iterative Local Search) on a wide range of optimization problems.

### Core Technologies
- **Language:** Java 11 (JDK 11)
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
- JDK 11
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

Use diagrams (UML, flowcharts) where appropriate. Update the plan during implementation to reflect completed steps and any changes. Avoid significant plan changes without proper consideration.

## Development Conventions

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
