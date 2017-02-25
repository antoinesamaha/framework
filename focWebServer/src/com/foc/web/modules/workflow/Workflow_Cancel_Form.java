package com.foc.web.modules.workflow;

import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolder;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.desc.FocObject;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout_ComboBox;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout_MenuBar;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class Workflow_Cancel_Form extends FocXMLLayout{

	protected FVStatusLayout_MenuBar   fvStatusLayout_ComboBox = null;
	protected FVStatusLayout            fvStatusLayout         = null;
	protected WFTransactionWrapper_Form transactionWrapperForm = null;
	
	/*public FVStatusLayout getStatusLayout(){
		return fvStatusLayout;
	}
	
	public void setStatusLayout(FVStatusLayout fvStatusLayout){
		this.fvStatusLayout = fvStatusLayout;
	}*/
	
	public FVStatusLayout_MenuBar getStatusLayout(){
		return fvStatusLayout_ComboBox;
	}
	
	public void setStatusLayout(FVStatusLayout_MenuBar fvStatusLayout_ComboBox){
		this.fvStatusLayout_ComboBox = fvStatusLayout_ComboBox;
	}
	
	public IWorkflow getIWorkflow(){
		return (IWorkflow) getFocData();
	}
	
	public Workflow getWorkflow(){
		return getIWorkflow().iWorkflow_getWorkflow();
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		addCancelListener();
		addDoNotCancelListener();
	}
	
	public void addCancelListener(){
	  FVButton cancelTransaction = (FVButton) getComponentByName("YES_CANCEL_TRANSACTION");
	  
	  cancelTransaction.addClickListener(new ClickListener() {
		  public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		  	copyGuiToMemory();
		  	cancelTransactionExecution();
		  	goBack(null);
		  }
	  });	
	}
	
	public void addDoNotCancelListener(){
	  FVButton dontCancel = (FVButton) getComponentByName("NO_DONT_CANCEL");
	  
	  dontCancel.addClickListener(new ClickListener() {
		  public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		  	goBack(null);
		  }
	  });	
	}
	
  public void cancelTransactionExecution(){
  	FocObject focObj = getFocObject();
  	if(focObj != null && focObj instanceof IStatusHolder){
  		StatusHolder statusHolder = ((IStatusHolder)focObj).getStatusHolder();
    	if(statusHolder != null){
    		statusHolder.setStatusToCanceled(getWorkflow().getCancelReason());
    	}
      focObj.validate(true);
  	}
  	if(getStatusLayout() != null){
  		getStatusLayout().cancelTransaction();
  	}
  	if(getTransactionWrapperForm() != null){
  		getTransactionWrapperForm().gotoNextSlide();
  	}
  }

	public WFTransactionWrapper_Form getTransactionWrapperForm() {
		return transactionWrapperForm;
	}

	public void setTransactionWrapperForm(WFTransactionWrapper_Form transactionWrapperForm) {
		this.transactionWrapperForm = transactionWrapperForm;
	}
}
