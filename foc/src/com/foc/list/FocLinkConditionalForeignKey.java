package com.foc.list;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FFieldPath;
import com.foc.property.IFDescProperty;

public class FocLinkConditionalForeignKey extends FocLinkForeignKey implements Cloneable {
	private FFieldPath descPropertyPathFromCurrentObject = null;
	
	public FocLinkConditionalForeignKey(FFieldPath descPropertyPathFromCurrentObject, boolean transactionalWithChildren){
		super(null, FField.NO_FIELD_ID, transactionalWithChildren);
		this.descPropertyPathFromCurrentObject = descPropertyPathFromCurrentObject;
	}
	
  public FocDesc getSlaveDesc(FocObject currentFocObject) {
  	IFDescProperty prop = (IFDescProperty)descPropertyPathFromCurrentObject.getPropertyFromObject(currentFocObject);
  	FocDesc desc = prop.getSelectedFocDesc();
  	setSlaveDesc(desc);
    return desc;
  }
  
  public FocLinkConditionalForeignKey clone(){
  	FocLinkConditionalForeignKey focLinkForeignKey = null;
  	try {
			focLinkForeignKey = (FocLinkConditionalForeignKey) super.clone();
		} catch (CloneNotSupportedException e) {
			Globals.logException(e);
		}
  	return focLinkForeignKey;
  }
}
