package com.foc.fluttercodewriter;

import java.io.PrintStream;

import com.foc.Globals;

public class FlutterCodeWriter_Provider extends FlutterCodeWriter_Abstract {

	public FlutterCodeWriter_Provider(FlutterCodeWriterMain main, String module, String tablename) {
		super(main, module, tablename);
	}

	public void dispose() {
		super.dispose();
	}

	public void generate() {
		generate_AutoGenFile();
		generate_MainFile();
	}

	public void generate_AutoGenFile() {
		try {
			String entityname = getFocDesc().getStorageName();
			String controler_auto = entityname+"_Provider_AutoGen";
			PrintStream outPrintStream = newFileInAutoGen(controler_auto);
			
			outPrintStream.println("import 'package:kite/provider/KiteProvider.dart';");
			outPrintStream.println("");
			
			outPrintStream.println("import '../" + entityname + ".dart';");
			outPrintStream.println("import '../" + entityname + "_Controler.dart';");
			outPrintStream.println("");
			
			outPrintStream.println("class " + controler_auto + " extends KiteProvider<" + entityname + ", " + entityname + "_Controler> {");
			outPrintStream.println("  " + entityname + "_Provider_AutoGen() : super( " + entityname + "_Controler());");
			outPrintStream.println("}");
			
			outPrintStream.close();
		} catch (Exception e) {
			Globals.logException(e);
		}
	}

	public void generate_MainFile() {
		try {
			String entityname = getFocDesc().getStorageName();
			String entityname_auto = entityname+"_AutoGen";
			PrintStream outPrintStream = newFileInMain(entityname+"_Provider");
			
			if (outPrintStream != null) {
				outPrintStream.println("import 'autogen/" + entityname + "_Provider_AutoGen.dart';");
				outPrintStream.println("");
				outPrintStream.println("class " + entityname + "_Provider extends " + entityname + "_Provider_AutoGen {");
				outPrintStream.println("");
				outPrintStream.println("}");
				
				outPrintStream.close();
			}
		} catch (Exception e) {
			Globals.logException(e);
		}
	}

}
