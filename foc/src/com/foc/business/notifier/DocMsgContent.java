package com.foc.business.notifier;

import com.foc.desc.FocConstructor;

@SuppressWarnings("serial")
public class DocMsgContent extends FocPage{

	public DocMsgContent(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
	
	public String getXMLContent(){
    return getPropertyString(DocMsgContentDesc.FLD_XML);
  }
  
  public void setXMLContent(String xmlContent){
    setPropertyString(DocMsgContentDesc.FLD_XML, xmlContent);
  }
}
