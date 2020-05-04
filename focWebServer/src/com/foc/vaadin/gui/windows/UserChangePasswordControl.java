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
package com.foc.vaadin.gui.windows;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.util.Encryptor;
import com.foc.vaadin.gui.components.FVPasswordField;

public class UserChangePasswordControl {

	private FocUser         user     = null;
	private FVPasswordField newPass  = null;
	private FVPasswordField confPass = null;
	private FVPasswordField oldPass  = null;
	private boolean         isAdmin  = false;
	
  public UserChangePasswordControl(FocUser user, boolean isAdmin) {
  	this.user    = user;
    this.isAdmin = isAdmin;
  }
  
  public void dispose(){
  	user     = null;
  	newPass  = null;
  	confPass = null;
  	oldPass  = null;
  }
  
  public FVPasswordField getOldPasswordField(boolean create){
  	if(!isAdmin && create && oldPass == null){
  	  oldPass = new FVPasswordField(null, null);
  	  oldPass.setCaption("Old Password:");
  	  oldPass.setWidth("100%");
  	}
  	return oldPass;
  }
  
  public FVPasswordField getNewPasswordField(boolean create){
  	if(create && newPass == null){
	  	newPass  = new FVPasswordField(null, null);
	  	newPass.setCaption("New Password:");
	  	newPass.setWidth("100%");
  	}
  	return newPass;
  }
  
  public FVPasswordField getConfirmPasswordField(boolean create){
  	if(create && confPass == null){
	  	confPass = new FVPasswordField(null, null);
	  	confPass.setCaption("Confirm Password:");
	  	confPass.setWidth("100%");
  	}
  	return confPass;
  }
  
	public FocUser getUser() {
		return user;
	}

	public String executeChange(){
		String message = getNewPasswordField(false) == null ? "New Password field not found Error" : null;
		if(message == null){
			String newPassStr  = (String) getNewPasswordField(false).getValue();
	
			message = FocUser.checkPasswordPolicyAbidance(newPassStr);
			
			message = canChangePassword(); 
			if(message == null){
				Globals.logString(" = Username "+getUser().getName()+" password encrypted "+Encryptor.encrypt_MD5(String.valueOf(newPassStr)));
				newPassStr = Encryptor.encrypt_MD5(String.valueOf(newPassStr));
				getUser().setPassword(newPassStr);
				getUser().validate(true);
//				Globals.showNotification("Password Changed", "Successful", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
//				FocWebApplication.getInstanceForThread().removeWindow(UserChangePasswordControl.this);
			}
		}
		return message;
	}
	
  private String canChangePassword(){
  	String errorMessage = null;
  	
  	//If the old password is null this means the administrator is recreating the password. 
    if(oldPass != null){
      String oldPassStr = getOldPasswordField(false).getValueString();
      oldPassStr = Encryptor.encrypt_MD5(String.valueOf(oldPassStr));
      
      boolean oldPasswordMatches = false;
      if(getUser().getPassword().isEmpty()){
      	oldPasswordMatches = Encryptor.encrypt_MD5("").equals(oldPassStr);
      }else{
      	oldPasswordMatches = getUser().getPassword().equals(oldPassStr);
      }
      
      //If the password was never set it is still empty in the FocUser
      if(!oldPasswordMatches){
      	errorMessage = "Old password is incorrect!";
      }
    }
    
    if(errorMessage == null){
	    String newPassStr  = (String) getNewPasswordField(false).getValue();
	    String confPassStr = (String) getConfirmPasswordField(false).getValue();
	    
	    if(!newPassStr.equals(confPassStr)){
	    	errorMessage = "Passwords do not match!";
	    }

	    if(errorMessage == null && ConfigInfo.hasPasswordPolicy()) {
	    	errorMessage = FocUser.checkPasswordPolicyAbidance(newPassStr);
	    }
    }
    
    return errorMessage;
  }
}
