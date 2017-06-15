package com.foc.business.photoAlbum;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FCompanyField;
import com.foc.list.FocList;

public class PhotoAlbumConfigDesc extends FocDesc {
  public static final int FLD_SINGLE_GROUP = 1;
  public static final int FLD_USE_KEYWORDS = 2;
  
  public static final String DB_TABLE_NAME = "PHOTO_ALBUM_CONFIG";
  
  public PhotoAlbumConfigDesc(){
    super(PhotoAlbumConfig.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);

    addReferenceField();
    
    FCompanyField companyField = new FCompanyField(false, true);
    addField(companyField);
    
    FBoolField oFld = new FBoolField("USE_SINGLE_GROUP", "Single Group of rights", FLD_SINGLE_GROUP, false);
    addField(oFld);

    oFld = new FBoolField("USE_KEYWORDS", "Use keywords", FLD_USE_KEYWORDS, false);
    addField(oFld);
  }
  
  //ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE LIST
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  
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
  
  //ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, PhotoAlbumConfigDesc.class);    
  }
}
