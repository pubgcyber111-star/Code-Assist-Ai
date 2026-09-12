package com.tyron.code.ai;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class AIEngineManager {
  private final SecureKeyStore keys; private final SharedPreferences prefs;
  public AIEngineManager(Context context){Context c=context.getApplicationContext();keys=new SecureKeyStore(c);prefs=c.getSharedPreferences("codeassist_ai_settings",Context.MODE_PRIVATE);}
  public List<AIProviderCatalog.Provider> providers(){return AIProviderCatalog.all();}
  public AIProviderCatalog.Provider selectedProvider(){return AIProviderCatalog.byId(prefs.getString("provider","openai"));}
  public void selectProvider(String id){prefs.edit().putString("provider",id).apply();}
  public void saveProvider(String id,String key,String endpoint,String model){keys.put("key_"+id,key==null?"":key.trim());prefs.edit().putString("endpoint_"+id,endpoint==null?"":endpoint.trim()).putString("model_"+id,model==null?"":model.trim()).apply();}
  public String savedKey(String id){return keys.get("key_"+id);}
  public String endpoint(AIProviderCatalog.Provider p){return prefs.getString("endpoint_"+p.id,p.endpoint);}
  public String model(AIProviderCatalog.Provider p){return prefs.getString("model_"+p.id,p.model);}
  public boolean isConfigured(){for(AIProviderCatalog.Provider p:providers())if(!savedKey(p.id).isEmpty())return true;return false;}
  public AIModels.ProjectResponse generateProject(String request)throws Exception{return parseProject(call(projectPrompt()+"\nUser request:\n"+request));}
  public AIModels.FixResponse fixBuildError(String log,String context)throws Exception{JSONObject r=new JSONObject(cleanJson(call(fixPrompt()+"\nBuild log:\n"+limit(log,12000)+"\nProject context:\n"+limit(context,18000))));return new AIModels.FixResponse(r.optString("rootCause"),r.optString("explanation"),parseFiles(r.optJSONArray("modifiedFiles")));}
  public String chat(String message,String projectContext,String conversation)throws Exception{return cleanText(call("You are CodeAssist AI, a senior Android pair programmer. Answer in the user's language. Do not modify files in chat mode.\nProject context:\n"+limit(projectContext,8000)+"\nConversation:\n"+limit(conversation,12000)+"\nNew user message:\n"+limit(message,6000)));}
  public AIModels.FixResponse proposeChanges(String request,String projectContext,String conversation)throws Exception{String prompt="You are CodeAssist AI applying a requested project change. Return ONLY JSON: {\"rootCause\":\"Change summary\",\"explanation\":\"Implementation details\",\"modifiedFiles\":[{\"path\":\"relative path\",\"content\":\"complete file content\",\"action\":\"CREATE|UPDATE|DELETE\"}]}. Never use absolute paths, markdown, secrets, or files outside the project.\nProject context:\n"+limit(projectContext,16000)+"\nConversation:\n"+limit(conversation,10000)+"\nRequested change:\n"+limit(request,6000);JSONObject r=new JSONObject(cleanJson(call(prompt)));return new AIModels.FixResponse(r.optString("rootCause"),r.optString("explanation"),parseFiles(r.optJSONArray("modifiedFiles")));}
  private String projectPrompt(){return "You are a senior Android architect. Return ONLY JSON: {\"projectName\":\"...\",\"packageName\":\"...\",\"summary\":\"...\",\"files\":[{\"path\":\"relative path\",\"content\":\"full file\",\"action\":\"CREATE\"}]}. Generate a buildable Kotlin Android project. Never use absolute paths or markdown.";}
  private String fixPrompt(){return "You are a senior Android debugging agent. Return ONLY JSON: {\"rootCause\":\"...\",\"explanation\":\"...\",\"modifiedFiles\":[{\"path\":\"relative path\",\"content\":\"full file\",\"action\":\"UPDATE\"}]}. Never use absolute paths or markdown.";}
  private String call(String prompt)throws Exception{AIProviderCatalog.Provider p=selectedProvider();String key=savedKey(p.id);if(key.isEmpty())throw new IllegalStateException("أضف مفتاح API للمزود المحدد: "+p.name);if(p.protocol==AIProviderCatalog.Protocol.GEMINI)return extractGemini(post(endpoint(p)+"?key="+key,geminiBody(prompt,p),null));if(p.protocol==AIProviderCatalog.Protocol.ANTHROPIC)return extractAnthropic(post(endpoint(p),anthropicBody(prompt,p),key));return extractOpenAI(post(endpoint(p),openaiBody(prompt,p),key));}
  private String openaiBody(String prompt,AIProviderCatalog.Provider p)throws Exception{return new JSONObject().put("model",model(p)).put("temperature",0.1).put("messages",new JSONArray().put(new JSONObject().put("role","user").put("content",prompt))).toString();}
  private String geminiBody(String prompt,AIProviderCatalog.Provider p)throws Exception{return new JSONObject().put("contents",new JSONArray().put(new JSONObject().put("parts",new JSONArray().put(new JSONObject().put("text",prompt))))).toString();}
  private String anthropicBody(String prompt,AIProviderCatalog.Provider p)throws Exception{return new JSONObject().put("model",model(p)).put("max_tokens",8192).put("messages",new JSONArray().put(new JSONObject().put("role","user").put("content",prompt))).toString();}
  private String post(String endpoint,String body,String bearer)throws Exception{HttpURLConnection c=(HttpURLConnection)new URL(endpoint).openConnection();c.setConnectTimeout(30000);c.setReadTimeout(120000);c.setRequestMethod("POST");c.setDoOutput(true);c.setRequestProperty("Content-Type","application/json");if(bearer!=null)c.setRequestProperty("Authorization","Bearer "+bearer);if(endpoint.contains("anthropic")){c.setRequestProperty("x-api-key",bearer);c.setRequestProperty("anthropic-version","2023-06-01");c.setRequestProperty("anthropic-version","2023-06-01");}try(OutputStream out=c.getOutputStream()){out.write(body.getBytes(StandardCharsets.UTF_8));}int code=c.getResponseCode();BufferedReader r=new BufferedReader(new InputStreamReader(code>=200&&code<300?c.getInputStream():c.getErrorStream(),StandardCharsets.UTF_8));StringBuilder s=new StringBuilder();String line;while((line=r.readLine())!=null)s.append(line);if(code<200||code>=300)throw new IllegalStateException("استجابة المزود HTTP "+code+": "+limit(s.toString(),300));return s.toString();}
  private String extractGemini(String raw)throws Exception{return new JSONObject(raw).getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");}
  private String extractOpenAI(String raw)throws Exception{return new JSONObject(raw).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");}
  private String extractAnthropic(String raw)throws Exception{return new JSONObject(raw).getJSONArray("content").getJSONObject(0).getString("text");}
  private AIModels.ProjectResponse parseProject(String raw)throws Exception{JSONObject r=new JSONObject(cleanJson(raw));return new AIModels.ProjectResponse(r.optString("projectName","AIProject"),r.optString("packageName","com.example.aiproject"),r.optString("summary"),parseFiles(r.optJSONArray("files")));}
  private List<AIModels.GeneratedFile> parseFiles(JSONArray a)throws Exception{List<AIModels.GeneratedFile> f=new ArrayList<>();if(a==null)return f;for(int i=0;i<a.length();i++){JSONObject o=a.getJSONObject(i);f.add(new AIModels.GeneratedFile(o.optString("path"),o.optString("content"),o.optString("action","CREATE")));}return f;}
  private String cleanJson(String raw){String t=raw.trim();if(t.startsWith("```"))t=t.replaceFirst("^```(?:json)?\\s*","").replaceFirst("\\s*```$","");int s=t.indexOf('{'),e=t.lastIndexOf('}');return s>=0&&e>s?t.substring(s,e+1):t;}
  private String cleanText(String raw){String t=raw==null?"":raw.trim();return t.startsWith("```")&&t.endsWith("```")?t.substring(t.indexOf('\n')+1,t.length()-3).trim():t;}
  private String limit(String v,int max){return v==null?"":v.length()<=max?v:v.substring(v.length()-max);}
}
