package com.foc.formula;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class PropertyFormulaGuiBrowsePanel extends FPanel {
  public PropertyFormulaGuiBrowsePanel(FocList list, int viewID){
    super("Property Formulas", FPanel.FILL_NONE);
    FocDesc desc = PropertyFormulaDesc.getInstance();

    FListPanel selectionPanel = null;
    if (desc != null) {
      if(list == null){
        list = PropertyFormulaDesc.getList(FocList.LOAD_IF_NEEDED);
      }else{
        list.loadIfNotLoadedFromDB();
      }
      selectionPanel = new FListPanel(list);
      FTableView tableView = selectionPanel.getTableView();   
      
      tableView.addColumn(desc, PropertyFormulaDesc.FLD_FIELD_NAME, true);
      tableView.addColumn(desc, PropertyFormulaDesc.FLD_EXPRESSION, true);

      
      selectionPanel.construct();
      selectionPanel.showEditButton(false);
      selectionPanel.showDuplicateButton(false);
      selectionPanel.requestFocusOnCurrentItem();      
    }

    add(selectionPanel, 0, 0);
    
    FValidationPanel savePanel = showValidationPanel(true);
    if (savePanel != null) {
      savePanel.addSubject(list);
    }
  }
}
