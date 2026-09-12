# CodeAssist AI — Architecture Note

## Implemented foundation

The advanced foundation is integrated as an isolated `com.tyron.code.ai` package so the existing editor and build pipeline remain stable. `AIEngineManager` supports Gemini and OpenAI-compatible requests with provider failover, strict JSON response contracts, timeouts, bounded context, general chat, and structured change proposals. `SecureKeyStore` protects user-entered provider keys with an Android Keystore AES-GCM key; no provider key is committed to source code.

`ProjectGenerator` writes generated projects only below the application workspace and rejects absolute paths, backslashes, and canonical paths outside the workspace. `CheckpointManager` provides local snapshot and restore primitives. `ChangeApplier` creates a checkpoint before a chat proposal is applied and validates every path before writing. `AICenterFragment` exposes prompt-to-project generation, provider setup, a mobile chat panel, proposal review, apply, rollback, and Virtual Preview actions.

## Conversation and change workflow

The mobile AI Center contains a scrollable chat history, multiline input, asynchronous send action, and conversation context. Chat responses are advisory and do not write files. When a project exists, the user can request a structured proposal from the latest message. The proposal is shown with a summary, explanation, action type, and affected file paths. Only after explicit user approval does `ChangeApplier` create a checkpoint and write validated files. The user can then restore the last checkpoint from the same screen.

## Virtual Preview

`PreviewFragment` opens as an in-app phone-shaped screen from the AI Center. `ProjectTypeDetector` identifies Compose Kotlin sources, Android XML layouts, and Java sources. `PreviewRenderer` parses a bounded, safe subset of Android XML layouts and renders common `LinearLayout`, `ScrollView`, `TextView`, `Button`, `EditText`, and `ImageView` elements without executing generated project code. Compose and Java are currently represented by a readable source card with an explicit status, while XML receives the interactive structural renderer. The user can refresh the preview manually after a chat change. This is a preview foundation, not yet a full Android emulator or true Compose hot-reload runtime.

## Deliberate scope boundary

The current increment does not claim a full in-app emulator, true Compose hot reload, AST-aware patch merging, or automatic rebuild after a chat patch. These are separate integrations requiring the existing compiler service and a tested preview runtime. The current primitives are designed to receive those integrations without exposing the editor to arbitrary AI file writes.

## Security decisions

The API key is entered at runtime and encrypted locally. Generated responses and chat proposals are constrained to relative paths and full-file actions. The app does not include the API keys found in the supplied draft text; those values were treated as compromised and were not copied into the project.

## Build note

The source was checked structurally, but this sandbox does not have an Android SDK configured. Gradle therefore stops before compilation with `SDK location not found`. On a machine with Android SDK 34, run `./gradlew :app:assembleDebug` after setting `ANDROID_HOME` or `local.properties`.
