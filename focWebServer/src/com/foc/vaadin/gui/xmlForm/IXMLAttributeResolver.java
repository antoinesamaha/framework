package com.foc.vaadin.gui.xmlForm;

import com.foc.dataDictionary.FocDataDictionary;
import com.foc.shared.dataStore.IFocData;

public interface IXMLAttributeResolver {
	public FocDataDictionary getFocDataDictionary(boolean createIfNeeded);
	public IFocData getFocData();
}
