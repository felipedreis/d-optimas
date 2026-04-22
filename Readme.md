# d-optimas (Bimasco)

**d-optimas** is a distributed optimization framework based on the **Actor Model**. It enables the execution of various metaheuristics (such as Genetic Algorithms, Particle Swarm Optimization, Differential Evolution, GRASP, and Iterative Local Search) on complex optimization problems by partitioning the search space and enabling autonomous agents to cooperate or compete.

## Core Technologies
- **Language:** Java 21 (JDK 21)
- **Concurrency & Distribution:** [Akka](https://akka.io/) (Cluster, Persistence, Remote, Sharding)
- **Database:** [Apache Cassandra](https://cassandra.apache.org/) (used for journaling, state persistence, and extraction)
- **Serialization & RPC:** [gRPC](https://grpc.io/) and [Protocol Buffers](https://developers.google.com/protocol-buffers)
- **Native Integration:** [COCO](https://github.com/numbbo/coco) (Comparing Continuous Optimizers) via JNI for benchmarking.
- **Analysis:** Python scripts for processing results and generating plots.
- **Infrastructure:** Docker and Ansible for deployment and orchestration.

## Architecture
The system follows a hierarchical actor structure where a simulation is decomposed into regions, and regions contain multiple agents:

1.  **MainActor:** Entry point that coordinates the simulation based on HOCON configuration.
2.  **SimulationActor:** Global coordinator for a specific simulation instance.
3.  **RegionActor:** Represents a search space partition where agents interact and share solutions.
4.  **AgentActor:** Individual actors implementing a specific metaheuristic (GA, PSO, DE, etc.).
5.  **Persistence:** All agent states and simulation data are persisted to Cassandra using Akka Persistence, enabling recovery and offline analysis.

## Getting Started

### Prerequisites
- **JDK 21**
- **Maven 3.x**
- **Cassandra 3.11** (typically run via Docker)
- **Native COCO libraries** (located in `native/`)

### Building
To build the project and create a deployable "shaded" JAR:
```bash
./mvnw clean package
```
The resulting JAR will be in `target/d-optimas-2.10-SNAPSHOT-deploy.jar`.

### Running a Simulation
Simulations are driven by HOCON configuration files (found in `experiments/` or `simulation.conf`).

**Important:** Running on Java 17+ requires specific JVM flags for Akka and reflection:
```bash
java --add-opens java.base/java.nio=ALL-UNNAMED \
     --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
     --add-exports java.base/jdk.internal.misc=ALL-UNNAMED \
     -Djava.library.path=./native/ \
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

## Running with Docker
A `docker-compose.yml` is provided to spin up a Cassandra cluster and a d-optimas node.
```bash
cd docker
docker-compose up
```

## Project Structure
- `src/main/java`: Core logic (actors, heuristics, problems, persistence).
- `src/main/proto`: gRPC service and message definitions.
- `native/`: JNI bindings for the COCO framework.
- `experiments/`: Library of simulation configuration files.
- `analysis/`: Python scripts for post-simulation data processing.
- `ansible/`: Playbooks for automated deployment.
- `docker/`: Docker Compose and Dockerfile for containerization.

## Contributing
Please see our [contributing guidelines](CONTRIBUTING.md) before starting work. We follow standard Java coding conventions and favor convention over configuration.
