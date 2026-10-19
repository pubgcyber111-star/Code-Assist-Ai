package com.tyron.code.ai;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Builds bounded, privacy-aware project inventory/context for AI review. */
public final class ProjectAuditService {
  private static final int MAX_FILES = 5000;
  private static final int MAX_TEXT_FILE_BYTES = 24000;
  private static final int MAX_CONTEXT_CHARS = 180000;
  private static final Pattern PACKAGE = Pattern.compile("(?:applicationId|package|namespace)\\s*[=:]\\s*[\\\"']?([A-Za-z_][A-Za-z0-9_.]+)");
  private static final List<String> BINARY = java.util.Arrays.asList("png","jpg","jpeg","webp","gif","mp3","wav","mp4","zip","7z","aar","jar","so","dex","keystore","db");

  public String buildContext(File root) throws Exception {
    if (root == null || !root.exists() || !root.isDirectory()) throw new IllegalArgumentException("مجلد المشروع غير موجود");
    List<File> files = new ArrayList<>(); collect(root, root, files);
    Collections.sort(files, Comparator.comparing(File::getAbsolutePath));
    StringBuilder out = new StringBuilder();
    out.append("PROJECT_AUDIT_V1\nroot=").append(root.getCanonicalPath()).append("\n");
    long total=0; int text=0; List<String> packages=new ArrayList<>();
    out.append("FILES_BEGIN\n");
    for (File file : files) {
      long size=file.length(); total+=size; String rel=root.toPath().relativize(file.toPath()).toString();
      String ext=extension(file.getName()); boolean isText=!BINARY.contains(ext);
      if (isText) text++;
      out.append(rel).append(" | bytes=").append(size).append(" | type=").append(isText?"text":"binary").append("\n");
      if (isText && size<=MAX_TEXT_FILE_BYTES && shouldRead(file)) {
        String source=new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        Matcher matcher=PACKAGE.matcher(source); while(matcher.find() && packages.size()<40) if(!packages.contains(matcher.group(1))) packages.add(matcher.group(1));
        int remaining=MAX_CONTEXT_CHARS-out.length();
        if (remaining>300) out.append("FILE_BEGIN ").append(rel).append("\n").append(source,0,Math.min(source.length(),Math.min(MAX_TEXT_FILE_BYTES,remaining-80))).append("\nFILE_END\n");
      }
    }
    out.append("FILES_END\nfileCount=").append(files.size()).append("\ntotalBytes=").append(total).append("\ntextFiles=").append(text).append("\npackages=").append(packages).append("\n");
    if(files.size()>=MAX_FILES) out.append("WARNING=file inventory capped at ").append(MAX_FILES).append(" files\n");
    return out.length()>MAX_CONTEXT_CHARS?out.substring(0,MAX_CONTEXT_CHARS):out.toString();
  }

  private void collect(File root, File current, List<File> result) {
    if(result.size()>=MAX_FILES) return;
    File[] children=current.listFiles(); if(children==null)return;
    for(File f:children){ if(result.size()>=MAX_FILES)break; if(f.isDirectory() && isExcluded(root,f))continue; if(f.isFile())result.add(f); else if(f.isDirectory())collect(root,f,result); }
  }
  private boolean isExcluded(File root, File dir){String rel=root.toPath().relativize(dir.toPath()).toString().replace('\\','/'); return rel.equals(".git")||rel.startsWith(".git/")||rel.equals("build")||rel.endsWith("/build")||rel.contains("/.gradle");}
  private boolean shouldRead(File f){return !f.getName().equals("local.properties")&&!f.getName().endsWith(".keystore")&&!f.getName().endsWith(".jks");}
  private String extension(String name){int i=name.lastIndexOf('.');return i<0?"":name.substring(i+1).toLowerCase(java.util.Locale.US);}
}
