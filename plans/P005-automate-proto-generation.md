# P005 - Automate Protobuf Generation and CI Pipeline

## Context
Currently, Protobuf contracts are manually copied from `d-optimas` to the Go client (`doptctl`). This is error-prone and hard to maintain. We need an automated way to generate Go stubs and publish them so the Go client can consume them as a standard module.

## Tasks
1. [x] Update `doptimas.proto` with correct `go_package` option.
2. [x] Create a GitHub Action workflow in `d-optimas` for CI (Build & Test).
3. [x] Create a GitHub Action workflow in `d-optimas` to generate and push Go stubs to a dedicated repository.
4. [x] Provide instructions for setting up the Go stub repository.
5. [ ] Provide instructions for consuming the Go stubs in `doptctl`.

## Dependencies
- GitHub repository `doptimas-proto-go` (needs to be created by the user).
- GitHub Personal Access Token (PAT) with `repo` scope for the CI to push to the stub repo.

## Design Decisions
- **Repository Strategy:** We will use a dedicated repository for Go stubs (`doptimas-proto-go`). This keeps the `d-optimas` repo clean of generated Go code and allows `doptctl` to use standard Go module versioning.
- **Generation Tool:** We will use `protoc` with `protoc-gen-go` and `protoc-gen-go-grpc`.
- **CI/CD:** GitHub Actions will be used for both the Java build pipeline and the Protobuf automation.

## Implementation Details

### 1. Protobuf Update
Update `option go_package` in `src/main/proto/doptimas.proto` to `github.com/felipedreis/doptimas-proto-go/api`.

### 2. Java CI Workflow
Create `.github/workflows/ci.yml` to run `./mvnw clean verify` on every push and PR.

### 3. Protobuf Publish Workflow
Create `.github/workflows/publish-proto.yml` triggered on changes to `src/main/proto/`.
Steps:
1. Checkout `d-optimas`.
2. Install Go and Protoc.
3. Generate Go code.
4. Checkout `doptimas-proto-go` in a subfolder.
5. Copy generated code to `doptimas-proto-go`.
6. Commit and push to `doptimas-proto-go`.

## Acceptance Criteria
- [ ] `d-optimas` has a working CI pipeline for Java.
- [ ] Pushing changes to `doptimas.proto` triggers the generation of Go stubs.
- [ ] Go stubs are pushed to `doptimas-proto-go`.
- [ ] `doptctl` can import the stubs via `go get`.
