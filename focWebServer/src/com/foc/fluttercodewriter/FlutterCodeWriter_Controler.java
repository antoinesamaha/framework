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
		try {
			String entityname = getFocDesc().getStorageName();
			String controler_auto = entityname+"Controler_AutoGen";
			PrintStream outPrintStream = newFileInAutoGen(controler_auto);
			
			outPrintStream.println("import 'package:kite/config.dart';");
			outPrintStream.println("import 'package:kite/provider/KiteControler.dart';");
			outPrintStream.println("import 'package:kite/entity/Kite.dart';");
			outPrintStream.println("");
			outPrintStream.println("class " + controler_auto + " extends MappedKite {");
			outPrintStream.println("");
			outPrintStream.println("  String _name;");
			outPrintStream.println("");
			outPrintStream.println("  String get name => _name;");
			outPrintStream.println("  set name(String value) {");
			outPrintStream.println("    _name = value;");
			outPrintStream.println("  }");
			outPrintStream.println("");
			outPrintStream.println("  "+controler_auto+"();");
			outPrintStream.println("");
			outPrintStream.println("  @override");
			outPrintStream.println("  fromJson(Map<String, dynamic> json) {");
			outPrintStream.println("    super.fromJson(json);");
			outPrintStream.println("    name = json['name'];");
			outPrintStream.println("  }");
			outPrintStream.println("");
			outPrintStream.println("}");
			outPrintStream.println("");
			outPrintStream.println("class "+entityname+"Controler extends KiteControler<"+entityname+"> {");
			outPrintStream.println("  "+entityname+"Controler() : super(new KiteConfig().serverUrl, \""+entityname+"\");");
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

}