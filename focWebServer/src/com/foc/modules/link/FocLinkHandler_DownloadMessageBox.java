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

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.foc.Globals;
import com.foc.link.FocLinkOutBox;
import com.foc.link.FocLinkOutBoxDesc;
import com.foc.link.FocLinkOutBoxDetail;
import com.foc.link.FocLinkOutBoxDetailDesc;
import com.foc.list.FocList;
import com.foc.util.Utils;

public class FocLinkHandler_DownloadMessageBox extends AbstractSpecificHandler implements IFocLinkConst {

	private String storage         = null;
	private String transactionType = null;
	private String status          = null;
	
	private String lastTagEncountered = null; 
	
	private ArrayList<FocLinkOutBoxDetail> arrayOfSentMessages = null;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		lastTagEncountered = qName;
	}
	
  @Override
  public void characters(char ch[], int start, int length){
  	String content = new String(ch, start, length).trim();
  	
		if(lastTagEncountered.equals(TAG_STORAGE)){
			setStorage(content);
		}else if(lastTagEncountered.equals(TAG_TRANSACTION_TYPE)){
			transactionType = content;
		}else if(lastTagEncountered.equals(TAG_STATUS)){
			setStatus(content);
		}
    
	}
	
	@Override
	public StringBuffer getResponse() { // adapt_notQuery
		StringBuffer buffer = new StringBuffer(); // adapt_notQuery
		
		buffer.append("<"+TAG_LINK_RESPONSE+">");

		scanOutLogList(buffer);

		buffer.append("</"+TAG_LINK_RESPONSE+">");

		return buffer;
	}
	
	public FocList getOutLogList() {
		return FocLinkOutBoxDesc.getList();
	}

	public void scanOutLogList(StringBuffer buffer){ // adapt_notQuery
		if(getOutLogList() != null && Globals.getApp().getUser_ForThisSession() != null){
			getOutLogList().reloadFromDB();
			for(int i=0; i<getOutLogList().size(); i++){
				FocLinkOutBox currentOutLog = (FocLinkOutBox) getOutLogList().getFocObject(i);

				//Filtering by storage. If the storage is not specified in the request we accept all storage names.
				//Filtering by transaction type
				if(currentOutLog != null && (Utils.isStringEmpty(getStorage()) || currentOutLog.getStorage().equals(getStorage())) && (getTransactionType() == null /*|| (getTransactionType() == currentOutLog.getTransactionType()) */)){
					FocList outLogDetailsList = currentOutLog.getDetailsList();

					if(outLogDetailsList != null){
						for(int j=0; j<outLogDetailsList.size(); j++){
							FocLinkOutBoxDetail currentDetail = (FocLinkOutBoxDetail) outLogDetailsList.getFocObject(j);

							if(currentDetail != null){
								//Filtering by username and status.
								//If status is unspecified, we assume the status is "Posted"
								if(currentDetail.getToUser() != null && currentDetail.getToUser().equalsRef(Globals.getApp().getUser_ForThisSession()) && currentDetail.getStatus() == FocLinkOutBoxDetailDesc.mapStatusStringToStatusInteger(getStatus())){
									
									generateObjectXmlWrapper(buffer, currentOutLog);
									
									if(arrayOfSentMessages == null){
										arrayOfSentMessages = new ArrayList<FocLinkOutBoxDetail>();
									}
									arrayOfSentMessages.add(currentDetail);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void generateObjectXmlWrapper(StringBuffer buffer, FocLinkOutBox outLog){ // adapt_notQuery
		buffer.append("<LINK_DOWNLOAD ref=\""+outLog.getReference()+"\">");
		buffer.append(outLog.getXmlMessage());
		buffer.append("</LINK_DOWNLOAD>");		
	}

	public String getStorage() {
		return storage;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public void commit() {
		if(arrayOfSentMessages != null){
			for(int i=0; i<arrayOfSentMessages.size(); i++){
				FocLinkOutBoxDetail currentOutLog = arrayOfSentMessages.get(i);
				currentOutLog.setStatus(FocLinkOutBoxDetailDesc.STATUS_SENT);
				currentOutLog.validate(false);
				if(currentOutLog.getFatherSubject() != null){
					currentOutLog.getFatherSubject().validate(true);
				}
			}
		}
	}
}
