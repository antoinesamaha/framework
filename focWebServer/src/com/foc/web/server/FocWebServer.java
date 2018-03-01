package com.foc.web.server;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;

import com.foc.Application;
import com.foc.ConfigInfo;
import com.foc.FocMainClass;
import com.foc.FocThreadLocal;
import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.GrpWebModuleRights;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.adrBook.AdrBookPartyDesc;
import com.foc.business.company.Company;
import com.foc.business.company.CompanyDesc;
import com.foc.business.company.UserCompanyRightsDesc;
import com.foc.business.printing.ReportFactory;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.WFTitle;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.business.workflow.rights.RightLevel;
import com.foc.business.workflow.rights.RightLevelDesc;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.dataDictionary.IFocDataResolver;
import com.foc.desc.FocDesc;
import com.foc.helpBook.FocHelpBook;
import com.foc.list.FocList;
import com.foc.saas.manager.SaaSConfig;
import com.foc.shared.IFocWebModuleShared;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocMobileModule;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.IApplicationConfigurator;
import com.foc.vaadin.IFocMobileModule;
import com.foc.vaadin.IFocWebModule;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationMore;
import com.foc.vaadin.gui.layouts.validationLayout.IValidationLayoutMoreMenuFiller;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.modules.downloadableContent.DownloadableContentWebModule;
import com.foc.web.modules.fab.FabEndUserWebModule;
import com.foc.web.modules.fab.FabWebModule;
import com.foc.web.modules.migration.MigrationWebModule;
import com.foc.web.modules.notifier.module.NotifierWebModule;
import com.foc.web.modules.photoAlbum.PhotoAlbumWebModule;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.server.session.FocWebSession;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.foc.web.unitTesting.FocUnitDictionary;
import com.foc.webservice.FocWebService;

import b01.everpro.web.modules.accountManagement.AccountManagementWebModule;

@SuppressWarnings("serial")
public class FocWebServer implements Serializable {
	
  public static final String SERVLET_CONTEXT_ATTRIBUTE_NAME_FOR_SERVER = "FOC_WEB_SERVER";
  
	private Application                         focApplication       = null;
	private ArrayList<FocWebApplication>        applicationArrayList = null;
	
	private XMLViewDictionary                   xmlViewDictionary    = null;
	private FocUnitDictionary                   focUnitDictionary    = null;
	private FocHelpBook                         focHelpBook          = null;
	
  private ArrayList<IApplicationConfigurator> configuratorArray    = null ;

  private HashMap<String, IFocMobileModule>   mobileModulesMap     = null ;
  private HashMap<String, IFocWebModule>      moduleMap            = null ;
  private boolean                             modulesDeclared      = false;
	private boolean                             ready                = false;
	private int                                 nextModuleOrder      = 1;
	private String                              versionTitle         = "";
	
	public FocWebServer(){
		applicationArrayList = new ArrayList<FocWebApplication>();
		FocThreadLocal.setWebServer(this);
		
		FocWebEnvironment focWebEnvironment = new FocWebEnvironment();
		Globals.setIFocNotification(focWebEnvironment);
		focWebEnvironment.setThemeName("everpro_theme");

		FocMainClass mainClass = newMainClass();
		
  	Application focApplication = mainClass.getApplication();
  	setFocApplication(focApplication);
		if(ConfigInfo.isAdaptDataModelAtStartup()){
			Globals.getApp().adaptDataModel(false, false);
		}

		declareModules_IfNeeded();
		
		if(SaaSConfig.getInstance() != null){
			SaaSConfig.getInstance().adaptUserRights();
		}

		//Check if there is no company we create the Empty one + Empty User
		//-----------------------------------------------------------------
		AdrBookParty adrBookParty = null;
		//Scan companies and give rights to 01Barmaja
		FocList companyList  = CompanyDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
		if(companyList != null){
			if(companyList.size() == 0){
				//We need to create a company if it is the first one
				Company company = (Company) companyList.newEmptyItem();
				company.setName("EMPTY");
				
				adrBookParty = (AdrBookParty) AdrBookPartyDesc.getInstance().getFocList().newEmptyItem();
				adrBookParty.setCode("P14-0001");
				adrBookParty.setName(AdrBookParty.EMPTY_PARTY);
				adrBookParty.validate(true);
				AdrBookPartyDesc.getInstance().getFocList().validate(true);
				company.setAdrBookParty(adrBookParty);
				FocGroup group = FocGroup.createIfNotExist(FocGroup.STANDARD);
				group.validate(true);
				FocUser user = FocUser.createIfNotExist(FocUser.EMPTY_USER, "", group);
				user.validate(true);
				company.validate(true);
				
				company.findOrCreateSite(WFSite.DEFAULT_SITE_NAME);
				companyList.add(company);
				
				companyCreated(company);
				//20151216
				//We need Create WFTransactionConfig after having a new company if there is no companies and it is just created 
				//because we where checking for the WFTransactionConfig in WFTransactionConfigDesc.afterAdaptTableModel() method in WFTransactionConfigDesc 
				//and in case of 'Saasprocurement' we don't have a company at that level.
				WFTransactionConfigDesc.completeListForAllCompanies();
				//-------
				companyList.validate(true);
			}
		}
		
		//Create the user 01BARMAJA if not exists and the group 01BARMAJA as well
		//-----------------------------------------------------------------------
		FocUser barmajaUser = null;
		FocGroup barmajaGroup = null;
		WFTitle superTitle = null;
		RightLevel rightLevel = null; 
				
		FocList focList = FocUserDesc.getList();
		focList.loadIfNotLoadedFromDB();
//		if(focList.size() == 0){
		if(ConfigInfo.isCreateAdminUserIfNotExist()){
			barmajaGroup = FocGroup.createIfNotExist("FOCADMIN");//"01BARMAJA"
			barmajaUser  = FocUser.createIfNotExist("FOCADMIN", "", barmajaGroup);//"01BARMAJA", "01BMJ"
			superTitle = WFTitleDesc.getInstance().findOrAddTitle(WFTitle.TITLE_SUPERUSER);
			rightLevel = RightLevelDesc.getInstance().findOrAddRightLevel(RightLevelDesc.ALL_RIGHTS);
		
			//Give 01BARMAJA the rights on all defined companies
			//--------------------------------------------------
			if(barmajaUser != null){
				for(int i=0; i<companyList.size(); i++){
					Company comp = (Company) companyList.getFocObject(i);
					barmajaUser.addCompanyRights(comp, UserCompanyRightsDesc.ACCESS_RIGHT_READ_WRITE);
					WFSite site = comp.findSite(WFSite.DEFAULT_SITE_NAME);
					if(site != null){
						site.findOrAddUserTransactionRight(superTitle, null, "", rightLevel);
						site.findOrAddOperator(superTitle, barmajaUser);
					}
					comp.validate(true);
				}
			}

			barmajaUser.validate(true);
			if(barmajaUser.getFatherSubject() != null) barmajaUser.getFatherSubject().validate(true);
			//-----------------------------------------------------------------------
		}
		
		modules_ScanAndAddJasperReports();
		modules_ScanAndAddXMLViews();
		modules_ScanAndDeclareLeafMenuItems();			
		modules_ScanAndAddWebModulesToGroupsForRights();
		modules_ScanAndAddMobileModulesToGroupsForRights();
		
		if(barmajaGroup != null){
			//Put the rights for the Admin module
			//-----------------------------------
			GrpWebModuleRights webModuleRight = barmajaGroup.getWebModuleRightsObject(AdminWebModule.MODULE_NAME);
			if(webModuleRight != null){
				webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION);
				webModuleRight.validate(true);
			}
			webModuleRight = barmajaGroup.getWebModuleRightsObject("SITES_AND_WORKFLOW");
			if(webModuleRight != null){
				webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION);
				webModuleRight.validate(true);
			}
			webModuleRight = barmajaGroup.getWebModuleRightsObject("FINANCE");
			if(webModuleRight != null){
				webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION);
				webModuleRight.validate(true);
			}
			webModuleRight = barmajaGroup.getWebModuleRightsObject("INVENTORY");
			if(webModuleRight != null){
				webModuleRight.setRight(GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION);
				webModuleRight.validate(true);
			}
	
			barmajaGroup.getWebModuleRightsList().validate(true);
			barmajaGroup.validate(true);
			FocList listOfGroups = FocGroup.getList(FocList.LOAD_IF_NEEDED);
			listOfGroups.validate(true);
		}
		//-----------------------------------
	
		FVValidationMore.getInstance().setValidationLayoutMoreMenuFiller(newValidationLayoutMoreFiller());
		
		fillDataDictionaryWithApplicationParameters();
		
		setReady(true);

		/*
		Binding binding = new Binding();
		binding.setVariable("foo", new Integer(2));
		GroovyShell shell = new GroovyShell(binding);

		shell.evaluate("groovy/GroovyScriptTest.groovy");
		*/
	}
	
	public void dispose(){
		if(applicationArrayList != null){
			for(int i=0; i<applicationArrayList.size(); i++){
				FocWebApplication app = applicationArrayList.get(i);
				app.dispose();
			}
			applicationArrayList.clear();
			applicationArrayList = null;
		}
		if(configuratorArray != null){
			for(int i=0; i<configuratorArray.size(); i++){
				IApplicationConfigurator app = configuratorArray.get(i);
				app.dispose();
			}
			applicationArrayList.clear();
			applicationArrayList = null;
		}		
	  if(moduleMap != null){
  	  moduleMap.clear();
  	  moduleMap = null;
	  }
	  if(mobileModulesMap != null){
	  	mobileModulesMap.clear();
	  	mobileModulesMap = null;
	  }
	  if(focHelpBook != null){
	  	focHelpBook.dispose();
	  	focHelpBook = null;
	  }
	  if(focUnitDictionary != null){
	  	focUnitDictionary.dispose();
	  	focUnitDictionary = null;
	  }
	  if(xmlViewDictionary != null){
	  	xmlViewDictionary.dispose();
	  	xmlViewDictionary = null;
	  }	  
	}

	protected FocMainClass newMainClass(){
		Globals.logString("Init Web Server");
  	String[] args = { "/IS_SERVER:1", "/nol:1"};
  	
  	FocMainClass main = new FocMainClass(args);
		main.init2(args);
		main.init3(args);
  	
  	return main;
	}
	
	protected void companyCreated(Company company){
	}
	
	protected IValidationLayoutMoreMenuFiller newValidationLayoutMoreFiller(){
		return new FocValidationLayoutMoreMenuFiller();
	}
	
  protected void fillDataDictionaryWithApplicationParameters(){
    FocDataDictionary.getInstance().putParameter("DEV_MODE", new IFocDataResolver() {
			public Object getValue(IFocData focData, ArrayList<String> arguments) {
				return (ConfigInfo.isDevMode()) ? "1" : "0";
			}
		});
	}
	
	public void makeDataAnonymous(){
		
	}
	
	public FocWebService newFocWebService(){
		return new FocWebService();
	}
	
	protected FocUnitDictionary newUnitDictionary(){
	  return new FocUnitDictionary();
	}
	
	public FocUnitDictionary getUnitDictionary(){
	  if(focUnitDictionary == null){
	    focUnitDictionary = newUnitDictionary();
	  }
	  return focUnitDictionary;
	}
	
	public XMLViewDictionary getXMLViewDictionary(){
	  if(xmlViewDictionary == null){
	  	xmlViewDictionary = new XMLViewDictionary();
	  }
	  return xmlViewDictionary;
	}

	protected FocHelpBook newHelpBook(){
	  return new FocHelpBook();
	}
	
	public FocHelpBook getHelpBook(){
	  if(focHelpBook == null){
	  	focHelpBook = newHelpBook();
	  }
	  return focHelpBook;
	}

	public void removeApplicationsNotRunning(){
		try {
			ArrayList<FocWebSession> sessionArray = new ArrayList<FocWebSession>();
			for(int i=getApplicationCount()-1; i>=0; i--){
				FocWebApplication app = getApplicationAt(i);
				if(app.isClosing()){
					FocWebSession webSession = app.getFocWebSession();
					if(webSession != null) sessionArray.add(webSession);
					app.dispose();
					applicationArrayList.remove(app);
				}
			}
	
			//Check if any of the sessions for which UI is disposed is still used.
			for(int i=getApplicationCount()-1; i>=0; i--){
				FocWebApplication app = getApplicationAt(i);
				if(!app.isClosing()){
					FocWebSession webSession = app.getFocWebSession();
					if(sessionArray.contains(webSession)){
						sessionArray.remove(webSession);
					}
				}
			}
			
			if(sessionArray != null) {
				//Here all FocWebSession in sessionArray are unused
				for(int i=0; i<sessionArray.size(); i++) {
					FocWebSession sess = sessionArray.get(i); 
					sess.dispose();
				}
			}
			sessionArray.clear();
		}catch(Exception e) {
			Globals.logException(e);
		}
	}
	
	public void addApplication(FocWebApplication app){
		removeApplicationsNotRunning();
		if(!applicationArrayList.contains(app)){
			applicationArrayList.add(app);
		}
	}

	public FocWebApplication getApplicationAt(int i){
		return applicationArrayList != null ? applicationArrayList.get(i) : null;
	}
	
	public int getApplicationCount(){
		return applicationArrayList != null ? applicationArrayList.size() : 0;
	}
	
	public static FocWebApplication findWebApplicationBySessionID(String sessionID, FocWebServer webServer){
		FocWebApplication webApplication_Found = null;

		webServer.removeApplicationsNotRunning();
		
    for (int i = 0; i < webServer.getApplicationCount(); i++) {
      FocWebApplication webApplication = webServer.getApplicationAt(i);

      if (webApplication != null) {
        FocWebSession currentWebSession = webApplication.getFocWebSession();

        if (currentWebSession != null) {
          String currentSessionID = currentWebSession.getId();

          if (sessionID.equals(currentSessionID)) {
          	webApplication_Found = webApplication;
            break;
          }
        }
      }
    }
    
    return webApplication_Found;
	}
	
	public static FocWebSession findWebSessionBySessionID(String sessionID, FocWebServer webServer){
		
    FocWebSession webSession = null;

    FocWebApplication webApplication = findWebApplicationBySessionID(sessionID, webServer);
    if(webApplication != null){
    	webSession = webApplication.getFocWebSession();
    }
    
    return webSession;
	}
	
	//------------------------------------------------------
	//Modules
	//------------------------------------------------------
	private HashMap<String, IFocWebModule> modules_GetMap(){
	  if(moduleMap == null){
	    moduleMap = new HashMap<String, IFocWebModule>();
	  }
	  return moduleMap;
	}
	
	public Iterator<IFocWebModule> newModuleIterator(){
	  return moduleMap != null ? moduleMap.values().iterator() : null;
	}
	
	private HashMap<String, IFocMobileModule> modulesMobile_GetMap(){
	  if(mobileModulesMap == null){
	  	mobileModulesMap = new HashMap<String, IFocMobileModule>();
	  }
	  return mobileModulesMap;
	}
	
	public Iterator<IFocMobileModule> newMobileModuleIterator(){
	  return mobileModulesMap != null ? mobileModulesMap.values().iterator() : null;
	}
	
	public void modules_Add(FocWebModule focWebModule){
	  if(focWebModule != null && !focWebModule.getName().isEmpty()){
	    modules_GetMap().put(focWebModule.getName(), focWebModule);
	    focWebModule.setOrder(getNextModuleOrder_AndIncrement());
	  }
	}
	
	public IFocWebModule modules_Get(String moduleName){
		IFocWebModule iFocWebModule = null;
	  if(modules_GetMap() != null && moduleName != null && !moduleName.isEmpty()){
	  	iFocWebModule = modules_GetMap().get(moduleName);
	  }
	  return iFocWebModule;
	}
	
	public void modulesMobile_Add(FocMobileModule focWebModule){
	  if(focWebModule != null && !focWebModule.getName().isEmpty()){
	  	modulesMobile_GetMap().put(focWebModule.getName(), focWebModule);
	    focWebModule.setOrder(getNextModuleOrder_AndIncrement());
	  }
	}
	
	public void modules_ScanAndAddXMLViews(){
	  int level = 0;
	  //Scan using the iterator. and declare only if the module level is equal to the level.  
	  
	  while(level >= 0){

	    int nextLevel = -1;
	    
  	  Iterator<IFocWebModule> iter = modules_GetMap().values().iterator();
  	  while(iter != null && iter.hasNext()){
  	    IFocWebModule focWebModule = iter.next();
  	    if(focWebModule.getPriorityInDeclaration() == level){ 
  	      focWebModule.declareXMLViewsInDictionary();
  	    }else{
  	      if(focWebModule.getPriorityInDeclaration() > level && (focWebModule.getPriorityInDeclaration() < nextLevel || nextLevel == -1)){
  	        nextLevel = focWebModule.getPriorityInDeclaration();
  	      }
  	    }
  	  }
	  
  	  level = nextLevel;
	  }
	  XMLViewDictionary.getInstance().loadViewsFromTable();
	}
	
	public void modules_ScanAndAddJasperReports(){
		ReportFactory.getInstance().loadReportsFromTable();
	}

  public void modules_ScanAndDeclareLeafMenuItems(){
    Iterator<IFocWebModule> iter = modules_GetMap().values().iterator();
    while(iter != null && iter.hasNext()){
      IFocWebModule focWebModule = iter.next();
      focWebModule.declareLeafMenuItems();
    }
    XMLViewDictionary.getInstance().loadViewsFromTable();
  }

	public void modules_ScanAndAddWebModulesToGroupsForRights(){
    FocList       listOfGroups = FocGroup.getList(FocList.LOAD_IF_NEEDED);
    for(int g=0; g<listOfGroups.size(); g++){
      FocGroup group = (FocGroup) listOfGroups.getFocObject(g);
      group.scanAndAddWebModulesToGroup();
    }
	}
	
	public void modules_ScanAndAddMobileModulesToGroupsForRights(){
    FocList       listOfGroups = FocGroup.getList(FocList.LOAD_IF_NEEDED);
    for(int g=0; g<listOfGroups.size(); g++){
      FocGroup group = (FocGroup) listOfGroups.getFocObject(g);
      modules_ScanAndAddMobileModulesToGroup(group);
    }
	}
	
	public Iterator<IFocWebModuleShared> modules_NewIterator(){
		return modules_GetMap() != null && modules_GetMap().values() != null ? (Iterator) (modules_GetMap().values().iterator()) : null;
	}

	public ArrayList<IFocWebModule> newModulesArrayList(){ 
		ArrayList<IFocWebModule> arrayList = new ArrayList<IFocWebModule>();
		arrayList.addAll(modules_GetMap().values());

		Collections.sort(arrayList, new Comparator<IFocWebModule>() {
			@Override
			public int compare(IFocWebModule arg0, IFocWebModule arg1) {
				return arg0 != null && arg1 != null ? arg0.getOrder() - arg1.getOrder() : 0;
			}
		});

		return arrayList;
	}
	
	public void modules_ScanAndAddMobileModulesToGroup(FocGroup group){
    Iterator<IFocMobileModule> iter = modulesMobile_GetMap().values().iterator();
    while(iter != null && iter.hasNext()){
      IFocMobileModule focWebModule = iter.next();
      if(group != null && focWebModule.getName() != null && !focWebModule.getName().isEmpty()){
        group.addMobileModule(focWebModule.getName(), focWebModule.getTitle());
      }
    }
	}
	
	public void declareModules_IfNeeded(){
    if(!modulesDeclared){
      declareModules();
      
      modulesDeclared = true;
    }
	}
	
  public void declareModules(){
    FocWebServer.getInstance().modules_Add(new FabWebModule());
    FocWebServer.getInstance().modules_Add(new FabEndUserWebModule());
    FocWebServer.getInstance().modules_Add(new AdminWebModule());
    FocWebServer.getInstance().modules_Add(new MigrationWebModule());
    FocWebServer.getInstance().modules_Add(new AccountManagementWebModule());
    FocWebServer.getInstance().modules_Add(new PhotoAlbumWebModule());
    FocWebServer.getInstance().modules_Add(new NotifierWebModule());
    FocWebServer.getInstance().modules_Add(new DownloadableContentWebModule());
    FocWebServer.getInstance().modules_Add(new WorkflowWebModule());
    
    //Here we declare the modules that are FocRelated
    //put aboolean that checks if the declaration is done or not.
    //Call this method from getWindow();
  }
  //--------------------------------------------------------
	
  //Application Configurators
  //-------------------------
  public void applicationConfigurators_Add(IApplicationConfigurator configurator){
  	if(configuratorArray == null){
  		configuratorArray = new ArrayList<IApplicationConfigurator>();
  	}
  	configuratorArray.add(configurator);
  }
  
  public int applicationConfigurators_Size(){
  	return configuratorArray != null ? configuratorArray.size() : 0;
  }
  
  public IApplicationConfigurator applicationConfigurators_Get(int at){
  	return configuratorArray != null ? configuratorArray.get(at) : null;
  }
  //-------------------------
	
	private static FocWebServer getInstance_FromServletContext(ServletContext servletContext){
	  FocWebServer server = null;
    if(servletContext != null){
    	Object obj = servletContext.getAttribute(FocWebServer.SERVLET_CONTEXT_ATTRIBUTE_NAME_FOR_SERVER);
      server = (FocWebServer) obj;
    }
	  return server;
	}

  private static void setInstance_InServletContext(ServletContext servletContext, FocWebServer server){
	  if(servletContext != null){
	    servletContext.setAttribute(FocWebServer.SERVLET_CONTEXT_ATTRIBUTE_NAME_FOR_SERVER, server);
	  }
	}

  private static FocWebServer newWebServer(){
    FocWebServer server = null;
    try {    
      ConfigInfo.loadFile();
      String serverClassName = ConfigInfo.getFocWebServerClassName();
      Class<FocWebServer> cls;

      cls = (Class<FocWebServer>) Class.forName(serverClassName);
      
      //DEBUGGING
//      if(cls != null){
//      	Class klass = cls;
//      	URL location = klass.getResource('/'+klass.getName().replace('.', '/')+".class");
//      	String path = location.toString();
//      	Globals.logString(path);
//      	
//      	klass = FocWebServer.class;
//      	location = klass.getResource('/'+klass.getName().replace('.', '/')+".class");
//      	path = location.toString();
//      	Globals.logString(path);
//      	
//      	klass = ConfigInfo.class;
//      	location = klass.getResource('/'+klass.getName().replace('.', '/')+".class");
//      	path = location.toString();
//      	Globals.logString(path);
//      }
      //---------
      
      if (serverClassName != null) {
        Class<FocDesc>[]  param = null;
        Object[] args = null;
        
        Constructor<FocWebServer> constr = cls.getConstructor(param);
        Object createdObject = constr.newInstance(args);
        if(createdObject != null && createdObject instanceof FocWebServer){
        	server = (FocWebServer) createdObject;
        }else{
        	int debug = 3;
        	debug++;
        }
      }
    } catch (Exception e) {
      Globals.logException(e);
    }
    return server;
  }
  
  public static synchronized FocWebServer connect(ServletContext servletContext){
    FocWebServer server = null; 
    if(servletContext != null){
    	try{
    		server = getInstance_FromServletContext(servletContext);	
    	}catch(Exception e){
    		Globals.logException(e);
    		server = getInstance_FromServletContext(servletContext);
    	}
      
      if(server == null){
        server = newWebServer();
        setInstance_InServletContext(servletContext, server);
      }else{
      	//This line was commented and I had to put it again for the Mobile applications second call to work
      	//Because then the server is not created with the session....
        FocThreadLocal.setWebServer(server);
      }
      if(FocWebApplication.getInstanceForThread() != null){
      	FocWebApplication.getInstanceForThread().setData(server);
      }
    }
    return server;
  }
  
  public static void disconnect(){
    FocThreadLocal.setWebServer(null);
  }
  
  public static FocWebServer getInstance(){
    FocWebServer  srv     = null;
    srv = (FocWebServer) FocThreadLocal.getWebServer();
    return srv;
  }

	public Application getFocApplication() {
		return focApplication;
	}

	public void setFocApplication(Application focApplication) {
		this.focApplication = focApplication;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public int getNextModuleOrder_AndIncrement() {
		return nextModuleOrder++;
	}

	public void setNextModuleOrder(int nextModuleOrder) {
		this.nextModuleOrder = nextModuleOrder;
	}

	public String getVersionTitle() {
		return versionTitle;
	}

	public void setVersionTitle(String versionTitle) {
		this.versionTitle = versionTitle;
	}
}
