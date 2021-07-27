/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.web.modules.admin;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;

import com.fab.codeWriter.CodeWriter;
import com.fab.codeWriter.CodeWriterSet;
import com.fab.model.table.TableDefinition;
import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.IFocEnvironment;
import com.foc.OptionDialog;
import com.foc.access.FocLogLineDesc;
import com.foc.access.FocLogger;
import com.foc.admin.ActiveUserDesc;
import com.foc.admin.ActiveUserList;
import com.foc.admin.DocRightsGroupDesc;
import com.foc.admin.DocRightsGroupUsersDesc;
import com.foc.admin.FocDBLog;
import com.foc.admin.FocGroup;
import com.foc.admin.FocGroupDesc;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.GroupXMLViewDesc;
import com.foc.admin.MenuRightsDesc;
import com.foc.admin.userModuleAccess.UserModuleList;
import com.foc.business.adrBook.ContactDesc;
import com.foc.business.multilanguage.LanguageDesc;
import com.foc.db.migration.MigFieldMapDesc;
import com.foc.db.migration.MigrationSourceDesc;
import com.foc.desc.AutoPopulatable;
import com.foc.desc.FocDesc;
import com.foc.gui.table.view.ColumnsConfigDesc;
import com.foc.gui.table.view.UserViewDesc;
import com.foc.gui.table.view.ViewConfigDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.FocMenuItemDesc;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.saas.manager.SaaSApplicationAdaptor;
import com.foc.saas.manager.SaaSConfig;
import com.foc.saas.manager.SaaSConfigDesc;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.IApplicationConfigurator;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.windows.optionWindow.IOption;
import com.foc.vaadin.gui.windows.optionWindow.OptionDialogWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.xmleditor.XMLEditor;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.microservice.servlets.ActiveUsersServlet;
import com.foc.web.modules.admin.gui.FocDBLog_Table;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.foc.web.unitTesting.FocUnitDictionary;
import com.foc.web.unitTesting.FocUnitTestingSuite;
import com.vaadin.server.Page;

@SuppressWarnings("deprecation")
public class AdminWebModule extends FocWebModule {
	public static final String CTXT_LOGIN = "Login";
	public static final String CTXT_LOGIN_AR = "Login-ar";
	public static final String CTXT_HISTORY = "History";
	public static final String CTXT_GROUP_MENU_RIGHT = "GroupMenuRight";
	public static final String CTXT_MIGRATION_SOURCE_SET = "MigrationSourceSet";
	public static final String CONTEXT_COMPANY_CONFIGURATION = "ComapnyConfiguration";
	public static final String MODULE_NAME = "ADMINISTRATOR";
	public static final String CONTEXT_COMPANY_SELECTION = "CompanySelection";
	public static final String CONTEXT_COMPANY_SELECTION_AR = "CompanySelectionAr";
	public static final String CONTEXT_HOMEPAGE = "HomePage";
	public static final String STORAGE_HOMEPAGE = "HOMEPAGE";
	public static final String CONTEXT_BEFORE_LOGIN = "BeforeLogin";
	public static final String STORAGE_NAME_MANAGE_ACCOUNT = "Manage Account";
	public static final String CONTEXT_GROUP_SELECTOR = "Group Selector";
	public static final String VIEW_LIMITED = "LIMITED";
	public static final String OPTION_WINDOW_STORAGE = "OPTION_WINDOW_STORAGE";
	public static final String RIGHT_PANEL_STORAGE = "RIGHT_PANEL_STORAGE";
	public static final String CONTEXT_CONTACT = "ContactUser";
	public static final String MNU_CODE_UNIT_TEST_LOG = "UNIT_TEST_LOG";

	public AdminWebModule() {
		super(MODULE_NAME, "Administrator");
		setAdminConsole(true);
	}

	public static void adapt_BarmajaUser() {
	}

	public void declareXMLViewsInDictionary() {
		XMLViewDictionary.getInstance().put(DocRightsGroupDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/DocRightsGroup_Table.xml", 0, DocRightsGroup_Table.class.getName());

		XMLViewDictionary.getInstance().put(DocRightsGroupDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/DocRightsGroup_Form.xml", 0, DocRightsGroup_Form.class.getName());

		XMLViewDictionary.getInstance().put(DocRightsGroupUsersDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/DocRightsGroupUsers_Table.xml", 0, DocRightsGroupUsers_Table.class.getName());

		XMLViewDictionary.getInstance().put(MigrationSourceDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/MigrationSource_Table.xml", 0, MigrationSource_Table.class.getName());

		XMLViewDictionary.getInstance().put(ActiveUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/ActiveUser_Table.xml", 0, ActiveUser_Table.class.getName());

		XMLViewDictionary.getInstance().put(SaaSConfigDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/SaaSConfig_Form.xml", 0, SaaSConfig_Form.class.getName());

		XMLViewDictionary.getInstance().put(MigrationSourceDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, true, "/xml/com/foc/admin/MigrationSource_Form.xml", 0, null);

		XMLViewDictionary.getInstance().put(MigrationSourceDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, false, "/xml/com/foc/admin/MigrationSource_Set_Form.xml", 0, MigrationSource_Set_Form.class.getName());

		XMLViewDictionary.getInstance().put(MigFieldMapDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/MigFieldMap_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put("FVERSION", XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/Fversion_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put("FVERSION", XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/Fversion_Form.xml", 0, null);

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocUser_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocUser_Form.xml", 0, FocUser_Form.class.getName());

		XMLViewDictionary.getInstance().put(STORAGE_NAME_MANAGE_ACCOUNT, XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/ManageAccount_Form.xml", 0, ManageAccount_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CONTEXT_CONTACT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocUser_Contact_Form.xml", 0, FocUser_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CONTEXT_COMPANY_SELECTION, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocUser_CompanySelection_Form.xml", 0, FocUser_CompanySelection_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CONTEXT_COMPANY_SELECTION_AR, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocUser_CompanySelection_Form-ar.xml", 0, FocUser_CompanySelection_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CONTEXT_COMPANY_SELECTION, VIEW_LIMITED, "/xml/com/foc/admin/FocUser_CompanySelection_Limited_Form.xml", 0, FocUser_CompanySelection_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocGroupDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CONTEXT_COMPANY_CONFIGURATION, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocGroup_CompanyConfiguration_Form.xml", 0, FocGroup_CompanyConfiguration_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocGroupDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CONTEXT_GROUP_SELECTOR, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocGroup_Selector_Form.xml", 0, FocGroup_Selector_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CONTEXT_HOMEPAGE, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocUser_HomePage_Form.xml", 0, FocUser_HomePage_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocGroupDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocGroup_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(FocGroupDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocGroup_Form.xml", 0, FocGroup_Form.class.getName());

		XMLViewDictionary.getInstance().put(UserViewDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/UserView_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(UserViewDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/UserView_Form.xml", 0, null);

		XMLViewDictionary.getInstance().put(ViewConfigDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/ViewConfig_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(ViewConfigDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/ViewConfig_Form.xml", 0, null);

		XMLViewDictionary.getInstance().put(ColumnsConfigDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/ColumnsConfig_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(ColumnsConfigDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/ColumnsConfig_Form.xml", 0, null);

		XMLViewDictionary.getInstance().put(MenuRightsDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/MenuRights_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(MenuRightsDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/MenuRights_Form.xml", 0, null);

		XMLViewDictionary.getInstance().put(LanguageDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/Language_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(LanguageDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/Language_Form.xml", 0, null);

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CTXT_LOGIN, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/login.xml", 0, FocUser_Login_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CTXT_LOGIN_AR, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/login-ar.xml", 0, FocUser_Login_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CTXT_LOGIN, "No Buttons", "/xml/com/foc/admin/login_nobuttons.xml", 0, FocUser_Login_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocMenuItemDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocMenuItem_Tree.xml", 0, FVMenuTree.class.getName());

		XMLViewDictionary.getInstance().put(FocMenuItemDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE, CTXT_HISTORY, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocMenuItem_History_Tree.xml", 0, FVMenuTree.class.getName());

		XMLViewDictionary.getInstance().put(FocMenuItemDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE, CTXT_GROUP_MENU_RIGHT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocMenuItem_GroupMenuRight_Tree.xml", 0, FocMenuItem_GroupMenuRight_Tree.class.getName());

		XMLViewDictionary.getInstance().put(STORAGE_HOMEPAGE, XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/HomePage_AfterLogin_Form.xml", 0, HomePage_AfterLogin_Form.class.getName());

		XMLViewKey key = new XMLViewKey(STORAGE_HOMEPAGE, XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT);
		key.setMobileFriendly(true);
		XMLView view = new XMLView(key, "/xml/com/foc/admin/HomePage_AfterLogin_Mobile_Form.xml", HomePage_AfterLogin_Mobile_Form.class.getName());
		XMLViewDictionary.getInstance().put(view);

		// XMLViewDictionary.getInstance().put(
		// STORAGE_HOMEPAGE,
		// XMLViewKey.TYPE_FORM,
		// XMLViewKey.CONTEXT_DEFAULT,
		// XMLViewKey.VIEW_MOBILE,
		// "/xml/com/foc/admin/HomePage_AfterLogin_Mobile_Form.xml", 0,
		// HomePage_AfterLogin_Mobile_Form.class.getName());

		// XMLViewDictionary.getInstance().put(
		// STORAGE_HOMEPAGE,
		// XMLViewKey.TYPE_FORM,
		// XMLViewKey.CONTEXT_DEFAULT,
		// XMLViewKey.VIEW_MOBILE,
		// "/xml/com/foc/admin/HomePage_AfterLogin_Mobile_Form.xml", 0,
		// HomePage_AfterLogin_Mobile_Form.class.getName());

		XMLViewDictionary.getInstance().put(STORAGE_HOMEPAGE, XMLViewKey.TYPE_FORM, CONTEXT_BEFORE_LOGIN, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/HomePage_BeforeLogin_Form.xml", 0, HomePage_BeforeLogin_Form.class.getName());

		XMLViewDictionary.getInstance().put(STORAGE_HOMEPAGE, XMLViewKey.TYPE_FORM, CONTEXT_BEFORE_LOGIN, XMLViewKey.VIEW_MOBILE, "/xml/com/foc/admin/HomePage_BeforeLogin_Mobile_Form.xml", 0, HomePage_BeforeLogin_Form.class.getName());

		XMLViewDictionary.getInstance().put(FocLogLineDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/Log_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(FocLogLineDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/Log_Table.xml", 0, null);

		XMLViewDictionary.getInstance().put(GroupXMLViewDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/GroupXMLView_Table.xml", 0, GroupXMLView_Table.class.getName());

		XMLViewDictionary.getInstance().put(GroupXMLViewDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/GroupXMLView_Form.xml", 0, GroupXMLView_Form.class.getName());

		XMLViewDictionary.getInstance().put(MenuRightsDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/MenuRights_Tree.xml", 0, null);

		XMLViewDictionary.getInstance().put(OPTION_WINDOW_STORAGE, XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/OptionDialog_Form.xml", 0, OptionDialog_Form.class.getName());

		XMLViewDictionary.getInstance().put(RIGHT_PANEL_STORAGE, XMLViewKey.TYPE_TREE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/xml/com/foc/admin/FocRightPanel_Tree.xml", 0, FocRightPanel_Tree.class.getName());

		XMLViewDictionary.getInstance().put(FocDBLog.DBNAME, XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT, "/com/foc/web/modules/admin/gui/FocDBLog^Table.xml", 0, FocDBLog_Table.class.getName());
		XMLViewDictionary.getInstance().put(FocDBLog.DBNAME, XMLViewKey.TYPE_FORM, "Banner", XMLViewKey.VIEW_DEFAULT, "/com/foc/web/modules/admin/gui/FocDBLog^Banner^Standard^Form.xml", 0, null);
		XMLViewDictionary.getInstance().put(FocDBLog.DBNAME, XMLViewKey.TYPE_FORM, "Banner", XMLViewKey.VIEW_DEFAULT, false, "ar", "/com/foc/web/modules/admin/gui/FocDBLog^Banner^Standard^Form-ar.xml", 0, null);

	}

	public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
		FocMenuItem systemMenu = menuTree.pushRootMenu("System", "System");

		FocMenuItem menuItem = systemMenu.pushMenu("REINDEX_ALL", "Reindex All Indexes");
		menuItem.setMenuAction(new IFocMenuItemAction() {

			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				OptionDialog dialog = new OptionDialog("Reindex all", "This may take a few minutes to reindex all indexes.") {

					@Override
					public boolean executeOption(String optionName) {
						if (optionName != null && optionName.equals("ADAPT")) {
							Globals.getApp().getDataSource().command_AdaptDataModel_Reindex();
						}
						return false;
					}
				};
				dialog.addOption("ADAPT", "Yes Reindex");
				dialog.addOption("CANCEL", "No Cancel Reindexing");
				dialog.setWidth("500px");
				dialog.setHeight("200px");
				Globals.popupDialog(dialog);
			}
		});

		menuItem = systemMenu.pushMenu("ADAPT_DATA_MODEL", "Adapt Data Model");
		menuItem.setMenuAction(new IFocMenuItemAction() {

			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				OptionDialog dialog = new OptionDialog("Adapt data to model", "This may take a few minutes to adapt the database tables and fields according to the executable's version.") {

					@Override
					public boolean executeOption(String optionName) {
						if (optionName != null && optionName.equals("ADAPT")) {
							Globals.getApp().adaptDataModel(false, false);
						}
						return false;
					}
				};
				dialog.addOption("ADAPT", "Yes Adapt");
				dialog.addOption("CANCEL", "No Cancel Adapt");
				dialog.setWidth("500px");
				dialog.setHeight("200px");
				Globals.popupDialog(dialog);
			}
		});

		menuItem = systemMenu.pushMenu("ADAPT_DATA_MODEL_FORCED", "Adapt Data Model - Forced");
		menuItem.setMenuAction(new IFocMenuItemAction() {

			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				OptionDialog dialog = new OptionDialog("Adapt data to model", "This may take a few minutes to adapt the database tables and fields according to the executable's version.") {

					@Override
					public boolean executeOption(String optionName) {
						if (optionName != null && optionName.equals("ADAPT")) {
							Globals.getApp().adaptDataModel(true, false);
						}
						return false;
					}
				};
				dialog.addOption("ADAPT", "Yes Adapt");
				dialog.addOption("CANCEL", "No Cancel Adapt");
				dialog.setWidth("500px");
				dialog.setHeight("200px");
				Globals.popupDialog(dialog);
			}
		});

		menuItem = systemMenu.pushMenu("DB_CONNECTION_MONITORING", "Database Connection Monitoring");
		menuItem.setMenuAction(new IFocMenuItemAction() {

			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				String message = "";
				if (Globals.getApp() != null && Globals.getApp().getDataSource() != null) {
					StringBuffer buff = Globals.getApp().getDataSource().getMonitoringText();
					if (buff != null) {
						message = buff.toString();

						OptionDialog dialog = new OptionDialog("Database Connection Monitoring", message) {
							@Override
							public boolean executeOption(String optionName) {
								return false;
							}

						};
						dialog.addOption("OK", "Ok");
						dialog.setWidth("1000px");
						dialog.setHeight("500px");
						Globals.popupDialog(dialog);
					}
				}
			}
		});

		menuItem = systemMenu.pushMenu("ACTIVE_USERS", "Active Users");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;

				if (FocWebServer.getInstance() != null) {
					FocWebServer.getInstance().removeApplicationsNotRunning();
				}

				ActiveUserList list = new ActiveUserList();
				for (int i = 0; i < FocWebServer.getInstance().getApplicationCount(); i++) {
					FocWebApplication app = FocWebServer.getInstance().getApplicationAt(i);
					if (app != null && app.getFocWebSession() != null && app.getFocWebSession().getFocUser() != null && !app.isClosing()) {
						FocUser user = app.getFocWebSession().getFocUser();
						
						list.addActiveUser(user, app.getLastHeartbeatTimestamp(), ActiveUserDesc.ORIGIN_DESKTOP);
					}
				}
				
				ActiveUsersServlet.fillActiveUsersList(list);

				XMLViewKey xmlViewKey = new XMLViewKey(ActiveUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
				ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocWebVaadinWindow) mainWindow, xmlViewKey, list);
				centralPanel.setFocDataOwner(true);
				mainWindow.changeCentralPanelContent(centralPanel, true);
			}
		});

		menuItem = systemMenu.pushMenu("ADMIN_CLEAR_LOGGER", "Clear Logger Tree");
		menuItem.setMenuAction(new IFocMenuItemAction() {

			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				FocLogger.getInstance().dispose();
			}
		});

		menuItem = systemMenu.pushMenu("FREE_UNUSED_MEMORY", "Free Unused Memory");
		menuItem.setMenuAction(new IFocMenuItemAction() {

			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				// INavigationWindow mainWindow = (INavigationWindow) navigationWindow;

		    // DEBUGGING Code allows to capture any setWithList (true) that needs to be captured
				/*
		    try{
			  	java.util.Iterator<FocDesc> iterDescs = Globals.getApp().getFocDescMap().values().iterator();
			  	while(iterDescs != null && iterDescs.hasNext()){
			  		FocDesc focDesc = iterDescs.next();
		  			if(focDesc != null){
		  				for(int i=0; i<focDesc.getFieldsSize(); i++) {
		  					FField fld = focDesc.getFieldAt(i);
		  					if (fld != null && fld instanceof FObjectField) {
		  						FObjectField objFld = (FObjectField) fld;
		  						FocDesc subDesc = objFld.getFocDesc();
		  						if(subDesc != null && subDesc.getStorageName().equals("Vehicles")) {
		  							if(objFld.isWithList()) {
		  								Globals.logString("Vehicles are ferenced in "+focDesc.getStorageName()+" field "+objFld.getName()+"  -  WITH LIST  -");
		  							} else {
		  								Globals.logString("Vehicles are ferenced in "+focDesc.getStorageName()+" field "+objFld.getName());
		  							}
		  						}
		  					}
		  				}
		  			}
			  	}
		    } catch(Exception e) {
		    	Globals.logException(e);
		    }
		    */
		    // -------------------------------
				
				String beforeMessage = Globals.logMemory("Before Freeing ");

				System.gc();
				Globals.logMemory("");

				System.gc();
				Globals.logMemory("");

				System.gc();
				String afterMessage = Globals.logMemory("After  Freeing ");

				Globals.showNotification(beforeMessage, afterMessage, IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
			}
		});

		menuItem = systemMenu.pushMenu("SHOW_MEMORY_OBJECTS", "Dump living entity counts");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				String beforeMessage = Globals.logMemory("Before Freeing ");

				System.gc();
				Globals.logMemory("");

				System.gc();
				Globals.logMemory("");

				System.gc();
				String afterMessage = Globals.logMemory("After  Freeing ");

				String html = Globals.getApp().dumpLivingFocObjectCounts(true, true, false);
				afterMessage += "\n" + html; 
				
				Globals.showNotification(beforeMessage, afterMessage, IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
				
			}
		});
		
		menuItem = systemMenu.pushMenu("MEMORY", "Memory");
		menuItem.setMenuAction(new IFocMenuItemAction() {

			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				String message = Globals.logMemory("");
				Globals.showNotification("", message, IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
			}
		});

		FocMenuItem mainMenu = menuTree.pushRootMenu("Admin", "Admin");

		menuItem = mainMenu.pushMenu("FOC_USER", "Users");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FocList focList = FocUserDesc.getList();
				mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
			}
		});

		menuItem = mainMenu.pushMenu("DOC_RIGHTS_GROUPS", "Doc Rights Group");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FocList focList = DocRightsGroupDesc.getInstance().getFocList();
				mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
			}
		});

		menuItem = mainMenu.pushMenu("FOC_GROUP", "Groups");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FocList focList = FocGroup.getList(FocList.LOAD_IF_NEEDED);
				mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
			}
		});

		menuItem = mainMenu.pushMenu("APP_CONFIG", "App Configuration");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				SaaSConfig appConfiguration = SaaSConfig.getInstance();
				if (appConfiguration != null) {
					XMLViewKey xmlViewKey = new XMLViewKey(SaaSConfigDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM);
					ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, xmlViewKey, appConfiguration);
					mainWindow.changeCentralPanelContent(centralPanel, true);
				}
			}
		});

		menuItem = mainMenu.pushMenu("APP_CONFIGURATOR", "Adapt User Rights");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				if (SaaSConfig.getInstance() != null) {
					SaaSApplicationAdaptor adapter = SaaSConfig.getInstance().getSaasApplicationAdaptor();
					adapter.adaptApplication(true);
					adapter.adaptUserRights();
				}
			}
		});

		menuItem = mainMenu.pushMenu("ADMIN_DOWNLOAD_USER_ACCESS", "Download User Modules Access");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				UserModuleList list = new UserModuleList();
				list.fill();
				UserModuleAccess_ExcelExport userExport = new UserModuleAccess_ExcelExport(list);
				userExport.init();
				userExport.dispose();
				list.dispose();
			}
		});

		menuItem = mainMenu.pushMenu("FOC_MIGRATION_SOURCE", "Migration Source");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FocList focList = MigrationSourceDesc.getInstance().getFocList();
				mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
			}
		});

		menuItem = mainMenu.pushMenu("DOCUMENT_HEADER_EDIT", "Document Header Format");
		menuItem.setExtraAction0("Reset");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				if (extraActionIndex == 0) {
					XMLViewKey key = XMLViewDictionary.getInstance().newXMLViewKey_ForDocumentHeader(XMLViewKey.VIEW_DEFAULT);
					XMLView view = XMLViewDictionary.getInstance().get(key);
					FocWebApplication.getInstanceForThread().addWindow(new XMLEditor(view, "Document header", view.getXmlviewDefinition().getXML()));
				} else if (extraActionIndex == 1) {
					OptionDialog dialog = new OptionDialog("Confirmation", "Are you sure you want to reset the transaction header layout?") {
						public boolean executeOption(String optionName) {
							if (optionName.equals("YES")) {
								XMLViewKey key = XMLViewDictionary.getInstance().newXMLViewKey_ForDocumentHeader(XMLViewKey.VIEW_DEFAULT);
								XMLView view = XMLViewDictionary.getInstance().get(key);
								if (view != null) {
									XMLViewDictionary.getInstance().updateDocumentHearerView(view.getXmlviewDefinition());
								}
							}
							return false;
						}
					};
					dialog.addOption("YES", "Yes reset");
					dialog.addOption("CANCEL", "No cancel");
					dialog.setWidth("500px");
					dialog.setHeight("200px");

					Globals.popupDialog(dialog);
				}
			}
		});

		menuItem = mainMenu.pushMenu("DOCUMENT_HEADER_PRINT_EDIT", "Document Header Printing Format");
		menuItem.setExtraAction0("Reset");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				if (extraActionIndex == 0) {
					XMLViewKey key = XMLViewDictionary.getInstance().newXMLViewKey_ForDocumentHeader(XMLViewKey.VIEW_PRINTING);
					XMLView view = XMLViewDictionary.getInstance().get_WithoutAdjustToLastSelection(key);
					FocWebApplication.getInstanceForThread().addWindow(new XMLEditor(view, "Document header", view.getXmlviewDefinition().getXML()));
				} else if (extraActionIndex == 1) {
					OptionDialog dialog = new OptionDialog("Confirmation", "Are you sure you want to reset the transaction header printing layout?") {
						public boolean executeOption(String optionName) {
							if (optionName.equals("YES")) {
								XMLViewKey key = XMLViewDictionary.getInstance().newXMLViewKey_ForDocumentHeader(XMLViewKey.VIEW_PRINTING);
								XMLView view = XMLViewDictionary.getInstance().get(key);
								if (view != null) {
									XMLViewDictionary.getInstance().updateDocumentHearerView_Printing(view.getXmlviewDefinition());
								}
							}
							return false;
						}
					};
					dialog.addOption("YES", "Yes reset");
					dialog.addOption("CANCEL", "No cancel");
					Globals.popupDialog(dialog);
				}
			}
		});

		FocMenuItem countryPresetingsMenu = null;
		if (FocWebServer.getInstance() != null && FocWebServer.getInstance().applicationConfigurators_Size() > 0) {
			for (int i = 0; i < FocWebServer.getInstance().applicationConfigurators_Size(); i++) {
				IApplicationConfigurator config = FocWebServer.getInstance().applicationConfigurators_Get(i);
				if (countryPresetingsMenu == null) {
					countryPresetingsMenu = mainMenu.pushMenu("Country Presettings", "Country presettings");
				}
				FocMenuItem countryMenuItem = countryPresetingsMenu.pushMenu(config.getCode(), config.getTitle());
				countryMenuItem.setMenuAction(config);
			}
		}

		URI uri = Page.getCurrent().getLocation();
		if (Globals.getApp().isUnitTest()) {
			menuItem = mainMenu.pushMenu(MNU_CODE_UNIT_TEST_LOG, "Unit Test Logs");
			menuItem.setMenuAction(new IFocMenuItemAction() {

				@Override
				public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
					INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
					FocUnitDictionary.getInstance().popupLogger(mainWindow);
				}
			});
		}

		if (ConfigInfo.isUnitAllowed() && uri.getHost().equals("localhost")) {
			menuItem = mainMenu.pushMenu("UNIT_TEST_LOG", "Unit Test Logs");
			menuItem.setMenuAction(new IFocMenuItemAction() {

				@Override
				public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
					INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
					FocUnitDictionary.getInstance().popupLogger(mainWindow);
				}
			});

			FocMenuItem unitTestingMenuList = mainMenu.pushMenu("UNIT_TEST", "Unit Tests");

			FocUnitDictionary dictionary = FocUnitDictionary.getInstance();
			Collection<FocUnitTestingSuite> collection = dictionary != null ? dictionary.getTestingSuiteMapValues() : null;
			if (collection != null) {
				Iterator<FocUnitTestingSuite> itr = collection.iterator();

				while (itr != null && itr.hasNext()) {
					final FocUnitTestingSuite suite = (FocUnitTestingSuite) itr.next();

					if (suite.isShowInMenu()) {
						menuItem = unitTestingMenuList.pushMenu(suite.getName(), suite.getName());
						menuItem.setMenuAction(new IFocMenuItemAction() {
							public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
								INavigationWindow mainWindow = (INavigationWindow) navigationWindow;

								FocUnitDictionary dictionary = FocUnitDictionary.getInstance();
								try {
									dictionary.runUnitTest(suite.getName());
								} catch (Exception e) {
									Globals.logException(e);
								}

								dictionary.popupLogger(mainWindow);
							}
						});
					}
				}
			}
		}

		if (ConfigInfo.isDevMode()) {
			menuItem = mainMenu.pushMenu("MAKE_DATA_ANONIMOUS", "Make Data Anonymous");
			menuItem.setMenuAction(new IFocMenuItemAction() {
	
				@Override
				public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
	
					OptionDialogWindow optionWindow = new OptionDialogWindow("ARE YOU SURE YOU WANT TO LOOSE DATA NAMES!!!!! (NOT RECOMMENDED) SHOULD NEVER BE USED FOR PRODUCTION REAL DATA !!!!!!!!!!! !!!!!!!!!!!! !!!!!!!!!!!!! !!!!!!!!!!!", null);
					optionWindow.setWidth("500px");
					optionWindow.setHeight("200px");
	
					optionWindow.addOption("Yes, Loose Data", new IOption() {
						@Override
						public void optionSelected(Object contextObject) {
							//FocWebServer.getInstance().makeDataAnonymous();
						}
					});
	
					optionWindow.addOption("Cancel", new IOption() {
						@Override
						public void optionSelected(Object contextObject) {
						}
					});
					FocWebApplication.getInstanceForThread().addWindow(optionWindow);
				}
			});
		}

		FocMenuItem autoPopulateMenu = null;
		Iterator<IFocDescDeclaration> focDescDeclarationIterator = Globals.getApp().getFocDescDeclarationIterator();
		while (focDescDeclarationIterator != null && focDescDeclarationIterator.hasNext()) {

			IFocDescDeclaration focDescDeclaration = focDescDeclarationIterator.next();
			if (focDescDeclaration != null) {
				FocDesc focDesc = focDescDeclaration.getFocDescription();
				if (focDesc instanceof AutoPopulatable) {
					final AutoPopulatable autoPopulatable = (AutoPopulatable) focDesc;
					if (autoPopulateMenu == null) {
						autoPopulateMenu = mainMenu.pushMenu("AUTO_POPULATE", "Auto Populate");
						FocMenuItem allautoPopulateMenu = autoPopulateMenu.pushMenu("ALL_AUTO_POPULATE", "All Auto Populate");
						if (allautoPopulateMenu != null) {
							allautoPopulateMenu.setMenuAction(new AllAutoPopulateListener());
						}
					}
					final String autoPopulatableTitle = autoPopulatable.getAutoPopulatableTitle();
					FocMenuItem autoPopulatableMenuItem = autoPopulateMenu.pushMenu(autoPopulatableTitle.toUpperCase(), autoPopulatableTitle);
					autoPopulatableMenuItem.setMenuAction(new IFocMenuItemAction() {

						@Override
						public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
							OptionDialog optionDialog = new OptionDialog("Confirm Auto Populate", "This may take a few minutes to populate " + autoPopulatableTitle) {
								@Override
								public boolean executeOption(String optionName) {
									if (optionName.equals("OK_BUTTON")) {
										autoPopulatable.populate();
									}
									return false;
								}
							};
							optionDialog.addOption("OK_BUTTON", "Auto Populate");
							optionDialog.addOption("CANCEL", "Cancel");
							optionDialog.setWidth("500px");
							optionDialog.setHeight("200px");
							Globals.popupDialog(optionDialog);
						}
					});
				}
			}
		}

		menuItem = mainMenu.pushMenu("GENERATE_MOBILE_APP_SOURCE", "Generate Source Code For Mobile Apps");
		menuItem.setMenuAction(new IFocMenuItemAction() {

			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				//String rootDir = "C:/eclipseworkspace_everpro/xeverpro/src/com/barmaja/everpro/mobileApp/modules";
				//String rootPackkage = "com.barmaja.everpro.mobileApp.modules";

				TableDefinition tableDef = TableDefinition.getTableDefinitionForFocDesc(ContactDesc.getInstance());

				CodeWriterSet cws = new CodeWriterSet(tableDef);

				CodeWriter codeWriter = null;

				codeWriter = cws.getCodeWriter_Proxy();
				codeWriter.generateCode();
				codeWriter = null;
			}
		});
	}

	public class AllAutoPopulateListener implements IFocMenuItemAction {

		@Override
		public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
			OptionDialog optionDialog = new OptionDialog("Auto populate", "This may take a few minutes to auto populate all modules.") {
				@Override
				public boolean executeOption(String option) {
					if (option.equals("YES")) {
						Iterator<IFocDescDeclaration> focDescDeclarationIterator = Globals.getApp().getFocDescDeclarationIterator();
						while (focDescDeclarationIterator != null && focDescDeclarationIterator.hasNext()) {
							IFocDescDeclaration focDescDeclaration = focDescDeclarationIterator.next();
							if (focDescDeclaration != null) {
								FocDesc focDesc = focDescDeclaration.getFocDescription();
								if (focDesc instanceof AutoPopulatable) {
									AutoPopulatable autoPopulatable = (AutoPopulatable) focDesc;
									if (autoPopulatable != null) {
										autoPopulatable.populate();
									}
								}
							}
						}
					}
					return false;
				}
			};
			optionDialog.addOption("YES", "Yes");
			optionDialog.addOption("CANCEL", "Cancel");
			optionDialog.setWidth("600px");
			optionDialog.setHeight("170px");
			Globals.popupDialog(optionDialog);
		}

	}
}
