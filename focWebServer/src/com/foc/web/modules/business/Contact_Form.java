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
package com.foc.web.modules.business;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.adrBook.Contact;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.menuBar.FVMenuBarCommand;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.modules.admin.FocUser_Form;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
public class Contact_Form extends FocXMLLayout {

	private Contact contact = null;
  private FocUser user = null;

  public Contact getContact() {
    return (Contact) getFocData();
  }

  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
    super.init(window, xmlView, focData);
  }
  
  @Override
  public boolean validationCheckData(FVValidationLayout validationLayout) {
  	copyGuiToMemory();
    Contact contact = (Contact) getFocData();
    AdrBookParty party = contact.getAdrBookParty();

    boolean error;
    if (/* party != null && */contact != null) {
      error = /* party.iFocData_validate() || */contact.iFocData_validate();
    } else
      error = false;

    if (!error) {
      // party.validate(true);
      error = super.validationCheckData(validationLayout);
    }
    return error;
  }

  @Override
  public void addMoreMenuItems(FVValidationLayout validationLayout) {
    super.addMoreMenuItems(validationLayout);

    if (validationLayout != null) {
      contact = (Contact) getFocData();
      if (contact != null && FocUser.findUser(contact) == null) {
        validationLayout.addMoreItem("Create user for contact", new FVMenuBarCommand() {

          public void menuSelected(MenuItem selectedItem) {
          	String message = null;
            copyGuiToMemory();
            if(contact != null){
              contact.validate(true);
              if(contact.getAdrBookParty() != null){
                contact.getAdrBookParty().validate(true);
              }
            }
            if(FocUser.findUser(contact) == null){
              user = FocUser.createUserForContact(contact);
              if(user != null){
              	FocUser_Form.changePasswordForUser(user);
              }
            }else{
              message = "User already exists for this account";
              Globals.showNotification(message, "", FocWebEnvironment.TYPE_WARNING_MESSAGE);
            }
          }
        });
      }else if(contact != null && FocUser.findUser(contact) != null){
        validationLayout.addMoreItem("Edit user settings", new FVMenuBarCommand(){

          public void menuSelected(MenuItem selectedItem){
            contact = (Contact) getFocData();
            INavigationWindow mainWindow = (INavigationWindow) getMainWindow();
            XMLViewKey xmlViewKey = new XMLViewKey(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CONTEXT_CONTACT, XMLViewKey.VIEW_DEFAULT);// FocWebVaadinWindow
            ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) mainWindow, xmlViewKey, FocUser.findUser(contact));
            mainWindow.changeCentralPanelContent(centralPanel, true);
          }
        });
      }
    }
  }
}
