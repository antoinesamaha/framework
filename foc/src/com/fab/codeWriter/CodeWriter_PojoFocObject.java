package com.fab.codeWriter;

import com.fab.model.fieldFactory.FieldFactory;
import com.fab.model.table.FieldDefinition;
import com.foc.list.FocList;

public class CodeWriter_PojoFocObject extends CodeWriter {

	public CodeWriter_PojoFocObject(CodeWriterSet set){
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
		return "";
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

		CodeWriter_Const cw_Const = getCodeWriterSet().getCodeWriter_Const();
		
		//-----------------------------------
		intWriter.addImport("com.foc.annotations.model.FocChoice");
		intWriter.addImport("com.foc.annotations.model.FocEntity");
    intWriter.addImport("com.foc.annotations.model.FocField");
		intWriter.addImport("com.foc.desc.FocConstructor");
		intWriter.addImport("com.foc.desc.pojo.PojoFocObject");
		intWriter.addImport("com.foc.desc.pojo.PojoFocDesc");
		if(isUseAutoGenDirectory()) intWriter.addImport(extWriter.getPackageName()+".*");
//		intWriter.addImport(extWriter.getPackageName()+"."+getClassName_FocObject());
//		intWriter.addImport(extWriter.getPackageName()+"."+getClassName_FocObject()+CLASS_NAME_SUFFIX_CONSTANT);
//		intWriter.addImport(extWriter.getPackageName()+"."+getClassName_FocObject()+CLASS_NAME_SUFFIX_FOC_DESC);
		
		intWriter.printCore("@FocEntity()\n");
		intWriter.printCore("public class "+intWriter.getClassName()+" extends PojoFocObject {\n\n");
		
		intWriter.printCore("  public static final String DBNAME = \""+intWriter.getClassName()+"\";\n\n");
		
		intWriter.printCore("  public "+intWriter.getClassName()+"(FocConstructor constr){\n");
		
		intWriter.printCore("    super(constr);\n");
		intWriter.printCore("  }\n\n");

		FocList fieldList = getTblDef().getFieldDefinitionList();
		for(int i=0; i<fieldList.size(); i++){
			FieldDefinition fieldDefinition = (FieldDefinition) fieldList.getFocObject(i);
			FieldFactory.getInstance().addFieldDeclarationInPojo(this, fieldDefinition);
		}

		intWriter.printCore("\n");
		
		fieldList = getTblDef().getFieldDefinitionList();
		for(int i=0; i<fieldList.size(); i++){
			FieldDefinition fieldDefinition = (FieldDefinition) fieldList.getFocObject(i);
			FieldFactory.getInstance().addFieldGetterSetterInPojo(this, fieldDefinition);
		}

		intWriter.printCore("  public static PojoFocDesc getFocDesc() {\n");
	  intWriter.printCore("  	return PojoFocDesc.getInstance(DBNAME);\n");
	  intWriter.printCore("  }\n");
		
		intWriter.printCore("}\n");
		intWriter.compile();
		
		//-----------------------------------
		if(isUseAutoGenDirectory()){
			extWriter.addImport(intWriter.getPackageName()+"."+intWriter.getClassName());
	
			extWriter.printCore("public class "+extWriter.getClassName()+" extends "+intWriter.getClassName()+" {\n");
			
			extWriter.printCore("}\n");
			extWriter.compile();
		}
		
		closeFiles();
	}
}
