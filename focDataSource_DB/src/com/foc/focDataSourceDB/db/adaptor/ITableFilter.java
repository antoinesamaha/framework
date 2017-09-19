package com.foc.focDataSourceDB.db.adaptor;

public interface ITableFilter {
	public boolean isIncluded(String tableName);
	public void dispose();
}
