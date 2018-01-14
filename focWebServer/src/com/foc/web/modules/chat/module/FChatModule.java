package com.foc.web.modules.chat.module;

import com.foc.menuStructure.FocMenuItem;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;

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
}

