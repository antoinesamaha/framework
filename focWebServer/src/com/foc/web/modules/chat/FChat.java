package com.foc.web.modules.chat;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import com.foc.Globals;
import com.foc.access.AccessSubject;
import com.foc.admin.FocUser;
import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocDate;
import com.foc.annotations.model.fields.FocForeignEntity;
import com.foc.annotations.model.fields.FocReference;
import com.foc.annotations.model.fields.FocString;
import com.foc.annotations.model.fields.FocTableName;
import com.foc.annotations.model.fields.FocTime;
import com.foc.annotations.model.predefinedFields.FocCOMPANY;
import com.foc.business.workflow.WFOperator;
import com.foc.business.workflow.WFSite;
import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;
import com.foc.list.FocList;

@SuppressWarnings("serial")
@FocEntity
@FocCOMPANY
public class FChat extends PojoFocObject {	
	
	public static final String DBNAME = "FChat";
	
	@FocForeignEntity(table = "FUSER", cascade = false, cachedList = false)
	public static final String FIELD_Sender = "Sender";

	@FocDate()
	public static final String FIELD_Date = "Date";

	@FocTime()
	public static final String FIELD_Time = "Time";

	@FocString(size = 4000)
	public static final String FIELD_Message = "Message";

	@FocTableName()
	public static final String FIELD_SubjectTableName = "SubjectTableName";

	@FocReference()
	public static final String FIELD_SubjectReference = "SubjectReference";
	
	public static final String FIELD_ReceiverList = FChatReceiver.DBNAME+"_LIST";

	public FChat(FocConstructor constr) {
		super(constr);
		
		setSender(Globals.getApp().getUser_ForThisSession());
		setDate(Globals.getApp().getSystemDate());
		Time time = new Time(Globals.getApp().getSystemDate().getTime());
		setTime(time);
	}
	
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}

	@Override
	public int getPropertyAccessRight(int fieldID) {
  	int access = super.getPropertyAccessRight(fieldID);
  	FField fld = getThisFocDesc().getFieldByID(fieldID);
  	String fName = fld.getName();
  	if(    fName.equals(FIELD_Sender)
  			|| fName.equals(FIELD_Date)
  			|| fName.equals(FIELD_Time)) {
  		access = PROPERTY_RIGHT_READ;
  	}
  	return access;
  }
  	
	public FocUser getSender() {
		return (FocUser) getPropertyObject(FIELD_Sender);
	}

	public void setSender(FocUser value) {
		setPropertyObject(FIELD_Sender, value);
	}

	public Date getDate() {
		return getPropertyDate(FIELD_Date);
	}

	public void setDate(Date value) {
		setPropertyDate(FIELD_Date, value);
	}

	public Time getTime() {
		return getPropertyTime(FIELD_Time);
	}

	public void setTime(Time value) {
		setPropertyTime(FIELD_Time, value);
	}

	public String getMessage() {
		return getPropertyString(FIELD_Message);
	}

	public void setMessage(String value) {
		setPropertyString(FIELD_Message, value);
	}

	public String getSubjectTableName() {
		return getPropertyString(FIELD_SubjectTableName);
	}

	public void setSubjectTableName(String value) {
		setPropertyString(FIELD_SubjectTableName, value);
	}

	public long getSubjectReference() {
		return getPropertyLong(FIELD_SubjectReference);
	}

	public void setSubjectReference(long value) {
		setPropertyLong(FIELD_SubjectReference, value);
	}
	
  public FocList getReceiverList() {
  	return getPropertyList(FIELD_ReceiverList);
  }
  
	private void receiversArray_AddUser(ArrayList<FocUser> usersToReceiveArray, FocUser user) {
		if(usersToReceiveArray != null && user != null) {
			if(!usersToReceiveArray.contains(user)) usersToReceiveArray.add(user);
		}
	}
	
	private void addSiteUsersToReceivers(ArrayList<FocUser> usersToReceiveArray, WFSite site) {
  	FocList opList = site.getOperatorList();
  	for(int i=0; i<opList.size(); i++) {
  		WFOperator operator = (WFOperator) opList.getFocObject(i);
  		FocUser user = operator.getUser();

  		if(!user.equalsRef(getSender())) {
  			receiversArray_AddUser(usersToReceiveArray, getSender()) ;
  		}
  	}
	}
	
  public void addReceipients(FocObject focObject) {
  	ArrayList<FocUser> usersToReceiveArray = new ArrayList<FocUser>();
  	
  	if(focObject.workflow_IsWorkflowSubject()){
  	  IWorkflow workflow = (IWorkflow) focObject;
  	  if(workflow.iWorkflow_getWorkflow() != null){ 
  	    WFSite site = workflow.iWorkflow_getWorkflow().getArea();

  	    addSiteUsersToReceivers(usersToReceiveArray, site);

  	    AccessSubject subject = getFatherSubject();
  	    if(subject instanceof FChatList) {
  	    	FChatList chatList = (FChatList) subject;
  	    	for(int i=0; i<chatList.size(); i++) {
  	    		FChat chat = (FChat) chatList.getFocObject(i);
  	    		receiversArray_AddUser(usersToReceiveArray, chat.getSender());
  	    	}
  	    }
  	    
  	  }
  	}
  	
  	for(int i=0; i<usersToReceiveArray.size(); i++) {
  		FocUser user = usersToReceiveArray.get(i);
  		FChatReceiver receiver = (FChatReceiver) getReceiverList().newEmptyItem();
  		receiver.setReceiver(user);
  		getReceiverList().add(receiver);
  	}
  }
}
