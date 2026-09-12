package com.tyron.code.ai;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class ProjectTypeDetector {
  private static final long MAX_SOURCE_BYTES=2L*1024L*1024L;
  public enum Type { COMPOSE, XML, JAVA, UNKNOWN }
  public static final class Result { public final Type type; public final File source; public final String detail; Result(Type type,File source,String detail){this.type=type;this.source=source;this.detail=detail;} }
  public Result detect(File root)throws Exception{if(root==null||!root.isDirectory())return new Result(Type.UNKNOWN,null,"مسار المشروع غير صالح");File compose=find(new File(root,"app/src/main"),".kt","@Composable");if(compose!=null)return new Result(Type.COMPOSE,compose,"تم اكتشاف واجهة Jetpack Compose");File xml=find(new File(root,"app/src/main/res/layout"),".xml","");if(xml!=null)return new Result(Type.XML,xml,"تم اكتشاف واجهة Android XML");File java=find(new File(root,"app/src/main"),".java","class ");if(java!=null)return new Result(Type.JAVA,java,"تم اكتشاف مصدر Java؛ المعاينة المرئية تحتاج Layout XML مرتبطاً");return new Result(Type.UNKNOWN,null,"لم يتم اكتشاف مصدر واجهة قابل للمعاينة");}
  private File find(File root,String suffix,String marker)throws Exception{if(root==null||!root.exists()||root.length()>MAX_SOURCE_BYTES)return null;File[] files=root.listFiles();if(files==null)return null;for(File f:files){if(f.isDirectory()){String n=f.getName();if(n.equals("build")||n.equals(".gradle")||n.equals(".git"))continue;File found=find(f,suffix,marker);if(found!=null)return found;}else if(f.length()<=MAX_SOURCE_BYTES&&f.getName().endsWith(suffix)){if(marker.isEmpty()||new String(Files.readAllBytes(f.toPath()),StandardCharsets.UTF_8).contains(marker))return f;}}return null;}
}
