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
    new Provider("openai","OpenAI","https://api.openai.com/v1/chat/completions","gpt-4o-mini",Protocol.OPENAI,"gpt-4o-mini","gpt-4o","gpt-4.1-mini","gpt-4.1","o4-mini"),
    new Provider("gemini","Google Gemini","https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent","gemini-2.0-flash",Protocol.GEMINI,"gemini-2.0-flash","gemini-2.0-flash-lite","gemini-1.5-flash","gemini-1.5-pro","gemini-2.5-flash"),
    new Provider("anthropic","Anthropic Claude","https://api.anthropic.com/v1/messages","claude-3-5-haiku-latest",Protocol.ANTHROPIC,"claude-3-5-haiku-latest","claude-3-5-sonnet-latest","claude-3-7-sonnet-latest","claude-sonnet-4-20250514","claude-opus-4-20250514"),
    new Provider("openrouter","OpenRouter","https://openrouter.ai/api/v1/chat/completions","openai/gpt-4o-mini",Protocol.OPENAI,"openai/gpt-4o-mini","google/gemini-2.0-flash-001","anthropic/claude-3.5-sonnet","deepseek/deepseek-chat","meta-llama/llama-3.3-70b-instruct"),
    new Provider("groq","Groq","https://api.groq.com/openai/v1/chat/completions","llama-3.1-8b-instant",Protocol.OPENAI,"llama-3.1-8b-instant","llama-3.3-70b-versatile","llama-4-scout-17b-16e-instruct","mixtral-8x7b-32768","gemma2-9b-it"),
    new Provider("together","Together AI","https://api.together.xyz/v1/chat/completions","meta-llama/Meta-Llama-3.1-8B-Instruct-Turbo",Protocol.OPENAI,"meta-llama/Meta-Llama-3.1-8B-Instruct-Turbo","meta-llama/Llama-3.3-70B-Instruct-Turbo","Qwen/Qwen2.5-72B-Instruct-Turbo","deepseek-ai/DeepSeek-V3"),
    new Provider("deepseek","DeepSeek","https://api.deepseek.com/chat/completions","deepseek-chat",Protocol.OPENAI,"deepseek-chat","deepseek-reasoner"),
    new Provider("mistral","Mistral AI","https://api.mistral.ai/v1/chat/completions","mistral-small-latest",Protocol.OPENAI,"mistral-small-latest","mistral-large-latest","codestral-latest","ministral-8b-latest","pixtral-large-latest"),
    new Provider("cohere","Cohere","https://api.cohere.com/compatibility/v1/chat/completions","command-r-plus",Protocol.OPENAI,"command-r-plus","command-r","command-a-03-2025"),
    new Provider("xai","xAI Grok","https://api.x.ai/v1/chat/completions","grok-3-mini",Protocol.OPENAI,"grok-3-mini","grok-3","grok-2-latest","grok-2-vision-latest"),
    new Provider("perplexity","Perplexity","https://api.perplexity.ai/chat/completions","sonar",Protocol.OPENAI,"sonar","sonar-pro","sonar-reasoning-pro","sonar-deep-research"),
    new Provider("cerebras","Cerebras","https://api.cerebras.ai/v1/chat/completions","llama-3.1-8b",Protocol.OPENAI,"llama-3.1-8b","llama-3.3-70b","qwen-3-32b"),
    new Provider("azure","Azure OpenAI","https://YOUR_RESOURCE.openai.azure.com/openai/deployments/YOUR_DEPLOYMENT/chat/completions?api-version=2024-10-21","YOUR_DEPLOYMENT",Protocol.OPENAI,"YOUR_DEPLOYMENT","gpt-4o","gpt-4.1"),
    new Provider("ollama","Ollama المحلي","http://127.0.0.1:11434/v1/chat/completions","llama3.2",Protocol.OPENAI,"llama3.2","qwen2.5-coder","deepseek-coder-v2","llama3.1","mistral")
  ));
  private AIProviderCatalog(){}
  public static List<Provider> all(){return ALL;}
  public static Provider byId(String id){for(Provider p:ALL)if(p.id.equals(id))return p;return ALL.get(0);}
}
