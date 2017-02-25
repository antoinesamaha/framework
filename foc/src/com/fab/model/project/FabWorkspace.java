package com.fab.model.project;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class FabWorkspace extends FocObject {
	
	public FabWorkspace(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public String getWorkspacePath(){
		return getPropertyString(FabWorkspaceDesc.FLD_WORKSPACE_PATH);
	}
	
	public void setWorkspacePath(String projectPath){
		setPropertyString(FabWorkspaceDesc.FLD_WORKSPACE_PATH, projectPath);
	}
}

