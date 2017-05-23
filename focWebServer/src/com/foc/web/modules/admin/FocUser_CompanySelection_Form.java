package com.foc.web.modules.admin;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.admin.UserSession;
import com.foc.business.workflow.WFTitle;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.windows.UserChangePasswordWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

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
  }
  
  @Override
  public boolean validationCommit(FVValidationLayout validationLayout){
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
    
    return super.validationCommit(validationLayout);
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
