package com.foc.desc;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.shared.dataStore.AbstractDataStore;

@SuppressWarnings("serial")
public class FocDescMap extends HashMap<String, FocDesc>{
	
	public FocDescMap(){
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
	
	public static FocDescMap getInstance(){
		return (Globals.getApp() != null) ? Globals.getApp().getFocDescMap() : null;
	}

  @Override
  public FocDesc put(String arg0, FocDesc arg1) {
    return super.put(arg0, arg1);
  }

  public FocDesc put(String tableName, int type, FocDesc arg1) {
    return super.put(buildKey(tableName, type), arg1);
  }

  public FocDesc get(String arg0) {
    return super.get(arg0);
  }
  
  public FocDesc get(String tableName, int type) {
    return super.get(buildKey(tableName, type));
  }

  private String buildKey(String tableName, int type){
  	return type != AbstractDataStore.TRANSACTION_TYPE_NONE ? tableName+"|"+type : tableName;
  }
}
