package com.foc.business.photoAlbum;

import com.foc.desc.FocModule;

public class PhotoAlbumManagmentModule extends FocModule{

  public PhotoAlbumManagmentModule(){
  }
  
  @Override
  public void declareFocObjectsOnce() {
  	declareFocDescClass(PhotoAlbumConfigDesc.class);
  	declareFocDescClass(PhotoAlbumAppGroup.class);
    declareFocDescClass(PhotoAlbumDesc.class);
    declareFocDescClass(PhotoAlbumAccessDesc.class);
    declareFocDescClass(DocumentTypeDesc.class);
    declareFocDescClass(DocTypeAccessDesc.class);
  }
  
  private static PhotoAlbumManagmentModule module = null;
  public static PhotoAlbumManagmentModule getInstance(){
    if(module == null){
      module = new PhotoAlbumManagmentModule();
    }
    return module;
  }
}
