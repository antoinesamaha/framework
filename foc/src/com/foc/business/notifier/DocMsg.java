package com.foc.business.notifier;

import java.sql.Date;

import com.foc.admin.FocUser;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class DocMsg extends FocObject{

	public DocMsg(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}
	
	public FocUser getSender(){
		return (FocUser) getPropertyObject(DocMsgDesc.FLD_SENDER);
	}
	
	public void setSender(FocUser sender){
		setPropertyObject(DocMsgDesc.FLD_SENDER, sender);
	}
	
	public FocUser getReceiver(){
		return (FocUser) getPropertyObject(DocMsgDesc.FLD_RECEIVER);
	}
	
	public void setReceiver(FocUser receiver){
		setPropertyObject(DocMsgDesc.FLD_RECEIVER, receiver);
	}
	
	public boolean isMessageSent(){
		return getPropertyBoolean(DocMsgDesc.FLD_SENT);
	}
	
	public void setMessageSent(boolean sent){
		setPropertyBoolean(DocMsgDesc.FLD_SENT, sent);
	}
	
	public Date getSendDate(){
		return getPropertyDate(DocMsgDesc.FLD_SEND_DATE);
	}
	
	public void setSendDate(Date date){
		setPropertyDate(DocMsgDesc.FLD_SEND_DATE, date);
	}
	
	public boolean isMessageRead(){
		return getPropertyBoolean(DocMsgDesc.FLD_IS_READ);
	}
	
	public void setMessageRead(boolean isRead){
		setPropertyBoolean(DocMsgDesc.FLD_IS_READ, isRead);
	}
	
	public Date getReadDate(){
		return getPropertyDate(DocMsgDesc.FLD_READ_DATE);
	}
	
	public void setReadDate(Date date){
		setPropertyDate(DocMsgDesc.FLD_READ_DATE, date);
	}
	
	public DocMsgContent getDocMsgContent(){
		return (DocMsgContent) getPropertyObject(DocMsgDesc.FLD_DOC_MSG_CONTENT);
	}
	
	public void setDocMsgContent(DocMsgContent docMsgContent){
		setPropertyObject(DocMsgDesc.FLD_DOC_MSG_CONTENT, docMsgContent);
	}
}
