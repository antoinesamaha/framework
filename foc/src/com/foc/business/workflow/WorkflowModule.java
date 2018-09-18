package com.foc.business.workflow;

import com.foc.Application;
import com.foc.Globals;
import com.foc.admin.FocVersion;
import com.foc.business.workflow.map.WFMapDesc;
import com.foc.business.workflow.map.WFSignatureDesc;
import com.foc.business.workflow.map.WFStageDesc;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.business.workflow.rights.RightLevelDesc;
import com.foc.business.workflow.rights.UserTransactionRightDesc;
import com.foc.desc.FocModule;
import com.foc.menu.FMenuList;

public class WorkflowModule extends FocModule {
  
	public static final String MODULE_NAME = "WORKFLOW";
	
	public static final int VERSION_ID = 156;
	
	public static final int VERSION_ID_LAST_BEFORE_FLD_NAME_CUT = 152;
	
	private WorkflowModule(){
	}
  
	public void dispose(){
	}
	
  public void declare(){
    Application app = Globals.getApp();
    FocVersion.addVersion(MODULE_NAME, "workflow 1.0", VERSION_ID);
    app.declareModule(this);
  }

  public void addApplicationMenu(FMenuList menuList) {
  }

  public void addConfigurationMenu(FMenuList menuList) {
  }
  
  public void afterAdaptDataModel() {
  }

  public void afterApplicationEntry() {
  }

  public void afterApplicationLogin() {
  }

  public void beforeAdaptDataModel() {
  }
  
	public void declareFocObjectsOnce() {
		declareFocDescClass(WFSiteDesc.class);		
		declareFocDescClass(WFTitleDesc.class);
		declareFocDescClass(WFOperatorDesc.class);
		declareFocDescClass(WFMapDesc.class);
		declareFocDescClass(WFSignatureDesc.class);
		declareFocDescClass(WFStageDesc.class);
		declareFocDescClass(WFTransactionConfigDesc.class);
		declareFocDescClass(WFFieldLockStageDesc.class);
		//declareFocDescClass(WFSuperUserDesc.class);
		
		declareFocDescClass(RightLevelDesc.class);
		declareFocDescClass(UserTransactionRightDesc.class);
		//declareFocDescClass(WFAssignFunctionalStageCorrespondanceDesc.class);
	}

  private static WorkflowModule module = null;
  public static WorkflowModule getInstance(){
    if(module == null){
      module = new WorkflowModule();
    }
    return module;
  }
}
