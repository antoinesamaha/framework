package com.foc.business.workflow.implementation;

import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.map.WFSignature;
import com.foc.gui.FPanel;

public interface IWorkflow extends IAdrBookParty{
	public Workflow     iWorkflow_getWorkflow();
	public WFSite       iWorkflow_getComputedSite();
	public String       iWorkflow_getCode();
	public String       iWorkflow_getDescription();
	public FPanel       iWorkflow_newDetailsPanel();
	public boolean      iWorkflow_allowSignature(WFSignature signature);
}
