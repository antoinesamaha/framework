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
import com.foc.desc.FocObject;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FVStageLayout_ComboBox extends ComboBox implements com.vaadin.data.Property.ValueChangeListener{

	//changes can be found under this comment
	//hadi_stage_combobox
	
	private final static String ITEM_PROPERTY_ID = "STAGES";
	
	private final static String ITEM_TITLE_SIGN                           = "Sign";
	private final static String ITEM_TITLE_UNSIGNED                       = "Unsigned";
	private final static String ITEM_TITLE_UNDO_MY_SIGNATURE              = "Undo My Signature";
	private final static String ITEM_TITLE_REJECT_ALL_PREVIOUS_SIGNATURES = "Reject All Previous Signatures";

	private FocXMLLayout   xmlLayout = null;
	private FocObject      focObject = null;
	
	public FVStageLayout_ComboBox(FocXMLLayout xmlLayout, FocObject focObject) {
		this.focObject = focObject;
		this.xmlLayout = xmlLayout;
		init();
	}
	
	private void init(){
		setCaption("Stage Status");
		setImmediate(true);
		setItemCaptionMode(ItemCaptionMode.ITEM);
		addStages();
		addValueChangeListener(this);
		selectCurrentStage();
	}
	
	public void dispose(){
		xmlLayout = null;
		focObject = null;
		removeAllItems();
	}
	
	private void addStages(){
		Container container = getContainerDataSource();
		if(container != null){
			container.addContainerProperty(ITEM_PROPERTY_ID, String.class, "-");
		
			if(isObjectNeedsSignatureFromThisUser()){
				addNewItemProperty(ITEM_TITLE_SIGN);
				
				Workflow workflow = getWorkflow();
	  		if(workflow != null && workflow.getCurrentStage() != null){
	  			addNewItemProperty(ITEM_TITLE_REJECT_ALL_PREVIOUS_SIGNATURES);
	  		}
			}
			if(isLastSignatureDoneByThisUser()){
				addNewItemProperty(ITEM_TITLE_UNDO_MY_SIGNATURE);
			}
			if(getCurrentStageName() == null || getCurrentStageName().isEmpty() || getCurrentStageName().equals(ITEM_TITLE_UNSIGNED)){
				addNewItemProperty(ITEM_TITLE_UNSIGNED);
			}
		}
	}
	
	private boolean isLastSignatureDoneByThisUser(){
		return getFocObject() != null ? getFocObject().workflow_IsLastSignatureDoneByThisUser(true) : false;
	}
	
	@SuppressWarnings("unchecked")
	private void addNewItemProperty(String itemId){
		if(getContainerDataSource() != null){
			Item item = getContainerDataSource().addItem(itemId);
			Property<String> property = item != null ? item.getItemProperty(ITEM_PROPERTY_ID) : null;
			if(property != null){
				property.setValue(itemId);
			}
		}
	}
	
	private boolean isObjectNeedsSignatureFromThisUser(){
		return getFocObject() != null ? getFocObject().workflow_NeedsSignatureOfThisUser() : false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
		Property<String> property = event != null ? event.getProperty() : null;
		if(property != null &&property.getValue() != null){
			if(property.getValue().equals(ITEM_TITLE_SIGN)){
				sign();
			}else if(property.getValue().equals(ITEM_TITLE_UNSIGNED)){
//				undoMySignature();
			}else if(property.getValue().equals(ITEM_TITLE_UNDO_MY_SIGNATURE)){
				undoMySignature();
			}else if(property.getValue().equals(ITEM_TITLE_REJECT_ALL_PREVIOUS_SIGNATURES)){
				rejectAllPreviousSignatures();
			}
		}
	}
	
	public void sign(){
		OptionDialog optionDialog = new OptionDialog("Confirmation", "Are you sure you want to Sign this transaction?") {
  		@Override
  		public boolean executeOption(String option) {
  			if(option.equals("YES")){
		  		Workflow workflow = getWorkflow();
		  		FocXMLLayout focXMLLayout = getFocXMLLayout();
		  		if(workflow != null && focXMLLayout != null && focXMLLayout.getValidationLayout() != null){
		  			FVValidationLayout validationLayout = focXMLLayout.getValidationLayout();
		  			workflow.sign();
		  			refreshStatusLayout();
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
	
	private String getCurrentStageName(){
		String currentStageName = "Unsigned";
		if(getCurrentStage() != null){
			currentStageName = getCurrentStage().getName();
		}
		addItemIfNotFound(currentStageName);
		return currentStageName;
	}
	
	private void addItemIfNotFound(String itemName){
		if(itemName != null && !itemName.isEmpty() && getContainerDataSource() != null && !getContainerDataSource().containsId(itemName)
				&& getWorkflow() != null 
				&& (isObjectNeedsSignatureFromThisUser() || isLastSignatureDoneByThisUser())
				){
			addNewItemProperty(itemName);
		}
	}
	
	private WFStage getCurrentStage(){
		WFStage stage = null;
		Workflow workflow = getWorkflow();
		if(workflow != null){
			stage = workflow.getCurrentStage();
		}
		return stage;
	}
	
	private Workflow getWorkflow(){
		IWorkflow iWorkflow = getIWorkflow();
    return iWorkflow != null ? iWorkflow.iWorkflow_getWorkflow() : null;
  }
	
	public void refreshStatusLayout(){
		FVStatusLayout_MenuBar statusLayout = getStatusLayout();
		if(statusLayout != null){
			statusLayout.refreshStatusMenuBar();
		}
	}
	
	private FVStatusLayout_MenuBar getStatusLayout(){
		FVStatusLayout_MenuBar statusLayout = null;
		FVValidationLayout validationLayout = getFocXMLLayout().getValidationLayout();
		if(validationLayout != null){
			statusLayout = validationLayout.getStatusLayout(false);
		}
		return statusLayout;
	}
	
	private void selectCurrentStage(){
		select(getCurrentStageName());
	}
	
	public void refreshStageLayout(){
		selectCurrentStage();
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

}
