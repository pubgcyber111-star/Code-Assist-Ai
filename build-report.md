# CodeAssist AI — Build Report

## Latest provider build

The AI Center now includes a unified Arabic configuration card for fourteen provider presets: OpenAI, Google Gemini, Anthropic Claude, OpenRouter, Groq, Together AI, DeepSeek, Mistral AI, Cohere, xAI Grok, Perplexity, Cerebras, Azure OpenAI, and local Ollama.

The user selects a provider, reviews or edits its default endpoint and model, enters an API key, and saves the configuration. The selected provider is used by project generation, chat, structured change proposals, and build repair. Provider keys are protected with Android Keystore-backed AES-GCM storage. No provider key is embedded in the source or APK.

## Build result

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export ANDROID_HOME=/home/ubuntu/android-sdk
./gradlew :app:assembleDebug --no-daemon --stacktrace
```

Result: `BUILD SUCCESSFUL in 33s`.

| Property | Value |
|---|---|
| Package | `org.codeassist.unofficial.ai` |
| Version name | `1.0.2` |
| Compile SDK | 34 |
| Target SDK | 33 |
| Minimum SDK | 26 |
| APK size | 142 MB |
| SHA-256 | `4c5ec536f269bd47b1752b86a96cd0b35e50f5e3585f8d1ca16599bb768a1f4c` |
| Signature | Debug signing, APK Signature Scheme v2 verified |

The build emitted existing dependency/parser warnings but no compilation or packaging failure. Static checks passed with `git diff --check`, provider-reference checks, and scans for common API-key/private-key patterns.

## Scope and limitations

OpenAI-compatible providers share the standard `/chat/completions` request shape. Gemini and Claude use their native request and response contracts. Azure OpenAI and Ollama presets remain editable because their URLs depend on the user's resource or local network. Provider quotas, billing, endpoint reachability, and terms remain the user's responsibility. The delivered artifact is a Debug APK; a Release build needs a separately managed signing key.

## Project AI editor integration

The editor screen now contains an Arabic `Project AI` bar directly below the open-file tabs. It provides a chat toggle, an in-editor message panel, multiline request input, send action, and a preview action. The chat request includes the current project root, the active file path, and a bounded excerpt of the open file source so the selected provider can answer in context. The integration is lifecycle-safe: network work runs on a single background executor and the executor is stopped when the editor Fragment is destroyed.

The updated Debug APK was rebuilt successfully after this integration:

```text
BUILD SUCCESSFUL in 45s
```

## In-editor change proposal workflow

Project AI now supports a reviewable change workflow inside the editor. After sending a request, the user can select `اقتراح تعديل`; the selected provider returns a structured file proposal. A review dialog lists the root cause, explanation, actions, and paths. The user must explicitly choose `إنشاء نقطة حفظ وتطبيق`. `ChangeApplier` validates relative paths, creates a checkpoint before writing, and exposes `تراجع` to restore the last checkpoint.

The workflow was verified by a successful Debug build:

```text
BUILD SUCCESSFUL in 33s
```

## Execution and preview verification

The request path is now explicit: the selected provider receives the project and open-file context; chat answers remain informational; `اقتراح تعديل` requests a structured `FixResponse`; the review dialog displays affected paths; approval creates a Checkpoint and `ChangeApplier` writes validated relative paths under the project root; `تراجع` restores the latest checkpoint.

The editor's phone-icon `معاينة` button now opens `PreviewFragment` in the app's main fragment container with back-stack navigation. The Preview screen reads the project safely, detects XML/Compose/Java, renders the supported XML subset inside a phone-shaped frame, and provides refresh and back actions. It is a pre-install structural preview, not an emulator or a full Compose runtime.

Build result after the integration:

```text
BUILD SUCCESSFUL in 26s
```

## Branding and package rename

The source branding is now `CodeAssist.ai`, with application ID and Android namespace `org.codeassist.unofficial.ai`. All generated `R` and `BuildConfig` imports were updated consistently, and the manifest package/authority now follow the new application ID. The launcher icon was replaced with a new CodeAssist.ai mark featuring white `AI` centered between code brackets on a blue developer-tool background, exported at mdpi through xxxhdpi sizes.

Per request, no APK was assembled or exported for this branding-only update. A source compile check passed with `:app:compileDebugJavaWithJavac` (`BUILD SUCCESSFUL in 39s`).
