package com.foc.pivot;

import com.foc.desc.FocObject;

public interface IPivotTableHandler {
	public boolean addTitles(FPivotTitleDescriptionSet titleSet, FocObject nativeObject, FPivotBreakdown breakdown);
	public boolean copyPropertiesFromRawObjectToRawObject(FocObject tarNative, FocObject srcNative);
}
