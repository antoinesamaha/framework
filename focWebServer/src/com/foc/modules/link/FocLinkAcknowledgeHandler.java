package com.foc.modules.link;

import com.foc.link.IFocOperation;

public class FocLinkAcknowledgeHandler extends AbstractSpecificHandler{

	@Override
	public StringBuffer getResponse() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<LINK_ACKNOWLEDGE>");
		IFocOperation operation = null;
//		FocOperationFactory.getInstance().getOperationByName("");                  //just to make it work
		
		if(operation != null){
			operation.execute();
			buffer.append(operation.postExecutionXML());
	  }else{
	  	buffer.append("<ERROR>\r\n");
	  	buffer.append("Operation not found");
	  	buffer.append("</ERROR>\r\n");
		}
		
		buffer.append("</LINK_ACKNOWLEDGE>");
		return buffer;
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

}
