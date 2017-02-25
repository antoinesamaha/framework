package com.foc.admin.defaultappgroup;

import com.foc.desc.FocDesc;

public class DefaultAppGroupDesc extends FocDesc {
	
	public DefaultAppGroupDesc(){
		super(DefaultAppGroup.class, false, "DEFAULT_APP_GROUP", false);
	}
	
  //ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  private static FocDesc focDesc = null;
  
  public static FocDesc getInstance() {
    if (focDesc==null){
      focDesc = new DefaultAppGroupDesc();
    }
    return focDesc;
  } 

}
