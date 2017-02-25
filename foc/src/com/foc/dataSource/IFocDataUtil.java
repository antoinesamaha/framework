package com.foc.dataSource;

import com.foc.desc.FocDesc;

public interface IFocDataUtil {
	public void dbUtil_RemoveAllIndexesForTable_ExceptRef(FocDesc focDesc);
	public void dbUtil_UpdateFieldEqualToAnother(String tableName, String tarField, String srcField);
	public void dbUtil_DuplicateTable(String previousName, String newName);
	public void dbUtil_RenameTable(String previousName, String newName);
	public void dbUtil_RenameColumnsText(String table, String srcCol, String tarCol, int size);
	public void dbUtil_RenameColumnsInteger(String table, String srcCol, String tarCol);
	public void dbUtil_RenameColumnsDate(String table, String srcCol, String tarCol);
}
