package com.foc.web.modules.workflow;

import com.foc.business.company.CompanyDesc;
import com.foc.business.company.UserCompanyRightsDesc;
import com.foc.business.workflow.WFFieldLockStageDesc;
import com.foc.business.workflow.WFOperatorDesc;
import com.foc.business.workflow.WFSiteDesc;
import com.foc.business.workflow.WFSiteTree;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.business.workflow.map.WFMapDesc;
import com.foc.business.workflow.map.WFSignatureDesc;
import com.foc.business.workflow.map.WFStageDesc;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.business.workflow.rights.RightLevelDesc;
import com.foc.business.workflow.rights.UserTransactionRightDesc;
import com.foc.business.workflow.signing.WFTransactionWrapperDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.workflow.gui.COMPANY_FirstLogin_Standard_Form;
import com.foc.web.modules.workflow.gui.COMPANY_Form;
import com.foc.web.modules.workflow.gui.COMPANY_Saas_Standard_Form;
import com.foc.web.modules.workflow.gui.ACC_DEPARTMENT_Selection_Standard_Tree;
import com.foc.web.modules.workflow.gui.TRANSACTION_FILTER_Form;
import com.foc.web.modules.workflow.gui.WF_STAGE_Form;
import com.foc.web.modules.workflow.gui.WF_STAGE_Table;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class WorkflowWebModule extends FocWebModule {

	public static final String CTXT_DEPARTMENT_SELECTION       = "Selection";
	public static final String CTXT_CANCEL_TRANSACTION         = "Cancel Transaction";
	public static final String CTXT_WF_COMMENT_LOG             = "WFCommentLog";
	public static final String CTXT_WF_NO_WORKFLOW__LOG_ONLY   = "NoWorkflow";
	public static final String CTXT_TRANSACTION_HISTORY        = "TRANSACTION_HISTORY";
	public static final String CTXT_FIRST_LOGIN                = "FirstLogin";
	public static final String CTXT_SITE_SELECTION             = "SITE_SELECTION";
	public static final String CTXT_TITLE_SELECTION            = "TITLE_SELECTION";
	public static final String CTXT_SAAS                       = "Saas";
	public static final String STORAGE_NAME_TRANSACTION_FILTER = "TRANSACTION_FILTER";
	public static final String STORAGE_NAME_WORKFLOW_CONSOLE   = "WFConsole";
	public static final String MODULE_NAME                     = "SITES_AND_WORKFLOW";
	
  public WorkflowWebModule(){
    super(MODULE_NAME, "Sites and Workflow configuration");
    setAdminConsole(true);
  }
  
  public void declareXMLViewsInDictionary() {
  	scanGuiPackage("com.foc.web.modules.workflow.gui");
  	
  	/*
  	XMLViewDictionary.getInstance().put(
      DepartmentDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TREE,
      CTXT_DEPARTMENT_SELECTION,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/Department_Selection_Tree.xml", 0, ACC_DEPARTMENT_Selection_Standard_Tree.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
			STORAGE_NAME_TRANSACTION_FILTER,
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/TransactionFilter_Form.xml", 0, TRANSACTION_FILTER_Form.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
      WFLogDesc.WF_LOG_VIEW_KEY,
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      false, "ar",
      "/xml/com/foc/workflow/WFLog_Table-ar.xml", 0, WFLog_Table.class.getName());
  	
    XMLViewDictionary.getInstance().put(
      WFLogDesc.WF_LOG_VIEW_KEY,
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      false, "ar",
      "/xml/com/foc/workflow/WFLog_Form-ar.xml", 0, WFLog_Form.class.getName());
    
  	XMLViewDictionary.getInstance().put(
      WFLogDesc.WF_LOG_VIEW_KEY,
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFLog_Table.xml", 0, WFLog_Table.class.getName());
    	
    XMLViewDictionary.getInstance().put(
      WFLogDesc.WF_LOG_VIEW_KEY,
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFLog_Form.xml", 0, WFLog_Form.class.getName());
      
  	XMLViewDictionary.getInstance().put(
      WFLogDesc.WF_LOG_VIEW_KEY,
      XMLViewKey.TYPE_TABLE,
      CTXT_WF_COMMENT_LOG,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFLog_Comment_Log_Table.xml", 0, WFLog_Comment_Log_Table.class.getName());
      
		XMLViewDictionary.getInstance().put(
      WFLogDesc.WF_LOG_VIEW_KEY,
      XMLViewKey.TYPE_TABLE,
      "Banner",
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFLog_Banner_Standard_Table.xml", 0, WFLog_Banner_Standard_Table.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
      WFLogDesc.WF_LOG_VIEW_KEY,
      XMLViewKey.TYPE_FORM,
      "Banner",
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFLog_Banner_Standard_Form.xml", 0, WFLog_Banner_Standard_Form.class.getName());
  	  	
  	XMLViewDictionary.getInstance().put(
  		STORAGE_NAME_WORKFLOW_CONSOLE,
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFConsole_Form.xml", 0, WFConsole_Form.class.getName());

  	XMLViewDictionary.getInstance().put(
  		STORAGE_NAME_WORKFLOW_CONSOLE,
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      false,"ar",
      "/xml/com/foc/workflow/WFConsole_Form-ar.xml", 0, WFConsole_Form.class.getName());
  	*/
  	
  	XMLViewDictionary.getInstance().put(
    		WFFieldLockStageDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/workflow/WFFieldLockStage_Table.xml", 0, WFFieldLockStage_Table.class.getName());

  	XMLViewDictionary.getInstance().put(
      "IWorkflow",
      XMLViewKey.TYPE_FORM,
      CTXT_CANCEL_TRANSACTION,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/Workflow_Cancel_Form.xml", 0, Workflow_Cancel_Form.class.getName());

  	XMLViewDictionary.getInstance().put(
      "IWorkflow",
      XMLViewKey.TYPE_FORM,
      CTXT_CANCEL_TRANSACTION,
      XMLViewKey.VIEW_DEFAULT, false, "ar",
      "/xml/com/foc/workflow/Workflow_Cancel_Form-ar.xml", 0, Workflow_Cancel_Form.class.getName());

  	XMLViewDictionary.getInstance().put(
  		WFFieldLockStageDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFFieldLockStage_Form.xml", 0, WFFieldLockStage_Form.class.getName());
      
    XMLViewDictionary.getInstance().put(
  		WFTransactionWrapperDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTransactionWrapper_Form-ar.xml", 0, WFTransactionWrapper_Form.class.getName());
    
    XMLViewDictionary.getInstance().put(
  		WFTransactionWrapperDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      "Comment",
      XMLViewKey.VIEW_DEFAULT,false,"ar",
      "/xml/com/foc/workflow/WFTransactionWrapper^Comment^Standard^Form-ar.xml", 0, WFTransactionWrapper_Comment_Standard_Form.class.getName());
    
    XMLViewDictionary.getInstance().put(
  		WFTransactionWrapperDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      "Comment",
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTransactionWrapper^Comment^Standard^Form.xml", 0, WFTransactionWrapper_Comment_Standard_Form.class.getName());

    XMLViewDictionary.getInstance().put(
  		WFTransactionWrapperDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTransactionWrapper_Table.xml", 0, WFTransactionWrapper_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
  		WFTransactionWrapperDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      "Dashboard",
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTransactionWrapper_Dashboard_Standard_Table.xml", 0, WFTransactionWrapper_Dashboard_Standard_Table.class.getName());

    XMLViewDictionary.getInstance().put(
  		WFTransactionWrapperDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      "Dashboard",
      XMLViewKey.VIEW_DEFAULT,false,"ar",
      "/xml/com/foc/workflow/WFTransactionWrapper_Dashboard_Standard_Table-ar.xml", 0, WFTransactionWrapper_Dashboard_Standard_Table.class.getName());

    XMLViewDictionary.getInstance().put(
  		WFTransactionWrapperDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      CTXT_TRANSACTION_HISTORY,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTransactionWrapper_TransactionHistory_Table.xml", 0, WFTransactionWrapper_TransactionHistory_Table.class.getName());
    
//    XMLViewDictionary.getInstance().put(
//      CompanyDesc.getInstance().getStorageName(),
//      XMLViewKey.TYPE_TABLE,
//      XMLViewKey.CONTEXT_DEFAULT,
//      XMLViewKey.VIEW_DEFAULT,
//      "/xml/com/foc/workflow/Company_Table.xml", 0, null);
//
//    XMLViewDictionary.getInstance().put(
//      CompanyDesc.getInstance().getStorageName(),
//      XMLViewKey.TYPE_FORM,
//      XMLViewKey.CONTEXT_DEFAULT,
//      XMLViewKey.VIEW_DEFAULT,
//      "/xml/com/foc/workflow/Company_Form.xml", 0, Company_Form.class.getName());
    
//    XMLViewDictionary.getInstance().put(
//      CompanyDesc.getInstance().getStorageName(),
//      XMLViewKey.TYPE_FORM,
//      CTXT_SAAS,
//      XMLViewKey.VIEW_DEFAULT,
//      "/xml/com/foc/workflow/Company_Saas_Form.xml", 0, Company_Saas_Form.class.getName());
//    
//    XMLViewDictionary.getInstance().put(
//        CompanyDesc.getInstance().getStorageName(),
//        XMLViewKey.TYPE_FORM,
//        CTXT_FIRST_LOGIN,
//        XMLViewKey.VIEW_DEFAULT,
//        "/xml/com/foc/workflow/Company_FirstLogin_Form.xml", 0, Company_FirstLogin_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      UserCompanyRightsDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/UserCompanyRights_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      UserCompanyRightsDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/UserCompanyRights_Form.xml", 0, UserCompanyRights_Form.class.getName());
  	
    XMLViewDictionary.getInstance().put(
      WFSiteDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TREE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFSite_Table.xml", 0, null);
    
    XMLViewDictionary.getInstance().put(
      WFSiteDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      CTXT_SITE_SELECTION,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFSite_SiteSelection_Table.xml", 0, WFSite_SiteSelection_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      WFSiteDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFSite_Form.xml", 0, FocXMLLayout.class.getName());

    XMLViewDictionary.getInstance().put(
      WFSiteDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      "With Warehouses",
      "/xml/com/foc/workflow/WFSite_WithStockWarehouseList_Form.xml", 0, FocXMLLayout.class.getName());

    XMLViewDictionary.getInstance().put(
      WFTitleDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTitle_Table.xml", 0, FocXMLLayout.class.getName());
    
    XMLViewDictionary.getInstance().put(
      WFTitleDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      CTXT_TITLE_SELECTION,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTitle_TitleSelection_Table.xml", 0, WFTitle_TitleSelection_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      WFOperatorDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFOperator_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      WFOperatorDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFOperator_Form.xml", 0, null);

    /*
    XMLViewDictionary.getInstance().put(
      WFMapDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFMap_Table.xml", 0, WF_MAP_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      WFMapDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFMap_Form.xml", 0, WF_MAP_Form.class.getName());
      
    XMLViewDictionary.getInstance().put(
      WFStageDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFStage_Table.xml", 0, WF_STAGE_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      WFStageDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFStage_Form.xml", 0, WF_STAGE_Form.class.getName());
      
    XMLViewDictionary.getInstance().put(
  		WFSignatureDesc.getInstance().getStorageName(),
  		XMLViewKey.TYPE_TABLE,
  		XMLViewKey.CONTEXT_DEFAULT,
  		XMLViewKey.VIEW_DEFAULT,
  		"/xml/com/foc/workflow/WFSignature_Table.xml", 0, WFSignature_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      WFTransactionConfigDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTransactionConfig_Table.xml", 0, WFTransactionConfig_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
      WFTransactionConfigDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTransactionConfig_Form.xml", 0, WFTransactionConfig_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      WFTransactionConfigDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/WFTransactionConfig_Form.xml", 0, null);
     */

    XMLViewDictionary.getInstance().put(
      RightLevelDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/RightLevel_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      RightLevelDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/RightLevel_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      UserTransactionRightDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/UserTransactionRight_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      UserTransactionRightDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/workflow/UserTransactionRight_Form.xml", 0, null);

  }

  public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
  	FocMenuItem companyMenu  = menuTree.pushRootMenu("MNU_COMPANY", "Company structure");
    FocMenuItem workflowMenu = menuTree.pushRootMenu("MNU_WORKFLOW", "Workflow");
    FocMenuItem menuItem = null;    
    
    menuItem = companyMenu.pushMenu("COMPANY_TABLE", "Company");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = CompanyDesc.getList(FocList.LOAD_IF_NEEDED);
        mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
    
    menuItem = companyMenu.pushMenu("SITE_TABLE", "Site");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        
        WFSiteTree siteTree = WFSiteTree.newInstance();
        XMLViewKey xmlViewKey = new XMLViewKey(WFSiteDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE);
        ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) mainWindow, xmlViewKey, siteTree);
        mainWindow.changeCentralPanelContent(centralPanel, true);
      }
    });

    menuItem = workflowMenu.pushMenu("WF_TITLE_TABLE", "Title");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = WFTitleDesc.getList(FocList.LOAD_IF_NEEDED);
        mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = workflowMenu.pushMenu("WF_MAP_TABLE", "Workflow Sequence Map");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = WFMapDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = workflowMenu.pushMenu("WF_STAGE_TABLE", "Workflow Stage");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = WFStageDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = workflowMenu.pushMenu("WF_TRANSACTION_CONFIG_TABLE", "Transaction Config");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = WFTransactionConfigDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = workflowMenu.pushMenu("WF_RIGHT_LEVEL_TABLE", "Right Level");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = RightLevelDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
  }
}