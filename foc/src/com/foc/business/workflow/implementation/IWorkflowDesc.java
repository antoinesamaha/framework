package com.foc.business.workflow.implementation;

public interface IWorkflowDesc extends ILoggableDesc {
	public WorkflowDesc iWorkflow_getWorkflowDesc();
	public int          iWorkflow_getFieldID_ForSite_1();
	public int          iWorkflow_getFieldID_ForSite_2();
	public String       iWorkflow_getDBTitle();
	public String       iWorkflow_getTitle();
	public String       iWorkflow_getSpecificAdditionalWhere();
	public String       iWorkflow_getCodePrefix();
	public String       iWorkflow_getCodePrefix_ForProforma();
	public int          iWorkflow_getCode_NumberOfDigits();
	public int          iWorkflow_getApprovalMethod();
	//public void         iWorkflow_fillFunctionalStagesArray(WFFunctionalStagesArray functionalArray);
}
