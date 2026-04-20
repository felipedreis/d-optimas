# P001: Fix GA Bugs

## Problem Description
The Genetic Algorithm (GA) implementation has two identified issues:
1. **Redundant Population Copy:** `proxPopulacao` is initialized with a copy of the current population, and then new offspring are added to it. This effectively doubles the population size during the iteration before `nextPopulation` selection is performed.
2. **Inconsistent Variable Usage:** The `getNumPais()` method returns `maxIterations` instead of `numPais`.

## Tasks
- [x] Investigate `ModifiesSolutionCollections` and its implementations for "next population" selection.
- [x] Fix `getNumPais()` in `GA.java`.
- [x] Fix redundant initialization of `proxPopulacao` in `GA.java`.
- [x] Verify the fix with tests.

## Dependencies
- GA.java
- ModifiesSolutionCollections implementations (for context)

## Design Decisions
- Removed the redundant copy of the current population into `proxPopulacao` at the start of each GA iteration. This ensures `proxPopulacao` contains only new offspring, aligning with a generational GA approach and avoiding population growth issues.
- Fixed `getNumPais()` to return the correct field `numPais`.
