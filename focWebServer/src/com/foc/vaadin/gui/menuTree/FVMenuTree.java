package com.foc.vaadin.gui.menuTree;

import java.util.ArrayList;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserHistory;
import com.foc.admin.FocUserHistoryDesc;
import com.foc.admin.FocUserHistoryList;
import com.foc.admin.UserSession;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.FocMenuItemDesc;
import com.foc.menuStructure.FocMenuItemList;
import com.foc.menuStructure.FocMenuItemTree;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.IFocWebModule;
import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

@SuppressWarnings("serial")
public class FVMenuTree extends FocXMLLayout {

	private FocMenuItemTree menuTree = null;
	private FocMenuItemList menuList = null;
//	private FocUserHistoryList historyList = null;

	private int treeType = TYPE_HISTORY;

	public static final int TYPE_HISTORY = 0;
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_ADMIN = 2;

	public FVMenuTree() {
	}

	public void dispose() {
		super.dispose();
		if(menuTree != null){
			menuTree.dispose();
			menuTree = null;
		}
		if(menuList != null){
			menuList.dispose();
			menuList = null;
		}
//		if(historyList != null){
//			historyList.dispose();
//			historyList = null;
//		}
	}	
	
	public void setTreeType(int type) {
		this.treeType = type;
	}

	public void fill() {
		setWidth("100%");

		fillMainTree();
		setFocData(getMenuTree());
	}

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
	}

	@Override
	public void parseXMLAndBuildGui() {
		if (isHistory()) {
			fill();
		}
		super.parseXMLAndBuildGui();
	}

	@Override
	protected void afterLayoutConstruction() {
		addGuiTree();
	}

	public void addMenu(String menu, String parent, boolean areChildrenAllowed) {
		// addItem(menu);
		// setParent(menu, parent);
		// setChildrenAllowed(menu, areChildrenAllowed);
	}

	public boolean menuClicked(String menu) {
		boolean treated = false;
		return treated;
	}

	public FocMenuItemList getMenuList() {
		if (menuList == null) {
			menuList = new FocMenuItemList();
		}
		return menuList;
	}

	public FocUserHistoryList getHistoryList() {
		return (FocUserHistoryList) FocUserHistoryDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
//		if (historyList == null) {
//			historyList = new FocUserHistoryList();
//		}
//		return historyList;
	}

	public FocMenuItemTree getMenuTree() {
		if (menuTree == null) {
			menuTree = new FocMenuItemTree(getMenuList());
			// FocMenuItemNode rootNode = menuTree.getRoot();
		}
		return menuTree;
	}

	public void setMenuTree(FocMenuItemTree menuTree) {
		this.menuTree = menuTree;
	}

	public INavigationWindow getNavigationWindow() {
		return findAncestor(FocCentralPanel.class);
	}

	public FVTableWrapperLayout getTreeTableWrapper() {
		return (FVTableWrapperLayout) getComponentByName("MENU_TREE");
	}
	
	public FVTreeTable getTreeTable() {
		FVTableWrapperLayout tableWrapperLayout = getTreeTableWrapper();
		return tableWrapperLayout != null ? (FVTreeTable) tableWrapperLayout.getTableOrTree() : null;
	}

	public FocMenuItem findMenuItem(String menuCode){ 
  	FocMenuItem menuItem = (FocMenuItem) getMenuList().searchByPropertyStringValue(FocMenuItemDesc.FLD_CODE, menuCode);
  	return menuItem;
  }
	
	public void clickMenuItem(INavigationWindow iNavigationWindow, String menuCode){ 
  	FocMenuItem menuItem = findMenuItem(menuCode);
  	if(menuItem != null){
  		menuItem.getMenuAction().actionPerformed(iNavigationWindow, menuItem, 0);
			FocUserHistoryList.addHistory(menuCode);
  	}
  }
	
	public void addGuiTree() {
		/*
		 * FVTreeTable treeTable = new FVTreeTable(null){
		 * 
		 * @Override public void open(FocObject focObject) { FocMenuItem menuItem =
		 * (FocMenuItem) focObject; if(menuItem.getMenuAction() != null){
		 * menuItem.getMenuAction().actionPerformed(getMainWindow(), menuItem); } }
		 * };
		 * 
		 * treeTable.setPageLength(20);
		 * treeTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
		 * treeTable.setTree(getMenuTree()); treeTable.setWidth("100%");
		 * treeTable.setHeight("100%");
		 * 
		 * //FField fldCode =
		 * FocMenuItemDesc.getInstance().getFieldByID(FocMenuItemDesc.FLD_CODE);
		 * FField fldTitle =
		 * FocMenuItemDesc.getInstance().getFieldByID(FocMenuItemDesc.FLD_TITLE);
		 * 
		 * treeTable.addColumn(fldTitle.getName(), "Menu");
		 * //treeTable.addColumn(fldCode.getName(), "Code");
		 * 
		 * treeTable.setItemCaptionPropertyId(fldTitle.getName());
		 * 
		 * treeTable.applyFocListAsContainer();
		 * 
		 * addComponent(treeTable);
		 * 
		 * treeTable.setSelectable(true); treeTable.setImmediate(true);
		 */
		if (getTreeTable() != null) {
			if(isHistory()){
				FVTableWrapperLayout tableWrapperLayout = getTreeTableWrapper();
				if(tableWrapperLayout != null){
					/*
					FVButton addressBookButton = new FVButton("Companies", new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
			         INavigationWindow mainWindow = (INavigationWindow) getNavigationWindow();
			         FocList focList = AdrBookPartyDesc.getList(FocList.LOAD_IF_NEEDED);
			         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
						}
					});
					addressBookButton.setStyleName(BaseTheme.BUTTON_LINK);
//					addressBookButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_ADDRESS_BOOK));
					tableWrapperLayout.addHeaderComponent_ToLeft(addressBookButton);
					
					addressBookButton = new FVButton("Contacts", new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
			         INavigationWindow mainWindow = (INavigationWindow) getNavigationWindow();
			         FocList focList = ContactDesc.getList(FocList.LOAD_IF_NEEDED);
			         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
						}
					});
					addressBookButton.setStyleName(BaseTheme.BUTTON_LINK);
//					addressBookButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_ADDRESS_BOOK));
					tableWrapperLayout.addHeaderComponent_ToLeft(addressBookButton);
					*/					
				}
			}
			
			getTreeTable().addItemClickListener(new ItemClickListener() {
				@Override
				public void itemClick(ItemClickEvent event) {
					// menuClicked((String) event.getItemId());
					try {
						if(getMenuList() == null || getMenuList().searchByReference(((Integer) event.getItemId())) == null){
							int debug = 3;
							debug++;
						}
						FocMenuItem menuItem = (FocMenuItem) getMenuList().searchByReference(((Integer) event.getItemId()).intValue());
						if (menuItem != null && menuItem.getMenuAction() != null) {
							FocCentralPanel window = findAncestor(FocCentralPanel.class);
							if (window != null) {
								boolean isActionPerformed = false;
								if (event.getPropertyId().equals("EXTRA_ACTION_0")) {
									menuItem.getMenuAction().actionPerformed(window, menuItem, 1);
									isActionPerformed = true;
								}

								boolean executeAction = true;
								// Adding this condition to alter the click behavior if it's
								// mobile-based
								if (!FocWebApplication.getInstanceForThread().isMobile()) {
									if (!event.isDoubleClick()) {
										executeAction = false;
									}
								}

								if (event.getPropertyId().equals("TITLE") && executeAction) {
									menuItem.getMenuAction().actionPerformed(window, menuItem, 0);
									isActionPerformed = true;
								}
								if (isActionPerformed) {
									if (isNormal() || isHistory() || isAdminConsole()) {
										FocUserHistoryList.addHistory(menuItem.getCode());
									}
								}
							}
						} else {
							// Here we should toggle the collapse expand state of the node we
							// are on.
							FVTreeTable treeTable = getTreeTable();
							if (treeTable.isCollapsed(event.getItemId())) {
								treeTable.setCollapsed(event.getItemId(), false);
							} else {
								treeTable.setCollapsed(event.getItemId(), true);
							}
						}
					} catch (Exception e) {
						Globals.logException(e);
					}
				}
			});
		}
		// ((FVTreeTable)tableWrapperLayout.getTableOrTree()).addShortcutListener(new
		// AbstractField.FocusShortcut("Enter Shortcut",
		// ShortcutListener.KeyCode.ENTER, null));
	}

	public FocMenuItem pushRootMenu(String code, String title) {
		return pushMenu(null, code, title);
	}

	public FocMenuItem pushMenu(FocMenuItem father, String code, String title) {
		FocMenuItemList list = getMenuList();
		return list != null ? list.pushMenu(father, code, title) : null;
	}

	protected void fillMainTree() {
		FocMenuItemList list = getMenuList();

		FocMenuItem root = null;
		FocUser user = FocWebApplication.getFocUser();

		/*
		 * root = list.pushMenu(null, "Main", "Main menu");
		 * root.getReference().setInteger(-10000); root.validate(true);
		 * getMenuList().validate(true);
		 */

		ArrayList<IFocWebModule> modulesSortedArrayList = FocWebServer.getInstance().newModulesArrayList();
		for(int i=0; i<modulesSortedArrayList.size(); i++){
			IFocWebModule module = (IFocWebModule) modulesSortedArrayList.get(i);
			if ((isAdminConsole() && module.isAdminConsole()) || (!isAdminConsole() && !module.isAdminConsole()) || (isHistory())) {
				boolean allow = allowAccessToModule(module.getName());
				if (allow) {
					
					module.menu_FillMenuTree(this, root);
					
				}
			}
		}

		ArrayList<FocMenuItem> toRemove = new ArrayList<FocMenuItem>();

		FocUserHistory history = null;
		if (isHistory()) {
			getHistoryList().loadIfNotLoadedFromDB();
			history = getHistoryList().findHistory(user);
		}

		for (int i = 0; i < list.size(); i++) {
			boolean historyConditions = false;
			FocMenuItem item = (FocMenuItem) list.getFocObject(i);

			// For History
			if (isHistory()) {
				if (history != null) {
					if (!history.findHistory(item.getCode()))
						historyConditions = true;
					else {
						item.setFatherObject(null);
					}
				} else {
					historyConditions = true;
				}
			}
			// ----------

			if ((item.getTitle().startsWith("_") && !ConfigInfo.isForDevelopment()) || (user.isGuest() && !item.getGuest()) || historyConditions == true) {
				toRemove.add(item);
			}
		}

		for (int i = 0; i < toRemove.size(); i++) {
			FocMenuItem item = toRemove.get(i);
			list.remove(item);
		}
	}

	public boolean isAdminConsole() {
		return (treeType == TYPE_ADMIN);
	}

	public boolean isHistory() {
		return (treeType == TYPE_HISTORY);
	}

	public boolean isNormal() {
		return (treeType == TYPE_NORMAL);
	}

	protected FocGroup getFocGroup(){
		return FocWebApplication.getFocUser() != null ? FocWebApplication.getFocUser().getGroup() : null;
	}
	
	public boolean allowAccessToModule(String moduleName) {
		return FocWebModule.allowAccessToModule(getFocGroup(), moduleName);
	}
	
	public static String USER_SESSION_PARAMETER_MENU_TREE = "MENU_TREE";
	public static FVMenuTree getMenuTree_ForThisSession(){
		FVMenuTree menuTree = null;
		try{
 			menuTree = (FVMenuTree) UserSession.getParameter(USER_SESSION_PARAMETER_MENU_TREE);
 			if(menuTree == null){
 				menuTree = new FVMenuTree();
 				menuTree.setTreeType(FVMenuTree.TYPE_NORMAL);
 		  	menuTree.fill();
 		  	UserSession.putParameter(USER_SESSION_PARAMETER_MENU_TREE, menuTree);
 			}
		}catch(Exception e){
			Globals.logExceptionWithoutPopup(e);
			menuTree = null;
		}
 		return menuTree;
	}
	
	public static void resetMenuTree_ForThisSession(){
		FVMenuTree menuTree = (FVMenuTree) UserSession.getParameter(USER_SESSION_PARAMETER_MENU_TREE);
		if(menuTree != null){
			menuTree.dispose();
	  	UserSession.putParameter(USER_SESSION_PARAMETER_MENU_TREE, null);
		}
	}
}
