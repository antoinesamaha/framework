package com.foc.business.printing;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class PrnLayoutGuiDetailsPanel extends FPanel{

	public static final int VIEW_STANDARD  = FocObject.DEFAULT_VIEW_ID;
	
	public PrnLayoutGuiDetailsPanel(FocObject focObj, int view){
		PrnLayout layout = (PrnLayout) focObj;
		
		add(layout, PrnLayoutDesc.FLD_CONTEXT, 0, 0);
		add(layout, PrnLayoutDesc.FLD_FILE_NAME, 0, 1);

		FValidationPanel validPanel = showValidationPanel(true);
		validPanel.addSubject(layout);
	}
}
