package com.foc.emailService;

import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailDesc;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.desc.FocConstructor;
import com.foc.shared.dataStore.IFocData;

public class EMailService {

	public static FocNotificationEmail newEMail(
																FocNotificationEmailTemplate template,
																FocDataDictionary dictionary,
																IFocData focData
																){
		FocConstructor       contr = new FocConstructor(FocNotificationEmailDesc.getInstance(), null);
		FocNotificationEmail email = new FocNotificationEmail(contr);
		
		email.setTemplate(template);
		
		//Filling the Email Fields from the template and the dictionary and FocData
    if (template != null && dictionary != null) {
    	String subject = dictionary.resolveExpression(focData, template.getSubject(), false);
      email.setSubject(subject);
      
      String text = dictionary.resolveExpression(focData, template.getText(), false);
      email.setText(text);
      
      String recipients = dictionary.resolveExpression(focData, template.getRecipients(), false);
      email.setRecipients(recipients);
      
      String bcc = dictionary.resolveExpression(focData, template.getBcc(), false);
      email.setBcc(bcc);
      
      email.setSender("01barmaja@01barmaja.com");
    } else {
    	email.setSubject("");
    	email.setText("");
    	email.setSender("01barmaja@01barmaja.com");
    }
    
    return email;
	}

}
