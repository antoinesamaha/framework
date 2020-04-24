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
package com.foc.web.modules.admin;

import com.foc.ConfigInfo;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.UserSession;
import com.foc.business.workflow.WFSite;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.windows.UserChangePasswordWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class FocUser_CompanySelection_Form extends FocXMLLayout {

	private boolean companyFieldChanged = false;
  private FVButton passwordButton = null;

  public FocUser getUser() {
    return (FocUser) getFocData();
  }

  public FVButton getPasswordButton() {
    if (passwordButton == null) {
      passwordButton = (FVButton) getComponentByName("CHANGE_PASSWORD");
    }
    return passwordButton;
  }

  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
    super.init(window, xmlView, focData);
    FocUser user = (FocUser) focData;
    
    FProperty companyProperty = user.getFocProperty(FocUserDesc.FLD_CURRENT_COMPANY);
    FProperty siteProperty = user.getFocProperty(FocUserDesc.FLD_CURRENT_SITE);
    companyProperty.addListener(new FPropertyListener() {
      @Override
      public void propertyModified(FProperty property) {
      	companyFieldChanged = true;
      	
        FocUser user = (FocUser) getFocData();
        UserSession.getInstanceForThread().copyCompanyFromUser();
        
        FocXMLGuiComponent comp1 = (FocXMLGuiComponent) getComponentByName("CURRENT_SITE");
        FocXMLGuiComponent comp2 = (FocXMLGuiComponent) getComponentByName("CURRENT_TITLE");

        if(comp1 != null){
        	//We do not know why, but when we add this line we start having problems:
        	//1-When we change the Company the Site we get is sometimes still the sit of the previous company
        	//2-We save the modified company and site (Wrong site) we get in the Home Page a Title not selected!
        	//
        	//comp1.setFocData(user.getFocProperty(FocUserDesc.FLD_CURRENT_SITE));
        	//
        	//------------------------        	
        }
        if(comp2 != null){
          comp2.setFocData(user.getFocProperty(FocUserDesc.FLD_CURRENT_TITLE));
        }
      }

      @Override
      public void dispose() {
      }
    });

  //USERREFACTOR
    /*
    siteProperty.addListener(new FPropertyListener() {
      @Override
      public void propertyModified(FProperty property) {
        FocUser user = (FocUser) getFocData();
        if(user != null){
	        user.dispose_TitlesList();
	        user.setCurrentTitle(null);
	        if (user.getTitlesList().size() == 1)
	          user.setCurrentTitle((WFTitle) user.getTitlesList().getFocObject(0));
	        FocXMLGuiComponent comp = (FocXMLGuiComponent) getComponentByName("CURRENT_TITLE");
	        if(comp != null){
	        	comp.setFocData(user.getFocProperty(FocUserDesc.FLD_CURRENT_TITLE));
	        }
        }
      }

      @Override
      public void dispose() {
      }
    });
    */
  }

  @Override
  protected void afterLayoutConstruction() {
    super.afterLayoutConstruction();

    if(getPasswordButton() != null){
	    getPasswordButton().addClickListener(new ClickListener() {
	
	      @Override
	      public void buttonClick(ClickEvent event) {
	        changePassword();
	      }
	    });
    }
    
    Component comp = getComponentByName("CONTEXT_HELP_ACTIVATION");
    if(comp != null) comp.setVisible(ConfigInfo.isContextHelpAllowed());
    comp = getComponentByName("SIMULATION_ACTIVE");
    if(comp != null) comp.setVisible(ConfigInfo.isSimulationAllowed());
    
		FVObjectComboBox siteCombo = (FVObjectComboBox) getComponentByName("CURRENT_SITE");
		FocDataWrapper wrapper = siteCombo != null ? siteCombo.getListWrapper() : null;
		if(wrapper != null) {
			wrapper.addContainerFilter(new Filter() {
				
				@Override
				public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
					WFSite  site = (WFSite) item;
					FocUser user = getUser();
					return user != null ? user.hasSite(site) : null;
				}
	
				@Override
				public boolean appliesToProperty(Object propertyId) {
					return false;
				}
			});
		}
    
  }
  
  @Override
  public boolean validationCheckData(FVValidationLayout validationLayout){
    copyGuiToMemory();
		UserSession.getInstanceForThread().copyCredentialsFromUser();    
    FocWebVaadinWindow window = (FocWebVaadinWindow) getMainWindow();
    
//    if(companyFieldChanged && window != null){
//    	window.removeFocAllWindows();
////  		Globals.logString("DEBUG_SESSION_NOT_VALID FocWebVaadinWindow.buttonClick() calling Session Logout");
//  		window.dispose();
//    	getUI().getPage().setLocation( getUI().getPage().getLocation() );
//    	
//    	companyFieldChanged = false;
//    }else{
	    window.adjustCompaneyName();
	    FocXMLLayout mainLayout = (FocXMLLayout) window.getCentralPanel();
	    mainLayout.re_parseXMLAndBuildGui();
//    }
    
    return super.validationCheckData(validationLayout);
  };

  private void changePassword() {
    boolean error = getValidationLayout().commit();// In case the user is still
                                                   // created, the commit makes
                                                   // sure it gets saved with a
                                                   // real reference before
                                                   // updating the Password.
    if (!error) {
      FocUser user = (FocUser) getFocData();
      UserChangePasswordWindow loginWindow = new UserChangePasswordWindow(user, false);
      loginWindow.init();
      
      getUI().addWindow(loginWindow);
    }
  }
  
  @Override
  public void copyMemoryToGui() {
  	super.copyMemoryToGui();
  }
}
