package com.foc.vaadin.gui.windows;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVPasswordField;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class UserChangePasswordWindow extends Window {

	UserChangePasswordControl control = null;
	
  public UserChangePasswordWindow(FocUser user, boolean isAdmin) {
  	control = new UserChangePasswordControl(user, isAdmin);
    setWidth("300px");
    setHeight("-1px");
    setModal(true);
    setCaption("Change Password");
  }
  
  public void dispose(){
  	if(control != null){
  		control.dispose();
  		control = null;
  	}
  }
  
  public void init(){
  	FVVerticalLayout vLay = new FVVerticalLayout(null);
  	vLay.setWidth("90%");
  	setContent(vLay);
  	
  	vLay.setMargin(true);
  	vLay.setSpacing(true);
  	
  	FVPasswordField pFld = control.getOldPasswordField(true);
  	if(pFld != null) vLay.addComponent(pFld);

  	pFld = control.getNewPasswordField(true);
  	if(pFld != null) vLay.addComponent(pFld);

  	pFld = control.getConfirmPasswordField(true);
  	if(pFld != null) vLay.addComponent(pFld);
  	
  	FVHorizontalLayout hLay = new FVHorizontalLayout(null);
  	hLay.setSpacing(true);
  	vLay.addComponent(hLay);
  	
  	FVButton validButton  = new FVButton("Ok");
  	validButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String message = control.executeChange();
				if(message == null){
					Globals.showNotification("Password Changed", "Successful", FocWebEnvironment.TYPE_HUMANIZED_MESSAGE);
					FocWebApplication.getInstanceForThread().removeWindow(UserChangePasswordWindow.this);					
				}else{
					Globals.showNotification(message, "", FocWebEnvironment.TYPE_ERROR_MESSAGE);
				}
			}
		});
  	hLay.addComponent(validButton);
  	
  	FVButton cancelButton = new FVButton("Cancel");
  	cancelButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				FocWebApplication.getInstanceForThread().removeWindow(UserChangePasswordWindow.this);
			}
		});
  	hLay.addComponent(cancelButton);
  }
}
