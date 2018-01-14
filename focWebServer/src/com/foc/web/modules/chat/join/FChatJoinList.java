package com.foc.web.modules.chat.join;

import com.foc.list.FocLinkJoinRequest;
import com.foc.list.FocListWithFilter;

@SuppressWarnings("serial")
public class FChatJoinList extends FocListWithFilter {

	public FChatJoinList() {
		super(FChatJoinFilter.getFocDesc(), new FocLinkJoinRequest(FChatJoin.getFocDesc().getFocRequestDesc(false)));
	}
	
	public FChatJoinList(String tableName, long reference) {
		super(FChatJoinFilter.getFocDesc(), new FocLinkJoinRequest(FChatJoin.getFocDesc().getFocRequestDesc(false)));
		
	}

	
}
