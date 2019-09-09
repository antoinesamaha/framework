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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.foc.ConfigInfo;
import com.foc.FocThreadLocal;
import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.dataSource.IExecuteResultSet;
import com.foc.db.DBManager;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.layouts.validationLayout.FVHelpLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.FocWebServer;
import com.foc.web.server.session.FocWebSession;
import com.foc.web.unitTesting.FocUnitDictionary;
import com.foc.webservice.FocWebService;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@PreserveOnRefresh
@Theme("focVaadinTheme")
//@Push(PushMode.MANUAL)
public abstract class FocWebApplication extends UI {

	public static final String ATT_WEB_SESSION = "FOC_WEB_SESSION";
	public static final String URL_PARAMETER_KEY_UNIT_SUITE = "unitsuite";

	private FocWebSession focSession_ForNonVaadinAndHTTP = null;
	
	private HttpSession httpSession = null;
	private boolean isMobile = false;
	private FocCentralPanel navigationWindow = null;
	private VerticalLayout footerLayout = null; 
	
	private FVValidationLayout validationLayoutBackup = null;
  
//  public abstract String          getApplicationTitle();
  public abstract String          getThemeName();
//  public abstract boolean       startApplicationServer();
    
//MultiTab  
//  private FocWebSession            focWebSession   = null;

  public FocCentralPanel newWindow(){
  	return new FocWebVaadinWindow();
  }
  
	public static FocWebApplication getInstanceForThread(){
		return (FocWebApplication) FocThreadLocal.getWebApplication();
//		return (FocWebApplication) FocWebApplication.getInstanceForThread();
	}
	
	//This is used in the Servlet case because the UI object is then created artificially not coming from Vaadin
	public static void setInstanceForThread(FocWebApplication focWebApplication){
		FocThreadLocal.setWebApplication(focWebApplication);
//		UI.setCurrent(focWebApplication);
	}
	
	public static FocWebSession getFocWebSession_Static(){
	  FocWebApplication app = getInstanceForThread();
	  return app != null ? app.getFocWebSession() : null;
	}

	public static FocUser getFocUser(){
	  FocWebSession session = getFocWebSession_Static();
	  return session != null ? session.getFocUser() : null;
  }

	public static boolean isGuestUser(){
		FocUser user = getFocUser();
		return user != null && user.isGuest();
	}
	
	private void setSessionIfEmpty(HttpSession httpSession){
		if(getFocWebSession() == null){
			setFocWebSession(new FocWebSession(httpSession));
		}
	}
	
	protected void init2(VaadinRequest request) {
		// Create the footer Layout with a simple Label
		VerticalLayout footerLayout = new VerticalLayout();
		{
			footerLayout.addComponent(new Label("This is the footer always visible"));
			footerLayout.setHeight("-1px");
		}
		
		// Create the Body Panel that contains a VerticalLayout itself
		Panel panel = new Panel();
		{
			panel.setSizeFull();// Panel size is undefined
			final VerticalLayout panelLayout = new VerticalLayout();
			panelLayout.setSizeUndefined();// Panel Layout size is undefined
			panel.setContent(panelLayout);
			
			// Fill the panel layout with lots of labels to exceed the window height 
			for(int i=0; i<200; i++){
				panelLayout.addComponent(new Label("Thank you for clicking"));
			}
		}

		// Put all in the the MainVerticalLayout
		final VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setSizeFull();// Main Layout is set to size Full so that it fills all the height
		mainVerticalLayout.addComponent(panel);
		mainVerticalLayout.addComponent(footerLayout);
		mainVerticalLayout.setExpandRatio(panel, 1);
		setContent(mainVerticalLayout);
	}
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		HttpSession httpSession = null;
		WrappedHttpSession wrapperHttpSession = VaadinSession.getCurrent() != null ? (WrappedHttpSession) VaadinSession.getCurrent().getSession() : null;
		if(wrapperHttpSession != null){
			httpSession = wrapperHttpSession.getHttpSession();
		}
				
		initialize(vaadinRequest, VaadinServlet.getCurrent().getServletContext(), httpSession, false);
		initializeGUI(vaadinRequest, VaadinServlet.getCurrent().getServletContext(), httpSession);
	}
	
	public void initialize(VaadinRequest vaadinRequest, ServletContext servletContext, HttpSession httpSession, boolean webServicesOnly) {
//		setTheme(FocVaadinTheme.THEME_NAME);
//		Page.getCurrent().setUriFragment("01barmaja");
		Globals.logString("FocWebApplication.init 111");
		setErrorHandler(new DefaultErrorHandler() {
			@Override
	    public void error(com.vaadin.server.ErrorEvent event) {
				Globals.logString("Error - 1");
				Throwable throwable = event != null ? event.getThrowable() : null;
				Globals.logString("Error - 2");
				if(throwable != null && throwable instanceof Exception){
					Globals.logString("Error - 3");
					Globals.logException((Exception)throwable);
					Globals.logString("Error - 4");
				}
				
        // Do the default error handling (optional)
        doDefault(event);
	    } 
		});
		
		String userAgent = vaadinRequest != null ? vaadinRequest.getHeader("User-Agent") : null;
		isMobile = Utils.isMobile(userAgent);
		Globals.logString("FocWebApplication.init 2");
//		isMobile = Globals.isTouchDevice();
		
		String path = vaadinRequest != null ? vaadinRequest.getPathInfo() : null;
		Globals.logString("FocWebApplication.init 3");
  	if(path != null && !path.isEmpty() && path.length() > 1){
  		path = path.substring(1);
  		isMobile = path.toLowerCase().trim().equals("m");
  	}
  	Globals.logString("FocWebApplication.init 4");
  	
  	addStyleName("focMainWindow");
  	
  	Globals.logString("FocWebApplication.init 5");
  	
		//		FocWebApplication.setInstanceForThread(this);
		setSessionIfEmpty(httpSession);
		Globals.logString("FocWebApplication.init 6");
		startApplicationServer(servletContext, webServicesOnly);
		Globals.logString("FocWebApplication.init 7");
		FocWebServer focWebServer = FocWebServer.getInstance();
		if(focWebServer == null){
			Globals.logString("FocWebApplication.init 8 - SERVER IS NULL");
		}else{
			Globals.logString("FocWebApplication.init 8 - SERVER OK");
		}
		focWebServer.addApplication(this);
		Globals.logString("FocWebApplication.init 9");
	}
	
	public void initializeGUI(VaadinRequest vaadinRequest, ServletContext servletContext, HttpSession httpSession) {
		String path = vaadinRequest != null ? vaadinRequest.getPathInfo() : null;
		navigationWindow = newWindow();

		if(Utils.isStringEmpty(Globals.getApp().getURL())){
			if(Page.getCurrent() != null) {
				URI url = Page.getCurrent().getLocation();
				Globals.getApp().setURL(url.toString());
			}
		}
		
		applyUserThemeSelection();
		if(Globals.isValo() && !isPrintUI()){
			
			navigationWindow.setHeightUndefined();
			navigationWindow.setHeight("100%");
			FocXMLGuiComponentStatic.setCaptionMargin_Zero(navigationWindow);

			//PANEL
//			Panel bodyPanel = new Panel();
//			bodyPanel.setSizeFull();
//			bodyPanel.setContent(navigationWindow);
			//-----
			
			footerLayout = new VerticalLayout();
			footerLayout.setHeight("-1px");
			//footerLayout.addComponent(new Label(""));

			VerticalLayout mainVerticalLayout = new VerticalLayout();
			mainVerticalLayout.setSizeFull();// Main Layout is set to size Full so that it fills all the height
			
			//PANEL
//			mainVerticalLayout.addComponent(bodyPanel);
//			mainVerticalLayout.setExpandRatio(bodyPanel, 1);
			//-----
			mainVerticalLayout.addComponent(navigationWindow);
			mainVerticalLayout.setExpandRatio(navigationWindow, 1);
			mainVerticalLayout.addComponent(footerLayout);
			setContent(mainVerticalLayout);
		}else{
			setContent(navigationWindow);
			footerLayout = null;
		}

		initAccountFromDataBase();

		if(getNavigationWindow() != null){
			INavigationWindow window = getNavigationWindow();
			
			URI uri = Page.getCurrent().getLocation();
			
	    //Make sure the environment allows unit testing			
			if(ConfigInfo.isUnitAllowed() && uri.getHost().equals("localhost")){
				//If there are no test indexes already then we need to check the URL for test request
				if(!FocUnitDictionary.getInstance().hasNextTest()) {
					String suiteName = null;
					String testName  = null;
					
			  	if(path != null && path.toLowerCase().startsWith(URL_PARAMETER_KEY_UNIT_SUITE+":")){
			  		suiteName = path.substring((URL_PARAMETER_KEY_UNIT_SUITE+":").length());

				  	if(!Utils.isStringEmpty(suiteName)) {
			  			int indexOfSuperior = suiteName.indexOf(".");
			  			if(indexOfSuperior > 0){
			  				testName  = suiteName.substring(indexOfSuperior+1, suiteName.length());
			  				suiteName = suiteName.substring(0, indexOfSuperior);
			  			}
			  			
			  			FocUnitDictionary.getInstance().initializeCurrentSuiteAndTest(suiteName, testName);
				  	}
			  	}
				}

				if(FocUnitDictionary.getInstance().hasNextTest()) {
		  		try{
		  			Globals.getApp().setIsUnitTest(true);
						FocUnitDictionary.getInstance().runSequence();
		  		}catch(Exception e){
		  			Globals.logException(e);
		  		}finally {
//		  			if(		 !FocUnitDictionary.getInstance().isPause()
//		  					&& !FocUnitDictionary.getInstance().isNextTestExist()
//		  					){
//		  				FocUnitDictionary.getInstance().popupLogger(window);
//		  			}
		  			Globals.getApp().setIsUnitTest(false);
					}
				}
			}
		}
		//--------------------------------------------
	}
	
	private void initAccountFromDataBase(){
		if(Globals.getApp() != null && Globals.getApp().getDataSource() != null && Globals.getDBManager().getProvider() == DBManager.PROVIDER_MYSQL){
			String INITIAL_SAAS_CONFIG_DB_NAME = "_initial_saas_config";
			StringBuffer checkIfTableExists = new StringBuffer("SHOW TABLES LIKE '" + INITIAL_SAAS_CONFIG_DB_NAME + "'");
			ArrayList<String> retivedDataFromInitialSaasConfigList = Globals.getApp().getDataSource().command_SelectRequest(checkIfTableExists);
			boolean doesInitialSaasConfigTableExists = retivedDataFromInitialSaasConfigList.size() > 0;
			
			if(doesInitialSaasConfigTableExists){
				StringBuffer selectFromInitialSaasConfigStringBuffer = new StringBuffer("select ");
				selectFromInitialSaasConfigStringBuffer.append("account_ref, ");
				selectFromInitialSaasConfigStringBuffer.append("company_name, ");
				selectFromInitialSaasConfigStringBuffer.append("unsername, ");
				selectFromInitialSaasConfigStringBuffer.append("password, ");
				selectFromInitialSaasConfigStringBuffer.append("currency, ");
				selectFromInitialSaasConfigStringBuffer.append("application_type, ");
				selectFromInitialSaasConfigStringBuffer.append("application_plan, ");
				selectFromInitialSaasConfigStringBuffer.append("user_role, ");
				selectFromInitialSaasConfigStringBuffer.append("renewed_until ");
				selectFromInitialSaasConfigStringBuffer.append("from " + INITIAL_SAAS_CONFIG_DB_NAME);
  			
				Globals.getApp().getDataSource().executeCustomQuery(selectFromInitialSaasConfigStringBuffer, new IExecuteResultSet() {
					
					private ArrayList<String> retivedDataFromInitialSaasConfigList = null;
					
					@Override
					public void executeResultSet(int rowIndex, int columnIndex, String value) {
						if(retivedDataFromInitialSaasConfigList == null){
							retivedDataFromInitialSaasConfigList = new ArrayList<String>();
						}
						retivedDataFromInitialSaasConfigList.add(value);
					}

					@Override
					public void afterResultSetFinished() {
						if(retivedDataFromInitialSaasConfigList != null){
							FocWebService focWebService = FocWebServer.getInstance().newFocWebService();
		  				focWebService.initAcount(FocWebApplication.this,
		  						retivedDataFromInitialSaasConfigList.get(1), 
		  						retivedDataFromInitialSaasConfigList.get(2), 
		  						retivedDataFromInitialSaasConfigList.get(3), 
		  						retivedDataFromInitialSaasConfigList.get(4), 
		  						retivedDataFromInitialSaasConfigList.get(5), 
		  						retivedDataFromInitialSaasConfigList.get(6), 
		  						retivedDataFromInitialSaasConfigList.get(7), null);
		  				
		  				retivedDataFromInitialSaasConfigList.clear();
		  				retivedDataFromInitialSaasConfigList = null;
						}
					}
				});
				
				StringBuffer dropTable = new StringBuffer("DROP TABLE " + INITIAL_SAAS_CONFIG_DB_NAME);
				Globals.getApp().getDataSource().command_ExecuteRequest(dropTable);
				
			}else{
				Globals.logString("Table '_initial_saas_config' doesn't exist");
			}
		}
	}
	
	public void applyUserThemeSelection(){
		boolean valo = Globals.isValo();
		/*if(valo){
			setTheme("foctheme_Valo");
		}else{
		}*/
		setTheme(getThemeName());
//		setTheme(FocVaadinTheme.THEME_NAME);
	}
	
	public INavigationWindow getNavigationWindow(){
		return navigationWindow;
	}
	
	@Override
	public Component getContent() {
		return super.getContent();
	}
	
	public void dispose(){
		try {
			if(validationLayoutBackup != null){
				validationLayoutBackup.dispose();
				validationLayoutBackup = null;
			}
			if(navigationWindow != null) {
				navigationWindow.dispose();
				navigationWindow = null;
			}
			httpSession = null;
			footerLayout = null;
		}catch(Exception e) {
			Globals.logExceptionWithoutPopup(e);
		}
		//MultiTab
		/*
	  if(focWebSession != null){
	    focWebSession.dispose();
	    focWebSession = null;
	  }
	  */
	}

	public void logout(FocCentralPanel focCentralPanel){
		Globals.logString("DEBUG_SESSION_NOT_VALID FocWebApplication.logout()");
		if(focCentralPanel != null){
			focCentralPanel.dispose();
		}
		
		close();
		FocWebSession session = getFocWebSession();
		if(session != null){
			session.close();
		}
	}

	public String getSessionID_Debug(){
  	String id = "";

  	if(getHttpSession() != null){
  		id = "HTTP_"+getHttpSession().getId();
//  		focWebSession = (FocWebSession) getHttpSession().getAttribute("FOC_WEB_SESSION");
  	}else{
  		VaadinSession vaadinSession = getSession();
  		if(vaadinSession == null){
  			id = "VAAD_NULL";
  		}else{
  			if(vaadinSession.getSession() != null){
  				id = "VAAD_"+vaadinSession.getSession().getId();
  			}else{
  				id = "VAAD_X_NULL";
  			}
  		}
  	}
  	return id;
	}
	
  public FocWebSession getFocWebSession() {
  	//MultiTab
  	FocWebSession focWebSession = null;

  	if(getHttpSession() != null){
  		focWebSession = (FocWebSession) getHttpSession().getAttribute(ATT_WEB_SESSION);
  	}else{
  		VaadinSession vaadinSession = getSession();
    	focWebSession = vaadinSession != null ? (FocWebSession) vaadinSession.getAttribute(ATT_WEB_SESSION) : focSession_ForNonVaadinAndHTTP;
  	}
    return focWebSession;
  }
  
  public void setFocWebSession(FocWebSession focSession) {
  	//MultiTab  	
  	VaadinSession vaadinSession = getSession();
  	if(vaadinSession != null){
  		vaadinSession.setAttribute(ATT_WEB_SESSION, focSession);
//  		WrappedHttpSession wSession = (WrappedHttpSession) vaadinSession.getSession();
//  		setHttpSession(wSession.getHttpSession());
//  		setFocWebSession(getHttpSession(), focSession);
  	}else if(getHttpSession() != null){
  		getHttpSession().setAttribute(ATT_WEB_SESSION, focSession);
  	} else {
  		focSession_ForNonVaadinAndHTTP = focSession;
  	}
  }
  
  public void setFocWebSession(HttpSession httpSession, FocWebSession focSession) {
  	setHttpSession(httpSession);
  	if(httpSession != null){
  		httpSession.setAttribute(ATT_WEB_SESSION, focSession);	
  	}
  }
  
  private synchronized void startApplicationServer(ServletContext servletContext, boolean webServicesOnly){
  	FocWebServer server = null;

  	//    if(getFocWebSession() != null && getFocWebSession().getHttpSession() != null && getFocWebSession().getHttpSession().getServletContext() != null){
  	server = FocWebServer.connect(servletContext, webServicesOnly);
  	//    }

  	if(server != null){
	  	while(!server.isReady()){
	  		try{
	  			Thread.sleep(1000);
	  		}catch (InterruptedException e){
	  			Globals.logException(e);
	  		}
	  	}
  	}
  }
  
  public boolean isPrintUI(){
  	return false;
  }
  
  public boolean isMobile(){
  	return isMobile;
  }
  
  public void setIsMobile(boolean isMobile){
  	this.isMobile = isMobile;
  }
  
	public HttpSession getHttpSession() {
		return httpSession;
	}
	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	
	@Override
	public void addWindow(Window window) throws IllegalArgumentException, NullPointerException {
		boolean executedWithoutPopup = false;
		
		if(isMobile()){
			Object content = window.getContent();
			if(content instanceof ICentralPanel){
				if(getContent() != null && getContent() instanceof FocCentralPanel){
					window.setContent(null);
					FocCentralPanel centralComponent = (FocCentralPanel) getContent();
					
					centralComponent.changeCentralPanelContent((ICentralPanel) content, true);
					
					executedWithoutPopup = true;
				}
			}
		}
		
		if(!executedWithoutPopup){
			window.setModal(true);
			super.addWindow(window);
		}
	}
	
	@Override
	public void detach() {
		super.detach();
	
		//This was causing problem SESSION NOt VALID see debug points DEBUG_SESSION_NOT_VALID
/*
		FocCentralPanel focWebVaadinWindow = (FocCentralPanel) getContent();
		Globals.logString("DEBUG_SESSION_NOT_VALID FocWebApplication.detatch() calling Session Logout");
		logout(focWebVaadinWindow);
*/
	}

	public FVHelpLayout getHelpLayout(){
		FVHelpLayout helpLayout = null;
		
		if(footerLayout != null){
			if(footerLayout.getComponentCount() == 1 && footerLayout.getComponent(0) instanceof FVHelpLayout){
				helpLayout = (FVHelpLayout) footerLayout.getComponent(0);
			}
		}
		return helpLayout;
	}
	
	public boolean isHelpOn(){
		return getHelpLayout() != null;
	}
	
	public VerticalLayout setFooterLayoutToHelp(FVHelpLayout helpLayout) {
		if(helpLayout != null && footerLayout != null){
			if(footerLayout.getComponentCount() == 1 && footerLayout.getComponent(0) instanceof FVValidationLayout){
				validationLayoutBackup = (FVValidationLayout) footerLayout.getComponent(0);
			}
			replaceFooterLayout(helpLayout);
		}
		return footerLayout;
	}

	public void removeHelpFromFooterLayout(){
		FVHelpLayout helpLayout = getHelpLayout();
		replaceFooterLayout(validationLayoutBackup);
		helpLayout.dispose();
	}
	
	public VerticalLayout replaceFooterLayout(Component footerContent) {
		if(footerLayout != null){
			footerLayout.removeAllComponents();
			if(footerContent != null){
				footerLayout.addComponent(footerContent);
			}
		}
		return footerLayout;
	}
	
	public boolean hasModalWindowOverIt(Window myWindow){
		boolean hasOtherWindow = false;
		if(myWindow == null){//Today we do not block if a window has another window over it. 
			//We block only when window over main application window.
			Collection<Window> coll = getWindows();
			Iterator<Window> iter = coll.iterator();
			while(iter != null && iter.hasNext()){
				Window window = iter.next();
				hasOtherWindow = window != myWindow;
			}
		}
		return hasOtherWindow;
	}
}
