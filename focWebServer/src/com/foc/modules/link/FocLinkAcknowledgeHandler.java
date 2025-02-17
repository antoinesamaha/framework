/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.modules.link;

import com.foc.link.IFocOperation;

public class FocLinkAcknowledgeHandler extends AbstractSpecificHandler{

	@Override
	public StringBuffer getResponse() { // adapt_notQuery
		StringBuffer buffer = new StringBuffer(); // adapt_notQuery
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
