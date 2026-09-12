package com.tyron.code.ai;

import com.tyron.builder.project.Project;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

/** Coordinates diagnosis, checkpointing and bounded application of AI build fixes. */
public final class BuildRepairEngine {
  public interface Callback { void onStatus(String status); }
  private final AIEngineManager ai;
  private final File checkpointRoot;
  private CheckpointManager lastCheckpoints;
  public BuildRepairEngine(android.content.Context context) { ai = new AIEngineManager(context); checkpointRoot = new File(context.getFilesDir(), "ai-checkpoints"); }

  public AIModels.FixResponse diagnose(Project project, String buildLog, Callback callback) throws Exception {
    if (project == null || project.getRootFile() == null) throw new IllegalArgumentException("لا يوجد مشروع مفتوح");
    callback.onStatus("جاري إنشاء نقطة حفظ قبل التحليل…");
    lastCheckpoints = new CheckpointManager(project.getRootFile(), checkpointRoot);
    lastCheckpoints.create("قبل إصلاح البناء بالذكاء الاصطناعي");
    callback.onStatus("جاري جمع الملفات ذات الصلة…");
    Map<String, String> context = collect(project.getRootFile());
    StringBuilder compact = new StringBuilder();
    for (Map.Entry<String, String> entry : context.entrySet()) compact.append("FILE: ").append(entry.getKey()).append("\n").append(entry.getValue()).append("\n---\n");
    callback.onStatus("جاري تحليل السجل…");
    return ai.fixBuildError(buildLog, compact.toString());
  }

  public boolean rollback(Project project) throws Exception {
    if (lastCheckpoints == null || lastCheckpoints.history().isEmpty()) return false;
    return lastCheckpoints.restore(lastCheckpoints.history().get(lastCheckpoints.history().size() - 1).id);
  }

  public int apply(Project project, AIModels.FixResponse response) throws Exception {
    if (project == null || response == null) throw new IllegalArgumentException("بيانات الإصلاح غير مكتملة");
    int applied = 0;
    File root = project.getRootFile().getCanonicalFile();
    for (AIModels.GeneratedFile item : response.files) {
      if (item.path.isEmpty() || item.path.contains("\\") || new File(item.path).isAbsolute()) continue;
      File target = new File(root, item.path).getCanonicalFile();
      if (!target.toPath().startsWith(root.toPath())) continue;
      if ("DELETE".equals(item.action)) { if (target.exists() && target.delete()) applied++; continue; }
      File parent = target.getParentFile(); if (parent != null) parent.mkdirs();
      Files.write(target.toPath(), item.content.getBytes(StandardCharsets.UTF_8)); applied++;
    }
    return applied;
  }

  private Map<String, String> collect(File root) throws Exception {
    Map<String, String> result = new LinkedHashMap<>();
    Files.walk(root.toPath()).filter(Files::isRegularFile).filter(path -> {
      String name = path.getFileName().toString();
      return name.endsWith(".kt") || name.endsWith(".java") || name.endsWith(".xml") || name.endsWith(".gradle") || name.endsWith(".properties");
    }).limit(18).forEach(path -> { try { String text = new String(Files.readAllBytes(path), StandardCharsets.UTF_8); if (text.length() < 24000) result.put(root.toPath().relativize(path).toString(), text); } catch (Exception ignored) {} });
    return result;
  }
}
