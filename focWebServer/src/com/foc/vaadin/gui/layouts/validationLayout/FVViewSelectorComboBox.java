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