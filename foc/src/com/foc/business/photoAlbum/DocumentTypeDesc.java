package com.foc.business.photoAlbum;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.list.FocList;

public class DocumentTypeDesc extends FocDesc{

	public static final int FLD_CAPTION                   = 1; 
	public static final int FLD_DOCUMENT_TYPE_ACCESS_LIST = 2;
	
	public static final String DB_TABLE_NAME = "DOCUMENT_TYPE";
	
	public static final String SELECT_ALL = "All";
  
  public DocumentTypeDesc() {
    super(DocumentType.class, DB_RESIDENT, DB_TABLE_NAME, false);

    addReferenceField();
    
    FStringField nameField = addNameField();
    nameField.setSize(100);
    
    FStringField cFld = new FStringField("CAPTION", "Caption", FLD_CAPTION, false, 200);
    addField(cFld);
  }
  
  public FocList newFocList(){
	  FocList list = super.newFocList();
	  list.setDirectlyEditable(false);
	  list.setDirectImpactOnDatabase(true);
	  return list;
	}
  
  public static DocumentTypeDesc getInstance(){
    return (DocumentTypeDesc) getInstance(DB_TABLE_NAME, DocumentTypeDesc.class);
  }
}
