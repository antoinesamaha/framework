package com.foc.business.workflow;

import java.util.ArrayList;

import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.business.workflow.implementation.WorkflowDesc;
import com.foc.desc.FocDesc;

public class WorkflowTransactionFactory {
	ArrayList<IWorkflowDesc> workflowTransactionArray = null;
	
	public WorkflowTransactionFactory(){
		workflowTransactionArray = new ArrayList<IWorkflowDesc>();
	}
	
	public void dispose(){
		if(workflowTransactionArray != null){
			workflowTransactionArray.clear();
			workflowTransactionArray = null;
		}
	}

	public IWorkflowDesc findWorkflowDescForDBName(String storageName){
		IWorkflowDesc foundDesc = null;
		for(int i=0; i<WorkflowTransactionFactory.getInstance().getFocDescCount() && foundDesc == null; i++){
			IWorkflowDesc iWorkflowDesc = WorkflowTransactionFactory.getInstance().getIWorkflowDesc(i);
			if(iWorkflowDesc != null && iWorkflowDesc.iWorkflow_getWorkflowDesc() != null){
				WorkflowDesc workflowDesc = iWorkflowDesc.iWorkflow_getWorkflowDesc();
				String iworkflowDescName = workflowDesc.getFocDesc() != null ? workflowDesc.getFocDesc().getStorageName() : null;
				if(iworkflowDescName != null && iworkflowDescName.equals(storageName)){
					foundDesc = iWorkflowDesc;
				}
			}
  	}
		return foundDesc;
	}
	
	public IWorkflowDesc findWorkflowDesc_ByDBTitle(String className){
		IWorkflowDesc foundDesc = null;
		for(int i=0; i<WorkflowTransactionFactory.getInstance().getFocDescCount() && foundDesc == null; i++){
			IWorkflowDesc iWorkflowDesc = WorkflowTransactionFactory.getInstance().getIWorkflowDesc(i);
			if(iWorkflowDesc != null && iWorkflowDesc.iWorkflow_getWorkflowDesc() != null){
				String dbTitle = iWorkflowDesc.iWorkflow_getDBTitle();
				if(dbTitle.equals(className)){
					foundDesc = iWorkflowDesc;
				}
			}
  	}
		return foundDesc;
	}
	
	public int getFocDescCount(){
		return workflowTransactionArray.size();
	}
	
	public FocDesc getFocDescAt(int i){
		return (FocDesc) workflowTransactionArray.get(i);
	}
	
	public IWorkflowDesc getIWorkflowDesc(int i){
		return (IWorkflowDesc) workflowTransactionArray.get(i);
	}
	
	public void add(IWorkflowDesc iWorkflowDesc){
		workflowTransactionArray.add(iWorkflowDesc);
	}
	
	private static WorkflowTransactionFactory instance = null;
	public static WorkflowTransactionFactory getInstance(){
		if(instance == null){
			instance = new WorkflowTransactionFactory();
		}
		return instance;
	}
}
