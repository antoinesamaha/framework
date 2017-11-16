package com.foc.web.unitTesting;

import com.foc.ConfigInfo;
import com.foc.Globals;

public class FocUnitRecorder {
	
	private StringBuffer buffer = null;
	
	public FocUnitRecorder(){
		buffer = new StringBuffer();
	}
	
	private void recordLineInternal(String line){
		buffer.append(line);
		buffer.append("\n");
	}
	
	public static void startRecording(){
		FocUnitRecorder.getInstance(true);
	}
	
	public static void stopRecording(){
		FocUnitRecorder recorder = FocUnitRecorder.getInstance(false);
		Globals.logString(recorder.buffer.toString(), false);
		instance = null;
	}
	
	public static void recordLine(String line){
		FocUnitRecorder recorder = FocUnitRecorder.getInstance(false);
		if(recorder != null) recorder.recordLineInternal(line);
	}
	
	private static FocUnitRecorder instance = null;
	
	private static FocUnitRecorder getInstance(boolean create){
		if(instance == null && create) instance = new FocUnitRecorder();
		return instance;
	}
	
	public static boolean isAllowRecording(){
		return ConfigInfo.isUnitAllowed();
	}
}
