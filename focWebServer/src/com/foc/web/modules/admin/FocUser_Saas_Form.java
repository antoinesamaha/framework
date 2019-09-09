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

import java.net.URI;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.business.adrBook.Contact;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.dataDictionary.FocDataResolver_StringConstant;
import com.foc.ecomerce.EComConfiguration;
import com.foc.emailService.EMailService;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVButtonClickEvent;
import com.foc.vaadin.gui.components.FVEmailField;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.windows.UserChangePasswordControl;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractField;

@SuppressWarnings("serial")
public class FocUser_Saas_Form extends FocUser_Form {
	
	private UserChangePasswordControl control = null;

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		FocUser user = (FocUser) getFocUser();
		if(user != null){
			user.createContactIfNotExist();
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		if(control != null){
			control.dispose();
			control = null;
		}
	}

	private FVEmailField getEmailGuiField(){
		return (FVEmailField) getComponentByName("_EMAIL");
	}
	
	private AbstractField getRoleField(){
		return (AbstractField) getComponentByName("_ROLE");
	}
	
	private FVLayout getPasswordLayout(){
		return (FVLayout) getComponentByName("_PASSWORD_LAYOUT");
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		FVEmailField txtFld = getEmailGuiField();
		if(txtFld != null && txtFld.getEmailText() != null){
			txtFld.getEmailText().setRequired(true);
		}
		
		AbstractField roleFld = getRoleField();
		if(roleFld != null){
			roleFld.setRequired(true);
		}
		
		FVLayout layout = getPasswordLayout();
		if(layout != null){
			FocUser user = getFocUser();
			if(user != null && user.isCreated()){

			}
		}
//		addContextHelp();
	}
	
//	private FVTextField getNameTextField(){
//  	return (FVTextField) getComponentByName("NAME");
//  }
	
//	private void addContextHelp() {
//		ContextHelp contextHelp = new ContextHelp();
//		contextHelp.extend(FocWebApplication.getInstanceForThread());
//		contextHelp.setFollowFocus(true);
//		contextHelp.addHelpForComponent(getNameTextField(), "Test Context Help");
//	}
	
	@Override
	public boolean validateDataBeforeCommit(FVValidationLayout validationLayout) {
		FocUser user = (FocUser) getFocUser();
		
		boolean error = user == null;
		if(!error){
			Contact contact = user.getContact();
			error = contact == null;
			if(!error){
				user.setFullName(contact.getFullName());
				error = !user.isContentValid(true);
				if(!error){
					error = !contact.isContentValid(true);
					if(!error){
						error = !Utils.isEmail(contact.getEMail());
						if(error){
							Globals.showNotification("Please enter a valid email", "", IFocEnvironment.TYPE_ERROR_MESSAGE);
						}
					}
					if(!error){
						error = user.getSaasApplicationRole() == FocUserDesc.APPLICATION_ROLE_NONE;
						if(error){
							Globals.showNotification("Please specify the Role", "", IFocEnvironment.TYPE_ERROR_MESSAGE);
						}
					}
				}				
			}
		}
		
		return error;
	}
	
	public void button_RESET_PASSWORD_SAAS_Clicked(FVButtonClickEvent evt){
		resetPassword();
	}
	
	@Override
  protected void resetPassword() {
    boolean error = getValidationLayout().commit();//In case the user is still created, the commit makes sure it gets saved with a real reference before updating the Password.
    if(!error){
    	FocUser user = (FocUser) getFocData();
    	String newPassword = user.resetPassword();

    	FocNotificationEmailTemplate template = EComConfiguration.getInstance().getEmailTemplate_PasswordReset();
    	if(template != null){
	    	FocDataDictionary dataDictionary = new FocDataDictionary();
	    	dataDictionary.putParameter("NEW_PASSWORD", new FocDataResolver_StringConstant(newPassword));
	    	URI url = Page.getCurrent().getLocation();
	    	dataDictionary.putParameter("URL", new FocDataResolver_StringConstant(url.toString()));
	    	
	    	FocNotificationEmail email = EMailService.newEMail(template, dataDictionary, user);
	    	try{
	    		email.sendWithException();
	    	}catch(Exception e){
	    		Globals.showNotification("ERROR Sending Email", "Please check addresse email to: "+email.getRecipients(), IFocEnvironment.TYPE_ERROR_MESSAGE);
//	    		Globals.logException(e);
	    	}
    	}
    	
//      FocNotificationEmailTemplate template = EComConfiguration.getInstance().getEmailTemplate_PasswordReset();
//      FocDataMap focDataMap = new FocDataMap(user);
//      focDataMap.put("NEW_PASSWORD", new REsol);
//      FocNotificationEmail email = template.newEmail(get)
    }
  }
}
