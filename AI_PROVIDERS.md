# CodeAssist AI Providers

The AI Center now exposes a single Arabic provider configuration surface with fourteen provider presets: OpenAI, Google Gemini, Anthropic Claude, OpenRouter, Groq, Together AI, DeepSeek, Mistral AI, Cohere, xAI Grok, Perplexity, Cerebras, Azure OpenAI, and local Ollama.

Each preset supplies a default protocol, endpoint, and model. The user can select a provider, review or customize the endpoint and model, enter its API key, and save it. The key is stored using the Android Keystore-backed AES-GCM `SecureKeyStore`; it is not written to source, logs, or plain preferences. Endpoint and model settings are stored locally as non-secret configuration.

The active provider is used consistently by project generation, chat, structured change proposals, and build repair. OpenAI-compatible providers use the `/chat/completions` contract. Gemini uses `generateContent`, and Claude uses the Anthropic Messages contract. Azure OpenAI and Ollama presets are intentionally editable because their resource URL or local network address varies by installation.

The app does not ship provider keys, claim that a provider account is free, or hide provider errors. The user remains responsible for provider terms, quotas, billing, endpoint reachability, and the permissions required by any custom endpoint.
