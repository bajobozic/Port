# Port Architecture Guide (For AI Agent Reuse)

## 1. Goal of This Document

This document describes the current high-level architecture of the `Port` Kotlin Multiplatform app
and converts it into reusable rules for building a new project with the same structure.

Target stack to preserve:

- Kotlin Multiplatform (KMP)
- Compose Multiplatform UI
- 3 targets: Android, iOS, Desktop (JVM)
- Multi-module architecture with explicit dependency direction

---

## 2. High-Level Architecture

The project uses a modularized, layered architecture:

- `composeApp` is the composition root and app shell.
- Feature modules are split by responsibility:
    - `feature_name_ui`: presentation layer for a feature
    - `feature_name_component`: domain + data orchestration for a feature
- Cross-feature infrastructure modules:
    - `network`: remote data access
    - `storage`: local data access
- Shared modules:
    - `shared_component`: shared core abstractions/utilities (errors, outcomes, config)
    - `shared_ui`: shared routes, theme, and reusable UI primitives

This gives us vertical features + horizontal shared infrastructure.

---

## 3. Current Module Inventory

Root modules from `settings.gradle.kts`:

- `:composeApp`
- `:home_ui`
- `:detail_ui`
- `:map_ui`
- `:signin_ui`
- `:home_component`
- `:network`
- `:storage`
- `:shared_component`
- `:shared_ui`

### Practical role of each module

- `composeApp`: app entrypoint, navigation graph, DI bootstrap (`initKoin`), platform startup hooks.
- `*_ui`: Compose screens, UI state/events/view models, feature DI module for VMs, expect/actual UI
  bridges.
- `*_component`: feature repository + use cases + mediator/orchestration logic.
- `network`: Ktor client, API clients/DTOs, remote data source, repository impl, remote use cases.
- `storage`: Room DB, DAOs/entities, local data source, repository impl, local use cases.
- `shared_component`: `Outcome`, `BaseError`, `ErrorHandler`, generated `SharedConfig`.
- `shared_ui`: `Routes`, theme tokens, shared UI components/extensions.

---

## 4. Naming and Folder Rules (Template for New Project)

Keep these rules for new projects.

### 4.1 Module naming

- Presentation module: `<feature>_ui`
- Domain/data-orchestration module: `<feature>_component`
- Infrastructure modules: `network`, `storage`
- Shared modules: `shared_ui`, `shared_component`
- App shell: `composeApp`

Examples:

- `catalog_ui`, `catalog_component`
- `profile_ui`, `profile_component`

### 4.2 Package layout inside modules

Follow this package pattern in `commonMain`:

- `presentation/` for screens, UI state/events, view models (UI modules)
- `di/` for Koin module declarations
- `domain/` for business interfaces/models/use cases
- `data/` for implementations (repository/data sources/DTO/mappers)

Observed concrete patterns:

- `home_ui/.../presentation`, `home_ui/.../di`
- `home_component/.../domain`, `home_component/.../data`, `home_component/.../di`
- `network/.../domain`, `network/.../data`, `network/.../di`
- `storage/.../domain`, `storage/.../data`, `storage/.../di`

---

## 5. Dependency Direction (Most Important Rule)

### 5.1 Actual current project dependencies

- `composeApp` -> all feature + shared + infra modules
- `home_ui` -> `home_component`, `shared_ui`, `shared_component`, `storage`
- `detail_ui` -> `network`, `shared_ui`, `shared_component`
- `map_ui` -> `shared_ui`
- `signin_ui` -> `shared_ui`
- `home_component` -> `network`, `storage`, `shared_component`
- `network` -> `shared_component`
- `storage` -> `shared_component`
- `shared_ui` -> (no project module dependencies)
- `shared_component` -> (no project module dependencies)

### 5.2 Direction policy to preserve

Only allow dependencies to point inward/downward:

1. `composeApp` can depend on everything.
2. `*_ui` modules may depend on:
    - their own `*_component`
    - `shared_ui`
    - `shared_component`
    - infra modules only when truly needed by that UI.
3. `*_component` modules may depend on:
    - infra modules (`network`, `storage`)
    - `shared_component`
4. infra modules (`network`, `storage`) may depend on `shared_component` only.
5. shared modules should not depend on feature modules.

Do not create reverse dependencies (for example `network` -> feature or `shared_*` -> feature).

---

## 6. Source Set Strategy for 3 Targets

Each module follows KMP source sets:

- `commonMain`: shared logic first
- `androidMain`: Android specifics
- `iosMain`: iOS specifics
- `jvmMain`: Desktop specifics

Platform-specific behavior uses `expect/actual` from `commonMain` to platform source sets.

Examples in this project:

- `network/getClientEngine()` expect/actual (OkHttp/Darwin)
- `storage/getDatabaseBuilder()` per platform
- `signin_ui/rememberCameraManager()` per platform
- `detail_ui/VideoPlayer` and `BackIcon` per platform
- `map_ui/PortMapView` per platform

Rule for new projects:

- Keep business rules in `commonMain`
- Isolate platform APIs behind `expect/actual` or small platform wrappers
- Keep platform files close to their feature/infrastructure module

---

## 7. DI and Composition Root

DI is centralized using Koin.

### 7.1 Current bootstrap

- `composeApp/initKoin.kt` starts Koin and loads modules.
- Platform app entrypoints call `initKoin()`:
    - Android: `PortApplication`
    - iOS: `MainViewController` (with Swift callback to register iOS native factory)
    - Desktop: `main.kt`

### 7.2 DI module ownership

- Each module owns its own `di/*Module.kt`.
- Feature UI modules register view models.
- Feature component/infra modules register repositories/data sources/use cases.

Rule for new projects:

- Keep registrations in the same module as implementation.
- Export only what downstream modules need.

---

## 8. High-Level Data Flows

### 8.1 Home flow (paging + offline cache)

UI -> feature use case -> feature repository -> Pager(RemoteMediator + local PagingSource)

Detailed chain:

1. `HomeScreen` consumes `PagingData` from `HomeViewModel`.
2. `HomeViewModel` calls `GetPagingDataUseCase`.
3. `HomeRepositoryImp` builds `Pager`.
4. `MovieRemoteMediator` coordinates:
    - remote fetch via `GetMoviesUseCase`/`GetGenresUseCase` (`network`)
    - local writes + remote keys via `storage` use cases
5. UI reads paged data from local DB through `PagingSource`.

Result: remote is synchronized into local storage; UI is fed from local paging source.

### 8.2 Detail flow (on-demand remote fetch)

UI -> `DetailViewModel` -> `GetMovieDetailUseCase` + `GetMovieVideoUseCase` -> `network`
repository -> API client

`DetailViewModel` combines both responses and updates `DetailUiState`.

### 8.3 Sign-in flow (platform bridge pattern)

`signin_ui` holds shared screen and state, but camera capture and iOS native button are
platform-specific:

- Camera via `expect/actual rememberCameraManager`
- iOS native button through injected `NativeViewFactory` registered from Swift side

### 8.4 Map flow

`map_ui` screen uses `PortMapView` expect/actual for platform-specific map rendering.

---

## 9. Navigation Pattern (Should Be Reused)

Navigation is part of the architecture and should be treated as a first-class rule in new projects.

### 9.1 Current pattern in this codebase

- Navigation is centralized in `composeApp` (`App.kt`) using Navigation3.
- Route definitions are shared in `shared_ui/Routes.kt` as a sealed `NavKey` hierarchy.
- Feature UI modules expose navigation entry registration functions (for example `homeScreen`,
  `detailScreen`, `signInScreen`, `mapsScreen`) and `composeApp` composes them in one
  `entryProvider`.

### 9.2 Ownership and responsibilities

- `shared_ui`: owns route contracts (`Routes`) only.
- `composeApp`: owns back stack and navigation container (`NavDisplay`, transitions, global scene
  strategy).
- `*_ui`: owns route-specific UI and emits navigation intents/events, but should not own global
  navigation state.

### 9.3 Navigation flow rule

Use this direction:

1. UI emits event/intention (for example: `NavigateToDetails(id)`).
2. Screen entry handler in feature UI maps that event to route action.
3. `composeApp` back stack applies `add/remove` operations.

Keep route transitions at the app shell level, not duplicated inside each feature.

### 9.4 Rules for new projects

- Add new routes in `shared_ui/Routes.kt`.
- Register the route entry in the relevant `*_ui` module.
- Wire route entry from `composeApp/App.kt` `entryProvider`.
- Keep navigation arguments in route objects (strongly typed route models).
- Keep cross-feature navigation explicit through shared routes (no hidden string route constants).

---

## 10. Error and Result Model

Shared result/error handling lives in `shared_component`:

- `Outcome.Success` / `Outcome.Error`
- `BaseError` hierarchy
- `ErrorHandler` contract

`network` implements `NetworkErrorHandler` and maps thrown exceptions into `BaseError`.

Rule for new projects:

- Keep common error and result contracts in shared core module.
- Convert platform/library exceptions at module boundaries.

---

## 11. Reuse Blueprint for New Feature

For a new feature `favorites`, replicate this structure:

- `favorites_ui`
    - `presentation/` (`FavoritesScreen`, `FavoritesViewModel`, state/events)
    - `di/FavoritesModule.kt`
    - `expect/actual` files for platform-only UI/device APIs
- `favorites_component`
    - `domain/repository/FavoritesRepository.kt`
    - `domain/usecase/...`
    - `data/repository/FavoritesRepositoryImpl.kt`
    - `data/...` orchestration
    - `di/FavoritesComponentModule.kt`

Then wire:

1. Add modules to `settings.gradle.kts`
2. Add project dependencies following direction rules
3. Register Koin module in `composeApp/initKoin.kt`
4. Register navigation route in `shared_ui/Routes.kt` and in `composeApp/App.kt`

---

## 12. Guardrails for AI Agent (Do/Do Not)

### Do

- Keep `commonMain` as default implementation location.
- Use `expect/actual` for platform APIs.
- Keep DI per module and compose at app root.
- Keep feature UI and feature component separate.
- Keep shared abstractions in `shared_component`/`shared_ui`.
- Prefer `internal` by default inside modules; make types/functions public only when they are part
  of cross-module API.

### Do not

- Do not make infra modules depend on feature modules.
- Do not put platform code in `commonMain`.
- Do not place shared types in feature modules if used by multiple features.
- Do not bypass module boundaries by importing internal implementation packages across modules.

---

## 13. Visibility Contract (`internal` vs Public)

Visibility is part of architecture and should be explicit in new projects.

### 13.1 What the current codebase already shows

- Many implementation details are `internal` (feature view models/states/events, repositories, data
  sources, mediators, protected DI submodules).
- Cross-module contracts are exposed as public by default (for example shared contracts, routes, use
  case fun interfaces, and route-entry extension functions).

### 13.2 Recommended policy for new projects

Use this default:

1. Mark everything `internal` first.
2. Promote to public only when another module must consume it.

Per layer:

- `*_ui`:
    - Keep screen internals (`UiState`, `Event`, composable internals, VM) `internal`.
    - Expose only entry points needed by app shell (route entry registration functions).
- `*_component`:
    - Keep data/repository implementations `internal`.
    - Expose only use cases/contracts consumed by `*_ui` or composition root.
- `network` / `storage`:
    - Keep client/data source/repository impl `internal`.
    - Expose only domain models/use cases/contracts needed by consumers.
- `shared_component` / `shared_ui`:
    - Public only for intentionally shared APIs (error/result contracts, routes, theme/components
      meant for reuse).
    - Keep helper internals `internal`.

### 13.3 API-surface sanity check for agents

When adding any new declaration:

- Ask: `Will another module import this?`
- If no: use `internal`.
- If yes: keep public and treat it as stable API.

This prevents accidental module coupling and keeps boundaries enforceable over time.

---

## 14. Optional Improvements (While Keeping This Architecture)

These are compatible improvements and can be applied incrementally:

- Add strict API module boundaries (public API surface + internal implementations).
- Move duplicated mapping code into dedicated mapper files/functions.
- Add architecture validation checks (forbidden dependency rules).
- Add static checks (Detekt/custom lint) to flag unintended public declarations.
- Add test strategy per layer:
    - unit tests for use cases/repositories
    - integration tests for data source boundaries
    - UI tests per feature module

---

## 15. One-Page Mental Model

Use this as the default dependency mental model for new projects:

`composeApp` -> (`*_ui`, `*_component`, `network`, `storage`, `shared_*`)

`*_ui` -> (`*_component`, `shared_ui`, `shared_component`, optional infra)

`*_component` -> (`network`, `storage`, `shared_component`)

`network` / `storage` -> `shared_component`

`shared_ui` and `shared_component` -> no feature dependencies
