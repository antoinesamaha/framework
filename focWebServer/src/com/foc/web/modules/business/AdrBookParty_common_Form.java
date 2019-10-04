/******************************************************************************* Copyright 2016 Antoine Nicolas SAMAHA
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. ******************************************************************************/
package com.foc.web.modules.business;

import com.foc.OptionDialog;
import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.adrBook.Contact;
import com.foc.business.adrBook.ContactDesc;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.list.FocList;
import com.foc.property.FReference;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.modules.admin.AdminWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

@SuppressWarnings("serial")
public class AdrBookParty_common_Form extends FocXMLLayout {

	public AdrBookParty getAdrBookParty() {
		return (AdrBookParty) getFocObject();
	}

	protected Contact getContactByRef(long ref) {
		Contact contact = (Contact) ContactDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED).searchByReference(ref);
		return contact;
	}

	public FVTableWrapperLayout getContactTableWrapper() {
		return (FVTableWrapperLayout) getComponentByName("_CONTACTS");
	}

	protected FVTableWrapperLayout getContactTableWrapperLayout() {
		return (FVTableWrapperLayout) getComponentByName("_CONTACTS");
	}

	private void refreshContactTableButton() {
		FVTableWrapperLayout contactTablelayout = getContactTableWrapperLayout();
		if (contactTablelayout != null && contactTablelayout.getTableTreeDelegate() != null && contactTablelayout.getTableTreeDelegate().getTable() != null) {
			contactTablelayout.getTableTreeDelegate().getTable().refreshRowCache();
		}
	}

	@Override
	protected void table_BeforeEndElement(String tableName, ITableTree table) {
		setFilterByPropertyForContactTable();
	};

	@SuppressWarnings("unused")
	public void setFilterByPropertyForContactTable() {
		int partyRef = 0;
		FReference selectAdrBkPartyRef = getAdrBookParty() != null ? getAdrBookParty().getReference() : null;
		if (selectAdrBkPartyRef != null) {
			partyRef = selectAdrBkPartyRef.getInteger();
		}

		FVTableWrapperLayout tableWrapperLayout = getContactTableWrapper();
		if (tableWrapperLayout != null) {
			ITableTree tableTree = tableWrapperLayout.getTableOrTree();
			if (tableTree != null) {
				FocDataWrapper focDataWrapper = tableTree.getFocDataWrapper();
				if (focDataWrapper != null) {
					focDataWrapper.addContainerFilter(new Filter() {
						public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
							boolean passes = false;
							AdrBookParty party = getAdrBookParty();
							Contact contact = (Contact) item;
							if (party != null && party.getReference() != null && party.getReference().getInteger() > 0 && contact != null && contact.getAdrBookParty() != null && contact.getAdrBookParty().equalsRef(party)) {
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
	public ColumnGenerator table_getGeneratedColumn(String tableName, final FVTableColumn tableColumn) {
		ColumnGenerator columnGenerator = null;
		if (tableColumn.getName().equals("SET_DEFAULT_CONTACT")) {
			columnGenerator = new ColumnGenerator() {
				public Object generateCell(Table source, Object itemId, Object columnId) {
					FVButton button = null;
					long ref = (Long) itemId;
					if (getAdrBookParty().getDefaultContact() == null || getAdrBookParty().getDefaultContact().getReferenceInt() != ref) {
						button = new FVButton(tableColumn.getCaption());
						button.addClickListener(new SetDefaultContactListener(ref));
						String compName = TableTreeDelegate.newComponentName(tableName, String.valueOf(ref), "SET_DEFAULT_CONTACT");
						putComponent(compName, button);
					}
					return button;
				}
			};
		} else if (tableColumn.getName().equals("USER_MANAGEMENT")) {
			columnGenerator = new ColumnGenerator() {
				public Object generateCell(Table source, Object itemId, Object columnId) {
					long ref = (Long) itemId;
					Contact contact = getContactByRef(ref);
					FVButton button = null;
					if (contact != null && FocUser.findUser(contact) == null) {
						button = new CreateUserButton(contact);
						String compName = TableTreeDelegate.newComponentName(tableName, String.valueOf(ref), "CREATE_USER");
						putComponent(compName, button);
					} else if (contact != null && FocUser.findUser(contact) != null) {
						button = new EditUserButton(contact);
						String compName = TableTreeDelegate.newComponentName(tableName, String.valueOf(ref), "EDIT_USER");
						putComponent(compName, button);
					}
					return button;
				}
			};
		}
		return columnGenerator;
	}

	public class SetDefaultContactListener implements ClickListener {
		private long defaultContactRef = 0;

		public SetDefaultContactListener(long defaultContactRef) {
			this.defaultContactRef = defaultContactRef;
		}

		public void buttonClick(ClickEvent event) {
			Contact contact = getContactByRef(defaultContactRef);
			if (contact != null && getAdrBookParty() != null) {
				getAdrBookParty().setDefaultContact(contact);
				copyMemoryToGui();
				refreshContactTableButton();
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
			copyGuiToMemory();
			if (contact != null && contact.validate(true)) {
				if (Utils.isStringEmpty(contact.getEMail())) {
					OptionDialog option = new OptionDialog("Warning", "This contact does not have an email. If you create a user, no email can be sent to him.") {
						@Override
						public boolean executeOption(String optionName) {
							if (optionName.equals("CREATE_USER")) {
								contact.createUserAndSendEmailNotification();
								copyMemoryToGui();
								refreshContactTableButton();
								return false;
							} else if (optionName.equals("DO_NOT_CREATE")) {
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
					copyMemoryToGui();
					refreshContactTableButton();
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
			if (contact != null) {
				INavigationWindow mainWindow = (INavigationWindow) getMainWindow();
				XMLViewKey xmlViewKey = new XMLViewKey(FocUserDesc.getInstance().getStorageName(), XMLViewKey.TYPE_FORM, AdminWebModule.CONTEXT_CONTACT, XMLViewKey.VIEW_DEFAULT);
				FocXMLLayout centralPanel = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel((FocCentralPanel) mainWindow, xmlViewKey, FocUser.findUser(contact));
				centralPanel.popupInDialog();
			}
		}
	}
}
