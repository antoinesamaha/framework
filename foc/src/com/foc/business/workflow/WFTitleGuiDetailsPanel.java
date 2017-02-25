package com.foc.business.workflow;

import com.foc.desc.FocObject;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class WFTitleGuiDetailsPanel extends FPanel{

	public static final int VIEW_STANDARD  = FocObject.DEFAULT_VIEW_ID;
	public static final int VIEW_SELECTION = 2;
	
	public WFTitleGuiDetailsPanel(FocObject focObj, int view){
		WFTitle titleObj = (WFTitle) focObj;
		
		if(view == VIEW_SELECTION){
			FGTextField tf = (FGTextField) add(titleObj, WFTitleDesc.FLD_NAME, 0, 0);
			tf.setEditable(false);
			
			tf = (FGTextField) add(titleObj, WFTitleDesc.FLD_DESCRIPTION, 0, 1);
			tf.setEditable(false);
		}else{
			add(titleObj, WFTitleDesc.FLD_NAME, 0, 0);
			add(titleObj, WFTitleDesc.FLD_DESCRIPTION, 0, 1);
	
			FValidationPanel validPanel = showValidationPanel(true);
			validPanel.addSubject(titleObj);
		}
	}
}
