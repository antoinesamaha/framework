package com.foc.util;

import java.io.IOException;
import java.util.ArrayList;

import com.foc.Globals;

public class EMail {

	public static boolean sendMail(String recepient, String attachment){
		boolean error = false;
		String commanddLine = Globals.getApp().getUser().getSendEmailCommandLine();
		
		ArrayList<String> argArray = new ArrayList<String>();
		argArray.add(commanddLine);
		argArray.add("/c");
		argArray.add("ipm.note");
		if(recepient != null && !recepient.isEmpty()){
			argArray.add("/m");
			argArray.add(recepient);
		}
		if(attachment != null && !attachment.isEmpty()){
			argArray.add("/a");
			argArray.add(attachment);
		}
		
		String[] args = new String[argArray.size()];
		for(int i=0; i<argArray.size(); i++){
			args[i] = argArray.get(i);
		}
		
		try{
			Runtime.getRuntime().exec(args);
		}catch (IOException e){
			error = true;
			Globals.logException(e);
		}
		return error;
	}
	
}
