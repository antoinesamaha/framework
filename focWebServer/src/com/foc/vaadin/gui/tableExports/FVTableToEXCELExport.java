package com.foc.vaadin.gui.tableExports;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class FVTableToEXCELExport extends FVTableTreeEXCELExport {
	public FVTableToEXCELExport(TableTreeDelegate paramTableTreeDelegate) {
		super(paramTableTreeDelegate);
	}

	public void scan() {
		FocList localFocList = getFocList();
		TableTreeDelegate localTableTreeDelegate = getTableTreeDelegate();
		FocDataWrapper localFocDataWrapper = localTableTreeDelegate.getTreeOrTable().getFocDataWrapper();
		Collection localCollection = localFocDataWrapper.getItemIds();
		Iterator localIterator = localCollection.iterator();
		while ((localIterator != null) && (localIterator.hasNext())){
			Long localInteger = (Long) localIterator.next();
			FocObject localFocObject = localFocList.searchByReference(localInteger.longValue());
			if(localFocObject != null){
				
				for(int i = 0; i < getColumnCount(); i++){
					FVTableColumn localFVTableColumn = getColumnAt(i);
					if(localFVTableColumn != null){
						localFVTableColumn.setDuringExcelExport(true);
						String str = getPropertyStringValue(localFocObject, localFVTableColumn);
						localFVTableColumn.setDuringExcelExport(false);
						addCellValue(str);
					}
				}
				
				addNewLine();
			}
		}
	}
}
