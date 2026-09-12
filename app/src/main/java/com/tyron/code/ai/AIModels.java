package com.tyron.code.ai;

import java.util.ArrayList;
import java.util.List;

public final class AIModels {
  private AIModels() {}
  public enum Provider { GEMINI, OPENAI_COMPATIBLE }
  public static final class GeneratedFile {
    public final String path; public final String content; public final String action;
    public GeneratedFile(String path, String content, String action) { this.path=path==null?"":path; this.content=content==null?"":content; this.action=action==null?"CREATE":action.toUpperCase(); }
  }
  public static final class ProjectResponse {
    public final String projectName; public final String packageName; public final String summary; public final List<GeneratedFile> files;
    public ProjectResponse(String projectName,String packageName,String summary,List<GeneratedFile> files) { this.projectName=projectName;this.packageName=packageName;this.summary=summary;this.files=files==null?new ArrayList<GeneratedFile>():files; }
  }
  public static final class FixResponse {
    public final String rootCause; public final String explanation; public final List<GeneratedFile> files;
    public FixResponse(String rootCause,String explanation,List<GeneratedFile> files) { this.rootCause=rootCause==null?"":rootCause;this.explanation=explanation==null?"":explanation;this.files=files==null?new ArrayList<GeneratedFile>():files; }
  }
}
