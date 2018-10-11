package com.foc.business.notifier;

import com.foc.admin.FocVersion;
import com.foc.desc.FocModule;
import com.foc.list.FocList;
import com.foc.menu.FMenuList;

public class NotifierModule extends FocModule {
	
	public static final String MODULE_NAME = "NOTIFIER";
	public static final int VERSION = 1002;
	
	public static final int VERSION_MULTI_REPORT_IN_TRIGGER = 1002;
	
	private boolean copyMonoReportToSlaveTable = false;
	
	public NotifierModule(){
	  FocVersion.addVersion(MODULE_NAME, "notfier 1.0", VERSION);
	}
	
	public void dispose(){
		
	}
	
	@Override
	public void declareFocObjectsOnce() {
//		declareFocDescClass(FNotifTriggerDesc.class);
		declareFocDescClass(FocNotificationEmailTemplateDesc.class);
		declareFocDescClass(FocNotificationEmailDesc.class);
		declareFocDescClass(FocPageLinkDesc.class);
		declareFocDescClass(DocMsgContentDesc.class);
		declareFocDescClass(DocMsgDesc.class);
		
		scanModelPackage("com.foc.business.notifier");
	}

	public void addApplicationMenu(FMenuList menuList) {
	}

	public void addConfigurationMenu(FMenuList menuList) {
	}

	public void afterApplicationEntry() {
	}

	public void afterApplicationLogin() {
	}

	public void beforeAdaptDataModel() {
		super.beforeAdaptDataModel();
		copyMonoReportToSlaveTable = false;
		FocVersion version = FocVersion.getDBVersionForModule(MODULE_NAME);
		if (version != null && version.getId() < VERSION_MULTI_REPORT_IN_TRIGGER) {
			copyMonoReportToSlaveTable = true;
		}
	}

	public void afterAdaptDataModel() {
		if(copyMonoReportToSlaveTable) {
			FocList list = FNotifTrigger.getFocDesc().getFocList();
			list.loadIfNotLoadedFromDB();
			for(int i=0; i<list.size(); i++) {
				FNotifTrigger trigger = (FNotifTrigger) list.getFocObject(i);
				if(trigger.getAction() == FNotifTrigger.ACTION_EXECUTE_REPORT) {
					FocList repList = trigger.getReportList();
					FNotifTrigReport report = (FNotifTrigReport) repList.newEmptyItem();
					report.setReportTableName(trigger.getReportTableName());
					report.setReportReference(trigger.getReportReference());
					report.setReportLayout(trigger.getReportLayout());
					repList.add(report);
					report.validate(true);
				}
				trigger.validate(true);
			}
		}
	}

	private static NotifierModule notifierModule = null;
	public static NotifierModule getInstance(){
		if(notifierModule == null){
			notifierModule = new NotifierModule();
		}
		return notifierModule;
	}
}
