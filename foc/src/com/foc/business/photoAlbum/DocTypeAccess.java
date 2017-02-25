package com.foc.business.photoAlbum;

import com.foc.admin.DocRightsGroup;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class DocTypeAccess extends FocObject{

	public DocTypeAccess(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}

	public PhotoAlbum getPhotoAlbum(){
  	return (PhotoAlbum) getPropertyObject(PhotoAlbumAccessDesc.FLD_PHOTO_ALBUM);
  }
	
	public void setDocumentType(DocumentType documentType){
  	setPropertyObject(DocTypeAccessDesc.FLD_DOCUMENT_TYPE, documentType);
  }
	
	public DocRightsGroup getDocRightsGroup(){
  	return (DocRightsGroup) getPropertyObject(DocTypeAccessDesc.FLD_DOC_RIGHTS_GROUP);
  }
	
	public void setDocRightsGroup(DocRightsGroup docRightsGroup){
  	setPropertyObject(DocTypeAccessDesc.FLD_DOC_RIGHTS_GROUP, docRightsGroup);
  }
	
	public int getAccessRight(){
		return getPropertyMultiChoice(DocTypeAccessDesc.FLD_ACCESS_RIGHT);
	}	

	public void setAccessRightType(int accessRight){
		setPropertyMultiChoice(DocTypeAccessDesc.FLD_ACCESS_RIGHT, accessRight);
	}
}
