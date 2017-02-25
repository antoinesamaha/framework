package com.foc.business.calendar;

import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class FCalendarList extends FocList {

	public FCalendarList() {
		super(new FocLinkSimple(FCalendarDesc.getInstance()));
    setDirectlyEditable(false);
    setDirectImpactOnDatabase(true);
    if(getListOrder() == null){
      FocListOrder order = new FocListOrder(FCalendarDesc.FLD_NAME);
      setListOrder(order);
    }
	}

	@Override
	public void setDirectlyEditable(boolean directlyEditable) {
		super.setDirectlyEditable(directlyEditable);
	}
	
}
