package com.foc.fluttercodewriter;

import java.io.PrintStream;

import com.foc.Globals;

public class FlutterCodeWriter_Controler extends FlutterCodeWriter_Abstract {

	public FlutterCodeWriter_Controler(FlutterCodeWriterMain main, String module, String tablename) {
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
			String controler_auto = entityname+"_Controler_AutoGen";
			PrintStream outPrintStream = newFileInAutoGen(controler_auto);
			
			outPrintStream.println("import 'package:kite/config.dart';");
			outPrintStream.println("import 'package:kite/provider/KiteControler.dart';");
			outPrintStream.println("");
			outPrintStream.println("import '../" + entityname + ".dart';");
			outPrintStream.println("");
			outPrintStream.println("class " + controler_auto + " extends KiteControler<" + entityname + "> {");
			outPrintStream.println("  "+entityname+"_Controler_AutoGen() : super(new KiteConfig().serverUrl, \""+entityname+"\");");
			outPrintStream.println("");
			
			outPrintStream.println("  @override");
			outPrintStream.println("  "+entityname+" newKiteEntity() {");
			outPrintStream.println("    return "+entityname+"();");
			outPrintStream.println("  }");
			outPrintStream.println("");
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
			PrintStream outPrintStream = newFileInMain(entityname+"_Controler");
			
			if (outPrintStream != null) {
				outPrintStream.println("import 'autogen/" + entityname + "_Controler_AutoGen.dart';");
				outPrintStream.println("");
				outPrintStream.println("class " + entityname + "_Controler extends " + entityname + "_Controler_AutoGen {");
				outPrintStream.println("");
				outPrintStream.println("}");
				
				outPrintStream.close();
			}
		} catch (Exception e) {
			Globals.logException(e);
		}
	}

}
