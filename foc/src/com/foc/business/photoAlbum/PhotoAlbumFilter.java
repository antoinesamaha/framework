package com.foc.business.photoAlbum;

import com.foc.desc.FocConstructor;
import com.foc.list.filter.FocListFilter;
import com.foc.list.filter.FocListFilterBindedToList;
import com.foc.list.filter.ObjectCondition;

@SuppressWarnings("serial")
public class PhotoAlbumFilter extends FocListFilterBindedToList{

  public PhotoAlbumFilter(FocConstructor constr) {
    super(constr);
    newFocProperties();
    setFilterLevel(FocListFilter.LEVEL_DATABASE);
  }
  
  public void setTableName(int operation, PhotoAlbum photoAlbum, boolean lock){
    ObjectCondition tableNameCondition = (ObjectCondition) findFilterCondition(PhotoAlbumFilterDesc.TABLE_NAME_CONDITION);
    if(lock){
      tableNameCondition.forceToValue(this, operation, photoAlbum);
    }else{
      tableNameCondition.setToValue(this, operation, photoAlbum);  
    }
  }
  
  public void setObjectReference(int operation, PhotoAlbum photoAlbum, boolean lock){
    ObjectCondition objectRefCondition = (ObjectCondition) findFilterCondition(PhotoAlbumFilterDesc.OBJECT_REF_CONDITION);
    if(lock){
      objectRefCondition.forceToValue(this, operation, photoAlbum);
    }else{
      objectRefCondition.setToValue(this, operation, photoAlbum);  
    }
  }
  
  public void setDocumentType(int operation, DocumentType type, boolean lock){
    ObjectCondition tableNameCondition = (ObjectCondition) findFilterCondition(PhotoAlbumFilterDesc.CONDITION_TYPE);
    if(lock){
      tableNameCondition.forceToValue(this, operation, type);
    }else{
      tableNameCondition.setToValue(this, operation, type);  
    }
  }
}