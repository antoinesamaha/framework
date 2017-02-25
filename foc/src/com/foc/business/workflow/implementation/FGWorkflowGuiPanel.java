package com.foc.business.workflow.implementation;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class FGWorkflowGuiPanel extends FPanel {

	private FGWorkflowButtonPanel signatureButton = null;
	private FocObject        focObject       = null;
	
	public FGWorkflowGuiPanel(FocObject object, String title, int view){
		super(title, view);
		focObject = object;
	}
	
	public void dispose(){
		super.dispose();
		focObject = null;
	}
	
	public FPanel getMainPanel(){
		return this;
	}

	@Override
  public FValidationPanel showValidationPanel(boolean show){
		FValidationPanel vPanel = super.showValidationPanel(show, true);
		if(focObject != null && !focObject.isCreated()){
			signatureButton = new FGWorkflowButtonPanel(focObject);
			vPanel.addButton(signatureButton);
		}
    return vPanel;
  }
}
