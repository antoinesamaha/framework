package com.foc.property;

import java.sql.Blob;

import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FBlobMediumProperty extends FBlobProperty{

	private boolean  uploading = false;
	
  public FBlobMediumProperty(FocObject focObj, int fieldID, Blob defaultValue) {
    super(focObj, fieldID, defaultValue);
  }

  public boolean isUploading(){
  	return uploading;
  }
  
  public void setUploading(boolean uploading){
  	this.uploading = uploading;
  }
}
