package com.fab.model.fieldFactory;

import com.fab.codeWriter.CodeWriter;
import com.fab.codeWriter.CodeWriter_OneFile;
import com.fab.model.table.FabMultiChoiceSet;
import com.fab.model.table.FabMultipleChoice;
import com.fab.model.table.FieldDefinition;

public class FDMultipleChoice extends FDAbstract {

	@Override
	public String getJavaType(FieldDefinition fldDef) {
		return "int";
	}

	@Override
	public String getGWTJavaType(FieldDefinition fldDef, boolean fullPath) {
		return "Integer";
	}
	
	@Override
	public String getGWTJavaType_ObjectNotNative(FieldDefinition fldDef, boolean fullPath) {
		return getGWTJavaType(fldDef, fullPath);
	}
	
	@Override
	public String getFocPropertyMethodPartialName(FieldDefinition fldDef) {
		return "MultiChoice";
	}
	
	@Override
	public String getDefaultValue(FieldDefinition fldDef) {
		return "0";
	}
	
	@Override
	public void addFieldDeclarationInFocDesc(CodeWriter codeWriter, FieldDefinition fldDef) {
		CodeWriter_OneFile intWrt = codeWriter.getInternalFileWriter();
		
		String varName = getFieldVariableName(fldDef);
		intWrt.addImport("b01.foc.desc.field.FMultipleChoiceField");
		intWrt.printCore("    FMultipleChoiceField "+varName+" = new FMultipleChoiceField(\""+fldDef.getName()+"\", \""+fldDef.getTitle()+"\", "+fldDef.getCW_FieldConstanteName()+", "+fldDef.isKey()+", "+fldDef.getLength()+");\n");
		FabMultiChoiceSet mcSet = fldDef.getMultiChoiceSet();
		if(mcSet != null && mcSet.getMultipleChoiceList() != null){
			for(int i=0; i<mcSet.getMultipleChoiceList().size(); i++){
				FabMultipleChoice choice = (FabMultipleChoice) mcSet.getMultipleChoiceList().getFocObject(i);
				intWrt.printCore("    "+varName+".addChoice("+choice.getIntValue()+", \""+choice.getDisplayText()+"\");\n");
			}
		}
		addFieldDeclarationInFocDesc_Common(codeWriter, fldDef, varName);
	}
}
