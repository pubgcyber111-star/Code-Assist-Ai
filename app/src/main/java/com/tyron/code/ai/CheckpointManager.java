package com.tyron.code.ai;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CheckpointManager {
  public static final class Checkpoint { public final String id; public final String label; public final File folder; Checkpoint(String id, String label, File folder) { this.id=id; this.label=label; this.folder=folder; } }
  private final File project; private final File storage; private final List<Checkpoint> history = new ArrayList<>();
  public CheckpointManager(File project, File storage) { this.project=project; this.storage=storage; }
  public synchronized Checkpoint create(String label) throws Exception { String id = "checkpoint_" + System.currentTimeMillis(); File folder = new File(storage, id); copy(project, folder); Checkpoint checkpoint = new Checkpoint(id, label, folder); history.add(checkpoint); return checkpoint; }
  public synchronized List<Checkpoint> history() { return Collections.unmodifiableList(new ArrayList<>(history)); }
  public synchronized boolean restore(String id) throws Exception { for (Checkpoint checkpoint: history) if (checkpoint.id.equals(id)) { if (project.exists()) delete(project); copy(checkpoint.folder, project); return true; } return false; }
  private void copy(File from, File to) throws Exception { if (!from.exists()) return; if (from.isDirectory()) { if (!to.exists() && !to.mkdirs()) throw new IllegalStateException("تعذر إنشاء نقطة الحفظ"); File[] children=from.listFiles(); if(children!=null) for(File child:children) copy(child,new File(to,child.getName())); } else { java.nio.file.Files.copy(from.toPath(),to.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING); } }
  private void delete(File file) { if (file.isDirectory()) { File[] children=file.listFiles(); if(children!=null) for(File child:children) delete(child); } file.delete(); }
}
