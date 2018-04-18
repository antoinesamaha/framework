package com.foc.business.workflow.implementation;

import java.sql.Date;

import com.foc.admin.FocUser;
import com.foc.business.workflow.WFTitle;
import com.foc.business.workflow.map.WFStage;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.property.FObject;

public class WFLog extends FocObject {
	
	public WFLog(FocConstructor constr){
		super(constr);
		newFocProperties();
	}

	public void setLogSubjectReference(long ref) {
		FObject prop = (FObject) getFocProperty(WFLogDesc.FLD_MASTER);
		if(prop != null) prop.setLocalReferenceInt(ref);
	}
	
	public void setEventType(int type){
		setPropertyMultiChoice(WFLogDesc.FLD_EVENT_TYPE, type);
	}

	public int getEventType(){
		return getPropertyMultiChoice(WFLogDesc.FLD_EVENT_TYPE);
	}
	
	public void setUser(FocUser user){
		setPropertyObject(WFLogDesc.FLD_USER, user);
	}

	public FocUser getUser(){
		return (FocUser) getPropertyObject(WFLogDesc.FLD_USER);
	}

	public void setDateTime(Date date){
		setPropertyDate(WFLogDesc.FLD_DATE_TIME, date);
	}

	public Date getDateTime(){
		return getPropertyDate(WFLogDesc.FLD_DATE_TIME);
	}

	public void setDescription(String str){
		setPropertyString(WFLogDesc.FLD_DESCERIPTION, str);
	}

	public String getDescription(){
		return getPropertyString(WFLogDesc.FLD_DESCERIPTION);
	}

	public void setTitle(WFTitle title){
		setPropertyObject(WFLogDesc.FLD_TITLE, title);
	}

	public WFTitle getTitle(){
		return (WFTitle) getPropertyObject(WFLogDesc.FLD_TITLE);
	}

	public void setOnBehalfOf(boolean onBehalfOf){
		setPropertyMultiChoice(WFLogDesc.FLD_ON_BEHALF_OF, onBehalfOf ? 1 : 0);
	}

	public boolean isOnBehalfOf(){
		int onBehalf = getPropertyMultiChoice(WFLogDesc.FLD_ON_BEHALF_OF);
		return onBehalf == 1 ? true : false;
	}

	public void setComment(String comment){
		setPropertyString(WFLogDesc.FLD_COMMENT, comment);
	}

	public String getComment(){
		return getPropertyString(WFLogDesc.FLD_COMMENT);
	}
	
	
	public void setPreviousStage(WFStage stage){
		setPropertyObject(WFLogDesc.FLD_PREVIOUS_STAGE, stage);
	}

	public WFStage getPreviousStage(){
		return (WFStage) getPropertyObject(WFLogDesc.FLD_PREVIOUS_STAGE);
	}

	public void setTargetStage(WFStage stage){
		setPropertyObject(WFLogDesc.FLD_TARGET_STAGE, stage);
	}

	public WFStage getTargetStage(){
		return (WFStage) getPropertyObject(WFLogDesc.FLD_TARGET_STAGE);
	}
	
	public boolean getEventUndone(){
		return getPropertyBoolean(WFLogDesc.FLD_EVENT_UNDONE);
	}
	
	public void setEventUndone(boolean undone){
		setPropertyBoolean(WFLogDesc.FLD_EVENT_UNDONE, undone);
	}
	
	public String getSignedTransactionXML(){
    return getPropertyString(WFLogDesc.FLD_SIGNED_TRANSACTION_XML);
  }
  
  public void setSignedTransactionXML(String xmlContent){
    setPropertyString(WFLogDesc.FLD_SIGNED_TRANSACTION_XML, xmlContent);
  }
}
