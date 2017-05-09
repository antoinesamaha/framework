package com.foc.business.workflow.map;

import com.foc.business.company.Company;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WorkflowTransactionFactory;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WFTransactionConfig extends FocObject {
	
	public WFTransactionConfig(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
  public void setCompany(Company company){
  	super.setCompany(company);
  }

	public void setTransactionDBTitle(String dbTitle){
		setPropertyString(WFTransactionConfigDesc.FLD_TRANSACTION, dbTitle);
	}

	public String getTransactionDBTitle(){
		return getPropertyString(WFTransactionConfigDesc.FLD_TRANSACTION);
	}
	
	public void setWorkflowMap(WFMap map){
		setPropertyObject(WFTransactionConfigDesc.FLD_MAP, map);
	}

	public WFMap getWorkflowMap(){
		return (WFMap) getPropertyObject(WFTransactionConfigDesc.FLD_MAP);
	}
	
	public WFSite getDefaultArea(){
		return (WFSite) getPropertyObject(WFTransactionConfigDesc.FLD_DEFAULT_AREA);
	}

	public int getApprovalMethod(){
		return getPropertyInteger(WFTransactionConfigDesc.FLD_APPROVAL_METHOD);
	}

	public void setApprovalMethod(int method){
		setPropertyInteger(WFTransactionConfigDesc.FLD_APPROVAL_METHOD, method);
	}

	public boolean isApproveOnCreation(){
		return getApprovalMethod() == WFTransactionConfigDesc.APPROVAL_METHOD_AT_CREATION;
	}

	public void setApproveOnCreation(){
		setApprovalMethod(WFTransactionConfigDesc.APPROVAL_METHOD_AT_CREATION);
	}

	public boolean isApproveByMapSignature(){
		return getApprovalMethod() == WFTransactionConfigDesc.APPROVAL_METHOD_BY_WORKFLOW && getWorkflowMap() != null;
	}

	public boolean isPromtForApproveUponValidation(){
		return getPropertyBoolean(WFTransactionConfigDesc.FLD_PROMPT_FOR_APPROVE_UPON_VALIDATION);
	}

	public boolean isLeaveCodeEmpty(){
		return getPropertyBoolean(WFTransactionConfigDesc.FLD_LEAVE_CODE_EMPTY);
	}

	public boolean isIncludeProjectCode(){
		return getPropertyBoolean(WFTransactionConfigDesc.FLD_INCLUDE_PROJECT_CODE);
	}

	public boolean isCodeBySITE(){
		return getPropertyBoolean(WFTransactionConfigDesc.FLD_CODE_BY_STE);
	}

	public boolean codePrefix_isUseSitePrefix(){
		return getPropertyBoolean(WFTransactionConfigDesc.FLD_CODE_PREFIX_USE_SITE_PREFIX);
	}

	public String codePrefix_getTransactionPrefix(){
		return getPropertyString(WFTransactionConfigDesc.FLD_CODE_PREFIX_TRANSACTION_PREFIX);
	}

	public void codePrefix_setTransactionPrefix(String prefix){
		setPropertyString(WFTransactionConfigDesc.FLD_CODE_PREFIX_TRANSACTION_PREFIX, prefix);
	}

	public String codePrefix_getTransactionPrefixForProposal(){
		return getPropertyString(WFTransactionConfigDesc.FLD_CODE_PREFIX_TRANSACTION_PREFIX_PROPOSAL);
	}

	public void codePrefix_setTransactionPrefixForProposal(String codePrefix){
		setPropertyString(WFTransactionConfigDesc.FLD_CODE_PREFIX_TRANSACTION_PREFIX_PROPOSAL, codePrefix);
	}
	
	public int codePrefix_getNbrOfDigits(){
		return getPropertyInteger(WFTransactionConfigDesc.FLD_CODE_PREFIX_NUMBER_OF_DIGITS);
	}
	
	public void codePrefix_setNbrOfDigits(int nbrDigits){
		setPropertyInteger(WFTransactionConfigDesc.FLD_CODE_PREFIX_NUMBER_OF_DIGITS, nbrDigits);
	}

	public String getTransactionTitle(){
		return getPropertyString(WFTransactionConfigDesc.FLD_TRANSACTION_TITLE);
	}

	public String getTransactionTitle_Proposal(){
		return getPropertyString(WFTransactionConfigDesc.FLD_TRANSACTION_TITLE_PROPOSAL);
	}
	
	public boolean doNotPrintTheProposalTag(){
		return !getTransactionTitle().isEmpty() && !getTransactionTitle().equals(getTransactionTitle_Proposal());
	}

	public FocList getFucntionalStageList(){
		FocList list = getPropertyList(WFTransactionConfigDesc.FLD_FUNCTIONAL_STAGE_LIST);
		list.setDirectlyEditable(true);
		list.setDirectImpactOnDatabase(false);
		return list;
	}
	
	public FocList getFieldLockStageList(){
		FocList list = getPropertyList(WFTransactionConfigDesc.FLD_WF_FIELD_LOCK_STAGE_LIST);
		list.setDirectlyEditable(false);
		list.setDirectImpactOnDatabase(true);
		return list;
	}
	
	public IWorkflowDesc getWorkflowDesc(){
		IWorkflowDesc workflowDesc = null;
		for(int i=0; i<WorkflowTransactionFactory.getInstance().getFocDescCount() && workflowDesc == null; i++){
			IWorkflowDesc currentWorkflowDesc = WorkflowTransactionFactory.getInstance().getIWorkflowDesc(i);
			if(currentWorkflowDesc != null){
				String dbTitle = currentWorkflowDesc.iWorkflow_getDBTitle();
				if(dbTitle.equals(getTransactionDBTitle())){
					workflowDesc = currentWorkflowDesc;
				}
			}
		}
		return workflowDesc;
	}
}
