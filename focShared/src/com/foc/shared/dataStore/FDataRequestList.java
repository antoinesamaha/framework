package com.foc.shared.dataStore;

import java.util.ArrayList;

public class FDataRequestList<O extends FDataRequest_Abstract> {
	
	private ArrayList<O> arrayList = null;

	private int nextSerialNumber = 1;
	
	public FDataRequestList(){
		arrayList = new ArrayList<O>();
	}
	
	public int size(){
		return arrayList != null ? arrayList.size() : 0;
	}
	
	public O get(int i){
		return (O) (arrayList != null ? arrayList.get(i) : 0);
	}
	
	public void addDataRequest(O o){
		o.setSerialNumber(nextSerialNumber++);
		if(arrayList != null) arrayList.add(o);
	}

}
