package com.fab.model.fieldFactory;

import com.fab.codeWriter.CodeWriter;
import com.fab.codeWriter.CodeWriter_OneFile;
import com.fab.model.table.FieldDefinition;

public class FDChar extends FDAbstract {

	@Override
	public String getJavaType(FieldDefinition fldDef) {
		return "String";
	}
	
	@Override
	public String getGWTJavaType(FieldDefinition fldDef, boolean fullPath) {
		return getJavaType(fldDef);
	}
	
	@Override
	public String getGWTJavaType_ObjectNotNative(FieldDefinition fldDef, boolean fullPath) {
		return getGWTJavaType(fldDef, fullPath);
	}
	
	@Override
	public String getFocPropertyMethodPartialName(FieldDefinition fldDef){
		return "String";
	}
	
	@Override
	public String getDefaultValue(FieldDefinition fldDef) {
		return "null";
	}

	@Override
	public void addFieldDeclarationInFocDesc(CodeWriter codeWriter, FieldDefinition fldDef) {
		CodeWriter_OneFile intWrt = codeWriter.getInternalFileWriter();
		
		String varName = getFieldVariableName(fldDef);
		intWrt.addImport("b01.foc.desc.field.FCharField");
		intWrt.printCore("    FCharField "+varName+" = new FCharField(\""+fldDef.getName()+"\", \""+fldDef.getTitle()+"\", "+fldDef.getCW_FieldConstanteName()+", "+fldDef.isKey()+", "+fldDef.getLength()+");\n");
		addFieldDeclarationInFocDesc_Common(codeWriter, fldDef, varName);
	}

	@Override
	public String getClassFor_GuiComponent_Details(){
		return "FwTextBox";
	}

}
