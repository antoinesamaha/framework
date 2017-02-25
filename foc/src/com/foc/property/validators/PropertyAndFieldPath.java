package com.foc.property.validators;

import com.foc.desc.field.FFieldPath;
import com.foc.property.FProperty;

public class PropertyAndFieldPath {
	private FProperty  property  = null;
	private FFieldPath fieldPath = null;
	
	public PropertyAndFieldPath(FFieldPath fieldPath, FProperty property){
		this.fieldPath = fieldPath;
		this.property  = property;
	}

	public FProperty getProperty() {
		return property;
	}

	public FFieldPath getFieldPath() {
		return fieldPath;
	}
}
