package com.foc.desc.field;

import java.util.HashMap;

import com.fab.model.table.FieldDefinition;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.property.FDescPropertyStringBased;
import com.foc.property.FProperty;

public class FDescFieldStringBased extends FMultipleChoiceStringField {
	private HashMap<String, FocDesc> focDescMap = null;
	
	public FDescFieldStringBased(String name, String title, int id, boolean key) {
		super(name, title, id, key, 50);
	}
	
	public void dispose(){
		super.dispose();
		if(this.focDescMap != null){
			this.focDescMap.clear();
			this.focDescMap = null;
		}
	}
	
  @Override
  public int getFabType() {
    return FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE_FOC_DESC;
  }

  public FProperty newProperty(FocObject masterObj, Object defaultValue){
  	return new FDescPropertyStringBased(masterObj, getID(), (String) (defaultValue == null ? "" : defaultValue));
  }
  
  private HashMap<String, FocDesc> getFocDescMap(){
  	if(this.focDescMap == null){
  		this.focDescMap = new HashMap<String, FocDesc>();
  	}
  	return this.focDescMap;
  }
  
  public void putChoice(String focDescName){
  	addChoice(focDescName);
  }
  
  public FocDesc getFocDesc(String focDescName){
  	FocDesc focDesc = getFocDescMap().get(focDescName);
  	if(focDesc == null){
  		focDesc = Globals.getApp().getFocDescByName(focDescName);
  		if(focDesc != null){
  			getFocDescMap().put(focDescName, focDesc);
  		}
  	}
  	return focDesc;
  }
}
