package com.foc.fluttercodewriter;

public class FlutterCodeWriterMain {
	
	//public String codepath = "/home/antoine/work/flutterws/central_inspection_hr/it_quiz_web/lib";
	public String codepath = "f:/git/siren/flutterlab/fenix/lib";
	
	public FlutterCodeWriterMain() {
		//generateFilesFor("news", "Incident");
		//generateFilesFor("aid", "PcmAid");
		
		generateFilesFor("incident/common", "OpCallSeverity");
	}
	
	private void generateFilesFor(String module, String objectType) {
		FlutterCodeWriter_Entity descWriter = new FlutterCodeWriter_Entity(this, module, objectType);
		descWriter.generate();
		descWriter.dispose();
		
		FlutterCodeWriter_Controler controlerWriter = new FlutterCodeWriter_Controler(this, module, objectType);
		controlerWriter.generate();
		controlerWriter.dispose();
		
		FlutterCodeWriter_Provider providerWriter = new FlutterCodeWriter_Provider(this, module, objectType);
		providerWriter.generate();
		providerWriter.dispose();
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

