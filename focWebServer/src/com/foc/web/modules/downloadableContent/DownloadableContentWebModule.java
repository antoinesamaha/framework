package com.foc.web.modules.downloadableContent;

import com.foc.business.downloadableContent.DownloadableContentDesc;
import com.foc.business.photoAlbum.PhotoAlbumDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class DownloadableContentWebModule extends FocWebModule {
	
	public final static String CTXT_SPECIAL_URL  = "SPECIAL URL";
	public final static String CTXT_FOREACH_FORM = "FOREACH FORM";
	
  public DownloadableContentWebModule() {
    super("DOWNLOADABLE_CONTENT", "Downloadable Content");
  }

  public void declareXMLViewsInDictionary() {
    XMLViewDictionary.getInstance().put(
      DownloadableContentDesc.getInstance().getStorageName(),
      XMLViewKey.TYPE_TABLE, 
      XMLViewKey.CONTEXT_DEFAULT, 
      XMLViewKey.VIEW_DEFAULT, 
      "/xml/b01/foc/downloadableContent/DownloadableContent_Table.xml", 0, DownloadableContent_Table.class.getName());
    
    XMLViewDictionary.getInstance().put(
        DownloadableContentDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_TABLE, 
        CTXT_SPECIAL_URL, 
        XMLViewKey.VIEW_DEFAULT, 
        "/xml/b01/foc/downloadableContent/DownloadableContent_URLEntry_Table.xml", 0, null);
    
    XMLViewDictionary.getInstance().put(
    		DownloadableContentDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM, 
        XMLViewKey.CONTEXT_DEFAULT, 
        XMLViewKey.VIEW_DEFAULT, 
        "/xml/b01/foc/downloadableContent/DownloadableContent_Form.xml", 0, null);

    XMLViewDictionary.getInstance().put(
    		DownloadableContentDesc.getInstance().getStorageName(),
        XMLViewKey.TYPE_FORM, 
        CTXT_FOREACH_FORM, 
        XMLViewKey.VIEW_DEFAULT, 
        "/xml/b01/foc/downloadableContent/DownloadableContent_ForEach_Form.xml", 0, DownloadableContent_Form.class.getName());
  }

	public void menu_Declare(FocWebVaadinWindow mainWindow, FVMenuTree menuTree) {
	}

	public boolean menu_HandleAction(FocWebVaadinWindow mainWindow, String menu) {
		return false;
	}

  @Override
  public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuITem) {
  	FocMenuItem mainMenu = menuTree.pushRootMenu("Downloadable Content", "Downloadable Content");
    FocMenuItem menuItem = null;
    
    menuItem = mainMenu.pushMenu("ALL_DOWNLOADABLE_CONTENT", "Downloadable Content - All");
    menuItem.setMenuAction(new IFocMenuItemAction() {
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
        INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
        FocList focList = DownloadableContentDesc.getList(FocList.LOAD_IF_NEEDED);
        mainWindow.changeCentralPanelContent_ToTableForFocList(focList);
			}
    });
    
  }
}