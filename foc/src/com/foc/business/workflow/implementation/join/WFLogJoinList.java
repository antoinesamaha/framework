package com.foc.business.workflow.implementation.join;

import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.db.SQLFilter;
import com.foc.list.FocLinkJoinRequest;
import com.foc.list.FocListOrder;
import com.foc.list.FocListWithFilter;
import com.foc.log.FocLogEvent;

@SuppressWarnings("serial")
public class WFLogJoinList extends FocListWithFilter {

	public WFLogJoinList(WFLogJoinFilterDesc filterDesc, WFLogJoinDesc joinDesc){
		super(filterDesc, new FocLinkJoinRequest(joinDesc.getFocRequestDesc()));
		setListOrder(new FocListOrder(WFLogDesc.FLD_DATE_TIME));
	}

	public void setFilter_ByStatus(){
		SQLFilter filter = getFilter();
		filter.putAdditionalWhere("STATUS", "EVT_STATUS="+FocLogEvent.STATUS_INCLUDED+" OR EVT_STATUS="+FocLogEvent.STATUS_POSTED+" OR (EVT_STATUS>="+FocLogEvent.STATUS_ERROR_START+" AND EVT_STATUS<"+FocLogEvent.STATUS_ERROR_END+")");
	}
	
}