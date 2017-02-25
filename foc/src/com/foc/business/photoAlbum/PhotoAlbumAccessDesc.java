package com.foc.business.photoAlbum;

import com.foc.admin.DocRightsGroupDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;

public class PhotoAlbumAccessDesc extends FocDesc{

  public static final int FLD_PHOTO_ALBUM       = 1;
  public static final int FLD_ACCESS_RIGHT      = 2;
  public static final int FLD_DOC_RIGHTS_GROUP  = 3;
  
  public static final int READ_ONLY    = 0;
  public static final int READ_WRITE   = 1;
  
  public static final String DB_TABLE_NAME = "PHOTO_ALBUM_ACCESS";
  
  public PhotoAlbumAccessDesc() {
    super(PhotoAlbumAccess.class, DB_RESIDENT, DB_TABLE_NAME, false);

    addReferenceField();

    FObjectField objFld = new FObjectField("PHOTO_ALBUM", "Photo ALbum", FLD_PHOTO_ALBUM, PhotoAlbumDesc.getInstance(), this, PhotoAlbumDesc.FLD_PHOTO_ALBUM_ACCESS_LIST);
    objFld.setWithList(false);
    addField(objFld);
    
    objFld = new FObjectField("DOC_RIGHTS_GROUP", "Doc Rights Group", FLD_DOC_RIGHTS_GROUP, DocRightsGroupDesc.getInstance());
    addField(objFld);
    
    FMultipleChoiceField fMultipleField = new FMultipleChoiceField("ACCESS_RIGHT", "Access Right", FLD_ACCESS_RIGHT, false, 2);
    fMultipleField.addChoice(READ_ONLY , "Read Only");
    fMultipleField.addChoice(READ_WRITE, "Read Write");
    addField(fMultipleField);
  }
  
  public FocList newFocList(){
	  FocList list = super.newFocList();
	  list.setDirectlyEditable(false);
	  list.setDirectImpactOnDatabase(true);
	  return list;
	}
  
  public static PhotoAlbumAccessDesc getInstance(){
    return (PhotoAlbumAccessDesc) getInstance(DB_TABLE_NAME, PhotoAlbumAccessDesc.class);
  }
}
