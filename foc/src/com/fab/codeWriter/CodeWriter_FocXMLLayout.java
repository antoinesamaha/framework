package com.fab.codeWriter;

import com.fab.model.fieldFactory.FieldFactory;
import com.fab.model.table.FieldDefinition;
import com.foc.list.FocList;

public class CodeWriter_FocXMLLayout extends CodeWriter {

	public CodeWriter_FocXMLLayout(CodeWriterSet set){
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
		
		//  Internal
		//  --------
		
		intWriter.addImport("b01.foc.desc.FocConstructor");
		intWriter.addImport("b01.foc.desc.FocObject");
		intWriter.addImport(extWriter.getPackageName()+"."+extWriter.getClassName()+CodeWriter.CLASS_NAME_SUFFIX_CONSTANT);
		
		intWriter.printCore("public class "+intWriter.getClassName()+" extends FocObject implements "+extWriter.getClassName()+CodeWriter.CLASS_NAME_SUFFIX_CONSTANT+ " {\n\n");
		intWriter.printCore("  public "+intWriter.getClassName()+"(FocConstructor constr){\n");
		
		intWriter.printCore("    super(constr);\n");
		intWriter.printCore("    newFocProperties();\n");
		intWriter.printCore("  }\n\n");

		FocList fieldList = getTblDef().getFieldDefinitionList();
		for(int i=0; i<fieldList.size(); i++){
			FieldDefinition fieldDefinition = (FieldDefinition) fieldList.getFocObject(i);
			FieldFactory.getInstance().addGetterSetterInFocObject(this, fieldDefinition);
		}
		
	  if(getTblDef().isSingleInstance()){
	  	intWriter.addImport("b01.foc.list.FocList");
	  	intWriter.addImport(extWriter.getPackageName()+"."+extWriter.getClassName());
	  	intWriter.addImport(extWriter.getPackageName()+"."+extWriter.getClassName()+CodeWriter.CLASS_NAME_SUFFIX_SERVICE_INSTANCE);

	  	intWriter.printCore("  private static "+extWriter.getClassName()+" instance = null;\n");
			intWriter.printCore("  public static "+extWriter.getClassName()+" getInstance(){\n");
			intWriter.printCore("    if(instance == null){\n");
			intWriter.printCore("    	 FocList list = "+extWriter.getClassName()+CodeWriter.CLASS_NAME_SUFFIX_SERVICE_INSTANCE+".getInstance().getFocList(true);\n");
			intWriter.printCore("    	 if(list.size() == 0){\n");
			intWriter.printCore("    	   instance = ("+extWriter.getClassName()+") list.newEmptyItem();\n");
			intWriter.printCore("    	   list.add(instance);\n");
			intWriter.printCore("    	   instance.validate(true);\n");
			intWriter.printCore("    	 }else{\n");
			intWriter.printCore("    	   instance = ("+extWriter.getClassName()+") list.getFocObject(0);\n");
			intWriter.printCore("    	 }\n");
			intWriter.printCore("    }\n");
			intWriter.printCore("    return instance;\n");
			intWriter.printCore("  }\n");
	  }
		
		intWriter.printCore("}\n");
		intWriter.compile();
		
		//  External
		//  --------
		
		extWriter.addImport("b01.foc.desc.FocConstructor");
		extWriter.addImport(intWriter.getPackageName()+"."+intWriter.getClassName());
		
		extWriter.printCore("public class "+extWriter.getClassName()+" extends "+intWriter.getClassName()+" {\n");
		
		extWriter.printCore("  public "+extWriter.getClassName()+"(FocConstructor constr){\n");
		extWriter.printCore("    super(constr);\n");
		extWriter.printCore("  }\n");
				
		extWriter.printCore("}");
		extWriter.compile();
		
		closeFiles();
	}
}
