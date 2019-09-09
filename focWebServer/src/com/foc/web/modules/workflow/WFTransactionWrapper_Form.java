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
package com.foc.web.modules.workflow;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.admin.FocUser;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.WFLog;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.business.workflow.map.WFSignature;
import com.foc.business.workflow.map.WFStage;
import com.foc.business.workflow.signing.WFTransactionWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVComboBox;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVCommentLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVStageLayout_Button;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FValidationSettings;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class WFTransactionWrapper_Form extends FocXMLLayout {

	private WFTransactionWrapper_Table wrapperTable      = null;
	private FocXMLLayout               innerFocXMLLayout = null; 
	
	private FVButton signButton                        = null;
	private FVButton skipForTheMomentButton            = null;
	private FVButton hideButton                        = null;
	private FVButton cancelTransactionButton           = null;
	private FVButton cancelAllPreviousSignaturesButton = null;
	private FVButton cancelPreviousSignatureButton     = null;
	private FVButton rejectSignatureToButton           = null;
	private FVButton exitSlideShowButton               = null;

	private HashMap<WFStage, WFLog> stageLogMap        = null;
	
	@Override
	public void dispose() {
		if(innerFocXMLLayout != null && getValidationLayout() != null){
			getValidationLayout().removeValidationListener(innerFocXMLLayout);
		}
		super.dispose();
		wrapperTable = null;
		signButton = null;
		hideButton = null;
		skipForTheMomentButton = null;
		cancelAllPreviousSignaturesButton = null;
		cancelPreviousSignatureButton     = null;
		cancelTransactionButton = null;
		exitSlideShowButton = null;
		rejectSignatureToButton = null;
		
		innerFocXMLLayout = null;
	}

	public WFTransactionWrapper getTransactionWrapper(){
		return (WFTransactionWrapper) getFocObject();
	}
	
	public IWorkflow getIWorkflow(){
		return getTransactionWrapper() != null ? getTransactionWrapper().getWorkflow() : null;
	}
	
	public Workflow getWorkflow(){
		return getIWorkflow() != null ? getIWorkflow().iWorkflow_getWorkflow() : null;
	}
	
	public FocObject getTransactionFocObject(){
//		return getWorkflow() != null && getWorkflow().iWorkflow_getWorkflow() != null ? getWorkflow().iWorkflow_getWorkflow().getFocObject() : null;
		return (FocObject) getIWorkflow();
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		FocObject    focObject    = getTransactionFocObject();
		XMLViewKey   xmlViewKey   = buildXmlViewKey(getIWorkflow().iWorkflow_getWorkflow());
		innerFocXMLLayout = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel_NoAdjustmentToLastSelectedView(getMainWindow(), xmlViewKey, focObject);
		
		FVVerticalLayout rootLayout = (FVVerticalLayout) getComponentByName("ROOTLAYOUT");
		rootLayout.addComponent(innerFocXMLLayout);
//		innerFocXMLLayout.setParent(rootLayout);
		innerFocXMLLayout.setParentLayout(this);
		initLastCommentLabel();
	}
	
	private void initLastCommentLabel(){
		FVLabel lastCommentLabel = (FVLabel) getComponentByName("LAST_COMMENT");
		if(lastCommentLabel != null && getIWorkflow() != null && getIWorkflow().iWorkflow_getWorkflow() != null){
			String lastComment = getIWorkflow().iWorkflow_getWorkflow().getLastComment();
			lastCommentLabel.setValue(lastComment);
		}
	}
	
	@Override
	public void showValidationLayout(boolean showBackButton) {
		
		if(innerFocXMLLayout != null) {
			FValidationSettings settings = innerFocXMLLayout.getValidationSettings(true);
			getValidationSettings(true).copy(settings);
		} 
		
		super.showValidationLayout(showBackButton);

		FVValidationLayout validationLayout = getValidationLayout();
		if(validationLayout != null){
			FVStageLayout_Button stageButton = validationLayout.getStageLayout(false);
			if(stageButton != null) stageButton.buttonClicked();
			
			/*
			FVHorizontalLayout horizontalLayout = newSlideShowControlPanelToValidationLayout();
//			horizontalLayout.setWidth("100%");
			validationLayout.addComponentAsFirst(horizontalLayout);
			validationLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_LEFT);
			validationLayout.setExpandRatio(horizontalLayout, 1);
			
			horizontalLayout.addStyleName("foc-footerLayout");
			
			validationLayout.adjustForSignatureSlideShow();

			validationLayout.addValidationListener(innerFocXMLLayout);
			*/
		}	
	}
	
	private XMLViewKey buildXmlViewKey(Workflow workflow){
		XMLViewKey xmlViewKey = null;
		FocObject   focObject     = workflow.getFocObject();
		String      storageName   = workflow.getFocDesc().getStorageName();
		WFSignature nextSignature = workflow.nextSignature();
		
		String context = XMLViewKey.CONTEXT_DEFAULT;
		if(nextSignature.getTransactionContext() != null && !nextSignature.getTransactionContext().isEmpty()){
			context = nextSignature.getTransactionContext();
		}
		String view = XMLViewKey.VIEW_DEFAULT;
		if(nextSignature.getTransactionView() != null && !nextSignature.getTransactionView().isEmpty()){
			view = nextSignature.getTransactionView();
		}
		
		xmlViewKey = new XMLViewKey(storageName, XMLViewKey.TYPE_FORM, context, view);
		boolean isViewFound = XMLViewDictionary.getInstance().isXMLViewFound(xmlViewKey); 
		
		if(!isViewFound){
			xmlViewKey = new XMLViewKey(focObject.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
		}
		return xmlViewKey;
	}
	
	private FVHorizontalLayout newSlideShowControlPanelToValidationLayout(){
		FVHorizontalLayout controlPanel = new FVHorizontalLayout(null);
//		controlPanel.setWidth("100%");
		
		FVLabel label = newStageNameLabel();
		label.setWidth("100px");
		controlPanel.addComponent(label);
		controlPanel.setComponentAlignment(label, Alignment.BOTTOM_LEFT);

		FVCommentLayout commentLayout = new FVCommentLayout(this, controlPanel, getTransactionFocObject());
		commentLayout.setWidth("200px");
		controlPanel.addComponent(commentLayout);
		controlPanel.setComponentAlignment(commentLayout, Alignment.BOTTOM_RIGHT);		
//		controlPanel.setExpandRatio(commentLayout, 1);
		
//		FVTextField txtFld = new FVTextField("Comment");
//		txtFld.setValue("-- Under development --");
//		controlPanel.addComponent(txtFld);
//		txtFld.setWidth("100%");
//		controlPanel.setExpandRatio(txtFld, 1);
		
		controlPanel.addComponent(getSignButton());
		controlPanel.setComponentAlignment(getSignButton(), Alignment.BOTTOM_RIGHT);
		
		controlPanel.addComponent(getSkipForTheMomentButton());
		controlPanel.setComponentAlignment(getSkipForTheMomentButton(), Alignment.BOTTOM_RIGHT);

		controlPanel.addComponent(getHideButton());
		controlPanel.setComponentAlignment(getHideButton(), Alignment.BOTTOM_RIGHT);

		WFLog log = getTransactionWrapper().getWorkflow().iWorkflow_getWorkflow().getLastSignatureEvent(false);
		if(log != null){
			controlPanel.addComponent(getCancelPreviousSignatureButton());
			controlPanel.setComponentAlignment(getCancelPreviousSignatureButton(), Alignment.BOTTOM_RIGHT);
		}
			
//		if(getStageLogMap().size() > 1){
//			controlPanel.addComponent(getRejectSignatureToButton());
//			controlPanel.setComponentAlignment(getRejectSignatureToButton(), Alignment.BOTTOM_RIGHT);
//		}
			
		if(getStageLogMap().size() >= 1){
			controlPanel.addComponent(getCancelAllPreviousSignaturesButton());
			controlPanel.setComponentAlignment(getCancelAllPreviousSignaturesButton(), Alignment.BOTTOM_RIGHT);
		}

  	if(((FocObject)getIWorkflow()).workflow_IsAllowCancel()){
			controlPanel.addComponent(getCancelTransactionButton());
			controlPanel.setComponentAlignment(getCancelTransactionButton(), Alignment.BOTTOM_RIGHT);
  	}

		controlPanel.addComponent(getExitSlideShowButton());
		controlPanel.setComponentAlignment(getExitSlideShowButton(), Alignment.BOTTOM_RIGHT);
		return controlPanel;
	}

	private FVButton getSignButton(){
		if(signButton == null){
			signButton = new FVButton("Sign");
			signButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					copyGuiToMemory();
					getTransactionWrapper().sign();
					
					/*
					if(getTransactionWrapper().getWorkflow() != null && innerFocXMLLayout != null){
						String xmlContent = XMLViewDictionary.getInstance().pasrseXmlLayout(innerFocXMLLayout.getFocData(), innerFocXMLLayout.getXMLView(), innerFocXMLLayout.getXMLView().getXmlViewKey());
						WFLog log = getTransactionWrapper().getWorkflow().iWorkflow_getWorkflow().getLastSignatureEvent(false);
						if(log != null){
							log.setSignedTransactionXML(xmlContent);
							log.validate(true);
						}
					}
					*/
					
					gotoNextSlide();
				}
			});
		}
		return signButton;
	}
	
	private FVButton getHideButton(){
		if(hideButton == null){
			hideButton = new FVButton("Hide");
			hideButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					getTransactionWrapper().setHide(true);

					gotoNextSlide();
				}
			});
		}
		return hideButton;
	}
	
	private FVButton getSkipForTheMomentButton(){
		if(skipForTheMomentButton == null){
			skipForTheMomentButton = new FVButton("Skip For Now");
			skipForTheMomentButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					if(getValidationLayout().isAskForConfirmationForExit()){
						OptionDialog optionDialog = new OptionDialog("01Barmaja - Warning", "Do you want to confirm the changes you made?") {
							@Override
							public boolean executeOption(String option) {
								if(option.equals("DO_VALIDATION")){
									gotoNextSlide();
								}else if(option.equals("NO")){
									WFTransactionWrapper_Table wrapperTable = getWFTransactionWrapper_Table();//Get this before we apply because the property will be disposed when we apply
									getValidationLayout().cancel_ExecutionWithoutPrompt();
									if(wrapperTable != null) wrapperTable.openNextWrapper();									
								}
								return false;
							}
						};
						optionDialog.addOption("DO_VALIDATION", "Yes");
						optionDialog.addOption("NO", "No");
						optionDialog.addOption("CANCEL", "Cancel");
						Globals.popupDialog(optionDialog);
					}else{
						WFTransactionWrapper_Table wrapperTable = getWFTransactionWrapper_Table();//Get this before we apply because the property will be disposed when we apply
						goBack(null);
						if(wrapperTable != null) wrapperTable.openNextWrapper();									
					}
//					getValidationLayout().cancel();
					
				}
			});
		}
		return skipForTheMomentButton;
	}
	
	private FVButton getCancelTransactionButton(){
		if(cancelTransactionButton == null){
			cancelTransactionButton = new FVButton("Cancel/Archive");
			cancelTransactionButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					FVStatusLayout.popupCancel((FocCentralPanel) getMainWindow(), getTransactionFocObject(), null, WFTransactionWrapper_Form.this);
				}
			});
			/*
			cancelTransactionButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					
					OptionDialog optionDialog = new OptionDialog("Alert!", "Are you sure you want to Cancel the transaction.") {
						@Override
						public boolean executeOption(String option) {
							if(option.equals("YES")){
								if(getTransactionWrapper() != null){
									IWorkflow iWorkflow = getTransactionWrapper().getWorkflow();
									
									if(iWorkflow instanceof IStatusHolder){
										IStatusHolder iStatusHolder = (IStatusHolder) iWorkflow;
										iStatusHolder.getStatusHolder().setStatusToCanceled();
									}
									getValidationLayout().cancel();
								}
								
//								WFTransactionWrapper_Table wrapperTable = getWFTransactionWrapper_Table();//Get this before we apply because the property will be disposed when we apply
//								getValidationLayout().apply();
//								if(wrapperTable != null) wrapperTable.openNextWrapper();
								
							}else if(option.equals("NO")){
								getValidationLayout().cancel();
							}
							return false;
						}
					};
					optionDialog.addOption("YES", "Yes");
					optionDialog.addOption("NO", "No");
					optionDialog.addOption("CANCEL", "Cancel");
					Globals.popupDialog(optionDialog);
					
				}
			});
			*/
		}
		return cancelTransactionButton;
	}
		
	private FVButton getCancelAllPreviousSignaturesButton(){
		if(cancelAllPreviousSignaturesButton == null){
			cancelAllPreviousSignaturesButton = new FVButton("Reject All");
			cancelAllPreviousSignaturesButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					
					OptionDialog optionDialog = new OptionDialog("Alert!", "Are you sure you want to reject all previous signatures! This will take the transaction back to the beginning of the workflow.") {
						@Override
						public boolean executeOption(String option) {
							if(option.equals("YES")){
								if(getTransactionWrapper() != null){
									getTransactionWrapper().undoAllSignatures();
								}
								
								gotoNextSlide();
							}
							return false;
						}
					};
					optionDialog.addOption("YES", "YES undo all signatures");
//					optionDialog.addOption("NO", "No");
					optionDialog.addOption("CANCEL", "Cancel");
					Globals.popupDialog(optionDialog);
					
				}
			});
		}
		return cancelAllPreviousSignaturesButton;
	}
	
	public void gotoNextSlide(){
		WFTransactionWrapper_Table wrapperTable = getWFTransactionWrapper_Table();//Get this before we apply because the property will be disposed when we apply
		getValidationLayout().apply();
		if(wrapperTable != null) wrapperTable.openNextWrapper();
	}
	
	private FVButton getCancelPreviousSignatureButton(){
		if(cancelPreviousSignatureButton == null){
			cancelPreviousSignatureButton = new FVButton("Reject Previous Signature");
			cancelPreviousSignatureButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					
					OptionDialog optionDialog = new OptionDialog("Alert!", "Are you sure you want to cancel previous signature!") {
						@Override
						public boolean executeOption(String option) {
							if(option.equals("YES")){
								if(getIWorkflow() != null && getIWorkflow().iWorkflow_getWorkflow() != null){
									Workflow workflow = getIWorkflow().iWorkflow_getWorkflow();
									
									workflow.undoLastSignature();
									workflow.moveComment2LastComment();

									gotoNextSlide();
								}
							}
							return false;
						}
					};
					optionDialog.addOption("YES", "Yes");
					optionDialog.addOption("CANCEL", "Cancel");
					Globals.popupDialog(optionDialog);
					
				}
			});
		}
		return cancelPreviousSignatureButton;
	}
	
	private FVButton getRejectSignatureToButton(){
		if(rejectSignatureToButton == null){
			rejectSignatureToButton = new FVButton("Reject Signature To");
			rejectSignatureToButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					SignatureMapListWindow signatureMapListWindow = new SignatureMapListWindow();
					UI.getCurrent().addWindow(signatureMapListWindow);
					OptionDialog optionDialog = new OptionDialog("Alert!", "Are you sure you want to continue!") {
						@Override
						public boolean executeOption(String option) {
							if(option.equals("YES")){
								SignatureMapListWindow signatureMapListWindow = new SignatureMapListWindow();
								UI.getCurrent().addWindow(signatureMapListWindow);
							}
							return false;
						}
					};
					optionDialog.addOption("YES", "Yes");
					optionDialog.addOption("CANCEL", "Cancel");
					Globals.popupDialog(optionDialog);
					
				}
			});
		}
		return rejectSignatureToButton;
	}
	
	private FVButton getExitSlideShowButton(){
		if(exitSlideShowButton == null){
			exitSlideShowButton = new FVButton("Exit Slide Show");
			exitSlideShowButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					resetNextWrapperIndex();
					goBack(null);
					/*
					if(getCurrentFocXMLLayout() != null){
						getCurrentFocXMLLayout().getCentralPanel().goBack();
					}
					*/
				}
			});
		}
		return exitSlideShowButton;
	}
	
	private void resetNextWrapperIndex(){
		if(getWFTransactionWrapper_Table() != null){
			getWFTransactionWrapper_Table().resetNextWrapperIndex();
		}
	}

	private WFStage getCurrentStage(){
		WFStage currentStage = null;
		WFTransactionWrapper transactionWrapper = getTransactionWrapper();
		if(transactionWrapper != null){
			currentStage = transactionWrapper.getTransactionStage();
		}
		return currentStage;
	}
	
	private String getCurrentStageName(){
		String currentStageName = "Unsigned";
		if(getCurrentStage() != null){
			currentStageName = getCurrentStage().getName();
		}
		return currentStageName;
	}
	
	private FVLabel newStageNameLabel(){
 		return new FVLabel(getCurrentStageName());
  }
	
	public void setWFTransactionWrapper_Table(WFTransactionWrapper_Table wrapperTable){
		this.wrapperTable = wrapperTable;
	}
	
	private WFTransactionWrapper_Table getWFTransactionWrapper_Table(){
		return wrapperTable;
	}

	protected void startSlideShowAt(int rowStart){
		/*
		row = rowStart;
		WFTransactionWrapperList transList = getWFTransactionWrapperList();
		if(transList != null && transList.size() > 0){
			WFTransactionWrapper wfTransactionWrapper = (WFTransactionWrapper) transList.getFocObject(row);
			if(wfTransactionWrapper != null){
				currentWrapper = wfTransactionWrapper; 
				
				changeLayoutForNextTransactionWrapper(wfTransactionWrapper, true);
			}
		}	
		*/	
	}
	
	private HashMap<WFStage, WFLog> getStageLogMap(){
		if(stageLogMap == null){
			stageLogMap = new HashMap<WFStage, WFLog>();
			reFillMap();
		}
		return stageLogMap;
	}
	
	private void reFillMap(){
		if(getStageLogMap() != null){
			getStageLogMap().clear();
			
			Workflow workflow = getWorkflow();
			if(workflow != null){
				FocList logList = workflow.getLogList();
				
				for(int i=0; i<logList.size(); i++){
					WFLog wfLog = (WFLog) logList.getFocObject(i);
					FocUser currentUser = wfLog.getUser();
					WFStage wfStage = wfLog.getTargetStage();
					if(currentUser != null && wfLog.getEventType() == WFLogDesc.EVENT_SIGNATURE && !wfLog.getEventUndone()){
						getStageLogMap().put(wfStage, wfLog);
					}
				}
			}
		}
	}
	
	private class SignatureMapListWindow extends Window{
		
		private FVVerticalLayout verticalLayout = null;
		private FVComboBox signatureLevelComboBox = null;
		private FVButton   undoSignature = null;
		private Object     selectedWFTitle = null;
		
		public SignatureMapListWindow() {
			init();
		}
		
		private void init(){
			setWidth("500px");
			setHeight("200px");
			setModal(true);
			
			verticalLayout = new FVVerticalLayout();
			verticalLayout.addComponent(getSignatureComBox());
			verticalLayout.addComponent(getApplyButton());
			
			setContent(verticalLayout);
		}
		
		private FVComboBox getSignatureComBox(){
			if(signatureLevelComboBox == null){
				signatureLevelComboBox = new FVComboBox();
				signatureLevelComboBox.setImmediate(true);
				signatureLevelComboBox.setCaption("Rejection Stage");
				signatureLevelComboBox.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						setSelectedWFTitle(event.getProperty().getValue());
					}
				});
			}
			fillSignatuerLevelComboBox();
			return signatureLevelComboBox;
		}
		
		private void setSelectedWFTitle(Object selectedWFTitle){
			this.selectedWFTitle = selectedWFTitle;
		}
		
		private Object getSelectedWFTitle(){
			return selectedWFTitle;
		}
		
		private void fillSignatuerLevelComboBox(){
			if(signatureLevelComboBox != null){
				signatureLevelComboBox.removeAllItems();
				HashMap<WFStage, WFLog> map = getStageLogMap();
				Iterator<WFLog> iter = map.values().iterator();
				while(iter != null && iter.hasNext()){
					WFLog wfLog = iter.next();
					WFStage wfStage = wfLog.getTargetStage();
					signatureLevelComboBox.addItem(wfStage);
				}
				
				/*
				getStageLogMap().clear();
				
				Workflow workflow = getWorkflow();
				if(workflow != null){
					FocList logList = workflow.getLogList();
					
					for(int i=0; i<logList.size(); i++){
						WFLog wfLog = (WFLog) logList.getFocObject(i);
						FocUser currentUser = wfLog.getUser();
						WFStage wfStage = wfLog.getTargetStage();
						if(currentUser != null && wfLog.getEventType() == WFLogDesc.EVENT_SIGNATURE && !wfLog.getEventUndone()){
							signatureLevelComboBox.addItem(wfStage);
							getStageLogMap().put(wfStage, wfLog);
						}
					}
				}
				*/
			}
		}
		
		private FVButton getApplyButton(){
			if(undoSignature == null){
				undoSignature = new FVButton("Undo", new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						WFStage selectedWFTitle = (WFStage) getSelectedWFTitle();
						WFLog targetWFLog = getStageLogMap().get(selectedWFTitle);
						if(targetWFLog != null){
							getWorkflow().undoSignaturesTo(targetWFLog);
							SignatureMapListWindow.this.close();
							getValidationLayout().apply();
							WFTransactionWrapper_Table wrapperTable = getWFTransactionWrapper_Table();//Get this before we apply because the property will be disposed when we apply
							if(wrapperTable != null) wrapperTable.openNextWrapper();
						}
					}
				});
			}
			return undoSignature;
		}
	}
}
