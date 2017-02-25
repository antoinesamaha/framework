package com.foc.focDataSourceDB.db;

import java.util.HashMap;
import java.util.Iterator;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;

public class VisitedObjectsContainer {
	private FocDesc focDesc = null;
	private boolean withRef = false;
	private HashMap<FocObject, FocObject> mapObj = null;
	private HashMap<Integer, FocObject>   mapRef = null;
	
	public VisitedObjectsContainer(FocDesc focDesc){
		this.focDesc = focDesc;
		if(focDesc.getFieldByID(FField.REF_FIELD_ID) != null){
			withRef = true;
		}
		if(withRef){
			mapRef = new HashMap<Integer, FocObject>();
		}else{
			mapObj = new HashMap<FocObject, FocObject>();
		}
	}
	
	public void dispose(){
		focDesc = null;
	}

	public void push(FocObject obj){
		if(withRef){
			mapRef.put(obj.getReference().getInteger(), obj);
		}else{
			mapObj.put(obj, obj);
		}
	}
	
	public boolean contains(FocObject obj){
		boolean contains = false;
		if(withRef){
			contains = mapRef.get(obj.getReference().getInteger()) != null;
		}else{
			contains = mapObj.get(obj) != null;
		}
		return contains;
	}
	
	public Iterator<FocObject> valuesIterator(){
		Iterator<FocObject> iter = null;
		if(withRef){
			iter = mapRef.values().iterator();
		}else{
			iter = mapObj.values().iterator();
		}
		return iter;
	}
}
