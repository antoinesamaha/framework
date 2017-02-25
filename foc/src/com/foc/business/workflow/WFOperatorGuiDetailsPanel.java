package com.foc.business.workflow;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class WFOperatorGuiDetailsPanel extends FPanel{

	public static final int VIEW_STANDARD  = FocObject.DEFAULT_VIEW_ID;
	
	public WFOperatorGuiDetailsPanel(FocObject focObj, int view){
		WFOperator warehouse = (WFOperator) focObj;
		
		add(warehouse, WFOperatorDesc.FLD_AREA, 0, 0);
		add(warehouse, WFOperatorDesc.FLD_TITLE, 0, 1);
		add(warehouse, WFOperatorDesc.FLD_USER, 0, 2);

		FValidationPanel validPanel = showValidationPanel(true);
		validPanel.addSubject(warehouse);
	}
}
