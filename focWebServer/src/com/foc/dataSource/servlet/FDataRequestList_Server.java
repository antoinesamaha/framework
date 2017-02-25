package com.foc.dataSource.servlet;

import com.foc.shared.dataStore.FDataRequestList;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.shared.json.B01JsonSaxParser;

public class FDataRequestList_Server extends FDataRequestList<FDataRequest_Server>{

	public FDataRequest_Server newDataRequest(){
		FDataRequest_Server dr = new FDataRequest_Server();
		addDataRequest(dr);
		return dr;
	}
	
	public void dispose(){
	}

	public String toJson(){
		B01JsonBuilder jSonBuffer = new B01JsonBuilder();
		toJson(jSonBuffer);
		return jSonBuffer.toString();
	}
	
	public void toJson(B01JsonBuilder jSonBuffer){
		jSonBuffer.beginList();
		
		for(int i=0; i<size(); i++){
			FDataRequest_Server dataRequest_Server = get(i);
			dataRequest_Server.toJson(jSonBuffer);
		}
		
		jSonBuffer.endList();		
	}
	
	public void parseJson(String json){
		B01JsonCallBack_Server callBack = new B01JsonCallBack_Server(this);
		B01JsonSaxParser parser = new B01JsonSaxParser(json, callBack);
		parser.parse();
		callBack.dispose();
		callBack = null;
		parser.dispose();
		parser = null;
	}
}
