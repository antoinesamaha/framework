package com.foc.web.modules.workflow;

import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.WFLog;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.business.workflow.signing.WFSignatureNeededResult;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.FVTextArea;
import com.foc.vaadin.gui.layouts.FVForEachLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class WFConsole_Form extends FocXMLLayout {
	
	private FocXMLLayout logLayout = null;

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		logLayout = null;
	}
	
	public Workflow getWorkflow() {
		IWorkflow focObj = (IWorkflow) getFocObject();
		return focObj != null ? focObj.iWorkflow_getWorkflow() : null;
	}

	public FocList getLogList() {
		Workflow workflow = getWorkflow();
		return workflow != null ? workflow.getLogList() : null;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FocObject focObj = getFocObject();
		
		FVTextArea textArea = getTextArea();
		if(textArea != null) {
			textArea.setEnabled(true);
		}
		FVButton signButton   = getSignButton();
		FVButton rejectButton = getRejectButton();
		FVButton undoButton   = getUndoMySigButton();
		
		if(undoButton != null) {
			undoButton.setVisible(focObj != null && focObj.workflow_IsLastSignatureDoneByThisUser(true));
		}
		
		if(focObj != null && signButton != null) {
			WFSignatureNeededResult result = focObj.workflow_NeedsSignatureOfThisUser_AsTitleIndex(null);
			if(result != null && result.getTitleIndex() >= 0){
				if(result.isOnBehalfOf()){
					signButton.setCaption(isArabic() ? "امضاء بالنيابة" : "Sign PP");
					signButton.setDescription(isArabic() ? "امضاء بالنيابة وبصفة " + result.getTitle() : "Sign per procurationement as "+result.getTitle());
				}else{
					signButton.setCaption(isArabic() ? "امضاء" : "Sign");
					signButton.setDescription(isArabic() ? "امضاء وبصفة "+ result.getTitle(): "Sign as "+result.getTitle());
				}
				signButton.setVisible(true);
				rejectButton.setVisible(true);
			} else {
				signButton.setVisible(false);
				rejectButton.setVisible(false);
			}
		}
	}
	
	public FVTextArea getTextArea() {
		return (FVTextArea) getComponentByName("MY_COMMENT");
	}
	
	public FVButton getSignButton() {
		return (FVButton) getComponentByName("SIGN");
	}
	
	public FVButton getRejectButton() {
		return (FVButton) getComponentByName("REJECT");
	}
	
	public FVButton getUndoMySigButton() {
		return (FVButton) getComponentByName("UNDO_MY_SIG");
	}
	
	public String getCommentWritten() {
		FVTextArea txtArea = getTextArea();
		return txtArea != null ? txtArea.getValue() : "";
	}

	public void setCommentWritten(String comment) {
		FVTextArea txtArea = getTextArea();
		if(txtArea != null) {
			txtArea.setValue(comment);
		}
	}

	public void button_SEND_COMMENT_Clicked(FVButtonClickEvent evt){
		FocList  list     = getLogList();
		Workflow workflow = getWorkflow();
		
		FVTextArea textArea = getTextArea();
		if(list != null && textArea != null) {
			String message = textArea.getValue();
			if(!Utils.isStringEmpty(message)) {
				long refLogline = workflow.insertLogLine(WFLogDesc.EVENT_COMMENT, message);
				list.reloadFromDB();
				
				WFLog log = (WFLog) list.searchByReference(refLogline);
//  		  FocDataMap focDataMap = new FocDataMap(chat);
//	  		focDataMap.putString("TABLE_NAME", ChatJoinDesc.getInstance().getStorageName());
//				FocNotificationManager.getInstance().fireEvent(new FocNotificationEvent(FocNotificationConst.EVT_TABLE_ADD, focDataMap));
				
				if(log != null) {
	        FVForEachLayout forEachLayout = (FVForEachLayout) logLayout.getComponentByName("_BannerTable");
	        if(forEachLayout != null){
	        	forEachLayout.addBannerForFocObject(log);
	        	setCommentWritten("");
					}
				}
			}
		}
	}

	public void button_SIGN_Clicked(FVButtonClickEvent evt){
		Workflow  workflow  = getWorkflow();
		
		WFSignatureNeededResult result = getFocObject() != null ? getFocObject().workflow_NeedsSignatureOfThisUser_AsTitleIndex(null) : null;
		if(result != null){
			workflow.sign(result.getSignature(), result.getTitleIndex(), result.isOnBehalfOf(), getCommentWritten());
		}else{
			workflow.sign(getCommentWritten());
		}
		setCommentWritten("");
//			workflow.sign(getSignature(), getTitleIndex());
	}

	public void button_REJECT_Clicked(FVButtonClickEvent evt){
		OptionDialog optionDialog = new OptionDialog("Alert!", "Are you sure you want to reject all previous signatures! This will take the transaction back to the beginning of the workflow.") {
			@Override
			public boolean executeOption(String option) {
				if(option.equals("YES")){
					Workflow  workflow  = getWorkflow();
					if(workflow != null) workflow.undoAllSignatures();
					if(getValidationLayout() != null) getValidationLayout().apply();
				}
				return false;
			}
		};
		optionDialog.addOption("YES", "YES undo all signatures");
		optionDialog.addOption("CANCEL", "Cancel");
		Globals.popupDialog(optionDialog);
	}	

	public FocXMLLayout getLogLayout() {
		return logLayout;
	}

	public void setLogLayout(FocXMLLayout logLayout) {
		this.logLayout = logLayout;
	}

}
