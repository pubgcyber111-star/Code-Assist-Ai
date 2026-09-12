package com.tyron.code.ai;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class ChangeApplier {
  private final File checkpointRoot;
  private CheckpointManager lastCheckpoints;
  public ChangeApplier(File checkpointRoot) { this.checkpointRoot=checkpointRoot; }
  public synchronized int apply(File project, AIModels.FixResponse response) throws Exception {
    if(project==null||response==null)throw new IllegalArgumentException("بيانات التعديل غير مكتملة");
    lastCheckpoints=new CheckpointManager(project,checkpointRoot); lastCheckpoints.create("قبل تطبيق اقتراح الدردشة");
    File root=project.getCanonicalFile(); int count=0;
    for(AIModels.GeneratedFile item:response.files){ if(item.path.isEmpty()||item.path.contains("\\")||new File(item.path).isAbsolute())continue; File target=new File(root,item.path).getCanonicalFile(); if(!target.toPath().startsWith(root.toPath()))continue; if("DELETE".equals(item.action)){if(target.exists()&&target.delete())count++;continue;} File parent=target.getParentFile();if(parent!=null)parent.mkdirs();Files.write(target.toPath(),item.content.getBytes(StandardCharsets.UTF_8));count++; }
    return count;
  }
  public synchronized boolean rollback() throws Exception { if(lastCheckpoints==null||lastCheckpoints.history().isEmpty())return false; CheckpointManager.Checkpoint c=lastCheckpoints.history().get(lastCheckpoints.history().size()-1); return lastCheckpoints.restore(c.id); }
}
