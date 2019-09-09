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
package com.foc.vaadin.gui.layouts.validationLayout;

import com.foc.admin.GroupXMLViewDesc;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVComboBox;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public class FVViewSelectorComboBox extends FVComboBox {

	public FVViewSelectorComboBox() {
		addStyleName("noPrint");		
	}

	public void dispose(){
		super.dispose();
	}
	
	public void fillViewNames(XMLViewKey xmlViewKey, int rightLevel){
		String[] arrayOfViews = null;
		if(rightLevel == GroupXMLViewDesc.ALLOW_NOTHING){
			arrayOfViews = new String[1];
			arrayOfViews[0] = xmlViewKey.getUserView();
		}else{
			arrayOfViews = XMLViewDictionary.getInstance().getXmlViews(xmlViewKey);
		}
		
		for(int i = 0; i < arrayOfViews.length; i++){
			String viewString = arrayOfViews[i];
			addItem(viewString);
		}
	}
}
