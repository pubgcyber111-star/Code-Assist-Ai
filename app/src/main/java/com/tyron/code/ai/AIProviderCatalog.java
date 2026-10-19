package com.tyron.code.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class AIProviderCatalog {
  public enum Protocol { OPENAI, GEMINI, ANTHROPIC }
  public static final class Provider {
    public final String id, name, endpoint, model;
    public final Protocol protocol;
    private final List<String> models;
    Provider(String id, String name, String endpoint, String model, Protocol protocol, String... models) {
      this.id=id; this.name=name; this.endpoint=endpoint; this.model=model; this.protocol=protocol;
      this.models=Collections.unmodifiableList(Arrays.asList(models));
    }
    public String label(){ return name; }
    public List<String> models(){ return models; }
  }
  private static final List<Provider> ALL=Collections.unmodifiableList(Arrays.asList(
    new Provider("openai","OpenAI","https://api.openai.com/v1/chat/completions","gpt-4.1",Protocol.OPENAI,
      "gpt-5.6-sol","gpt-5.5","gpt-5.4","gpt-5.3-codex","gpt-5.2-codex","gpt-5.1-codex-max","gpt-5.1","gpt-5","gpt-4.1","gpt-4.1-mini","gpt-4.1-nano","gpt-4o","gpt-4o-mini","gpt-4-turbo","o3","o3-mini","o4-mini","o1","o1-pro"),
    new Provider("gemini","Google Gemini","https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent","gemini-2.5-flash",Protocol.GEMINI,
      "gemini-3.1-pro-preview","gemini-3.8-flash","gemini-3.7-flash","gemini-3.6-flash","gemini-3.5-flash","gemini-3.5-flash-lite","gemini-3.1-flash-lite","gemini-3.1-flash-live-preview","gemini-3.1-flash-tts-preview","gemini-3.5-transcribe","gemini-2.5-pro","gemini-2.5-flash","gemini-2.5-flash-native-audio-preview-12-2025"),
    new Provider("anthropic","Anthropic Claude","https://api.anthropic.com/v1/messages","claude-sonnet-4-6",Protocol.ANTHROPIC,
      "claude-opus-5","claude-sonnet-5","claude-opus-4-8","claude-opus-4-7","claude-opus-4-6","claude-sonnet-4-6","claude-opus-4-5","claude-haiku-4-5","claude-sonnet-4-5","claude-4-1-opus","claude-4-sonnet","claude-3-5-sonnet","claude-3-haiku","claude-3-opus"),
    new Provider("openrouter","OpenRouter","https://openrouter.ai/api/v1/chat/completions","anthropic/claude-sonnet-5",Protocol.OPENAI,
      "anthropic/claude-fable-5","anthropic/claude-sonnet-5","anthropic/claude-opus-4.8-fast","anthropic/claude-opus-4.8","google/gemini-3.1-flash-lite-image","openai/gpt-image-1","openai/gpt-image-2"),
    new Provider("groq","Groq","https://api.groq.com/openai/v1/chat/completions","llama-3.3-70b-versatile",Protocol.OPENAI,
      "whisper-large-v3","whisper-large-v3-turbo"),
    new Provider("together","Together AI","https://api.together.xyz/v1/chat/completions","meta-llama/Llama-3.3-70B-Instruct-Turbo",Protocol.OPENAI,
      "deepseek-ai/DeepSeek-V4-Pro-0813","deepseek-ai/DeepSeek-V4-Flash-0731","moonshotai/Kimi-K3","Qwen/Qwen3.8-2.4T-A95B","zai-org/GLM-5.3","meta-llama/Llama-3.3-70B-Instruct-Turbo"),
    new Provider("deepseek","DeepSeek","https://api.deepseek.com/chat/completions","deepseek-v4-flash",Protocol.OPENAI,
      "deepseek-v4-flash","deepseek-v4-pro","deepseek-chat","deepseek-reasoner"),
    new Provider("mistral","Mistral AI","https://api.mistral.ai/v1/chat/completions","mistral-medium-2508",Protocol.OPENAI,
      "mistral-medium-2508","mistral-small-2506","devstral-2512","magistral-medium-2509","magistral-small-2509","mistral-ocr-2505","codestral-2501","mistral-large-2411","pixtral-large-2411"),
    new Provider("cohere","Cohere","https://api.cohere.com/compatibility/v1/chat/completions","command-a-03-2025",Protocol.OPENAI,
      "command-a-03-2025","command-a-plus-05-2026","command-a-reasoning-08-2025","command-a-translate-08-2025","command-a-vision-07-2025","command-r-08-2024","command-r-plus-08-2024","command-r7b-12-2024","command-r7b-arabic-02-2025","north-mini-code-1-0"),
    new Provider("xai","xAI Grok","https://api.x.ai/v1/chat/completions","grok-4-1-fast-non-reasoning",Protocol.OPENAI,
      "grok-4-1-fast-non-reasoning","grok-code-fast-1","grok-2-vision-1212","grok-3-mini"),
    new Provider("perplexity","Perplexity","https://api.perplexity.ai/chat/completions","sonar",Protocol.OPENAI,
      "sonar","gpt-5.2","claude-sonnet-4.6","gemini-3.1-pro","claude-4.6-opus","nemotron-3-super-120b"),
    new Provider("cerebras","Cerebras","https://api.cerebras.ai/v1/chat/completions","gpt-oss-120b",Protocol.OPENAI,
      "gpt-oss-120b","qwen-3.8-27b"),
    new Provider("azure","Azure OpenAI","https://YOUR_RESOURCE.openai.azure.com/openai/deployments/YOUR_DEPLOYMENT/chat/completions?api-version=2024-10-21","YOUR_DEPLOYMENT",Protocol.OPENAI,
      "YOUR_DEPLOYMENT","gpt-5.5","gpt-5.4","gpt-5.3-codex","gpt-4.1","gpt-4o"),
    new Provider("ollama","Ollama المحلي","http://127.0.0.1:11434/v1/chat/completions","llama3.2",Protocol.OPENAI,
      "llama3.2","qwen2.5-coder","deepseek-coder-v2","llama3.1","mistral")
  ));
  private AIProviderCatalog(){}
  public static List<Provider> all(){return ALL;}
  public static Provider byId(String id){for(Provider p:ALL)if(p.id.equals(id))return p;return ALL.get(0);}
}
