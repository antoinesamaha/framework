package com.foc;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc; 
import com.foc.business.status.StatusHolderDesc;
import com.foc.gui.DisplayManager;
import com.foc.gui.MainFrame;
import com.foc.gui.table.FTable;
import com.foc.util.Encryptor;
import com.foc.util.Utils;

public class LanguageConfigInfo {

	private static Properties englishProps = null; 
  
  private static Properties arabicProps = null;
  
  private static boolean loaded = false;
  
  public static String getProperty(String key, String language){
  	String val = "";
  	if(!Utils.isStringEmpty(key)){
  		if(language.equalsIgnoreCase("ar")) val = arabicProps.getProperty(key);
  		else if (language.equalsIgnoreCase("en")) val = englishProps.getProperty(key);
  	}
  	
  	return val;
  }
  
  public static void loadFiles() {
    if(!loaded){
      loaded = true;
      try{
        englishProps = new Properties();
        arabicProps = new Properties();

        InputStream englishIn = null;
        InputStream arabicIn = null;
        
        String homePath = "";
        
        // Reading english translation file
        Globals.logString("A-reading file "+homePath+"properties/languages/en.properties");
        englishIn = Globals.getInputStream(homePath+"/properties/languages/en.properties");
        
        if( englishIn == null ){
        	Globals.logString("B-Trying properties/languages/en.properties");
          englishIn = Globals.getInputStream("properties/languages/en.properties");
        }
        if( englishIn == null ){
        	Globals.logString("C-Trying /properties/languages/en.properties");
          englishIn = Globals.getInputStream("/properties/languages/en.properties");
        }
        if( englishIn == null ){
        	Globals.logString("D-Trying ./properties/languages/en.properties");
          englishIn = Globals.getInputStream("./properties/languages/en.properties");
        }
        if( englishIn == null ){
        	Globals.logString("E-Trying /languages/en.properties");
          englishIn = Globals.getInputStream("/languages/en.properties");
        }
        
        if(englishIn != null){
  	      englishProps.load(englishIn);
  	      englishIn.close();
        }else{
        	Globals.logString("--Could not load english translation file!");
        	if(Globals.getDisplayManager() != null){
        		Globals.getDisplayManager().popupMessage("--Could not load english translation file");
        	}
        	
        	String path = LanguageConfigInfo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        	String decodedPath = URLDecoder.decode(path, "UTF-8");
        	Globals.logString("--This JAR path is:"+decodedPath);
        }
        
        //Reading arabic translation file
        
        Globals.logString("A-reading file "+homePath+"properties/languages/ar.properties");
        arabicIn = Globals.getInputStream(homePath+"/properties/languages/ar.properties");
        
        if( arabicIn == null ){
        	Globals.logString("B-Trying properties/languages/ar.properties");
        	arabicIn = Globals.getInputStream("properties/languages/ar.properties");
        }
        if( arabicIn == null ){
        	Globals.logString("C-Trying /properties/languages/ar.properties");
        	arabicIn = Globals.getInputStream("/properties/languages/ar.properties");
        }
        if( arabicIn == null ){
        	Globals.logString("D-Trying ./properties/languages/ar.properties");
        	arabicIn = Globals.getInputStream("./properties/languages/ar.properties");
        }
        if( arabicIn == null ){
        	Globals.logString("E-Trying /languages/ar.properties");
        	arabicIn = Globals.getInputStream("/languages/ar.properties");
        }
        
        if(arabicIn != null){
  	      arabicProps.load(arabicIn);
  	      arabicIn.close();
        }else{
        	Globals.logString("--Could not load arabic translation file!");
        	if(Globals.getDisplayManager() != null){
        		Globals.getDisplayManager().popupMessage("--Could not load arabic translation file");
        	}
        	
        	String path = LanguageConfigInfo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        	String decodedPath = URLDecoder.decode(path, "UTF-8");
        	Globals.logString("--This JAR path is:"+decodedPath);
        }
      } catch(Exception e){
        e.printStackTrace();
        JOptionPane.showMessageDialog(Globals.getDisplayManager().getMainFrame(),
            "Error during translation files load : properties/languages\n" + e.getMessage(),
            "01Barmaja",
            JOptionPane.ERROR_MESSAGE,
            null);
      }    
    }   
  }
 
}