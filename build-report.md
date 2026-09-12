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
| Package | `org.codeassist.unofficial` |
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
