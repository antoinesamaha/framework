package com.foc.access;

import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FocLogLineList extends FocList {
	
	public FocLogLineList(){
		super(new FocLinkSimple(FocLogLineDesc.getInstance()));
		setDirectlyEditable(false);
		setDirectImpactOnDatabase(true);
	}
	
	@Override
	public void dispose(){
		super.dispose();
	}
}
