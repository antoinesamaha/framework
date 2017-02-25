package com.foc.modules.link;

import java.util.Map;

import com.foc.link.FocLinkOutBox;
import com.foc.link.FocLinkOutBoxDesc;
import com.foc.link.FocLinkOutBoxDetail;
import com.foc.link.FocLinkOutBoxDetailDesc;
import com.foc.link.FocOperationFactory;
import com.foc.link.IFocOperation;
import com.foc.list.FocList;
import com.foc.util.Utils;

public class FocLinkResponseGenerator implements IFocLinkConst {
	private String     userName   = null;
	private FocList    outLogList = null;
	private Map<String, Object>  attributeMap = null;

	public FocLinkResponseGenerator(String userName, Map<String, Object> map) {
		this.setUserName(userName);
		this.attributeMap = map;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public FocList getOutLogList() {
		if(outLogList == null){
			outLogList = FocLinkOutBoxDesc.getList();
		}
		return outLogList;
	}

	public void setOutLogList(FocList outLogList) {
		this.outLogList = outLogList;
	}

	public String scanOutLogList(){

		String objectXmls = "";

		if(getOutLogList() != null && !Utils.isStringEmpty(getUserName())){
			for(int i=0; i<getOutLogList().size(); i++){
				FocLinkOutBox currentOutLog = (FocLinkOutBox) getOutLogList().getFocObject(i);

				//Filtering by storage. If the storage is not specified in the request we accept all storage names.
				//Filtering by transaction type
				if(currentOutLog != null && (Utils.isStringEmpty(getStorage()) || currentOutLog.getStorage().equals(getStorage())) && (getTransactionType() == null || (getTransactionType() == currentOutLog.getTransactionType()))){
					FocList outLogDetailsList = currentOutLog.getDetailsList();

					if(outLogDetailsList != null){
						for(int j=0; j<outLogDetailsList.size(); j++){
							FocLinkOutBoxDetail currentDetail = (FocLinkOutBoxDetail) outLogDetailsList.getFocObject(j);

							if(currentDetail != null){
								//Filtering by username and status.
								//If status is unspecified, we assume the status is "Posted"
								if(currentDetail.getToUser() != null && currentDetail.getToUser().getName().equals(getUserName()) && currentDetail.getStatus() == FocLinkOutBoxDetailDesc.mapStatusStringToStatusInteger(getStatus())){
									objectXmls += generateObjectXmlWrapper(currentOutLog);
									if(getStatus().equals(FocLinkOutBoxDetailDesc.STATUS_POSTED_VALUE)){
										currentDetail.setStatus(FocLinkOutBoxDetailDesc.STATUS_SENT);
										currentDetail.validate(false);
									}
								}
							}
						}
					}
				}
			}
		}

		return objectXmls;
	}

	
	public String generateObjectXmlWrapper(FocLinkOutBox outLog){
		String xmlWrapper = "";


		xmlWrapper += "<LINK_DOWNLOAD ref=\""+outLog.getReference()+"\">\r\n";

		xmlWrapper+= outLog.getXmlMessage();


		xmlWrapper += "</LINK_DOWNLOAD>\r\n";


		return xmlWrapper;

	}
	
	public String generateXmlResponse(){
		String xmlResponse = "";

		//The XML response generated if the client requested a download
		if(isDownloadRequest()){
			xmlResponse = "<LINK_RESPONSE>\r\n";

			xmlResponse+= scanOutLogList();

			xmlResponse += "</LINK_RESPONSE>\r\n";
		}
		
		//If the client sent an ACK to a download, we just set the status and receiver comments in the database
		else if(isAck()){
			Map<String, String> receiverComments = getReceiverComments();
			Map<String, String> statusMap = getAckStatus();

			if(receiverComments != null && statusMap != null && receiverComments.keySet().equals(statusMap.keySet())){
				if(getOutLogList() != null){
					for(int i=0; i<getOutLogList().size(); i++){
						FocLinkOutBox currentOutLog = (FocLinkOutBox) getOutLogList().getFocObject(i);

						if(currentOutLog != null && receiverComments.keySet().contains(currentOutLog.getReference().getString())){
							FocList detailsList = currentOutLog.getDetailsList();

							if(detailsList != null){
								for(int j=0; j<detailsList.size(); j++){
									FocLinkOutBoxDetail currentDetail = (FocLinkOutBoxDetail) detailsList.getFocObject(i);

									if(currentDetail != null && currentDetail.getToUser().getName().equals(getUserName())){
										if(currentDetail.getStatus() != FocLinkOutBoxDetailDesc.mapStatusStringToStatusInteger(statusMap.get(currentOutLog.getReference().getString()))){
											currentDetail.setStatus(FocLinkOutBoxDetailDesc.mapStatusStringToStatusInteger(statusMap.get(currentOutLog.getReference().getString())));
										}

										currentDetail.setReceiverComment(receiverComments.get(currentOutLog.getReference().getString()));
										currentDetail.validate(false);
									}
								}
							}
						}
					}
				}
			}
		}
		
		//If the client is requesting the execution of an operation
		else if(isOperationRequest()){
			xmlResponse = "<LINK_ACKNOWLEDGE>\r\n";
			
			IFocOperation operation = FocOperationFactory.getInstance().getOperationByName(getOperationName());
			
			if(operation != null){
				operation.execute();
				xmlResponse += operation.postExecutionXML();
			}
			else{
				xmlResponse += "<ERROR>\r\n";
				xmlResponse += "Operation not found";
				xmlResponse += "</ERROR>\r\n";
			}
			
			xmlResponse += "</LINK_ACKNOWLEDGE>\r\n";
			
		}

		return xmlResponse;
	}

	public Map<String, Object> getMap() {
		return attributeMap;
	}
	
	public String getStorage(){
		return (String) (attributeMap == null ? null : getMap().get(TAG_STORAGE));
	}
	
	public Integer getTransactionType(){
		return (Integer) (attributeMap == null ? null : getMap().get(TAG_TRANSACTION_TYPE));
	}
	
	public String getStatus(){
		return (String) (attributeMap == null ? null : getMap().get(TAG_STATUS));
	}
	
	public String getOperationName(){
		return (String) (attributeMap == null ? null : getMap().get(ATT_NAME));
	}
	
	public boolean isDownloadRequest(){
		return (Boolean) (attributeMap == null ? null : getMap().get(TAG_LINK_DOWNLOAD));
	}
	
	public boolean isAck(){
		return (Boolean) (attributeMap == null ? null : getMap().get(TAG_LINK_ACK));
	}
	
	public boolean isOperationRequest(){
		return (Boolean) (attributeMap == null ? null : getMap().get(TAG_LINK_OPERATION));
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @return The key of the map is the download reference, the value is the receiver comment.
	 */
	public Map<String, String> getReceiverComments(){
		return (Map<String, String>) (attributeMap == null ? null : getMap().get(TAG_RECEIVER_COMMENT));
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @return The key of the map is the download reference, the value is the status.
	 */
	public Map<String, String> getAckStatus(){
		return (Map<String, String>) (attributeMap == null ? null : getMap().get(TAG_STATUS));
	}
	
}
