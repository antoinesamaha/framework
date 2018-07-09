package com.foc.vaadin.gui.layouts.validationLayout;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.business.workflow.map.WFStage;
import com.foc.business.workflow.signing.WFSignatureNeededResult;
import com.foc.desc.FocObject;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class FVStageLayout_Button extends Button {

	private FocXMLLayout xmlLayout    = null;
	private FocObject    focObject    = null;
	
	public FVStageLayout_Button(FocXMLLayout xmlLayout, FocObject focObject) {
		this.focObject = focObject;
		this.xmlLayout = xmlLayout;
		init();
		setIcon(FontAwesome.ARROW_UP);
		addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buttonClicked();
			}
		});
	}
	
	private void init(){
		setImmediate(true);
//		addMenuBarItems();
		selectCurrentStage();
	}
	
	public void dispose(){
		xmlLayout = null;
		focObject = null;
	}

	public boolean isArabic(){
		return ConfigInfo.isArabic();
	}
	
	public void buttonClicked() {
		FVValidationLayout vLayout = getValidationLayout();
		if(vLayout != null) {
			vLayout.setVisible_LogLayout(!vLayout.isVisible_LogLayout());
			vLayout.setVisible_WorkflowConsole(!vLayout.isVisible_WorkflowConsole());
			
			if(vLayout.isVisible_WorkflowConsole()) {
				setIcon(FontAwesome.ARROW_DOWN);
			} else {
				setIcon(FontAwesome.ARROW_UP);
			}
		}
	}
	
	public FVValidationLayout getValidationLayout() {
		FVValidationLayout validationLayout = null;
		FocXMLLayout focXMLLayout = getFocXMLLayout();
		if(focXMLLayout != null && focXMLLayout.getValidationLayout() != null){
			validationLayout = focXMLLayout.getValidationLayout();
		}
		return validationLayout;
	}
	
		/*
	private void addMenuBarItems(){
		WFSignatureNeededResult result = getFocObject() != null ? getFocObject().workflow_NeedsSignatureOfThisUser_AsTitleIndex(null) : null;
		if(result !=null && result.getTitleIndex() >= 0){
			if(result.isOnBehalfOf()){
				getRootMenuItem().addItem("Sign On Behalf of "+result.getTitle(), new SignCommand(result));
			}else{
				getRootMenuItem().addItem("Sign", new SignCommand(result));
			}
			
			Workflow workflow = getWorkflow();
  		if(workflow != null && workflow.getCurrentStage() != null){
  			getRootMenuItem().addItem("Reject all previous signatures", new Command() {
					@Override
					public void menuSelected(MenuItem selectedItem) {
						rejectAllPreviousSignatures();
						refreshStageMenuBar();	
					}
				});
  		}
		}
		
		if(isLastSignatureDoneByThisUser()){
			getRootMenuItem().addItem("Undo My Signature", new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					undoMySignature();
					refreshStageMenuBar();
				}
			});
		}
//		if(getCurrentStageName() == null || getCurrentStageName().isEmpty() || getCurrentStageName().equals(ITEM_TITLE_UNSIGNED)){
//			getRootMenuItem().addItem(ITEM_TITLE_UNSIGNED, menuClickListener);
//		}
	}
	*/
	private void rejectAllPreviousSignatures(){
		OptionDialog optionDialog = new OptionDialog("Alert!", "Are you sure you want to cancel all previous signatures! This will take the transaction back to the beginning of the workflow.") {
  		@Override
  		public boolean executeOption(String option) {
  			if(option.equals("YES")){
  				if(getWorkflow() != null){
  					getWorkflow().undoAllSignatures();
  					selectCurrentStage();
  				}
  			}
  			return false;
  		}
  	};
		optionDialog.addOption("YES", "Yes reject signatures");
		optionDialog.addOption("CANCEL", "Cancel");
		Globals.popupDialog(optionDialog);
	}
	
	public void refreshStatusLayout(){
		FVStatusLayout_MenuBar statusLayout = getStatusLayout();
		if(statusLayout != null){
			statusLayout.refreshStatusMenuBar();
		}
	}
	
	public void refreshStageMenuBar(){
//		getRootMenuItem().removeChildren();
		selectCurrentStage();
//		addMenuBarItems();
	}
	
	private FVStatusLayout_MenuBar getStatusLayout(){
		FVStatusLayout_MenuBar statusLayout = null;
		FVValidationLayout validationLayout = getFocXMLLayout().getValidationLayout();
		if(validationLayout != null){
			statusLayout = validationLayout.getStatusLayout(false);
		}
		return statusLayout;
	}
	
	private void undoMySignature(){
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
  		  				refreshStageMenuBar();
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
	
	private void selectCurrentStage(){
		setCaption(getCurrentStageName());
	}
	
	private String getCurrentStageName(){
		String currentStageName = isArabic() ? "ملاحظات" : "Comment";
		if(ConfigInfo.isShowStageNameOnValidationLayoutButton()) {
				WFStage stage = getCurrentStage();
				if(stage != null){
					currentStageName = stage.getName();
				}
		} else {
			if(getFocObject().workflow_NeedsSignatureOfThisUser()) {
				currentStageName = isArabic() ? "موافقة" : "Signature";
			}
		}
		return currentStageName;
	}
	
	private WFStage getCurrentStage(){
		WFStage stage = null;
		Workflow workflow = getWorkflow();
		if(workflow != null){
			stage = workflow.getCurrentStage();
		}
		return stage;
	}
	
	private boolean isLastSignatureDoneByThisUser(){
		return getFocObject() != null ? getFocObject().workflow_IsLastSignatureDoneByThisUser(true) : false;
	}
	
	private Workflow getWorkflow(){
		IWorkflow iWorkflow = getIWorkflow();
    return iWorkflow != null ? iWorkflow.iWorkflow_getWorkflow() : null;
  }
	
	private IWorkflow getIWorkflow(){
    return (IWorkflow) getFocObject();
  }
	
	private FocObject getFocObject(){
    return focObject;
  }
	
	private FocXMLLayout getFocXMLLayout(){
		return xmlLayout;
	}
	
	public void sign(){
		sign(null);
	}
	
	public void sign(WFSignatureNeededResult result){
		Workflow workflow = getWorkflow();
		FocXMLLayout focXMLLayout = getFocXMLLayout();
		if(workflow != null && focXMLLayout != null && focXMLLayout.getValidationLayout() != null){
			FVValidationLayout validationLayout = focXMLLayout.getValidationLayout();
			refreshStatusLayout();
			validationLayout.applyButtonClickListener();
			if(result != null){
				workflow.sign(result.getSignature(), result.getTitleIndex(), result.isOnBehalfOf());
			}else{
				workflow.sign();
			}
			validationLayout.apply();
			refreshStageMenuBar();
		}
		refreshStageMenuBar();
	}
}
