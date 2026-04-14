# AI Instruction Guide: Compose Multiplatform MVI Architecture

This document serves as a strict blueprint for AI agents and developers building or extending
Compose Multiplatform applications using the specific architectural patterns, module organization,
and coding standards of this project.

When generating code, **you must adhere to these structural and architectural rules.**

## 1. Module Organization & Naming

The project follows a highly modularized structure based on features and cross-cutting concerns to
maximize build caching, maintainability, and clear dependency graphs.

### Feature Modules

Each feature must be split into two distinct modules to separate presentation from business logic:

- `{feature}_ui`: Contains ONLY UI-related code (Compose screens, ViewModels, UI State, Events).
    - *Depends on*: `{feature}_component`, `shared_ui`.
- `{feature}_component`: Contains business logic (Domain models, UseCases, Repository Interfaces,
  Data Sources).
    - *Depends on*: `network`, `storage`, `shared_component`.

### Core/Shared Modules

- `network`: Centralized Ktor HTTP client, remote data sources, DTOs, and API definitions.
- `storage`: Local persistence logic, Room KMP database setup, DAOs, and Entities.
- `shared_ui`: Common Compose components, Theme (Colors, Typography, Shapes), Compose Resources, and
  Navigation Routes.
- `shared_component`: Shared business logic, core wrappers (`Outcome`, `BaseError`), and utility
  mappers.
- `composeApp`: The main entry point. Wires up the Navigation graph and initializes Dependency
  Injection.

## 2. Visibilities & Encapsulation (Strict Rules)

To prevent module leakage and enforce the architecture, strict visibility modifiers must be used. *
*The UI layer must ONLY interact with the component layer through UseCases.**

- **`internal` (Default for most implementation details):**
    - ViewModels, UI States, and Events (`_ui` modules).
    - **Repository Interfaces** AND their Implementations (`_component`, `network`, `storage`
      modules).
    - Mappers, Data Sources, DTOs, and Database Entities.
    - Internal Koin setup modules.
- **`public` (Only expose the strict API surface):**
    - High-level DI modules (e.g., `val homeModule = module { ... }`).
    - **UseCases** and Domain Models (`_component` modules).
    - Screen entry-point extension functions for Navigation (e.g.,
      `fun NavGraphBuilder.homeScreen(...)`).

## 3. Architecture: MVI (Model-View-Intent)

Every UI module must strictly implement the MVI pattern:

- **UiState**: A `data class` representing the entire state of the screen. State should be
  immutable.
- **Event**: A `sealed interface` (or class) representing user intentions (e.g., `OnBackClicked`,
  `LoadData`).
- **ViewModel**:
    - Encapsulates state management. Exposes state as a `StateFlow` (or Multiplatform Paging
      `Flow`).
    - Exposes a single `onEvent(event: Event)` function to process user intents.
    - Interacts exclusively with injected **UseCases**. Never inject repositories directly into a
      ViewModel.
- **Compose Screens (Statelessness)**:
    - The primary `@Composable` screen must be stateless.
    - Pass the `UiState` and a lambda for event dispatching `(Event) -> Unit` down to the UI
      components.
    - A tiny wrapper Composable collects the state from the ViewModel and passes it to the stateless
      screen.

## 4. Data Layer & Error Handling

- **Domain Models as Truth:** Repositories must map DTOs (from `network`) and Entities (from
  `storage`) into pure Domain Models before returning them to UseCases. UI modules should never see
  a DTO or Entity.
- **Outcome Wrapper:** Use the centralized `Outcome<T, E>` (or standard Kotlin `Result`) pattern
  from `shared_component` for all repository operations.
- **Error Handling:** Catch network and database exceptions in their respective layers and map them
  to domain-specific `BaseError` sealed classes using unified error handlers.
- **Paging:** For paginated data, use the Multiplatform Paging library (`app.cash.paging`). If
  offline support is needed, implement `RemoteMediator` in the component module to coordinate Ktor
  fetching and Room caching.

## 5. Platform-Specific Code (`expect` / `actual`)

Minimize platform-specific code. When integrating native SDKs or complex native UI:

- Create `expect` functions, interfaces, or composables in `commonMain`.
- Provide `actual` implementations in `androidMain`, `iosMain`, and `jvmMain` (Desktop).

## 6. Dependency Injection (Koin)

- Every module must contain a `di` package.
- Define dependencies using Koin's DSL (`module { ... }`).
- Prefer constructor injection utilities like `factoryOf(::MyUseCase)`,
  `singleOf(::MyRepositoryImpl)`, and `viewModelOf(::MyViewModel)` to reduce boilerplate.
- Each feature module exports a single aggregated Koin module.
- `composeApp` aggregates all feature, network, storage, and shared modules in its `initKoin`
  function.

## 7. Resources & Theming

- **Resources**: Use the official JetBrains `Compose Resources` library. Reference them via
  type-safe generated accessors.
- **Theming**: Centralize all styling in `shared_ui/presentation/theme`. Define a strict
  `MaterialTheme` encompassing custom `Color`, `Shape`, and `Typography`. Do not hardcode colors in
  feature modules.

## 8. Navigation

- Use type-safe navigation (Serialization).
- Define all `@Serializable` routes/destinations inside `shared_ui`.
- Each feature module must provide an extension function on `NavGraphBuilder` to encapsulate its own
  screens, isolating navigation logic from the core app module.

## 9. Example Module Structure Blueprint

```text
feature_name_ui/
  src/commonMain/kotlin/com.package.feature_name_ui/
    di/
      FeatureUiModule.kt         # Public Koin module (exports ViewModels)
    presentation/
      FeatureScreen.kt           # Screen entry point & stateless content (public wrapper, internal screen)
      FeatureViewModel.kt        # Internal ViewModel
      FeatureEvent.kt            # Internal sealed interface
      FeatureUiState.kt          # Internal data class

feature_name_component/
  src/commonMain/kotlin/com.package.feature_name_component/
    di/
      FeatureComponentModule.kt  # Public Koin module (exports UseCases)
    data/
      repository/
        FeatureRepositoryImpl.kt # Internal implementation
      mapper/                    # Internal mappers
    domain/
      model/                     # Public domain models
      repository/
        FeatureRepository.kt     # Internal interface
      usecase/
        GetFeatureDataUseCase.kt # Public UseCase