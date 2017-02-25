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

public class FVTableToCSVExport extends FVTableTreeCSVExport {
  public FVTableToCSVExport(TableTreeDelegate paramTableTreeDelegate)
  {
    super(paramTableTreeDelegate);
  }
  
  public void scan()
  {
    FocList localFocList = getFocList();
    ArrayList localArrayList = getVisibleColumns();
    TableTreeDelegate localTableTreeDelegate = getTableTreeDelegate();
    FocDataWrapper localFocDataWrapper = localTableTreeDelegate.getTreeOrTable().getFocDataWrapper();
    Collection localCollection = localFocDataWrapper.getItemIds();
    Iterator localIterator = localCollection.iterator();
    while ((localIterator != null) && (localIterator.hasNext()))
    {
      Integer localInteger = (Integer)localIterator.next();
      FocObject localFocObject = localFocList.searchByReference(localInteger.intValue());
      if (localFocObject != null)
      {
        for (int i = 0; i < localArrayList.size(); i++)
        {
          FVTableColumn localFVTableColumn = (FVTableColumn)localArrayList.get(i);
          if (localFVTableColumn != null)
          {
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

