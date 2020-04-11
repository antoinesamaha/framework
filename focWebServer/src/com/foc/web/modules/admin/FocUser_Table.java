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
package com.foc.web.modules.admin;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.OptionDialog;
import com.foc.admin.FocGroup;
import com.foc.admin.FocUser;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class FocUser_Table extends FocXMLLayout{
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
	}

	public FVButton getButtonForDeleteUselessUsers() {
		FVButton button = null;
		
		Component comp = getComponentByName("DELETE_UNUSED_USERS");
		if(comp != null && comp instanceof FVButton) {
			button = (FVButton) comp;
		}

		return button;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();

		FVButton button = getButtonForDeleteUselessUsers();
		if(button != null) {
			FocGroup group = Globals.getApp().getGroup_ForThisSession();
			if (group == null || !group.allowDeleteUnusedUsers()) {
				button.setVisible(false);
			} else {
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						OptionDialog dialog = new OptionDialog("WARNING", "This will permanently delete all unused users") {
							
							@Override
							public boolean executeOption(String optionName) {
								
								if(optionName != null && optionName.equals("DELETE")) {
									deleteUnusedUsers();
								}
								
								return false;
							}
						};
						
						dialog.addOption("DELETE", "Delete anyway");
						dialog.addOption("CANCEL", "Cancel");
						dialog.popup();
					}
				});
			}
		}
	}

	protected void deleteUnusedUsers() {
		FocList list = getFocList();
		if (list != null) {
			ArrayList<FocUser> usersToDelete = new ArrayList<FocUser>();
			for (int i=0; i<list.size(); i++) {
				FocUser user = (FocUser) list.getFocObject(i);
				int refs = user.referenceCheck_GetNumber(null);
				if(refs == 0) {
					usersToDelete.add(user);
				}
			}
			
			for(int i=0; i<usersToDelete.size(); i++) {
				FocUser user = usersToDelete.get(i);
				user.setDeleted(true);
	//			user.validate(true);
			}
	//		list.validate(true);
			
			usersToDelete.clear();
		}
	}
}
