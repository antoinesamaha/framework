package com.foc.pivot;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FPivotRowList extends FocList {
	
	public FPivotRowList(FocDesc desc){
		super(new FocLinkSimple(desc));
	}
	
	public FPivotRowList(FocDesc desc, FPivotRowList list){
	  super(new FocLinkSimple(desc));
	  
	  for(int i=0; i<list.size(); i++){
	    FocObject temp = this.newEmptyItem();
	    temp = list.getFocObject(i);
	    this.add(temp);
	  }
	}

}
