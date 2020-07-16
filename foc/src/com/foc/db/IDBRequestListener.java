package com.foc.db;

import java.util.Date;

public interface IDBRequestListener {


	void newRequestInsert(String tableName, Date date);
	void newRequestUpdate(String tableName, Date date);
	void newRequestDelete(String tableName, Date date);


	

}
