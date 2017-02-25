package com.foc.dataWrapper;

import java.util.ArrayList;

import com.foc.desc.FocObject;

public interface ITableTreeDelegate {

	public ArrayList<String> newVisibleColumnIds();
	public void listListenerCall_BeforeObjectReferenceSet(FocObject focObj);
}
