package com.foc.business.workflow;

import java.sql.Date;

import com.foc.admin.FocUser;
import com.foc.business.workflow.map.WFStage;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class WFHorizontalLog extends FocObject{

	public WFHorizontalLog(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}
	
	public FocObject getTransaction(){
		return getPropertyObject(WFHorizontalLogDesc.FLD_TRANSACTION);
	}
	
	public void setTransaction(FocObject transaction){
		setPropertyObject(WFHorizontalLogDesc.FLD_TRANSACTION, transaction);
	}
	
	public void setUserAt(FocUser user, int i){
		setPropertyObject(WFHorizontalLogDesc.FLD_USER+i, user);
	}

	public FocUser getUserAt(int i){
		return (FocUser) getPropertyObject(WFHorizontalLogDesc.FLD_USER+i);
	}
	
	public Date getDateTimeAt(int i){
		return getPropertyDate(WFHorizontalLogDesc.FLD_DATE_TIME+i);
	}
	
	public void setDateTimeAt(Date date, int i){
		setPropertyDate(WFHorizontalLogDesc.FLD_DATE_TIME+i, date);
	}
	
	public void setCommentAt(String reason, int i){
		setPropertyString(WFHorizontalLogDesc.FLD_COMMENT+i, reason);
	}

	public String getCommentAt(int i){
		return getPropertyString(WFHorizontalLogDesc.FLD_COMMENT+i);
	}
	
	public void setTitleAt(String reason, int i){
		setPropertyString(WFHorizontalLogDesc.FLD_TITLE+i, reason);
	}

	public String getTitleAt(int i){
		return getPropertyString(WFHorizontalLogDesc.FLD_TITLE+i);
	}
	
	public void setStageAt(WFStage stage, int i){
		setPropertyObject(WFHorizontalLogDesc.FLD_STAGE+i, stage);
	}

	public WFStage getStageAt(int i){
		return (WFStage) getPropertyObject(WFHorizontalLogDesc.FLD_STAGE+i);
	}
	
	public int getWFLogRefAt(int i){
		return getPropertyInteger(WFHorizontalLogDesc.FLD_WF_LOG+i);
	}
	
	public void setWFLogRefAt(int logInt, int i){
		setPropertyInteger(WFHorizontalLogDesc.FLD_WF_LOG+i, logInt);
	}
}
