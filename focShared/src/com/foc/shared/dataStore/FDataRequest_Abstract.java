package com.foc.shared.dataStore;

import com.foc.shared.dataStore.IDataStoreConst;
import com.foc.shared.json.B01JsonBuilder;

public abstract class FDataRequest_Abstract implements IDataStoreConst {
	
	private int       serialNumber = 0;
	private int       command      = COMMAND_QUERY;
	private String    dataKey      = null;
	
	public abstract void parseJson(StringBuffer buffer);
	public abstract void toJson(B01JsonBuilder buffer);
	
	public FDataRequest_Abstract(){
		
	}
	
	public FDataRequest_Abstract(int serialNumber, int command, String dataKey){
		this.serialNumber = serialNumber;
		this.dataKey      = dataKey;
		this.command      = command;
	}
	
	public void dispose(){
		dataKey     = null;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	
	public void setJsonKeyValue(String key, String value){
		if(key.equals(JSON_KEY_SERIAL_NUMBER)){
			setSerialNumber(Integer.valueOf(value));
		}else if(key.equals(JSON_KEY_COMMAND)){
			setCommand(Integer.valueOf(value));
		}else if(key.equals(JSON_KEY_DATA_KEY)){
			setDataKey(value);
		} 
	}
}
