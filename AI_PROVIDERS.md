# CodeAssist AI Providers and Models

The provider card now displays fourteen AI services and a model identifier list for each selected service. Selecting a provider refreshes the model list; selecting a model writes its exact identifier into the active configuration. The endpoint and API key remain editable for custom deployments such as Azure OpenAI and local Ollama.

The user can save and activate the selected provider/model, or run **اختبار الاتصال** before using chat, generation, build repair, or file proposals. Gemini model selection also updates the model segment of its native endpoint automatically.

## Error handling

The client classifies common failures for the user: `AUTH` for missing or rejected credentials, `MODEL/ENDPOINT` for invalid model or URL combinations, `NETWORK` for DNS/reachability problems, `TIMEOUT` for slow providers, `QUOTA` for rate limits or exhausted quotas, and `PROVIDER` for temporary server failures. HTTP response details are bounded and API keys are never included in error text.

## Execution contract

Chat responses are informational. Project generation and change proposals return structured file objects. Applying a proposal remains an explicit user action: the app lists the files, creates a checkpoint, validates that each path stays under the project root, and then creates, updates, or deletes the requested files. The rollback action restores the latest checkpoint.

Model names are provider identifiers and availability can change. A provider may remove, rename, gate, or rate-limit a model; the connection test exposes that error instead of silently selecting another model. Provider pricing, account access, quotas, and terms remain the user's responsibility.
