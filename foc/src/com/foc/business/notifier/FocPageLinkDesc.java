package com.foc.business.notifier;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.list.FocList;

public class FocPageLinkDesc extends FocPageDesc {
  
	public final static int FLD_KEY = FocPageDesc.MAX_NUMBER_OF_FIELDS_RESERVED+1;
	
	public final static String DB_TABLE_NAME = "FOC_PAGE_LINK";
	
	public FocPageLinkDesc() {
    super(FocPageLink.class, DB_TABLE_NAME);
    
    addReferenceField();
    
    FStringField focFld = new FStringField("LINK_KEY", "Link Key", FLD_KEY, false, 200);
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
    return getInstance(DB_TABLE_NAME, FocPageLinkDesc.class);
  }
	
}