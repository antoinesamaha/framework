package com.foc.business.notifier.actions;

import com.foc.business.notifier.FNotifTrigger;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailDesc;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.business.notifier.FocNotificationEvent;
import com.foc.desc.FocConstructor;

public class FocNotifAction_SendEmail extends FocNotifAction_Abstract {

	@Override
	public void execute(FNotifTrigger notifier, FocNotificationEvent event) {
    FocNotificationEmailTemplate template = (FocNotificationEmailTemplate) notifier.getTemplate();
    if(template != null) {
	    FocNotificationEmail email = new FocNotificationEmail(new FocConstructor(FocNotificationEmailDesc.getInstance(), null), template, event != null ?event.getEventFocData() : null);
	    email.send();
	    email.setCreated(true);
	    email.validate(true);
    }		
	}

}