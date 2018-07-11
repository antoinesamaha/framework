package com.foc.web.modules.workflow;

import com.foc.business.workflow.signing.WFTransactionWrapper;
import com.foc.business.workflow.signing.WFTransactionWrapperDesc;
import com.foc.business.workflow.signing.WFTransactionWrapperList;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FSignatureButton;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

@SuppressWarnings("serial")
public class WFTransactionWrapper_Table extends FocXMLLayout{

	private int rowOpened = -1;

	@Override
	public void dispose() {
		super.dispose();
		rowOpened = -1;
	}
	
	public WFTransactionWrapperList getWFTransactionWrapperList(){
		return (WFTransactionWrapperList) getFocData();
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		createSlideShowButton();
		markAsDirty();
	}
	
	private void createSlideShowButton(){
		FVButton button = (FVButton) getComponentByName("SIGN_IN_SLIDE_SHOW");
		if(button != null){
			button.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					openNextWrapper();
				}
			});
		}
	}
	
	public FVTableWrapperLayout getTableWrapperLayout(){
		return (FVTableWrapperLayout) getComponentByName("WFTRANSACTION_WRAPPER_TABLE");
	}
	
	@Override
	public ICentralPanel table_OpenItem(String tableName, ITableTree table, FocObject focObject, int viewContainer_Open) {
		if(rowOpened == -1){
			WFTransactionWrapperList transList = getWFTransactionWrapperList();
			for(int i=0; i<transList.size(); i++){
				WFTransactionWrapper wfTransactionWrapper = (WFTransactionWrapper) transList.getFocObject(i);
				if(wfTransactionWrapper != null){
					if(wfTransactionWrapper.equalsRef(focObject)){
						rowOpened = i;
						break;
					}
				}
			}
		}
		
		ICentralPanel centralPanel = super.table_OpenItem(tableName, table, focObject, viewContainer_Open);
		if(centralPanel != null && centralPanel instanceof WFTransactionWrapper_Form){
			WFTransactionWrapper_Form currentWrapper_Form = (WFTransactionWrapper_Form) centralPanel;
			currentWrapper_Form.setWFTransactionWrapper_Table(WFTransactionWrapper_Table.this);
		}
		
		return centralPanel;
	}
	
	public void refreshSignatureCountButton(){
		if(getMainWindow() != null && getMainWindow() instanceof FocWebVaadinWindow){
			FocWebVaadinWindow focWebVaadinWindow = (FocWebVaadinWindow) getMainWindow();
			FSignatureButton nativeButton = focWebVaadinWindow.getPendingSignatureButton();
			if(nativeButton != null) {
				nativeButton.reset(getWFTransactionWrapperList());
			}
		}
	}
	
	public void resetNextWrapperIndex(){
		rowOpened = -1;
	}
	
	public void openNextWrapper(){
		boolean incrementRow = true;
		if(rowOpened >=0 && rowOpened < getWFTransactionWrapperList().size()){
			WFTransactionWrapper previousWrapper = (WFTransactionWrapper) getWFTransactionWrapperList().getFocObject(rowOpened);
			FocObject focObj = (FocObject) previousWrapper.getWorkflow();
			if(!focObj.workflow_NeedsSignatureOfThisUser()){
				getWFTransactionWrapperList().remove(previousWrapper);
				incrementRow = false;
				refreshSignatureCountButton();				
			}
		}
		if(incrementRow) rowOpened++;
		if(rowOpened < getWFTransactionWrapperList().size()){
			WFTransactionWrapper wfTransactionWrapper = (WFTransactionWrapper) getWFTransactionWrapperList().getFocObject(rowOpened);
			if(getTableWrapperLayout() != null && getTableWrapperLayout().getTableTreeDelegate() != null){
				getTableWrapperLayout().getTableTreeDelegate().open(wfTransactionWrapper);
			}
		}
	}
	
	@Override
	public ColumnGenerator table_getGeneratedColumn(String tableName, final FVTableColumn tableColumn) {
		ColumnGenerator columnGenerator = null;
		if (     tableColumn != null 
				&&   tableColumn.getName() != null
				&&  (tableColumn.getName().equals("SIGN") || tableColumn.getName().equals("REJECT"))) {
			columnGenerator = new ColumnGenerator() {
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					long objId = (Long) itemId;
					
					FocList list = getFocList();
					WFTransactionWrapper transaction = (WFTransactionWrapper) list.searchByReference(objId);
					
					if (transaction != null) {
						boolean isReject = columnId.equals("REJECT");
						SignRejectButton button = new SignRejectButton(transaction, isReject);
						String compName = TableTreeDelegate.newComponentName("WFTRANSACTION_WRAPPER_TABLE", String.valueOf(objId), tableColumn.getName());
						putComponent(compName, button);
						return button;
					}
					
					return null;
				}
			};
		}

		return columnGenerator;
	}

	protected void rejectAllClicked(WFTransactionWrapper wrapper) {
//		if(wrapper != null) wrapper.undoAllSignatures();
		XMLViewKey key = new XMLViewKey(WFTransactionWrapperDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, "Comment", XMLViewKey.VIEW_DEFAULT);
		WFTransactionWrapper_Comment_Standard_Form transferComplaintForm = (WFTransactionWrapper_Comment_Standard_Form) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, wrapper);
		if(transferComplaintForm != null) {
			transferComplaintForm.setIsSigning(false);
			transferComplaintForm.popupInDialog();
		}
	}
	
	protected void signClicked(WFTransactionWrapper wrapper) {
//		if(wrapper != null) wrapper.sign();
		XMLViewKey key = new XMLViewKey(WFTransactionWrapperDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, "Comment", XMLViewKey.VIEW_DEFAULT);
		WFTransactionWrapper_Comment_Standard_Form transferComplaintForm = (WFTransactionWrapper_Comment_Standard_Form) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, wrapper);
		if(transferComplaintForm != null) {
			transferComplaintForm.setIsSigning(true);
			transferComplaintForm.popupInDialog();
		}
	}
	
	protected void refreshAfterButtonClick() {
	}
	
	public class SignRejectButton extends FVButton {

		private WFTransactionWrapper wrapper = null;
		private boolean reject = false;
		
		public SignRejectButton(WFTransactionWrapper wrapper, boolean isReject) {
			super(" مواققة ");
			reject = isReject;
			if(isReject) { 
				setCaption(" رفض ");
				setIcon(FontAwesome.TIMES);
			} else {
				setIcon(FontAwesome.CHECK_CIRCLE);
			}
			this.wrapper = wrapper;
			setWidth("100%");
			addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					if(reject) {
						rejectAllClicked(wrapper);
					} else {
						signClicked(wrapper);
					}
//					refresh();
				}
			});
		}
		
		public void dispose() {
			super.dispose();
			wrapper = null;
		}
	}
}
