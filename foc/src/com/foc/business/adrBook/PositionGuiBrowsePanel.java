package com.foc.business.adrBook;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class PositionGuiBrowsePanel extends FListPanel {
  public PositionGuiBrowsePanel(FocList list, int viewID){
    super("Position", FPanel.FILL_VERTICAL);
    FocDesc desc = PositionDesc.getInstance();

    if(desc != null){
      if(list == null){
        list = PositionDesc.getList(FocList.LOAD_IF_NEEDED);
      }else{
        list.loadIfNotLoadedFromDB();
      }
      setFocList(list);
      FTableView tableView = getTableView();   
      
      tableView.addColumn(desc, PositionDesc.FLD_NAME, true);
      
      construct();
      
      showEditButton(false);
      showDuplicateButton(false);
      requestFocusOnCurrentItem();      
    }
       
    FValidationPanel savePanel = showValidationPanel(true);
    if (savePanel != null) {
      savePanel.addSubject(list);
    }
  }
}
