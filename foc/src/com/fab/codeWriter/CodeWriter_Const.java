package com.fab.codeWriter;

import com.fab.model.table.FieldDefinition;
import com.foc.list.FocList;

public class CodeWriter_Const extends CodeWriter {

	public CodeWriter_Const(CodeWriterSet set) {
		super(set);
	}

	@Override
	public boolean hasInternalFile() {
		return true;
	}

	@Override
	public boolean hasExternalFile() {
		return true;
	}

	@Override
	public String getFileSuffix() {
		return CLASS_NAME_SUFFIX_CONSTANT;
	}

	@Override
	public boolean isServerSide() {
		return true;
	}

	@Override
	public void generateCode() {
		initFiles();
		
		CodeWriter_OneFile intWriter = getInternalFileWriter(); 
		CodeWriter_OneFile extWriter = getExternalFileWriter();
		
		//  Internal
		//  --------

		intWriter.printCore("public interface "+intWriter.getClassName()+" {\n\n");

		intWriter.printCore("  public static final String "+getConstant_TableName()+" = \""+getTblDef().getName()+"\";\n");
		
		FocList fieldList = getTblDef().getFieldDefinitionList();
		for(int i=0; i<fieldList.size(); i++){
			FieldDefinition fieldDefinition = (FieldDefinition) fieldList.getFocObject(i);
			
			intWriter.printCore("  public static final int "+fieldDefinition.getCW_FieldConstanteName()+" = "+fieldDefinition.getID()+";\n");
		}

		intWriter.printCore("\n");
		intWriter.printCore("}\n");
		intWriter.compile();
		
		//  External
		//  --------
		if(isUseAutoGenDirectory()){
			extWriter.addImport(intWriter.getPackageName()+"."+intWriter.getClassName());
			
			extWriter.printCore("public interface "+extWriter.getClassName()+" extends "+intWriter.getClassName()+" {\n");
			extWriter.printCore("}");
			
			extWriter.compile();
		}
		
		closeFiles();
	}
	
	public String getConstant_TableName(){
		return "DB_TABLE_NAME";//+getTblDef().getName();
	}
}
