package com.foc.business.notifier;

import com.foc.admin.FocVersion;
import com.foc.desc.FocModule;
import com.foc.menu.FMenuList;

public class NotifierModule extends FocModule {
	
	public NotifierModule(){
	  FocVersion.addVersion("NOTIFIER", "notfier 1.0", 1000);
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

	public void afterAdaptDataModel() {
	}

	public void afterApplicationEntry() {
	}

	public void afterApplicationLogin() {
	}

	public void beforeAdaptDataModel() {
	}
	
	private static NotifierModule notifierModule = null;
	public static NotifierModule getInstance(){
		if(notifierModule == null){
			notifierModule = new NotifierModule();
		}
		return notifierModule;
	}
}
