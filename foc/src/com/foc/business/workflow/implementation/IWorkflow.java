package com.foc.business.workflow.implementation;

import java.sql.Date;

import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.map.WFSignature;
import com.foc.gui.FPanel;

public interface IWorkflow extends ILoggable {
	public Workflow     iWorkflow_getWorkflow();
	public WFSite       iWorkflow_getComputedSite();
	public String       iWorkflow_getCode();
	public Date         iWorkflow_getDate();	
	public String       iWorkflow_getDescription();
	public FPanel       iWorkflow_newDetailsPanel();
	public boolean      iWorkflow_allowSignature(WFSignature signature);
}
