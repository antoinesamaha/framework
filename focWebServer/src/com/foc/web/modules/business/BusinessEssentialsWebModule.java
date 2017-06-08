package com.foc.web.modules.business;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.fab.gui.xmlView.IAddClickSpecialHandler;
import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.business.adrBook.AdrBookPartyDesc;
import com.foc.business.adrBook.ContactDesc;
import com.foc.business.calendar.DayShiftDesc;
import com.foc.business.calendar.FCalendarDesc;
import com.foc.business.calendar.HolidayDesc;
import com.foc.business.calendar.WeekShiftDesc;
import com.foc.business.calendar.WeekShiftExceptionDesc;
import com.foc.business.calendar.WorkShiftDesc;
import com.foc.business.config.BusinessConfig;
import com.foc.business.config.BusinessConfigDesc;
import com.foc.business.country.CountryDesc;
import com.foc.business.currency.Currencies;
import com.foc.business.currency.Currency;
import com.foc.business.currency.CurrencyDesc;
import com.foc.business.currency.DateLineDesc;
import com.foc.business.printing.PrnLayoutDesc;
import com.foc.business.units.DimensionDesc;
import com.foc.business.units.UnitDesc;
import com.foc.desc.dataModelTree.DataModelNodeDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.BlobResource;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.components.FVUploadLayout_Form;
import com.foc.vaadin.gui.components.menuBar.IUploadReader;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.IWebModuleMenuCode;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Window;

//This class needs to be extended by the global application (EVERPRO) to be included as a module.
public class BusinessEssentialsWebModule extends FocWebModule {

	private FocMenuItem businessEssentialsMenu = null; 
	private FocMenuItem adrBookMainMenu = null;
	
  public static final String CTXT_NEW_BUSINESS_CARD = "NewBusinessCard";
  
  public static final String MENU_CODE_ADR_BK_PARTY = IWebModuleMenuCode.BASICS_WEB_MODULE_MENU_CODE_ADR_BK_PARTY;
  
	public static final String STORAGE_FORMULA = "FORMULA";
	public static String STORAGE_UPLOAD_LAYOUT = "UPLOAD_LAYOUT";
	public static final String MODULE_NAME = "BUSINESS_ESSENTIALS";
	
	public static final String MENU_CUR_CURRENCY = "CUR_CURRENCY";
	public static final String MENU_CUR_RATES    = "CUR_RATES";
	
  public BusinessEssentialsWebModule(){
    super(MODULE_NAME, "Business essentials");
  }
  
  public FocGroup getFocGroup(){
		FocGroup group = null;
		if(Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null && Globals.getApp().getUser_ForThisSession().getGroup() != null){
			group = Globals.getApp().getUser_ForThisSession().getGroup();
		}
		return group;
	}
  
  public void declareXMLViewsInDictionary() {
  	XMLViewDictionary.getInstance().put(
      "CHART",
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/business/Chart_Form.xml", 0, Chart_Form.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
      PrnLayoutDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/business/PrnLayout_Table.xml", 0, PrnLayout_Table.class.getName());
  	
    XMLViewDictionary.getInstance().put(
      BusinessConfigDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/business/BusinessConfig_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      BusinessConfigDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/business/BusinessConfig_Form.xml", 0, BusinessConfig_Form.class.getName());
    
    XMLViewDictionary.getInstance().put(
      STORAGE_FORMULA,
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/business/FocFormula_Form.xml", 0, FocFormula_Form.class.getName());
    
    XMLViewDictionary.getInstance().put(
      DataModelNodeDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TREE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/business/FocDataModel_Tree.xml", 0, FocDataModel_Tree.class.getName());

    //-----------------------------------------------------------------------
    // Calendar
    //-----------------------------------------------------------------------
    
    XMLViewDictionary.getInstance().put(
      FCalendarDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/FCalendar_Table.xml", 0, FCalendar_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      FCalendarDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/FCalendar_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      WeekShiftExceptionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/WeekShiftException_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      WeekShiftExceptionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/WeekShiftException_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      HolidayDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/Holiday_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      HolidayDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/Holiday_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      WeekShiftDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/WeekShift_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      WeekShiftDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/WeekShift_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      DayShiftDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/DayShift_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      DayShiftDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/DayShift_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      WorkShiftDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/WorkShift_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      WorkShiftDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/calendar/WorkShift_Form.xml", 0, null);
      
    //-----------------------------------------------------------------------
    // Currency
    //-----------------------------------------------------------------------
    
    XMLViewDictionary.getInstance().put(
      CurrencyDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/currency/Currency_Table.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      CurrencyDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/currency/Currency_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
      Currencies.getFocDesc().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/currency/Currencies_Form.xml", 0, Currencies_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      DateLineDesc.TABLE_NAME,
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/currency/Dateline_Table.xml", 0, CurrencyDateLine_Table.class.getName());
        
    XMLViewDictionary.getInstance().put(
      DateLineDesc.TABLE_NAME,
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/currency/Dateline_Form.xml", 0, CurrencyDateLine_Form.class.getName());
    
    //-----------------------------------------------------------------------
    // Unit
    //-----------------------------------------------------------------------
    
    XMLViewDictionary.getInstance().put(
      DimensionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/unit/Dimension_Table.xml", 0, Dimension_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      DimensionDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/unit/Dimension_Form.xml", 0, Dimension_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      STORAGE_UPLOAD_LAYOUT,
      XMLViewKey.TYPE_FORM, 
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/unit/FVUploadLayout_Form.xml", 0, FVUploadLayout_Form.class.getName());
    
    //Adr Book Party
    //--------------
    XMLViewDictionary.getInstance().put(
        ContactDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/business/Contact_Table.xml", 0, Contact_Table.class.getName());
      
      XMLViewDictionary.getInstance().put(
        ContactDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_MOBILE,
        "/xml/com/foc/business/Contact_Mobile_Table.xml", 0, Contact_Table.class.getName());

      XMLViewDictionary.getInstance().put(
        ContactDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        CTXT_NEW_BUSINESS_CARD,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/business/Contact_NewBusinessCard_Form.xml", 0, Contact_NewBusinessCard_Form.class.getName());
      
      XMLViewDictionary.getInstance().put(
        ContactDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/business/Contact_Form.xml", 0, Contact_Form.class.getName());
      
      XMLViewDictionary.getInstance().put(
        ContactDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_MOBILE,
        "/xml/com/foc/business/Contact_Mobile_Form.xml", 0, Contact_Form.class.getName());
      
      XMLViewDictionary.getInstance().put(
        AdrBookPartyDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/business/AdrBookParty_Form.xml", 0, AdrBookParty_Form.class.getName());

      XMLViewDictionary.getInstance().put(
        AdrBookPartyDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_SIMPLE,
        "/xml/com/foc/business/AdrBookParty_Simple_Form.xml", 0, AdrBookParty_Form.class.getName());

      XMLViewDictionary.getInstance().put(
        AdrBookPartyDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_MOBILE,
        "/xml/com/foc/business/AdrBookParty_Mobile_Form.xml", 0, AdrBookParty_Form.class.getName());

      XMLViewDictionary.getInstance().put(
        AdrBookPartyDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_DEFAULT,
        "/xml/com/foc/business/AdrBookParty_Table.xml", 0, null);

      XMLViewDictionary.getInstance().put(
        AdrBookPartyDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE,
        XMLViewKey.CONTEXT_DEFAULT,
        XMLViewKey.VIEW_MOBILE,
        "/xml/com/foc/business/AdrBookParty_Mobile_Table.xml", 0, null);
    //--------------
    
    XMLViewDictionary.getInstance().putAddClickSpecialHandler(UnitDesc.getInstance().getStorageName(), new IAddClickSpecialHandler() {
			@Override
			public void addClicked(Object mainWindow, Object comboBox) {
				FocList focList = DimensionDesc.getList(FocList.FORCE_RELOAD);
        XMLViewKey key = new XMLViewKey(DimensionDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
        Dimension_Table centralPanel = (Dimension_Table) XMLViewDictionary.getInstance().newCentralPanel((INavigationWindow) mainWindow, key, focList);
        centralPanel.setComboBox((FVObjectComboBox) comboBox);
        ((INavigationWindow) mainWindow).changeCentralPanelContent(centralPanel, true);
			}
		});
  }

  public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
    	
    businessEssentialsMenu = menuTree.pushRootMenu("BUSINESS_ESSENTIALS", "Business essentials");
    FocMenuItem menuItem = null;    
    if(getFocGroup().getWebModuleRights(MODULE_NAME) == GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION){
	    menuItem = businessEssentialsMenu.pushMenu("BUSINESS_CONFIG", "Business Config");
	    menuItem.setMenuAction(new IFocMenuItemAction() {
	      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
	         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
	         
	         BusinessConfig businessConfig = BusinessConfig.getInstance();
	         
	         XMLViewKey key = new XMLViewKey(BusinessConfigDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM);
	         ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((INavigationWindow) navigationWindow, key, businessConfig);
	         mainWindow.changeCentralPanelContent(centralPanel, true);
	      }
	    });
    }
    
    /*
    menuItem = businessEssentialsMenu.pushMenu("User Setttings", "User Settings");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem) {
         Window mainWindow = (Window) navigationWindow;
         Window parent = mainWindow.getParent();
         if(parent != null){
           mainWindow = parent;
         }

         XMLViewKey key = new XMLViewKey(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CONTEXT_COMPANY_SELECTION, XMLViewKey.VIEW_DEFAULT);
         ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((INavigationWindow) navigationWindow, key, FocWebApplication.getFocUser());
         FocCentralPanel centralWindow = new FocCentralPanel();
         centralWindow.fill();
         centralWindow.changeCentralPanelContent(centralPanel, false);
         
         mainWindow.addWindow(centralWindow);
      }
    });
    */
    
  	menu_FillMenuTree_ForCalendar(menuTree, fatherMenuItem);
  	menu_FillMenuTree_Currency(menuTree, fatherMenuItem);
    menu_FillMenuTree_UnitAndDimensions(menuTree, fatherMenuItem);
    
    //----------------------------------------------------------------
    // Address book
    //----------------------------------------------------------------
    
    adrBookMainMenu = getBusinessEssentialsMenu().pushMenu("ADDRESS_BOOK", "Address book");

    menuItem = adrBookMainMenu.pushMenu(MENU_CODE_ADR_BK_PARTY, "Adr Book Party");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = AdrBookPartyDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = adrBookMainMenu.pushMenu("ADR_BK_CONTACT", "Contact");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = ContactDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
    
    menuItem = adrBookMainMenu.pushMenu("ADR_BK_CONTACT_NEW_BC", "Contact - New Business Card");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        
        XMLViewKey key = new XMLViewKey(ContactDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, CTXT_NEW_BUSINESS_CARD, XMLViewKey.VIEW_DEFAULT);
        ICentralPanel central = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, null);
        mainWindow.changeCentralPanelContent(central, true);
      }
    });

    menuItem = adrBookMainMenu.pushMenu("ADR_BK_COUNTRY", "Country");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = CountryDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
  }

  public void menu_FillMenuTree_ForCalendar(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
    FocMenuItem mainMenu = getBusinessEssentialsMenu().pushMenu("CALENDAR", "Calendar");
    FocMenuItem menuItem = null;    menuItem = mainMenu.pushMenu("CAL_CALENDAR", "Calendar");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = FCalendarDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("CAL_WEEK_SHIFT", "Week Shift");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = WeekShiftDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

    menuItem = mainMenu.pushMenu("CAL_DAY_SHIFT", "Day Shift");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = DayShiftDesc.getList(FocList.LOAD_IF_NEEDED);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
  }
  
  public void menu_FillMenuTree_Currency(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
    FocMenuItem mainMenu = getBusinessEssentialsMenu().pushMenu("CUR_CURRENCY_ROOT", "Currency");
    FocMenuItem menuItem = null;    
    
    menuItem = mainMenu.pushMenu("CUR_CONFIG", "module configuration");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        Currencies currencies = Currencies.getCurrencies();
        XMLViewKey key = new XMLViewKey(Currencies.getFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
        ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, currencies);
        
        if(!currencies.hasInitError()){
          FocXMLLayout layout = (FocXMLLayout) centralPanel;
          
          FocXMLGuiComponent comp = null;
          FocXMLAttributes attrib = null;
          
          comp = (FocXMLGuiComponent) layout.getComponentByName("INSTRUCTION_LABEL");
          if(comp != null){
            attrib = new FocXMLAttributes(layout, comp.getAttributes());
            attrib.addAttribute(FXML.ATT_VALUE, "The module was initialized and cannnot be modified any more, except for the default view currency.");
            comp.setAttributes(attrib);
          }
          
          comp = (FocXMLGuiComponent) layout.getComponentByName("BASE_CURR");
          if(comp != null){
            attrib = new FocXMLAttributes(layout, comp.getAttributes());
            attrib.addAttribute(FXML.ATT_EDITABLE, "false");
            comp.setAttributes(attrib);
          }
        }
        
        mainWindow.changeCentralPanelContent(centralPanel, true);
      }
    });
    
    menuItem = mainMenu.pushMenu(MENU_CUR_CURRENCY, "Currencies");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = Currency.getList(false);
        focList.loadIfNotLoadedFromDB();
        mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
    
    menuItem = mainMenu.pushMenu(MENU_CUR_RATES, "Rates");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;

        XMLViewKey key = new XMLViewKey(DateLineDesc.TABLE_NAME, XMLViewKey.TYPE_TABLE);
        ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, null);
        
        mainWindow.changeCentralPanelContent(centralPanel, true);
      }
    });
  }
 
  public void menu_FillMenuTree_UnitAndDimensions(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
    FocMenuItem mainMenu = getBusinessEssentialsMenu().pushMenu("Unit", "Units and Dimensions");
    FocMenuItem menuItem = null;
    menuItem = mainMenu.pushMenu("Dimension", "Units and Dimensions");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
         FocList focList = DimensionDesc.getList(FocList.FORCE_RELOAD);
         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });

  }

	public FocMenuItem getBusinessEssentialsMenu() {
		return businessEssentialsMenu;
	}
	
	public static FVUploadLayout_Form popupUploadLayout(IUploadReader iUploadReader){
		FocCentralPanel centralPanel = new FocCentralPanel();
    XMLViewKey key = new XMLViewKey(BusinessEssentialsWebModule.STORAGE_UPLOAD_LAYOUT, XMLViewKey.TYPE_FORM);
    FVUploadLayout_Form uploadLayout = (FVUploadLayout_Form) XMLViewDictionary.getInstance().newCentralPanel(centralPanel, key, null);
    uploadLayout.setUploadReader(iUploadReader);
    centralPanel.fill();
    centralPanel.changeCentralPanelContent(uploadLayout, true);
    Window window = centralPanel.newWrapperWindow();
    window.setWidth("400px");
    window.setHeight("300px");
    FocWebApplication.getInstanceForThread().addWindow(window);
    return uploadLayout;
	}
	
	public static void downloadFile(File file, String downloadFileName){
		try{
			if(file != null){
				byte[] fileBytes = new byte[(int) file.length()];
				FileInputStream fileInputStream = new FileInputStream(file);
				fileInputStream.read(fileBytes);
				fileInputStream.close();
				
		    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
		    BlobResource         blobResource         = new BlobResource(new File(""), byteArrayInputStream, downloadFileName);
				blobResource.openDownloadWindow();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public FocMenuItem getAdrBookMainMenu() {
		return adrBookMainMenu;
	}
}