package com.foc.formula;

import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FProperty;

public class FocListFormulaModel implements IFormulaModel{
  
	private FocList list = null;
	
	public FocListFormulaModel(FocList list){
		this.list = list;
	}

  @Override
  public void dispose(){
  	list = null;
  }
	
	@Override
	public String getViewName(){
  	return "MEMORY_VIEW";
  }
  
  @Override
  public String getFilterCriteria(){
  	return "NO_NEED";
  }
  
  @Override
  public FProperty getFormulaProperty(String objectRefs){
  	FProperty property = null;
  	
  	if(objectRefs != null && !objectRefs.isEmpty()){
  		int commaIndex = objectRefs.indexOf(',');
  		if(commaIndex > 0){
  			int ref     = Integer.valueOf(objectRefs.substring(0, commaIndex));	
  			int fieldID = Integer.valueOf(objectRefs.substring(commaIndex));
  			
  			FocObject focObj = list.searchByReference(ref);
  			if(focObj != null){
  				property = focObj.getFocProperty(fieldID);
  			}
  		}
  	}
  	
  	return property;
  }
  
  @Override
  public String getObjectRefs(FProperty property){
  	String str = "";
  	if(property != null){
  		FocObject focObj = property.getFocObject();
  		if(focObj != null){
  			str = focObj.getReference().getInteger()+","+property.getFocField().getID();
  		}
  	}
  	return str;
  }
}
