
package com.foc.web.modules.photoAlbum;

import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.business.photoAlbum.DocTypeAccessDesc;
import com.foc.business.photoAlbum.DocumentTypeDesc;
import com.foc.business.photoAlbum.PhotoAlbumAccessDesc;
import com.foc.business.photoAlbum.PhotoAlbumAppGroup;
import com.foc.business.photoAlbum.PhotoAlbumConfig;
import com.foc.business.photoAlbum.PhotoAlbumConfigDesc;
import com.foc.business.photoAlbum.PhotoAlbumDesc;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class PhotoAlbumWebModule extends FocWebModule {
	
	public static final String MODULE_NAME                                = "ATTACHMENTS";
	public final static String CTXT_UNRELATED_ATTACHMENTS                 = "UNRELATED_ATTACHMENTS";
	public final static String CONTEXT_GROUP_ACCESS_RIGHT_SELECTION       = "GROUP_ACCESS_RIGHT_SELECTION";
	public static final String CTXT_ALL_DOCUMENTS                         = "AllDocuments";
	
	public static final String STORAGE_NAME_DOCUMENT_DASHBOARD = "DOCUMENT_DASHBOARD";
	
	public static final String MENU_CODE_ALL_DOCUMENTS      = "DOCUMENT_TABLE";
	public static final String MENU_CODE_DOCUMENT_TYPE      = "DOCUMENT_TYPES";
	public static final String MENU_CODE_DOCUMENT_DASHBOARD = "DOCUMENT_DASHBOARD";
	public static final String MENU_CODE_CONFIG             = "DOCUMENT_CONFIG";
	
  public PhotoAlbumWebModule() {
    super(MODULE_NAME, "Document Management");
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
  		PhotoAlbumConfigDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbumConfig_Form.xml", 0, PhotoAlbumConfig_Form.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
  		PhotoAlbumAppGroup.getFocDesc().getStorageName(),
      XMLViewKey.TYPE_FORM, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbumAppGroup_Form.xml", 0, PhotoAlbumAppGroup_Form.class.getName());
  	
  	XMLViewDictionary.getInstance().put(
      DocTypeAccessDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/DocTypeAccess_Table.xml", 0, DocTypeAccess_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbum_Table.xml", 0, PhotoAlbum_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
      PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      "Thumb", 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbum_Thumb_Table.xml", 0, PhotoAlbum_Thumb_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
    	DocumentTypeDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/DocumentType_Table.xml", 0, DocumentType_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
    	DocumentTypeDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/DocumentType_Form.xml", 0, DocumentType_Form.class.getName());
    
    XMLViewDictionary.getInstance().put(
      PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      CTXT_UNRELATED_ATTACHMENTS,
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbum_UnrelatedAttachments_Table.xml", 0, PhotoAlbum_UnrelatedAttachments_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
      PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbum_Form.xml", 0, PhotoAlbum_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM, 
      "Thumb", 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbum_Thumb_Form.xml", 0, PhotoAlbum_Thumb_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_FORM, 
      "Reduced", 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbum_Reduced_Form.xml", 0, PhotoAlbum_Reduced_Form.class.getName());

    XMLViewDictionary.getInstance().put(
      PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      XMLViewKey.CONTEXT_DEFAULT, 
      "Simple", 
      "/xml/com/foc/photoAlbum/PhotoAlbum_Main_Simple_Table.xml", 0, PhotoAlbum_Main_Simple_Table.class.getName());

    XMLViewDictionary.getInstance().put(
      PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      XMLViewKey.CONTEXT_DEFAULT, 
      "Simple", false, "ar", 
      "/xml/com/foc/photoAlbum/PhotoAlbum_Main_Simple_Table-ar.xml", 0, PhotoAlbum_Main_Simple_Table.class.getName());
  
//    XMLViewDictionary.getInstance().put(
//      PhotoAlbumDesc.getInstance().getStorageName(),
//      XMLViewKey.TYPE_FORM, 
//      XMLViewKey.CONTEXT_DEFAULT, 
//      "Simple", 
//      "/xml/com/foc/photoAlbum/PhotoAlbum_Main_Simple_Form.xml", 0, PhotoAlbum_Form.class.getName());
    
    XMLViewDictionary.getInstance().put(
      PhotoAlbumAccessDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      CONTEXT_GROUP_ACCESS_RIGHT_SELECTION, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbumAccess_GroupAccessRightSelection_Table.xml", 0, PhotoAlbumAccess_GroupAccessRightSelection_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
      PhotoAlbumAccessDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbumAccess_Table.xml", 0, PhotoAlbumAccess_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
    	PhotoAlbumDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      CTXT_ALL_DOCUMENTS, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/PhotoAlbum_AllDocuments_Table.xml", 0, PhotoAlbum_AllDocuments_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
    	STORAGE_NAME_DOCUMENT_DASHBOARD,
      XMLViewKey.TYPE_FORM, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/com/foc/photoAlbum/DocumentBoard_Form.xml", 0, DocumentBoard_Form.class.getName());
  }

	public void menu_Declare(FocWebVaadinWindow mainWindow, FVMenuTree menuTree) {
	}

	public boolean menu_HandleAction(FocWebVaadinWindow mainWindow, String menu) {
		return false;
	}

  @Override
  public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuITem) {
  	FocMenuItem mainMenu = menuTree.pushRootMenu("DOCUMENT_MANAGEMENT", "Document Management");

		if (getFocGroup().getWebModuleRights(MODULE_NAME) == GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION) {

	  	FocMenuItem menuItem = mainMenu.pushMenu(MENU_CODE_CONFIG, "Document management configuration");
	    menuItem.setMenuAction(new IFocMenuItemAction() {
	    	
				public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
					INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
	        XMLViewKey xmlViewKey = new XMLViewKey(PhotoAlbumConfigDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT);
	      	ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocWebVaadinWindow) mainWindow, xmlViewKey, PhotoAlbumConfig.getInstance());
	      	mainWindow.changeCentralPanelContent(centralPanel, true);
	      }
				
	    });

	    if(getFocGroup().getWebModuleRights(MODULE_NAME) == GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION){
		    menuItem = mainMenu.pushMenu(MENU_CODE_DOCUMENT_TYPE, "Document Types");
		    menuItem.setMenuAction(new IFocMenuItemAction() {
		      public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
		         INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
		         FocList focList = DocumentTypeDesc.getInstance().getFocList();
		         mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
		      }
	      });
	    }
		}
		
  	FocMenuItem menuItem = mainMenu.pushMenu(MENU_CODE_DOCUMENT_DASHBOARD, "Document Dashboard");
    menuItem.setMenuAction(new IFocMenuItemAction() {
    	
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        //FocList focList = PhotoAlbumDesc.getInstance().getFocList();
        XMLViewKey xmlViewKey = new XMLViewKey(STORAGE_NAME_DOCUMENT_DASHBOARD, XMLViewKey.TYPE_FORM, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT);
      	ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocWebVaadinWindow) mainWindow, xmlViewKey, null);
      	mainWindow.changeCentralPanelContent(centralPanel, true);
      }
			
    });

  	menuItem = mainMenu.pushMenu(MENU_CODE_ALL_DOCUMENTS, "Documents View");
    menuItem.setMenuAction(new IFocMenuItemAction() {
    	
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = PhotoAlbumDesc.getInstance().getFocList();
        XMLViewKey xmlViewKey = new XMLViewKey(PhotoAlbumDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE, CTXT_ALL_DOCUMENTS, XMLViewKey.VIEW_DEFAULT);
      	ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocWebVaadinWindow) mainWindow, xmlViewKey, focList);
      	mainWindow.changeCentralPanelContent(centralPanel, true);
       }
    });
  }
  
  public static ICentralPanel newAttachmentCentralPanel(INavigationWindow navigationWindow, FocObject focObject){
  	ICentralPanel centralPanel = null; 
  	if(navigationWindow != null && focObject != null){
	    PhotoAlbumListWithFilter focList = new PhotoAlbumListWithFilter(focObject);
	    XMLViewKey key = new XMLViewKey(PhotoAlbumDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
	    centralPanel = XMLViewDictionary.getInstance().newCentralPanel(navigationWindow, key, focList);
	    if(centralPanel != null) centralPanel.setFocDataOwner(true);
  	}
    return centralPanel;
  }

}