package com.tyron.code.ai;

import android.content.Context;
import android.graphics.Color;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import java.io.File;
import java.io.FileInputStream;

/** Renders a safe subset of Android XML layouts without executing project code. */
public final class PreviewRenderer {
  private static final long MAX_XML_BYTES=1024L*1024L;
  private final Context context;
  public PreviewRenderer(Context context){this.context=context;}
  public View render(File xml)throws Exception{if(xml==null||!xml.isFile())throw new IllegalArgumentException("ملف XML غير موجود");if(xml.length()>MAX_XML_BYTES)throw new IllegalArgumentException("ملف XML كبير جداً للمعاينة");XmlPullParser parser=Xml.newPullParser();try(FileInputStream input=new FileInputStream(xml)){parser.setInput(input,"UTF-8");return parse(parser);}}
  private View parse(XmlPullParser parser)throws Exception{int event;while((event=parser.next())!=XmlPullParser.START_TAG&&event!=XmlPullParser.END_DOCUMENT){}if(event==XmlPullParser.END_DOCUMENT)throw new IllegalStateException("ملف XML فارغ");return parseElement(parser);}
  private View parseElement(XmlPullParser parser)throws Exception{String name=parser.getName();View view=createView(name,parser);if(view instanceof ViewGroup){ViewGroup group=(ViewGroup)view;int event;while((event=parser.next())!=XmlPullParser.END_TAG){if(event==XmlPullParser.START_TAG)group.addView(parseElement(parser));}}else skip(parser);return view;}
  private View createView(String name,XmlPullParser parser){View view;if(name.endsWith("LinearLayout")){LinearLayout layout=new LinearLayout(context);layout.setOrientation("horizontal".equals(parser.getAttributeValue("http://schemas.android.com/apk/res/android","orientation"))?LinearLayout.HORIZONTAL:LinearLayout.VERTICAL);view=layout;}else if(name.endsWith("ScrollView"))view=new ScrollView(context);else if(name.endsWith("Button"))view=new Button(context);else if(name.endsWith("EditText"))view=new EditText(context);else if(name.endsWith("ImageView"))view=new ImageView(context);else view=new TextView(context);if(view instanceof TextView){TextView text=(TextView)view;String value=parser.getAttributeValue("http://schemas.android.com/apk/res/android","text");text.setText(value==null?name.substring(name.lastIndexOf('.')+1):value);text.setTextColor(Color.DKGRAY);text.setPadding(16,12,16,12);}view.setBackgroundColor(Color.WHITE);view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));return view;}
  private void skip(XmlPullParser parser)throws Exception{int depth=1;while(depth!=0){int event=parser.next();if(event==XmlPullParser.START_TAG)depth++;else if(event==XmlPullParser.END_TAG)depth--;}}
}
