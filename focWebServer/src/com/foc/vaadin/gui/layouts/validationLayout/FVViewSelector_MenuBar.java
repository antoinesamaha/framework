package com.foc.vaadin.gui.layouts.validationLayout;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.admin.FocGroup;
import com.foc.admin.FocGroupDesc;
import com.foc.admin.GroupXMLViewDesc;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.RightPanel;
import com.foc.vaadin.gui.windows.OptionSelectorWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.modules.admin.FocGroup_Selector_Form;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.MenuBar;

@SuppressWarnings("serial")
public class FVViewSelector_MenuBar extends MenuBar {

	private ICentralPanel centralPanel = null;

	private MenuItem rootMenuItem = null;

	private MenuItem toolsMenuItem = null;

	private boolean saveSelectionActive = true;

	private boolean allowToolsMenuItem = true;

	public FVViewSelector_MenuBar(ICentralPanel centralPanel) {
		this(centralPanel, "View");
	}

	public FVViewSelector_MenuBar(ICentralPanel centralPanel, String caption) {
		addStyleName(FocXMLGuiComponentStatic.STYLE_NO_PRINT);
		this.centralPanel = centralPanel;
		XMLView xmlView = centralPanel != null ? centralPanel.getXMLView() : null;
		if(xmlView != null){

			int rightLevel = centralPanel.getViewRights();
			// Adding the tool menu item on the right level.
			if(rightLevel == GroupXMLViewDesc.ALLOW_NOTHING || rightLevel == GroupXMLViewDesc.ALLOW_SELECTION){
				setAllowToolsMenuItem(false);
			}else{
				setAllowToolsMenuItem(true);
			}
			fillViews();
			fillToolsMenuItem();
			setView(xmlView.getXmlViewKey().getUserView());
		}
	}

	public XMLViewKey getXMLViewKey(){
		XMLViewKey xmlViewKey = null;
		XMLView xmlView = getCentralPanel() != null ? getCentralPanel().getXMLView() : null;
		if(xmlView != null){
			xmlViewKey = xmlView.getXmlViewKey();
		}
		return xmlViewKey;
	}
		
	public void fillViews() {
		XMLView xmlView = getCentralPanel() != null ? getCentralPanel().getXMLView() : null;
		XMLViewKey xmlViewKey = xmlView.getXmlViewKey();
		int rightLevel = getCentralPanel().getViewRights();
		getRootMenuItem().removeChildren();
		fillViewNames(xmlViewKey, rightLevel);
	}

	public void resetCheckedMenuItems() {
		for(int i = 0; i < getRootMenuItem().getChildren().size(); i++){
			getRootMenuItem().getChildren().get(i).setChecked(false);
		}
	}

	private void addNewMenuItem(String viewString) {
		MenuItem menuItem = getRootMenuItem().addItem(viewString, new ViewSelectorClickListener());
		menuItem.setCheckable(true);
	}

	private void setMenuBarText(String viewName) {
		if(getRootMenuItem() != null){
			getRootMenuItem().setText("View:  " + viewName);
		}
	}

	private void setView(String viewName) {
		MenuItem menuItem = getMenuItemByTitle(viewName);
		if(menuItem != null){
			menuItem.setChecked(true);
		}
		setMenuBarText(viewName);
	}

	public void selectView(String viewName) {
		MenuItem menuItem = getMenuItemByTitle(viewName);
		if(menuItem != null){
			menuItem.setChecked(true);
			menuItem.getCommand().menuSelected(menuItem);
		}
	}

	public MenuItem addView(String viewName) {
		return addView(viewName, false);
	}

	public MenuItem addView(String viewName, boolean selectMenuItem) {
		MenuItem menuItem = getRootMenuItem().addItem(viewName, new ViewSelectorClickListener());
		if(selectMenuItem){
			menuItem.setCheckable(true);
			resetCheckedMenuItems();
			menuItem.setChecked(true);
		}
		return menuItem;
	}

	public void removeView(String view) {
		if(view != null){
			MenuItem founrMenuItem = getMenuItemByTitle(view);
			if(founrMenuItem != null){
				getRootMenuItem().removeChild(founrMenuItem);

				resetCheckedMenuItems();
				selectStandardView();
				markAsDirty();
			}
		}
	}

	private MenuItem getSelectedMenuItem() {
		MenuItem selectedMenuItem = null;
		for(int i = 0; i < getRootMenuItem().getChildren().size() && selectedMenuItem == null; i++){
			MenuItem menuItem = getRootMenuItem().getChildren().get(i);
			if(menuItem != null && menuItem.isChecked()){
				selectedMenuItem = menuItem;
			}
		}
		return selectedMenuItem;
	}

	private MenuItem getMenuItemByTitle(String title) {
		MenuItem founrMenuItem = null;
		for(int i = 0; i < getRootMenuItem().getChildren().size() && founrMenuItem == null; i++){
			MenuItem menuItem = getRootMenuItem().getChildren().get(i);
			if(title.equals(menuItem.getText())){
				founrMenuItem = menuItem;
			}
		}
		return founrMenuItem;
	}

	private void fillViewNames(XMLViewKey xmlViewKey, int rightLevel) {

		String[] arrayOfViews = null;
		if(rightLevel == GroupXMLViewDesc.ALLOW_NOTHING){
			arrayOfViews = new String[1];
			arrayOfViews[0] = xmlViewKey.getUserView();
		}else{
			arrayOfViews = XMLViewDictionary.getInstance().getXmlViews(xmlViewKey, false);
		}

		for(int i = 0; i < arrayOfViews.length; i++){
			String viewString = arrayOfViews[i];
			addNewMenuItem(viewString);
		}
	}

	private void fillToolsMenuItem(){
		MenuItem toolsMenuItem = getToolsMenuItem();
		if(toolsMenuItem != null && Globals.getApp().getUser_ForThisSession() != null && !Globals.getApp().getUser_ForThisSession().isGuest()){
			toolsMenuItem.addItem("Modify current view", new Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					if(getCentralPanel().getXMLView().isSystemView() && !ConfigInfo.isForDevelopment()){
						Globals.showNotification("Cannot modify system view.", "You can modify a system view.", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
					}else{
						if(centralPanel != null){
							if(Globals.isValo()){
								String xmlContent = (centralPanel != null && centralPanel.getXMLView() != null) ? centralPanel.getXMLView().getXMLString() : "";
								if(xmlContent != null){
									RightPanel.popupXmlEditor(getCentralPanel().getXMLView(), xmlContent);
								}
							}else{
								INavigationWindow window = centralPanel.getMainWindow();
								if(window != null){
									RightPanel rightPanel = (RightPanel) centralPanel.getRightPanel(true);// new
																																												// RightPanel(centralPanel,
																																												// focDesc);
									window.addUtilityPanel(rightPanel);
								}
							}
						}
					}				
				}
			});
			
			toolsMenuItem.addItem("Duplicate current view", new Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					OptionSelectorWindow displayWindow = new OptionSelectorWindow(FVViewSelector_MenuBar.this);
					displayWindow.duplicate();
					FocWebApplication.getInstanceForThread().addWindow(displayWindow);
				}
			});
			
			toolsMenuItem.addItem("New blank view", new Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					OptionSelectorWindow displayWindow = new OptionSelectorWindow(FVViewSelector_MenuBar.this);
					displayWindow.newViewWindow();
					FocWebApplication.getInstanceForThread().addWindow(displayWindow);
				}
			});
			
			toolsMenuItem.addItem("Delete current view", new Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					if(getCentralPanel().getXMLView().isSystemView()){
						Globals.showNotification("Cannot delete system views", "", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
					}else{
						MenuItem selectedMenuItem = getSelectedMenuItem();
						if(selectedMenuItem != null){
							OptionSelectorWindow displayWindow = new OptionSelectorWindow(FVViewSelector_MenuBar.this);
							displayWindow.deleteView(getSelectedMenuItem().getText());
							FocWebApplication.getInstanceForThread().addWindow(displayWindow);
						}
					}
				}
			});
			
			toolsMenuItem.addItem("Show included views", new Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					// if(getCentralPanel().getXMLView().isSystemView()){
					// Globals.showNotification("Cannot delete system views", "",
					// FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
					// }else{
	
					ICentralPanel centralPanel = getCentralPanel();
					if(centralPanel instanceof FocXMLLayout){
						FocXMLLayout lay = (FocXMLLayout) centralPanel;
						lay.scanLayoutsAndShowViewValidationLayouts();
					}
				}
			});
			
			toolsMenuItem.addItem("Set current view as default printing view", new Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					XMLView xmlView = getCentralPanel() != null ? getCentralPanel().getXMLView() : null;
					if(xmlView != null){
						XMLViewKey xmlViewKey = xmlView.getXmlViewKey();
						if(xmlViewKey != null){
							XMLViewDictionary xmlViewDictionary = XMLViewDictionary.getInstance();
							xmlViewDictionary.userViewSelected_saveViewForUserPrintingView(FocWebApplication.getFocUser(), xmlViewKey);
							Globals.showNotification("Printing view has been updated.", "", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
						}
					}
				}
			});

			
			boolean allowSettingViewsToOtherGroups = 	Globals.getApp() != null &&
																								Globals.getApp().getUser_ForThisSession() != null &&
																								FocWebModule.allowAccessToModule(Globals.getApp().getUser_ForThisSession().getGroup(), AdminWebModule.MODULE_NAME);
			
			if(allowSettingViewsToOtherGroups){
				toolsMenuItem.addItem("Set current view as Default to Group...", new Command() {
					
					@Override
					public void menuSelected(MenuItem selectedItem) {
						XMLView xmlView = getCentralPanel() != null ? getCentralPanel().getXMLView() : null;
						if(xmlView != null){
							XMLViewKey xmlViewKey = xmlView.getXmlViewKey();
							if(xmlViewKey != null){
//								XMLViewDictionary xmlViewDictionary = XMLViewDictionary.getInstance();
								
								XMLViewKey grpSelKey = new XMLViewKey(FocGroupDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CONTEXT_GROUP_SELECTOR, XMLViewKey.VIEW_DEFAULT);
								FocGroup_Selector_Form form = (FocGroup_Selector_Form) XMLViewDictionary.getInstance().newCentralPanel(getWindow(), grpSelKey, null);
								
								INavigationWindow window = getCentralPanel() != null ? getCentralPanel().getMainWindow() : null;
								if(window != null){
									window.changeCentralPanelContent(form, true);
									if(form.getValidationLayout() != null){
										form.getValidationLayout().addValidationListener(new IValidationListener() {
											
											FocGroup group = null;
											
											@Override
											public void validationDiscard(FVValidationLayout validationLayout) {
											}
											
											@Override
											public boolean validationCommit(FVValidationLayout validationLayout) {
												FocGroup_Selector_Form form = (FocGroup_Selector_Form) validationLayout.getCentralPanel();
												group = form.getSelectedGroup();
												return false;
											}
											
											@Override
											public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
												if(commited && group != null){
													XMLViewKey xmlViewKey = getXMLViewKey();
													group.adjustXMLViewRight(xmlViewKey.getStorageName(), xmlViewKey.getContext(), xmlViewKey.getType(), xmlViewKey.getUserView());
													group.validate(true);
												}
											}
										});
									}
								}
							}
						}
					}
				});
			}
		}
	}

	public void selectStandardView() {
		MenuItem menuItem = getMenuItemByTitle(XMLViewKey.VIEW_DEFAULT);
		if(menuItem != null){
			menuItem.setChecked(true);
			setMenuBarText(XMLViewKey.VIEW_DEFAULT);
		}
	}

	public void viewChanged(){
		MenuItem selectedItem = getSelectedMenuItem();
		viewChanged(selectedItem);
	}
	
	public void viewChanged(MenuItem selectedItem){
		if(getCentralPanel() != null && selectedItem != null){
			getCentralPanel().beforeViewChangeListenerFired();

			resetCheckedMenuItems();
			
			selectedItem.setChecked(true);
			String view = selectedItem.getText();
			setMenuBarText(view);

			// setView(view);

			getCentralPanel().copyGuiToMemory();

			if(getCentralPanel().getXMLView() != null){
				XMLViewKey newKey = new XMLViewKey(getCentralPanel().getXMLView().getXmlViewKey());
				newKey.setUserView(view);
				XMLView xmlView = XMLViewDictionary.getInstance().get_WithoutAdjustToLastSelection(newKey);
				if(xmlView.getJavaClassName().equals(getCentralPanel().getClass().getName())){
					getCentralPanel().setXMLView(xmlView);
					getCentralPanel().re_parseXMLAndBuildGui();
				}else{
					if(getCentralPanel() != null){
						INavigationWindow navigationWindow = getCentralPanel().getMainWindow();
						if(navigationWindow != null){
							ICentralPanel newCentralPanel = XMLViewDictionary.getInstance().newCentralPanel_NoAdjustmentToLastSelectedView(navigationWindow, newKey, getCentralPanel().getFocData());
							if(newCentralPanel != null){
  							//2017-09-08 To prevent dispose of the FocData while changing views								
								getCentralPanel().setFocDataOwner(false);
								//------
								navigationWindow.goBack(getCentralPanel());// This disposes
																														// the
																														// ViewSelector
								navigationWindow.changeCentralPanelContent(newCentralPanel, true);
								centralPanel = newCentralPanel;
							}
						}
					}
				}
				if(isSaveSelectionActive()){
					XMLViewDictionary.getInstance().userViewSelected_saveViewForUserAndKey(FocWebApplication.getFocUser(), newKey);
				}
			}
		}
	}
	
	private class ViewSelectorClickListener implements Command {

		@Override
		public void menuSelected(MenuItem selectedItem) {
			viewChanged(selectedItem);
		}
	}

	public boolean containsView(String view) {
		boolean contains = false;
		if(getRootMenuItem() != null && getRootMenuItem().getChildren() != null){
			try{
				for(int i = 0; i < getRootMenuItem().getChildren().size() && !contains; i++){
					MenuItem menuItem = getRootMenuItem().getChildren().get(i);
					if(view != null && view.equals(menuItem.getText())){
						contains = true;
					}
				}
			}catch (Exception e){
				Globals.logException(e);
				contains = false;
			}
		}
		return contains;
	}

	public void setView_WithoutSavingSelection(String view) {
		setSaveSelectionActive(false);
		setView(view);
		setSaveSelectionActive(true);
	}

	public ICentralPanel getCentralPanel() {
		return centralPanel;
	}

	private boolean isSaveSelectionActive() {
		return saveSelectionActive;
	}

	private void setSaveSelectionActive(boolean saveSelectionActive) {
		this.saveSelectionActive = saveSelectionActive;
	}

	public FocCentralPanel getWindow() {
		return findAncestor(FocCentralPanel.class);
	}

	public MenuItem getRootMenuItem() {
		if(rootMenuItem == null){
			rootMenuItem = addItem("View", null);
		}
		return rootMenuItem;
	}

	private MenuItem getToolsMenuItem() {
		if(toolsMenuItem == null && isAllowToolsMenuItem()){
			MenuItem menuItem = getRootMenuItem();
			toolsMenuItem = menuItem.addItem("View Management", null);
		}
		return toolsMenuItem;
	}

	public boolean isAllowToolsMenuItem() {
		return allowToolsMenuItem;
	}

	public void setAllowToolsMenuItem(boolean allowToolsMenuItem) {
		this.allowToolsMenuItem = allowToolsMenuItem;
	}
}
