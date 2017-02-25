package com.foc.db.sync;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.db.sync.model.SyncTable;
import com.foc.desc.FocDesc;

public class SyncManager {

	private HashMap<String, SyncTable> syncTableMap = null;
	
	public SyncManager(){
	}

	public void dispose(){
		if(syncTableMap != null){
			Iterator iter = syncTableMap.values().iterator();
			while(iter != null && iter.hasNext()){
				SyncTable st = (SyncTable) iter.next();	
				st.dispose();
			}
			syncTableMap.clear();
			syncTableMap = null;
		}
	}

	public HashMap<String, SyncTable> getSyncTableArray(boolean create){
		if(syncTableMap == null && create){
			syncTableMap = new HashMap<String, SyncTable>();

			Iterator<IFocDescDeclaration> iter = Globals.getApp().getFocDescDeclarationIterator();
	    while(iter != null && iter.hasNext()){
	    	IFocDescDeclaration focDescDeclaration = iter.next();
	    	if(focDescDeclaration != null){
		    	FocDesc focDesc =  focDescDeclaration.getFocDescription();
		    	if(focDesc != null && focDesc.isDbResident()){
		    		SyncTable syncTable = new SyncTable(focDesc);
		    		syncTableMap.put(focDesc.getStorageName(), syncTable);
		    	}
	    	}
	    }
		}
		return syncTableMap;
	}
	
	public void fillTableListFromDatabase(){
		HashMap<String, SyncTable> syncTableMap = getSyncTableArray(true);
		
		Iterator<SyncTable> iter = syncTableMap.values().iterator();
		while(iter != null && iter.hasNext()){
			SyncTable syncTable = iter.next();
			syncTable.fillFromDatabase();
		}
	}
	
	public boolean writeToFile(){
		boolean error = false;
		
		return error;
	}
}
