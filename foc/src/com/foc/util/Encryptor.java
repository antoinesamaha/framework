package com.foc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.foc.shared.Base64;

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Encryptor {
  public static String encrypt_MD5(String str){
    String encrypted = null;
    byte [] data = str.getBytes();
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(data);
      byte[] digest = md.digest();
      
      encrypted =  Base64.encode(digest).toString();
      encrypted = encrypted.substring(0, encrypted.indexOf("=="));
    }catch (NoSuchAlgorithmException e){ 
      e.printStackTrace();
    }
    return encrypted;
  }
}
