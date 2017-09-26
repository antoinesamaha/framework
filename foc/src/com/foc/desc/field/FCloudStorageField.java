package com.foc.desc.field;

import java.sql.Blob;

import com.foc.desc.FocObject;
import com.foc.property.FCloudStorageProperty;
import com.foc.property.FProperty;

public class FCloudStorageField extends FBlobMediumField {

	private int    fileNameFieldID   = FField.NO_FIELD_ID;
	private String fileNameFieldName = null;
	
	public FCloudStorageField(String name, String title, int id, boolean key, int fileNameFieldID) {
		super(name, title, id, key);
		setDBResident(false);
		
		this.fileNameFieldID = fileNameFieldID;
	}
	
	public FCloudStorageField(String name, String title, int id, boolean key, String fileNameFieldString) {
		super(name, title, id, key);
		setDBResident(false);
		
		this.fileNameFieldName = fileNameFieldString;
	}
	
	public int getFileNameFieldID(){
		return fileNameFieldID;
	}
	
	public String getFileNameFieldName(){
		return fileNameFieldName;
	}
		
	@Override
	public FProperty newProperty_ToImplement(FocObject masterObj, Object defaultValue) {
		return new FCloudStorageProperty(masterObj, getID(), (Blob) defaultValue);
	}
	
}
