package com.fab.model.fieldFactory;

import com.fab.codeWriter.CodeWriter;
import com.fab.codeWriter.CodeWriter_OneFile;
import com.fab.model.table.FieldDefinition;

public class FDBoolean extends FDAbstract {

	@Override
	public String getJavaType(FieldDefinition fldDef) {
		return "boolean";
	}
	
	@Override
	public String getGWTJavaType(FieldDefinition fldDef, boolean fullPath) {
		return "boolean";
	}
	
	@Override
	public String getGWTJavaType_ObjectNotNative(FieldDefinition fldDef, boolean fullPath) {
		return "Boolean";
	}

	@Override
	public String getFocPropertyMethodPartialName(FieldDefinition fldDef){
		return "Boolean";
	}

	@Override
	public String getDefaultValue(FieldDefinition fldDef) {
		return "false";
	}
	
	@Override
	public void addFieldDeclarationInFocDesc(CodeWriter codeWriter, FieldDefinition fldDef) {
		CodeWriter_OneFile intWrt = codeWriter.getInternalFileWriter();
		
		String varName = getFieldVariableName(fldDef);
		intWrt.addImport("b01.foc.desc.field.FBoolField");
		intWrt.printCore("    FBoolField "+varName+" = new FBoolField(\""+fldDef.getName()+"\", \""+fldDef.getTitle()+"\", "+fldDef.getCW_FieldConstanteName()+", "+fldDef.isKey()+");\n");
		addFieldDeclarationInFocDesc_Common(codeWriter, fldDef, varName);
	}

	@Override
	public String getConvertString2ValueFunction(){
		return "convertString2Boolean";
	}
	
	@Override
	public String getConvertValue2StringFunction(){
		return "convertBoolean2String";
	}

	@Override
	public String getClassFor_GuiComponent_Details(){
		return "FwCheckBox";
	}
}
