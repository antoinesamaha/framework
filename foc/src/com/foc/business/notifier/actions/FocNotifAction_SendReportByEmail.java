package com.foc.business.notifier.actions;

import com.foc.business.notifier.FNotifTrigger;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailDesc;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.printing.PrnContext;
import com.foc.business.printing.PrnLayout;
import com.foc.business.printing.ReportFactory;
import com.foc.business.printing.gui.PrintingAction;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class FocNotifAction_SendReportByEmail extends FocNotifAction_Abstract {

	@Override
	public void execute(FNotifTrigger notifier, FocNotificationEvent event) {
    FocNotificationEmailTemplate template = notifier != null ? (FocNotificationEmailTemplate) notifier.getTemplate() : null;
    if(template != null) {
    	notifier.copyReportConfig_Ref2Object();
    	FocObject reportConfig = notifier.getReportConfiguration();
    	if(reportConfig != null && reportConfig.getThisFocDesc() != null) {
	    	PrnContext context   = ReportFactory.getInstance().findContext(reportConfig.getThisFocDesc().getReportContext());
	    	PrnLayout  prnLayout = context != null ? context.getLayoutByName(notifier.getReportLayout()) : null;
	
				PrintingAction printingAction = reportConfig.getThisFocDesc().newPrintingAction();
				printingAction.setObjectToPrint(reportConfig);
				printingAction.setLaunchedAutomatically(true);
				printingAction.initLauncher();
				
		    FocNotificationEmail email = new FocNotificationEmail(new FocConstructor(FocNotificationEmailDesc.getInstance(), null));
				email.init(template, printingAction.getObjectToPrint());
				prnLayout.attachToEmail(email, printingAction, false);
				email.fill();
		    email.send();
		    email.setCreated(true);
		    email.validate(true);
		    printingAction.setLaunchedAutomatically(false);
    	}
    }		
	}

}