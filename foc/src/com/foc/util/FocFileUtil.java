package com.foc.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FocFileUtil {

	public static void saveStreamToFile(InputStream in, String fileName){
		try {
			OutputStream out = new FileOutputStream(fileName);
		  byte[] buf = new byte[1024];
		  int len;
		  while((len=in.read(buf))>0){
		    out.write(buf,0,len);
		  }
		  out.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	public static byte[] inputStreamToByteArray(InputStream inputStream){
		ByteArrayOutputStream byteArrayOutputStream = inputStreamToByteArrayOutputStream(inputStream);
		return byteArrayOutputStream.toByteArray();
	}
	
	public static ByteArrayOutputStream inputStreamToByteArrayOutputStream(InputStream inputStream){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try{
			int byteRead;
			while((byteRead = inputStream.read()) != -1) {
				byteArrayOutputStream.write(byteRead);
			}
			byteArrayOutputStream.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
		return byteArrayOutputStream;
	}
}
