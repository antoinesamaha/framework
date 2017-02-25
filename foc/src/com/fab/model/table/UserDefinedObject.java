package com.fab.model.table;

import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.status.IStatusHolder;
import com.foc.business.status.StatusHolder;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.implementation.IAdrBookParty;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.business.workflow.map.WFSignature;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class UserDefinedObject extends FocObject implements IStatusHolder, IWorkflow {
	
	private StatusHolder statusHolder = null;
	private Workflow     workflow     = null;
	
	public UserDefinedObject(FocConstructor constr){
		super(constr);
		newFocProperties();
		
		if(getThisFocDesc().getFabTableDefinition().hasWorkflow()){
			if(workflow == null){
				workflow = new Workflow(this);
			}
		}
	}
	
	public void dispose(){
		super.dispose();
		if(workflow != null){
			workflow.dispose();
			workflow = null;
		}
		if(statusHolder != null){
			statusHolder.dispose();
			statusHolder = null;
		}
	}

	@Override
  public boolean hasAdrBookParty() {
  	return getThisFocDesc() != null && getThisFocDesc().getFabTableDefinition() != null && getThisFocDesc().getFabTableDefinition().hasWorkflow();
  }
	
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
		return code_getCode();
	}

	@Override
	public FPanel iWorkflow_newDetailsPanel() {
		return null;
	}

	@Override
	public boolean iWorkflow_allowSignature(WFSignature signature) {
		return true;
	}

	@Override
	public StatusHolder getStatusHolder() {
		if(statusHolder == null && getThisFocDesc().getFabTableDefinition().hasWorkflow()){
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

  @Override
  public AdrBookParty iWorkflow_getAdrBookParty() {
    return null;
  }

}
