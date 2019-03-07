package com.foc.vaadin.gui.layouts.validationLayout;

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
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.modules.workflow.Workflow_Cancel_Form;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;

@SuppressWarnings("serial")
public class FVStatusLayout_MenuBar extends MenuBar {

	private static String ITEM_TITLE_APPROVED          = "Approved";
	private static String ITEM_TITLE_RESET_STATUS      = "Reset to Proposal";
	private static String ITEM_TITLE_RESET_TO_APPROVED = "Reset to Approved";
	private static String ITEM_TITLE_CANCEL            = "Cancel";
	private static String ITEM_TITLE_CLOSE             = "Close";

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
		if(ConfigInfo.isArabic()) {
			ITEM_TITLE_APPROVED          = "موافقة";
			ITEM_TITLE_RESET_STATUS      = "أعادة المعاملة كمسودة";
			ITEM_TITLE_RESET_TO_APPROVED = "أعادة فتح المعاملة";
			ITEM_TITLE_CANCEL            = "الغاء";
			ITEM_TITLE_CLOSE             = "اغلاق";
		}
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
				FontAwesome iconResource = FontAwesome.valueOf(StatusHolderDesc.getFontAwesomeIconNameForValue(StatusHolderDesc.STATUS_APPROVED));
				getRootMenuItem().addItem(ITEM_TITLE_APPROVED, iconResource, statusMenuItemClicKListener);
			}
			if(getFocObject().workflow_IsAllowCancel()){
				FontAwesome iconResource = FontAwesome.valueOf(StatusHolderDesc.getFontAwesomeIconNameForValue(StatusHolderDesc.STATUS_CANCELED));
				getRootMenuItem().addItem(ITEM_TITLE_CANCEL, iconResource, statusMenuItemClicKListener);
			}
		}else if(status == StatusHolderDesc.STATUS_APPROVED){
			if(getFocObject().workflow_IsAllowCancel()){
				FontAwesome iconResource = FontAwesome.valueOf(StatusHolderDesc.getFontAwesomeIconNameForValue(StatusHolderDesc.STATUS_CANCELED));
				getRootMenuItem().addItem(ITEM_TITLE_CANCEL, iconResource, statusMenuItemClicKListener);
			}
			if(getFocObject().workflow_IsAllowClose()){
				FontAwesome iconResource = FontAwesome.valueOf(StatusHolderDesc.getFontAwesomeIconNameForValue(StatusHolderDesc.STATUS_CLOSED));
				getRootMenuItem().addItem(ITEM_TITLE_CLOSE, iconResource, statusMenuItemClicKListener);
			}
			if(getFocObject().workflow_IsAllowResetToProposal()){
				FontAwesome iconResource = FontAwesome.valueOf(StatusHolderDesc.getFontAwesomeIconNameForValue(StatusHolderDesc.STATUS_PROPOSAL));
				getRootMenuItem().addItem(ITEM_TITLE_RESET_STATUS, iconResource, statusMenuItemClicKListener);
			}
		}else if(status == StatusHolderDesc.STATUS_CLOSED || status == StatusHolderDesc.STATUS_CANCELED) {
			if(getFocObject().workflow_IsAllowResetToApproved()){
				FontAwesome iconResource = FontAwesome.valueOf(StatusHolderDesc.getFontAwesomeIconNameForValue(StatusHolderDesc.STATUS_APPROVED));
				getRootMenuItem().addItem(ITEM_TITLE_RESET_TO_APPROVED, iconResource, statusMenuItemClicKListener);
			}
		}
	}
	
	private void selectCurrentStatus() {
		FProperty property = focObject != null ? (FProperty) focObject.iFocData_getDataByPath(StatusHolderDesc.FNAME_STATUS) : null;
		if(property != null && getRootMenuItem() != null){
			FontAwesome iconResource = null;
			String iconName = StatusHolderDesc.getFontAwesomeIconNameForValue(property.getInteger());
			if(!Utils.isStringEmpty(iconName)) {
				iconResource = FontAwesome.valueOf(iconName);
			}
			if(getRootMenuItem().getSize() <= 0) {
				getRootMenuItem().addItem(property.getString(), iconResource, null);
			}
			getRootMenuItem().setText(property.getString());
			if(iconResource != null) {
				getRootMenuItem().setIcon(iconResource);
			}
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
