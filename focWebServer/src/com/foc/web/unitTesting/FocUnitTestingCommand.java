package com.foc.web.unitTesting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.access.FocLogger;
import com.foc.admin.FocUserDesc;
import com.foc.desc.FocObject;
import com.foc.menuStructure.FocMenuItemConst;
import com.foc.saas.manager.SaaSConfigDesc;
import com.foc.util.Encryptor;
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVGearWrapper.PopupLinkButton;
import com.foc.vaadin.gui.components.FVMultipleChoiceOptionGroupPopupView;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.components.FVObjectPopupView;
import com.foc.vaadin.gui.components.FVObjectSelector;
import com.foc.vaadin.gui.components.FVTable;
import com.foc.vaadin.gui.components.FVTablePopupMenu;
import com.foc.vaadin.gui.components.FVTreeTable;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.components.popupButton.FVPopupButton;
import com.foc.vaadin.gui.components.popupButton.FVPopupContentButton;
import com.foc.vaadin.gui.layouts.FVForEachLayout;
import com.foc.vaadin.gui.layouts.FVForEachLayout.DeleteButtonForEach;
import com.foc.vaadin.gui.layouts.FVForEachLayout.FVBannerLayout;
import com.foc.vaadin.gui.layouts.FVMoreLayout;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVStageLayout_MenuBar;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout_MenuBar;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVViewSelector_MenuBar;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.FocWebServer;
import com.foc.webservice.FocWebService;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

public class FocUnitTestingCommand {
  private String methodName = null;
  private FocUnitXMLAttributes attributes = null;
  private FocUnitTest unitTest = null;

  public FocUnitTestingCommand(FocUnitTest unitTest, String methodName, Attributes attributes) {
    this.unitTest = unitTest;
    setMethodName(methodName);
    setAttributes(new FocUnitXMLAttributes(unitTest, attributes));
  }

  public void dispose() {
    unitTest = null;
    methodName = null;
    attributes = null;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public FocUnitXMLAttributes getAttributes() {
    return attributes;
  }

  public void setAttributes(FocUnitXMLAttributes attributes) {
    this.attributes = attributes;
  }

  public FocUnitTest getUnitTest() {
    return unitTest;
  }

  public FocUnitTestingSuite getUnitSuite() {
    return getUnitTest().getSuite();
  }

  private boolean isStopWhenFail(){
  	return true;
  }
  
  public void execute() {
  	if(!isStopWhenFail() || !FocLogger.getInstance().isHasFailure()){
  		String message = getMethodName() + " " + ((getAttributes() != null) ? getAttributes().getString() : "");
  		Globals.logString(message);
	    FocLogger.getInstance().openNode(message);
	    IFUnitMethod method = FocUnitMethodFactory.getInstance().getMethodByName(getMethodName());
	    method.executeMethod(this, getAttributes());
	    FocLogger.getInstance().closeNode();
  	}
  }

  /**
   * Gets the current application.
   */
  public FocWebApplication getApplication() {
    FocWebApplication application = (FocWebApplication.getInstanceForThread() != null) ? FocWebApplication.getInstanceForThread() : null;
    return application;
  }

  /**
   * Gets the current main window.
   */
  public FocWebVaadinWindow getMainWindow() {
  	FocWebVaadinWindow window = (FocWebVaadinWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
    return window;
  }

  /**
   * Returns the current central panel.
   * @return The current central panel.
   */
  public FocXMLLayout getCurrentCentralPanel() {
  	FocXMLLayout result = null;

  	FocWebApplication ui = (FocWebApplication) FocWebApplication.getInstanceForThread();
    FocWebVaadinWindow window = (FocWebVaadinWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
    
    if (window != null) {
    	result = (FocXMLLayout) window.getCentralPanel();
      Collection<Window> children = ui.getWindows();
      if (!children.isEmpty()) {
      	Object obj = ((Window) children.toArray()[0]).getContent();
      	if(obj instanceof FocXMLLayout){
      		result = (FocXMLLayout) obj;
      	}else{
      		FocCentralPanel panel = (FocCentralPanel) ((Window) children.toArray()[0]).getContent();
          
          if (panel != null) {
            result = (FocXMLLayout) panel.getCentralPanel();
          }
      	}
      }
    }
  	
    String layoutName = getAttributes().getValue(FXMLUnit.ATT_LAYOUT_NAME);
    if(layoutName == null || layoutName.equals(FXMLUnit.ATT_LAYOUT_NAME)){
      String fieldName  = "$F{"+FXMLUnit.ATT_LAYOUT_NAME+"}";
      layoutName = getAttributes().resolveValue(fieldName);

      if(layoutName == null || layoutName.equals(fieldName)){
      	layoutName = null;
      }
    }
    
  	if(layoutName != null){
	    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(result, layoutName, false);
	    if (tableWrapper != null) {
	    	ICentralPanel centralPanel = tableWrapper.innerLayout_GetICentralPanel();
	    	if(centralPanel != null){
	    		result = (FocXMLLayout) centralPanel; 
	    	}
	    }
  	}
    
    return result;
  }
  
  /**
   * Gets the logger found in FocUnitDictionary.
   * 
   * @return returns the logger.
   */
  public FocLogger getLogger() {
    return FocUnitDictionary.getInstance().getLogger();
  }

  /**
   * Simulates a click on the "Log Out" button in the application.
   */
  public void logout(String nextTestSuite) {
  	if(Globals.isValo()){
  		
  		getMainWindow().logout(nextTestSuite);
  		/*
  		MenuItem logoutMenuItem = getMainWindow() != null ? getMainWindow().getLogoutMenuItem() : null;
  		if(logoutMenuItem != null && logoutMenuItem.getCommand() != null){
  			logoutMenuItem.getCommand().menuSelected(logoutMenuItem);
  			getMainWindow().setMenuBarFilled(false); //To Fix Logout Bug  06-01-2016 //
  		}else{
  			getLogger().addFailure("Log Out button not found.");
  		}
  		*/
  		
  	}else{
	    NativeButton logout = getMainWindow().getLogoutButton();
	    if (logout != null) {
	    	logout.click();
	    } else {
	      getLogger().addFailure("Log Out button not found.");
	    }
  	}
  }

  /**
   * Simulates a click on the "Navigation" button in the application.
   * 
   */
  public void navigate() {
    NativeButton navigation = getMainWindow().getNavigationButton();
    if (navigation != null) {
      navigation.click();
    } else {
      getLogger().addFailure("Navigation button not found.");
    }
  }

  /**
   * Simulates a click on the "Home" button in the application.
   */
  public void home() {
    NativeButton home = getMainWindow().getHomeButton();
    if (home != null) {
      home.click();
    } else {
      getLogger().addFailure("Home button not found.");
    }
  }

  /**
   * Simulates a click on the "Admin" button in the application.
   * 
   */
  protected void admin() {
    NativeButton admin = getMainWindow().getAdminButton();
    if (admin != null) {
      admin.click();
    } else {
      getLogger().addFailure("Admin button not found.");
    }
  }

  /**
   * Simulates a click on the "Apply" button in the validation layout.
   * 
   */
  protected void validationApply() {
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
  	
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();

      if (validationLayout != null) {
      	Button apply = validationLayout.getApplyButton(false);
      	if(Globals.isValo()){
      		apply = validationLayout.valo_GetApplyButton(false);      		
      	}
        if (apply != null) {
          apply.click();
        } else {
          getLogger().addFailure("Apply button not found.");
        }
      } else {
        getLogger().addFailure("Validation layout not found");
      }
    } else {
      getLogger().addFailure("Navigation layout not found");
    }
  }
  
  protected void validationSave() {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();

      if (validationLayout != null) {
      	Button save = validationLayout.getSaveButton(false);
      	if(Globals.isValo()){
      		save = validationLayout.valo_GetSaveButton(false);      		
      	}
        if (save != null) {
          save.click();
        } else {
          getLogger().addFailure("Save button not found.");
        }
      } else {
        getLogger().addFailure("Validation layout not found");
      }
    } else {
      getLogger().addFailure("Navigation layout not found");
    }
  }

  /**
   * Simulates clicking on the "Apply" button in the validation layout until the apply button can't be found anymore.
   * 
   */
  protected void validationDeepApply(){
  	boolean keepLooping = true;
  	
  	while(keepLooping){
  		FocXMLLayout layout = getCurrentCentralPanel();
  		
  		if(layout != null){
  			FVValidationLayout validationLayout = layout.getValidationLayout();
  			
  			if(validationLayout != null){
  				Button apply = validationLayout.getApplyButton(false);
  				
  				if(apply != null){
  					apply.click();
  				}
  				else{
  					keepLooping = false;
  				}
  			}
  			else{
  				keepLooping = false;
  			}
  		}
  		else{
  			keepLooping = false;
  		}
  	}
  }

  /**
   * Simulates a click on the "Discard" button in the validation layout.
   * 
   */
  protected void validationDiscard(String tableName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    
  	if(tableName != null){
	    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
	    if (tableWrapper != null) {
	    	ICentralPanel centralPanel = tableWrapper.innerLayout_GetICentralPanel();
	    	if(centralPanel != null){
	    		navigationLayout = (FocXMLLayout) centralPanel; 
	    	}
	    }
  	}
    
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();

      if (validationLayout != null) {
        Button discard = null;
      	if(Globals.isValo()){
      		discard = validationLayout.valo_GetDiscardButton(false);      		
      	}else{
      		discard = validationLayout.getDiscardButton(false);
      	}

        if (discard != null) {
          discard.click();
        } else {
          getLogger().addFailure("Discard button not found.");
        }
      } else {
        getLogger().addFailure("Validation layout not found");
      }
    } else {
      getLogger().addFailure("Navigation layout not found");
    }
  }

  /**
   * Simulates a click on the "Print" button in the validation layout.
   * 
   */
  protected void validationPrint() {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();

      if (validationLayout != null) {
        Button print = validationLayout.getPrintButton(false);
        if (print != null) {
          print.click();
        } else {
          getLogger().addFailure("Print button not found.");
        }
      } else {
        getLogger().addFailure("Validation layout not found");
      }
    } else {
      getLogger().addFailure("Navigation layout not found");
    }
  }

  /**
   * Simulates a click on the "Full Screen" button in the validation layout.
   * 
   */
  protected void validationFullScreen() {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();

      if (validationLayout != null) {
        Button fullScreen = validationLayout.getFullScreenButton(false);
        if (fullScreen != null) {
          fullScreen.click();
        } else {
          getLogger().addFailure("Full screen button not found.");
        }
      } else {
        getLogger().addFailure("Validation layout not found");
      }
    } else {
      getLogger().addFailure("Navigation layout not found");
    }
  }

  /**
   * Simulates a click on the "Attach Image" button in the validation layout.
   * 
   */
  protected void validationAttachImage() {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();

      if (validationLayout != null) {
        Button attach = validationLayout.getAttachButton(false);
        if (attach != null) {
          attach.click();
        } else {
          getLogger().addFailure("Attach image button not found.");
        }
      } else {
        getLogger().addFailure("Validation layout not found");
      }
    } else {
      getLogger().addFailure("Navigation layout not found");
    }
  }

  /**
   * Simulates a click on the "Back" button in the validation layout.
   * 
   */
  protected void validationBack() {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();

      if (validationLayout != null) {
        Button back = validationLayout.getGoBackButton(false);
        if (back != null) {
          back.click();
        } else {
          getLogger().addFailure("Back button not found.");
        }
      } else {
        getLogger().addFailure("Validation layout not found");
      }
    } else {
      getLogger().addFailure("Navigation layout not found");
    }
  }

  protected void view_Select(String layoutName, String viewName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if(navigationLayout != null){
    	FocXMLGuiComponent layoutComponent = null;
    	if(layoutName != null){
    		layoutComponent = findComponent(navigationLayout, layoutName);
    	}else{
    		layoutComponent = navigationLayout;
    	}
    	
	    if(layoutComponent != null){
	    	FVViewSelector_MenuBar viewSelector = null;
	    	//We can give the name of the Table that holds the viewSelector
	    	if(layoutComponent instanceof FVTableWrapperLayout){
	    		viewSelector = ((FVTableWrapperLayout) layoutComponent).getViewSelector();
	    	}else{
	    		//The root component name VerticalLayout for example 
	    		//is not the name of the FocXMLLayout this is why we search for the first ancestor 
		    	if(!(layoutComponent instanceof FocXMLLayout)){
		    		layoutComponent = ((AbstractComponent)layoutComponent).findAncestor(FocXMLLayout.class);
		    	}
	    		if(layoutComponent != null && layoutComponent instanceof FocXMLLayout){
	    			FocXMLLayout layout = (FocXMLLayout) layoutComponent;
	    			if(layout.getValidationLayout() != null && layout.getValidationLayout().isVisible()){
	    				viewSelector = ((FocXMLLayout)layoutComponent).getValidationLayout().getViewSelector(false);
				    }else{
				    	getLogger().addFailure("Layout named: "+layoutName+" has no visible validation layout");
				    }
	    		}
	    	}

		    if(viewSelector != null){
		    	viewSelector.fillViews();
			    if(viewSelector.containsView(viewName)){
			    	viewSelector.selectView(viewName);
			    }else{
			    	getLogger().addFailure("Layout named: "+layoutName+" has no view : "+viewName);
			    }
		    }else{
		    	getLogger().addFailure("Layout named: "+layoutName+" has no view selector");
		    }
	    }else{
	    	getLogger().addFailure("Layout component named : "+layoutName+" not found");
	    }
    }else{
    	getLogger().addFailure("Navigation layout null");
    }
  }
  
  
  /**
   * Simulates a click on the "Approve" button under "Status" in the Validation
   * layout.
   * 
   */
  protected void validationApprove() {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if (navigationLayout != null) {
      FVStatusLayout_MenuBar statusLayout = navigationLayout.getValidationLayout().getStatusLayout(false);

      if (statusLayout != null) {
      	statusLayout.approve();
        OptionDialog optionDialog = statusLayout.getOptionDialog();
        if (optionDialog != null) {
        	FVButton button = (FVButton) optionDialog.getOptionButtonAt(0);
        	if(button != null){
	        	button.click();
        	}else{
        		getLogger().addFailure("Button at 0 is null");
        	}
        } else {
          getLogger().addFailure("OptionDialog is null");
        }
      } else {
        getLogger().addFailure("Approve button not found.");
      }
    } else {
      getLogger().addFailure("Validation layout not found");
    }
  }
  
  /**
   * Simulates clicking on a gear button in a component and then clicking on the
   * Add button.
   * 
   * @param componentName
   *          The name of the gear component.
   */
  public void gearComponentAdd(String componentName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    if (navigationLayout != null) {
      FVObjectSelector component = (FVObjectSelector) findComponent(navigationLayout, componentName);
      if (component != null) {
        PopupLinkButton button = component.getAddButton();
        if (button != null) {
          button.click();
        } else {
          getLogger().addFailure("Could not find Add button in gear wrapper.");
        }
      } else {
        getLogger().addFailure("Could not find gear wrapper.");
      }
    } else {
      getLogger().addFailure("Validation layout not found.");
    }
  }

  /**
   * Simulates clicking on a gear button in a component and then clicking on the
   * Open button.
   * 
   * @param componentName
   *          The name of the gear component.
   */
  public void gearComponentOpen(String componentName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    if (navigationLayout != null) {
      FVObjectSelector component = (FVObjectSelector) findComponent(navigationLayout, componentName);
      if (component != null) {
        PopupLinkButton button = component.getOpenButton();
        if (button != null) {
          button.click();
        } else {
          getLogger().addFailure("Could not find Open button in gear wrapper.");
        }
      } else {
        getLogger().addFailure("Could not find gear wrapper.");
      }
    } else {
      getLogger().addFailure("Validation layout not found.");
    }
  }

  /**
   * Searches for a menu item under the more menu by name.
   * 
   * @param menuCaption
   *          The text of the menu in question.
   * @return The menu if found, null otherwise.
   */
  protected MenuItem searchMoreMenu(String menuCaption) {
    MenuItem menuItem = null;
    if (menuCaption != null) {
      FocXMLLayout navigationLayout = getCurrentCentralPanel();
      List listOFMenuItems = navigationLayout.getValidationLayout().getMenubar(false).getItems().get(0).getChildren();

      for (int i = 0; i < listOFMenuItems.size(); i++) {
        if (((MenuItem) listOFMenuItems.get(i)).getText().equals(menuCaption)) {
          menuItem = (MenuItem) listOFMenuItems.get(i);
        }
      }
    } else {
      getLogger().addError("Cannot search for a menu item under the more menu without specifying a caption.");
    }
    if (menuItem == null) {
      getLogger().addFailure("Menu item " + menuCaption + " not found.");
    }
    return menuItem;
  }

  /**
   * Simulates clicking on a specific menu item in the more menu.
   * 
   * @param menuCaption
   *          The name of the more menu item.
   */
  protected void validationMore(String menuCaption) {
    if (menuCaption != null) {
      MenuItem menuItem = searchMoreMenu(menuCaption);
      if (menuItem != null) {
        menuItem.getCommand().menuSelected(menuItem);
      } else {
        getLogger().addFailure("Could not found menu item " + menuCaption + ".");
      }
    } else {
      getLogger().addFailure("Menu caption is null.");
    }

  }

  /**
   * Simulates the navigation to a specific menu item in the main navigation
   * while expanding all parent menu items and selecting the menu item in
   * question.
   * 
   * @param propertyName
   *          The name of the property. The use of the property name "CODE" is
   *          recommended.
   * @param propertyValue
   *          The value of the property we are looking for.
   */
  public void navigateTo(String menuCode) {
    navigate();
    changePanel(menuCode);
  }
  
  public void navigateToAdmin(String menuCode) {
  	admin();
  	changePanel(menuCode);
  }
  
  /**
   * open Panel according to menuCode
   * @param menuCode
   */
  private void changePanel(String menuCode){
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    if (navigationLayout != null) {
      FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) navigationLayout.getComponentByName("MENU_TREE");

      if (tableWrapper != null) {
        FVTreeTable treeTable = (FVTreeTable) tableWrapper.getTableOrTree();

        if (treeTable != null) {
          TableTreeDelegate delegate = treeTable.getTableTreeDelegate();

          getLogger().addInfo("Navigating to the menu item with the code " + menuCode + ".");
          FocObject object = delegate.selectByFocProperty(FocMenuItemConst.FNAME_CODE, menuCode);

          if (object != null) {
            Object objectID = treeTable.getParent(treeTable.getValue());

            while (objectID != null) {
              treeTable.setCollapsed(objectID, false);
              objectID = treeTable.getParent(objectID);
            }
          } else {
            getLogger().addFailure("Could not navigate to " + menuCode + ".");
          }
        }
      }
    }
  }

  /**
   * Simulates the navigation to a specific menu item in the main navigation
   * while expanding all parent menu items and selecting the menu item in
   * question. This function then constructs a mouse click event to simulate a
   * double click on the selected item.
   * 
   * @param propertyName
   *          The name of the property. The use of the property name "CODE" is
   *          recommended.
   * @param propertyValue
   *          The value of the property we are looking for.
   */
  public void navigateToAndDoubleClick(String menuCode) {
    navigateTo(menuCode);
    menuDoubleClickAction();
  }

  public void navigateToAdminAndDoubleClick(String menuCode) {
  	navigateToAdmin(menuCode);
  	menuDoubleClickAction();
  }

  /**
   * MenuTree Double click action 
   */
  private void menuDoubleClickAction(){
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, "MENU_TREE");
    FVTreeTable treeTable = (FVTreeTable) tableWrapper.getTableOrTree();

    String eventString = MouseEventDetails.MouseButton.LEFT+",0,0,false,false,false,false,2,0,0";
    MouseEventDetails details = MouseEventDetails.deSerialize(eventString);

    ItemClickEvent doubleClickEvent = new ItemClickEvent(treeTable, treeTable.getFocList().getItem(treeTable.getValue()), treeTable.getValue(), "TITLE", details);
    treeTable.getItemClickListener().itemClick(doubleClickEvent);  	
  }
  
  /**
   * Simulates a navigation and a click on the "Launch Unit Test" button
   * effectively launching the test over and over again. Useful function to
   * crash the server for the lulz.
   * 
   */
  public void launchUnitTest() {
    admin();
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, "MENU_TREE");
    FVTreeTable treeTable = (FVTreeTable) tableWrapper.getTableOrTree();

    TableTreeDelegate delegate = treeTable.getTableTreeDelegate();
    delegate.selectByFocProperty("CODE", "UNIT_TEST");

    Object objectID = treeTable.getParent(treeTable.getValue());

    while (objectID != null) {
      treeTable.setCollapsed(objectID, false);
      objectID = treeTable.getParent(objectID);
    }

    String eventString = MouseEventDetails.MouseButton.LEFT+",0,0,false,false,false,false,2,0,0";
    MouseEventDetails details = MouseEventDetails.deSerialize(eventString);

    ItemClickEvent doubleClickEvent = new ItemClickEvent(treeTable, treeTable.getFocList().getItem(treeTable.getValue()), treeTable.getValue(), "TITLE", details);
    treeTable.getItemClickListener().itemClick(doubleClickEvent);
  }

  public void collapseAll(String tableName, boolean collapse){
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
  	FocXMLGuiComponent foundComp = findComponent(navigationLayout, tableName);
  	if(foundComp != null){
  		if(foundComp instanceof FVTableWrapperLayout){
  			FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
  			if(tableWrapper.getTableOrTree() instanceof FVTreeTable){
    			FVTreeTable treeTable = (FVTreeTable) tableWrapper.getTableOrTree();
    			treeTable.expandCollapseNodes(!collapse);
    			treeTable.refreshRowCache();
  			}else{
  				getLogger().addFailure("Table found is not a FVTreeTable it is a:"+tableWrapper.getTableOrTree().getClass().getName());
  			}
  		}else{
  			getLogger().addFailure("Componet found is not a FVTableWrapperLayout it is a:"+foundComp.getClass().getName());  			
  		}
  	}
  }
  
  /**
   * Simulates selecting an item in an open table. Also saves the reference of
   * the selected object in a variable.
   * 
   * @param tableName
   *          The name of the table to add an item in.
   * @param propertyName
   *          The name of the property.
   * @param propertyValue
   *          The value of the property.
   * @param variableName
   *          The name of the variable to store the line reference in.
   */
  public int selectItemInTable(String tableName, String propertyName, String propertyValue, String variableName, int ancestor){
  	int referenceOfSelectedItem = 0;
  	
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    if(tableWrapper != null){//The find method will report the right log of failure
    	ITableTree iTableTree = tableWrapper.getTableOrTree();
	    
    	FocObject object = iTableTree != null ? iTableTree.getTableTreeDelegate().selectByFocProperty(propertyName, propertyValue, ancestor) : null;
  		
	    if(object != null && object.getReference() != null){
	    	referenceOfSelectedItem = object.getReference().getInteger();
	    	String reference = object.getReference().toString();

	    	//If tree table we expand all the parents so that the item becomes visible for selection
	    	if(iTableTree instanceof FVTreeTable){
	    		FVTreeTable treeTable = (FVTreeTable) iTableTree;
	    		Integer ref = object.getReference().getInteger();
	    		ref = (Integer) treeTable.getParent(ref);
	    		while(ref != null){
	    			treeTable.setCollapsed(ref, false);
	    			ref = (Integer) treeTable.getParent(ref);	    			
	    		}
	    	}
	    	//---------------------------------------------------------------------------------------
	    	
	    	if (variableName != null && !variableName.isEmpty()) {
	        getUnitSuite().getDictionary().putXMLVariable(variableName, reference);
	        getLogger().addInfo("Storing selected item reference in variable " + variableName + ".");
		    }	    	
	    }else {
        getLogger().addFailure("Could not find item in table " + tableName + " where " + propertyName + " = " + propertyValue);
      }
    }
    return referenceOfSelectedItem;
  }

  /**
   * Simulates adding an item in an open table (right-click -> Add). Also stores
   * the id of the created object in a variable.
   * 
   * @param tableName
   *          The name of the table to add an item in.
   * @param variableName
   *          The variable that will contain the id of the created object.
   * @param father
   */
  public void addItemInTable(String tableName, String variableName) {
    String reference = null;
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    if (tableWrapper != null) {
      Table table = (Table) tableWrapper.getTableOrTree();
      if (table != null) {
        FocObject father = null;
        if (table.getValue() != null) {
          father = ((ITableTree) table).getFocList().searchByReference((Integer) table.getValue());
        }
        FocObject object = ((ITableTree) table).getTableTreeDelegate().addItem(father);
        getLogger().addInfo("Adding a new item in table " + tableName + ".");
        if (object != null && object.getReference() != null) {
          if (variableName != null && !variableName.isEmpty()) {
            reference = object.getReference().toString();
            getUnitSuite().getDictionary().putXMLVariable(variableName, reference);
            getLogger().addInfo("Storing added item reference in variable " + variableName + ".");
          }
          table.select(object.getReference().getInteger());
        }
      }
    }
  }

  /**
   * Simulates a popup menu selection on a table 
   * 
   * @param tableName
   *          The name of the table to add an item in.
   * @param variableName
   *          The variable that will contain the id of the created object.
   * @param father
   */
  
  //Hussein
  public void table_RightClick(String tableName, String menuCaption) {
    boolean IsClicked = false;
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    if (tableWrapper != null) {
    	TableTreeDelegate delegate = tableWrapper.getTableTreeDelegate();
    	if(delegate != null){
      	FVTablePopupMenu foundMenu = null;
      	
      	ArrayList<FVTablePopupMenu> popupMenus = delegate.getPopupMenuArrayList();
      	for(int i=0; i<popupMenus.size(); i++){
      		if(menuCaption.equals(popupMenus.get(i).getCaption())){
      			foundMenu = popupMenus.get(i);
      		}
      	}

        Table table = (Table) tableWrapper.getTableOrTree();
        if (table != null && foundMenu != null) {
        	IsClicked = true;
        	Integer currentSelection = (Integer) table.getValue();
        	if(currentSelection != null){
	        	delegate.rightClick_HandleAction(foundMenu, null, currentSelection);
	        }
	    	}else{
	    		getLogger().addFailure("No item selected in table : " + tableName + ".");
	    	}
    	}
    }
    if(!IsClicked){
    	getLogger().addFailure("No item clicked in table : " + tableName + ".");
    }
  }
  
  /**
   * Simulates opening the selected item in a table (right-click -> Open).
   * 
   * @param tableName
   *          The name of the table to open the item selected in.
   */
  public void openItemInTable(String tableName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    Table table = (Table) tableWrapper.getTableOrTree();

    if(table.getValue() == null){
      getLogger().addFailure("No item selected in table : " + tableName + ".");
    }else{
      FocObject obj = ((ITableTree) table).getFocList().searchByReference((Integer) table.getValue());
      if (obj != null) {
        ((ITableTree) table).getTableTreeDelegate().open(obj);
        getLogger().addInfo("Opening selected item in table " + tableName + ".");
      } else {
        getLogger().addFailure("No object selected. Could not open in table " + tableName + ".");
      }
    }
  }

  /**
   * Simulates deleting the selected item in a table (right-click -> Delete).
   * 
   * @param tableName
   *          The name of the table to delete the item selected in.
   */
  public void deleteItemInTable(String tableName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    Table table = (Table) tableWrapper.getTableOrTree();

    FocObject obj = ((ITableTree) table).getFocList().searchByReference((Integer) table.getValue());
    if (obj != null) {
      ((ITableTree) table).getTableTreeDelegate().delete_NoPopupConfirmation(obj);
      getLogger().addInfo("Deleing selected item in table " + tableName + ".");
    } else {
      getLogger().addFailure("No object selected. Could not delete in table " + tableName + ".");
    }
  }
  
  public void componentAssertEnabled(String componentName, boolean assertEnabled){
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FocXMLGuiComponent component  = findComponent(navigationLayout, componentName);
    
    if(component != null && component.getDelegate() != null){
	    boolean isComponentEditable = ((Component)component).isEnabled();
	    
	    if(isComponentEditable != assertEnabled){
	    	String strg = assertEnabled ? " not Enabled" : " is Enabled";
	    	getLogger().addFailure("Component '" + componentName + "' " + strg);
	    }
    }else{
    	getLogger().addFailure("Faild to find component '" + componentName + "'");
    }
  }
  
  private boolean assertComponentValue(String componetnValue, String expectedComponentValue){
  	boolean isEqual = componetnValue == null && expectedComponentValue == null;
  	if(!isEqual){
  		isEqual = componetnValue.trim().equals(expectedComponentValue.trim());
  	}
  	return isEqual;
  }
  
  /**
   * Simulates setting the value of a component in a form.
   * 
   * @param componentName
   *          The name of the component as specified in the XML in the 'name'
   *          tag.
   * @param componentValue
   *          The value to be set (String)
   */
  public void setComponentValue(String componentName, String componentValue, boolean isAssert) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if(navigationLayout == null){
    	navigationLayout = getCurrentCentralPanel();
    }
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
   	setComponentValue(component, componentName, componentValue, isAssert);
  }
  
  public void AssertComponentEditable(String componentName) {
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if(component != null && component.getDelegate().isEditable()){
    	getLogger().addInfo("Component "+componentName+" is Editable");
    }else{
    	getLogger().addFailure("Component "+componentName+" is not Editable");
    }
	}
  
  
  private boolean setComponentValue(FocXMLGuiComponent component, String compNameForTheMessage, String componentValue, String priorityToCaptionProperty){
  	return setComponentValue(component, compNameForTheMessage, componentValue, false, priorityToCaptionProperty);
  }
  
  private boolean setComponentValue(FocXMLGuiComponent component, String compNameForTheMessage, String componentValue, boolean isAssert){
  	return setComponentValue(component, compNameForTheMessage, componentValue, isAssert, null);
  }
  
  private boolean setComponentValue(FocXMLGuiComponent component, String compNameForTheMessage, String componentValue, boolean isAssert, String priorityToCaptionProperty){
    boolean error = false;
    if(component != null){
    	if(component instanceof Component){
    		if(!((Component)component).isEnabled()){
    			getLogger().addFailure("Failed to Set component " + compNameForTheMessage + " to "+ componentValue + " because not enabled");
    			error = true;
    		}
    	}
    	if(!error){
    		if(component instanceof FVMultipleChoiceOptionGroupPopupView){
    			if(!isAssert){
    				component.setValueString(componentValue);
    			}
    		}else if(component instanceof FVObjectPopupView){
    			FVObjectPopupView choiceOptionGroupPopupView = (FVObjectPopupView) component;
    			boolean value = priorityToCaptionProperty == null || priorityToCaptionProperty.equals("true") ? true : false;
    			choiceOptionGroupPopupView.setPriorityToCaptionProperty(value);
    			component.setValueString(componentValue);
    		}else if(component instanceof FVObjectComboBox && componentValue.equalsIgnoreCase("")){
    			((FVObjectComboBox) component).setValue(null);
					((FVObjectComboBox) component).setValueString(null);
					componentValue = null;
    		}else{
    			if(!isAssert){
    				component.setValueString(componentValue);
    			}
        
    			String retValue = component.getValueString();
    			if(Utils.isEqual_String(retValue, componentValue)){
    				if(isAssert){
    					getLogger().addInfo("Asserted component " + compNameForTheMessage + " is equal to " + componentValue + ".");
    				}else{
    					getLogger().addInfo("Set component " + compNameForTheMessage + " to " + componentValue + ".");
    				}
    			}else{
    				error = true;
    				if(isAssert){
    					getLogger().addFailure("Failed Assertion component " + compNameForTheMessage + " = '" + retValue+ "' <> '"+ componentValue + "'");
    				}else{
        			retValue = component.getValueString();

    					getLogger().addFailure("Failed to Set component " + compNameForTheMessage + " to '" + componentValue + "' value remained = '"+retValue+"'");
    				}
    			}
    		}
    	}
    }
    return error;
   }
  
  /**
   * Simulates setting the value of a component in a table.
   * 
   * @param tableName
   *          The name of the table.
   * @param objRef
   *          The reference of the object to change as an integer.
   * @param fieldName
   *          The name of the field.
   * @param componentValue
   *          The value to set in the field.
   */
  public void setComponentValueInTable(String tableName, String objRef, String fieldName, String componentValue, String priorityToCaptionProperty) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    String componentName = TableTreeDelegate.newComponentName(tableName, objRef, fieldName);
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	setComponentValue(component, componentName, componentValue, priorityToCaptionProperty);
    }
  }
  
  public void assertComponentValueInTable(String tableName, String objRef, String fieldName, String componentValue) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    String componentName = TableTreeDelegate.newComponentName(tableName, objRef, fieldName);
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	setComponentValue(component, componentName, componentValue, true);
    }
  }
  
  public void assertComponentEnabledInTable(String tableName, String objRef, String fieldName, boolean assertEnabled) {
    String componentName = TableTreeDelegate.newComponentName(tableName, objRef, fieldName);
  	componentAssertEnabled(componentName, assertEnabled);
  }

  
  /**
   * Simulates selecting a component in a table.
   * 
   * @param tableName
   *          The name of the table.
   * @param objRef
   *          The reference of the object to change as an integer.
   * @param fieldName
   *          The name of the field.
   */
  public void selectComponentInTable(String tableName, String objRef, String fieldName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    String componentName = TableTreeDelegate.newComponentName(tableName, objRef, fieldName);
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	FVTableWrapperLayout tableWraperLayout = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    	tableWraperLayout.getTableTreeDelegate().adjustFormulaLayoutForComponent(component);
    }
  }
  
  public void applyTheFormula(String tableName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
   	FVTableWrapperLayout tableWraperLayout = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
   	if(tableWraperLayout != null && tableWraperLayout instanceof FVTableWrapperLayout){
    	String error = tableWraperLayout.getTableTreeDelegate().unitTesting_ApplyTheFormulaInTheTextField();
    	if(error != null){
    		getLogger().addError(error);
    	}
   	}else{
   		getLogger().addError("Component " + tableName + " is not an FVTableWrapperLayout");
   	}
  }

  public void checkReplaceCheckBoxForTable(String tableName, String value) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
   	FVTableWrapperLayout tableWraperLayout = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
   	if(tableWraperLayout != null && tableWraperLayout instanceof FVTableWrapperLayout){
   		if(value.toLowerCase().equals("true") || value.equals("1")){
   			String error = tableWraperLayout.setReplaceActive(true);
   			if(error != null){
   				getLogger().addError(error);	
   			}
   		}else if(value.toLowerCase().equals("false") || value.equals("0")){
   			String error = tableWraperLayout.setReplaceActive(false);
   			if(error != null){
   				getLogger().addError(error);	
   			}
   		}else{
   			getLogger().addError("Value = '"+value+"' is not boolean (true,false)");
   		}
   	}else{
   		getLogger().addError("Component " + tableName + " is not an FVTableWrapperLayout");
   	}
  }
  
  /**
   * Stores the value of a component in a hash map at the level of the
   * dictionary
   * 
   * @param componentName
   *          The name of the component.
   * @param variableName
   *          The name of the key that will contain the value in the hash map.
   */
  public void getComponentValue(String componentName, String variableName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
      getUnitSuite().getDictionary().putXMLVariable(variableName, component.getValueString());
      getLogger().addInfo("Adding the value of component " + componentName + " to the variable " + variableName + ".");
    }
  }

  /**
   * Simulates a click on a button of a certain name.
   * 
   */
  public void buttonClick(String buttonName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVButton button = (FVButton) findComponent(navigationLayout, buttonName);
    if (button != null) {
      button.click();
    } else {
      getUnitSuite().getDictionary().getLogger().addFailure("Button " + buttonName + " not found.");
    }
  }

  /**
   * Simulates a click on the "Add New Line" in an FVForEachLayout.
   * 
   * @param bannerLayoutName
   *          The name of the layout.
   */
  public void bannerAddNewLine(String bannerLayoutName) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      FVButton addLineButton = forEachLayout.getAddLineButton();

      if (addLineButton != null) {
        getLogger().addInfo("Adding a line in layout " + bannerLayoutName + ".");
        addLineButton.click();
      } else {
        getLogger().addFailure("Could not find 'Add New Line' button in layout " + bannerLayoutName + ".");
      }
    } else {
      getLogger().addFailure("Could not find layout " + bannerLayoutName + ".");
    }
  }

  /**
   * Simulates a click on the "Delete" in an FVForEachLayout on a specific line.
   * 
   * @param bannerLayoutName
   *          The name of the layout.
   * @param index
   *          The index of the line to delete.
   */
  public void bannerDeleteLineByIndex(String bannerLayoutName, String index) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      ArrayList<FVBannerLayout> bannerList = forEachLayout.getBannerList(false);

      if (bannerList != null) {
        int idx = Integer.parseInt(index);
        FVBannerLayout bannerLayout = bannerList.get(idx);

        if (bannerLayout != null) {
          DeleteButtonForEach deleteButton = bannerLayout.getDeleteButton();

          if (deleteButton != null) {
            getLogger().addInfo("Deleting line in layout " + bannerLayoutName + " at index " + index + ".");
            deleteButton.click();
          } else {
            getLogger().addFailure("Could not find Delete button");
          }
        } else {
          getLogger().addFailure("Could not find banner at index " + index + ".");
        }
      } else {
        getLogger().addFailure("Banner list is null.");
      }
    } else {
      getLogger().addFailure("Could not find layout " + bannerLayoutName + ".");
    }
  }

  /**
   * Simulates a click on the "Delete" in an FVForEachLayout on a specific line.
   * 
   * @param bannerLayoutName
   *          The name of the layout.
   * @param objRef
   *          The object reference of the line to delete.
   */
  public void bannerDeleteLineByReference(String bannerLayoutName, String objRef) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      ArrayList<FVBannerLayout> bannerList = forEachLayout.getBannerList(false);

      if (bannerList != null) {

        for (int i = 0; i < bannerList.size(); i++) {
          FVBannerLayout bannerLayout = bannerList.get(i);

          if (bannerLayout != null) {
            FocObject focObject = (FocObject) bannerLayout.getCentral().getFocData();
            if (focObject != null && focObject.getReference().toString().equals(objRef)) {
              DeleteButtonForEach deleteButton = bannerLayout.getDeleteButton();

              if (deleteButton != null) {
                deleteButton.click();
                getLogger().addInfo("Deleting line in layout " + bannerLayoutName + " with object reference " + objRef + ".");
              } else {
                getLogger().addFailure("Could not find Delete button");
              }
            }
          }
        }
      } else {
        getLogger().addFailure("Banner list is null.");
      }
    } else {
      getLogger().addFailure("Could not find layout " + bannerLayoutName + ".");
    }
  }

  /**
   * Simulates setting the value of a component in an FVForEachLayout.
   * 
   * @param bannerLayoutName
   *          The name of the layout.
   * @param objRef
   *          The reference of the line (The FVBannerLayout).
   * @param componentName
   *          The name of the component.
   * @param componentValue
   *          The value of the component.
   */
  public void setComponentValueInBannerByReference(String bannerLayoutName, String objRef, String componentName, String componentValue) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      FocXMLGuiComponent component = bannerFindLineByReferenceComponentByName(bannerLayoutName, componentName, objRef);
      if (component != null) {
      	setComponentValue(component, componentName, componentValue, null);
      } else {
        getLogger().addFailure("Component " + componentName + " not found in banner layout.");
      }

    } else {
      getLogger().addFailure("Banner layout " + bannerLayoutName + " not found.");
    }
  }

  /**
   * Simulates setting the value of a component in an FVForEachLayout.
   * 
   * @param bannerLayoutName
   *          The name of the layout.
   * @param index
   *          The index of the line (The FVBannerLayout).
   * @param componentName
   *          The name of the component.
   * @param componentValue
   *          The value of the component.
   */
  public void setComponentValueInBannerByIndex(String bannerLayoutName, String index, String componentName, String componentValue, boolean IsAssert) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      FocXMLGuiComponent component = bannerFindLineByIndexComponentByName(bannerLayoutName, componentName, index);
      if (component != null) {
      	setComponentValue(component, componentName, componentValue, IsAssert);
      } else {
        getLogger().addFailure("Component " + componentName + " not found in banner layout.");
      }
    } else {
      getLogger().addFailure("Banner layout " + bannerLayoutName + " bout found.");
    }

  }

  /**
   * Stores the reference of a banner line in a variable.
   * @param bannerLayoutName The name of the banner layout.
   * @param componentName The name of the component to look for.
   * @param componentValue The value of the component to look for.
   * @param variableName The variable in which we store the banner line reference.
   */
  public void getComponentValueInBanner(String bannerLayoutName, String componentName, String componentValue, String variableName) {
    FocXMLLayout layout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(layout, bannerLayoutName);

    if (forEachLayout != null) {
      ArrayList<FVBannerLayout> bannerList = forEachLayout.getBannerList(false);

      if (bannerList != null) {
        getLogger().addInfo("Looking for component " + componentName + " with value " + componentValue + " in banner layout " + bannerLayoutName + ".");
        for (int i = 0; i < bannerList.size(); i++) {
          FVBannerLayout bannerLayout = bannerList.get(i);

          FocXMLLayout centralLayout = bannerLayout.getCentral();
          FocXMLGuiComponent componentFound = findComponent(centralLayout, componentName);

          if (componentFound != null) {
            String valueFound = componentFound.getValueString();

            if (valueFound.equals(componentValue)) {
              FocObject object = (FocObject) bannerList.get(i).getCentral().getFocData();
              getLogger().addInfo("Component " + componentName + " with value " + componentValue + "found in banner layout " + bannerLayoutName + ".");
              if (object != null) {
                getUnitSuite().getDictionary().putXMLVariable(variableName, object.getReference().toString());
                getLogger().addInfo("Adding component reference to variable " + variableName + ".");
                break;
              }
            }
          }
        }
        getLogger().addInfo("Finished looking for component " + componentName + " with value " + componentValue + " in banner layout " + bannerLayoutName + ".");
      } else {
        getLogger().addFailure("Banner list is null.");
      }
    } else {
      getLogger().addFailure("Banner layout " + bannerLayoutName + " not found.");
    }
  }

  /**
   * Finds a component in an FVForEachLayout.
   * 
   * @param bannerLayoutName
   *          The name of the FVForEachLayout.
   * @param componentName
   *          The name of the component we are looking for.
   * @param objRef
   *          The reference of the FVBannerLayout in the bannerList.
   * @return The component, if found, otherwise null.
   */
  protected FocXMLGuiComponent bannerFindLineByReferenceComponentByName(String bannerLayoutName, String componentName, String objRef) {
    FocXMLGuiComponent component = null;
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      ArrayList<FVBannerLayout> bannerList = forEachLayout.getBannerList(false);

      if (bannerList != null) {

        for (int i = 0; i < bannerList.size(); i++) {
          FVBannerLayout bannerLayout = bannerList.get(i);

          if (bannerLayout != null) {
            FocObject focObject = (FocObject) bannerLayout.getCentral().getFocData();
            if (focObject != null && focObject.getReference().toString().equals(objRef)) {
              component = findComponent(bannerLayout.getCentral(), componentName);
              if (component != null) {
                getLogger().addInfo("Component " + componentName + " found in banner layout " + bannerLayoutName + " with object reference " + objRef + ".");
              }
            }
          } else {
            getLogger().addError("Banner is null at index " + i + ".");
          }
        }
      } else {
        getLogger().addFailure("Banner list is empty.");
      }
    } else {
      getLogger().addFailure("Could not find layout " + bannerLayoutName + ".");
    }
    return component;
  }

  /**
   * Finds a component in an FVForEachLayout.
   * 
   * @param bannerLayoutName
   *          The name of the FVForEachLayout.
   * @param componentName
   *          The name of the component we are looking for.
   * @param index
   *          The index of the FVBannerLayout in the bannerList.
   * @return The component, if found, otherwise null.
   */
  protected FocXMLGuiComponent bannerFindLineByIndexComponentByName(String bannerLayoutName, String componentName, String index) {
    FocXMLGuiComponent component = null;
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      ArrayList<FVBannerLayout> bannerList = forEachLayout.getBannerList(false);

      if (bannerList != null) {
        int idx = Integer.parseInt(index);
        FVBannerLayout bannerLayout = bannerList.get(idx);

        if (bannerLayout != null) {
          component = findComponent(bannerLayout.getCentral(), componentName);
        } else {
          getLogger().addFailure("Could not find banner at index " + index + ".");
        }
      } else {
        getLogger().addFailure("Banner list is empty.");
      }
    } else {
      getLogger().addFailure("Could not find layout " + bannerLayoutName + ".");
    }
    return component;
  }

  /**
   * Gets an FocXMLGuiComponent by name.
   * 
   * @param navigationLayout
   *          Name of the FocXMLLayout to look in.
   * @param name
   *          Name of the component.
   * @return the FocXMLGuiComponent
   */
  protected FocXMLGuiComponent findComponent(FocXMLLayout navigationLayout, final String componentName) {
  	return findComponent(navigationLayout, componentName, true);
  }

  /**
   * Gets an FocXMLGuiComponent by name.
   * 
   * @param navigationLayout
   *          Name of the FocXMLLayout to look in.
   * @param name
   *          Name of the component.
   * @return the FocXMLGuiComponent
   */
  protected FocXMLGuiComponent findComponent(FocXMLLayout navigationLayout, final String componentName, boolean failIfNotFound) {
  	FocXMLGuiComponent component = (FocXMLGuiComponent) navigationLayout.getComponentByName(componentName);

  	if(component == null){
  		if(failIfNotFound){
	  		getLogger().addFailure("Could not find component " + componentName + ".");
	  		navigationLayout.debug_PrintAllComponents();
  		}
  	}else{
			AbstractComponent comp = (AbstractComponent) component;
			
			FVMoreLayout moreLayout = comp.findAncestor(FVMoreLayout.class);
			
			if(moreLayout != null && !moreLayout.isExtended()){
				moreLayout.setExtended(true);
			}
  		getLogger().addInfo("Component " + componentName + " found.");
  	}
  	return component;
  }

  /**
   * Sets a Variable value can be used for constants to declare once the name of a project or a client... 
   * 
   * @param variableName
   *          Name of the variable
   * @param variableValue
   *          Value of the variable
   */
  public void setVariable(String variableName, String variableValue){
    if(getUnitSuite() != null && getUnitSuite().getDictionary() != null && variableName != null && variableValue != null){
      getUnitSuite().getDictionary().putXMLVariable(variableName, variableValue);
    }
  }
  
  /**
   * Simulates a drag and drop event.
   * @param source
   * @param sourceItem
   * @param target
   */
  
  public void dragAndDropHighLevel(String sourceName, String sourcePropertyName, String sourcePropertyValue, String targetName, String targetPropertyName, String targetPropertyValue){
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
  	
  	if(navigationLayout != null){
  		
  		FVTableWrapperLayout sourceWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, sourceName);
  		FVTableWrapperLayout targetWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, targetName);
  		
  		ITableTree sourceTableOrTree = sourceWrapper == null ? null : sourceWrapper.getTableOrTree();
  		ITableTree targetTableOrTree = targetWrapper == null ? null : targetWrapper.getTableOrTree();
  		
  		Object sourceItemId = null, targetItemId = null;
  		
  		if(sourceTableOrTree != null){
  			FocObject sourceObject = sourceTableOrTree.getTableTreeDelegate().selectByFocProperty(sourcePropertyName, sourcePropertyValue);
  			
  			if(sourceObject != null){
  				sourceItemId = sourceObject.getReference().getInteger();
  			}else{
  				String message = "Could not find source row PROPERTY="+sourcePropertyName+" VALUE="+sourcePropertyValue;
  				getLogger().addFailure(message);
  				Globals.logString(message);
  				sourceTableOrTree.getTableTreeDelegate().debug_ListOfColumns();
  			}
  		}
  		
  		if(targetTableOrTree != null){
  			if(targetPropertyName != null && targetPropertyValue != null){
  				FocObject targetObject = targetTableOrTree.getTableTreeDelegate().selectByFocProperty(targetPropertyName, targetPropertyValue);
  				
  				if(targetObject != null){
  					targetItemId = targetObject.getReference().getInteger();
  				}
  			}
  		}
  		
  		dragAndDropLowLevel(sourceTableOrTree, sourceItemId, targetTableOrTree, targetItemId);
  	}
  }
  
  private void dragAndDropLowLevel(ITableTree source, Object sourceItemId, ITableTree target, Object targetItemId){

  	if(source != null && sourceItemId != null && target != null){

  		Map<String, Object> rawVariables = new HashMap<String, Object>();

  		Object propertyId = "1";
  		
  		rawVariables.put("propertyId", null);
  		rawVariables.put("itemId", null);

  		DataBoundTransferable dbt = null;

  		if(source instanceof FVTable){
  			FVTable sourceTable = (FVTable) source;
  			dbt = sourceTable.getTransferable(rawVariables);
  		} else if (source instanceof FVTreeTable){
  			FVTreeTable sourceTree = (FVTreeTable) source;
  			dbt = sourceTree.getTransferable(rawVariables);
  		}
  		dbt.setData("propertyId", propertyId);
			dbt.setData("itemId", sourceItemId);

  		DropHandler dropHandler = null;
  		AbstractSelectTargetDetails astd = null;
  		
  		if(target instanceof FVTable){
  			FVTable targetTable = (FVTable) target;
  			dropHandler = targetTable.getDropHandler();
  			
  			rawVariables = new HashMap<String, Object>();
  			rawVariables.put("itemIdOver", null);
  			astd = targetTable.translateDropTargetDetails(rawVariables);
  		} else if (target instanceof FVTreeTable){
  			FVTreeTable targetTree = (FVTreeTable) target;
  			dropHandler = targetTree.getDropHandler();
  			
  			if(targetItemId != null){
  				rawVariables = new HashMap<String, Object>();
  				rawVariables.put("itemIdOver", null);
  				astd = targetTree.translateDropTargetDetails(rawVariables);
  				astd.setData("itemIdOver", targetItemId);
  			}
  		}

  		DragAndDropEvent dde = new DragAndDropEvent(dbt, astd);
  		if(dropHandler != null){
  			dropHandler.drop(dde);
  			getLogger().addInfo("Drag and drop successful");
  		}
  	}
  	else{
  		getLogger().addFailure("Drag and drop failure.");
  	}
  }
  
  public void login(){
    NativeButton loginButton = getMainWindow().getLoginButton();
    if (loginButton != null) {
      loginButton.click();
    } else {
      getLogger().addFailure("Login button not found.");
    }
  }
  
  public void sign(){
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();
      if (validationLayout != null) {
      	FVStageLayout_MenuBar stageLayout = validationLayout.getStageLayout(false);
      	if(stageLayout != null){
      		stageLayout.sign(null);
      	}
      }
    }
  }

  public void callTest(){
	  String suiteName = attributes.getValue(FXMLUnit.ATT_CALL_SUIT);
	  String testName = attributes.getValue(FXMLUnit.ATT_CALL_TEST);
	  String testComposed = attributes.getValue(FXMLUnit.ATT_CALL_TEST_COMPOSED);
	  
	  if(testComposed != null){
	  	StringTokenizer tok = new StringTokenizer(testComposed, ">");
	  	String token1 = null;
	  	String token2 = null;
	  	if(tok.hasMoreTokens()) token1 = tok.nextToken();  
	  	if(tok.hasMoreTokens()) token2 = tok.nextToken();

	  	if(token2 != null){
	  		suiteName = token1;
	  		testName = token2;
	  	}else{
	  		suiteName = null;
	  		testName = token1;
	  	}
	  }
	
	  FocUnitTestingSuite suite = getUnitSuite();
	  if (suiteName != null){
	    suite = FocUnitDictionary.getInstance().getTestingSuite(suiteName);
	  }
	
	  if (suite != null) {
	    if (testName == null || testName.isEmpty()) {
	      suite.runSuite();
	    } else {
	      suite.runTestByName(testName, attributes);
	    }
	  }
  }
  
  public void initialiseSaaSAccount(){
  	String company     = "ANONYMOUS";
  	String accountName = "ADEL";
  	String password    = "ADEL123";
  	String currency    = "USD";
  	String appType     = SaaSConfigDesc.APP_TYPE_PROCUREMENT;
  	String appPlan     = SaaSConfigDesc.PLAN_NAME_FREE;
  	String userRole    = FocUserDesc.APP_ROLE_NAME_PROCUREMENT_OFFICER;
  	
    String encryptedPassword = Encryptor.encrypt_MD5(String.valueOf(password));
  	
		FocWebService focWebService = FocWebServer.getInstance().newFocWebService();
		focWebService.initAcount(company, accountName, encryptedPassword, currency, appType, appPlan, userRole, null);
  }
  
  /**
   * Simulates a click on a button of a certain name.
   * 
   */
  public void popupButtonClick(String firstButton, String secondButton) {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVPopupButton button = (FVPopupButton) findComponent(navigationLayout, firstButton);
    if (button != null) {
    	FVPopupContentButton contentButton = button.getContentButton(secondButton);
    	if(contentButton != null){
    		contentButton.click();
    	}else{
    		getUnitSuite().getDictionary().getLogger().addFailure("Content Button " + secondButton + " for popup button "+firstButton+" not found.");
    	}
    } else {
      getUnitSuite().getDictionary().getLogger().addFailure("Popup Button " + firstButton + " not found.");
    }
  }

}