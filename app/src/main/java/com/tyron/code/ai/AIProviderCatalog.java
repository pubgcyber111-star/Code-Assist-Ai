package com.tyron.code.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class AIProviderCatalog {
  public enum Protocol { OPENAI, GEMINI, ANTHROPIC }
  public static final class Provider {
    public final String id,name,endpoint,model; public final Protocol protocol;
    Provider(String id,String name,String endpoint,String model,Protocol protocol){this.id=id;this.name=name;this.endpoint=endpoint;this.model=model;this.protocol=protocol;}
    public String label(){return name+" — "+model;}
  }
  private static final List<Provider> ALL=Collections.unmodifiableList(Arrays.asList(
    new Provider("openai","OpenAI","https://api.openai.com/v1/chat/completions","gpt-4o-mini",Protocol.OPENAI),
    new Provider("gemini","Google Gemini","https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent","gemini-2.0-flash",Protocol.GEMINI),
    new Provider("anthropic","Anthropic Claude","https://api.anthropic.com/v1/messages","claude-3-5-haiku-latest",Protocol.ANTHROPIC),
    new Provider("openrouter","OpenRouter","https://openrouter.ai/api/v1/chat/completions","openai/gpt-4o-mini",Protocol.OPENAI),
    new Provider("groq","Groq","https://api.groq.com/openai/v1/chat/completions","llama-3.1-8b-instant",Protocol.OPENAI),
    new Provider("together","Together AI","https://api.together.xyz/v1/chat/completions","meta-llama/Meta-Llama-3.1-8B-Instruct-Turbo",Protocol.OPENAI),
    new Provider("deepseek","DeepSeek","https://api.deepseek.com/chat/completions","deepseek-chat",Protocol.OPENAI),
    new Provider("mistral","Mistral AI","https://api.mistral.ai/v1/chat/completions","mistral-small-latest",Protocol.OPENAI),
    new Provider("cohere","Cohere","https://api.cohere.com/compatibility/v1/chat/completions","command-r-plus",Protocol.OPENAI),
    new Provider("xai","xAI Grok","https://api.x.ai/v1/chat/completions","grok-3-mini",Protocol.OPENAI),
    new Provider("perplexity","Perplexity","https://api.perplexity.ai/chat/completions","sonar",Protocol.OPENAI),
    new Provider("cerebras","Cerebras","https://api.cerebras.ai/v1/chat/completions","llama-3.1-8b",Protocol.OPENAI),
    new Provider("azure","Azure OpenAI","https://YOUR_RESOURCE.openai.azure.com/openai/deployments/YOUR_DEPLOYMENT/chat/completions?api-version=2024-10-21","YOUR_DEPLOYMENT",Protocol.OPENAI),
    new Provider("ollama","Ollama المحلي","http://127.0.0.1:11434/v1/chat/completions","llama3.2",Protocol.OPENAI)
  ));
  private AIProviderCatalog(){}
  public static List<Provider> all(){return ALL;}
  public static Provider byId(String id){for(Provider p:ALL)if(p.id.equals(id))return p;return ALL.get(0);}
}
