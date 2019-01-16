package com.foc.vaadin.gui.layouts.validationLayout;

import java.util.List;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolder;
import com.foc.business.status.StatusHolderDesc;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.business.workflow.map.WFTransactionConfig;
import com.foc.desc.FocObject;
import com.foc.property.FProperty;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.modules.workflow.Workflow_Cancel_Form;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.MenuBar;

@SuppressWarnings("serial")
public class FVStatusLayout_MenuBar extends MenuBar {

	private final static String ITEM_TITLE_APPROVED          = "Approved";
	private final static String ITEM_TITLE_RESET_STATUS      = "Reset to Proposal";
	private final static String ITEM_TITLE_RESET_TO_APPROVED = "Reset to Approved";
	private final static String ITEM_TITLE_CANCEL            = "Cancel";
	private final static String ITEM_TITLE_CLOSE             = "Close";

	private FocXMLLayout xmlLayout = null;
	private FocObject    focObject = null;
	private MenuItem     rootMenuItem = null;
	private OptionDialog optionDialog = null;

	public FVStatusLayout_MenuBar(FocXMLLayout xmlLayout, FocObject focObject) {
		setFocObject(focObject);
		this.xmlLayout = xmlLayout;
		init();
		selectCurrentStatus();
	}

	private void init() {
		fillStatusMenuBar();
	}

	public void dispose() {
		rootMenuItem = null;
		focObject = null;
		xmlLayout = null;
		optionDialog = null;
	}

	private void setOptionDialog(OptionDialog optionDialog) {
		this.optionDialog = optionDialog;
	}

	public OptionDialog getOptionDialog() {
		return this.optionDialog;
	}

	private void fillStatusMenuBar() {
		FProperty property = (FProperty) getFocObject().iFocData_getDataByPath(StatusHolderDesc.FNAME_STATUS);
		int status = property.getInteger();

		WFTransactionConfig transConfig = getFocObject().workflow_getTransactionConfig();
		boolean hasMapForSignatures = transConfig != null ? transConfig.isApproveByMapSignature() : false;

		StatusMenuItemClickListener statusMenuItemClicKListener = new StatusMenuItemClickListener();

		if(status == StatusHolderDesc.STATUS_PROPOSAL){
			if(getFocObject().workflow_IsAllowApprove() && !hasMapForSignatures){
				getRootMenuItem().addItem(ITEM_TITLE_APPROVED, statusMenuItemClicKListener);
			}
			if(getFocObject().workflow_IsAllowCancel()){
				getRootMenuItem().addItem(ITEM_TITLE_CANCEL, statusMenuItemClicKListener);
			}
		}else if(status == StatusHolderDesc.STATUS_APPROVED){
			if(getFocObject().workflow_IsAllowCancel()){
				getRootMenuItem().addItem(ITEM_TITLE_CANCEL, statusMenuItemClicKListener);
			}
			if(getFocObject().workflow_IsAllowClose()){
				getRootMenuItem().addItem(ITEM_TITLE_CLOSE, statusMenuItemClicKListener);
			}
			if(getFocObject().workflow_IsAllowResetToProposal()){
				getRootMenuItem().addItem(ITEM_TITLE_RESET_STATUS, statusMenuItemClicKListener);
			}
		}else if(status == StatusHolderDesc.STATUS_CLOSED || status == StatusHolderDesc.STATUS_CANCELED) {
			if(getFocObject().workflow_IsAllowResetToApproved()){
				getRootMenuItem().addItem(ITEM_TITLE_RESET_TO_APPROVED, statusMenuItemClicKListener);
			}
		}
//		if(allowModification && FocWebApplication.getFocUser().getGroup().allowStatusManualModif()){
	}

	private void selectCurrentStatus() {
		FProperty property = focObject != null ? (FProperty) focObject.iFocData_getDataByPath(StatusHolderDesc.FNAME_STATUS) : null;
		if(property != null){
			getRootMenuItem().setText(property.getString());
			addStatusIfNeededAndSelectIt(property.getString());
		}
	}
	
	private void addStatusIfNeededAndSelectIt(String status){
		List<MenuItem> menuItemsList = getRootMenuItem() != null ? getRootMenuItem().getChildren() : null;
		if(menuItemsList == null && status != null && !status.isEmpty()){
			getRootMenuItem().addItem(status, null);
		}
	}

	private void setFocObject(FocObject focObject) {
		this.focObject = focObject;
	}

	private FocObject getFocObject() {
		return focObject;
	}

	public StatusHolder getStatusHolder() {
		return getIStatusHolder().getStatusHolder();
	}

	public IStatusHolder getIStatusHolder() {
		return (IStatusHolder) focObject;
	}

	private class StatusMenuItemClickListener implements Command {

		@Override
		public void menuSelected(MenuItem selectedItem) {
			String menuItemTitle = selectedItem.getText();
			if(menuItemTitle != null && !menuItemTitle.isEmpty()){
				if(menuItemTitle.equals(ITEM_TITLE_APPROVED)){
					approve();
				}else if(menuItemTitle.equals(ITEM_TITLE_CANCEL)){
					cancel();
				}else if(menuItemTitle.equals(ITEM_TITLE_CLOSE)){
					close();
				}else if(menuItemTitle.equals(ITEM_TITLE_RESET_STATUS)){
					resetToProposal();
				}else if(menuItemTitle.equals(ITEM_TITLE_RESET_TO_APPROVED)){
					resetToApproved();					
				}
				refreshStatusMenuBar();

				// getVali
				// refreshPendingSignatureButtonCaption((ICentralPanel)getWindow());

			}
		}
	}

	public FocCentralPanel getWindow() {
		return findAncestor(FocCentralPanel.class);
	}

	public void cancelTransaction() {
		// if(getStatusHolder() != null){
		// getStatusHolder().setStatusToCanceled(comment);
		// }
		// if(getFocObject() != null){
		// getFocObject().validate(true);
		// }
		if(xmlLayout != null && xmlLayout.getValidationLayout() != null){
			xmlLayout.getValidationLayout().commit();
		}
		refreshStatusMenuBar();
	}

	public void refreshStatusMenuBar() {
		getRootMenuItem().removeChildren();
		selectCurrentStatus();
		fillStatusMenuBar();
		if(xmlLayout != null && xmlLayout.getValidationLayout() != null){
			xmlLayout.getValidationLayout().refreshPendingSignatureButtonCaption(null);
		}
	}

	public void approve() {
		boolean isArabic = ConfigInfo.isArabic();
		OptionDialog dialog = new OptionDialog(isArabic ? "تأكيد الموافقة" : "Approve Confirmation", isArabic ? "انت اكيد من الموافقة على المعاملة؟" : "Are you sure you want to approve this transaction?") {

			@Override
			public boolean executeOption(String optionName) {
				if(optionName != null){
					if(optionName.equals("APPROVE")){
						getStatusHolder().setStatusToValidated();
						getFocObject().validate(true);
						if(xmlLayout != null) {
							xmlLayout.copyMemoryToGui();
							xmlLayout.getValidationLayout().commit();
						}
						refreshStatusMenuBar();
						xmlLayout.re_parseXMLAndBuildGui();						
					}else if(optionName.equals("CANCEL")){
						selectCurrentStatus();
					}
				}
				return false;
			}
		};
		dialog.addOption("APPROVE", isArabic ? "موافقة" : "Yes Approve");
		dialog.addOption("CANCEL", isArabic ? "كلا" : "No Cancel");
		dialog.setWidth("400px");
		dialog.setHeight("180px");
		dialog.popup();
		setOptionDialog(dialog);// For Unit Testing
	}

	public void cancel() {
		popupCancel(getWindow(), FVStatusLayout_MenuBar.this, getFocObject(), xmlLayout);
	}

	public static void popupCancel(FocCentralPanel mainWindow, FVStatusLayout_MenuBar statusLayout_MenuBar, FocObject focObject, FocXMLLayout focXMLLayout) {
		if(focObject != null && focObject.getThisFocDesc() instanceof IWorkflowDesc){
			XMLViewKey key = new XMLViewKey("IWorkflow", XMLViewKey.TYPE_FORM, WorkflowWebModule.CTXT_CANCEL_TRANSACTION, XMLViewKey.VIEW_DEFAULT);
			Workflow_Cancel_Form centralPanel = (Workflow_Cancel_Form) XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, focObject);
			if(centralPanel != null) {
				centralPanel.popupInDialog();
				centralPanel.setFocXMLLayout(focXMLLayout);
			}
			
			/*
			XMLViewKey xmlKey = new XMLViewKey("IWorkflow", XMLViewKey.TYPE_FORM, WorkflowWebModule.CTXT_CANCEL_TRANSACTION, XMLViewKey.VIEW_DEFAULT);
			Workflow_Cancel_Form centralPanel = (Workflow_Cancel_Form) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(mainWindow, xmlKey, (IFocData) iworkflow);
			centralPanel.setStatusLayout(statusLayout_MenuBar);
			centralPanel.setTransactionWrapperForm(transactionWrapperForm);
			centralPanel.parseXMLAndBuildGui();

			FocCentralPanel centralWindow = new FocCentralPanel();
			centralWindow.fill();
			centralWindow.changeCentralPanelContent(centralPanel, false);

			Window window = centralWindow.newWrapperWindow();
			// window.setWidth("500px");
			// window.setHeight("300px");
			window.setPositionX(200);
			window.setPositionY(100);
			FocWebApplication.getInstanceForThread().addWindow(window);
			*/
		}
	}

	public void close() {
		boolean isArabic = ConfigInfo.isArabic();
		OptionDialog dialog = new OptionDialog(isArabic ? "تاكيد الاغلاق" : "Close Confirmation", isArabic ? "انت اكيد من اغلاق المعاملة؟" :"Are you sure you want to close this transaction"+"?") {

			@Override
			public boolean executeOption(String optionName) {
				if(optionName != null){
					if(optionName.equals("CLOSE")){
						xmlLayout.getValidationLayout().saveAndRefreshWithoutGoBack();
						getStatusHolder().setStatusToClosed();
						getStatusHolder().setClosureDate(Globals.getDBManager().getCurrentTimeStamp_AsTime());					
						getFocObject().validate(true);
						
						refreshStatusMenuBar();
						xmlLayout.re_parseXMLAndBuildGui();
					}else if(optionName.equals("CANCEL")){
						selectCurrentStatus();
					}
				}
				return false;
			}
		};
		dialog.addOption("CLOSE", isArabic ? "اغلاق" : "Yes Close");
		dialog.addOption("CANCEL", isArabic ? "كلا" : "No Cancel");
		dialog.setWidth("400px");
		dialog.setHeight("180px");
		dialog.popup();
		setOptionDialog(dialog);// For Unit Testing
	}

	public void resetToProposal() {
		boolean isArabic = ConfigInfo.isArabic();
		OptionDialog dialog = new OptionDialog(isArabic ? "تأكيد أعادة المعاملة كمسودة" : "Reset to Proposal Confirmation", isArabic ? "انت اكيد من أعادة المعاملة كمسودة؟" : "Reset this transaction to proposal?") {

			@Override
			public boolean executeOption(String optionName) {
				if(optionName != null){
					if(optionName.equals("RESET")){
						boolean error = getStatusHolder().resetStatusToProposal();
						if(!error){
							getFocObject().validate(true);
							xmlLayout.getValidationLayout().commit();
						}
						refreshStatusMenuBar();
						xmlLayout.re_parseXMLAndBuildGui();
					}else if(optionName.equals("CANCEL")){
						selectCurrentStatus();
					}
				}
				return false;
			}
		};
		dialog.addOption("RESET", isArabic ? "أعادة المعاملة كمسودة" : "Yes Reset");
		dialog.addOption("CANCEL", isArabic ? "كلا" : "No Cancel");
		dialog.setWidth("400px");
		dialog.setHeight("180px");
		dialog.popup();
		setOptionDialog(dialog);
	}

	public void resetToApproved() {
		boolean isArabic = ConfigInfo.isArabic();
		OptionDialog dialog = new OptionDialog(isArabic ? "تأكيد أعادة فتح المعاملة" : "Reset to Approved Confirmation", isArabic ? "انت اكيد من أعادة فتح المعاملة؟" : "Reset this transaction to approved?") {

			@Override
			public boolean executeOption(String optionName) {
				if(optionName != null){
					if(optionName.equals("RESET")){
						boolean error = getStatusHolder().resetStatusToApproved();
						if(!error){
							getFocObject().validate(true);
							xmlLayout.getValidationLayout().commit();
						}
						refreshStatusMenuBar();
						xmlLayout.re_parseXMLAndBuildGui();
					}else if(optionName.equals("CANCEL")){
						selectCurrentStatus();
					}
				}
				return false;
			}
		};
		dialog.addOption("RESET", isArabic ? "أعادة فتح المعاملة" : "Yes Reset");
		dialog.addOption("CANCEL", isArabic ? "كلا": "No Cancel");
		dialog.setWidth("400px");
		dialog.setHeight("180px");
		dialog.popup();
		setOptionDialog(dialog);
	}

	public MenuItem getRootMenuItem() {
		if(rootMenuItem == null){
			rootMenuItem = addItem("Status", null);
		}
		return rootMenuItem;
	}
}
