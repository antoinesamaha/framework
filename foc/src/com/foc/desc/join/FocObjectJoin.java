package com.foc.desc.join;

import com.foc.access.AccessControl;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

public class FocObjectJoin extends FocObject{
	public FocObjectJoin(AccessControl accessControl) {
		super(accessControl);
	}

	public FocObjectJoin(FocConstructor constr) {
		super(constr);
	}

	public FocObjectJoin(FocDesc desc) {
		super(desc);
	}
}
