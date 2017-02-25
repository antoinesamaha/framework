package com.foc.db.migration;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class MigDirectoryGuiBrowsePanel extends FListPanel {
  public MigDirectoryGuiBrowsePanel(FocList list, int viewID){
    super("Directory", FPanel.FILL_VERTICAL);
    FocDesc desc = MigDirectoryDesc.getInstance();

    if (desc != null) {
      if(list == null){
        list = MigDirectoryDesc.getList(FocList.LOAD_IF_NEEDED);
      }else{
        list.loadIfNotLoadedFromDB();
      }
      setFocList(list);
      FTableView tableView = getTableView();   

      tableView.addColumn(desc, FField.FLD_NAME, false);
      tableView.addColumn(desc, MigDirectoryDesc.FLD_DIR_PATH, false);      
      
      construct();
      
      tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
      
      showEditButton(true);
      showDuplicateButton(false);
      requestFocusOnCurrentItem();      
    }

    FValidationPanel savePanel = showValidationPanel(true);
    if (savePanel != null) {
      savePanel.addSubject(list);
    }
  }
}
