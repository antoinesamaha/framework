package com.foc.web.modules.notifier;

import com.foc.business.notification.FocNotificationDesc;
import com.foc.business.notifier.DocMsgDesc;
import com.foc.business.notifier.FocNotificationEmailDesc;
import com.foc.business.notifier.FocNotificationEmailTemplateDesc;
import com.foc.business.notifier.FocNotificationEventConfiguratorDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class NotifierWebModule extends FocWebModule {

//	public static final String CTXT_EMAIL_PDF       = "EmailPdf";
	
  public NotifierWebModule(){
    super("NOTIFICATIONS", "e-Mail Notifications");
    setAdminConsole(false);
  }
  
  public void declareXMLViewsInDictionary() {
  	XMLViewDictionary.getInstance().put(
      FocNotificationDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/notifier/FocNotification_Table.xml", 0, FocNotification_Table.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
      DocMsgDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/notifier/DocMsg_Form.xml", 0, DocMsg_Form.class.getName());
  	
    XMLViewDictionary.getInstance().put(
      FocNotificationEmailTemplateDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/notifier/FocNotificationEmailTemplate_Table.xml", 0, null);
    
    XMLViewDictionary.getInstance().put(
      FocNotificationEmailTemplateDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM,
      XMLViewKey.CONTEXT_DEFAULT,
      XMLViewKey.VIEW_DEFAULT,
      "/xml/com/foc/notifier/FocNotificationEmailTemplate_Form.xml", 0, null);
  
	  XMLViewDictionary.getInstance().put(
	    FocNotificationEventConfiguratorDesc.getInstance().getStorageName(),
	    XMLViewKey.TYPE_TABLE,
	    XMLViewKey.CONTEXT_DEFAULT,
	    XMLViewKey.VIEW_DEFAULT,
	    "/xml/com/foc/notifier/FocNotificationEvent_Notifier_Table.xml", 0, null);
	  
	  XMLViewDictionary.getInstance().put(
	    FocNotificationEmailDesc.getInstance().getStorageName(),
	    XMLViewKey.TYPE_FORM,
	    XMLViewKey.CONTEXT_DEFAULT,
	    XMLViewKey.VIEW_DEFAULT,
	    "/xml/com/foc/notifier/FocNotificationEmail_Form.xml", 0, Email_Form.class.getName());
	  
//	  XMLViewDictionary.getInstance().put(
//	    FocNotificationEmailDesc.getInstance().getStorageName(),
//	    XMLViewKey.TYPE_FORM,
//	    CTXT_EMAIL_PDF,
//	    XMLViewKey.VIEW_DEFAULT,
//	    "/xml/com/foc/notifier/FocNotificationEmail_EmailPdf_Form.xml", 0, FocNotificationEmail_EmailPdf_Form.class.getName());
  }

  public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
    FocMenuItem mainMenu = menuTree.pushRootMenu("Notifications", "Notifications");
    FocMenuItem menuItem = null;    
    
    menuItem = mainMenu.pushMenu("TEMPLATE_TABLE", "Email templates");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = FocNotificationEmailTemplateDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
        mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
    
    menuItem = mainMenu.pushMenu("EVENT_NOTIFIER_TABLE", "Triger Events");
    menuItem.setMenuAction(new IFocMenuItemAction() {
      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = FocNotificationEventConfiguratorDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
        mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
      }
    });
  }
}