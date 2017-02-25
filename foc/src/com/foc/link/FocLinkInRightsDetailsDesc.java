package com.foc.link;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;

public class FocLinkInRightsDetailsDesc extends FocDesc {

	public static final String DB_TABLE_NAME  = "FOC_LINK_IN_RIGHTS_DETAILS";
	
	public static final int    FLD_TABLE_NAME     = 1;
	public static final int    FLD_FIELD_NAME     = 2;
	public static final int    FLD_INSERT_ALLOWED = 3;
	public static final int    FLD_DELETE_ALLOWED = 4;
	public static final int    FLD_UPDATE_ALLOWED = 5;
	public static final int    FLD_MASTER         = 6;
	
	public FocLinkInRightsDetailsDesc() {
		super(FocLinkInRightsDetails.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);
		
		addReferenceField();
		
		FField ffld = new FDescFieldStringBased("TABLE_NAME", "Table", FLD_TABLE_NAME, true);
		ffld.setLockValueAfterCreation(true);
		addField(ffld);
		
		ffld = new FStringField("FIELD_NAME", "Field Name", FLD_FIELD_NAME, true, 50);
		addField(ffld);
		
		ffld = new FBoolField("INSERT_ALLOWED", "Insert Allowed", FLD_INSERT_ALLOWED, false);
		addField(ffld);
		
		ffld = new FBoolField("DELETE_ALLOWED", "Delete Allowed", FLD_DELETE_ALLOWED, false);
		addField(ffld);
		
		ffld = new FBoolField("UPDATE_ALLOWED", "Update Allowed", FLD_UPDATE_ALLOWED, false);
		addField(ffld);
		
		ffld = new FObjectField("RIGHTS_MASTER", "Rights Master", FLD_MASTER, FocLinkInRightsDesc.getInstance(), this, FocLinkInRightsDesc.FLD_DETAILS_LIST);
		ffld.setKey(true);
		((FObjectField) ffld).setSelectionList(null);
		((FObjectField) ffld).setWithList(false);
		addField(ffld);
	}
	
	
  @Override
  protected void afterConstruction() {
  	super.afterConstruction();

  	FDescFieldStringBased descField = (FDescFieldStringBased) getFieldByID(FLD_TABLE_NAME);
  	descField.fillWithAllDeclaredFocDesc();
  }
  
	public static FocLinkInRightsDetailsDesc getInstance() {
		return (FocLinkInRightsDetailsDesc) getInstance(DB_TABLE_NAME, FocLinkInRightsDetailsDesc.class);    
	}

}
