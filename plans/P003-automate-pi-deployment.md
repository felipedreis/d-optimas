# P003 - Automate Raspberry Pi Swarm Deployment

This plan addresses the automation of the Raspberry Pi cluster deployment as described in Issue #9.

## Context
Manual management of the 4-node ARM cluster is inefficient. An automated pipeline is needed to build, deploy, and retrieve results.

## Tasks
- [x] Fix `docker/Dockerfile.java21` by removing reference to missing `test_pso_sa.conf`.
- [x] Add python dependencies installation to Ansible playbook.
- [x] Fix Docker Swarm (certificate renewal/reset).
- [x] Run the Ansible playbook to build and deploy the Swarm stack.
- [x] Monitor the simulation and collect results.
- [x] Verify results in the `results-pi/` directory.

## Design Decisions
- Use Ansible for orchestration.
- The manager node (node-0) will be responsible for building the ARM-compatible Docker image and deploying the stack.
- Results will be fetched from the manager node back to the local machine.

## Implementation Details
- Playbook: `ansible/pi-swarm-experiment.yml`
- Inventory: `ansible/inventory/pi-cluster.yml`
- Dockerfile: `docker/Dockerfile.java21`
- Deployment: `docker/docker-stack.yml`

## Validation
- Successful execution of the Ansible playbook.
- Presence of results in the `results-pi/` folder after the run.
