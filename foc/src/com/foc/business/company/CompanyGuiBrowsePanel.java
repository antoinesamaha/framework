package com.foc.business.company;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class CompanyGuiBrowsePanel extends FListPanel{
  
  public CompanyGuiBrowsePanel(FocList list, int viewID){
    setFrameTitle("Company");
    FocDesc desc = CompanyDesc.getInstance();
    if(desc != null){
      if(list == null){
        list = CompanyDesc.getList(FocList.FORCE_RELOAD);
      }
      if(list != null){
        setFocList(list);

        FTableView tableView = getTableView();       
        tableView.addColumn(desc, FField.FLD_NAME, false);
        tableView.addColumn(desc, CompanyDesc.FLD_DESCRIPTION, false);
        
        construct();
        requestFocusOnCurrentItem();
        FValidationPanel savePanel = showValidationPanel(true);
        savePanel.addSubject(list);
        setFill(FPanel.FILL_NONE);
                
        showEditButton(true);
      }
    }
  }
}
