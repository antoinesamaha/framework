package com.foc.desc.dataModelTree;

import java.sql.Types;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.event.FValidationListener;
import com.foc.gui.FGOptionPane;
import com.foc.gui.FGTabbedPane;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class DataModelGuiPanel_ForReporting extends FPanel {
	
	private FGTabbedPane      tabbedPane    = null;
	private DataModelNodeList parameterList = null;
	private DataModelNodeList fieldList     = null;
	
	public DataModelGuiPanel_ForReporting(){
		setFill(FILL_BOTH);
		setMainPanelSising(FILL_BOTH);
		tabbedPane = new FGTabbedPane();
		add(tabbedPane, 0, 0);
		FValidationPanel vPanel = showValidationPanel(true);
		vPanel.setValidationListener(new FValidationListener() {
			@Override
			public boolean proceedValidation(FValidationPanel panel) {
				StringBuffer strBuffer = new StringBuffer();
				addListToBuffer(strBuffer, fieldList, false);
				addListToBuffer(strBuffer, parameterList, true);
				Globals.logString(strBuffer.toString());
				boolean exit = !FGOptionPane.popupOptionPane_YesNo("Copy Past in iReport XML view", strBuffer.toString());
				return exit;
			}
			
			@Override
			public boolean proceedCancelation(FValidationPanel panel) {
				return true;
			}
			
			@Override
			public void postValidation(FValidationPanel panel) {
			}
			
			@Override
			public void postCancelation(FValidationPanel panel) {
			}
		});
	}
	
	public void dispose(){
		super.dispose();
		tabbedPane = null;
	}
	
	public void setParameterDictionaryFocDesc(FocDesc parameterDictionaryFocDesc, int maxLevel){
		parameterList = new DataModelNodeList(parameterDictionaryFocDesc, maxLevel);
		DataModelNodeGuiTreePanel treePanel = new DataModelNodeGuiTreePanel(parameterList, DataModelNodeGuiTreePanel.VIEW_FOR_REPORT_GENERATION);
		
		tabbedPane.add("Parameters", treePanel);
	}

	public void setFieldDictionaryFocDesc(FocDesc fieldDictionaryFocDesc, int maxLevel){
		fieldList = new DataModelNodeList(fieldDictionaryFocDesc, maxLevel);
		DataModelNodeGuiTreePanel treePanel = new DataModelNodeGuiTreePanel(fieldList, DataModelNodeGuiTreePanel.VIEW_FOR_REPORT_GENERATION);
		
		tabbedPane.add("Fields", treePanel);
	}
	
	public void addListToBuffer(StringBuffer strBuffer, DataModelNodeList fieldList, boolean isParameter){
		for(int i=0; fieldList != null && i<fieldList.size(); i++){
			DataModelNode dataNode = (DataModelNode) fieldList.getFocObject(i);
			if(dataNode.isSelected()){
				String  name    = dataNode.getFullPath();
				String  clsname = "";
				//FocDesc focDesc = dataNode.getFather() != null ? dataNode.getFather().getFocDesc() : null;
				
				FField fld = dataNode.getFField();
				if(fld != null){
					switch(fld.getSqlType()){
					case Types.DATE:
						clsname = "java.lang.String";
						break;
					case Types.VARCHAR:
						clsname = "java.lang.String";
						break;
					case Types.DOUBLE:
						clsname = "java.lang.Number";
						break;
					case Types.INTEGER:
						clsname = "java.lang.Integer";
						break;
					}
				}
	
				if(isParameter){
					strBuffer.append("<parameter name=\""+name+"\" class=\""+clsname+"\" isForPrompting=\"false\"/>");
				}else{
					strBuffer.append("<field name=\""+name+"\" class=\""+clsname+"\"/>");
				}
				strBuffer.append("\n");
			}
		}
	}
}
