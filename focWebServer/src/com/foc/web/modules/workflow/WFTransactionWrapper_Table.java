package com.foc.web.modules.workflow;

import com.foc.business.workflow.signing.WFTransactionWrapper;
import com.foc.business.workflow.signing.WFTransactionWrapperList;
import com.foc.desc.FocObject;
import com.foc.vaadin.FSignatureButton;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

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
}
