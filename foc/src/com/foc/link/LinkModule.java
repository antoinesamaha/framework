package com.foc.link;

import com.foc.desc.FocModule;

public class LinkModule extends FocModule {

	@Override
	public void declareFocObjectsOnce() {
		declareFocDescClass(FocLinkOutBoxDesc.class);
		declareFocDescClass(FocLinkOutBoxDetailDesc.class);
		declareFocDescClass(FocLinkOutRightsDesc.class);
		declareFocDescClass(FocLinkOutRightsDetailsDesc.class);
		declareFocDescClass(FocLinkInRightsDesc.class);
		declareFocDescClass(FocLinkInRightsDetailsDesc.class);
	}
	
	//--------------------------------------------------------------
	// STATIC INSTANCE
	//--------------------------------------------------------------
	
  private static LinkModule instance = null;
  
  public static LinkModule getInstance(){
  	if(instance == null){
  		instance = new LinkModule();
  	}
  	return instance;
  }
}
