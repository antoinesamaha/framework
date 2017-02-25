package com.foc.modules.link;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.foc.util.Utils;


public class FocLinkHandler_OLD implements IFocLinkConst {

	private Document             doc          = null;
	private Map<String, Object>  attributeMap = null;

	public FocLinkHandler_OLD(InputStream inputStream) throws Exception {
		if(inputStream != null){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			setDoc(builder.parse(inputStream));
			setAttributes();
		}
	}

	
	/**
	 * Setting the appropriate attributes to be sent to the FocLinkResponseGenerator
	 */
	public void setAttributes(){
		getMap().put(TAG_LINK_DOWNLOAD, isDownloadRequest());
		getMap().put(TAG_LINK_ACK, isAck());
		getMap().put(TAG_LINK_OPERATION, isOperationRequest());
		getTransactionType();
		getStorage();
		
		if(isDownloadRequest()){
			getStatus();
		}
		if(isAck()){
			getAckReceiverComments();
			getAckStatus();
		}
		if(isOperationRequest()){
			getOperationName();
		}
	}

	public void respond(HttpServletResponse response, String username){
		FocLinkResponseGenerator responseGenerator = new FocLinkResponseGenerator(username, getMap());
		try {
			String xmlResponse = responseGenerator.generateXmlResponse();
			if(!Utils.isStringEmpty(xmlResponse)){
				response.getOutputStream().write(xmlResponse.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isDownloadRequest(){
		return getDoc().getFirstChild().getNodeName().equals(TAG_LINK_DOWNLOAD);
	}

	public boolean isAck(){
		return getDoc().getFirstChild().getNodeName().equals(TAG_LINK_ACK);
	}
	
	public boolean isOperationRequest(){
		return getDoc().getFirstChild().getNodeName().equals(TAG_LINK_OPERATION);
	}

	public boolean doesTagExist(String tagName){
		boolean result = false;
		if(getDoc() != null){
			result = getDoc().getElementsByTagName(tagName).getLength() > 0;
		}

		return result;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getStorage(){

		if(getMap().get(TAG_STORAGE) == null && getDoc() != null){
			NodeList list = getDoc().getElementsByTagName(TAG_STORAGE);

			if(list != null && list.getLength() > 0){
				Node node = list.item(0);
				if(node != null){
					getMap().put(TAG_STORAGE, node.getTextContent());
				}
			}
		}

		return (String) getMap().get(TAG_STORAGE);
	}

	public void setStorage(String storage) {
		getMap().put(TAG_STORAGE, storage);
	}


	public Integer getTransactionType() {
		if(getMap().get(TAG_TRANSACTION_TYPE) == null && getDoc() != null){
			NodeList list = getDoc().getElementsByTagName(TAG_TRANSACTION_TYPE);

			if(list != null && list.getLength() > 0){
				Node node = list.item(0);
				if(node != null){
					getMap().put(TAG_TRANSACTION_TYPE, Integer.parseInt(node.getTextContent()));
				}
			}
		}

		return (Integer) getMap().get(TAG_TRANSACTION_TYPE);
	}


	public void setTransactionType(Integer transactionType) {
		getMap().put(TAG_TRANSACTION_TYPE, transactionType);
	}


	public String getStatus() {
		if(getMap().get(TAG_STATUS) == null && getDoc() != null){
			NodeList list = getDoc().getElementsByTagName(TAG_STATUS);

			if(list != null && list.getLength() > 0){
				Node node = list.item(0);
				if(node != null){
					getMap().put(TAG_STATUS, node.getTextContent());
				}
			}
		}
		return (String) getMap().get(TAG_STATUS);
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * @return Map<String, String> taking the reference of the download as a key and the receiver comments as value
	 */
	public Map<String, String> getAckReceiverComments() {
		if(getMap().get(TAG_RECEIVER_COMMENT) == null && getDoc() != null){
			NodeList list = getDoc().getElementsByTagName(TAG_RECEIVER_COMMENT);
			
			if(list != null && list.getLength() > 0){
				Map<String, String> receieverComments = new HashMap<String, String>();
				for(int i=0; i<list.getLength(); i++){
					Node node = list.item(i);
					if(node != null){
						Node parent = node.getParentNode();
						receieverComments.put(parent.getAttributes().getNamedItem(ATT_REFERENCE).getTextContent(), node.getTextContent());
					}
					getMap().put(TAG_RECEIVER_COMMENT, receieverComments);
				}
			}
		}
		return (Map<String, String>) getMap().get(TAG_RECEIVER_COMMENT);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @return Map<String, String> taking the reference of the download as a key and the status as value
	 */
	public Map<String, String> getAckStatus(){
		if(getMap().get(TAG_STATUS) == null && getDoc() != null){
			NodeList list = getDoc().getElementsByTagName(TAG_STATUS);
			
			if(list != null && list.getLength() > 0){
				Map<String, String> statusMap = new HashMap<String, String>();
				for(int i=0; i<list.getLength(); i++){
					Node node = list.item(i);
					if(node != null){
						Node parent = node.getParentNode();
						statusMap.put(parent.getAttributes().getNamedItem(ATT_REFERENCE).getTextContent(), node.getTextContent());
					}
					getMap().put(TAG_STATUS, statusMap);
				}
			}
		}
		return (Map<String, String>) getMap().get(TAG_RECEIVER_COMMENT);
	}


	public void setStatus(String status) {
		getMap().put(TAG_STATUS, status);
	}

	public Map<String, Object> getMap() {
		if(attributeMap == null){
			attributeMap = new HashMap<String, Object>();
		}
		return attributeMap;
	}

	public void setAttributes(Map<String, Object> map) {
		this.attributeMap = map;
	}
	
	public String getOperationName(){
		if(getMap().get(ATT_NAME) == null && getDoc() != null){
			NodeList list = getDoc().getElementsByTagName(TAG_LINK_OPERATION);
			if(list != null && list.getLength() > 0){
				Node node = list.item(0);
				if(node != null && node.getAttributes() != null){
					getMap().put(ATT_NAME, node.getAttributes().getNamedItem(ATT_NAME));
				}
			}
		}
		return (String) getMap().get(ATT_NAME);
	}


}
