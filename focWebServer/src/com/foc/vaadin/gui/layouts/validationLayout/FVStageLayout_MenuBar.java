/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin.gui.layouts.validationLayout;

import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.business.workflow.map.WFStage;
import com.foc.business.workflow.signing.WFSignatureNeededResult;
import com.foc.desc.FocObject;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.MenuBar;

@SuppressWarnings("serial")
public class FVStageLayout_MenuBar extends MenuBar{

//	private final static String ITEM_TITLE_SIGN                           = "Sign";
//	//private final static String ITEM_TITLE_UNSIGNED                       = "Unsigned";
//	private final static String ITEM_TITLE_UNDO_MY_SIGNATURE              = "Undo My Signature";
//	private final static String ITEM_TITLE_REJECT_ALL_PREVIOUS_SIGNATURES = "Reject All Previous Signatures";

	private FocXMLLayout xmlLayout    = null;
	private FocObject    focObject    = null;
	private MenuItem     rootMenuItem = null;
	
	public FVStageLayout_MenuBar(FocXMLLayout xmlLayout, FocObject focObject) {
		this.focObject = focObject;
		this.xmlLayout = xmlLayout;
		init();
	}
	
	private void init(){
		setImmediate(true);
		addMenuBarItems();
		selectCurrentStage();
	}
	
	public void dispose(){
		rootMenuItem = null;
		xmlLayout = null;
		focObject = null;
		removeItems();
	}
		
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
		getRootMenuItem().removeChildren();
		selectCurrentStage();
		addMenuBarItems();
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
		if(getRootMenuItem() != null){
			getRootMenuItem().setText("WF Stage: "+getCurrentStageName());
		}
	}
	
	private String getCurrentStageName(){
		String  currentStageName = "Unsigned";
		WFStage stage = getCurrentStage();
		if(stage != null){
			currentStageName = stage.getName();
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
	
	public MenuItem getRootMenuItem(){
		if(rootMenuItem == null){
			rootMenuItem = addItem("Stage Status", null);
		}
		return rootMenuItem; 
	}
	
	public class SignCommand implements Command {
		
		private WFSignatureNeededResult result = null;
		
		public SignCommand(WFSignatureNeededResult result){
			this.result = result;
		}
		
		public void dispose(){
			result = null;
		}
		
		@Override
		public void menuSelected(MenuItem selectedItem) {
			popupAndSign();
		}
		
		private void popupAndSign(){
			OptionDialog optionDialog = new OptionDialog("Confirmation", "Are you sure you want to Sign this transaction?") {
	  		@Override
	  		public boolean executeOption(String option) {
	  			if(option.equals("YES")){
	  				sign(result);
	  			}
	  			return false;
	  		}
	  	};
			optionDialog.addOption("YES", "Yes sign");
			optionDialog.addOption("CANCEL", "Cancel");
			Globals.popupDialog(optionDialog);
		}
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
