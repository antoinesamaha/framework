package com.foc.business.photoAlbum;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FFieldPath;
import com.foc.list.filter.FilterDesc;
import com.foc.list.filter.FocDescForFilter;
import com.foc.list.filter.NumCondition;
import com.foc.list.filter.ObjectCondition;
import com.foc.list.filter.StringCondition;

public class PhotoAlbumFilterDesc extends FocDescForFilter{

  public static final String TABLE_NAME_CONDITION = "TABLE_NAME";
  public static final String OBJECT_REF_CONDITION = "REF";
  public static final String URL_KEY_CONDITION    = "URL_KEY";
  public static final String CONDITION_TYPE       = "TYPE";
  
  public PhotoAlbumFilterDesc(){
    super(PhotoAlbumFilter.class, FocDesc.NOT_DB_RESIDENT, "IMAGE_FILTER", true);
  }
  
  @Override
  public FilterDesc getFilterDesc() {
    if(filterDesc == null){
      filterDesc = new FilterDesc(PhotoAlbumDesc.getInstance());

      StringCondition tableNameCondition = new StringCondition(FFieldPath.newFieldPath(PhotoAlbumDesc.FLD_TABLE_NAME), TABLE_NAME_CONDITION);
      filterDesc.addCondition(tableNameCondition);
      
      NumCondition refCondition = new NumCondition(FFieldPath.newFieldPath(PhotoAlbumDesc.FLD_OBJECT_REF), OBJECT_REF_CONDITION);
      filterDesc.addCondition(refCondition);

      StringCondition urlKeyCondition = new StringCondition(FFieldPath.newFieldPath(PhotoAlbumDesc.FLD_URL_KEY), URL_KEY_CONDITION);
      filterDesc.addCondition(urlKeyCondition);

      ObjectCondition typeCondition = new ObjectCondition(FFieldPath.newFieldPath(PhotoAlbumDesc.FLD_DOCUMENT_TYPE), CONDITION_TYPE);
      filterDesc.addCondition(typeCondition);
      
      filterDesc.setNbrOfGuiColumns(1);
    }
    return filterDesc;
  }
  
  private static PhotoAlbumFilterDesc focDesc = null;
  public static PhotoAlbumFilterDesc getInstance() {
    if(focDesc == null){
      focDesc = new PhotoAlbumFilterDesc();
    }
    return focDesc;
  } 
}