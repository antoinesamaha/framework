package com.fab.codeWriter;

import com.fab.model.table.FieldDefinition;
import com.foc.list.FocList;

public class CodeWriter_GuiProxyListTable extends CodeWriter {

	public CodeWriter_GuiProxyListTable(CodeWriterSet set){
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
		return CLASS_NAME_SUFFIX_PROXY_GUI_TABLE;
	}

	@Override
	public boolean isServerSide() {
		return false;
	}

	@Override
	public void generateCode() {
		initFiles();
		
		CodeWriter_ProxyList cw_List = getCodeWriterSet().getCodeWriter_ProxyList();
		
		CodeWriter_OneFile intWriter = getInternalFileWriter(); 
		CodeWriter_OneFile extWriter = getExternalFileWriter();
		
		//  Internal
		//  --------
		
		intWriter.addImport("b01.focGWT.client.gui.components.FwGuiTable_ForFwList");
		
		intWriter.printCore("@SuppressWarnings(\"serial\")\n");
		intWriter.printCore("public class "+intWriter.getClassName()+" extends FwGuiTable_ForFwList {\n\n");
		
		intWriter.printCore("  public "+intWriter.getClassName()+"(){\n");
		intWriter.printCore("    super();\n");
		intWriter.printCore("  }\n\n");

		intWriter.printCore("}");
		intWriter.compile();
		
		//  External
		//  --------
		
		//extWriter.addImport("b01.foc.desc.FocConstructor");
		extWriter.addImport(intWriter.getPackageName()+"."+intWriter.getClassName());
		
		extWriter.printCore("@SuppressWarnings(\"serial\")\n");
		extWriter.printCore("public class "+extWriter.getClassName()+" extends "+intWriter.getClassName()+" {\n");

		extWriter.printCore("  public "+extWriter.getClassName()+"("+cw_List.getClassName()+" list){\n");
		extWriter.printCore("    super();\n");

		FocList fieldList = getTblDef().getFieldDefinitionList();
		for(int i=0; i<fieldList.size(); i++){
			FieldDefinition fieldDefinition = (FieldDefinition) fieldList.getFocObject(i);
		
			if(			fieldDefinition.getSQLType() != FieldDefinition.SQL_TYPE_ID_LIST_FIELD
					&& 	fieldDefinition.getSQLType() != FieldDefinition.SQL_TYPE_ID_OBJECT_FIELD){
				extWriter.printCore("    addColumn(\""+fieldDefinition.getName()+"\", \""+fieldDefinition.getTitle()+"\");\n");
			}
			
		}
		
		extWriter.printCore("    setList(list);\n");
		extWriter.printCore("  }\n\n");
		
		extWriter.printCore("}");
		extWriter.compile();
		
		closeFiles();
	}
}
