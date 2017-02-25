package com.foc.business.workflow.implementation;

import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolder;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.map.WFSignature;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.gui.FPanel;

public abstract class FocWorkflowObject extends FocObject implements IWorkflow, IStatusHolder {

  private StatusHolder     statusHolder     = null;
  private Workflow         workflow         = null;

	public FocWorkflowObject(FocConstructor constr) {
		super(constr);
		if(isWorkflowActivatedInFocDesc()){
			workflow = new Workflow(this);
		}
	}

  public void dispose(){
    super.dispose();
    if(statusHolder != null){
      statusHolder.dispose();
      statusHolder = null;
    }
    if(workflow != null){
      workflow.dispose();
      workflow = null;
    }
  }
  
  public boolean isWorkflowActivatedInFocDesc(){
  	return workflow_IsWorkflowSubject();
  }
  
  public WFSite getSite(){
  	WFSite site = null;
  	if(workflow != null){
  		site = workflow.getSite_1();
  	}
  	return site;
  }

  public void setSite(WFSite site){
  	if(workflow != null){
  		workflow.setSite_1(site);
  	}
  }
  
  //----------------------------------
  //IStatusHolder
  //----------------------------------
  
  @Override
  public StatusHolder getStatusHolder() {
    if(statusHolder == null && (isWorkflowActivatedInFocDesc())){
      statusHolder = new StatusHolder(this);
    }
    return statusHolder;
  }

  @Override
  public boolean allowSettingStatusTo(int newStatus) {
    return true;
  }

  @Override
  public void afterSettingStatusTo(int newStatus) {
  }
  
  //----------------------------------
  //IWorkflow
  //----------------------------------
  
  @Override
  public Workflow iWorkflow_getWorkflow() {
    return workflow;
  }

  @Override
  public WFSite iWorkflow_getComputedSite() {
    return WFTransactionConfigDesc.getDefaultArea_ForTransaction(this);
  }

  @Override
  public String iWorkflow_getCode() {
    return code_getCode();
  }

  @Override
  public String iWorkflow_getDescription() {
    return "";
  }

  @Override
  public AdrBookParty iWorkflow_getAdrBookParty() {
    return null;
  }

  @Override
  public FPanel iWorkflow_newDetailsPanel() {
    return newDetailsPanel(FocObject.DEFAULT_VIEW_ID);
  }

  @Override
  public boolean iWorkflow_allowSignature(WFSignature signature) {
    return true;
  }
}
