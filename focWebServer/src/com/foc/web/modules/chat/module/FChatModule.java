package com.foc.web.modules.chat.module;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.admin.FocGroup;
import com.foc.admin.FocUser;
import com.foc.admin.GrpWebModuleRightsDesc;
import com.foc.menuStructure.FocMenuItem;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.web.modules.chat.FChatReceiver;

public class FChatModule extends FocWebModule {

	public static final int    VERSION_ID = 1000;
	
	public static final String MODULE_NAME = "Chat module";
	public static final String MNU_CHAT    = "MNU_CHAT";
	
	public FChatModule() {
		super(MODULE_NAME, "Chat module", "com.foc.web.modules.chat", "com.foc.web.modules.chat.gui", "Chat Module 1.0", VERSION_ID);
		addPackages("com.foc.web.modules.chat.join", "com.foc.web.modules.chat.join.gui");
	}

	@Override
	public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
  }
	
	public static boolean userHasChat() {
		return userHasChat(Globals.getApp().getUser_ForThisSession());
	}
	
	public static boolean userHasChat(FocUser user) {
		boolean hasChat = false;
		FocGroup group = null;
		if(Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null && Globals.getApp().getUser_ForThisSession().getGroup() != null){
			group = user.getGroup();
		}

		if(group != null && group.getWebModuleRights(FChatModule.MODULE_NAME) != GrpWebModuleRightsDesc.ACCESS_NONE) {
			hasChat = true;
		}
		
		return hasChat;
	}
	
	public static int unreadCount() {
		return unreadCount(Globals.getApp().getUser_ForThisSession());		
	}
	
	public static int unreadCount(FocUser user) {
		int size = 0;
		try {
			StringBuffer buff = new StringBuffer("SELECT COUNT(*) FROM \""+FChatReceiver.DBNAME+"\" WHERE \""+FChatReceiver.FIELD_Read+"\"=0 AND \""+FChatReceiver.FIELD_Receiver+"_REF\"="+user.getReferenceInt());
			ArrayList<String> result = Globals.getApp().getDataSource().command_SelectRequest(buff);
			if(result != null && result.size() == 1) {
				String str = result.get(0);
				size = Integer.valueOf(str);
			}
		}catch(Exception e) {
			Globals.logExceptionWithoutPopup(e);
		}
		return size;
	}
}

