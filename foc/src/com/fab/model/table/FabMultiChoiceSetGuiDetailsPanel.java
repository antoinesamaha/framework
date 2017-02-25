package com.fab.model.table;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class FabMultiChoiceSetGuiDetailsPanel extends FPanel {

	public FabMultiChoiceSetGuiDetailsPanel(FocObject obj, int viewID){
		FabMultiChoiceSet set = (FabMultiChoiceSet) obj;
		
		add(set, FabMultiChoiceSetDesc.FLD_NAME, 0, 0);

		FabMultipleChoiceGuiBrowsePanel browse = new FabMultipleChoiceGuiBrowsePanel(set.getMultipleChoiceList(), FocObject.DEFAULT_VIEW_ID); 
		add(browse, 0, 1, 2, 1);
		
		FValidationPanel vPanel = showValidationPanel(true);
		vPanel.addSubject(set);
	}
}
