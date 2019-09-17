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
import com.foc.OptionDialog;
import com.foc.access.FocDataConstant;
import com.foc.access.FocDataMap;
import com.foc.admin.FocGroup;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.adrBook.AdrBookPartyDesc;
import com.foc.business.adrBook.Contact;
import com.foc.business.adrBook.ContactDesc;
import com.foc.business.config.BusinessConfig;
import com.foc.business.notifier.FocNotificationEmail;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.business.workflow.WFTitle;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FReference;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Encryptor;
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.FocWebVaadinWindow;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

@SuppressWarnings("serial")
public class AdrBookParty_Form extends FocXMLLayout{

	private AdrBookParty getAdrBookParty(){
		return (AdrBookParty) getFocData();
	}
	
	public void setFilterByPropertyForContactTable(){
		int partyRef = 0;
		FReference selectAdrBkPartyRef = getAdrBookParty() != null ? getAdrBookParty().getReference() : null;
		if(selectAdrBkPartyRef != null){
			partyRef = selectAdrBkPartyRef.getInteger();
		}
		
		FVTableWrapperLayout tableWrapperLayout = (FVTableWrapperLayout) getComponentByName("_CONTACTS");
		if(tableWrapperLayout != null){
			ITableTree  tableTree = tableWrapperLayout.getTableOrTree();
			if(tableTree != null){
				FocDataWrapper focDataWrapper = tableTree.getFocDataWrapper();
				if(focDataWrapper != null){
					focDataWrapper.addContainerFilter(new Filter(){
						public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
							boolean passes = false;
							AdrBookParty party = getAdrBookParty();
							Contact contact = (Contact) item;
							if(party != null && party.getReference() != null && party.getReference().getInteger() > 0 && contact != null && contact.getAdrBookParty() != null && contact.getAdrBookParty().equalsRef(party)){
								passes = true;
							}
							return passes;
						}

						public boolean appliesToProperty(Object propertyId) {
							return true;
						}
					});
				}
			}
		}
	}
	
	@Override
	protected void table_BeforeEndElement(String tableName, ITableTree table) {
		setFilterByPropertyForContactTable();
	};
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
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
	
	@Override
  public ColumnGenerator table_getGeneratedColumn(String tableName, final FVTableColumn tableColumn) {
  	ColumnGenerator columnGenerator = null;

  	if(tableColumn.getName().equals("SET_DEFAULT_CONTACT")){
  		columnGenerator = new ColumnGenerator() {
  			public Object generateCell(Table source, Object itemId, Object columnId) {
  				FVButton button = null;
  				long ref = (Long) itemId;
  				if(getAdrBookParty().getDefaultContact() == null || getAdrBookParty().getDefaultContact().getReferenceInt() != ref){
	  				button = new FVButton(tableColumn.getCaption());
	  				button.addClickListener(new SetDefaultContactListener(ref));
  				}
  				return button;
  			}
  		};
  	} else if (tableColumn.getName().equals("USER_MANAGEMENT")) {
  		columnGenerator = new ColumnGenerator() {
  			public Object generateCell(Table source, Object itemId, Object columnId) {
  				long ref = (Long) itemId;  				
  				Contact contact = getContactByRef(ref);
		  		if (contact != null && FocUser.findUser(contact) == null) {
	  				return new CreateUserButton(contact);
		  		} else if (contact != null && FocUser.findUser(contact) != null) {
		  			return new EditUserButton(contact);
		  		}
		  		return null;
  			}
  		};
  	}
  	return columnGenerator;
	}
	
	private Contact getContactByRef(long ref){
  	Contact contact = (Contact) ContactDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED).searchByReference(ref);
  	return contact;
  }

	private FVTableWrapperLayout getContactTableWrapperLayout() {
		return (FVTableWrapperLayout) getComponentByName("_CONTACTS");
	}

	public class SetDefaultContactListener implements ClickListener {
  	private long defaultContactRef = 0;
  	
  	public SetDefaultContactListener(long defaultContactRef){
  		this.defaultContactRef = defaultContactRef;
  	}
  	
		public void buttonClick(ClickEvent event) {
			Contact contact = getContactByRef(defaultContactRef);
			if(contact != null && getAdrBookParty() != null){
				getAdrBookParty().setDefaultContact(contact);
				copyMemoryToGui();
				refreshContactTableButton();
			}
		}

		private void refreshContactTableButton() {
			FVTableWrapperLayout contactTablelayout = getContactTableWrapperLayout();
			if(contactTablelayout!= null && contactTablelayout.getTableTreeDelegate() != null && contactTablelayout.getTableTreeDelegate().getTable() != null){
				contactTablelayout.getTableTreeDelegate().getTable().refreshRowCache();
			}
		}
  }
	
	public class CreateUserButton extends FVButton implements Button.ClickListener {
		
		private Contact contact = null;
		
		public CreateUserButton(Contact contact) {
			super("Create User");
			this.contact = contact;
			addClickListener(this);
		}
		
		public void dispose() {
			removeClickListener(this);
			super.dispose();
			contact = null;
		}

		@Override
		public void buttonClick(ClickEvent event) {
    	String message = null;
      copyGuiToMemory();
      
      if(contact != null && contact.validate(true)) {
      	if(Utils.isStringEmpty(contact.getEMail())) {
      		OptionDialog option = new OptionDialog("Warning", "This contact does not have an email. If you create a user, no email can be sent to him.") {
						@Override
						public boolean executeOption(String optionName) {
							if(optionName.equals("CREATE_USER")) {
								contact.createUserAndSendEmailNotification();
								return false;
							} else if(optionName.equals("DO_NOT_CREATE")) {
								return false;
							}
							return false;
						}
					};
					option.addOption("CREATE_USER", "Create user anyway");
					option.addOption("DO_NOT_CREATE", "Do not create user now");
					option.popup();
      	} else {
      		contact.createUserAndSendEmailNotification();
      	}
      }
		}
	}
	
	public class EditUserButton extends FVButton implements Button.ClickListener {
		
		private Contact contact = null;
		
		public EditUserButton(Contact contact) {
			super("Edit User");
			this.contact = contact;
			addClickListener(this);
		}
		
		public void dispose() {
			super.dispose();
			contact = null;
		}

		@Override
		public void buttonClick(ClickEvent event) {
      copyGuiToMemory();
      
      if(contact != null){
        INavigationWindow mainWindow = (INavigationWindow) getMainWindow();
        XMLViewKey xmlViewKey = new XMLViewKey(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CONTEXT_CONTACT, XMLViewKey.VIEW_DEFAULT);
        FocXMLLayout centralPanel = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) mainWindow, xmlViewKey, FocUser.findUser(contact));
        centralPanel.popupInDialog();
//        mainWindow.changeCentralPanelContent(centralPanel, true);
      }
		}
	}
}
