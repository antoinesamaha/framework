package com.foc.desc;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;

@SuppressWarnings("serial")
public class FocDescMap_ByFocObjectClassName extends HashMap<String, FocDesc>{
	
	public FocDescMap_ByFocObjectClassName(){
		
	}
	
	public void dispose(){
		Iterator<FocDesc> iter = values().iterator();
		while(iter != null && iter.hasNext()){
			FocDesc focDesc = iter.next();
			if(focDesc != null){
				focDesc.dispose();
			}
		}
		clear();
	}
	
	public static FocDescMap_ByFocObjectClassName getInstance(){
		return (Globals.getApp() != null) ? Globals.getApp().getFocDescMap_ByFocObjectClassName() : null;
	}

  @Override
  public FocDesc put(String arg0, FocDesc arg1) {
    return super.put(arg0, arg1);
  }	
}
