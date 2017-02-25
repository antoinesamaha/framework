package com.fab.model.fieldFactory;

import com.fab.codeWriter.CodeWriter;
import com.fab.codeWriter.CodeWriter_OneFile;
import com.fab.model.table.FieldDefinition;

public class FDDouble extends FDAbstract {

	@Override
	public String getJavaType(FieldDefinition fldDef) {
		return "double";
	}
	
	@Override
	public String getGWTJavaType(FieldDefinition fldDef, boolean fullPath) {
		return "double";
	}
	
	@Override
	public String getGWTJavaType_ObjectNotNative(FieldDefinition fldDef, boolean fullPath) {
		return "Double";
	}

	public String getFocPropertyMethodPartialName(FieldDefinition fldDef){
		return "Double";
	}
	
	@Override
	public String getDefaultValue(FieldDefinition fldDef) {
		return "0";
	}

	@Override
	public void addFieldDeclarationInFocDesc(CodeWriter codeWriter, FieldDefinition fldDef) {
		CodeWriter_OneFile intWrt = codeWriter.getInternalFileWriter();
		
		String varName = getFieldVariableName(fldDef);
		intWrt.addImport("b01.foc.desc.field.FNumField");
		intWrt.printCore("    FNumField "+varName+" = new FNumField(\""+fldDef.getName()+"\", \""+fldDef.getTitle()+"\", "+fldDef.getCW_FieldConstanteName()+", "+fldDef.isKey()+", "+fldDef.getLength()+", "+fldDef.getDecimals()+");\n");
		addFieldDeclarationInFocDesc_Common(codeWriter, fldDef, varName);
	}
	
	@Override
	public String getClassFor_GuiComponent_Details(){
		return "FwDoubleBox";
	}
}
