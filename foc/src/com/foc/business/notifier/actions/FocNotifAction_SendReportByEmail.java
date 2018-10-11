package com.foc.business.notifier.actions;

import com.foc.Globals;
import com.foc.business.notifier.FNotifTrigReport;
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
import com.foc.list.FocList;
import com.foc.util.Utils;

public class FocNotifAction_SendReportByEmail extends FocNotifAction_Abstract {

	@Override
	public void execute(FNotifTrigger notifier, FocNotificationEvent event) {
    FocNotificationEmailTemplate template = notifier != null ? (FocNotificationEmailTemplate) notifier.getTemplate() : null;
    if(template != null) {
    	try {
    		boolean emailInitialised = false;
		    FocNotificationEmail email = new FocNotificationEmail(new FocConstructor(FocNotificationEmailDesc.getInstance(), null));
		    
		    FocList list = notifier.getReportList();
		    for(int i=0; i<list.size(); i++) {
		    	FNotifTrigReport trigReport = (FNotifTrigReport) list.getFocObject(i);

		    	trigReport.copyReportConfig_Ref2Object();
		    	FocObject reportConfig = trigReport.getReportConfiguration();
		    	if(reportConfig != null && reportConfig.getThisFocDesc() != null) {
			    	PrnContext context   = ReportFactory.getInstance().findContext(reportConfig.getThisFocDesc().getReportContext());
			    	PrnLayout  prnLayout = context != null ? context.getLayoutByName(trigReport.getReportLayout()) : null;
			
						PrintingAction printingAction = reportConfig.getThisFocDesc().newPrintingAction();
						printingAction.setObjectToPrint(reportConfig);
						printingAction.setLaunchedAutomatically(true);
						printingAction.initLauncher();
						
						if(!emailInitialised) {
							email.init(template, printingAction.getObjectToPrint());
							emailInitialised = true;
						}
						
						String attachmentName = null;
						if(!Utils.isStringEmpty(reportConfig.getName())) {
							attachmentName = prnLayout.getFileNameWithoutExtensionIfExists()+"_"+reportConfig.getName();
						}
						prnLayout.attachToEmail(email, printingAction, false, attachmentName);
				    printingAction.setLaunchedAutomatically(false);
		    	}
		    }
	    	
				if(!emailInitialised) {
					email.init(template, null);
					emailInitialised = true;
				}

				email.fill();
		    email.send();
		    email.setCreated(true);
		    email.validate(true);	    	
    	}catch(Exception e) {
    		Globals.logException(e);
    	}
    }		
	}

}