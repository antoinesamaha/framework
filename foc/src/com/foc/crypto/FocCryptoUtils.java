package com.foc.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("serial")
public class FocCryptoUtils {
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
 
	public static void encrypt(String key, File inputFile, File outputFile) throws Exception {
		doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
	}
 
  public static void decrypt(String key, File inputFile, File outputFile) throws Exception {
  	doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
  }
 
  private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws Exception {
  	try {
  		Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(cipherMode, secretKey);
             
      FileInputStream inputStream = new FileInputStream(inputFile);
      byte[] inputBytes = new byte[(int) inputFile.length()];
      inputStream.read(inputBytes);
             
      byte[] outputBytes = cipher.doFinal(inputBytes);
             
      FileOutputStream outputStream = new FileOutputStream(outputFile);
      outputStream.write(outputBytes);
             
      inputStream.close();
      outputStream.close();
             
  	} catch (Exception ex) {
  		System.out.println(ex.getMessage());
  	}
  }
    
  public static void main(String[] args) {
    String key = "Mary has one cat";
    File inputFile = new File("c:/temp/SPIN-Selling.jpg");
    File encryptedFile = new File("c:/temp/SPIN-Selling_encrypted.jpg");
    File decryptedFile = new File("c:/temp/SPIN-Selling2.jpg");
     
    try {
      FocCryptoUtils.encrypt(key, inputFile, encryptedFile);
      FocCryptoUtils.decrypt(key, encryptedFile, decryptedFile);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }
}
