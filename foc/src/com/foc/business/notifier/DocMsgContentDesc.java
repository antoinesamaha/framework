package com.foc.business.notifier;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FBlobStringField;
import com.foc.list.FocList;

public class DocMsgContentDesc extends FocPageDesc{

	public final static int FLD_XML = FocPageDesc.MAX_NUMBER_OF_FIELDS_RESERVED+1;
	
	public final static String DB_TABLE_NAME = "DOC_MSG_CONTENT";
	
	public DocMsgContentDesc() {
    super(DocMsgContent.class, DB_TABLE_NAME);
    
    addReferenceField();
    
    FBlobStringField focFld = new FBlobStringField("XML", "XML Content", FLD_XML, false, 300, 5);
    addField(focFld);
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
    return getInstance(DB_TABLE_NAME, DocMsgContentDesc.class);
  }
}
