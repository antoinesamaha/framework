package com.foc.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.foc.Globals;

public class EncryptorSymetrical {
	private static Cipher cipher = null;

	private static void sample() throws Exception {

		// uncomment the following line to add the Provider of choice
		//Security.addProvider(new com.sun.crypto.provider.SunJCE());

		KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
		// keysize must be equal to 112 or 168 for this provider
		keyGenerator.init(168);
		SecretKey secretKey = keyGenerator.generateKey();
		cipher = Cipher.getInstance("DESede");

		String plainText = "Java Cryptography Extension";
		System.out.println("Plain Text Before Encryption: " + plainText);

		byte[] plainTextByte = plainText.getBytes("UTF8");
		byte[] encryptedBytes = encrypt(plainTextByte, secretKey);

		String encryptedText = new String(encryptedBytes, "UTF8");
		System.out.println("Encrypted Text After Encryption: " + encryptedText);

		byte[] decryptedBytes = decrypt(encryptedBytes, secretKey);
		String decryptedText = new String(decryptedBytes, "UTF8");
		System.out.println("Decrypted Text After Decryption: " + decryptedText);
	}

	private static Cipher getCipher() {
		if(cipher == null) {
			try{
				cipher = Cipher.getInstance("DESede");
			}catch (NoSuchAlgorithmException e){
				Globals.logException(e);
			}catch (NoSuchPaddingException e){
				Globals.logException(e);
			}
		}
		return cipher;
	}
	
	static byte[] encrypt(byte[] plainTextByte, SecretKey secretKey) throws Exception {
		byte[] encryptedBytes = null;
		if(getCipher() != null) {
			getCipher().init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedBytes = getCipher().doFinal(plainTextByte);
		}
		return encryptedBytes;
	}

	static byte[] decrypt(byte[] encryptedBytes, SecretKey secretKey) throws Exception {
		byte[] decryptedBytes = null;
		if(getCipher() != null) {
			getCipher().init(Cipher.DECRYPT_MODE, secretKey);
			decryptedBytes = getCipher().doFinal(encryptedBytes);
		}
		return decryptedBytes;
	}
}
