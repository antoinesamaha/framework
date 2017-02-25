package com.foc.web.modules.business;

import com.foc.access.FocDataMap;
import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.adrBook.AdrBookPartyDesc;
import com.foc.business.adrBook.Contact;
import com.foc.business.adrBook.ContactDesc;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class Contact_NewBusinessCard_Form extends FocXMLLayout {

  @Override
  public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
    FocList contactList = ContactDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
    Contact contact = (Contact) contactList.newEmptyItem();

    FocList adrBookPartyList  = AdrBookPartyDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
    AdrBookParty adrBookParty = (AdrBookParty) adrBookPartyList.newEmptyItem();

    FocDataMap focDataMap = new FocDataMap();
    focDataMap.put("NEW_CONTACT", contact);
    focDataMap.put("NEW_COMPANY", adrBookParty);
    focData = focDataMap;
    super.init(window, xmlView, focData);
  }

  @Override
  public boolean validationCommit(FVValidationLayout validationLayout) {
  	boolean error = false;
  	
  	copyGuiToMemory();

  	FocDataMap   map = (FocDataMap) getFocData();
  	Contact      contact      = (Contact) map.get("NEW_CONTACT");
  	AdrBookParty adrBookParty = (AdrBookParty) map.get("NEW_COMPANY");
  	
  	if(contact != null && adrBookParty != null){
  		adrBookParty.code_resetIfCreated();
  		error = !contact.validate(true);
  		if(!error){
	  		FocList adrBookPartyList = AdrBookPartyDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
	  		if(adrBookPartyList != null){
	  			AdrBookParty found = (AdrBookParty) adrBookPartyList.searchByPropertyStringValue(AdrBookPartyDesc.FLD_NAME, adrBookParty.getName());
	  			if(found != null && found.equalsRef(adrBookParty)){
	  				adrBookParty.cancel();
	  				adrBookParty = found;
	  			}else{
	  				adrBookParty.setDeliveryAddress(adrBookParty.getInvoiceAddress());
	  	  		error = !adrBookParty.validate(true);
	  			}
	  		}
	  		if(!error){
	  			contact.setAdrBookParty(adrBookParty);
	  			error = !contact.validate(true);
	  			if(adrBookParty.getDefaultContact() == null){
		  			adrBookParty.setDefaultContact(contact);
		  			adrBookParty.validate(true);
	  			}
	  		}
  		}
  	}

    if (!error) {
      // party.validate(true);
      error = super.validationCommit(validationLayout);
    }
    return error;
  }

}
