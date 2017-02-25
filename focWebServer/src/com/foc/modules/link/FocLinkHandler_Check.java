package com.foc.modules.link;

public class FocLinkHandler_Check extends AbstractSpecificHandler implements IFocLinkConst {

	@Override
	public StringBuffer getResponse() {
		StringBuffer buff = new StringBuffer("<");
		buff.append(TAG_LINK_CHECK);
		buff.append("></");
		buff.append(TAG_LINK_CHECK);
		buff.append(">");
		return buff;
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

}
