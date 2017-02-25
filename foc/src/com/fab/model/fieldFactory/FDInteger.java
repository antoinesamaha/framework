package com.fab.model.fieldFactory;

import com.fab.codeWriter.CodeWriter;
import com.fab.codeWriter.CodeWriter_OneFile;
import com.fab.model.table.FieldDefinition;

public class FDInteger extends FDAbstract {

	@Override
	public String getJavaType(FieldDefinition fldDef) {
		return "int";
	}

	@Override
	public String getGWTJavaType(FieldDefinition fldDef, boolean fullPath) {
		return "int";
	}
	
	@Override
	public String getGWTJavaType_ObjectNotNative(FieldDefinition fldDef, boolean fullPath) {
		return "Integer";
	}
	
	@Override
	public String getFocPropertyMethodPartialName(FieldDefinition fldDef) {
		return "Integer";
	}

	@Override
	public String getDefaultValue(FieldDefinition fldDef) {
		return "0";
	}

	@Override
	public void addFieldDeclarationInFocDesc(CodeWriter codeWriter, FieldDefinition fldDef) {
		CodeWriter_OneFile intWrt = codeWriter.getInternalFileWriter();
		
		String varName = getFieldVariableName(fldDef);
		intWrt.addImport("b01.foc.desc.field.FIntField");
		intWrt.printCore("    FIntField "+varName+" = new FIntField(\""+fldDef.getName()+"\", \""+fldDef.getTitle()+"\", "+fldDef.getCW_FieldConstanteName()+", "+fldDef.isKey()+", "+fldDef.getLength()+");\n");
		addFieldDeclarationInFocDesc_Common(codeWriter, fldDef, varName);
	}

	@Override
	public String getConvertString2ValueFunction(){
		return "convertString2Int";
	}
	
	@Override
	public String getConvertValue2StringFunction(){
		return "convertInt2String";
	}

	@Override
	public String getClassFor_GuiComponent_Details(){
		return "FwIntBox";
	}
}
