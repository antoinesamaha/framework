/*
 * Created on Jul 20, 2005
 */
package com.foc;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.foc.business.status.StatusHolderDesc;
import com.foc.gui.DisplayManager;
import com.foc.gui.MainFrame;
import com.foc.gui.table.FTable;
import com.foc.util.Encryptor;
import com.foc.util.Utils;

/**
 * @author 01Barmaja
 */
public class ConfigInfo {
	
  public static final String JDBC_DRIVERS   = "jdbc.drivers";
  public static final String JDBC_URL       = "jdbc.url";
  public static final String JDBC_USERNAME  = "jdbc.username";
  public static final String JDBC_PASSWORD  = "jdbc.password";
  public static final String JDBC_XPASSWORD = "jdbc.Xpassword";
	
  private static boolean loaded = false;
  
  private static String  appDirectory  = null; 
  
  private static String  jdbcDrivers   = null; 
  private static String  jdbcURL       = null;  
  private static String  jdbcSchema    = null;
  private static String  jdbcUserName  = null;
  private static String  jdbcPassword  = null;
  private static String  jdbcXPassword = null;
  private static String  syncMode      = null;//N none, S server, R remote
  
  private static String  userName          = null;
  private static String  encriptedPassword = null;
  private static String  password          = null;
  private static boolean createAdminUserIfNotExist = false;
  
  private static String  windowTitle   = null;  

  private static int guiNavigatorWidth  = 0; 
  private static int guiNavigatorHeight = 0;
  private static int fontSize = 14;

  private static String  dataModelFileName    = null;
  private static String  helpURL              = null;
  private static String  logDir               = null;
  private static boolean rtl                  = false;
  private static boolean logDetails           = false;
  private static boolean logDebug             = false;
  private static boolean logConsoleActive     = true;
  private static boolean logDBRequest         = true;
  private static boolean logDBSelect          = true;
  private static boolean logFileActive        = false;
  private static boolean logMemoryUsage       = false;
  private static boolean logFileWithTime      = false;
  private static boolean unitDevMode          = true;
  private static boolean devMode              = false;
  private static boolean contextHelpActive    = false;
  private static boolean unitAllowed          = false;
  private static boolean popupExceptionDialog = false;  
  private static boolean showStatusColumn     = false;
  private static boolean performanceActive    = false;
  private static boolean withLicenseBooking   = true;
  private static boolean showFullScreenButton = false;
  private static String  codeProjectPath      = null;
  private static String  focDataServletURL    = null;
  private static String  focWebServerClassName = null;
  private static boolean adaptDataModelAtStartup = true;
  private static boolean adaptEnabled            = true;
  private static boolean adaptIndexesEnabled     = true;
  private static String  cloudStorageClassName = null;
  private static boolean useLocalEmailClientForNotification = false;
  private static boolean allowAddInsideComboBox  = true;
  private static boolean shrinkDBNames = false;
  
  private static boolean allowCSVExport          = true;
  private static boolean allowEXCELExport        = true;
  
  private static String defaultConfigInfoFilePath = "testDB/defaultConfig.properties";
  
  private static String reportFileFullPath = "d:/eclipseworkspace_c3/c3plugin_mg/defaultConfig.properties";
    
  private static Properties props = null; 
  
  private static String getProperty(String property){
  	String val = Globals.getApp() != null ? Globals.getApp().getCommandLineArgument(property) : null;
  	if(val == null || val.isEmpty()){
  		val = props != null ? props.getProperty(property) : null;
  	}
  	
  	return val;
  }
  
  public static boolean isGuiRTL(){
  	return rtl;
  }
  
  public static boolean isArabic(){
  	return isGuiRTL();
  }
  
  public static void loadFile() {
    if(!loaded){
      loaded = true;
      try{
        props = new Properties();
        //FileInputStream in = null;
        InputStream in = null;
        
//        String directory = Globals.getApp().getDefaultAppDirectory();
//        String environment = Globals.getApp().getDefaultEnvironment();
        String homePath = "";//(directory != null && environment != null) ? directory+"/"+environment : "";
        
        Globals.logString("reading file "+homePath+"properties/config.properties");
        in = Globals.getInputStream(homePath+"/properties/config.properties");
        
//        if(Globals.getApp().isWithRegistry() && in == null){
//          in = Globals.getInputStream(defaultConfigInfoFilePath);
//        }
        
        if( in == null ){
        	Globals.logString("Trying properties/config.properties");
          in = Globals.getInputStream("properties/config.properties");
        }
        if( in == null ){
        	Globals.logString("Trying /properties/config.properties");
          in = Globals.getInputStream("/properties/config.properties");
        }
        if( in == null ){
        	Globals.logString("Trying ./properties/config.properties");
          in = Globals.getInputStream("./properties/config.properties");
        }
        if( in == null ){
        	Globals.logString("Trying configuration.properties");
          in = Globals.getInputStream("configuration.properties");
        }
        
        if(in != null){
  	      props.load(in);
  	      in.close();
        }else{
        	Globals.logString("Could not load properties file!");
        	if(Globals.getDisplayManager() != null){
        		Globals.getDisplayManager().popupMessage("Could not load config file");
        	}
        	
        	String path = ConfigInfo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        	String decodedPath = URLDecoder.decode(path, "UTF-8");
        	Globals.logString("This JAR path is:"+decodedPath);
        }
  	      
        String str = null;
        
        appDirectory  = getProperty("appDirectory"); 
          
        jdbcDrivers   = getProperty(JDBC_DRIVERS);
        jdbcURL       = getProperty(JDBC_URL);
        jdbcUserName  = getProperty(JDBC_USERNAME);
        jdbcPassword  = getProperty(JDBC_PASSWORD);
        jdbcXPassword = getProperty(JDBC_XPASSWORD);
        Globals.logString("Drivers: "+jdbcDrivers);
        Globals.logString("URL: "+jdbcURL);
        
        try{
	        if( jdbcURL.indexOf("mysql") != -1 ){//MySQL
	        	//jdbc.url=jdbc:mysql://localhost:3306/marblestone?zeroDateTimeBehavior=convertToNull
	          String localhost = jdbcURL.substring(jdbcURL.indexOf("//", 10)+2, jdbcURL.indexOf(':', 13));
	          String port = jdbcURL.substring(jdbcURL.indexOf(':', 13)+1, jdbcURL.indexOf('/', 13));
	          String schema = jdbcURL.substring(jdbcURL.indexOf('/', 13)+1, jdbcURL.length());
	          
	          jdbcSchema = schema.split("\\?")[0];
	        }else if( jdbcURL.indexOf("sqlserver") != -1 ){//SQL Server
	        	//jdbc.url=jdbc:sqlserver://localhost:49172;instance=SQLEXPRESS;databaseName=isf_inspection;integratedSecurity=true;useUnicode=true;characterEncoding=UTF-8;
	        	//jdbc.url=jdbc:sqlserver://localhost:1433;databaseName=FenixInspection;integratedSecurity=true;useUnicode=true;characterEncoding=UTF-8;
	          String localhost = jdbcURL.substring(jdbcURL.indexOf("//", 10)+2, jdbcURL.indexOf(':', 16));
	          String port = jdbcURL.substring(jdbcURL.indexOf(':', 16)+1, jdbcURL.indexOf(';', 16));
	          
	          int schemaIndex = jdbcURL.indexOf("databaseName=");
	          if(schemaIndex > 0){
	          	schemaIndex += "databaseName=".length();
	          	
	          	int schemaEnd = jdbcURL.indexOf(";", schemaIndex);
	          	if(schemaEnd < schemaIndex) schemaEnd = jdbcURL.length();
	          	
	          	jdbcSchema = jdbcURL.substring(schemaIndex, schemaEnd);
	          }
	        }
        }catch(Exception e){
        	Globals.logException(e);
        }
        
        dataModelFileName = getProperty("dataModelFileName");
        
        codeProjectPath = getProperty("code.project.root");
        
        focDataServletURL = getProperty("focDataServletURL");

        String serverClassName = getProperty("focWebServerClassName");
        if(serverClassName == null){
        	serverClassName = "com.foc.web.server.FocWebServer";
        }
        focWebServerClassName = serverClassName;
        Globals.logString("WebServerClass: "+focWebServerClassName);
        
        String cloudClassName = getProperty("cloudStorageClass");
        if(cloudClassName == null){
        	cloudClassName = "com.foc.cloudStorage.FocCloudStorage_LocalDisc";
        }
        setCloudStorageClassName(cloudClassName);
        
        //Setting default navigator size to full screen
        //Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        //guiNavigatorWidth = (int)screenDim.getWidth();
        //guiNavigatorHeight = (int)screenDim.getHeight();
        //Globals.logString(new String(guiNavigatorWidth + ","+guiNavigatorHeight));
        //System.out.print(new String(guiNavigatorWidth + ","+guiNavigatorHeight));
              
        windowTitle = getProperty("gui.windowTitle");      
        str = getProperty("gui.navigator.width");
        if(str != null) guiNavigatorWidth = Integer.valueOf(str).intValue();
        str = getProperty("gui.navigator.height");      
        if(str != null) guiNavigatorHeight = Integer.valueOf(str).intValue(); 
        str = getProperty("gui.table.maxWidth");
        if(str != null) FTable.MAX_WIDTH = Integer.valueOf(str).intValue();
        
        str = getProperty("devMode");
        devMode = str != null ? str.compareTo("1") == 0 : false;

        str = getProperty("contextHelpActive");
        contextHelpActive = str != null ? str.compareTo("1") == 0 : false;
        
        str = getProperty("localEmailClient");
        useLocalEmailClientForNotification = str != null ? str.compareTo("1") == 0 : false;
        
        str = getProperty("unitDevMode");
        unitDevMode = str != null ? str.compareTo("1") == 0 : false;
        
        str = getProperty("unitAllowed");
        unitAllowed = str != null ? str.compareTo("1") == 0 : false;

        if(logDir == null){//In few cases we set the logDir from the main without passing by the config.properties
  	      logDir = getProperty("log.dir");
  	      if(logDir == null) logDir = ".";
        }
  
        helpURL = getProperty("help.URL");
        
        syncMode = getProperty("sync.mode");
        
        str = getProperty("log.fileActive");
        logFileActive = str != null ? str.compareTo("1") == 0 : false;

        str = getProperty("log.memoryUsage");
        logMemoryUsage = str != null ? str.compareTo("1") == 0 : false;//Default value is false log Memory usage

        str = getProperty("showFullScreenButton");
        showFullScreenButton = str != null ? str.compareTo("1") == 0 : false;//Default value is false showFullScreenButton

        str = getProperty("allowAddInsideComboBox");
        allowAddInsideComboBox = str != null ? str.compareTo("1") == 0 : true;//Default value is true

        str = getProperty("shrinkDBNames");
        shrinkDBNames = str != null ? str.compareTo("1") == 0 : false;//Default value is false

        str = getProperty("allowCSVExport");
        if(str != null && str.compareTo("0") == 0){//Default value is true
        	allowCSVExport = false; 
				}				
				
        str = getProperty("allowEXCELExport");
        if(str != null && str.compareTo("0") == 0){//Default value is true
        	allowEXCELExport = false;
        } 
        
        str = getProperty("log.fileWithTime");
        logFileWithTime = str != null ? str.compareTo("1") == 0 : false;
  
        str = getProperty("log.ConsoleActive");
        logConsoleActive = str != null ? str.compareTo("1") == 0 : false;
  
        str = getProperty("log.dbRequest");
        logDBRequest = str != null ? str.compareTo("1") == 0 : false;
  
        str = getProperty("perf.active");
        performanceActive = str != null ? str.compareTo("1") == 0 : false;

  //      str = getProperty("l");
  //      withLicenseBooking = str != null ? str.compareTo("1") == 0 : false;
  
        str = getProperty("log.dbSelect");
        logDBSelect = str != null ? str.compareTo("1") == 0 : false;

        str = getProperty("adaptDataModelAtStartup");
        adaptDataModelAtStartup = str != null ? str.compareTo("1") == 0 : true;

        str = getProperty("adaptEnabled");
        adaptEnabled = str != null ? str.compareTo("1") == 0 : true;

        str = getProperty("adaptIndexesEnabled");
        adaptIndexesEnabled = str != null ? str.compareTo("1") == 0 : true;

        str = getProperty("log.details");
        logDetails = str != null ? str.compareTo("1") == 0 : false;
  
        str = getProperty("gui.rtl");
        rtl = str != null ? str.compareTo("1") == 0 : false;
        
        str = getProperty("log.debug");
        logDebug = str != null ? str.compareTo("1") == 0 : false;
        
        str = getProperty("log.popupExceptionDialog");
        popupExceptionDialog = str != null ? str.compareTo("1") == 0 : false;
        
        str = getProperty("debug.showStatusColumn");
        showStatusColumn = str != null ? str.compareTo("1") == 0 : false;     
  
        str = getProperty("statusTitle.proposal");
        if(str != null && !str.isEmpty()){
        	StatusHolderDesc.PRINTED_LABEL_FOR_PROPOSAL = str;
        }
        
        userName          = getProperty("userLogin");
        password          = getProperty("password");
        encriptedPassword = getProperty("encriptedPassword");
      	if(encriptedPassword == null){
      		encriptedPassword = password;
      		encriptedPassword = Encryptor.encrypt_MD5(String.valueOf(encriptedPassword));
      	}
        
      	String createAdminUserIfNotExistString = getProperty("createAdminUserIfNotExist");
      	createAdminUserIfNotExist = 		createAdminUserIfNotExistString != null 
      															&& 	(
      																	   createAdminUserIfNotExistString.toUpperCase().equals("TRUE")
      																	|| createAdminUserIfNotExistString.equals("1")
      																	);
      	
        reportFileFullPath = getProperty("report.fileFullPath");
        
        str = getProperty("gui.font.size");
        if(str != null){
          fontSize = Utils.parseInteger(str, 14);  
        }
        DisplayManager dm = Globals.getDisplayManager();
        if(dm != null){
        	MainFrame mainFrame = dm.getMainFrame();
        	if(mainFrame != null){
        		mainFrame.setWindowTitle();
        	}
        }
      }catch(Exception e){
        e.printStackTrace();
        JOptionPane.showMessageDialog(Globals.getDisplayManager().getMainFrame(),
            "Error during config file load : properties/config.properties\n" + e.getMessage(),
            "01Barmaja",
            JOptionPane.ERROR_MESSAGE,
            null);
      }
    }
  }
  
  public static String getDataModelFileName(){
  	return dataModelFileName;
  }
  
  public static String getWindowTitle() {
    return windowTitle;
  }
  
  public static String getAppDirectory() {
    return appDirectory;
  }
  
  public static int getGuiNavigatorHeight() {
    return guiNavigatorHeight;
  }
  
  public static int getGuiNavigatorWidth() {
    return guiNavigatorWidth;
  }
  
  public static void setGuiNavigatorHeight(int guiNavigatorHeight) {
    ConfigInfo.guiNavigatorHeight = guiNavigatorHeight;
  }
  
  public static void setGuiNavigatorWidth(int guiNavigatorWidth) {
    ConfigInfo.guiNavigatorWidth = guiNavigatorWidth;
  }
  
  public static String getJdbcDrivers() {
    return jdbcDrivers;
  }
  
  public static String getJdbcPassword() {
    return jdbcPassword;
  }

  public static String getJdbcXPassword() {
    return jdbcXPassword;
  }
  
  public static String getJdbcURL() {
    return jdbcURL;
  }
  
  public static String getJdbcSchema(){
  	return jdbcSchema;
  }
  
  public static String getJdbcUserName() {
    return jdbcUserName;
  }

  public static boolean isDevMode() {
    return devMode;
  }
  
  public static boolean isContextHelpActive() {
    return contextHelpActive;
  }

  public static boolean isUnitDevMode() {
    return unitDevMode;
  }

  public static boolean isUnitAllowed() {
    return unitAllowed;
  }
  
  public static void setUnitAllowed(boolean allowed) {
    unitAllowed = allowed;
  }

  public static String getLogDir(){
  	return logDir;
  }

  public static String getHelpURL(){
  	return helpURL;
  }
  
  public static void setLogDir(String logDir){
  	ConfigInfo.logDir = logDir;
  }
  
  public static String getSyncMode(){
  	return syncMode != null ? syncMode : "N";//N none, S server, R remote
  }
  
  public static boolean isLogFileActive() {
    return logFileActive;
  }

  public static boolean isLogFileWithTime() {
  	return logFileWithTime;
  }

  public static void setLogFileWithTime(boolean withTime) {
  	logFileWithTime = withTime;
  }

  public static boolean isLogConsoleActive() {
    return logConsoleActive;
  }
  
  public static void setLogConsoleActive(boolean logConsActive) {
    logConsoleActive = logConsActive;
  }

  public static boolean isLogDBRequestActive() {
    return logDBRequest;
  }

  public static boolean isLogDBSelectActive() {
    return logDBSelect;
  }
  
  public static boolean isAdaptDataModelAtStartup() {
    return adaptDataModelAtStartup;
  }

  public static boolean isAdaptEnabled() {
    return adaptEnabled;
  }
  
  public static boolean isAdaptIndexesEnabled() {
    return adaptIndexesEnabled;
  }

  public static boolean isShowStatusColumn() {
    return showStatusColumn;
  }

  public static boolean isPopupExceptionDialog() {
    return popupExceptionDialog;
  }
  
  public static boolean isLogDetails(){
  	return logDetails;
  }

  public static boolean isLogDebug(){
  	return logDebug;
  }

  public static boolean isDebugMode(){
  	return isLogDebug();
  }

  public static int getFontSize() {
    return fontSize;
  }
  public static void setFontSize(int iFont){
    fontSize = iFont;
  }

  public static String getReportFileFullPath() {
    return ConfigInfo.reportFileFullPath;
  }

  public static void setReportFileFullPath(String reportFileFullPath) {
    ConfigInfo.reportFileFullPath = reportFileFullPath;
  }
  
  public static boolean isPerformanceActive() {
		return performanceActive;
	}

	public static void setPerformanceActive(boolean performanceActive) {
		ConfigInfo.performanceActive = performanceActive;
	}

	/**
	 * @return the withLicenseBooking
	 */
	public static boolean isWithLicenseBooking() {
		return withLicenseBooking;
	}
	
	public static void setWithLicenseBooking(boolean b) {
		withLicenseBooking = b;
	}

	public static boolean isForDevelopment() {
		return codeProjectPath != null && !codeProjectPath.isEmpty();
	}
	
	/**
	 * @return the codeProjectPath
	 */
	public static String getCodeProjectPath() {
		return codeProjectPath;
	}
	
	public static String getFocDataServletURL() {
		return focDataServletURL;
	}

  public static String getFocWebServerClassName() {
	  return focWebServerClassName;
	}

	public static boolean isRemoveUndeclaredIndexesDuringAdaptDataModel(){
		return false;
	}
	
	public static String getUsername(){
		return userName;
	}
	
	public static String getPassword(){
		return password;
	}
	
	public static String getEncryptedPassword(){
		return encriptedPassword;
	}

	public static String getCloudStorageClassName() {
		return cloudStorageClassName;
	}

	public static void setCloudStorageClassName(String cloudStorageClassName) {
		ConfigInfo.cloudStorageClassName = cloudStorageClassName;
	}

	public static boolean isLogMemoryUsage() {
		return logMemoryUsage;
	}

	public static void setLogMemoryUsage(boolean logMemoryUsage) {
		ConfigInfo.logMemoryUsage = logMemoryUsage;
	}

	public static boolean isUseLocalEmailClientForNotification() {
		return useLocalEmailClientForNotification;
	}

	public static void setUseLocalEmailClientForNotification(boolean useLocalEmailClientForNotification) {
		ConfigInfo.useLocalEmailClientForNotification = useLocalEmailClientForNotification;
	}
	
	public static boolean showFullScreenButton(){
		return showFullScreenButton;
	}

	public static boolean isAllowAddInsideComboBox() {
		return allowAddInsideComboBox;
	}

	public static void setAllowAddInsideComboBox(boolean allowAddInsideComboBox) {
		ConfigInfo.allowAddInsideComboBox = allowAddInsideComboBox;
	}

	public static boolean isAllowCSVExport() {
		return allowCSVExport;
	}

	public static void setAllowCSVExport(boolean allowCSVExport) {
		ConfigInfo.allowCSVExport = allowCSVExport;
	}

	public static boolean isAllowEXCELExport() {
		return allowEXCELExport;
	}

	public static void setAllowEXCELExport(boolean allowEXCELExport) {
		ConfigInfo.allowEXCELExport = allowEXCELExport;
	}

	public static boolean isCreateAdminUserIfNotExist() {
		return createAdminUserIfNotExist;
	}

	public static void setCreateAdminUserIfNotExist(boolean createAdminUserIfNotExist) {
		ConfigInfo.createAdminUserIfNotExist = createAdminUserIfNotExist;
	}

	public static boolean isShrinkDBNames() {
		return shrinkDBNames;
	}
	
	public static boolean comboBoxShowDropDownEvenWhenDisabled(){
		return true;
	}
}
