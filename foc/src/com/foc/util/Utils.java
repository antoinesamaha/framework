package com.foc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class Utils {

	public static boolean isStringEmpty(String input){
		return input == null || input.isEmpty();
	}
	
	public static boolean isEmail(String input){
		boolean error = isStringEmpty(input);
		if(!error){
			error = !input.contains("@") || !input.contains(".");
			if(!error){
				error = input.trim().contains(" ");
			}
		}
		return !error;
	}

	public static boolean isEqual_String(String str1, String str2){
		boolean equal = str1==null && str2==null;
		if(!equal && str1 != null && str2 != null){
			try{
				String dblStr1 = str1.replace(",", "");
				String dblStr2 = str2.replace(",", "");

				Double dbl1 = Double.valueOf(dblStr1);
				Double dbl2 = Double.valueOf(dblStr2);
				equal = dbl1.doubleValue() == dbl2.doubleValue();
			}catch(Exception e){

			}
			if(!equal) equal = str1.equals(str2);
		}
		return equal;
	}


	public static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally {
			stream.close();
		}
	}

	public static boolean isNumeric(String text){
		boolean result = true;

		try{
			Double.parseDouble(text);
		}catch(NumberFormatException e){
			result = false;
		}
		return result;
	}
	
	public static boolean isInteger(String text){
		boolean result = true;

		try{
			Integer.valueOf(text);
		}catch(NumberFormatException e){
			result = false;
		}
		return result;
	}

	public static int parseInteger(String text, int defaultValue){
		int result = defaultValue;
		try{
			result = Integer.valueOf(text);
		}catch(NumberFormatException e){
		}
		return result;		
	}
	
	public static double parseDouble(String text, double defaultValue){
		double result = defaultValue;
		try{
			result = Double.valueOf(text);
		}catch(NumberFormatException e){
		}
		return result;		
	}

	public static boolean isMobile(String userAgent){
		boolean result = false;

		final String[] mobileSpecificSubscriptions = {
				"iPhone","iPad","Android","MIDP","Opera Mobi",
				"Opera Mini","BlackBerry","HP iPAQ","IEMobile",
				"MSIEMobile","Windows Phone","HTC","LG",
				"MOT","Nokia","Symbian","Fennec",
				"Maemo","Tear","Midori","armv",
				"Windows CE","WindowsCE","Smartphone","240x320",
				"176x220","320x320","160x160","webOS",
				"Palm","Sagem","Samsung","SGH",
				"SonyEricsson","MMP","UCWEB"};//"SIE" This makes IE10 think it is Mobile

		if(!Utils.isStringEmpty(userAgent)){

			for(int i=0; i<mobileSpecificSubscriptions.length; i++){
				if(userAgent.contains(mobileSpecificSubscriptions[i])){
					result = true;
					break;
				}
			}
		}

		return result;
	}

	public static String htmlToText(String html){
		String val = html.replace("<div><br></div>","[LINE_BREAK]");
		if(val.startsWith("<div>")){
			val = val.replaceFirst("<div>", "");
		}
		val = val.replace("<br>","[LINE_BREAK]");
		val = val.replace("\n","[LINE_BREAK]");
		val = val.replace("<div>","[LINE_BREAK]");
		val = val.replace("&nbsp;","[SPACE]");
		val = val.replace("&#160;","[SPACE]");
		val = Jsoup.parse(val).text();
		val = val.replace("[LINE_BREAK]","\n");
		val = val.replace("[SPACE]"," ");
		return val;
		
		/*
		if(html.startsWith("<div>")){
			html = html.replaceFirst("<div>", "");
		}
		html = html.replaceAll("\n", "NEW_LINE");
		html = html.replaceAll("<div>", "NEW_LINE");
		html = Jsoup.parse(html).text();
		html = html.replaceAll("NEW_LINE", "\n");
		return html;
	  */
	}
	
	public static String inputStreamToString(InputStream in) throws IOException{
		StringBuilder out = new StringBuilder();
		try{
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	    String line;
	    while ((line = reader.readLine()) != null) {
	        out.append(line);
	        out.append("\n");
	    }
		}catch(Exception e){
			e.printStackTrace();
		}
	  return out.toString();
	}
	
	public static int compareObjects_NullityOnly(Object o1, Object o2){
		int comp = 0;
		if(o1 == null && o2 != null){
			comp = -1;
		}else if(o1 != null && o2 == null){
			comp = 1;
		}
		return comp;
	}
	
	public static int compareStrings(String s1, String s2){
		int comp = 0;
		if(s1 != null && s2 == null){
			comp = 1 ;
		}else if(s1 == null && s2 != null){
			comp = -1;
		}else if(s1 != null && s2 != null){
			comp = s1.compareTo(s2);
		}
		return comp;
	}

}