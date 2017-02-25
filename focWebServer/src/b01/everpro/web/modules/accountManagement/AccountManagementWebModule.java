package b01.everpro.web.modules.accountManagement;

import com.foc.admin.FocUserDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.admin.FocUser_Saas_Form;
import com.foc.web.modules.admin.FocUser_Saas_Table;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class AccountManagementWebModule extends FocWebModule {
	
	private static final String MENU_ACCOUNT_MANAGEMENT = "ACCOUNT_MANAGEMENT";
  public static final String CTXT_SAAS                     = "Saas";
	
	public AccountManagementWebModule() {
    super(MENU_ACCOUNT_MANAGEMENT, "Account Management");
  }
	
	public void declareXMLViewsInDictionary() {
		
		XMLViewDictionary.getInstance().put(
	      FocUserDesc.getInstance().getStorageName(),
	      XMLViewKey.TYPE_TABLE,
	      CTXT_SAAS,
	      XMLViewKey.VIEW_DEFAULT,
	      "/xml/b01/foc/admin/FocUser_Saas_Table.xml", 0, FocUser_Saas_Table.class.getName());

	    XMLViewDictionary.getInstance().put(
	      FocUserDesc.getInstance().getStorageName(),
	      XMLViewKey.TYPE_FORM,
	      CTXT_SAAS,
	      XMLViewKey.VIEW_DEFAULT,
	      "/xml/b01/foc/admin/FocUser_Saas_Form.xml", 0, FocUser_Saas_Form.class.getName());
	}

	public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuITem) {
		FocMenuItem mainMenu = menuTree.pushRootMenu("ACCOUNT_MANAGEMENT_MAIN", "Account Managment");
		
		FocMenuItem menuItem = mainMenu.pushMenu("ACCOUNT_MANAGEMENT_USERS", "Users");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = FocUserDesc.getList();
        XMLViewKey xmlViewKey = new XMLViewKey(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE,CTXT_SAAS, XMLViewKey.VIEW_DEFAULT);
        ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, xmlViewKey, focList);
        mainWindow.changeCentralPanelContent(centralPanel, true);
      }
    });
	}
}
