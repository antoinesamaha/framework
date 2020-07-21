package com.foc.fluttercodewriter;

import com.foc.Globals;
import com.foc.desc.FocDesc;

public class FlutterCodeWriterMain {
	
	public String codepath = "/home/antoine/work/flutterws/central_inspection_hr/it_quiz_web/lib";
	
	public FlutterCodeWriterMain() {
		FlutterCodeWriter_Entity descWriter = new FlutterCodeWriter_Entity(this, "news", "Incident");
		descWriter.generate();
		descWriter.dispose();
		
		FlutterCodeWriter_Controler controlerWriter = new FlutterCodeWriter_Controler(this, "news", "Incident");
		controlerWriter.generate();
		controlerWriter.dispose();
	}

	public void dispose() {
		
	}

	public String getCodepath() {
		return codepath;
	}

	public void setCodepath(String codepath) {
		this.codepath = codepath;
	}
		
}

