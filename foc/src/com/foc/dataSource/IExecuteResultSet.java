package com.foc.dataSource;


public interface IExecuteResultSet {

	public void executeResultSet(int rowIndex, int columnIndex, String value);
	public void afterResultSetFinished();
}
