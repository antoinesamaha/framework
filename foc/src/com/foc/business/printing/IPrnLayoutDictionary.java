package com.foc.business.printing;

import java.util.Iterator;

import com.foc.shared.prnLayout.PrnLayoutKey;
import com.foc.shared.xmlView.XMLViewKey;

public interface IPrnLayoutDictionary {
	public Iterator<PrnLayoutKey> newIterator();
}
