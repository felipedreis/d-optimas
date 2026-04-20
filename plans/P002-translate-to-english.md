# P002: Translate Codebase to English

## Problem Description
The codebase contains a mix of Portuguese and English in variable names, method names, comments, and log messages. This makes the code less accessible and inconsistent with standard development practices for international projects.

## Tasks
- [ ] Identify and list all Portuguese terms used in the codebase.
- [ ] Create a translation mapping (Portuguese -> English).
- [ ] Refactor `GA.java` to use English names.
- [ ] Refactor core classes (e.g., `BasicFunctions.java`, `Memory.java`) to use English names.
- [ ] Update log messages and comments to English.
- [ ] Verify the changes with existing tests.

## Dependencies
- All Java source files in `src/main/java`.
- Tests in `src/test/java`.

## Translation Mapping (Draft)
- `solucao` -> `solution`
- `populacao` -> `population`
- `proxPopulacao` -> `nextPopulation`
- `pais` -> `parents`
- `filhos` -> `offspring` or `children`
- `iteracao` -> `iteration`
- `mutacao` -> `mutation`
- `escolha` -> `choice`
- `posicao` -> `position`
- `vetor` -> `vector` or `array`
- `valor` -> `value`
- `melhor` -> `best`
- `nova` -> `new`
- `gerada` -> `generated`
- `tipo` -> `type`
- `dados` -> `data`

## Design Decisions
- Use standard Java camelCase for renamed variables and methods.
- Ensure that string literals in configuration files or database keys are handled carefully (if they are part of a protocol or external interface).
- Perform refactoring in chunks to avoid massive merge conflicts and ensure validation.
