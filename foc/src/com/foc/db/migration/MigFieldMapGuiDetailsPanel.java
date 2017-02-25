package com.foc.db.migration;

import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class MigFieldMapGuiDetailsPanel extends FPanel{

	public final static int VIEW_SELECTION = 2; 
	
	public MigFieldMapGuiDetailsPanel(FocObject obj, int viewID){
		if(viewID == VIEW_SELECTION){
			setInsets(0, 0, 0, 0);
			FGTextField comp = (FGTextField) obj.getGuiComponent(FField.FLD_NAME);
			comp.setEditable(false);
			add(comp, 0, 0);
			comp = (FGTextField) obj.getGuiComponent(MigDirectoryDesc.FLD_DIR_PATH);
			comp.setColumns(40);
			comp.setEditable(false);
			add(comp, 1, 0);
		}else{
			add(obj, FField.FLD_NAME, 0, 0);
			add(obj, MigDirectoryDesc.FLD_DIR_PATH, 0, 1);
	    
			FValidationPanel vPanel = showValidationPanel(true);
			vPanel.addSubject(obj);
		}
	}
}

