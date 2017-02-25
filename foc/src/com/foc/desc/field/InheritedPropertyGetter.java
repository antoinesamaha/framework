package com.foc.desc.field;

import com.foc.desc.FocObject;
import com.foc.property.FProperty;

public interface InheritedPropertyGetter {
	public FProperty getInheritedProperty(FocObject object, FProperty property);
}
