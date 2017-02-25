package com.foc.admin.defaultappgroup;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.gui.FPanel;

public class DefaultAppGroup extends FocObject {

	public DefaultAppGroup(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}
	
	public FPanel newDetailsPanel(int viewID){
		return new FPanel();
	}

}
