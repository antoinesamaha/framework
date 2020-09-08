package com.foc.fluttercodewriter;

import java.io.File;
import java.io.PrintStream;

import com.foc.Globals;
import com.foc.desc.FocDesc;

public abstract class FlutterCodeWriter_Abstract {

	public abstract void generate();
	
	private FlutterCodeWriterMain main    = null;
	private String                module  = null;
	private FocDesc               focDesc = null;
	
	public FlutterCodeWriter_Abstract(FlutterCodeWriterMain main, String module, String tableName) {
		this.main    = main;
		this.focDesc = Globals.getApp().getFocDescByName(tableName);
		this.module  = module;
	}

	public void dispose() {
		main = null;
		focDesc = null;
	}

	public PrintStream newFileInAutoGen(String filename) throws Exception {
		String      kiteClassName  = focDesc.getStorageName();
		String      fullname       = main.getCodepath() + "/modules/" + module + "/autogen/"+filename + ".dart";
		PrintStream outPrintStream = new PrintStream(fullname, "UTF-8");

		return outPrintStream;
	}

	public PrintStream newFileInMain(String filename) throws Exception {
		String      kiteClassName  = focDesc.getStorageName();
		String      fullname       = main.getCodepath() + "/modules/" + module + "/"+filename + ".dart";
		
		PrintStream outPrintStream = null; 
				
		File file = new File(fullname);
		if(!file.exists()) {
			outPrintStream = new PrintStream(fullname, "UTF-8");
		}

		return outPrintStream;
	}

	public FocDesc getFocDesc() {
		return focDesc;
	}
}
