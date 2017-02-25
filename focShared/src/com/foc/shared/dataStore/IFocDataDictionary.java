package com.foc.shared.dataStore;


public interface IFocDataDictionary {

	public String resolveExpression(IFocData focData, String expression, boolean replaceUnknownWithEmptyString);
}
