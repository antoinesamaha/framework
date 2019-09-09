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

import com.foc.admin.FocUser;
import com.foc.business.adrBook.Contact;
import com.foc.business.company.UserCompanyRights;
import com.foc.desc.FocObject;
import com.foc.event.FocEvent;
import com.foc.event.FocListener;
import com.foc.list.FocList;
import com.foc.saas.manager.SaaSApplicationAdaptor;
import com.foc.saas.manager.SaaSConfig;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.windows.UserChangePasswordWindow;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.JavaScript;

@SuppressWarnings("serial")
public class FocUser_Form extends FocXMLLayout {
	
	private FocListener userCompanyRightsListener     = null;
	
	protected FocUser getFocUser(){
		return (FocUser) getFocObject();
	}
	
  @Override
  public void init(com.foc.web.gui.INavigationWindow window, com.foc.web.server.xmlViewDictionary.XMLView xmlView, com.foc.shared.dataStore.IFocData focData) {
    super.init(window, xmlView, focData);
    FocList userCompanyList = getFocUser().getCompanyRightsList();
    userCompanyRightsListener = new FocListener(){

      @Override
      public void focActionPerformed(FocEvent evt) {
        if((evt.getID() == FocEvent.ID_ITEM_ADD || evt.getID() == FocEvent.ID_ITEM_MODIFY || evt.getID() == FocEvent.ID_ITEM_REMOVE) && evt.getEventSubject() instanceof FocObject){
          UserCompanyRights userCompanyRights = (UserCompanyRights) evt.getEventSubject();
          if(userCompanyRights != null){
            FocUser user = userCompanyRights.getUser();
            if(user != null){
              user.clearCompanyList();
            }
          }
        }
      }

      @Override
      public void dispose() {
      } 
    };
    
    userCompanyList.addFocListener(userCompanyRightsListener);
  };

	
	@Override
	public void showValidationLayout(boolean showBackButton) {
		super.showValidationLayout(showBackButton);
		if(showBackButton && getValidationLayout() != null){
			/*
	  	Button emailButton = (Button) getValidationLayout().getEmailButton(true);
	  	if(emailButton != null){
	  		emailButton.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						FocUser focUser = getFocUser();
						if(focUser != null && focUser.getContact() != null){
							String email = focUser.getContact().getEMail();
							if(email != null && !email.isEmpty()){
								String name = focUser.getContact().getFullName(); 
								String javaScript = "window.location = 'mailto:" + email + "?body=Dear "+name+",%20%0d%0a%20%0d%0aPlease find here under your username and password to track online your deliveries:%20%0d%0aUsername:"+focUser.getName()+"%20%0d%0aPassword:"+focUser.getPassword()+"%20%0d%0a%20%0d%0aRegards,'";
			          JavaScript.getCurrent().execute(javaScript);
							}
						}
					}
				});
	  	}
	  	*/
  	}
	}
	
	@Override
  protected void afterLayoutConstruction(){
		super.afterLayoutConstruction();
  	FVButton changePasswordBtn = (FVButton) getComponentByName("CHANGE_PASSWORD");
    
  	if(changePasswordBtn != null){
	  	changePasswordBtn.addClickListener(new ClickListener() {
	      @Override
	      public void buttonClick(ClickEvent event) {
	        changePassword();
	      }
	    });
  	}
  	
  	FVButton resetPasswordBtn = (FVButton) getComponentByName("RESET_PASSWORD");
  	if(resetPasswordBtn != null){
  		resetPasswordBtn.addClickListener(new ClickListener() {
	      @Override
	      public void buttonClick(ClickEvent event) {
	        resetPassword();	        
	      }
	    });
  	}  	
  }
  
  protected void resetPassword() {
    boolean error = getValidationLayout().commit();//In case the user is still created, the commit makes sure it gets saved with a real reference before updating the Password.
    if(!error){
    	FocUser user = (FocUser) getFocData();
    	changePasswordForUser(user);
    }
  }

  public static void changePasswordForUser(FocUser focUser){
  	String newPassStr = focUser.resetPassword(); 

		if(focUser != null && focUser.getContact() != null){
			String email = focUser.getContact().getEMail();
			if(email != null && !email.isEmpty()){
				String name = focUser.getContact().getFullName();
				String ENT = "%20%0d%0a";
				String javaScript = "window.location = 'mailto:" + email + "?body=Dear "+name+","+ENT+ENT;
				javaScript += "You can have real time follow up of your goods delivery on our online website: "+ENT+Page.getCurrent().getLocation()+ENT+ENT;
				javaScript += "Username: "+focUser.getName()+ENT;
				javaScript += "Password: "+newPassStr+ENT+ENT;
				javaScript += "Regards,'";
        JavaScript.getCurrent().execute(javaScript);
			}
		}
  }
  
  private void changePassword() {
    boolean error = getValidationLayout().commit();//In case the user is still created, the commit makes sure it gets saved with a real reference before updating the Password.
    if(!error){
    	FocUser user = (FocUser) getFocData();
    	UserChangePasswordWindow loginWindow = new UserChangePasswordWindow(user, true);
    	loginWindow.init();
      FocWebApplication.getInstanceForThread().addWindow(loginWindow);
    }
  }
  
  protected Contact getRelatedContact(){
  	return getFocUser() != null ? getFocUser().getContact() : null;
  }
  
  protected void setRelatedContact(Contact contact){
  	if(getFocUser() != null){
  		getFocUser().setContact(contact);
  	}
  }
  
  @Override
  public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
  	super.validationAfter(validationLayout, commited);
  	if(commited){
  		if(getRelatedContact() != null){
  			getRelatedContact().validate(true);
  		}
    	SaaSApplicationAdaptor adaptor = SaaSConfig.getInstance() != null ? SaaSConfig.getInstance().getSaasApplicationAdaptor() : null;
    	if(adaptor != null){
    		adaptor.adaptUserRights(getFocUser());
    	}
  	}
  }
}
