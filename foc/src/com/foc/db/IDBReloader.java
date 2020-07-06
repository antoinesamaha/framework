package com.foc.db;

import java.util.Date;
import java.util.Set;

public interface IDBReloader {

	void reloadTables(Set<String> tableNames);

	void addToMap(String tableName, Date date);
	
	void run();
}
