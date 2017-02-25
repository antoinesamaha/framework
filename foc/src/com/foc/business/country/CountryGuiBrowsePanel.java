package com.foc.business.country;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class CountryGuiBrowsePanel extends FListPanel{
  
  public CountryGuiBrowsePanel(FocList list, int viewID){
    setFrameTitle("Country");
    setMainPanelSising(FPanel.MAIN_PANEL_FILL_VERTICAL);
    FocDesc desc = CountryDesc.getInstance();
    if(desc != null){
      if(list == null){
        list = CountryDesc.getList(FocList.FORCE_RELOAD);
      }
      if(list != null){
        setFocList(list);

        FTableView tableView = getTableView();       
        tableView.addColumn(desc, CountryDesc.FLD_COUNTRY_NAME, false);
        //tableView.setDetailPanelViewID(viewID);
        
        construct();
        requestFocusOnCurrentItem();
        FValidationPanel savePanel = showValidationPanel(true);
        savePanel.addSubject(list);
        //setMainPanelSising(FPanel.MAIN_PANEL_FILL_VERTICAL);
        setFill(FPanel.FILL_VERTICAL);
                
        showEditButton(true);
        tableView.setEditAfterInsertion(true);
      }
    }
  }
}
