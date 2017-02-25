package com.foc.desc.field;

import com.fab.model.table.FieldDefinition;
import com.foc.shared.xmlView.XMLViewKey;

public class FXMLViewSelectorField extends FMultipleChoiceStringField {
	
	private XMLViewKey xmlViewKey = null; 
	
	public FXMLViewSelectorField(String name, String title, int id) {
		super(name, title, id, false, XMLViewKey.LEN_VIEW);
	}
	
	public void dispose(){
		super.dispose();
	}
	
  @Override
  public int getFabType() {
    return FieldDefinition.SQL_TYPE_ID_XML_VIEW_SELECTOR;
  }

	
	public XMLViewKey getXmlViewKey() {
		return xmlViewKey;
	}

	public void setXmlViewKey(XMLViewKey xmlViewKey) {
		this.xmlViewKey = xmlViewKey;
	}

}
