package com.foc.web.modules.chat.join;

import com.foc.admin.FocUser;
import com.foc.list.FocLinkJoinRequest;
import com.foc.list.FocListWithFilter;

@SuppressWarnings("serial")
public class FChatJoinList extends FocListWithFilter {

	public FChatJoinList() {
		super(FChatJoinFilter.getFocDesc(), new FocLinkJoinRequest(FChatJoin.getFocDesc().getFocRequestDesc(false)));
	}
	
	public FChatJoinList(FocUser receiver) {
		super(FChatJoinFilter.getFocDesc(), new FocLinkJoinRequest(FChatJoin.getFocDesc().getFocRequestDesc(false)));
		
		FChatJoinFilter filter = (FChatJoinFilter) getFocListFilter();
		filter.filterReceiver(receiver);
		filter.filterUnread();
		filter.setActive(true);
	}
	
}
