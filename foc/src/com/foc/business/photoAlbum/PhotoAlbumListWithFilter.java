package com.foc.business.photoAlbum;

import java.io.InputStream;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocListOrder;
import com.foc.list.FocListWithFilter;
import com.foc.list.filter.FilterDesc;
import com.foc.list.filter.FocListFilter;
import com.foc.list.filter.NumCondition;
import com.foc.list.filter.ObjectCondition;
import com.foc.list.filter.StringCondition;
import com.foc.util.Utils;

@SuppressWarnings("serial")
public class PhotoAlbumListWithFilter extends FocListWithFilter {

	private FocObject focObject = null;
	private String    tableName = null;
	private int       objRef    = 0;
	
  public PhotoAlbumListWithFilter() {
    super(PhotoAlbumFilterDesc.getInstance(), new FocLinkSimple(PhotoAlbumDesc.getInstance()));
    getFocListFilter().setActive(this, false);
    setDirectlyEditable(false);
    setDirectImpactOnDatabase(true);
    FocListOrder order = new FocListOrder(PhotoAlbumDesc.FLD_UPLOAD_DATE);
    order.setReverted(true);
    setListOrder(order);
  }
  
  public PhotoAlbumListWithFilter(String tableName, int objRef) {
    this();
    this.objRef    = objRef;
    this.tableName = tableName;
    applyFilterOnTableName(tableName);
    applyFilterOnObjectReference(objRef);
  }

  public PhotoAlbumListWithFilter(FocObject focObject){
  	this();
  	this.focObject = focObject;
  	if(focObject != null && focObject.hasRealReference()){
      this.objRef    = focObject.getReference().getInteger();
      this.tableName = focObject.getThisFocDesc().getStorageName();
      applyFilterOnTableName(focObject.getThisFocDesc().getStorageName());
      applyFilterOnObjectReference(focObject.getReference().getInteger());
  	}
  }

  public PhotoAlbumListWithFilter(String urlKey){
  	this();
  	if(!Utils.isStringEmpty(urlKey)){
      applyFilterOnURLKey(urlKey);
  	}
  }
  
  public void dispose(){
  	super.dispose();
  }
  
  public int getObjectRef(){
  	return focObject != null ? focObject.getReference().getInteger() : objRef;
  }
  
  public String getTableName(){
  	return focObject != null ? focObject.getThisFocDesc().getStorageName() : tableName;
  }
  
  public String getTransactionCode(){
  	return focObject != null ? focObject.code_getCode() : "";
  }

  public void applyFilterOnURLKey(String urlKey){
    if(urlKey != null && !urlKey.isEmpty()){
      FilterDesc filterDesc = getFocListFilter().getThisFilterDesc();
      StringCondition mcCond = (StringCondition) filterDesc.findConditionByFieldPrefix(PhotoAlbumFilterDesc.URL_KEY_CONDITION);
      mcCond.forceToValue((FocListFilter) getFocListFilter(), StringCondition.OPERATION_EQUALS, urlKey);
    }
  }

  public void applyFilterOnTableName(String tableName){
    if(tableName != null && !tableName.isEmpty()){
      FilterDesc filterDesc = getFocListFilter().getThisFilterDesc();
      StringCondition mcCond = (StringCondition) filterDesc.findConditionByFieldPrefix(PhotoAlbumFilterDesc.TABLE_NAME_CONDITION);
      mcCond.forceToValue((FocListFilter) getFocListFilter(), StringCondition.OPERATION_EQUALS, tableName);
    }
  }

  public void applyFilterOnObjectReference(int objRef){
    if(objRef > 0){
      FilterDesc filterDesc = getFocListFilter().getThisFilterDesc();
      NumCondition mcCond = (NumCondition) filterDesc.findConditionByFieldPrefix(PhotoAlbumFilterDesc.OBJECT_REF_CONDITION);
      mcCond.forceToValue((FocListFilter) getFocListFilter(), NumCondition.OPERATOR_EQUALS, objRef, objRef);
    }
  }
  
  public void applyFilterOnUnrelatedObjects(){
  	FilterDesc filterDesc = getFocListFilter().getThisFilterDesc();
  	NumCondition mcCond = (NumCondition) filterDesc.findConditionByFieldPrefix(PhotoAlbumFilterDesc.OBJECT_REF_CONDITION);
    mcCond.forceToValue((FocListFilter) getFocListFilter(), NumCondition.OPERATOR_EQUALS, 0, 0);
  }
  
  public void applyFilterOnDocumentType(DocumentType type){
  	FilterDesc filterDesc = getFocListFilter().getThisFilterDesc();
  	ObjectCondition mcCond = (ObjectCondition) filterDesc.findConditionByFieldPrefix(PhotoAlbumFilterDesc.CONDITION_TYPE);
    mcCond.forceToValue((FocListFilter) getFocListFilter(), NumCondition.OPERATOR_EQUALS, type);
  }

	public FocObject getFocObject() {
		return focObject;
	}
  
	public PhotoAlbum addPhotoAlbum(String fileName, InputStream inputStream){
		PhotoAlbum photoAlbum = (PhotoAlbum) newEmptyItem();
		if(photoAlbum != null){
		  photoAlbum.setImageName(fileName);
		  if(Globals.getApp() != null){
		  	photoAlbum.setCreationUser(Globals.getApp().getUser_ForThisSession());
		  	photoAlbum.setUploadDate(Globals.getApp().getSystemDate());
		  }
	  	photoAlbum.setTransactionCode(getTransactionCode());
	  	photoAlbum.fillURLKey();
	  	
	  	photoAlbum.setTableName(getTableName());
	  	photoAlbum.setObjectRef(getObjectRef());
		  
	  	photoAlbum.validate(true);
		  add(photoAlbum);
//		  photoAlbum.setImage(inputStream);
		  photoAlbum.setImageCloud(inputStream, Globals.getApp().getCloudStorageDirectory(), true, fileName);
		  photoAlbum.validate(true);
//		  validate(true);
		}
	  return photoAlbum;
	}
}