package com.foc.web.modules.restmodule;
 
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class RESTModule extends FocWebModule {
	
  public static final int VERSION_ID = 1000;
	
	public static final String MODULE_NAME = "RESTModule";

	public static final String MNU_REST_API_UTILS   = "MNU_INCIDENT_MAIN";
	
	public RESTModule() {
		super(MODULE_NAME, "REST Management", "com.foc.web.modules.restmodule", "com.foc.web.modules.restmodule.gui", "REST Module 1.0", VERSION_ID);
	}
	
	@Override
	public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
		
		FocMenuItem mainMenu = menuTree.pushRootMenu(MNU_REST_API_UTILS, "REST API Utils");	

		FocMenuItem menuItem = mainMenu.pushMenu("MNU_GUEST_TOKENS", "Login Tokens");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				LoginTokenList list = new LoginTokenList();
				list.loadIfNotLoadedFromDB();

				XMLViewKey key = new XMLViewKey(LoginToken.DBNAME, XMLViewKey.TYPE_TABLE);
				INavigationWindow myNavigationWindow = (INavigationWindow) navigationWindow;
				FocXMLLayout table = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(myNavigationWindow, key, list);
				myNavigationWindow.changeCentralPanelContent(table, true);
				table.setFocDataOwner(true);
			}
		});
		
	}

	@Override
	public void beforeAdaptDataModel() {
		super.beforeAdaptDataModel();
	
//		FocVersion version = FocVersion.getDBVersionForModule(MODULE_NAME);
		
//		if (version == null || version.getId() < VERSION_ID_AUTOPOPULATE) {
//			autopopulateIncidentType = true;
//		} else {
//			autopopulateIncidentType = false;
//		}
		
	}

}
