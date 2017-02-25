package com.foc.dataSource.store;

import com.foc.Globals;
import com.foc.shared.dataStore.AbstractDataStore;

public class DataStore extends AbstractDataStore {
	public static DataStore getInstance(){
		return Globals.getApp() != null ? Globals.getApp().getDataStore() : null;
	}
}
