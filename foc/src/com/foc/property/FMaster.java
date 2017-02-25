package com.foc.property;

import com.foc.desc.FocObject;
import com.foc.desc.field.FField;

public class FMaster extends FProperty{

	public FMaster(FocObject focObj) {
		super(focObj, FField.MASTER_MIRROR_ID);
	}
	
  public Object getObject() {
    return getFocObject().getMasterObject();
  }
}
