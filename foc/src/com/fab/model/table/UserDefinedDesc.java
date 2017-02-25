package com.fab.model.table;

import com.foc.business.status.IStatusHolderDesc;
import com.foc.business.status.StatusHolderDesc;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.business.workflow.implementation.WorkflowDesc;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.FocModule;
import com.foc.list.FocList;

public class UserDefinedDesc extends FocDesc implements IStatusHolderDesc, IWorkflowDesc {

	public static final int FLD_ID_SHIFT        = 3000;
	public static final int FLD_SITE_1          = 2900;
	public static final int FLD_SITE_2          = 2901;
	public static final int FLD_STATUS          = 2902;
	public static final int FLD_CREATION_DATE   = 2903;
	public static final int FLD_VALIDATION_DATE = 2904;
	public static final int FLD_CLOSURE_DATE    = 2905;
	public static final int FLD_CREATION_USER   = 2906;
	
	private StatusHolderDesc statusHolderDesc = null; 
	
	public UserDefinedDesc(FocModule module, TableDefinition tableDefinition) {
		super(UserDefinedObject.class, tableDefinition.isDBResident(), tableDefinition.getName(), tableDefinition.isKeyUnique());
		if(tableDefinition.isWithReference()){
			addReferenceField();
		}
		setGuiBrowsePanelClass(UserDefinedObjectGuiBrowsePanel.class);
		setGuiDetailsPanelClass(UserDefinedObjectGuiDetailsPanel.class);
		
		setFabTableDefinition(tableDefinition);
		setModule(module);
	}

	@Override
	public void addFieldsFromTableDefinition(TableDefinition tableDefinition, FocList fieldsList){
		super.addFieldsFromTableDefinition(tableDefinition, fieldsList);

		if(tableDefinition != null && tableDefinition.hasWorkflow()){
	    statusHolderDesc = new StatusHolderDesc(this);
	    statusHolderDesc.addFields();
	    
	    workflowDesc = new WorkflowDesc(this);
	    workflowDesc.addFields();
		}
	}
	
	@Override
	public int iWorkflow_getFieldIDShift() {
		return FLD_ID_SHIFT;
	}

	@Override
	public int iWorkflow_getFieldID_ForSite_1() {
		return FLD_SITE_1;
	}

	@Override
	public int iWorkflow_getFieldID_ForSite_2() {
		return FLD_SITE_2;
	}

	@Override
	public WorkflowDesc iWorkflow_getWorkflowDesc() {
		return workflowDesc;
	}

	@Override
	public String iWorkflow_getDBTitle() {
		return getFabTableDefinition() != null ? getFabTableDefinition().getName() : null;
	}

	@Override
	public String iWorkflow_getTitle() {
		return getFabTableDefinition() != null ? getFabTableDefinition().getTitle() : null;
	}

	@Override
	public String iWorkflow_getSpecificAdditionalWhere() {
		return null;
	}

	@Override
	public StatusHolderDesc getStatusHolderDesc() {
		return statusHolderDesc;
	}

	@Override
	public int getFLD_STATUS() {
		return FLD_STATUS;
	}

	@Override
	public int getFLD_CREATION_DATE() {
		return FLD_CREATION_DATE;
	}

	@Override
	public int getFLD_VALIDATION_DATE() {
		return FLD_VALIDATION_DATE;
	}

	@Override
	public int getFLD_CLOSURE_DATE() {
		return FLD_CLOSURE_DATE;
	}

	@Override
	public int getFLD_CREATION_USER() {
		return FLD_CREATION_USER;
	}

	@Override
	public String iWorkflow_getCodePrefix() {
		return "USRDEF{YY}-";
	}

	@Override
	public String iWorkflow_getCodePrefix_ForProforma() {
		return "USRDEF---";
	}
	
	@Override
	public int iWorkflow_getApprovalMethod() {
  	return WFTransactionConfigDesc.APPROVAL_METHOD_BY_WORKFLOW;
	}
}
