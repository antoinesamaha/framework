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
package com.foc.vaadin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.admin.FocGroup;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.business.downloadableContent.DownloadableContentDesc;
import com.foc.business.notifier.FocPageLink;
import com.foc.business.notifier.FocPageLinkDesc;
import com.foc.business.workflow.signing.WFTransactionWrapperList;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.FocMenuItemDesc;
import com.foc.performance.PerfManager;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.broadcast.BroadcastNotifyer;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.menuBar.FVMenuBar;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVHelpButton;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.windows.LoginWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.modules.admin.FocUser_HomePage_Form;
import com.foc.web.modules.admin.OptionDialog_Form;
import com.foc.web.modules.downloadableContent.DownloadableContentWebModule;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.session.FocWebSession;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.foc.web.unitTesting.FocUnitDictionary;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;

@SuppressWarnings("serial")
public class FocWebVaadinWindow extends FocCentralPanel {

	private FVMenuBar    menuBar_User  = null;
  private NativeButton login         = null;
  private NativeButton logout        = null;
  private NativeButton home          = null;
//  private NativeButton chatAlerts    = null;
  private NativeButton navigation    = null;
  private NativeButton admin         = null;
  private NativeButton mobileOptionsButton = null;
  private FVHelpButton helpButton    = null;
  private FSignatureButton    pendingSignature  = null;
//  private FNotificationButton notificatonButton = null;//NOTIF_DEV
	private BroadcastNotifyer broadcastNotifyer = null;

  private Label        companyNameLabel = null;

	private HorizontalLayout centerHeaderLayout    = null;
	private HorizontalLayout headerMenuBar         = null;
	private AbsoluteLayout   emptyLeftMarginPanel  = null;
	
	protected HorizontalLayout hMainLayout           = null; 
	
	private AbsoluteLayout   emptyRightMarginPanel;
  private MenuItem         logoutMenuItem = null;
  private MenuItem         macroRecordingItem = null;
  
	public static final int FORMAT_PORTRAIT    = 0;
	public static final int FORMAT_LANDSCAPE   = 1;
	public static final int FORMAT_FULL_SCREEN = 2;
	
	private int     format        = FORMAT_FULL_SCREEN;
	private boolean menuBarFilled = false;
	
	private HashMap<String, NativeButton> menuBarIconsMap = null;
	
	private ArrayList<ButtonWithPendingSignature> buttonsWithSignatureArray = null;
	
	public FocWebVaadinWindow(){
		super();
		fill();
		fillMainWindow();

		//Case: Guest User opens a 2nd TAB
		//If we do not have this line, the 2nd tab will open the normal Home. Which we do not want.
		//For the First TAB the situation is different because it comes after the login, and in the login form we have a call for this method.
		changeCentralPanelIntoGuestHomePage();
		//--------------------------------
	}
		
	protected void menuBarIcons_Add(String menuItem, NativeButton button){
		if(menuBarIconsMap == null){
			menuBarIconsMap = new HashMap<String, NativeButton>();
		}
		menuBarIconsMap.put(menuItem, button);
		button.addStyleName("foc-f22");
		button.addStyleName("foc-text-center");
		button.setWidth("40px");
	}
	
	public void menuBarIcons_Highlight(String menuItem){
		if(menuBarIconsMap != null){
			NativeButton button = menuItem != null ? menuBarIconsMap.get(menuItem) : null;
			menuBarIcons_Highlight(button);
		}
	}
	
	public void menuBarIcons_Highlight(NativeButton button){
		if(menuBarIconsMap != null){
			Iterator<NativeButton> iter = menuBarIconsMap.values().iterator();
			while(iter != null && iter.hasNext()){
				NativeButton b = iter.next();
				if(b != null){
					if(b == button){
						b.addStyleName("foc-textOrange");
					}else{
						b.removeStyleName("foc-textOrange");
					}
				}
			}
		}
	}
	
	public void addButtonsInMenuBar(){
		
	}
	
	public boolean isIncludeLoginButtonInHeaderBar(){
		return false;
	}
	
	protected boolean isCropMarginPanelsInHeaderBanner(){
		return FocWebApplication.getInstanceForThread().isMobile();
	}
	
	protected boolean isCropMarginPanels(){
		return true;//FocWebApplication.getInstanceForThread().isMobile();
	}

	@Override
	public void fill() {
		setSizeFull();

		setSizeFull();
		setSpacing(false);
		setMargin(false);

		if (isNavigationVisible()) {
			headerMenuBar = new HorizontalLayout();
			headerMenuBar.setMargin(false);
			headerMenuBar.setSpacing(false);

			AbsoluteLayout emptyLeftPanel = null;
			if (!isCropMarginPanelsInHeaderBanner()) {
				emptyLeftMarginPanel = new AbsoluteLayout();

				emptyLeftPanel = new AbsoluteLayout();
				emptyLeftPanel.setSizeFull();
				emptyLeftPanel.setStyleName("focBannerButton");

				emptyLeftPanel.addComponent(newLogoEmbedded(), "top:0.0px;right:10.0px;");

				headerMenuBar.addComponent(emptyLeftPanel);
			}

			centerHeaderLayout = new HorizontalLayout();
			centerHeaderLayout.setMargin(false);
			centerHeaderLayout.setSpacing(false);
			if (!isCropMarginPanelsInHeaderBanner()) {// The condition should be about mobile not crop
				centerHeaderLayout.setWidth(getPreferredWidth());
			}

			headerMenuBar.addComponent(centerHeaderLayout);

			centerHeaderLayout.setStyleName("focBanner");
			headerMenuBar.setStyleName("focBanner");

			if (!isCropMarginPanelsInHeaderBanner()) {
				AbsoluteLayout emptyRightPanel = new AbsoluteLayout();
				emptyRightPanel.setSizeFull();
				emptyRightPanel.setStyleName("focBannerButton");
				headerMenuBar.addComponent(emptyRightPanel);

				headerMenuBar.setExpandRatio(emptyLeftPanel, 0.5f);
				headerMenuBar.setExpandRatio(emptyRightPanel, 0.5f);
			}

			headerMenuBar.setWidth("100%");
			headerMenuBar.setHeight("-1px");
			addComponent(headerMenuBar);
			setComponentAlignment(headerMenuBar, Alignment.TOP_LEFT);
		}

		hMainLayout = new HorizontalLayout();
		hMainLayout.setSizeFull();
		hMainLayout.addStyleName("focMainHorizontal");
		hMainLayout.setMargin(false);
		hMainLayout.setSpacing(false);
		addComponent(hMainLayout);
		setExpandRatio(hMainLayout, 1);

		if (!isCropMarginPanels()) {
			emptyLeftMarginPanel.setSizeFull();
			emptyLeftMarginPanel.setStyleName("focLeftRight");

			hMainLayout.addComponent(emptyLeftMarginPanel);
		}

		hMainLayout.addComponent(getCentralPanelWrapper());
		if (!isCropMarginPanels()) {
			emptyRightMarginPanel = new AbsoluteLayout();
			emptyRightMarginPanel.setSizeFull();
			emptyRightMarginPanel.setStyleName("focRightLeft");
			hMainLayout.addComponent(emptyRightMarginPanel);

			hMainLayout.setExpandRatio(emptyLeftMarginPanel, 0.5f);
			hMainLayout.setExpandRatio(emptyRightMarginPanel, 0.5f);
		} else {
			hMainLayout.setExpandRatio(getCentralPanelWrapper(), 1);
			hMainLayout.setComponentAlignment(getCentralPanelWrapper(), Alignment.TOP_CENTER);
			getCentralPanelWrapper().setWidth("100%");
		}
	}
	
	public void setMenuBarSpacing(boolean spacing){
		if(centerHeaderLayout != null) centerHeaderLayout.setSpacing(spacing);
	}
	
	protected String getLogoURL(){
		return "http://www.focframework.com";
	}
	
	protected Component newLogoEmbedded(){
		Link iconLink = new Link();
//		iconLink.setIcon(new ThemeResource("img/everpro_logo.png"));
		
		iconLink.setIcon(new ThemeResource("img/logo.png"));
		String logoURL = getLogoURL();
		if(logoURL != null){
			iconLink.setResource(new ExternalResource(logoURL));
		}
		
//		iconLink.setStyleName("everproLogo");
		iconLink.setStyleName("foc-UpperLogo");

		return iconLink;
	}
	
	public HorizontalLayout getCentralHeader() {
	  return centerHeaderLayout;
	}
	
	public void dispose(){
		super.dispose();
		headerMenuBar = null;
		if(menuBar_User != null){
			menuBar_User.dispose();
			menuBar_User = null;
		}
		logoutMenuItem = null;
		broadcastNotifyer_Dispose();
		if(buttonsWithSignatureArray != null) {
			buttonsWithSignatureArray.clear();
			buttonsWithSignatureArray = null;
		}
		dispose_MenuTree();
	}
	
	public void hideMenuBar(){
		if(headerMenuBar != null){
			headerMenuBar.setVisible(false);
		}
	}
	
	public void showMenuBar(){
		if(headerMenuBar != null){
			headerMenuBar.setVisible(true);
		}
	}
	
	public void setLeftPanelContent(AbsoluteLayout lefPanelLayout){
	  emptyLeftMarginPanel.addComponent(lefPanelLayout, "top:10px; left:0px");
  }
	
	public NativeButton newButtonInHeaderBar(String caption, boolean addButton){
		return newButtonInHeaderBar(caption, false, addButton);
	}

	public NativeButton newButtonInHeaderBar_IfNotExist(String caption, boolean asFirst, boolean addButton){
		NativeButton nBut = null;
		if(centerHeaderLayout != null){
			for(int i=0; i<centerHeaderLayout.getComponentCount(); i++){
				Component comp = centerHeaderLayout.getComponent(i);
				if(comp != null && comp.getCaption() != null && comp.getCaption().compareTo(caption) == 0){
					return null;
				}
			}
			
			nBut = newButtonInHeaderBar(caption, asFirst, addButton);
		}
		return nBut;
	}
	
	public NativeButton newButtonInHeaderBar(String caption, boolean asFirst, boolean addButton){
		NativeButton nBut = new NativeButton(caption);
		adjustButton(nBut, asFirst, addButton);
		return nBut;
	}

	private void adjustButton(NativeButton nBut, boolean addButton){
		adjustButton(nBut, false, addButton);
	}
	
	private void adjustButton(NativeButton nBut, boolean asFirst, boolean addButton){
		nBut.setHeight("40px");
		nBut.setStyleName("focBannerButton");
		if(addButton){
			if(centerHeaderLayout != null){
				if(asFirst){
					centerHeaderLayout.addComponentAsFirst(nBut);
				}else{
					centerHeaderLayout.addComponent(nBut);
				}
				centerHeaderLayout.setComponentAlignment(nBut, Alignment.TOP_LEFT);
			}
		}
	}
	
	public void init(){
	}

	public void refresh(){
		removeAllComponents();
		fillMainWindow();
	}
	
  public void removeHeaderBar() {
  	if(centerHeaderLayout != null) centerHeaderLayout.removeAllComponents();
  }

  public ICentralPanel newCentralPanel_BeforeLogin(){
    XMLViewKey xmlViewKey = new XMLViewKey(AdminWebModule.STORAGE_HOMEPAGE, XMLViewKey.TYPE_FORM, AdminWebModule.CONTEXT_BEFORE_LOGIN, XMLViewKey.VIEW_DEFAULT);
    ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) this, xmlViewKey, null);
    return centralPanel;
  }
  
  private String getSpecialDashboardContext() {
  	String specialDashboardContext = null;
		FocUser user = Globals.getApp() != null ? Globals.getApp().getUser_ForThisSession() : null;
		if (user != null && user.getGroup() != null) {
			specialDashboardContext = user.getGroup().getDashboardContext();
		}
		return specialDashboardContext;
  }
  
  public ICentralPanel newCentralPanel_AfterLogin(){
  	ICentralPanel centralPanel = null;
  	
  	XMLViewKey xmlViewKey = new XMLViewKey(AdminWebModule.STORAGE_HOMEPAGE, XMLViewKey.TYPE_FORM);
  	String specialDashboardContext = getSpecialDashboardContext();
  	if(!Utils.isStringEmpty(specialDashboardContext)) {
  		xmlViewKey.setContext(specialDashboardContext);
  		XMLView view = XMLViewDictionary.getInstance().get(xmlViewKey);//Check if key exists
  		if(view == null) xmlViewKey.setContext(XMLViewKey.CONTEXT_DEFAULT);//If not put back Main
  	}
  	
  	if(FocWebApplication.getInstanceForThread().isMobile()){
  		xmlViewKey.setMobileFriendly(true);
  	}
  	centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) this, xmlViewKey, null);
  	broadcastNotifyer_Init();
    return centralPanel;
  }

  private ICentralPanel newNavigationLayout_Internal(int navigationTreeType){
    XMLViewKey xmlViewKey = new XMLViewKey(FocMenuItemDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE);
    FVMenuTree centralPanel = (FVMenuTree) XMLViewDictionary.getInstance().newCentralPanel_NoParsing((FocCentralPanel) this, xmlViewKey, null);
    centralPanel.setTreeType(navigationTreeType);
    centralPanel.fill();
    centralPanel.parseXMLAndBuildGui();
    return centralPanel;
  }

  public ICentralPanel newNavigationPanel() {
    return newNavigationLayout_Internal(FVMenuTree.TYPE_NORMAL);
  }

  public ICentralPanel newAdministratorConsolePanel(){
    return newNavigationLayout_Internal(FVMenuTree.TYPE_ADMIN);
  }
  
  public ICentralPanel newHistoryPanel(){
    return newNavigationLayout_Internal(FVMenuTree.TYPE_HISTORY);
  }
  
	public boolean isMenuBarFilled() {
		return menuBarFilled;
	}

	public void setMenuBarFilled(boolean menuBarFilled) {
		this.menuBarFilled = menuBarFilled;
	}
	
	public NativeButton getMobileOptionsButton(){
		return mobileOptionsButton;
	}
	
	protected void executeAutomatedTesting() {
		FocUnitDictionary dictionary = FocUnitDictionary.getInstance();
		if (dictionary != null) {
			Globals.getApp().setIsUnitTest(true);
			try {
				dictionary.runSequence();
			} catch (Exception e) {
				Globals.logException(e);
			}
			Globals.getApp().setIsUnitTest(false);
		}
	}
	
  public void fillMenuBar_AfterLogin(){
  	if(!isMenuBarFilled()){
  		setMenuBarFilled(true);

  		if(FocWebApplication.getInstanceForThread().isMobile()){
  			mobileOptionsButton = newButtonInHeaderBar("", FocWebApplication.getInstanceForThread().isMobile());
  			mobileOptionsButton.setStyleName("mobileOptionsButton");
  			mobileOptionsButton.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_MENU));
  			mobileOptionsButton.setHeight("40px");
  			mobileOptionsButton.setStyleName("focBannerButton");
  			mobileOptionsButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						if(getCentralPanel() != null){
							getCentralPanel().optionButtonClicked();
						}
					}
				});
  		}
  		
  		home = newButtonInHeaderBar("", true);//Home
			home.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_HOME));
			home.addClickListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					menuBarIcons_Highlight((NativeButton)null);
					if(isGuestUser()){
						changeCentralPanelIntoGuestHomePage();
					}else{
						ICentralPanel centralPanel = newCentralPanel_AfterLogin();
		        changeCentralPanelContent(centralPanel, FocCentralPanel.PREVIOUS_REMOVE_ALL);
					}
				}
			});
			if(isNavigationVisible()){
//				navigation = newButtonInHeaderBar("Navigation", true);
				navigation = newButtonInHeaderBar("", true);
				navigation.setIcon(FontAwesome.TH_LIST);
				menuBarIcons_Add("_NAVIGATION_", navigation);
				navigation.addStyleName("foc-bold");

				if(FocWebApplication.getInstanceForThread().isMobile()){
					navigation.setCaption("");
					navigation.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_NAVIGATION));
				}
				navigation.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						menuBarIcons_Highlight((NativeButton) event.getButton());
		        ICentralPanel centralPanel = newNavigationPanel();
		        changeCentralPanelContent(centralPanel, FocCentralPanel.PREVIOUS_REMOVE_ALL);
					}
				});
			}
			
			addUnitTestingButtonIfAllowed();
  	
			addButtonsInMenuBar();
			
			if(!FocWebApplication.getInstanceForThread().isMobile()){
				addCompanyNameLabel();
			}
			
			if(ConfigInfo.isShowSignatureButton()) {
				pendingSignature = new FSignatureButton(this);
				adjustButton(pendingSignature, !FocWebApplication.getInstanceForThread().isMobile());
			}
			
			//NOTIF_DEV
			/*
			notificatonButton = new FNotificationButton(this);
			adjustButton(notificatonButton, !FocWebApplication.getInstanceForThread().isMobile());
			*/

			if(!FocWebApplication.getInstanceForThread().isMobile() && ConfigInfo.isContextHelpActive()){
				helpButton = new FVHelpButton(this);//Help
				adjustButton(helpButton, !FocWebApplication.getInstanceForThread().isMobile());
			}
			if(isNavigationVisible()){
				admin = newButtonInHeaderBar("", !FocWebApplication.getInstanceForThread().isMobile());//"Admin"
				admin.setIcon(FVIconFactory.getInstance().getFVIcon_Big(FVIconFactory.ICON_SETTINGS));
				admin.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						menuBarIcons_Highlight((NativeButton)null);
		        ICentralPanel centralPanel = newAdministratorConsolePanel();
		        changeCentralPanelContent(centralPanel, FocCentralPanel.PREVIOUS_REMOVE_ALL);
					}
				});
			}
			
			if(!FocWebApplication.getInstanceForThread().isMobile()){
				add_NewUserMenuBar();

//				if(FChatModule.userHasChat()) {
//					chatAlerts = newButtonInHeaderBar("", true);
//					chatAlerts.setIcon(FontAwesome.BELL);
//					updateChatAlertCount();
//					chatAlerts.addClickListener(new Button.ClickListener() {
//						public void buttonClick(ClickEvent event) {
//							buttonArray_Highlight(null);
//							
//							XMLViewKey key = new XMLViewKey(FChatJoin.DBNAME, XMLViewKey.TYPE_TABLE);
//							FChatJoin_Table table = (FChatJoin_Table) XMLViewDictionary.getInstance().newCentralPanel(FocWebVaadinWindow.this, key, null);
//							FocWebVaadinWindow.this.changeCentralPanelContent(table, true);
//							updateChatAlertCount();
////							if(isGuestUser()){
////								changeCentralPanelIntoGuestHomePage();
////							}else{
////								ICentralPanel centralPanel = newCentralPanel_AfterLogin();
////				        changeCentralPanelContent(centralPanel, FocCentralPanel.PREVIOUS_REMOVE_ALL);
////							}
//						}
//					});
//				}
			}else{
				logout = newButtonInHeaderBar("Log Out", !FocWebApplication.getInstanceForThread().isMobile());
	    
		    logout.addClickListener(new Button.ClickListener() {
		      public void buttonClick(ClickEvent event) {
		        if (getFocWebApplication() != null && getFocWebApplication().getFocWebSession() != null) {
		        	removeFocAllWindows();
							Globals.logString("DEBUG_SESSION_NOT_VALID FocWebVaadinWindow.buttonClick() calling Session Logout");
		        	getFocWebApplication().logout(FocWebVaadinWindow.this);
//		        	if(!Globals.getApp().isUnitTest()){
		        		getUI().getPage().setLocation( getUI().getPage().getLocation() );
//		        	}
		        	
		  	    	PerfManager.print();
		        }
		      }
		    });
			}
  	}
  }
  
  private boolean isNavigationVisible() {
		return Globals.getApp().isUnitTest() || 
				(Globals.getApp().getUser_ForThisSession() != null && Globals.getApp().getUser_ForThisSession().getSaasApplicationRole() == FocUserDesc.APPLICATION_ROLE_NONE);
	}

	public FVMenuBar getUserMenuBar(){
  	return menuBar_User;
  }
  
  private void add_NewUserMenuBar(){
  	menuBar_User = new FVMenuBar(null);
  	menuBar_User.setHeight("40px");
  	menuBar_User.setStyleName("focBannerButton");
    centerHeaderLayout.addComponent(menuBar_User);
    centerHeaderLayout.setComponentAlignment(menuBar_User, Alignment.MIDDLE_CENTER);
    
    FocWebApplication focWebApplication = getFocWebApplication();
    if(focWebApplication == null) focWebApplication = FocWebApplication.getInstanceForThread();
    FocUser focUser = focWebApplication.getFocWebSession().getFocUser();
    if(focUser != null){
    	String userFullname = focUser.getFullName();
    	if(userFullname.trim().isEmpty()) userFullname = focUser.getName();
    	
    	MenuItem userMenuItem = null;
    	if(Globals.isValo()){
    		userMenuItem = menuBar_User.addItem(userFullname, null);
    	}else{
    		userMenuItem = menuBar_User.addItem(userFullname, FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_DOWN_1), null);
    	}
    	userMenuItem.setStyleName("userItemCaption");
    	/*
    	if(!isGuestUser() && (SaaSConfig.getInstance() == null || SaaSConfig.getInstance().getApplicationType() == SaaSConfigDesc.APPLICATION_TYPE_NONE)){
	    	newUserMenuItem(userMenuItem, "Company Configuration", new MenuBar.Command() {
					
					@Override
					public void menuSelected(MenuItem selectedItem) {
		        XMLViewKey xmlViewKey = new XMLViewKey(FocGroupDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CONTEXT_COMPANY_CONFIGURATION, XMLViewKey.VIEW_DEFAULT);
		     	  ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(FocWebVaadinWindow.this, xmlViewKey, null);
		     	  changeCentralPanelContent(centralPanel, true);
					}
				});
    	}
    	*/
    	
    	newUserMenuItem(userMenuItem, "User account...", new MenuBar.Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					FocUser_HomePage_Form.popupUserCredintionals(FocWebVaadinWindow.this);						
				}
			});
    	
    	if(Globals.getApp().getUser_ForThisSession().isSaasAdmin()){
	    	newUserMenuItem(userMenuItem, "Manage Accounts...", new MenuBar.Command() {
					
					@Override
					public void menuSelected(MenuItem selectedItem) {
						FocList userList = FocUserDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
		        XMLViewKey xmlViewKey = new XMLViewKey(AdminWebModule.STORAGE_NAME_MANAGE_ACCOUNT, XMLViewKey.TYPE_FORM);
		     	  ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(FocWebVaadinWindow.this, xmlViewKey, userList);
		     	  changeCentralPanelContent(centralPanel, true);
					}
				});
    	}

    	newUserMenuItem(userMenuItem, "About...", new MenuBar.Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					if(FocWebServer.getInstance() != null){
						OptionDialog dialog = new OptionDialog("Software Version", FocWebServer.getInstance().getVersionTitle()){
							@Override
							public boolean executeOption(String optionName) {
								return false;
							}
						};
						dialog.addOption("OK", "Ok");
						dialog.setWidth("350px");
						dialog.setHeight("150px");
						OptionDialog_Form obtionForm = (OptionDialog_Form) Globals.popupDialog(dialog);
					
						FVVerticalLayout verticalLayout = obtionForm.getComponentsLayout();
						if(verticalLayout != null){
						}
					}
				}
			});

    	logoutMenuItem = newUserMenuItem(userMenuItem, "Log out", new MenuBar.Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					FocUnitDictionary.getInstance().setExitTesting(true);
					logout();
				}
			});
    	
    	if(FocUnitRecorder.isAllowRecording()){
    		macroRecordingItem = newUserMenuItem(userMenuItem, "Recod Macro", new MenuBar.Command() {
    			boolean recording = false;
    			
					@Override
					public void menuSelected(MenuItem selectedItem) {
						if(recording){
							recording = false;
							macroRecordingItem.setText("Start Recoding Macro");
							FocUnitRecorder.stopRecording();
						}else{
							recording = true;
							macroRecordingItem.setText("Stop Recording Macro");
							FocUnitRecorder.startRecording();
						}
					}
				});
    	}
    }
  }

  public void logout(){
		if (getFocWebApplication() != null && getFocWebApplication().getFocWebSession() != null) {
	  	removeFocAllWindows();
			Globals.logString("DEBUG_SESSION_NOT_VALID FocWebVaadinWindow.buttonClick() calling Session Logout");
	  	getFocWebApplication().logout(FocWebVaadinWindow.this);
	  	String location = getUI().getPage().getLocation().toString();
//	  	if(!Utils.isStringEmpty(unitTest) && !Utils.isStringEmpty(location)){
	  		/*
	  		int index = location.indexOf(FocWebApplication.URL_PARAMETER_KEY_UNIT_SUITE);
	  		if(index > 0){
	  			location = location.substring(0, index)+FocWebApplication.URL_PARAMETER_KEY_UNIT_SUITE+":"+unitTest;
	  		} else {
	  			location = location+FocWebApplication.URL_PARAMETER_KEY_UNIT_SUITE+":"+unitTest;
	  		}
	  		*/
//	  		FocWebServer.getInstance().setNextTestSuiteName(unitTest);
//	  	}
	  	getUI().getPage().setLocation(location);
	  	PerfManager.print();
	  }
  }
  
  private MenuItem newUserMenuItem(MenuItem userMenuItem, String caption, Command command){
  	MenuItem menuItem = userMenuItem.addItem(caption, command);
  	menuItem.setStyleName("userMenuItem");
  	return menuItem;
  }

  public Label newMidleTitleLabel(String companyName){
  	Label companyNameLabel = new Label(companyName);
  	companyNameLabel.setStyleName("focBannerButton");
  	if(!Globals.isValo() || ConfigInfo.isGuiRTL()){
	  	companyNameLabel.addStyleName("foc-f16");
  	}
	  companyNameLabel.addStyleName("foc-bold");
  	companyNameLabel.addStyleName("foc-text-center");
  	companyNameLabel.addStyleName("foc-text-middle");
  	companyNameLabel.addStyleName("foc-CompanyTitle");
//	  companyNameLabel.addStyleName("foc-text-bottom");
//  	companyNameLabel.setHeight("40px");
  	return companyNameLabel;
  }
  
  private String buildCompanyTitleString(){
  	String companyName = " ";
  	if(Globals.getApp() != null && Globals.getApp().getCurrentCompany() != null){
	  	companyName = Globals.getApp().getCurrentCompany().getName();
	  	
	  	FocUser user = Globals.getApp().getUser_ForThisSession();
	  	if(user != null){
	  		if(Globals.getApp().getCurrentSite() != null){
	  			companyName += " - " + Globals.getApp().getCurrentSite().getName();
	  		}
	  	}
  	}

  	return companyName;
  }
  
  private void addCompanyNameLabel(){
  	addCompanyNameLabel(buildCompanyTitleString());
  }

  public void addCompanyNameLabel(String companyName){
  	if(companyName == null) companyName = "";
  	if(centerHeaderLayout != null){
	  	companyNameLabel = newMidleTitleLabel(companyName);
//	  	companyNameLabel.setHeight("40px");
	  	centerHeaderLayout.setHeight("40px");
			centerHeaderLayout.addComponent(companyNameLabel);
			centerHeaderLayout.setExpandRatio(companyNameLabel, 1);	
			centerHeaderLayout.setComponentAlignment(companyNameLabel, Alignment.BOTTOM_LEFT);
  	}
  }

  public void adjustCompaneyName(){
  	if(companyNameLabel != null){
	  	String companyName = buildCompanyTitleString();
	  	
	  	setCompaneyName(companyName);
  	}
  	dispose_MenuTree();
  }
  
  public void setCompaneyName(String title){
  	if(companyNameLabel != null){
	  	companyNameLabel.setValue(title);
  	}
  }

  public FVHelpButton getHelpButton(boolean createIfNeeded){
  	if(helpButton == null && createIfNeeded){
			helpButton = new FVHelpButton(this);
  	}
  	return helpButton;
  }

  public NativeButton getLoginButton(){
  	return login;
  }
  
  public NativeButton getLogoutButton(){
    return logout;
  }
  
  public NativeButton getHomeButton(){
    return home;
  }
  
  public NativeButton getNavigationButton(){
    return navigation;
  }
  
  public FSignatureButton getPendingSignatureButton(){
    return pendingSignature;
  }

  public void refreshPendingSignatureCount() {
		FSignatureButton nativeButton = getPendingSignatureButton();
		if(nativeButton != null) nativeButton.reset(null);
  }

  public void resetPendingSignatureButtonCaption(WFTransactionWrapperList transactionWrapperList){
  	FSignatureButton nativeButton = getPendingSignatureButton();
  	if(nativeButton != null){
  		nativeButton.resetIfNeeded(transactionWrapperList);
  	}
  }
  
  protected int buttonTitleWithSigntureIndicator(FVButton menuButton, FocDesc focDesc) { 
		String buttonTitle = menuButton.getCaption();
		int count = WFTransactionWrapperList.getCountOfPendingSignatures(focDesc);
		if(count > 0) {
			buttonTitle += " - " + count;
			menuButton.removeStyleName("foc-orange");
			menuButton.addStyleName("foc-orange");
		}
		menuButton.setCaption(buttonTitle);
		return count;
  }
	
  public NativeButton getAdminButton(){
    return admin;
  }
  
  protected void prepareWindowBeforeLogin(){
		removeHeaderBar();
		hideMenuBar();
		
		addCompanyNameLabel();
		
		if(isIncludeLoginButtonInHeaderBar()){
			login = newButtonInHeaderBar("Log in", true);
	    login.addClickListener(new Button.ClickListener() {
	      public void buttonClick(ClickEvent event) {
	      	popupLogin();
	      }
	    });
		}
  	
  	showMenuBar();

  	addUnitTestingButtonIfAllowed();
  }
  
  public void addUnitTestingButtonIfAllowed() {
		// Auto-Testing Header Icon
		if (ConfigInfo.isUnitAllowed()) {
			NativeButton autoTestingIcon = newButtonInHeaderBar("", true);
			menuBarIcons_Add("_UNIT_", autoTestingIcon);
			autoTestingIcon.setIcon(FontAwesome.EXCLAMATION_TRIANGLE);
			autoTestingIcon.addClickListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					try {
						executeAutomatedTesting();
					} catch (Exception e) {
						Globals.logException(e);
					}
				}
			});
		}
  }
  
  /**
   * Prepares a new opened tab when user is already logged in  
   */
  protected void prepareWindowAfterLogin(){
		fillMenuBar_AfterLogin();
		showMenuBar();
  }

  protected void popupLogin(){
  	LoginWindow loginWindow = new LoginWindow();
  	loginWindow.init(FocWebVaadinWindow.this);
    getUI().addWindow(loginWindow);  	
  }

  public void loginWithUserAlreadyApproved(FocUser focUser){
    getFocWebApplication().getFocWebSession().setFocUser(focUser);
    if(getCentralHeader() != null){
    	getCentralHeader().removeAllComponents();
    }
    fillMenuBar_AfterLogin();
    ICentralPanel centralPanel = newCentralPanel_AfterLogin();
    changeCentralPanelContent(centralPanel, false);
    showMenuBar();

    //Check if the user is a guest user and take him to the dedicated screen
    //----------------------------------------------------------------------
    changeCentralPanelIntoGuestHomePage();
  }

	public void fillMainWindow(){
		FocWebSession focWebSession = FocWebApplication.getFocWebSession_Static();
    ICentralPanel centralPanel = openPopupForURLIfFound();
    if(centralPanel != null){
			if(focWebSession == null || focWebSession.getFocUser() == null){
				prepareWindowBeforeLogin();
		    changeCentralPanelContent(centralPanel, false);
			}else{
				prepareWindowAfterLogin();
				changeCentralPanelContent(newCentralPanel_AfterLogin(), false);
				changeCentralPanelContent(centralPanel, true);
			}

    	/*
    	prepareWindowBeforeLogin();
    	changeCentralPanelContent(centralPanel, false);
    	//This java script call to do a Reload was added as a work around to a bug:
    	//When we have a link to a MOM we get the scroll bars on the Attendees table, and I do not know why!! (I=Antoine) 
    	//but when we click the refresh, the scrolls disappear!
    	//So we added this refresh artificially. It is ugly but what can I do :)
    	JavaScript.getCurrent().execute("setTimeout(function() {  window.location.reload(true);}, 1000);");
    	*/
    }else{
			
			if(focWebSession == null || focWebSession.getFocUser() == null){
				prepareWindowBeforeLogin();
		    if(centralPanel == null){
		    	changeCentralPanelContent(newCentralPanel_BeforeLogin(), false);
		    }
			}else{
				if(!focWebSession.getUserSession().isLinkPageOnly()){
					prepareWindowAfterLogin();
					changeCentralPanelContent(newCentralPanel_AfterLogin(), false);
				}
			}
    }
	}
	
	public String getPathInfo(){
  	VaadinRequest vaadinRequest = VaadinService.getCurrent().getCurrentRequest();
  	String path = vaadinRequest != null ? vaadinRequest.getPathInfo() : null;
  	return path;
	}
	
	protected ICentralPanel openPopupForURLIfFound(){
		ICentralPanel centralPanel = FocWebApplication.getFocWebSession_Static().getInitialContectForm();
		FocWebApplication.getFocWebSession_Static().setInitialContectForm(null);
		if(centralPanel == null) {//Priority to sent ICentralPanel
			IFocData data = FocWebApplication.getFocWebSession_Static().getDataToPrint();
			XMLViewKey viewKey = FocWebApplication.getFocWebSession_Static().getViewKeyToPrint();
			if(data != null ) {
				centralPanel = XMLViewDictionary.getInstance().newCentralPanel(this, viewKey, data);
				if(centralPanel != null) {
					centralPanel.setFocDataOwner(FocWebApplication.getFocWebSession_Static().isFocDataOwner_ToPrint());
				}
				FocWebApplication.getFocWebSession_Static().removePrintingData();
			}
			
	  	String path = getPathInfo();
	  	if(path != null && path.toLowerCase().endsWith("/login")){
	  		if(Globals.getApp().getUser_ForThisSession() == null){
	  		  FocConstructor constr = new FocConstructor(FocUserDesc.getInstance(), null);
	  		  FocUser user = new FocUser(constr);
	  		  user.setDbResident(false);
	  		  XMLViewKey xmlViewKey = new XMLViewKey(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CTXT_LOGIN, XMLViewKey.VIEW_DEFAULT);
	  		  centralPanel = XMLViewDictionary.getInstance().newCentralPanel(this, xmlViewKey, user);
	  		}
	  	}else if(path != null && path.toLowerCase().startsWith("/popup/")){
	  		centralPanel = FocWebApplication.getFocWebSession_Static().getInitialContectForm();
	//      this.changeCentralPanelContent(newCentralPanel, true);
	  	}else if(path != null && path.toLowerCase().endsWith("/downloads")){
	  		FocList focList = DownloadableContentDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
	      XMLViewKey xmlViewKey = new XMLViewKey(DownloadableContentDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, DownloadableContentWebModule.CTXT_SPECIAL_URL, XMLViewKey.VIEW_DEFAULT);
	      centralPanel = XMLViewDictionary.getInstance().newCentralPanel(this, xmlViewKey, focList);
	      this.changeCentralPanelContent(centralPanel, true);
	  	}else if(path != null && path.toLowerCase().startsWith("/html:")){
	  		String htmlFileName = path.substring("/html:".length());
	  		
	  	}else if(path != null && !path.isEmpty() && path.length() > 1){
	  		path = path.substring(1);
	  		FocList focPageLinkList = FocPageLinkDesc.getList(FocList.LOAD_IF_NEEDED);
	    	
	    	FocPageLink foundFocPageLink = null;
	    	
	    	for(int i=0; i<focPageLinkList.size() && foundFocPageLink == null; i++){
	    		FocPageLink focPageLink = (FocPageLink) focPageLinkList.getFocObject(i);
	    		if(focPageLink != null){
	    			String dbKey = focPageLink.getKey().trim();
	    			if(path.equals(dbKey)){
	    				foundFocPageLink = focPageLink;
	    			}
	    		}
	    	}
	    	
	    	if(foundFocPageLink != null){
	    		FocDesc desc = Globals.getApp().getFocDescMap_ByFocObjectClassName().get(foundFocPageLink.getFocObjectClassName());
	    			
	  			if(Globals.getApp().getUser_ForThisSession() == null){
	  				FocWebApplication.getInstanceForThread().getFocWebSession().setFocUser(foundFocPageLink.getUser());
	  				FocWebApplication.getInstanceForThread().getFocWebSession().getUserSession().setLinkPageOnly(true);
	  				foundFocPageLink.getUser().setCurrentCompany(foundFocPageLink.getCompany());
	  				foundFocPageLink.getUser().setCurrentSite(foundFocPageLink.getSite());
	  				foundFocPageLink.getUser().setCurrentTitle(foundFocPageLink.getTitle());
	  			}
	  			
	  			FocObject focObject = null;
	  			if(desc != null){
			    	FocConstructor focConstructor = new FocConstructor(desc, null);
			    	focObject = focConstructor.newItem();
			    	focObject.setReference(foundFocPageLink.getTableRefernce());
			    	focObject.load();
		  			focObject.lockAllproperties();
	  			}
		    	
		  		XMLViewKey xmlViewKey = new XMLViewKey(foundFocPageLink.getViewStorageName(), foundFocPageLink.getViewType(), foundFocPageLink.getViewContext(), foundFocPageLink.getUserView());
		  		
		      centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) this, xmlViewKey, focObject, false, false, true, foundFocPageLink.getSerialisation());	      
		      if(centralPanel != null){
			      centralPanel.parseXMLAndBuildGui();
			      if(centralPanel instanceof FocXMLLayout){
			      	FocXMLLayout lay = (FocXMLLayout) centralPanel;
			      	lay.setValidationLayoutVisible(false);
			      }
		      }
	  		}
	  	}
		}
  	return centralPanel;
	}
	
	@Override
	public void addUtilityPanel(IRightPanel utilityPanel) {
		if(utilityPanel != null){
			emptyRightMarginPanel.addComponent((Component) utilityPanel);
		}
	}

	@Override
	public void removeUtilityPanel(IRightPanel utilityPanel) {
		if(utilityPanel != null){
			emptyRightMarginPanel.removeComponent((Component) utilityPanel);
		}
	}
	
	@Override
	protected void afterChangeCentralPanelContent(ICentralPanel iCentralPanel){
	}

	public int getFullScreenMode() {
		return format;
	}

	public void setFullScreenMode(int format) {
		this.format = format;
		if(format == FORMAT_FULL_SCREEN){
			if(emptyLeftMarginPanel != null && emptyRightMarginPanel != null){
				emptyLeftMarginPanel.setWidth("0px");
				emptyRightMarginPanel.setWidth("0px");
			}
			getCentralPanelWrapper().setWidth("100%");

			if(hMainLayout != null && emptyLeftMarginPanel != null && emptyRightMarginPanel != null){
				hMainLayout.setExpandRatio(getCentralPanelWrapper(), 1f);
				hMainLayout.setExpandRatio(emptyLeftMarginPanel, 0f);
				hMainLayout.setExpandRatio(emptyRightMarginPanel, 0f);
			}
		}else if(format == FORMAT_PORTRAIT){
			if(emptyLeftMarginPanel != null && emptyRightMarginPanel != null){
				emptyLeftMarginPanel.setWidth("100%");
				emptyRightMarginPanel.setWidth("100%");
			}
			getCentralPanelWrapper().setWidth(getPreferredWidth());
			
			if(hMainLayout != null && emptyLeftMarginPanel != null && emptyRightMarginPanel != null){
				hMainLayout.setExpandRatio(getCentralPanelWrapper(), 0f);
				hMainLayout.setExpandRatio(emptyLeftMarginPanel, 0.5f);
				hMainLayout.setExpandRatio(emptyRightMarginPanel, 0.5f);
			}
		}
	}
	
	@Override
  public void setPreferredWidth(String width) {
		super.setPreferredWidth(width);
    setFullScreenMode(getFullScreenMode());
  }

	public MenuItem getLogoutMenuItem() {
		return logoutMenuItem;
	}
	
	private boolean isGuestUser(){
		FocUser user = Globals.getApp().getUser_ForThisSession();
  	return user != null ? user.isGuest() : true;
	}
	
	public void changeCentralPanelIntoGuestHomePage(){
  	FocUser user = Globals.getApp().getUser_ForThisSession();
  	FocGroup group = user != null ? user.getGroup() : null;
  	if(group != null && !Utils.isStringEmpty(group.getStartupMenu())){
	  	FVMenuTree menuTree = new FVMenuTree();
	  	if(menuTree != null){
	  		menuTree.setTreeType(FVMenuTree.TYPE_NORMAL);
		  	menuTree.fill();
		  	FocMenuItem menuItem = (FocMenuItem) menuTree.getMenuList().searchByPropertyStringValue(FocMenuItemDesc.FLD_CODE, group.getStartupMenu());
		  	if(menuItem != null){
		  		menuItem.getMenuAction().actionPerformed(this, menuItem, 0);
		  	}
		  	menuItem.dispose();
	  	}
  	}
  }
	
  protected FVHorizontalLayout getMenuBarLayout(FocXMLLayout centralPanel){
  	FVHorizontalLayout horizontalLayout = centralPanel != null ? (FVHorizontalLayout) centralPanel.getComponentByName("MENU_ITEMS_LAYOUT") : null;
  	return horizontalLayout;
  }
  
	@Override
	public void fillHomepageShortcutMenu(FocXMLLayout centralPanel) {
		super.fillHomepageShortcutMenu(centralPanel);
		
		if (ConfigInfo.isUnitAllowed()) {
//		if(Globals.getApp().isUnitTest()){
			FVHorizontalLayout horizontalLayout = getMenuBarLayout(centralPanel);
	    if(horizontalLayout != null){
	    	FVButton isrButton = new FVButton("Unit LOG");
//	    	isrButton.addStyleName(FocXMLGuiComponentStatic.getButtonStyleForIndex(0));
	    	isrButton.addStyleName("foc-bold");
	    	isrButton.addStyleName("foc-f12");
	    	isrButton.setWidth("70px");
	    	isrButton.setHeight("20px");
	    	
	    	horizontalLayout.addComponent(isrButton);
	    	isrButton.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						FocUnitDictionary.getInstance().popupLogger(FocWebVaadinWindow.this);
					}
				});
	    }
		}
	}

	public void broadcastNotifyer_Init() {
		if (broadcastNotifyer == null) {
			broadcastNotifyer = new BroadcastNotifyer();
			broadcastNotifyer.init();
		}
	}

	public void broadcastNotifyer_Dispose() {
		if (broadcastNotifyer != null) {
			broadcastNotifyer.dispose();
			broadcastNotifyer = null;
		}
	}
		
	public void refreshAllSignatureCounts() {
		if(buttonsWithSignatureArray != null) {
			for(int i=0; i<buttonsWithSignatureArray.size(); i++) {
				ButtonWithPendingSignature button = buttonsWithSignatureArray.get(i);
				if(button != null) {
					button.refreshCaptionAndStyle();
				}
			}
		}
	}

	public class ButtonWithPendingSignature extends FVButton {
		private int     styleIndex          = 0;
		private FocDesc focDescForSigntures = null;
		private String  rawCaption          = "";
		
		public ButtonWithPendingSignature(String rawCaption, int styleIndex, FocDesc focDescForSigntures) {
			super(rawCaption);
			this.rawCaption = rawCaption;
			this.styleIndex = styleIndex;
			this.focDescForSigntures = focDescForSigntures;
			refreshCaptionAndStyle();
			
			if(buttonsWithSignatureArray == null) buttonsWithSignatureArray = new ArrayList<ButtonWithPendingSignature>();
			buttonsWithSignatureArray.add(this);
		}
		
		public void dispose() {
			if(buttonsWithSignatureArray != null) {
				buttonsWithSignatureArray.remove(this);
		  }
			super.dispose();
			focDescForSigntures = null;
		}
		
		public void refreshCaptionAndStyle() {
			int count = 0;
			String caption = rawCaption;
			if(focDescForSigntures != null) count = WFTransactionWrapperList.getCountOfPendingSignatures(focDescForSigntures);

			if(count > 0) caption += " - "+count;
			
			setCaption(caption);
			if(count > 0) {
				removeStyleName(FocXMLGuiComponentStatic.getButtonStyleForIndex(styleIndex));
				addStyleName("foc-button-orange");
			} else {
				removeStyleName("foc-button-orange");
				addStyleName(FocXMLGuiComponentStatic.getButtonStyleForIndex(styleIndex));
			}
		}
	}
	
//  public void updateChatAlertCount(){
//    String caption = ""; 
//    int count = FChatModule.unreadCount();
//    if(count > 0){
//    	caption = String.valueOf(count);
//    }
//  	chatAlerts.setCaption(caption);
//  }
}
