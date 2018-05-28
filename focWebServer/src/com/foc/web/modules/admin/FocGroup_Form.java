package com.foc.web.modules.admin;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.admin.FocAppGroup;
import com.foc.admin.FocGroup;
import com.foc.admin.FocGroupDesc;
import com.foc.admin.GroupXMLViewDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItemDesc;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class FocGroup_Form extends FocXMLLayout {

	private ArrayList<AppGroupButtonListener> listener_Array = null;
	
  private FocGroup getGroup(){
    return (FocGroup) getFocData();
  }

  @Override
  public void dispose(){
  	super.dispose();
  	if(listener_Array != null){
  		for(int i=0; i<listener_Array.size(); i++){
  			AppGroupButtonListener listener = listener_Array.get(i);
  			listener.dispose();
  		}
  		listener_Array.clear();
  		listener_Array = null;
  	}
  }
  
  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
    super.init(window, xmlView, focData);
    FocGroup group = (FocGroup) focData;
    if (group != null && group.isCreated()) {
    	group.scanAndAddWebModulesToGroup();
    }
  }

  @Override
  protected void afterLayoutConstruction() {
    getMenuRightsButton();
    getViewRightsButton();
    getRightsButton();
  }
  
  private FVButton getMenuRightsButton() {
    FVButton menuRightsButton = (FVButton) getComponentByName("MENU_RIGHTS_BUTTON");

    if (menuRightsButton != null) {
      menuRightsButton.addClickListener(new ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
          XMLViewKey xmlViewKey = new XMLViewKey(FocMenuItemDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE, AdminWebModule.CTXT_GROUP_MENU_RIGHT, XMLViewKey.VIEW_DEFAULT);
          FocMenuItem_GroupMenuRight_Tree centralPanel = (FocMenuItem_GroupMenuRight_Tree) XMLViewDictionary.getInstance().newCentralPanel_NoParsing(getMainWindow(), xmlViewKey, null);
          centralPanel.setFocGroup(getGroup());
          centralPanel.fill();
          centralPanel.parseXMLAndBuildGui();
          getMainWindow().changeCentralPanelContent(centralPanel, true);
        	
        	/*
          FVMenuTree menuRightsList = new FVMenuTree();
          
          FocMenuItemTree menuItemTree = new FocMenuItemTree(menuRightsList.getMenuList());
          
          XMLViewKey xmlViewKey = new XMLViewKey(MenuRightsDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE);
          ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, menuItemTree);
          getMainWindow().changeCentralPanelContent(centralPanel, true);
          */
          
        }
      });
    }
    
    return menuRightsButton;
  }

  private FVButton getViewRightsButton() {
    FVButton viewRightsButton = (FVButton) getComponentByName("VIEW_RIGHTS_BUTTON");

    if (viewRightsButton != null) {
      viewRightsButton.addClickListener(new ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
        	FocList focList = getGroup().getXmlViewRightsList();
          XMLViewKey xmlViewKey = new XMLViewKey(GroupXMLViewDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TABLE);
          ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, focList);
          getMainWindow().changeCentralPanelContent(centralPanel, true);
        }
      });
    }
    
    return viewRightsButton;
  }
  
  private void getRightsButton(){
    FVVerticalLayout buttonsLayout = (FVVerticalLayout) getComponentByName("APP_GROUPS_BUTTONS_LAYOUT");
  	
  	if(FocGroupDesc.getInstance().getNumberOfAppGroupListFieldID() > 0){
	    for(int i=0; i< FocGroupDesc.getInstance().getNumberOfAppGroupListFieldID(); i++){
	    	FocAppGroup appGroup = (FocAppGroup) getGroup().getAppGroupAt(FocGroupDesc.FLD_START_APP_GROUPS + i);
	    	if(appGroup != null){
	    		FVButton grpButton = new FVButton(appGroup.getTitle());
	    		grpButton.setWidth("100%");
	  			putComponent(appGroup.getTitle(), grpButton);
	    		buttonsLayout.addComponent(grpButton);
	    		AppGroupButtonListener listener = new AppGroupButtonListener(grpButton, appGroup);
	    		if(listener_Array == null){
	    			listener_Array = new ArrayList<FocGroup_Form.AppGroupButtonListener>(); 
	    		}
	    		listener_Array.add(listener);
	     	}
	    }
  	}
  	
  }
  
  private class AppGroupButtonListener implements Button.ClickListener{

  	private FocAppGroup appGroup = null;
  	private FVButton    button   = null;
  	
  	public AppGroupButtonListener(FVButton button, FocAppGroup appGroup){
  		this.appGroup = appGroup;
  		this.button   = button;
  		if(button != null){
  			button.addClickListener(this);
  		}
  	}
  	
  	public void dispose(){
  		if(button != null){
  			button.removeClickListener(this);
  		}
  		appGroup = null;
  		button   = null;
  	}
  	
		@Override
		public void buttonClick(ClickEvent event) {
			Globals.popup(appGroup, false);
		}
  }
}