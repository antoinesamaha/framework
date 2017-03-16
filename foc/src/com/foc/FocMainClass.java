package com.foc;

import java.util.Locale;

import com.fab.FabModule;
import com.fab.parameterSheet.ParameterSheetFactory;
import com.foc.admin.FocGroup;
import com.foc.admin.defaultappgroup.DefaultAppGroupDesc;
import com.foc.bsp.CodeMeterChecker;
import com.foc.bsp.IProduct;
import com.foc.business.BusinessModule;
import com.foc.business.calendar.CalendarModule;
import com.foc.business.currency.CurrencyModule;
import com.foc.business.multilanguage.AppLanguage;
import com.foc.business.multilanguage.MultiLanguage;
import com.foc.business.notifier.NotifierModule;
import com.foc.business.units.UnitModule;
import com.foc.business.workflow.WorkflowModule;
import com.foc.db.migration.MigrationModule;
import com.foc.ecomerce.ECommerceModule;
import com.foc.gui.DisplayManager;
import com.foc.link.LinkModule;
import com.foc.pivot.PivotModule;

public class FocMainClass {
	private Application app = null;

	public FocMainClass(String[] args){
	  init1(args);
  }
	
	public void dispose(){
		if(app != null){
			app.dispose();
			app = null;
		}
	}
	
	public boolean accessContinueInits(){
		return CodeMeterChecker.allowAccess();//CodeMeterChecker.allowAccess(CCCProduct.ESTIMATION);
	}
	
	protected IProduct newProduct(){
		return null;
	}
	
	public void init1(String[] args){
	  try {
    	ArgumentsHash argsHash = new ArgumentsHash(args);
    	boolean noGUI = argsHash.get("IS_SERVER") != null;
    	boolean noLicense = argsHash.get("nol") != null;
    	if(noLicense) ConfigInfo.setWithLicenseBooking(false);
    	CodeMeterChecker.getInstance(newProduct());
    	if(accessContinueInits()){
	      //CodeMeterChecker.getInstance().updateCertifiedTime();    		
    		Locale englishLocale = Locale.ENGLISH;
    		MultiLanguage.addLanguage(new AppLanguage(englishLocale.getDisplayLanguage(), englishLocale));
    		BusinessModule.getInstance().setMultiCompany(true);
    		
	      app = Globals.newApplication(true, true, noGUI ? DisplayManager.GUI_NAVIGATOR_NONE : DisplayManager.GUI_NAVIGATOR_MDI, args);
	      if(CodeMeterChecker.allowAccess()){
		      declareModules(app);
	    	}
    	}
    } catch (Exception e) {
      Globals.logException(e);
    }
	}
	
	public void init2(String[] args){
	  try {
    	if(accessContinueInits()){
	    	ConfigInfo.setLogFileWithTime(true);
	      app.setWithReporting(true);
	      
	      FocGroup.setApplicationGroup(DefaultAppGroupDesc.getInstance(), 1);
	    	ArgumentsHash argsHash = new ArgumentsHash(args);
	    	boolean webServer = argsHash.get("IS_SERVER") != null;
	      app.login(null, webServer);
    	}
    } catch (Exception e) {
      Globals.logException(e);
    }
	}

	public void init3(String[] args){
	  try {
    	if(accessContinueInits()){
    		app.entry();
    		if(CodeMeterChecker.isServer() && Globals.getDisplayManager() != null){
    			Globals.getDisplayManager().popupMessage("Your system is not protected correctly please contact 01Barmaja!");
    		}
    		//fillDesktopGraphicalMenu();
    	}
    } catch (Exception e) {
      Globals.logException(e);
    }
	}

	public Application getApplication(){
		return app;
	}
	  
  protected void declareModules(Application app){
//    CashierModule.getInstance().declare();

    //Common
    MigrationModule.getInstance().declare();
    UnitModule.getInstance().declare();
    
    FabModule.getInstance().declare();
    
//    BasicsModule.getInstance().declare();
//
//    PayrollModule.getInstance().declare();
//    RealPropertyManagementModule.getInstance().declare();
    
    WorkflowModule.getInstance().declare();
    NotifierModule.getInstance().declare();
    PivotModule.getInstance().declare();
    LinkModule.getInstance().declare();
    //------------------------
    
    ParameterSheetFactory.setEmptyParamSetAsDefaultParamSet();
    
//    ResourceModule.getInstance().declare();
//    BudgetModule.getInstance().declare();
//    ProcurementModule.getInstance().declare();
//    OrderManagementModule.getInstance().declare();
//    
//    WorkOrderModule.getInstance().declare();
//    
//    CashierModule.getInstance().declare();
    ECommerceModule.getInstance().declare();
    CurrencyModule.includeCurrencyModule();
    CalendarModule.getInstance().declare();

//    BudgetModule.getInstance().setBudgetReplacementTitle("Project");
  }

  public static String[] addArg(String args[], String argToAdd){
		if(args == null){
			args = new String[1];
			args[0] = argToAdd;
		}else{
			String args0[] = args; 
			args = new String[args0.length+1];
			for(int i=0; i<args0.length; i++){
				args[i] = args0[i];
			}
			args[args0.length] = argToAdd;
		}
		return args;
  }
}
