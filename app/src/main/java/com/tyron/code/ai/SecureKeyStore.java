package com.tyron.code.ai;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

final class SecureKeyStore {
  private static final String PREFS="codeassist_ai_secure"; private static final String KEY_ALIAS="codeassist_ai_key_v1"; private final SharedPreferences prefs;
  SecureKeyStore(Context context){prefs=context.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
  void put(String name,String value){try{Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,getKey());prefs.edit().putString(name,Base64.encodeToString(c.getIV(),Base64.NO_WRAP)+":"+Base64.encodeToString(c.doFinal(value.getBytes(StandardCharsets.UTF_8)),Base64.NO_WRAP)).apply();}catch(Exception e){throw new IllegalStateException("Unable to protect API key",e);}}
  String get(String name){String stored=prefs.getString(name,"");if(stored.isEmpty())return "";try{String[] p=stored.split(":",2);Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.DECRYPT_MODE,getKey(),new GCMParameterSpec(128,Base64.decode(p[0],Base64.DEFAULT)));return new String(c.doFinal(Base64.decode(p[1],Base64.DEFAULT)),StandardCharsets.UTF_8);}catch(Exception e){return "";}}
  private SecretKey getKey()throws Exception{KeyStore s=KeyStore.getInstance("AndroidKeyStore");s.load(null);if(s.containsAlias(KEY_ALIAS))return ((KeyStore.SecretKeyEntry)s.getEntry(KEY_ALIAS,null)).getSecretKey();KeyGenerator g=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");g.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build());return g.generateKey();}
}
