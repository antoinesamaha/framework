package com.foc.web.unitTesting;

import com.foc.ConfigInfo;
import com.foc.Globals;

public class FocUnitRecorder {
	
	private StringBuffer buffer = null;
	private boolean      pause  = false; 
	
	public FocUnitRecorder(){
		buffer = new StringBuffer();
	}
	
	private boolean isPause() {
		return pause;
	}

	private void setPause(boolean pause) {
		this.pause = pause;
	}
	
	private void recordLineInternal(String line){
		if(!isPause()) {
			buffer.append(line);
			buffer.append("\n");
		}
	}

	public static boolean isRecording(){
		return FocUnitRecorder.getInstance(false) != null;
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
	
	public static FocUnitRecorder getInstance(boolean create){
		if(instance == null && create) instance = new FocUnitRecorder();
		return instance;
	}
	
	public static boolean isAllowRecording(){
		return ConfigInfo.isUnitAllowed();
	}
	
	public static boolean pause(){
		boolean wasPaused = false;
		FocUnitRecorder recorder = FocUnitRecorder.getInstance(false);
		if(recorder != null) {
			wasPaused = recorder.isPause();
			recorder.setPause(true);
		}
		return wasPaused;
	}
	
	public static void resume(boolean wasPaused){
		FocUnitRecorder recorder = FocUnitRecorder.getInstance(false);
		if(recorder != null) {
			recorder.setPause(wasPaused);
		}
	}

}
