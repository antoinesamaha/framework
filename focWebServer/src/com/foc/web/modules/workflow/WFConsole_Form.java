package com.foc.web.modules.workflow;

import com.foc.Globals;
import com.foc.IFocEnvironment;
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
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.FVTextArea;
import com.foc.vaadin.gui.layouts.FVForEachLayout;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Alignment;

@SuppressWarnings("serial")
public class WFConsole_Form extends FocXMLLayout {
	
	private FocXMLLayout  logLayout     = null ;
	private ICentralPanel centralPanel  = null ;
	private boolean       forceHideSignCancel = false;

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		logLayout = null;
	}
	
	public ICentralPanel getFocXMLLayout() {
		return centralPanel;
	}

	public void setFocXMLLayout(ICentralPanel centralPanel) {
		this.centralPanel = centralPanel;
		adjustButtonsVisibility();
	}

	private WFTransactionWrapper_Form getTransactionWrapperForm() {
		return (centralPanel instanceof WFTransactionWrapper_Form) ? (WFTransactionWrapper_Form) centralPanel : null; 
	}

	protected void applyForm() {
		if(centralPanel != null) {
			FVValidationLayout vLay = centralPanel.getValidationLayout();
			if(vLay != null) vLay.apply();
		}
	}
	
	protected boolean gotoNextSlide() {
		boolean error = true;
		WFTransactionWrapper_Form transForm = getTransactionWrapperForm();
		if(transForm != null) {
			error = false;
			transForm.gotoNextSlide();
		}
		return error;
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
		
		FVTextArea textArea = getTextArea();
		if(textArea != null) {
			textArea.setEnabled(true);
		}
		adjustButtonsVisibility();
	}
	
	public void adjustButtonsVisibility() {
		FocObject focObj = getFocObject();
		
		FVButton signButton   = getSignButton();
		FVButton rejectButton = getRejectButton();
		FVButton undoButton   = getUndoMySigButton();
		FVButton nextButton   = getNextButton();
		
		if(undoButton != null) {
			undoButton.setVisible(focObj != null && focObj.workflow_IsLastSignatureDoneByThisUser(true));
		}
		
		if(nextButton != null) {
			nextButton.setVisible(getTransactionWrapperForm() != null);
		}
		
		if(focObj != null && signButton != null) {
			WFSignatureNeededResult result = focObj.workflow_NeedsSignatureOfThisUser_AsTitleIndex(null);
			if(result != null && result.getTitleIndex() >= 0){
				if(result.isOnBehalfOf()){
					signButton.setCaption(isArabic() ? "موافقة بالنيابة" : "Sign PP");
					signButton.setDescription(isArabic() ? "موافقة بالنيابة وبصفة " + result.getTitle() : "Sign per procurationement as "+result.getTitle());
				}else{
					signButton.setCaption(isArabic() ? "موافقة" : "Sign");
					signButton.setDescription(isArabic() ? "موافقة وبصفة "+ result.getTitle(): "Sign as "+result.getTitle());
				}
				signButton.setVisible(!isForceHideSignCancel());
				rejectButton.setVisible(!isForceHideSignCancel());
			} else {
				signButton.setVisible(false);
				rejectButton.setVisible(false);
			}
		}
		
		//When Arabic and the 3 buttons are visible make the undo my signature bigger 
		if(isArabic() && undoButton != null && signButton != null && rejectButton != null
				&& undoButton.isVisible() && signButton.isVisible() && rejectButton.isVisible()) {
			FVHorizontalLayout hLay = (FVHorizontalLayout) getComponentByName("_BUTTONS_HORIZONTAL_LAYOUT");
			hLay.setComponentAlignment(signButton, Alignment.BOTTOM_RIGHT);
			signButton.setWidth("-1px");
			hLay.setComponentAlignment(rejectButton, Alignment.BOTTOM_RIGHT);
			rejectButton.setWidth("-1px");
			hLay.setComponentAlignment(undoButton, Alignment.BOTTOM_RIGHT);
			hLay.setExpandRatio(undoButton, 1);
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
	
	public FVButton getNextButton() {
		return (FVButton) getComponentByName("NEXT");
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
	
	private void popupCommentsAreEmptyMessage() {
		String notificationMessage = "Please type a message";
		if(isArabic()) notificationMessage = "يرجى ادخال الملاحظات";
		Globals.showNotification(notificationMessage, "", IFocEnvironment.TYPE_WARNING_MESSAGE);
	}

	public void button_SEND_COMMENT_Clicked(FVButtonClickEvent evt){
		FocList  list     = getLogList();
		Workflow workflow = getWorkflow();
		
		if(list != null) {
			String message = getCommentWritten();
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
			} else {
				popupCommentsAreEmptyMessage();
			}
		}
	}

	public void button_SIGN_Clicked(FVButtonClickEvent evt){
		Workflow  workflow  = getWorkflow();
		
		String error = null;
		FocXMLLayout xmlLayout = getFocXMLLayout() instanceof FocXMLLayout ? (FocXMLLayout) getFocXMLLayout() : null;
		if(xmlLayout != null) {
			xmlLayout.copyGuiToMemory();
			error = xmlLayout.beforeSigning();
		}
		
		if(Utils.isStringEmpty(error)) {
			WFSignatureNeededResult result = getFocObject() != null ? getFocObject().workflow_NeedsSignatureOfThisUser_AsTitleIndex(null) : null;
			if(result != null){
				workflow.sign(result.getSignature(), result.getTitleIndex(), result.isOnBehalfOf(), getCommentWritten());
			}else{
				workflow.sign(getCommentWritten());
			}
			setCommentWritten("");
			if(xmlLayout != null) xmlLayout.afterSigning();
			
			if(gotoNextSlide()) {
				applyForm();
			}
		} else {
			Globals.showNotification(error, "", IFocEnvironment.TYPE_WARNING_MESSAGE);
		}
		
//			workflow.sign(getSignature(), getTitleIndex());
	}

	public void button_REJECT_Clicked(FVButtonClickEvent evt){
		String commentWritten = getCommentWritten();
		if(!Utils.isStringEmpty(commentWritten)) {
			String message = "Are you sure you want to reject all previous signatures! "
	                   + "This will take the transaction back to the beginning of the workflow.";
			String title = "Alert!";
			String yesCaption = "YES undo all signatures";
			String cancelCaption = "Cancel";
			if(isArabic()) {
				message = "هل تريد فعلا الغاء كل الموافقات السابقة واعادة المعاملة الى البدأ؟";
				title = "تنبيه"+"!";
				yesCaption = "نعم اريد الغاء الموافقات";
				cancelCaption = "لا اريد";
			}
	
			OptionDialog optionDialog = new OptionDialog(title, message) {
				@Override
				public boolean executeOption(String option) {
					if(option.equals("YES")){
						Workflow  workflow  = getWorkflow();
						if(workflow != null) workflow.undoAllSignatures(getCommentWritten());
						if(gotoNextSlide()) {
							applyForm();
						}
					}
					return false;
				}
			};
			optionDialog.addOption("YES", yesCaption);
			optionDialog.addOption("CANCEL", cancelCaption);
			Globals.popupDialog(optionDialog);
		} else {
			popupCommentsAreEmptyMessage();
		}
	}	

	public void button_UNDO_MY_SIG_Clicked(FVButtonClickEvent evt){
		String message = "Are you sure you want to undo your signature?";
		String title = "Alert!";
		String yesCaption = "YES undo my last signature";
		String cancelCaption = "Cancel";
		if(isArabic()) {
			message = "هل تريد فعلا الغاء موافقتك الاخيرة؟";
			title = "تنبيه"+"!";
			yesCaption = "نعم اريد الغاء موافقتي الاخيرة";
			cancelCaption = "لا اريد";
		}
		
		OptionDialog optionDialog = new OptionDialog(title, message) {
			@Override
			public boolean executeOption(String option) {
				if(option.equals("YES")){
					Workflow  workflow  = getWorkflow();
					if(workflow != null) workflow.undoLastSignature(getCommentWritten());
					if(gotoNextSlide()) {
						applyForm();
					}
				}
				return false;
			}
		};
		optionDialog.addOption("YES", yesCaption);
		optionDialog.addOption("CANCEL", cancelCaption);
		Globals.popupDialog(optionDialog);
	}

	public void button_NEXT_Clicked(FVButtonClickEvent evt){
		gotoNextSlide();
	}
	
	public FocXMLLayout getLogLayout() {
		return logLayout;
	}

	public void setLogLayout(FocXMLLayout logLayout) {
		this.logLayout = logLayout;
	}

	public boolean isForceHideSignCancel() {
		return forceHideSignCancel;
	}

	public void setForceHideSignCancel(boolean forceHideSignCancel) {
		this.forceHideSignCancel = forceHideSignCancel;
		adjustButtonsVisibility();
	}

}
