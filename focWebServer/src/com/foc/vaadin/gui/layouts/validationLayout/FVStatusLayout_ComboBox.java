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
import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolder;
import com.foc.business.status.StatusHolderDesc;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.business.workflow.map.WFTransactionConfig;
import com.foc.desc.FocObject;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.windows.optionWindow.IOption;
import com.foc.vaadin.gui.windows.optionWindow.OptionDialogWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WFTransactionWrapper_Form;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.modules.workflow.Workflow_Cancel_Form;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;

@SuppressWarnings("serial")
public class FVStatusLayout_ComboBox extends ComboBox implements com.vaadin.data.Property.ValueChangeListener{

	//changes can be found under this comment
	//hadi_status_combobox
	
	private FocXMLLayout xmlLayout        = null;
	private FocObject    focObject        = null;
	private VerticalLayout           root = null;
	private OptionDialogWindow approveOptionDialogWindow = null;
	private boolean disableValueChangeListener = false;
	
	private final static String ITEM_PROPERTY_ID        = "STATUS";
	
	private final static String ITEM_TITLE_APPROVED     = "Approved";
	private final static String ITEM_TITLE_RESET_STATUS = "Reset Status";
	private final static String ITEM_TITLE_CANCEL       = "Cancel";
	private final static String ITEM_TITLE_CLOSE        = "Close";
	
  public FVStatusLayout_ComboBox(FocXMLLayout xmlLayout, FocObject focObject){
		setFocObject(focObject);
		this.xmlLayout = xmlLayout;
		init();
		selectCurrentStatus();
		addValueChangeListener(this);
	}
	
	public void dispose(){
		if(approveOptionDialogWindow != null){
			approveOptionDialogWindow.dispose();
			approveOptionDialogWindow = null;
		}
		focObject = null;
		xmlLayout = null;
		root = null;
	}
	
	private void init(){
//		addStyleName("border");
		addStyleName("noPrint");
		setCaption("Status");
		setWidth("100px");
		setItemCaptionMode(ItemCaptionMode.ITEM);
		fillStatusComboBox();
	}
	
	private void fillStatusComboBox(){
		Container container = getContainerDataSource();
		if(container != null){
			container.removeAllItems();
			container.addContainerProperty(ITEM_PROPERTY_ID, String.class, "-");
			
			FProperty property = (FProperty) getFocObject().iFocData_getDataByPath(StatusHolderDesc.FNAME_STATUS);
		  int status = property.getInteger();
		  
		  WFTransactionConfig transConfig = getFocObject().workflow_getTransactionConfig();
		  boolean hasMapForSignatures = transConfig != null ? transConfig.isApproveByMapSignature() : false;
		  
		  if(status == StatusHolderDesc.STATUS_PROPOSAL){
		  	if(getFocObject().workflow_IsAllowApprove() && !hasMapForSignatures){
		  		addNewItemProperty(ITEM_TITLE_APPROVED);	  		
		  	}
		  	if(getFocObject().workflow_IsAllowCancel()){
		  		addNewItemProperty(ITEM_TITLE_CANCEL);	  		
		  	}
		  }else if(status == StatusHolderDesc.STATUS_APPROVED){
		  	if(getFocObject().workflow_IsAllowCancel()){
		  		addNewItemProperty(ITEM_TITLE_CANCEL);
		  	}
		  	if(getFocObject().workflow_IsAllowClose()){
		  		addNewItemProperty(ITEM_TITLE_CLOSE);	  		
		  	}
		  }
		  if(getFocObject().workflow_IsAllowResetToProposal()){
		  	addNewItemProperty(ITEM_TITLE_RESET_STATUS);
		  }
		}
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
	
	private void addItemIfNotFound(String itemName){
		if(itemName != null && !itemName.isEmpty() && getContainerDataSource() != null && !getContainerDataSource().containsId(itemName)){
			addNewItemProperty(itemName);
		}
	}
	
	private void selectCurrentStatus(){
		if(focObject != null){
			FProperty property = (FProperty) focObject.iFocData_getDataByPath(StatusHolderDesc.FNAME_STATUS);
			if(property != null){
				addItemIfNotFound(property.getString());
				setDisableValueChangeListener(true);
				select(property.getString());
				setDisableValueChangeListener(false);
			}
    }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
		if(!isDisableValueChangeListener()){
			Property<String> property = event != null ? event.getProperty() : null;
			if(property != null &&property.getValue() != null){
				if(property.getValue().equals(ITEM_TITLE_APPROVED)){
					approve();
				}else if(property.getValue().equals(ITEM_TITLE_CANCEL)){
					cancel();
				}else if(property.getValue().equals(ITEM_TITLE_CLOSE)){
					close();
				}else if(property.getValue().equals(ITEM_TITLE_RESET_STATUS)){
					resetToProposal();
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void approve(){
		approveOptionDialogWindow = new OptionDialogWindow("Approve this transaction", null);
    approveOptionDialogWindow.setWidth("300px");
    approveOptionDialogWindow.setHeight("200px");
    
    approveOptionDialogWindow.addOption("Yes, approve", new IOption() {
      @Override
      public void optionSelected(Object contextObject) {
        getStatusHolder().setStatusToValidated();
        getFocObject().validate(true);
        xmlLayout.copyMemoryToGui();
        xmlLayout.getValidationLayout().commit();
        refreshStatusLabel();
      }
    });
    approveOptionDialogWindow.addOption("Cancel", new IOption() {
      @Override
      public void optionSelected(Object contextObject) {
      	selectCurrentStatus();
      }
    });
    UI.getCurrent().addWindow(approveOptionDialogWindow);
	}
	
	@SuppressWarnings("deprecation")
	public void resetToProposal(){
		approveOptionDialogWindow = new OptionDialogWindow("Reset this transaction to proposal", null);
    approveOptionDialogWindow.setWidth("300px");
    approveOptionDialogWindow.setHeight("200px");
    
    approveOptionDialogWindow.addOption("Yes, reset", new IOption() {
      @Override
      public void optionSelected(Object contextObject) {
        boolean error = getStatusHolder().resetStatusToProposal();
        if(!error){
	        getFocObject().validate(true);
	        xmlLayout.getValidationLayout().commit();
        }
        refreshStatusLabel();
      }
    });
    approveOptionDialogWindow.addOption("Cancel", new IOption() {
      @Override
      public void optionSelected(Object contextObject) {
      	selectCurrentStatus();
      }
    });
    UI.getCurrent().addWindow(approveOptionDialogWindow);
	}
	
	@SuppressWarnings("deprecation")
	public void close(){
		OptionDialogWindow optionWindow = new OptionDialogWindow("Close this transaction", null);
    optionWindow.setWidth("200px");
    optionWindow.setHeight("200px");
    
    optionWindow.addOption("Yes close", new IOption() {
      @Override
      public void optionSelected(Object contextObject) {
        getStatusHolder().setStatus(StatusHolderDesc.STATUS_CLOSED);
        getStatusHolder().setClosureDate(Globals.getDBManager().getCurrentTimeStamp_AsTime());
        xmlLayout.getValidationLayout().commit();
        refreshStatusLabel();
      }
    });
    optionWindow.addOption("No", new IOption() {
      @Override
      public void optionSelected(Object contextObject) {
      	selectCurrentStatus();
      }
    });
    UI.getCurrent().addWindow(optionWindow);
	}
	
	public void cancel(){
		popupCancel(getWindow(), getFocObject(), FVStatusLayout_ComboBox.this, null);
	}
	
	public FocCentralPanel getWindow(){
		return findAncestor(FocCentralPanel.class);
	}
	
	public IStatusHolder getIStatusHolder(){
    return (IStatusHolder)focObject;
  }
  
  public StatusHolder getStatusHolder(){
    return getIStatusHolder().getStatusHolder();
  }
  
  @SuppressWarnings("deprecation")
	public OptionDialogWindow getApproveOptionDialogWindow() {
    return approveOptionDialogWindow;
  }
	
	//-------------------------------------------------------------------------------------------------------------------------------------------
  
  private void setFocObject(FocObject focObject){
    this.focObject = focObject;
  }
  
  private FocObject getFocObject(){
    return focObject;
  }
  
  public void refreshStatusLabel(){
  	fillStatusComboBox();  	
    selectCurrentStatus();
  }

  public static void popupCancel(FocCentralPanel mainWindow, FocObject focObject, FVStatusLayout_ComboBox statusLayout, WFTransactionWrapper_Form transactionWrapperForm){
		if(focObject != null && focObject.getThisFocDesc() instanceof IWorkflowDesc){
			
			IWorkflowDesc iworkflowDesc= (IWorkflowDesc) focObject.getThisFocDesc();
			IWorkflow iworkflow = (IWorkflow) focObject;
	  	
	  	XMLViewKey xmlKey = new XMLViewKey("IWorkflow", XMLViewKey.TYPE_FORM, WorkflowWebModule.CTXT_CANCEL_TRANSACTION, XMLViewKey.VIEW_DEFAULT);
			Workflow_Cancel_Form centralPanel = (Workflow_Cancel_Form) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(mainWindow, xmlKey, (IFocData) iworkflow);
//			centralPanel.setStatusLayout(statusLayout);hadi_13
//			centralPanel.setTransactionWrapperForm(transactionWrapperForm);
			centralPanel.parseXMLAndBuildGui();
			
			FocCentralPanel centralWindow = new FocCentralPanel();
			centralWindow.fill();
			centralWindow.changeCentralPanelContent(centralPanel, false);
			
			Window window = centralWindow.newWrapperWindow();
//			window.setWidth("500px");
//			window.setHeight("300px");
			window.setPositionX(200);
			window.setPositionY(100);
			FocWebApplication.getInstanceForThread().addWindow(window);
		}
  }

  public void cancelTransaction(){
//  	if(getStatusHolder() != null){
//  		getStatusHolder().setStatusToCanceled(comment);
//  	}
//    if(getFocObject() != null){
//    	getFocObject().validate(true);
//    }
    if(xmlLayout != null && xmlLayout.getValidationLayout() != null){
    	xmlLayout.getValidationLayout().commit();
    }
    refreshStatusLabel();
  }
  
  private void setRootVerticalLayout(VerticalLayout root){
    this.root = root;
  }
  
  public VerticalLayout getRootVerticalLayout(){
    return root;
  }

	public boolean isDisableValueChangeListener() {
		return disableValueChangeListener;
	}

	public void setDisableValueChangeListener(boolean disableValueChangeListener) {
		this.disableValueChangeListener = disableValueChangeListener;
	}
}
