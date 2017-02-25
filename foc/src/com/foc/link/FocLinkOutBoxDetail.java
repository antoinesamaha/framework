package com.foc.link;

import com.foc.admin.FocUser;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FocLinkOutBoxDetail extends FocObject {

	public FocLinkOutBoxDetail(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}
	
	public FocUser getToUser(){
		return (FocUser) getPropertyObject(FocLinkOutBoxDetailDesc.FLD_TO_USER);
	}
	
	public void setToUser(FocUser user){
		setPropertyObject(FocLinkOutBoxDetailDesc.FLD_TO_USER, user);
	}
	
	public int getStatus(){
		return getPropertyMultiChoice(FocLinkOutBoxDetailDesc.FLD_STATUS);
	}
	
	public void setStatus(int status){
		setPropertyMultiChoice(FocLinkOutBoxDetailDesc.FLD_STATUS, status);
	}
	
	public String getReceiverComment(){
		return getPropertyString(FocLinkOutBoxDetailDesc.FLD_RECEIVER_COMMENT);
	}
	
	public void setReceiverComment(String receiverComment){
		setPropertyString(FocLinkOutBoxDetailDesc.FLD_RECEIVER_COMMENT, receiverComment);
	}
	
  public static FocDesc getFocDesc() {
    return FocLinkOutBoxDetailDesc.getInstance();
  }

}
