package com.foc.business.multilanguage;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class Language extends FocObject {

  public Language(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public void dispose(){
  	super.dispose();
  }

  public String getCode(){
    return getPropertyString(LanguageDesc.FLD_CODE);
  }

  public void setCode(String code){
    setPropertyString(LanguageDesc.FLD_CODE, code);
  }

  public String getName(){
    return getPropertyString(LanguageDesc.FLD_NAME);
  }
  
  public void setName(String name){
    setPropertyString(LanguageDesc.FLD_NAME, name);
  }
  
  public String getDescription(){
    return getPropertyString(LanguageDesc.FLD_DESCRIPTION);
  }
  
  public void setDescription(String desc){
    setPropertyString(LanguageDesc.FLD_DESCRIPTION, desc);
  }  
}
