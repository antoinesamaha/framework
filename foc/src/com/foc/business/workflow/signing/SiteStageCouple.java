package com.foc.business.workflow.signing;

import com.foc.business.department.Department;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.map.WFStage;

public class SiteStageCouple{

	private WFSite     site       = null;
	private Department department = null;
	private WFStage    stage      = null;
	
	public SiteStageCouple(WFSite site, Department department, WFStage stage) {
		this.site       = site;
		this.stage      = stage;
		this.department = department;
	}
	
	public WFSite getSite(){
		return site;
	}
	
	public WFStage getStage(){
		return stage;
	}
	
	public Department getDepartment(){
		return department;
	}
}