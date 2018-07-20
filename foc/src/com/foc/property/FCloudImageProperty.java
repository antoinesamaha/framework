
package com.foc.property;

import java.sql.Blob;

import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FCloudImageProperty extends FCloudStorageProperty{

	public FCloudImageProperty(FocObject focObj, int fieldID, Blob defaultValue) {
		super(focObj, fieldID, defaultValue);
	}

}
