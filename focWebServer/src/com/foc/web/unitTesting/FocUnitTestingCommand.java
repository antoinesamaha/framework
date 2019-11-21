/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocDesc;
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
import com.foc.vaadin.gui.components.FVButtonClickEvent;
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
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVStageLayout_Button;
import com.foc.vaadin.gui.layouts.validationLayout.FVStatusLayout_MenuBar;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVViewSelector_MenuBar;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.admin.FocUser_HomePage_Form;
import com.foc.web.modules.workflow.gui.WFConsole_Form;
import com.foc.web.server.FocWebServer;
import com.foc.webservice.FocWebService;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetailsImpl;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

public class FocUnitTestingCommand {
  private String methodName = null;
  private FocUnitXMLAttributes attributes = null;
  private FocUnitTest unitTest = null;

  private static final int ASSERT_ONLY          =  1;
  private static final int SET_VALUE_AND_ASSERT =  0;
  private static final int DO_NOT_ASSERT        = -1;
  
  private boolean memoryCheckActive = false;
  
  public FocUnitTestingCommand(FocUnitTest unitTest, String methodName, Attributes attributes) {
  	setUnitTest(unitTest);
    setMethodName(methodName);
    setAttributes(new FocUnitXMLAttributes(unitTest, attributes));
  }

  public void dispose() {
    unitTest = null;
    methodName = null;
    attributes = null;
  }

  protected FocUnitDictionary getDictionary() {
    return getUnitSuite() != null ? getUnitSuite().getDictionary() : null;
  }
  
  public String getLayoutName(){
  	return getAttributes() != null ? getAttributes().getValue(FXMLUnit.ATT_LAYOUT_NAME) : null;
  }
  
  public String setLayoutName(String layoutName){
  	String backupName = getLayoutName();
  	boolean nodeCreated = !getLogger().openCommand("Change Layout : "+layoutName);
  	if(layoutName != null){
  		getAttributes().addAttribute(FXMLUnit.ATT_LAYOUT_NAME, layoutName);
  	}else{
  		getAttributes().removeAttribute(FXMLUnit.ATT_LAYOUT_NAME);
  	}
  	if(nodeCreated) getLogger().closeNode();
  	return backupName;
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
  
  public void setUnitTest(FocUnitTest unitTest) {
  	this.unitTest = unitTest;
  	if(getAttributes() != null) getAttributes().setTest(unitTest);
  }

  public FocUnitTestingSuite getUnitSuite() {
    return getUnitTest().getSuite();
  }

  private boolean isStopWhenFail(){
  	return true;
  }
  
  public void execute() throws Exception {
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
  public FocXMLLayout getCurrentCentralPanel() throws Exception {
  	FocXMLLayout result = null;

  	FocWebApplication ui = (FocWebApplication) FocWebApplication.getInstanceForThread();
    FocWebVaadinWindow window = (FocWebVaadinWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();
    
    if (window != null) {
    	result = (FocXMLLayout) window.getCentralPanel();
      Collection<Window> children = ui.getWindows();
      if (!children.isEmpty()) {
      	Object obj = ((Window) children.toArray()[children.size()-1]).getContent();
      	if(obj instanceof FocXMLLayout){
      		result = (FocXMLLayout) obj;
      	}else{
      		FocCentralPanel panel = (FocCentralPanel) ((Window) children.toArray()[children.size()-1]).getContent();
          
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
  		Component layoutComponent = (Component) findComponent(result, layoutName, false);
  		if(layoutComponent instanceof FVTableWrapperLayout) {
		    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) layoutComponent;
		    if (tableWrapper != null) {
		    	ICentralPanel centralPanel = tableWrapper.innerLayout_GetICentralPanel();
		    	if(centralPanel != null){
		    		result = (FocXMLLayout) centralPanel; 
		    	}
		    }
  		} else {
  			result = (FocXMLLayout) layoutComponent;
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

  public void popupUserAccount() {
  	FocUser_HomePage_Form.popupUserCredintionals(getMainWindow());
  }
  
  public void logout_IfMemoryCheckActive() throws Exception {
  	if(getDictionary().isMemoryCheckActive()) {
  		logout();
  	}
  }
  
  /**
   * Simulates a click on the "Log Out" button in the application.
   */
  public void logout() throws Exception {
  	if(Globals.isValo()){
  		
  		getDictionary().incrementTestIndexes();
  		getMainWindow().logout();
  		FocUnitDictionary.getInstance().setExitTesting(true);
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
  public void button_ClickNavigate() throws Exception {
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
  public void button_ClickHome() throws Exception {
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
  protected void button_ClickAdmin() throws Exception {
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
  public void button_ClickApply() throws Exception {
  	boolean nodeCreated = !getLogger().openCommand("Apply");
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
    
    if(nodeCreated) getLogger().closeNode();
  }
  
  public void button_ClickSave(String layoutName) throws Exception {
  	boolean nodeCreated = !getLogger().openCommand("Save");
  	String backup = getLayoutName();
  	setLayoutName(layoutName);
  	button_ClickSave();
  	setLayoutName(backup);
  	if(nodeCreated) getLogger().closeNode();
  }
  
  public void button_ClickSave() throws Exception {
  	boolean nodeCreated = !getLogger().openCommand("Save");
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
    if(nodeCreated) getLogger().closeNode();
  }

  /**
   * Simulates clicking on the "Apply" button in the validation layout until the apply button can't be found anymore.
   * 
   */
  protected void button_ClickApplyRecursive() throws Exception {
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
  protected void button_ClickDiscard() throws Exception {
    button_ClickDiscard(null);
  }
  
  /**
   * Simulates a click on the "Discard" button in the validation layout.
   * 
   */
  protected void button_ClickDiscard(String tableName) throws Exception {
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
   * Simulates a click on the "Delete" button in the validation layout.
   * 
   */
  protected void button_ClickDelete(String tableName) throws Exception {
   	boolean nodeCreated = !getLogger().openCommand("Delete");
   	FocXMLLayout navigationLayout = getCurrentCentralPanel();
   	
     if (navigationLayout != null) {
       FVValidationLayout validationLayout = navigationLayout.getValidationLayout();

       if (validationLayout != null) {
         AbstractComponent delete = validationLayout.valo_GetDeleteEmbedded(false);      		
         if (delete != null) {
        	 validationLayout.deleteButtonClickAction();
         } else {
           getLogger().addFailure("Delete button not found.");
         }
       } else {
         getLogger().addFailure("Validation layout not found");
       }
     } else {
       getLogger().addFailure("Navigation layout not found");
     }
     
     if(nodeCreated) getLogger().closeNode();
  }
  
  /**
   * Simulates a click on the "Print" button in the validation layout.
   * 
   */
  protected void button_ClickPrint() throws Exception {
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
  protected void button_ClickFullScreen() throws Exception {
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
  protected void button_ClickAttachment() throws Exception {
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
  protected void button_ClickBack() throws Exception {
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

  protected void view_Select(String layoutName, String viewName) throws Exception {
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
  protected void status_Approve() throws Exception {
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
  public void gearComponentAdd(String componentName) throws Exception {
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
  public void gearComponentOpen(String componentName) throws Exception {
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
  protected MenuItem searchMoreMenu(String menuCaption) throws Exception {
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
  protected void moreMenu_Select(String menuCaption) throws Exception {
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
   */
  public void menu_Highlight(String menuCode) throws Exception {
    button_ClickNavigate();
    changePanel(menuCode, false);
  }
  
  public void menuAdmin_Highlight(String menuCode) throws Exception {
  	button_ClickAdmin();
  	changePanel(menuCode, false);
  }
  
  /**
   * open Panel according to menuCode
   * @param menuCode
   */
  private void changePanel(String menuCode, boolean assertNotAvailable) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    if (navigationLayout != null) {
      FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) navigationLayout.getComponentByName("MENU_TREE");

      if (tableWrapper != null) {
        FVTreeTable treeTable = (FVTreeTable) tableWrapper.getTableOrTree();

        if (treeTable != null) {
          TableTreeDelegate delegate = treeTable.getTableTreeDelegate();

          getLogger().addInfo("Navigating to the menu item with the code " + menuCode + ".");
          FocObject object = delegate.selectByFocProperty(FocMenuItemConst.FNAME_CODE, menuCode);

          if(assertNotAvailable) {
          	if (object != null) {
          		getLogger().addFailure("Menu " + menuCode + " is available while it should not");
          	}
          } else {
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
  }

  public void menu_ExistAssert(String menuCode, boolean exist) throws Exception {
  	boolean nodeOpened = !getLogger().openCommand("Assert menu "+menuCode+" "+(exist?"exists":"does not exist"));
  	button_ClickNavigate();
  	changePanel(menuCode, true);
  	if(nodeOpened) getLogger().closeNode();
  }
  
  /**
   * Simulates the navigation to a specific menu item in the main navigation
   * while expanding all parent menu items and selecting the menu item in
   * question. This function then constructs a mouse click event to simulate a
   * double click on the selected item.
   * 
   */
  public void menu_Click(String menuCode) throws Exception {
  	boolean nodeOpened = !getLogger().openCommand("Navigate to : "+menuCode);
    menu_Highlight(menuCode);
    menuDoubleClickAction();
    if(nodeOpened) getLogger().closeNode();
  }

  public void menuAdmin_Click(String menuCode) throws Exception {
  	boolean nodeOpened = !getLogger().openCommand("Navigate to Admin : "+menuCode);
  	menuAdmin_Highlight(menuCode);
  	menuDoubleClickAction();
    if(nodeOpened) getLogger().closeNode();
  }

  /**
   * MenuTree Double click action 
   */
  private void menuDoubleClickAction() throws Exception {
  	boolean nodeOpened = !getLogger().openCommand("Menu Double Click");
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, "MENU_TREE");
    FVTreeTable treeTable = (FVTreeTable) tableWrapper.getTableOrTree();

    String eventString = MouseEventDetails.MouseButton.LEFT+",0,0,false,false,false,false,2,0,0";
    MouseEventDetails details = MouseEventDetails.deSerialize(eventString);

    ItemClickEvent doubleClickEvent = new ItemClickEvent(treeTable, treeTable.getFocList().getItem(treeTable.getValue()), treeTable.getValue(), "TITLE", details);
    treeTable.getItemClickListener().itemClick(doubleClickEvent);
    if(nodeOpened) getLogger().closeNode();
  }
  
  /**
   * Simulates a navigation and a click on the "Launch Unit Test" button
   * effectively launching the test over and over again. Useful function to
   * crash the server for the lulz.
   * 
   */
  public void launchUnitTest() throws Exception {
    button_ClickAdmin();
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

  public void tree_CollapseAll(String tableName, boolean collapse) throws Exception {
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
   * Sets the quick filter expression on the top right of the table. It checks if exists and enabled and returns the size of the list
   * 
   * @param tableName
   * @param filterValue
   * @return the size of the table after setting to the filterValue
   * @throws Exception
   */
  public int table_QuickFilter(String tableName, String filterValue) throws Exception {
    int size = -1;
    boolean nodeCreated = !getLogger().openCommand("Table "+tableName+" quick filter on: "+filterValue);
    
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    if(tableWrapper != null){//The find method will report the right log of failure
      String errorMessage = tableWrapper.setQuickFilterExpression(filterValue);
      if(Utils.isStringEmpty(errorMessage)) {
        FocDataWrapper wrapper = tableWrapper.getFocDataWrapper();
        size = wrapper.size();
      } else {
        getLogger().addFailure("Could not quick filter on table" + tableName+" Because: "+errorMessage);
      }
    } else {
      getLogger().addFailure("Could not find table " + tableName);
    }
    
    if(nodeCreated) getLogger().closeNode();
    
    return size;
  }
  
  public int table_Size(String tableName) throws Exception {
  	int size = -1;
  	boolean nodeCreated = !getLogger().openCommand("Table "+tableName+" get size");
  	
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    if(tableWrapper != null){//The find method will report the right log of failure
    	FocDataWrapper wrapper = tableWrapper.getFocDataWrapper();
    	size = wrapper.size();
    } else {
      getLogger().addFailure("Could not find table " + tableName);
    }
    
    if(nodeCreated) getLogger().closeNode();
    
    return size;
  }
  
  public long table_Select(String tableName, String propertyName, String propertyValue) throws Exception {
  	return table_Select(tableName, propertyName, propertyValue, null, 0);
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
  public long table_Select(String tableName, String propertyName, String propertyValue, String variableName, int ancestor) throws Exception {
  	boolean nodeCreated = !getLogger().openCommand("Table select where "+propertyName+" = "+propertyValue +" -> "+variableName);
  	long referenceOfSelectedItem = 0;
  	
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    if(tableWrapper != null){//The find method will report the right log of failure
    	ITableTree iTableTree = tableWrapper.getTableOrTree();
	    
    	FocObject object = iTableTree != null ? iTableTree.getTableTreeDelegate().selectByFocProperty(propertyName, propertyValue, ancestor) : null;
  		
	    if(object != null && object.getReference() != null){
	    	referenceOfSelectedItem = object.getReference().getLong();
	    	String reference = object.getReference().toString();

	    	//If tree table we expand all the parents so that the item becomes visible for selection
	    	if(iTableTree instanceof FVTreeTable){
	    		FVTreeTable treeTable = (FVTreeTable) iTableTree;
	    		Long ref = object.getReference().getLong();
	    		ref = (Long) treeTable.getParent(ref);
	    		while(ref != null){
	    			treeTable.setCollapsed(ref, false);
	    			ref = (Long) treeTable.getParent(ref);	    			
	    		}
	    	}
	    	//---------------------------------------------------------------------------------------
	    	
	    	if (variableName != null && !variableName.isEmpty()) {
	        getDictionary().putXMLVariable(variableName, reference);
	        getLogger().addInfo("Storing selected item reference in variable " + variableName + ".");
		    }	    	
	    }else {
        getLogger().addFailure("Could not find item in table " + tableName + " where " + propertyName + " = " + propertyValue);
      }
	    if(nodeCreated) getLogger().closeNode();
    }
    return referenceOfSelectedItem;
  }

  public long table_AddOrSelect(String tableName, String propertyName, String propertyValue, boolean assertOnly) throws Exception {
  	long ref = 0;
  	if(assertOnly) {
    	ref = table_Select(tableName, propertyName, propertyValue);
  	}else{
  		ref = table_Add(tableName);
  	}
  	
  	return ref;
  }
  	
  public long table_AddOrOpen(String tableName, String propertyName, String propertyValue, boolean assertOnly) throws Exception {
  	long ref = 0;
  	if(assertOnly) {
    	ref = table_Open(tableName, propertyName, propertyValue);
  	}else{
  		ref = table_Add(tableName);
  	}
  	
  	return ref;
  }
  
  /**
   * Simulates adding an item in an open table (right-click then Add). Also stores
   * the id of the created object in a variable.
   * 
   * @param tableName
   *          The name of the table to add an item in.
   */
  public long table_Add(String tableName) throws Exception {
  	return table_Add(tableName, null);
  }
  
  /**
   * Simulates adding an item in an open table (right-click then Add). Also stores
   * the id of the created object in a variable.
   * 
   * @param tableName
   *          The name of the table to add an item in.
   * @param variableName
   *          The variable that will contain the id of the created object.
   */
  public long table_Add(String tableName, String variableName) throws Exception {
  	long ref = 0;
  	String message = "Table add item : "+tableName;
  	if(!Utils.isStringEmpty(variableName)) message += " -> "+variableName;
  	boolean nodeCreated = !getLogger().openCommand(message);
  	
    String reference = null;
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    if (tableWrapper != null) {
      Table table = (Table) tableWrapper.getTableOrTree();
      if (table != null) {
        FocObject father = null;
        if (table.getValue() != null) {
          father = ((ITableTree) table).getFocList().searchByReference((Long) table.getValue());
        }
        TableTreeDelegate tableDelegate = ((ITableTree) table).getTableTreeDelegate();
        if(tableDelegate == null || !tableDelegate.isAddEnabled()) {
        	getLogger().addFailure("Add not allowed in table " + tableName);
        }else{
	        FocObject object = tableDelegate.addItem(father);
	        getLogger().addInfo("Adding a new item in table " + tableName + ".");
	        if (object != null && object.getReference() != null) {
	        	ref = object.getReference().getLong();
	          if (variableName != null && !variableName.isEmpty()) {
	            reference = object.getReference().toString();
	            getDictionary().putXMLVariable(variableName, reference);
	            getLogger().addInfo("Storing added item reference in variable " + variableName + ".");
	          }
	          table.select(object.getReference().getLong());
	        }
	      }
      }
    }
    if(nodeCreated) getLogger().closeNode();
    return ref;
  }

  /**
   * Simulates a popup menu selection on a table 
   * 
   * @param tableName
   *          The name of the table to add an item in.
   * @param menuCaption
   *          Menu caption
   */
  public void table_RightClick(String tableName, String menuCaption) throws Exception {
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
        	Long currentSelection = (Long) table.getValue();
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
  
  public long table_Open(String tableName, String propertyName, String propertyValue) throws Exception  {
  	boolean nodeCreated = !getLogger().openCommand("Open item in table "+tableName+" where "+propertyName+" = "+propertyValue);
  	long ref = table_Select(tableName, propertyName, propertyValue, null, 0);
  	table_Open(tableName);
  	if(nodeCreated) getLogger().closeNode();
  	return ref;
  }
  
  /**
   * Simulates opening the selected item in a table (right-click then Open).
   * 
   * @param tableName
   *          The name of the table to open the item selected in.
   */
  public void table_Open(String tableName) throws Exception  {
  	boolean nodeCreated = !getLogger().openCommand("Table open item : "+tableName);
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    if(tableWrapper == null) {
    	getLogger().addFailure("Table "+tableName+" not found");
    } else {
	    Table table = (Table) tableWrapper.getTableOrTree();
	
	    if(table.getValue() == null){
	      getLogger().addFailure("No item selected in table : " + tableName + ".");
	    }else{
	      FocObject obj = ((ITableTree) table).getFocList().searchByReference((Long) table.getValue());
	      if (obj != null) {
	        ((ITableTree) table).getTableTreeDelegate().open(obj);
	        getLogger().addInfo("Opening selected item in table " + tableName + ".");
	      } else {
	        getLogger().addFailure("No object selected. Could not open in table " + tableName + ".");
	      }
	    }
    }
    if(nodeCreated) getLogger().closeNode();
  }

  /**
   * Simulates deleting the selected item in a table (right-click then Delete).
   * 
   * @param tableName
   *          The name of the table to delete the item selected in.
   */
  public void table_Delete(String tableName) throws Exception  {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    Table table = (Table) tableWrapper.getTableOrTree();

    FocObject obj = ((ITableTree) table).getFocList().searchByReference((Long) table.getValue());
    if (obj != null) {
    	tableWrapper.deleteItemClickListenerContent();
    	button_Click("DELETE");
      //((ITableTree) table).getTableTreeDelegate().delete_NoPopupConfirmation(obj);
      getLogger().addInfo("Deleing selected item in table " + tableName + ".");
    } else {
      getLogger().addFailure("No object selected. Could not delete in table " + tableName + ".");
    }
  }
  
  public void component_AssertEnabled(String componentName, boolean assertEnabled) throws Exception {
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FocXMLGuiComponent component  = findComponent(navigationLayout, componentName);
    
    component_AssertEnabled(component, componentName, assertEnabled);
  }
  
  private void component_AssertEnabled(FocXMLGuiComponent component, String fieldNameForMessage, boolean assertEnabled) throws Exception {
    //Editable
    if(component != null && component instanceof Component){
      Component componentToCheck = (Component) component;
      if(componentToCheck instanceof FVWrapperLayout) {
        componentToCheck = ((FVWrapperLayout)component).getFormField();
      }
      //If we are not in assertOnly this means the component has to be enabled
      if(componentToCheck.isEnabled() != assertEnabled){
        getLogger().addFailure("Component "+fieldNameForMessage+" enable status: "+componentToCheck.isEnabled()+" different then expected: "+assertEnabled);
      } else {
        getLogger().addInfo("Component "+fieldNameForMessage+" enable status: "+componentToCheck.isEnabled()+" as expected");
      }
    }else{
      getLogger().addFailure("Faild to check component "+fieldNameForMessage+" enabled because either null or not instanceof Component");
    }
  }
  
  private boolean assertComponentValue(String componetnValue, String expectedComponentValue) throws Exception {
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
  public void component_SetValue(String componentName, String componentValue, boolean isAssert) throws Exception {
  	component_SetValue(componentName, componentValue, isAssert ? ASSERT_ONLY : SET_VALUE_AND_ASSERT);
  }

  public void component_SetValue_DoNotVerify(String componentName, String componentValue) throws Exception {
  	component_SetValue(componentName, componentValue, DO_NOT_ASSERT);
  }

  private void component_SetValue(String componentName, String componentValue, int isAssert) throws Exception {
  	boolean nodeCreated = !getLogger().openCommand("Set "+componentName+" = "+componentValue);
  	
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if(navigationLayout == null){
    	navigationLayout = getCurrentCentralPanel();
    }
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
   	setComponentValue(component, componentName, componentValue, isAssert, null);
   	
   	if(nodeCreated) getLogger().closeNode();
  }
  
  public void component_SetValue(String layoutName, String componentName, String componentValue) throws Exception {
  	component_SetValueOrAssert(layoutName, componentName, componentValue, false); 
  }
  
  public void component_SetValue(String layoutName, String componentName, String componentValue, boolean isAssertOnly) throws Exception {
  	component_SetValueOrAssert(layoutName, componentName, componentValue, isAssertOnly); 
  }
  
  public void component_AssertValue(String layoutName, String componentName, String componentValue) throws Exception {
  	component_SetValueOrAssert(layoutName, componentName, componentValue, true);
  }
  
  public void component_SetValueOrAssert(String layoutName, String componentName, String componentValue, boolean isAssert) throws Exception {
  	boolean nodeCreated = false;
  	if(isAssert){
  		nodeCreated = !getLogger().openCommand("Assert "+componentName+" = "+componentValue);
  	}else{
  		nodeCreated = !getLogger().openCommand("Set "+componentName+" = "+componentValue);
  	}
  	
  	String originalLayout = getLayoutName();
  	setLayoutName(layoutName);
  	component_SetValue(componentName, componentValue, isAssert);
  	setLayoutName(originalLayout);
  	
   	if(nodeCreated) getLogger().closeNode();
  }
  
  public void component_AssertEditable(String componentName) throws Exception {
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if(component != null && component.getDelegate().isEditable()){
    	getLogger().addInfo("Component "+componentName+" is Editable");
    }else{
    	getLogger().addFailure("Component "+componentName+" is not Editable");
    }
	}
  
  public void component_AssertEditable(String componentName, boolean editable) throws Exception {
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if(component != null && component.getDelegate().isEditable() == editable){
    	getLogger().addInfo("Component "+componentName+(editable ? " is Editable" : " is not editable"));
    }else{
    	getLogger().addFailure("Component "+componentName+(editable ? " should be Editable" : " should not be editable"));
    }
	}
  
  public void component_AssertVisible(String componentName, boolean visible) throws Exception {
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if(component == null) {
    	getLogger().addFailure("Component "+componentName+" not found");
    } else {
    	if(component instanceof Component) {
    		Component comp = (Component) component;
    		boolean compVisible = comp.isVisible();
    		while(compVisible && comp != null) {
    			comp = comp.getParent();
    			if(comp != null) compVisible = comp.isVisible();
    		}
    		
        if(compVisible == visible){
        	getLogger().addInfo("Component "+componentName+(visible ? " is Visible " : " is not Visible"));
        }else{
        	getLogger().addFailure("Component "+componentName+(visible ? " should be Visible " : " should not be Visible"));
        }    		
    	}
    }
	}
    
  private void setComponentValue(FocXMLGuiComponent component, String compNameForTheMessage, String componentValue, int assertOnly, String priorityToCaptionProperty) throws Exception {
    if(component != null){
      if(assertOnly != ASSERT_ONLY) {
        component_AssertEnabled(component, compNameForTheMessage, true);
      }
      
  		if(component instanceof FVMultipleChoiceOptionGroupPopupView){
  			if(assertOnly != ASSERT_ONLY){
  				component.setValueString(componentValue);
  			}
  		}else if(component instanceof FVObjectPopupView){
  			FVObjectPopupView choiceOptionGroupPopupView = (FVObjectPopupView) component;
  			boolean value = priorityToCaptionProperty == null || priorityToCaptionProperty.equals("true") ? true : false;
  			choiceOptionGroupPopupView.setPriorityToCaptionProperty(value);
  			component.setValueString(componentValue);
  		}else if(component instanceof FVObjectComboBox && componentValue.equalsIgnoreCase("")){
  			if(assertOnly == ASSERT_ONLY){
  				String retValue = component.getValueString();
  				if(retValue == null) {
  					getLogger().addInfo("Asserted component " + compNameForTheMessage + " is empty");
  				} else {
  					getLogger().addFailure("Failed Assertion component " + compNameForTheMessage + " != ''");
  				}
  			} else {
	  			((FVObjectComboBox) component).setValue(null);
					((FVObjectComboBox) component).setValueString(null);
  			}				
				componentValue = null;
  		}else{
  			if(assertOnly != ASSERT_ONLY){
  				component.setValueString(componentValue);
  			}

  			String retValue = component.getValueString();
  			if(assertOnly == DO_NOT_ASSERT || Utils.isEqual_String(retValue, componentValue)){
  				if(assertOnly == ASSERT_ONLY){
  					getLogger().addInfo("Asserted component " + compNameForTheMessage + " is equal to " + componentValue + ".");
  				}else{
  					getLogger().addInfo("Set component " + compNameForTheMessage + " to " + componentValue + ".");
  				}
  			}else{
  				if(assertOnly == ASSERT_ONLY){
  					getLogger().addFailure("Failed Assertion component " + compNameForTheMessage + " = '" + retValue+ "' <> '"+ componentValue + "'");
  				}else{
      			retValue = component.getValueString();
  					getLogger().addFailure("Failed to Set component " + compNameForTheMessage + " to '" + componentValue + "' value remained = '"+retValue+"'");
  				}
  			}
  		}
    }
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
  public void componentInTable_SetValue(String tableName, long objRef, String fieldName, String componentValue) throws Exception {
  	componentInTable_SetValue(tableName, objRef, fieldName, componentValue, null); 
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
  public void componentInTable_SetValue(String tableName, long objRef, String fieldName, String componentValue, String priorityToCaptionProperty) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    String componentName = TableTreeDelegate.newComponentName(tableName, String.valueOf(objRef), fieldName);
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	setComponentValue(component, componentName, componentValue, SET_VALUE_AND_ASSERT, priorityToCaptionProperty);
    }
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
   * @param assertOnly
   *          Do not set the value, only assert that the value is equal to this
   */
  public void componentInTable_SetValue(String tableName, long objRef, String fieldName, String componentValue, boolean assertOnly) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    String componentName = TableTreeDelegate.newComponentName(tableName, String.valueOf(objRef), fieldName);
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	setComponentValue(component, componentName, componentValue, assertOnly ? ASSERT_ONLY : SET_VALUE_AND_ASSERT, null);
    }
  }

  public void componentInTable_SetValue_DoNotVerify(String tableName, long objRef, String fieldName, String componentValue) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    String componentName = TableTreeDelegate.newComponentName(tableName, String.valueOf(objRef), fieldName);
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	setComponentValue(component, componentName, componentValue, DO_NOT_ASSERT, null);
    }
  }
  
  public void componentInTable_SetValue(String layoutName, String tableName, long objRef, String fieldName, String componentValue, String priorityToCaptionProperty) throws Exception {
  	String originalLayout = null;
  	originalLayout = getLayoutName();
  	
  	if(!Utils.isStringEmpty(layoutName)) {
	  	setLayoutName(layoutName);
  	}

  	componentInTable_SetValue(tableName, objRef, fieldName, componentValue, priorityToCaptionProperty);  	
    
    setLayoutName(originalLayout);
  }

  public void componentInTable_SetValue(String layoutName, String tableName, long objRef, String fieldName, String componentValue, boolean assertOnly) throws Exception {
  	String originalLayout = null;
  	originalLayout = getLayoutName();
  	
  	if(!Utils.isStringEmpty(layoutName)) {
	  	setLayoutName(layoutName);
  	}

  	componentInTable_SetValue(tableName, objRef, fieldName, componentValue, assertOnly);  	
    
    setLayoutName(originalLayout);
  }

  public void componentInTable_AssertValue(String tableName, long objRef, String fieldName, String componentValue) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    String componentName = TableTreeDelegate.newComponentName(tableName, String.valueOf(objRef), fieldName);
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	setComponentValue(component, componentName, componentValue, ASSERT_ONLY, null);
    }
  }
  
  public void componentInTable_AssertEnabled(String tableName, long objRef, String fieldName, boolean assertEnabled) throws Exception {
    String componentName = TableTreeDelegate.newComponentName(tableName, String.valueOf(objRef), fieldName);
  	component_AssertEnabled(componentName, assertEnabled);
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
  public void componentInTable_Select(String tableName, long objRef, String fieldName) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();

    String componentName = TableTreeDelegate.newComponentName(tableName, String.valueOf(objRef), fieldName);
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	FVTableWrapperLayout tableWraperLayout = (FVTableWrapperLayout) findComponent(navigationLayout, tableName);
    	tableWraperLayout.getTableTreeDelegate().adjustFormulaLayoutForComponent(component);
    }
  }
  
  public void applyTheFormula(String tableName) throws Exception {
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

  public void checkReplaceCheckBoxForTable(String tableName, String value) throws Exception {
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
  public String component_GetValue(String componentName, String variableName) throws Exception {
  	boolean created = !getLogger().openCommand("Set "+variableName+" = valueof("+componentName+")");
  	String value = null;
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FocXMLGuiComponent component = findComponent(navigationLayout, componentName);
    if (component != null) {
    	value = component.getValueString();
      getDictionary().putXMLVariable(variableName, value);
      getLogger().addInfo("Adding the value of component " + componentName + " to the variable " + variableName + ".");
    }
    if(created) getLogger().closeNode();
    return value;
  }
  
  /**
   * Stores the value of a component in a hash map at the level of the
   * dictionary
   * 
   * @param layoutName 
   *          Specifies the layout name where the component is to be searched for.
   *          This is only useful if there is possible ambiguity where 2 components might have the same name
   *          due to XMLInclude  
   * @param componentName
   *          The name of the component.
   * @param variableName
   *          The name of the key that will contain the value in the hash map.
   */
  public String component_GetValue(String layoutName, String componentName, String variableName) throws Exception {
  	String originalLayout = getLayoutName();
  	if(!Utils.isStringEmpty(layoutName)) {
	  	setLayoutName(layoutName);
  	}
  	String value = component_GetValue(componentName, variableName);
  	if(!Utils.isStringEmpty(layoutName)) {
  		setLayoutName(originalLayout);
  	}
  	return value;
  }
  
  public void button_Click(String layoutName, String buttonName) throws Exception {
  	boolean nodeCreated = !getLogger().openCommand("Button click : "+buttonName);
  	String backup = getLayoutName();
  	setLayoutName(layoutName);
  	button_Click(buttonName);
  	setLayoutName(backup);
  	if(nodeCreated) getLogger().closeNode();
  }
  
  /**
   * Simulates a click on a button of a certain name.
   * 
   */
  public void button_Click(String buttonName) throws Exception {
  	boolean nodeCreated = !getLogger().openCommand("Button click : "+buttonName);
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVButton button = (FVButton) findComponent(navigationLayout, buttonName);
    if (button != null) {
      button.click();
    } else {
      getDictionary().getLogger().addFailure("Button " + buttonName + " not found.");
    }
    if(nodeCreated) getLogger().closeNode();
  }

  /**
   * Simulates a click on the "Add New Line" in an FVForEachLayout.
   * 
   * @param bannerLayoutName
   *          The name of the layout.
   */
  public void banner_Add(String bannerLayoutName) throws Exception {
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
   * @param idx
   *          The index of the line to delete.
   */
  public void banner_DeleteLineByIndex(String bannerLayoutName, int idx) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      ArrayList<FVBannerLayout> bannerList = forEachLayout.getBannerList(false);

      if (bannerList != null) {
        FVBannerLayout bannerLayout = bannerList.get(idx);

        if (bannerLayout != null) {
          DeleteButtonForEach deleteButton = bannerLayout.getDeleteButton();

          if (deleteButton != null) {
            getLogger().addInfo("Deleting line in layout " + bannerLayoutName + " at index " + idx + ".");
            deleteButton.click();
          } else {
            getLogger().addFailure("Could not find Delete button");
          }
        } else {
          getLogger().addFailure("Could not find banner at index " + idx + ".");
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
  public void banner_DeleteLineByReference(String bannerLayoutName, long objRef) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      ArrayList<FVBannerLayout> bannerList = forEachLayout.getBannerList(false);

      if (bannerList != null) {

        for (int i = 0; i < bannerList.size(); i++) {
          FVBannerLayout bannerLayout = bannerList.get(i);

          if (bannerLayout != null) {
            FocObject focObject = (FocObject) bannerLayout.getCentral().getFocData();
            if (focObject != null && focObject.getReferenceInt() == objRef) {
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
  public void componentInBanner_SetValueByReference(String bannerLayoutName, String objRef, String componentName, String componentValue) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      FocXMLGuiComponent component = bannerFindLineByReferenceComponentByName(bannerLayoutName, componentName, objRef);
      if (component != null) {
      	setComponentValue(component, componentName, componentValue, SET_VALUE_AND_ASSERT, null);
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
  public void componentInBanner_SetValueByIndex(String bannerLayoutName, String index, String componentName, String componentValue, boolean IsAssert) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVForEachLayout forEachLayout = (FVForEachLayout) findComponent(navigationLayout, bannerLayoutName);

    if (forEachLayout != null) {
      FocXMLGuiComponent component = bannerFindLineByIndexComponentByName(bannerLayoutName, componentName, index);
      if (component != null) {
      	setComponentValue(component, componentName, componentValue, IsAssert ? ASSERT_ONLY : SET_VALUE_AND_ASSERT, null);
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
  public void componentInBanner_GetValue(String bannerLayoutName, String componentName, String componentValue, String variableName) throws Exception {
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
                getDictionary().putXMLVariable(variableName, object.getReference().toString());
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
  protected FocXMLGuiComponent bannerFindLineByReferenceComponentByName(String bannerLayoutName, String componentName, String objRef) throws Exception {
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
  protected FocXMLGuiComponent bannerFindLineByIndexComponentByName(String bannerLayoutName, String componentName, String index) throws Exception {
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
   * @param componentName
   *          Name of the component.
   * @return the FocXMLGuiComponent
   */
  protected FocXMLGuiComponent findComponent(FocXMLLayout navigationLayout, final String componentName) throws Exception {
  	return findComponent(navigationLayout, componentName, true);
  }

  /**
   * Gets an FocXMLGuiComponent by name.
   * 
   * @param navigationLayout
   *          Name of the FocXMLLayout to look in.
   * @param componentName
   *          Name of the component.
   * @return the FocXMLGuiComponent
   */
  protected FocXMLGuiComponent findComponent(FocXMLLayout navigationLayout, final String componentName, boolean failIfNotFound) throws Exception {
  	FocXMLGuiComponent component = null;
  	if(navigationLayout == null){
  		getLogger().addFailure("NavigationLayout null in findComponent: " + componentName);
  	}else{
	  	component = (FocXMLGuiComponent) navigationLayout.getComponentByName(componentName);
	
	  	if(component == null){
	  		if(failIfNotFound){
	  			Globals.logString("Could not find component " + componentName + ".");
	  			navigationLayout.debug_PrintAllComponents();
		  		getLogger().addFailure("Could not find component " + componentName + ".");
	  		}
	  	}else{
				AbstractComponent comp = (AbstractComponent) component;
				
				FVMoreLayout moreLayout = comp.findAncestor(FVMoreLayout.class);
				
				if(moreLayout != null && !moreLayout.isExtended()){
					moreLayout.setExtended(true);
				}
	  		getLogger().addInfo("Component " + componentName + " found.");
	  	}
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
  public void variable_Set(String variableName, String variableValue) throws Exception {
    if(getDictionary() != null && variableName != null && variableValue != null){
      getDictionary().putXMLVariable(variableName, variableValue);
    }
  }
  
  /**
   * Simulates a drag and drop event.
   * @param sourceName
   * @param sourcePropertyName
   * @param targetPropertyName
   * @param targetPropertyValue
   */
  
  public void dragAndDropHighLevel(String sourceName, String sourcePropertyName, String sourcePropertyValue, String targetName, String targetPropertyName, String targetPropertyValue) throws Exception {
  	FocXMLLayout navigationLayout = getCurrentCentralPanel();
  	
  	if(navigationLayout != null){
  		
  		FVTableWrapperLayout sourceWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, sourceName);
  		ITableTree sourceTableOrTree = sourceWrapper == null ? null : sourceWrapper.getTableOrTree();
  		Object sourceItemId = null;
  		
  		if(sourceTableOrTree != null){
  			FocObject sourceObject = sourceTableOrTree.getTableTreeDelegate().selectByFocProperty(sourcePropertyName, sourcePropertyValue);
  			
  			if(sourceObject != null){
  				sourceItemId = sourceObject.getReference().getLong();
  			}else{
  				String message = "Could not find source row PROPERTY="+sourcePropertyName+" VALUE="+sourcePropertyValue;
  				getLogger().addFailure(message);
  				Globals.logString(message);
  				sourceTableOrTree.getTableTreeDelegate().debug_ListOfColumns();
  			}
  		}

  		FocXMLGuiComponent targetComponent = findComponent(navigationLayout, targetName);
  		if(targetComponent instanceof FVTableWrapperLayout) {
	  		FVTableWrapperLayout targetWrapper = (FVTableWrapperLayout) findComponent(navigationLayout, targetName);
	  		ITableTree targetTableOrTree = targetWrapper == null ? null : targetWrapper.getTableOrTree();
	  		Object targetItemId = null;
	
	  		if(targetTableOrTree != null){
	  			if(targetPropertyName != null && targetPropertyValue != null){
	  				FocObject targetObject = targetTableOrTree.getTableTreeDelegate().selectByFocProperty(targetPropertyName, targetPropertyValue);
	  				
	  				if(targetObject != null){
	  					targetItemId = targetObject.getReference().getLong();
	  				}
	  			}
	  		}
	  		
	  		dragAndDropLowLevel(sourceTableOrTree, sourceItemId, targetTableOrTree, targetItemId);
  		} else if(targetComponent instanceof DropTarget) {
  			DataBoundTransferable dbt = buildDragTransferable(sourceTableOrTree, sourceItemId);
  			DropTarget dropTarget = (DropTarget) targetComponent;
  			DropHandler dropHandler = dropTarget.getDropHandler();

				HashMap<String, Object> rawVariables = new HashMap<String, Object>();

  			TargetDetailsImpl details = new TargetDetailsImpl(rawVariables, dropTarget); 
				DragAndDropEvent dde = new DragAndDropEvent(dbt, details);
				if(dropHandler != null){
					dropHandler.drop(dde);
				}
  		}
  	}
  }

  public void dragAndDropHighLevel(String sourceName, String sourcePropertyName, String sourcePropertyValue, String targetName) throws Exception {
  	dragAndDropHighLevel(sourceName, sourcePropertyName, sourcePropertyValue, targetName, null, null);
  }
  
  private DataBoundTransferable buildDragTransferable(ITableTree source, Object sourceItemId) throws Exception {
  	DataBoundTransferable dbt = null;
  	
  	if(source != null && sourceItemId != null){

  		Map<String, Object> rawVariables = new HashMap<String, Object>();

  		Object propertyId = "1";
  		
  		rawVariables.put("propertyId", null);
  		rawVariables.put("itemId", null);

  		dbt = null;

  		if(source instanceof FVTable){
  			FVTable sourceTable = (FVTable) source;
  			dbt = sourceTable.getTransferable(rawVariables);
  		} else if (source instanceof FVTreeTable){
  			FVTreeTable sourceTree = (FVTreeTable) source;
  			dbt = sourceTree.getTransferable(rawVariables);
  		}
  		dbt.setData("propertyId", propertyId);
			dbt.setData("itemId", sourceItemId);
  	}
  	
  	return dbt;
  }
  
  private void dragAndDropLowLevel(ITableTree source, Object sourceItemId, ITableTree target, Object targetItemId) throws Exception {
  	DataBoundTransferable dbt = buildDragTransferable(source, sourceItemId);
  	
  	if(dbt != null && target != null){
  		DropHandler dropHandler = null;
  		AbstractSelectTargetDetails astd = null;
  		
  		if(target instanceof FVTable){
  			FVTable targetTable = (FVTable) target;
  			dropHandler = targetTable.getDropHandler();
  			
  			HashMap<String, Object> rawVariables = new HashMap<String, Object>();
  			rawVariables.put("itemIdOver", null);
  			astd = targetTable.translateDropTargetDetails(rawVariables);
  		} else if (target instanceof FVTreeTable){
  			FVTreeTable targetTree = (FVTreeTable) target;
  			dropHandler = targetTree.getDropHandler();
  			
  			if(targetItemId != null){
  				HashMap<String, Object> rawVariables = new HashMap<String, Object>();
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
  	} else {
  		getLogger().addFailure("Drag and drop failure.");
  	}
  }
  
  public void login() throws Exception {
    NativeButton loginButton = getMainWindow().getLoginButton();
    if (loginButton != null) {
      loginButton.click();
    } else {
      getLogger().addFailure("Login button not found.");
    }
  }
  
  public void sign() throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    if (navigationLayout != null) {
      FVValidationLayout validationLayout = navigationLayout.getValidationLayout();
      if (validationLayout != null) {
      	if(!validationLayout.isVisible_WorkflowConsole()) {
      		FVStageLayout_Button stage = validationLayout.getStageLayout(false);
      		if(stage != null && stage.isVisible()) stage.click();
      	}
      	if(validationLayout.isVisible_WorkflowConsole()) {
      		WFConsole_Form console = validationLayout.getWorkflowConsole();
      		if(console != null && console.isVisible() && console.getComponentByName("SIGN") != null && console.getComponentByName("SIGN").isVisible()) {
      			console.button_SIGN_Clicked((FVButtonClickEvent)null);
      			getLogger().addInfo("Sign button Clicked");
      		} else {
      			getLogger().addFailure("Console not found");
      		}
      	} else {
      		getLogger().addFailure("Console not visible");
      	}
      	
//      	FVStageLayout_Button stageLayout = validationLayout.getStageLayout(false);
//      	if(stageLayout != null){
//      		stageLayout.sign(null);
//      	}
      } else {
      	getLogger().addFailure("Validation Layout not found");
      }
    } else {
    	getLogger().addFailure("Navigation Layout not found");
    }
  }

  public void callTest() throws Exception {
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
  public void popupButtonClick(String firstButton, String secondButton) throws Exception {
    FocXMLLayout navigationLayout = getCurrentCentralPanel();
    FVPopupButton button = (FVPopupButton) findComponent(navigationLayout, firstButton);
    if (button != null) {
    	FVPopupContentButton contentButton = button.getContentButton(secondButton);
    	if(contentButton != null){
    		contentButton.click();
    	}else{
    		getDictionary().getLogger().addFailure("Content Button " + secondButton + " for popup button "+firstButton+" not found.");
    	}
    } else {
      getDictionary().getLogger().addFailure("Popup Button " + firstButton + " not found.");
    }
  }

  public void notification_Expect(String notificationMessage, String description, String notificationType) {
  	getDictionary().expectedNotification_Set(notificationMessage, description, notificationType);
  }
  
  public void notification_CheckOccured() throws Exception {
  	FocUnitExpectedNotification pendingNotif = getDictionary().expectedNotification_Get();
  	if(pendingNotif != null) {
  		getDictionary().getLogger().addFailure("Expected Notification Did Not Occure");
  	}
  }
  
	public int memory_FocObjectCount(String storageName, int expectedCount) throws Exception {
		return memory_FocObjectCount(Globals.getApp().getFocDescByName(storageName), expectedCount, false);
	}
	
	public int memory_FocObjectCount(String storageName, int expectedCount, boolean doNotPrintWhenEqual) throws Exception {
		return memory_FocObjectCount(Globals.getApp().getFocDescByName(storageName), expectedCount, doNotPrintWhenEqual);
	}
	
	public int memory_FocObjectCount(FocDesc focDesc, int expectedCount) throws Exception {
		return memory_FocObjectCount(focDesc, expectedCount, false);
	}
	
	public int memory_FocObjectCount(FocDesc focDesc, int expectedCount, boolean doNotPrintWhenEqual) throws Exception {
		int count = 0;
		if(focDesc != null) {
	  	ArrayList<FocObject> array = focDesc.allFocObjectArray_get();
	  	if(array != null) {
	  		count = array.size();
	  		Globals.logString(" ----- FocObjects In ARRAY "+focDesc.getStorageName()+" SIZE=" + count);
	  		if(expectedCount >= 0 && count != expectedCount) {
	  			getDictionary().getLogger().addFailure("Expected instances "+expectedCount+" found "+ count+" for "+focDesc.getStorageName());
	  		}else {
	  			if(!doNotPrintWhenEqual) {
	  				getDictionary().getLogger().addInfo("Found "+count+" instances for "+focDesc.getStorageName());
	  			}
	  		}
	  	}
		}
		return count;
	}
}
