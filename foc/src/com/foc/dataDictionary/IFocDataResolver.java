package com.foc.dataDictionary;

import java.util.ArrayList;

import com.foc.shared.dataStore.IFocData;

public interface IFocDataResolver {
	Object getValue(IFocData focData, ArrayList<String> arguments);
}
