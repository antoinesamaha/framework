package com.foc.modules.link;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailConst;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class Email_Form extends FocXMLLayout {
  
  public FocNotificationEmail getEmail(){
    return (FocNotificationEmail) getFocData();
  }
  
  public void setEmail(FocNotificationEmail email){
    setFocData(email);
  }
  
  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
    super.init(window, xmlView, focData);
    FocNotificationEmail email = getEmail();
    FProperty emailProp = email.getFocProperty(FocNotificationEmailConst.FLD_TEMPLATE_NAME);
    emailProp.addListener(new FPropertyListener() {
      @Override
      public void propertyModified(FProperty property) {
        FocNotificationEmail modifiedEmail = (FocNotificationEmail) property.getFocObject();
        modifiedEmail.adjustTemplate();
        copyMemoryToGui();
      }
      
      @Override
      public void dispose() {
      }
    });
  }

  protected void afterLayoutConstruction() {
  	/*
    FVButton sendMailButton = (FVButton) getComponentByName("SEND_EMAIL");
    if(sendMailButton != null){
	    sendMailButton.addClickListener(new ClickListener() {
	
	      @Override
	      public void buttonClick(ClickEvent event) {
	        sendEmail();
	        goBack(null);
	      }
	
	    });
    }
    FVButton cancelButton = (FVButton) getComponentByName("CANCEL_SEND");
    if(cancelButton != null){
	    cancelButton.addClickListener(new ClickListener() {
	
	      @Override
	      public void buttonClick(ClickEvent event) {
	        goBack(null);
	      }
	
	    });
    }
    FVButton outlookButton = (FVButton) getComponentByName("OUTLOOK_OPEN");
    if(outlookButton != null){
    	outlookButton.addClickListener(new ClickListener() {
	      @Override
	      public void buttonClick(ClickEvent event) {
	      	JavaScript.getCurrent().execute("window.location = 'mailto:"+getEmail().getRecipients()+"'");
	      }
	    });
	  }
	  */
  }

	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = super.validationCheckData(validationLayout);
		if(!error){
			sendEmail();
		}
		return error;
	}
  
  private void sendEmail() {
    copyGuiToMemory();
    FocNotificationEmail email = (FocNotificationEmail) getFocData();

    if (email != null) {
      email.generateMimeMessage();
      email.setCreated(true);
      email.validate(true);
      email.send();
      Globals.showNotification("Email sent to: " + email.getRecipients() + ".", "", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
    }
  }

}
