package com.fab.model.fieldFactory;

import com.fab.codeWriter.CodeWriter;
import com.fab.model.table.FieldDefinition;

public interface IFieldType {
	public void addFieldDeclarationInFocDesc(CodeWriter codeWriter, FieldDefinition fldDef);
	public void addGetterSetterInFocObject(CodeWriter codeWriter, FieldDefinition fldDef);
	public void addFieldDeclarationInPojo(CodeWriter codeWriter, FieldDefinition fldDef);
	public void addGetterSetterInPojo(CodeWriter codeWriter, FieldDefinition fldDef);
	
	/*
	public void addDeclarationInFocObjectWebClient(CodeWriter codeWriter, FieldDefinition fldDef);
	public void addGetterSetterInWebFocObjectClient(CodeWriter codeWriter, FieldDefinition fldDef);
	*/
	
	public String getFocPropertyMethodPartialName(FieldDefinition fldDef);
	//public String getFocFieldType(FieldDefinition fldDef);
	
	public String getJavaType(FieldDefinition fldDef);
	public String getGWTJavaType(FieldDefinition fldDef, boolean fullPath);
	public String getGWTJavaType_ObjectNotNative(FieldDefinition fldDef, boolean fullPath);
	public String getDefaultValue(FieldDefinition fldDef);
}
