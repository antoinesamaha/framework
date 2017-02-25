package com.foc.business.notifier;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.shared.xmlView.XMLViewKey;

@SuppressWarnings("serial")
public class FocPageLink extends FocPage {
 
	public FocPageLink(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
	
	public String getKey() {
    return getPropertyString(FocPageLinkDesc.FLD_KEY);
  }
  
  public void setKey(String key) {
    setPropertyString(FocPageLinkDesc.FLD_KEY, key);
    Globals.logString("Link Key : "+key);
  }
  
  public void fill(FocObject focObj, XMLViewKey xmlViewKey, String serialisation, String linkKey){
  	super.fill(focObj, xmlViewKey, serialisation);
  	setKey(linkKey);
  }
}
