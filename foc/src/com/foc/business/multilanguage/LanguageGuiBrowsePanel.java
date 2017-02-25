package com.foc.business.multilanguage;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class LanguageGuiBrowsePanel extends FListPanel {
  public LanguageGuiBrowsePanel(FocList list, int viewID){
    super("Language", FPanel.FILL_VERTICAL);
    FocDesc desc = LanguageDesc.getInstance();

    if (desc != null) {
      if(list == null){
        list = LanguageDesc.getList(FocList.LOAD_IF_NEEDED);
      }else{
        list.loadIfNotLoadedFromDB();
      }
      setFocList(list);
      FTableView tableView = getTableView();   

      tableView.addColumn(desc, LanguageDesc.FLD_DESCRIPTION, false);      
      tableView.addColumn(desc, LanguageDesc.FLD_NAME, false);
      tableView.addColumn(desc, LanguageDesc.FLD_CODE, 10, false);
      
      construct();
      
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
