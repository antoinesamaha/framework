package com.foc.wrapper;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.list.FocLinkForeignKey;
import com.foc.list.FocList;
import com.foc.property.FProperty;

public class Wrapper extends FocObject{
	private HashMap<WrapperLevel, FocList> map = null;
	private WrapperLevel level = null; 
	
  public Wrapper(FocConstructor constr) {
    super(constr);
    newFocProperties();
    map = new HashMap<WrapperLevel, FocList>();
  }
  
  public void dispose(){
  	super.dispose();
  	if(map != null){
  		Iterator iter = map.values().iterator();
  		while(iter != null && iter.hasNext()){
  			FocList list = (FocList) iter.next();
  			if(list != null){
  				list.dispose();
  				list = null;
  			}
  		}
  		map.clear();
  		map = null;
  	}
  	level = null;
  }
  
  public FProperty getFocProperty(int fieldID){
    FProperty property = super.getFocProperty(fieldID);
    if(fieldID == WrapperDesc.FLD_WRAPPED){
      WrapperLevel level = getLevel();
      if(level != null){
        property = getFocProperty(level.getObjectFieldID());
      }
    }else if(fieldID == WrapperDesc.FLD_WRAPPED){
      WrapperLevel level = getLevel();
      if(level != null){
        property = getFocProperty(level.getObjectFieldID());
      }
    }
    
    return property;
  }
  
  public FocObject getWrappedObject() {
    return getFocObjectAccordinglyToLevel();
  }

  public String getNodeName(){
    return getPropertyString(FField.FLD_NAME);
  }
  
  public void setNodeName(String nodeName){
    setPropertyString(FField.FLD_NAME, nodeName);
  }
  
  public FocList getChildfocList(FocObject obj, WrapperLevel wrapperLevel){
  	FocList childrenList = map.get(wrapperLevel);
  	if(childrenList == null){
	    FocLinkForeignKey link = new FocLinkForeignKey(wrapperLevel.getFocDesc(), wrapperLevel.getForeignKeyField(), true);
	    childrenList = new FocList(obj, link, null);
	    childrenList.setFatherSubject(this);
	    childrenList.loadIfNotLoadedFromDB();
	    map.put(wrapperLevel, childrenList);
  	}
    return childrenList;
  }

	public WrapperLevel getLevel() {
		return level;
	}

	public void setLevel(WrapperLevel level) {
		this.level = level;
	}
  
	public FocObject getFocObjectAccordinglyToLevel(){
		FocObject focObject = null;
		WrapperLevel level = getLevel();
		if(level != null){
			focObject = getPropertyObject(level.getObjectFieldID());
		}
		return focObject;
	}
}
