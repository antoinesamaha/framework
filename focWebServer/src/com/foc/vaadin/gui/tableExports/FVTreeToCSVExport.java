package com.foc.vaadin.gui.tableExports;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.tree.FNode;
import com.foc.tree.FTree;
import com.foc.tree.TreeScanner;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.web.dataModel.FocTreeWrapper;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FVTreeToCSVExport
  extends FVTableTreeCSVExport
{
  public FVTreeToCSVExport(TableTreeDelegate paramTableTreeDelegate)
  {
    super(paramTableTreeDelegate);
  }
  
  protected void setTableTreeColumnsTitle()
  {
    try
    {
      getFileWriter().append("Level,");
    }
    catch (IOException localIOException)
    {
      Globals.logException(localIOException);
    }
    super.setTableTreeColumnsTitle();
  }
  
  public void scan()
  {
    FocTreeWrapper localFocTreeWrapper = getFocTreeWrapper();
    if ((localFocTreeWrapper != null) && (localFocTreeWrapper.getFTree() != null)) {
      localFocTreeWrapper.getFTree().scanVisible(new TreeScanner<FNode<?, ?>>()
      {
        int level = 1;
        
        public boolean beforChildren(FNode<?, ?> paramAnonymousFNode)
        {
          ArrayList localArrayList = FVTreeToCSVExport.this.getVisibleColumns();
          FileWriter localFileWriter = FVTreeToCSVExport.this.getFileWriter();
          if ((localFileWriter != null) && (localArrayList != null))
          {
            FocObject localFocObject = (FocObject)paramAnonymousFNode.getObject();
            if (localFocObject != null)
            {
              FVTreeToCSVExport.this.addCellValue(Integer.valueOf(this.level));
              for (int i = 0; i < localArrayList.size(); i++)
              {
                FVTableColumn localFVTableColumn = (FVTableColumn)localArrayList.get(i);
                if (localFVTableColumn != null)
                {
                	localFVTableColumn.setDuringExcelExport(true);
                  String str = FVTreeToCSVExport.this.getPropertyStringValue(localFocObject, localFVTableColumn);
                  localFVTableColumn.setDuringExcelExport(false);
                  if (i == 0) {
                    FVTreeToCSVExport.this.addCellValue(str, 2 * this.level);
                  } else {
                    FVTreeToCSVExport.this.addCellValue(str);
                  }
                }
              }
              FVTreeToCSVExport.this.addNewLine();
              this.level += 1;
            }
          }
          return true;
        }
        
        public void afterChildren(FNode<?, ?> paramAnonymousFNode)
        {
          FocObject localFocObject = (FocObject)paramAnonymousFNode.getObject();
          if (localFocObject != null) {
            this.level -= 1;
          }
        }
      });
    }
  }
}
