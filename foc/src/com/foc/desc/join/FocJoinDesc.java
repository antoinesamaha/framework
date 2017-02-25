package com.foc.desc.join;

import com.foc.desc.FocDesc;
import com.foc.join.FocRequestDesc;

public abstract class FocJoinDesc extends FocDesc{

	public abstract FocRequestDesc newRequestDesc();
	
	//private FocDesc        mainFocDesc    = null;
	private FocRequestDesc focReauestDesc = null;

	public FocJoinDesc(Class cls, FocDesc mainFocDesc) {
		super(cls);
		//this.mainFocDesc = mainFocDesc;
		focReauestDesc   = null;
	}
	
	public void dispose(){
		super.dispose();
		//mainFocDesc = null;
		if(focReauestDesc != null){
			focReauestDesc.dispose();
			focReauestDesc = null;
		}
	}
}
