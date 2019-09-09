/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.web.modules.notifier.module;

import com.foc.business.notifier.BroadcastMessage;
import com.foc.business.notifier.EMailAccount;
import com.foc.business.notifier.FNotifTriggerDesc;
import com.foc.business.notifier.FocNotificationEmailDesc;
import com.foc.business.notifier.FocNotificationEmailTemplateDesc;
import com.foc.desc.FocDesc;
import com.foc.list.FocList;
import com.foc.menuStructure.FocMenuItem;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.menuTree.FocMenuItemAbstractAction_WithAddCommand;

public class NotifierWebModule extends FocWebModule {

	public static final String MODULE_NAME = "Notifier Module";
	public static final String MNU_CHAT    = "MNU_NOTIFIER";
	
	public NotifierWebModule() {
		super(MODULE_NAME, "Noifier module", null, "com.foc.web.modules.notifier.gui");
//		addPackages("siren.isf.fenix.chat.join", "siren.isf.fenix.chat.join.gui");
	}

	@Override
	public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
		FocMenuItem mainMenu = menuTree.pushRootMenu("MNU_NOTIFIER_MAIN_MENU", "Notification Module");
		FocMenuItem menuItem = null;

		menuItem = mainMenu.pushMenu("MNU_NOTIF_EMAIL_ACCOUNTS", "My eMail Accounts");
		menuItem.setMenuAction(new FocMenuItemAbstractAction_WithAddCommand(menuItem, null) {
			@Override
			public FocList getFocList() {
				FocDesc focDesc = EMailAccount.getFocDesc();
				return focDesc.getFocList(FocList.LOAD_IF_NEEDED);
			}
		});

		menuItem = mainMenu.pushMenu("MNU_NOTIF_BROADCAST_MESSAGE", "Broadcast Messages");
		menuItem.setMenuAction(new FocMenuItemAbstractAction_WithAddCommand(menuItem, null) {
			@Override
			public FocList getFocList() {
				FocDesc focDesc = BroadcastMessage.getFocDesc();
				return focDesc.getFocList(FocList.LOAD_IF_NEEDED);
			}
		});
		
		menuItem = mainMenu.pushMenu("MNU_NOTIF_TRIGERS", "Notification Trigers");
		menuItem.setMenuAction(new FocMenuItemAbstractAction_WithAddCommand(menuItem, null) {
			@Override
			public FocList getFocList() {
				FocDesc focDesc = FNotifTriggerDesc.getInstance();
				return focDesc.getFocList(FocList.LOAD_IF_NEEDED);
			}
		});

		menuItem = mainMenu.pushMenu("MNU_NOTIF_TEMPLATE", "Notification Template");
		menuItem.setMenuAction(new FocMenuItemAbstractAction_WithAddCommand(menuItem, null) {
			@Override
			public FocList getFocList() {
				FocDesc focDesc = FocNotificationEmailTemplateDesc.getInstance();
				return focDesc.getFocList(FocList.LOAD_IF_NEEDED);
			}
		});

		menuItem = mainMenu.pushMenu("MNU_EMAIL", "Email list");
		menuItem.setMenuAction(new FocMenuItemAbstractAction_WithAddCommand(menuItem, null) {
			@Override
			public FocList getFocList() {
				FocDesc focDesc = FocNotificationEmailDesc.getInstance();
				return focDesc.getFocList(FocList.LOAD_IF_NEEDED);
			}
		});
		
  }
	
}

