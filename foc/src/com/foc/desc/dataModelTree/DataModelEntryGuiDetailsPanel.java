package com.foc.desc.dataModelTree;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.event.FValidationListener;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class DataModelEntryGuiDetailsPanel extends FPanel{

	DataModelEntry entry = null;
	
	public DataModelEntryGuiDetailsPanel(FocObject obj, int viewID){
		entry = (DataModelEntry) obj;
		if(entry != null){
			add(entry, DataModelEntryDesc.FLD_FOC_DESC , 0, 0);
			add(entry, DataModelEntryDesc.FLD_MAX_LEVEL, 0, 1);
		}
		FValidationPanel validPanel = showValidationPanel(true);
		validPanel.setValidationListener(new FValidationListener(){
			public void postCancelation(FValidationPanel panel) {
				
			}

			public void postValidation(FValidationPanel panel) {
				FocDesc rootDesc = entry.getPropertyDesc(DataModelEntryDesc.FLD_FOC_DESC);
				int     maxLevel = entry.getPropertyInteger(DataModelEntryDesc.FLD_MAX_LEVEL);
				DataModelNodeList list = new DataModelNodeList(rootDesc, maxLevel);
				DataModelNodeGuiTreePanel treePanel = new DataModelNodeGuiTreePanel(list, FocObject.DEFAULT_VIEW_ID);
				Globals.getDisplayManager().popupDialog(treePanel, "Data Dictionay Tree", true);
			}

			public boolean proceedCancelation(FValidationPanel panel) {
				return true;
			}

			public boolean proceedValidation(FValidationPanel panel) {
				return true;
			}
		});
	}
}
