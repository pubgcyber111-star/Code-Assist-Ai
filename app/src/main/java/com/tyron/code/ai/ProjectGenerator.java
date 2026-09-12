package com.tyron.code.ai;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public final class ProjectGenerator {
  private final File workspace;
  public ProjectGenerator(File workspace) { this.workspace = workspace; }
  public File apply(AIModels.ProjectResponse response) throws Exception {
    File project = safeChild(workspace, response.projectName.replaceAll("[^A-Za-z0-9._-]", "_"));
    if (!project.exists() && !project.mkdirs()) throw new IllegalStateException("تعذر إنشاء مجلد المشروع");
    for (AIModels.GeneratedFile item : response.files) {
      File target = safeChild(project, item.path);
      if ("DELETE".equals(item.action)) { if (target.exists() && !target.delete()) throw new IllegalStateException("تعذر حذف " + item.path); continue; }
      File parent = target.getParentFile(); if (parent != null && !parent.exists() && !parent.mkdirs()) throw new IllegalStateException("تعذر إنشاء مجلد الملف");
      try (FileOutputStream stream = new FileOutputStream(target)) { stream.write(item.content.getBytes(StandardCharsets.UTF_8)); }
    }
    return project;
  }
  private File safeChild(File root, String relative) throws Exception {
    if (relative == null || relative.trim().isEmpty() || relative.contains("\\") || new File(relative).isAbsolute()) throw new SecurityException("مسار ملف غير آمن");
    File candidate = new File(root, relative).getCanonicalFile(); File canonicalRoot = root.getCanonicalFile();
    if (!candidate.toPath().startsWith(canonicalRoot.toPath())) throw new SecurityException("تم رفض مسار خارج مساحة المشروع");
    return candidate;
  }
}
