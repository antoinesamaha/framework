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
import com.foc.IFocEnvironment;
import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.adrBook.AdrBookPartyDesc;
import com.foc.business.adrBook.Contact;
import com.foc.business.adrBook.ContactDesc;
import com.foc.business.config.BusinessConfig;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class AdrBookParty_Form extends AdrBookParty_common_Form {

	private AdrBookParty getAdrBookParty(){
		return (AdrBookParty) getFocData();
	}
	
	private boolean commitCurrent(){
		FocXMLLayout lay = this;
		FVValidationLayout vLay = lay.getValidationLayout();
		while(vLay == null && lay != null){
			lay = (FocXMLLayout) lay.getParentLayout();
			if(lay != null){
				vLay = lay.getValidationLayout();
			}
		}
		return vLay != null ? vLay.commit() : true;
	}

	@Override
	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
		commitCurrent();
		AdrBookPartyDesc.getList(FocList.NONE).reloadFromDB();
		Contact contact = (Contact) ContactDesc.getInstance().getFocList().newEmptyItem();
		contact.setAdrBookParty(getAdrBookParty());
		copyMemoryToGui();
		XMLViewKey xmlViewKey = new XMLViewKey(ContactDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM);
	  ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel((FocWebVaadinWindow) getMainWindow(), xmlViewKey, contact);
	  getMainWindow().changeCentralPanelContent(centralPanel, true);
		return contact;
	}

	@Override
	public ICentralPanel table_OpenItem(String tableName, ITableTree table, FocObject focObject, int viewContainer_Open) {
		commitCurrent();
		return super.table_OpenItem(tableName, table, focObject, viewContainer_Open);
	}
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		AdrBookParty adrBookParty = getAdrBookParty();
		if(adrBookParty != null && adrBookParty.getDefaultContact() == null){
			Contact contact = (Contact) ContactDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED).newEmptyItem();
			adrBookParty.setDefaultContact(contact);
			contact.setAdrBookParty(adrBookParty);
		}
	}
	/*
	public static boolean validationCheckData_OfAdrBook(AdrBookParty adrBookParty, FocXMLLayout layout, FVValidationLayout validationLayout){
		boolean error = false;
		BusinessConfig config = BusinessConfig.getInstance();
		if(config != null	&& config.isContactInPartyMandatory()){
			if(adrBookParty != null && adrBookParty.getDefaultContact() != null ){
				if(adrBookParty.getDefaultContact().getFirstName().isEmpty() || adrBookParty.getDefaultContact().getFamilyName().isEmpty()){
					Globals.showNotification("Contact Details Are Empty", "Please insert contact details before creating address book party", IFocEnvironment.TYPE_WARNING_MESSAGE);
					error = true;
				}else{
					if(adrBookParty != null && adrBookParty.getDefaultContact() != null){
						Contact contact = adrBookParty.getDefaultContact();
						//We are putting the null as default contact then resetting it because otherwise we get an infinite loop
						//Because when saving a FocObject FOC will look for Object Properties and if they are new will save them first.
						//Since here both FocObjets (Contact and AdrBookParty) are new, we will enter an infinite loop
//						getAdrBookParty().setDefaultContact(null);
						error = layout.super.validationCheckData(validationLayout);

					}
				}
			}
		}else{
			if(		 adrBookParty != null 
					&& adrBookParty.getDefaultContact() != null 
					&& adrBookParty.getDefaultContact().getFirstName().isEmpty() 
					&& adrBookParty.getDefaultContact().getFamilyName().isEmpty()){
				adrBookParty.getDefaultContact().setDeleted(true);
				adrBookParty.setDefaultContact(null);
				//error =  super.validationCheckData(validationLayout);
			}else if(
							adrBookParty != null 
					&& 	adrBookParty.getDefaultContact() != null
					&&  (
								(			 
									 !adrBookParty.getDefaultContact().getFirstName().isEmpty() 
							  &&  adrBookParty.getDefaultContact().getFamilyName().isEmpty()
								)
							|| 
								(   
										adrBookParty.getDefaultContact().getFirstName().isEmpty() 
								&& !adrBookParty.getDefaultContact().getFamilyName().isEmpty()
								)
						  )
						){
				error = true;
				Globals.showNotification("Contact Details Are Incomplete", "Please insert the missing contact details before creating address book party", IFocEnvironment.TYPE_WARNING_MESSAGE);
			}else{
				if(adrBookParty != null && adrBookParty.getDefaultContact() != null){
					Contact contact = adrBookParty.getDefaultContact();
					adrBookParty.setDefaultContact(null);
					error = super.validationCheckData(validationLayout);
					if(!error){
						contact.validate(true);
						adrBookParty.setDefaultContact(contact);
						adrBookParty.validate(false);
					}
				}
			}
		}
	}
	*/
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = false;
		BusinessConfig config = BusinessConfig.getInstance();
		if(config != null	&& config.isContactInPartyMandatory()){
			if(getAdrBookParty() != null && getAdrBookParty().getDefaultContact() != null ){
				if(getAdrBookParty().getDefaultContact().getFirstName().isEmpty() || getAdrBookParty().getDefaultContact().getFamilyName().isEmpty()){
					Globals.showNotification("Contact Details Are Empty", "Please insert contact details before creating address book party", IFocEnvironment.TYPE_WARNING_MESSAGE);
					error = true;
				}else{
					if(getAdrBookParty() != null && getAdrBookParty().getDefaultContact() != null){
						Contact contact = getAdrBookParty().getDefaultContact();
						//We are putting the null as default contact then resetting it because otherwise we get an infinite loop
						//Because when saving a FocObject FOC will look for Object Properties and if they are new will save them first.
						//Since here both FocObjets (Contact and AdrBookParty) are new, we will enter an infinite loop
//						getAdrBookParty().setDefaultContact(null);
						error = super.validationCheckData(validationLayout);
//						if(!error){
//							contact.validate(true);
//							getAdrBookParty().setDefaultContact(contact);
//							getAdrBookParty().validate(false);
//						}
					}
				}
			}
		}else{
			if(		 getAdrBookParty() != null 
					&& getAdrBookParty().getDefaultContact() != null 
					&& getAdrBookParty().getDefaultContact().getFirstName().isEmpty() 
					&& getAdrBookParty().getDefaultContact().getFamilyName().isEmpty()){
				getAdrBookParty().getDefaultContact().setDeleted(true);
				getAdrBookParty().setDefaultContact(null);
				error =  super.validationCheckData(validationLayout);
			}else if(
							getAdrBookParty() != null 
					&& 	getAdrBookParty().getDefaultContact() != null
					&&  (
								(			 
									 !getAdrBookParty().getDefaultContact().getFirstName().isEmpty() 
							  &&  getAdrBookParty().getDefaultContact().getFamilyName().isEmpty()
								)
							|| 
								(   
										getAdrBookParty().getDefaultContact().getFirstName().isEmpty() 
								&& !getAdrBookParty().getDefaultContact().getFamilyName().isEmpty()
								)
						  )
						){
				error = true;
				Globals.showNotification("Contact Details Are Incomplete", "Please insert the missing contact details before creating address book party", IFocEnvironment.TYPE_WARNING_MESSAGE);
			}else{
				if(getAdrBookParty() != null && getAdrBookParty().getDefaultContact() != null){
					Contact contact = getAdrBookParty().getDefaultContact();
					getAdrBookParty().setDefaultContact(null);
					error = super.validationCheckData(validationLayout);
					if(!error){
						contact.validate(true);
						getAdrBookParty().setDefaultContact(contact);
						getAdrBookParty().validate(false);
					}
				}
			}
		}
		return error;
	}
}
