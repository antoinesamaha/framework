package com.foc.desc.field;

import java.sql.Blob;

import com.foc.desc.FocObject;
import com.foc.property.FCloudStorageProperty;
import com.foc.property.FProperty;


public class FCloudStorageField extends FBlobMediumField {

	private int fileNameFieldID = FField.NO_FIELD_ID;
	
	public FCloudStorageField(String name, String title, int id, boolean key, int fileNameFieldID) {
		super(name, title, id, key);
		setDBResident(false);
		
		this.fileNameFieldID = fileNameFieldID;
	}
	
	public int getFileNameFieldID(){
		return fileNameFieldID;
	}
		
	@Override
	public FProperty newProperty_ToImplement(FocObject masterObj, Object defaultValue) {
		return new FCloudStorageProperty(masterObj, getID(), (Blob) defaultValue);
	}
	
}
