package com.foc.shared.dataStore;

public class DataKey {
	
	public static String getKey_ForTable(String table){
		return table+"|ALL";
	}
	
	public static String getKey_ForObject(String table, int ref){
		return table+"|"+ref;
	}
}
