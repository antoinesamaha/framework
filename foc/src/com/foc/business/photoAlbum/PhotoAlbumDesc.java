package com.foc.business.photoAlbum;

import java.util.HashMap;

import com.foc.SrvConst_ServerSide;
import com.foc.admin.FocUser;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FCloudStorageField;
import com.foc.desc.field.FDateField;
import com.foc.desc.field.FIntField;
import com.foc.desc.field.FMultipleChoiceStringField;
import com.foc.desc.field.FObjectField;
import com.foc.desc.field.FReferenceField;
import com.foc.list.FocList;
import com.foc.util.Utils;

public class PhotoAlbumDesc extends FocDesc {

  public static final int FLD_IMAGE                   = 1;
  public static final int FLD_IMAGE_NAME              = 2;
  public static final int FLD_TABLE_NAME              = 3;
  public static final int FLD_OBJECT_REF              = 4;
  public static final int FLD_UPLOAD_DATE             = 5;
  public static final int FLD_DESCRIPTION             = 6;
  public static final int FLD_PHOTO_ALBUM_ACCESS_LIST = 7;
  public static final int FLD_CREATION_USER           = 8;
  public static final int FLD_TRANSACTION_CODE        = 9;
  public static final int FLD_DOCUMENT_TYPE           = 10;
  public static final int FLD_ALLOWED_GROUPS          = 11;
  public static final int FLD_URL_KEY                 = 12;
  public static final int FLD_KEYWORD                 = 13;
  
  public static final String KEYWORD_BEFORE_FIX    = "Before Fix";
  public static final String KEYWORD_AFTER_FIX     = "After Fix";
	public static final String KEYWORD_UNDER_PROCESS = "Under Process";
	
  public static final String DB_TABLE_NAME = "PHOTO_ALBUM";
  
  public static final String FNAME_DOCUMENT_TYPE = "DOCUMENT_TYPE";
  public static final String FNAME_OBJECT_REF    = "OBJECT_REF";
  public static final String FNAME_TABLE_NAME    = "TABLE_NAME";
  public static final String FNAME_URL_KEY       = "URL_KEY";
  
  public PhotoAlbumDesc(){
    super(PhotoAlbum.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);
    addReferenceField();
    
    FCloudStorageField imageTestField = new FCloudStorageField("IMAGE_CLOUD", "ImageCloud", FLD_IMAGE, false, FLD_IMAGE_NAME);
    addField(imageTestField);
    
    FStringField imageName = new FStringField("IMAGE_NAME", "Image Name", FLD_IMAGE_NAME, false, 100);
    addField(imageName);
    
    FStringField allowedGroups = new FStringField("ALLOWED_GROUPS", "Allowed Groups", FLD_ALLOWED_GROUPS, false, 500);
    addField(allowedGroups);
    
    FStringField tableName = new FStringField(FNAME_TABLE_NAME, "Image Name", FLD_TABLE_NAME, false, 30);
    addField(tableName);
    
    FIntField tableRef = new FIntField(FNAME_OBJECT_REF, "Ref", FLD_OBJECT_REF, false, FReferenceField.LEN_REFERENCE);
    addField(tableRef);
    
    FDateField uploadDate = new FDateField("UPLOAD_DATE", "Upload date", FLD_UPLOAD_DATE, false);
    uploadDate.setAllwaysLocked(true);
    addField(uploadDate);
    
    FStringField cFld = new FStringField("DESCRIPTION", "Description", FLD_DESCRIPTION, false, 500);
    addField(cFld);
    
    cFld = new FStringField("TRANSACTION_CODE", "Transaction Code", FLD_TRANSACTION_CODE, false, 25);
    addField(cFld);
    
    FObjectField objFld = new FObjectField("CREATION_USER", "Creation User", FLD_CREATION_USER, FocUser.getFocDesc());
    objFld.setAllwaysLocked(true);
//    objFld.setLockValueAfterCreation(true);
    addField(objFld);
    
    objFld = new FObjectField(FNAME_DOCUMENT_TYPE, "Document Type", FLD_DOCUMENT_TYPE, DocumentTypeDesc.getInstance());
    addField(objFld);
    
    FStringField focFld = new FStringField(FNAME_URL_KEY, "URL Key", FLD_URL_KEY, false, 200);
    addField(focFld);
    
    FMultipleChoiceStringField multipleChoiceField = new FMultipleChoiceStringField("KEYWORD", "Keyword", FLD_KEYWORD, false, 3);
    multipleChoiceField.addChoice(KEYWORD_AFTER_FIX);
    multipleChoiceField.addChoice(KEYWORD_BEFORE_FIX);
    multipleChoiceField.addChoice(KEYWORD_UNDER_PROCESS);
    addField(multipleChoiceField);
  }
  
  public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, PhotoAlbumDesc.class);    
  }
  
  public static FocList getList(int mode){
    return getInstance().getFocList(mode);
  }
  
  @Override
  public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(false);
    list.setDirectImpactOnDatabase(true);
    return list;
  }
  
  @Override
  public FocList getCustomFocListForMobile(HashMap<String, String> extraParams, String key) {
  	FocList focList = null;
  	
  	if(extraParams != null){
  		String refStr     = extraParams.get(SrvConst_ServerSide.KEY_REFERENCE);
  		String storageStr = extraParams.get(SrvConst_ServerSide.KEY_STORAGE);
  		if(refStr != null && storageStr != null && Utils.isInteger(refStr)){
  			int inspectionAreaRef_Int = Utils.parseInteger(refStr, 0);
  		  
  			FocDesc focDesc = FocDesc.getInstance(storageStr, null);
  			if(focDesc != null && focDesc.getFocList(FocList.LOAD_IF_NEEDED) != null){
  				FocObject inspectionArea = focDesc.getFocList(FocList.LOAD_IF_NEEDED).searchByReference(inspectionAreaRef_Int);
  				focList = new PhotoAlbumListWithFilter(inspectionArea);
  				focList.loadIfNotLoadedFromDB();
  			}
  		}
  	}
  	return focList;
  }
}