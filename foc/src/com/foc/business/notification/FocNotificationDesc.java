package com.foc.business.notification;

import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FDateField;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FIntField;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FObjectField;
import com.foc.desc.field.FTimeField;
import com.foc.list.FocList;

public class FocNotificationDesc extends FocDesc{

	public final static String DB_TABLE_NAME = "FOC_NOTIFICATION";
	
	public final static int FLD_USER                 = 1;
	public final static int FLD_TRANSACTION_FOC_DESC = 2;
	public final static int FLD_REFERENCE            = 3;
	public final static int FLD_MESSAGE              = 4;
	public final static int FLD_DATE                 = 5;
	public final static int FLD_TIME                 = 6;
	public final static int FLD_NOTIFICATION_STATUS  = 7;

	public final static int STATUS_READ   = 0;
	public final static int STATUS_UNREAD = 1;
	
	public FocNotificationDesc(){
    super(FocNotification.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);
    
    addReferenceField();
    addCodeField();
    
    FObjectField fObjectFld = new FObjectField("USER", "User", FLD_USER, FocUser.getFocDesc());
    fObjectFld.setDisplayField(FocUserDesc.FLD_NAME);
    addField(fObjectFld);
    
    FDescFieldStringBased fld = new FDescFieldStringBased("TRANSACTION_FOC_DESC", "Foc Desc", FLD_TRANSACTION_FOC_DESC, false);
		addField(fld);
    
    FIntField intField = new FIntField("REFERENCE", "Reference", FLD_REFERENCE, false, 10);
    addField(intField);
    
    FStringField charField = new FStringField("MESSAGE", "Message", FLD_MESSAGE, false, 500);
    addField(charField);
    
    FDateField dateField  = new FDateField("DATE", "Date", FLD_DATE, false);
    addField(dateField);
    
    FTimeField timeField = new FTimeField("TIME", "Time", FLD_TIME, false);
    addField(timeField);
    
    FMultipleChoiceField multipleChoiceField = new FMultipleChoiceField("NOTIFICATION_STATUS", "Status", FLD_NOTIFICATION_STATUS, false, 2);
    multipleChoiceField.addChoice(STATUS_READ, "Read");
    multipleChoiceField.addChoice(STATUS_UNREAD, "Unread");
    addField(multipleChoiceField);
	}
	
	public static FocList getList(int mode){
  	return getInstance().getFocList(mode);
  }
  
  public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(false);
    list.setDirectImpactOnDatabase(true);
    return list;
  }
	
	public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, FocNotificationDesc.class);
  }
}
