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
package com.foc.vaadin.gui.layouts.link;

import java.io.ByteArrayOutputStream;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.admin.FocGroup;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.link.FocLinkOutBox;
import com.foc.link.FocLinkOutBoxDesc;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.link.LinkWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class FVLinkLayout extends FVHorizontalLayout {
	private Button   postButton = null;
	private IFocData focData    = null;
	
	
	public FVLinkLayout(Attributes attributes) {
		super(attributes);
		
		if(postButton == null){
			postButton = new Button("Post");
			postButton.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {

					if(getFocData() != null){

						FocGroup group = (Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null) ? Globals.getApp().getUser_ForThisSession().getGroup() : null;
						if(group != null && group.getLinkOutRights() != null){

							if(getFocData() instanceof FocObject){
								FocObject obj = (FocObject) getFocData();
								//							FocLink_Out_Log outLog = (FocLink_Out_Log) (FocLink_Out_LogDesc.getList() == null ? null : FocLink_Out_LogDesc.getList().newEmptyItem());
								FocConstructor constr = new FocConstructor(FocLinkOutBoxDesc.getInstance(), null);
								FocLinkOutBox outLog = (FocLinkOutBox) constr.newItem(); 
								outLog.setCreated(true);

								outLog.setFromUser(Globals.getApp().getUser_ForThisSession());
								outLog.setStorage(obj.getThisFocDesc().getStorageName());
								outLog.setTransactionType(obj.getThisFocDesc().focDesc_getTransactionType());
								ByteArrayOutputStream baos = new ByteArrayOutputStream();

								obj.writeXMLFile(baos, true, group.getLinkOutRights());
								try{
									outLog.setXmlMessage(baos.toString("UTF-8"));
								}catch(Exception e){
									Globals.logException(e);
								}

								INavigationWindow mainWindow = (INavigationWindow) FocWebApplication.getInstanceForThread().getNavigationWindow();

								if(mainWindow != null){
									XMLViewKey key = new XMLViewKey(FocLinkOutBoxDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, LinkWebModule.CTXT_POST, XMLViewKey.VIEW_DEFAULT);
									ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, outLog);
									mainWindow.changeCentralPanelContent(centralPanel, true);
								}
							}
						}
						else{
							Globals.showNotification("You do not have the rights to perform this action.", "Please contact your administrator.", IFocEnvironment.TYPE_ERROR_MESSAGE);
						}
					}

				}
			});
		}
		
		addComponent(postButton);
	}
	
	public IFocData getFocData(){
		return focData;
	}
	
	public void setFocData(IFocData data){
		this.focData = data;
	}

}
