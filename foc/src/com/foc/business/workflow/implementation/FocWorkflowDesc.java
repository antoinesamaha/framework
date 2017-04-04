package com.foc.business.workflow.implementation;

import com.foc.business.status.IStatusHolderDesc;
import com.foc.business.status.StatusHolderDesc;
import com.foc.business.workflow.WFSiteDesc;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;

public abstract class FocWorkflowDesc extends FocDesc implements IStatusHolderDesc, IWorkflowDesc {

  private static final int FLD_STATUS_SHIFT          = 1;
  private static final int FLD_CREATION_DATE_SHIFT   = 2;
  private static final int FLD_VALIDATION_DATE_SHIFT = 3;
  private static final int FLD_CLOSURE_DATE_SHIFT    = 4;
  private static final int FLD_CREATION_USER_SHIFT   = 5;
  private static final int FLD_SITE_1_SHIFT          = 6;

	private StatusHolderDesc statusHolderDesc = null;
	
	public FocWorkflowDesc(Class focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique) {
		this(focObjectClass, dbResident, storageName, isKeyUnique, true);
	}
	
	public FocWorkflowDesc(Class focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique, boolean withWorkfllow) {
		super(focObjectClass, dbResident, storageName, isKeyUnique);
		
		if(withWorkfllow){
			initWorkflow();
		}
	}

	public void initWorkflow(){
		addSite1Field();
		
    statusHolderDesc = new StatusHolderDesc(this);
    statusHolderDesc.addFields();
    
    workflowDesc = new WorkflowDesc(this);
    workflowDesc.addFields();
	}
	
	protected void addSite1Field(){
    FField focFld = WFSiteDesc.newSiteField(this, FField.FNAME_SITE, iWorkflow_getFieldID_ForSite_1(), FField.NO_FIELD_ID);
    addField(focFld);
	}
	
  //-----------------
  //IStatusHolderDesc
  //-----------------
  
  @Override
  public StatusHolderDesc getStatusHolderDesc() {
    return statusHolderDesc;
  }

  @Override
  public int getFLD_STATUS() {
    return iWorkflow_getFieldIDShift() + WorkflowDesc.FLD_LAST_RESERVED + FLD_STATUS_SHIFT;
  }

  @Override
  public int getFLD_CREATION_DATE() {
    return iWorkflow_getFieldIDShift() + WorkflowDesc.FLD_LAST_RESERVED + FLD_CREATION_DATE_SHIFT;
  }

  @Override
  public int getFLD_VALIDATION_DATE() {
    return iWorkflow_getFieldIDShift() + WorkflowDesc.FLD_LAST_RESERVED + FLD_VALIDATION_DATE_SHIFT;
  }

  @Override
  public int getFLD_CLOSURE_DATE() {
    return iWorkflow_getFieldIDShift() + WorkflowDesc.FLD_LAST_RESERVED + FLD_CLOSURE_DATE_SHIFT;
  }

  @Override
  public int getFLD_CREATION_USER() {
    return iWorkflow_getFieldIDShift() + WorkflowDesc.FLD_LAST_RESERVED + FLD_CREATION_USER_SHIFT;
  }

  //-----------------
  //IWorkflowDesc
  //-----------------
  
//  @Override
//  public int iWorkflow_getFieldIDShift() {
//    return FLD_WORKFLOW_SHIFT;
//  }

  @Override
  public int iWorkflow_getFieldID_ForSite_1() {
  	return iWorkflow_getFieldIDShift() + WorkflowDesc.FLD_LAST_RESERVED + FLD_SITE_1_SHIFT;
  }

  @Override
  public int iWorkflow_getFieldID_ForSite_2() {
    return FField.NO_FIELD_ID;
  }

  @Override
  public WorkflowDesc iWorkflow_getWorkflowDesc() {
    return workflowDesc;
  }

//  @Override
//  public String iWorkflow_getDBTitle() {
//    return "CheckDeposit";
//  }

  @Override
  public String iWorkflow_getTitle() {
    return iWorkflow_getDBTitle();
  }

  @Override
  public String iWorkflow_getSpecificAdditionalWhere() {
    return null;//"("+FNAME_TYPE+"="+getTransactionType()+")";
  }
  
  @Override
	public int iWorkflow_getApprovalMethod() {
  	return WFTransactionConfigDesc.APPROVAL_METHOD_BY_WORKFLOW;
	}
}
