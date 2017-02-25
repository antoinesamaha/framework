package com.fab.codeWriter;

import com.fab.model.fieldFactory.FieldFactory;
import com.fab.model.table.FieldDefinition;
import com.foc.list.FocList;

public class CodeWriter_FocObjectClt extends CodeWriter {

	public CodeWriter_FocObjectClt(CodeWriterSet set){
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
		return CLASS_NAME_SUFFIX_WEB_CLIENT;
	}

	@Override
	public boolean isServerSide() {
		return false;
	}

	@Override
	public void generateCode() {
		initFiles();
		
		CodeWriter_OneFile intWriter = getInternalFileWriter(); 
		CodeWriter_OneFile extWriter = getExternalFileWriter();
		
		//  Internal
		//  --------
		
		intWriter.addImport("b01.focGWT.client.proxy.ProxyObject");
		//intWriter.addImport(extWriter.getPackageName()+"."+extWriter.getClassName()+CodeWriter.CLASS_NAME_SUFFIX_CONSTANT);
		
		intWriter.printCore("public class "+intWriter.getClassName()+" extends GwfObject {\n\n");
		intWriter.printCore("  private static final long serialVersionUID = 1L;\n\n");
		
//		intWriter.printCore("  public "+intWriter.getClassName()+"(){\n");
//		intWriter.printCore("  }\n\n");

		//Variable declaration
		if(getTblDef().isWithReference()){
			intWriter.printCore("  private int reference = 0;\n");
		}
		FocList fieldList = getTblDef().getFieldDefinitionList();
		for(int i=0; i<fieldList.size(); i++){
			FieldDefinition fieldDefinition = (FieldDefinition) fieldList.getFocObject(i);
			FieldFactory.getInstance().addDeclarationInFocObjectWebClient(this, fieldDefinition);
		}

		intWriter.printCore("\n");

		//Getter Setter
		{
			//Reference Getter Setter 
			if(getTblDef().isWithReference()){
				intWriter.printCore("  public int getReference(){\n");
				intWriter.printCore("    return reference;\n");
				intWriter.printCore("  }\n\n");
				
				intWriter.printCore("  public void setReference(int reference){\n");
				intWriter.printCore("    this.reference = reference;\n");
				intWriter.printCore("  }\n\n");
			}
			//Other Getter Setter		
			for(int i=0; i<fieldList.size(); i++){
				FieldDefinition fieldDefinition = (FieldDefinition) fieldList.getFocObject(i);
				FieldFactory.getInstance().addGetterSetterInWebFocObjectClient(this, fieldDefinition);
			}
		}
		
		//End of the class
		intWriter.printCore("}\n");
		intWriter.compile();
		
		//  External
		//  --------
		
		//extWriter.addImport("b01.foc.desc.FocConstructor");
		extWriter.addImport(intWriter.getPackageName()+"."+intWriter.getClassName());
		
		extWriter.printCore("public class "+extWriter.getClassName()+" extends "+intWriter.getClassName()+" {\n");
		
//		extWriter.printCore("  public "+extWriter.getClassName()+"(FocConstructor constr){\n");
//		extWriter.printCore("    super(constr);\n");
//		extWriter.printCore("  }\n");
		
		extWriter.printCore("}");
		extWriter.compile();
		
		closeFiles();
	}
}
