package com.foc.business.country.city;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class CityGuiBrowsePanel extends FListPanel{
  
  public CityGuiBrowsePanel(FocList list, int viewID){
    setFrameTitle("City / Village");
    setMainPanelSising(FPanel.MAIN_PANEL_FILL_VERTICAL);
    FocDesc desc = CityDesc.getInstance();
    if(desc != null){
      if(list == null){
        list = CityDesc.getList(FocList.FORCE_RELOAD);
      }
      if(list != null){
        setFocList(list);

        FTableView tableView = getTableView();       
        tableView.addColumn(desc, CityDesc.FLD_CITY_NAME, true);
        //tableView.setDetailPanelViewID(viewID);
        
        construct();
        requestFocusOnCurrentItem();
        //FValidationPanel savePanel = showValidationPanel(true);
        //savePanel.addSubject(list);
        //setMainPanelSising(FPanel.MAIN_PANEL_FILL_VERTICAL);
        setFill(FPanel.FILL_VERTICAL);
                
        showEditButton(false);
      }
    }
  }
}
