package com.foc.web.modules.workflow;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolder;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.desc.FocObject;
import com.foc.util.Utils;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout_MenuBar;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;

@SuppressWarnings("serial")
public class Workflow_Cancel_Form extends FocXMLLayout{

	protected FVStatusLayout_MenuBar    fvStatusLayout_ComboBox = null;
	protected FVStatusLayout            fvStatusLayout          = null;
	protected WFTransactionWrapper_Form transactionWrapperForm  = null;
	private   FocXMLLayout              focXMLLayout            = null;	
	
	@Override
	public void dispose() {
		super.dispose();
		
		fvStatusLayout_ComboBox = null;
		fvStatusLayout          = null;
		transactionWrapperForm  = null;
		focXMLLayout            = null;
	}
	
	public FocXMLLayout getFocXMLLayout() {
		return focXMLLayout;
	}

	public void setFocXMLLayout(FocXMLLayout focXMLLayout) {
		this.focXMLLayout = focXMLLayout;
	}

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
	
	/*
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		addCancelListener();
		addDoNotCancelListener();
	}
	
	public void addCancelListener(){
	  FVButton cancelTransaction = (FVButton) getComponentByName("YES_CANCEL_TRANSACTION");
	  
	  if(cancelTransaction != null) {
		  cancelTransaction.addClickListener(new ClickListener() {
			  public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
			  	copyGuiToMemory();
			  	cancelTransactionExecution();
			  	goBack(null);
			  }
		  });	
	  }
	}
	
	public void addDoNotCancelListener(){
	  FVButton dontCancel = (FVButton) getComponentByName("NO_DONT_CANCEL");
	  
	  if(dontCancel != null) {
		  dontCancel.addClickListener(new ClickListener() {
			  public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
			  	goBack(null);
			  }
		  });
	  }
	}
	public WFTransactionWrapper_Form getTransactionWrapperForm() {
		return transactionWrapperForm;
	}
	
	public void setTransactionWrapperForm(WFTransactionWrapper_Form transactionWrapperForm) {
		this.transactionWrapperForm = transactionWrapperForm;
	}
	*/
	
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
//  	if(getTransactionWrapperForm() != null){
//  		getTransactionWrapperForm().gotoNextSlide();
//  	}
  }

	@Override
	public void showValidationLayout(boolean showBackButton, int position) {
		super.showValidationLayout(showBackButton, position);
		FVValidationLayout vLay = getValidationLayout();
		if(vLay != null) {
			vLay.addValidationListener(new IValidationListener() {
				
				@Override
				public void validationDiscard(FVValidationLayout validationLayout) {
				}
				
				@Override
				public boolean validationCommit(FVValidationLayout validationLayout) {
					return false;
				}
				
				@Override
				public boolean validationCheckData(FVValidationLayout validationLayout) {
					boolean error = false;
					Workflow portalComplaint = getWorkflow();
					if(portalComplaint != null) {
						error = Utils.isStringEmpty(portalComplaint.getCancelReason());
						if(error) {
							Globals.showNotification("يرجى ادخال سبب الالغاء", "", IFocEnvironment.TYPE_WARNING_MESSAGE);
						} else {
							cancelTransactionExecution();
//							PortalCompStatus status = PortalCompStatusDesc.getInstance().findStatusByID(PortalCompStatus.STATUS_CANCELLED);
//							portalComplaint.setComplaintStatus(status);
						}
					}
					return error;
				}
				
				@Override
				public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
					if(commited) {
						if(getFocXMLLayout() != null) {
							getFocXMLLayout().re_parseXMLAndBuildGui();
							if(getFocXMLLayout().getValidationLayout() != null) {
								FVStatusLayout_MenuBar statusLayout = getFocXMLLayout().getValidationLayout().getStatusLayout(false);
								if(statusLayout != null) {
									statusLayout.refreshStatusMenuBar();
								}
							}
						}
					}
				}
			});
		}
	}

}
