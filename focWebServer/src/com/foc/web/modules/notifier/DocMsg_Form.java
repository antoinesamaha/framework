package com.foc.web.modules.notifier;

import java.io.InputStream;
import java.util.Scanner;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.business.notifier.DocMsg;
import com.foc.business.notifier.DocMsgContent;
import com.foc.business.notifier.DocMsgContentDesc;
import com.foc.business.notifier.FocPageLink;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.mail.FocMailSender;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.ASCII;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class DocMsg_Form extends FocXMLLayout{

	private IFocData previousFocData = null;
	private XMLView previousXmlView = null;
	
	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		FocUser user = getUserForThisSession();
		if(getDocMsg() != null){
			getDocMsg().setSender(user);
			getDocMsg().setSendDate(Globals.getApp().getSystemDate());
		}
	}
	
	private XMLViewKey getPreviousXmlViewKey(){
		return getPreviousXmlView() != null ? getPreviousXmlView().getXmlViewKey() : null;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FVButton sendButton = getSendButton();
		if(sendButton != null){
			sendButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					copyGuiToMemory();
			
					DocMsg docMsg = getDocMsg();
					XMLViewKey xmlViewKey = getPreviousXmlViewKey();
					if(xmlViewKey != null && docMsg != null){
						FocList docMsgContentList   = DocMsgContentDesc.getInstance().getFocList();
						DocMsgContent docMsgContent = (DocMsgContent) docMsgContentList.newEmptyItem();
						docMsgContent.fill((FocObject) getPreviousFocData(), xmlViewKey, null);
//						String xmlContent = pasrseXmlLayout();
						String xmlContent = XMLViewDictionary.getInstance().pasrseXmlLayout(getPreviousFocData(), getPreviousXmlView(), getPreviousXmlViewKey());
						docMsgContent.setXMLContent(xmlContent);
						docMsgContent.validate(true);
						docMsg.setDocMsgContent(docMsgContent);
						docMsg.validate(true);
						//----------------------
//						FocList docMsgContentList   = DocMsgContentDesc.getInstance().getFocList();
//						DocMsgContent docMsgContent = (DocMsgContent) docMsgContentList.newEmptyItem();
//						String xmlContent = pasrseXmlLayout();
//						docMsgContent.setXMLContent(xmlContent);
//						docMsgContent.validate(true);
//						docMsg.setDocMsgContent(docMsgContent);
//						docMsg.validate(true);
//						
//						FocList focList = FocPageLinkDesc.getList(FocList.LOAD_IF_NEEDED);
//	        	FocPageLink focPageLink = (FocPageLink) focList.newEmptyItem();
//	        	focPageLink.fill((FocObject) getPreviousFocData(), xmlViewKey, null, getUrlKey(focList));
//	        	focPageLink.validate(true);
	        	
//						InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());
//						FocMailSender message = newFocMailSender("Text", "key");
//						sendEmailTo(message, "hadi.abouhamze@01barmaja.com");
//						String javaScript = "window.location = 'mailto:?body=Hello,%20%0d%0a%20%0d%0aClick on this link or copy it in you internet browser to open the document:%20%0d%0a"+Page.getCurrent().getLocation()+randomKeyStringForURL+"%20%0d%0a%20%0d%0aRegards,'"; 
//	          JavaScript.getCurrent().execute(javaScript);
					}
				}
			});
		}
	}
	
//	private String pasrseXmlLayout(){
//		StringBuilder newXmlContent = new StringBuilder();
//		XMLViewKey xmlViewKey = getPreviousXmlViewKey();
//		if(xmlViewKey != null && XMLViewDictionary.getInstance().isXMLViewFound(xmlViewKey)){
//			InputStream inputStream = getPreviousXmlView().getXMLStream_ForView();
//			Scanner xmlFileLine = new Scanner(inputStream).useDelimiter("\n");
//      while(xmlFileLine.hasNext()){
//      	String line = xmlFileLine.next();
//      	line = FocDataDictionary.getInstance().resolveExpression(getPreviousFocData(), line, true);
//      	newXmlContent.append(line);
//      }
//		}
//		return newXmlContent.toString();
//	}
	
	private FVButton getSendButton(){
		return (FVButton) getComponentByName("SEND_BUTTON");
	}
	
	private FocUser getUserForThisSession(){
		return Globals.getApp().getUser_ForThisSession();
	}
	
	public void setPreviousXmlView(XMLView previousXmlView){
		this.previousXmlView = previousXmlView;
	}
	
	private XMLView getPreviousXmlView(){
		return previousXmlView;
	}
	
	public void setPreviousFocData(IFocData previousFocData){
		this.previousFocData = previousFocData;
	}
	
	private IFocData getPreviousFocData(){
		return previousFocData;
	}
	
	private DocMsg getDocMsg(){
		return (DocMsg) getFocData();
	}
	
	private FocMailSender newFocMailSender(String subject, String url){
		FocMailSender sender = new FocMailSender();
		sender.setFrom("01barmaja@01barmaja.com");//getDocMsg().getSender()
		sender.setHost("email-smtp.us-east-1.amazonaws.com");
		sender.setPort("465");
		sender.setEncryptionConnectionType(FocMailSender.ENCRYPTION_TYPE_SSL);
		sender.setSubject(subject);
		sender.setMessage(url);
		return sender;
	}
	
	private void sendEmailTo(FocMailSender sender, String email){
	  String username = "AKIAIVMOV3G7F55RILIQ";
	  String password = "Ajmnx7ON3LoKYMtFdbx+LPqls3HnArYkGlJ96/42qFKu";
		
		sender.addTo(email);
		
		sender.send(username, password);
	}
}
