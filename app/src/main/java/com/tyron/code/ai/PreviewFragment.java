package com.tyron.code.ai;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.codeassist.unofficial.R;

public final class PreviewFragment extends Fragment {
  private File project; private FrameLayout phone; private TextView status;
  public static PreviewFragment newInstance(File project){PreviewFragment f=new PreviewFragment();Bundle b=new Bundle();b.putString("project",project.getAbsolutePath());f.setArguments(b);return f;}
  @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle state){return inflater.inflate(R.layout.ai_preview_fragment,container,false);}
  @Override public void onViewCreated(@NonNull View view,@Nullable Bundle state){project=new File(requireArguments().getString("project"));phone=view.findViewById(R.id.phoneFrame);status=view.findViewById(R.id.previewStatus);((MaterialToolbar)view.findViewById(R.id.previewToolbar)).setNavigationOnClickListener(v->requireActivity().getSupportFragmentManager().popBackStack());((MaterialButton)view.findViewById(R.id.refreshPreview)).setOnClickListener(v->loadPreview());loadPreview();}
  private void loadPreview(){phone.removeAllViews();if(!project.exists()){status.setText("المشروع غير موجود");return;}try{ProjectTypeDetector.Result result=new ProjectTypeDetector().detect(project);status.setText(result.detail+(result.source==null?"":" — "+result.source.getName()));if(result.type==ProjectTypeDetector.Type.XML){phone.addView(new PreviewRenderer(requireContext()).render(result.source));}else if(result.type==ProjectTypeDetector.Type.COMPOSE){phone.addView(sourceCard(result.source,"Compose Preview (تحليل المصدر)"));}else if(result.type==ProjectTypeDetector.Type.JAVA){phone.addView(sourceCard(result.source,"Java source detected"));}else{phone.addView(sourceCard(null,"لا توجد واجهة قابلة للمعاينة بعد"));}}catch(Exception e){status.setText("تعذر تجهيز المعاينة: "+e.getMessage());}}
  private View sourceCard(File source,String title){TextView card=new TextView(requireContext());card.setTextColor(Color.DKGRAY);card.setTextSize(14);card.setPadding(18,18,18,18);String content=title+"\n\n";try{if(source!=null){String text=new String(Files.readAllBytes(source.toPath()),StandardCharsets.UTF_8);content+=text.length()>2200?text.substring(0,2200)+"…":text;}}catch(Exception ignored){}card.setText(content);card.setBackgroundColor(Color.WHITE);return card;}
}
