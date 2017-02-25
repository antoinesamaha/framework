package com.foc.business.workflow.map;

import com.foc.desc.FocObject;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class WFStageGuiDetailsPanel extends FPanel{

	public static final int VIEW_STANDARD  = FocObject.DEFAULT_VIEW_ID;
	public static final int VIEW_SELECTION = 2;
	
	public WFStageGuiDetailsPanel(FocObject focObj, int view){
		WFStage titleObj = (WFStage) focObj;
		
		if(view == VIEW_SELECTION){
			FGTextField tf = (FGTextField) add(titleObj, WFStageDesc.FLD_NAME, 0, 0);
			tf.setEditable(false);
			
			tf = (FGTextField) add(titleObj, WFStageDesc.FLD_DESCRIPTION, 0, 1);
			tf.setEditable(false);
		}else{
			add(titleObj, WFStageDesc.FLD_NAME, 0, 0);
			add(titleObj, WFStageDesc.FLD_DESCRIPTION, 0, 1);
			add(titleObj, WFStageDesc.FLD_IS_APPROVAL, 0, 2);
	
			FValidationPanel validPanel = showValidationPanel(true);
			validPanel.addSubject(titleObj);
		}
	}
}
