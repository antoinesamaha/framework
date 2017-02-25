package com.foc.db.migration;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class MigFieldMapGuiBrowsePanel extends FListPanel {
  public MigFieldMapGuiBrowsePanel(FocList list, int viewID){
    super("Field mapping", FPanel.FILL_BOTH);
    FocDesc desc = MigFieldMapDesc.getInstance();

    if(desc != null && list != null){
      setFocList(list);
      FTableView tableView = getTableView();   

      tableView.addColumn(desc, MigFieldMapDesc.FLD_DB_FIELD_NAME, false);      
      tableView.addColumn(desc, MigFieldMapDesc.FLD_DB_FIELD_TITLE, false);
      tableView.addColumn(desc, MigFieldMapDesc.FLD_DB_FIELD_EXPLANATION, false);
      tableView.addColumn(desc, MigFieldMapDesc.FLD_COLUMN_TITLE, true);
      tableView.addColumn(desc, MigFieldMapDesc.FLD_MANDATORY, true);
      tableView.addColumn(desc, MigFieldMapDesc.FLD_KEY_FIELD, true);
      tableView.addColumn(desc, MigFieldMapDesc.FLD_DB_FOREIGN_FIELD, true);
      
      construct();
      
      tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
      
      showModificationButtons(false);
      showEditButton(false);
      showDuplicateButton(false);
      requestFocusOnCurrentItem();      
    }
  }
}
