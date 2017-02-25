package com.foc.vaadin.gui.layouts.validationLayout;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.business.workflow.map.WFStage;
import com.foc.desc.FocObject;
import com.foc.vaadin.gui.components.FVGearWrapper;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVStageLayout extends FVGearWrapper<FVLabel >{

	private VerticalLayout root           = null;
	private FocXMLLayout   xmlLayout      = null;
	private FocObject      focObject      = null;
	private FVLabel        stageNameLabel = null;
	
	private PopupLinkButton signButton         = null;
	private PopupLinkButton cancelPreSigButton = null;
	private PopupLinkButton undoSigButton      = null;
	
	public FVStageLayout(FocXMLLayout xmlLayout, FocObject focObject) {
		setFocObject(focObject);
		this.xmlLayout = xmlLayout;
		addStyleName("border");
		setCaption("Stage Status");
		setComponent(getStageNameLabel());
	}
	
	@Override
	public void dispose(){
		super.dispose();
		if(stageNameLabel != null){
			stageNameLabel.dispose();
			stageNameLabel = null;
		}
		if(signButton != null){
			signButton.dispose();
			signButton = null;
		}
		if(cancelPreSigButton != null){
			cancelPreSigButton.dispose();
			cancelPreSigButton = null;
		}
		if(undoSigButton != null){
			undoSigButton.dispose();
			undoSigButton = null;
		}
		focObject = null;
		xmlLayout = null;
		root = null;
	}
	
	@Override
	public void fillMenu(VerticalLayout root) {
		setRootVerticalLayout(root);
		
		if(isObjectNeedsSignatureFromThisUser()){
			PopupLinkButton button = getSignButton(true);
			if(button != null){
				root.addComponent(button);
		  }
			
			button = getCancelPreviousSignatureButton(true);
			if(button != null){
				root.addComponent(button);
			}
		}
		
		if(getFocObject() != null && getFocObject().workflow_IsLastSignatureDoneByThisUser(true)){
			PopupLinkButton button = getUndoSignatureButton(true);
			root.addComponent(button);
		}
	}
	
	private boolean isObjectNeedsSignatureFromThisUser(){
		return getFocObject() != null ? getFocObject().workflow_NeedsSignatureOfThisUser() : false;
	}
	
	private IWorkflow getIWorkflow(){
    return (IWorkflow) focObject;
  }
	
	private Workflow getWorkflow(){
		IWorkflow iWorkflow = getIWorkflow();
    return iWorkflow != null ? iWorkflow.iWorkflow_getWorkflow() : null;
  }

	private WFStage getCurrentStage(){
		WFStage stage = null;
		Workflow workflow = getWorkflow();
		if(workflow != null){
			stage = workflow.getCurrentStage();
		}
		return stage;
	}

	public PopupLinkButton getSignButton(boolean create) {
  	if(signButton == null && create){
  		signButton = new PopupLinkButton("Sign", new ClickListener() {
		    @Override
		    public void buttonClick(ClickEvent event) {
		    	
		    	OptionDialog optionDialog = new OptionDialog("Confirmation", "Are you sure you want to Sign this transaction?") {
		    		@Override
		    		public boolean executeOption(String option) {
		    			if(option.equals("YES")){
		  		  		Workflow workflow = getWorkflow();
		  		  		FocXMLLayout focXMLLayout = getFocXMLLayout();
		  		  		if(workflow != null && focXMLLayout != null && focXMLLayout.getValidationLayout() != null){
		  		  			FVValidationLayout validationLayout = focXMLLayout.getValidationLayout();
		  		  			workflow.sign();

//		  		  			validationLayout.commit();
		  		  			refreshStatusLayout();
//		  		  			getStageNameLabel().setValue(getCurrentStageName());
//		  		  			
//		  	  				getRootVerticalLayout().removeComponent(getSignButton(false));
		  	  				validationLayout.apply();
		  		  		}
		    			}
		    			return false;
		    		}
		    	};
					optionDialog.addOption("YES", "Yes sign");
					optionDialog.addOption("CANCEL", "Cancel");
					Globals.popupDialog(optionDialog);
		    }
		  });
  	}
  	return signButton;
  }
	
	public void refreshStatusLayout(){
//		FVStatusLayout statusLayout = getStatusLayout();hadi_status_combobox
		FVStatusLayout_MenuBar statusLayout = getStatusLayout();
		if(statusLayout != null){
			statusLayout.refreshStatusMenuBar();
		}
	}
	
	/*private FVStatusLayout getStatusLayout(){
		FVStatusLayout statusLayout = null;
		FVValidationLayout validationLayout = getFocXMLLayout().getValidationLayout();
		if(validationLayout != null){
			statusLayout = validationLayout.getStatusLayout(false);
		}
		return statusLayout;
	}
	hadi_status_combobox*/
	
	private FVStatusLayout_MenuBar getStatusLayout(){
		FVStatusLayout_MenuBar statusLayout = null;
		FVValidationLayout validationLayout = getFocXMLLayout().getValidationLayout();
		if(validationLayout != null){
			statusLayout = validationLayout.getStatusLayout(false);
		}
		return statusLayout;
	}
	
	private PopupLinkButton getUndoSignatureButton(boolean create) {
		if(undoSigButton == null){
			undoSigButton = new PopupLinkButton("Undo My Signature", new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					if(getFocObject() != null && getFocObject().workflow_IsLastSignatureDoneByThisUser(true)){
						OptionDialog optionDialog = new OptionDialog("Alert!", "This will undo your signature because no one signed after you") {
							@Override
							public boolean executeOption(String optionName) {
								if(optionName.equals("YES")){
									if(getFocObject() != null && getFocObject().workflow_IsLastSignatureDoneByThisUser(true)){
										getWorkflow().undoLastSignature();
										
				  		  		FocXMLLayout focXMLLayout = getFocXMLLayout();
				  		  		if(focXMLLayout != null && focXMLLayout.getValidationLayout() != null){
				  		  			FVValidationLayout validationLayout = focXMLLayout.getValidationLayout();
				  		  			if(validationLayout != null){
				  		  				validationLayout.commit();
				  		  				validationLayout.goBack();
				  		  			}
				  		  		}										
									}
								}
								return false;
							}
						};
						optionDialog.addOption("YES", "Are you sure you want to continue.");
						optionDialog.addOption("CANCEL", "Cancel");
						Globals.popupDialog(optionDialog);
					}
				}
			});
		}
		return undoSigButton;
	}
	
	public PopupLinkButton getCancelPreviousSignatureButton(boolean create) {
  	if(cancelPreSigButton == null && create){
  		
  		Workflow workflow = getWorkflow();
  		if(workflow != null && workflow.getCurrentStage() != null){
	  		cancelPreSigButton = new PopupLinkButton("Reject all previous signatures", new ClickListener() {
			    @Override
			    public void buttonClick(ClickEvent event) {
			    	OptionDialog optionDialog = new OptionDialog("Alert!", "Are you sure you want to cancel all previous signatures! This will take the transaction back to the beginning of the workflow.") {
			    		@Override
			    		public boolean executeOption(String option) {
			    			if(option.equals("YES")){
			    				if(getWorkflow() != null){
			    					getWorkflow().undoAllSignatures();
			    					getStageNameLabel().setValue(getCurrentStageName());
			    					getRootVerticalLayout().addComponentAsFirst(getSignButton(true));
			    				}
			    			}
			    			return false;
			    		}
			    	};
						optionDialog.addOption("YES", "Yes reject signatures");
						optionDialog.addOption("CANCEL", "Cancel");
						Globals.popupDialog(optionDialog);
			    }
			  });
  		}
  	}
  	return cancelPreSigButton;
  }
	
	private String getCurrentStageName(){
		String currentStageName = "Unsigned";
		if(getCurrentStage() != null){
			currentStageName = getCurrentStage().getName();
		}
		return currentStageName;
	}
	
	public void refreshStageLayout(){
		getStageNameLabel().setValueString(getCurrentStageName());
	}
	
	private void setFocObject(FocObject focObject){
    this.focObject = focObject;
  }
	
	private FocXMLLayout getFocXMLLayout(){
		return xmlLayout;
	}
	
	private void setRootVerticalLayout(VerticalLayout root){
    this.root = root;
  }
  
  private VerticalLayout getRootVerticalLayout(){
    return root;
  }
  
  private FVLabel getStageNameLabel(){
  	if(stageNameLabel == null){
  		stageNameLabel = new FVLabel(getCurrentStageName());
  	}
  	return stageNameLabel;
  }

	public FocObject getFocObject() {
		return focObject;
	}
}
