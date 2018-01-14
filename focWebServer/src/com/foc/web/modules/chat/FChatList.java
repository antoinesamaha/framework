package com.foc.web.modules.chat;

import com.foc.desc.FocObject;
import com.foc.list.FocLinkJoinRequest;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocListWithFilter;
import com.foc.web.modules.chat.join.FChatJoin;
import com.foc.web.modules.chat.join.FChatJoinFilter;

@SuppressWarnings("serial")
public class FChatList extends FocListWithFilter {

	private FocObject focObject = null;
	
	public FChatList() {
		super(FChatJoinFilter.getFocDesc(), new FocLinkJoinRequest(FChatJoin.getFocDesc().getFocRequestDesc(false)));
	}
	
	public FChatList(FocObject focObject) {
		super(FChatFilter.getFocDesc(), new FocLinkSimple(FChat.getFocDesc()));
		
		if(focObject != null && focObject.getThisFocDesc() != null) {
			String tableName = focObject.getThisFocDesc().getStorageName();
			long   reference = focObject.getReferenceInt();
			FChatFilter filter = (FChatFilter) getFocListFilter();
			filter.filterSubject(tableName, reference);
		}
	}
	
	public void dispose() {
		super.dispose();
		focObject = null;
	}
	
	@Override
	public FocObject newEmptyItem() {
		FChat  chat      = (FChat) super.newEmptyItem();
		String tableName = getTableName();
		if(tableName != null) {
			chat.setSubjectTableName(tableName);
		}
		long ref = getSubjectReference();
		if(ref > 0) {
			chat.setSubjectReference(ref);
		}
		chat.addReceipients(focObject);
		return chat;
	}

	public String getTableName() {
		String tableName = null;
		FChatFilter filter = (FChatFilter) getFocListFilter();
		tableName = filter != null ? filter.getTableName() : null;
		return tableName;
	}
	
	public long getSubjectReference() {
		FChatFilter filter = (FChatFilter) getFocListFilter();
		return filter != null ? filter.getSubjectReference() : null;
	}
}
