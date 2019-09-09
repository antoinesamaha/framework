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
package com.foc.web.modules.workflow.gui;

import com.foc.business.adrBook.AdrBookParty;
import com.foc.business.adrBook.AdrBookPartyDesc;
import com.foc.business.company.Company;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class COMPANY_Form extends FocXMLLayout {
	
	private FPropertyListener nameListener = null;
	private FPropertyListener descriptionListener = null;
	
	@Override
	public void dispose() {
		Company company = getCompany();
		if(company != null) {
			AdrBookParty party = company.getAdrBookParty();
			FProperty codeProp = party != null ? party.getFocProperty(AdrBookPartyDesc.FLD_CODE) : null;
			FProperty nameProp = party != null ? party.getFocProperty(AdrBookPartyDesc.FLD_NAME) : null;
		
			if(codeProp != null) {
				codeProp.removeListener(nameListener);
			}
			
			if(nameProp != null) {
				nameProp.removeListener(descriptionListener);
			}
		}

		super.dispose();
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		Company company = getCompany();
		if(company != null) {
			AdrBookParty party = company.getAdrBookParty();
			FProperty codeProp = party != null ? party.getFocProperty(AdrBookPartyDesc.FLD_CODE) : null;
			FProperty nameProp = party != null ? party.getFocProperty(AdrBookPartyDesc.FLD_NAME) : null;
			
			if(codeProp != null) {
				nameListener = new FPropertyListener() {
					@Override
					public void propertyModified(FProperty property) {
						if(getCompany() != null) getCompany().copyNameFromAdressBook();
					}
					
					@Override
					public void dispose() {
					}
				};
				codeProp.addListener(nameListener);
			}
			
			if(nameProp != null) {
				descriptionListener = new FPropertyListener() {
					@Override
					public void propertyModified(FProperty property) {
						if(getCompany() != null) getCompany().copyDescriptionFromAdressBook();
					}
					
					@Override
					public void dispose() {
					}
				};
				nameProp.addListener(descriptionListener);
			}
		}
	}
	
	public Company getCompany(){
    return (Company) getFocData();
  }	

}
