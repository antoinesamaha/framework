package com.foc.db.migration;

import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class MigDataBaseGuiDetailsPanel extends FPanel{

	public final static int VIEW_SELECTION = 2; 
	
	public MigDataBaseGuiDetailsPanel(FocObject obj, int viewID){
		if(viewID == VIEW_SELECTION){
			setInsets(0, 0, 0, 0);
			FGTextField comp = (FGTextField) obj.getGuiComponent(FField.FLD_NAME);
			comp.setEditable(false);
			add(comp, 0, 0);
			comp = (FGTextField) obj.getGuiComponent(MigDataBaseDesc.FLD_DESCRIPTION);
			comp.setEditable(false);
			add(comp, 1, 0);
		}else{
			add(obj, FField.FLD_NAME, 0, 0);
			add(obj, MigDataBaseDesc.FLD_DESCRIPTION, 0, 1);
			add(obj, MigDataBaseDesc.FLD_URL, 0, 2);
			add(obj, MigDataBaseDesc.FLD_JDBC_DRIVER, 0, 3);
			add(obj, MigDataBaseDesc.FLD_USER_NAME, 0, 4);			
			add(obj, MigDataBaseDesc.FLD_PASSWORD, 0, 5);
	    
			FValidationPanel vPanel = showValidationPanel(true);
			vPanel.addSubject(obj);
		}
	}
}

