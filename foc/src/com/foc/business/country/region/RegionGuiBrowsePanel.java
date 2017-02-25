package com.foc.business.country.region;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class RegionGuiBrowsePanel extends FListPanel{
  
  public RegionGuiBrowsePanel(FocList list, int viewID){
    setFrameTitle("Region");
    setMainPanelSising(FPanel.MAIN_PANEL_FILL_VERTICAL);
    FocDesc desc = RegionDesc.getInstance();
    if(desc != null){
      if(list == null){
        list = RegionDesc.getList(FocList.FORCE_RELOAD);
      }
      if(list != null){
        setFocList(list);

        FTableView tableView = getTableView();       
        tableView.addColumn(desc, RegionDesc.FLD_REGION_NAME, false);
        //tableView.setDetailPanelViewID(viewID);
        
        construct();
        requestFocusOnCurrentItem();
        //FValidationPanel savePanel = showValidationPanel(true);
        //savePanel.addSubject(list);
        //setMainPanelSising(FPanel.MAIN_PANEL_FILL_VERTICAL);
        setFill(FPanel.FILL_VERTICAL);
                
        showEditButton(false);
        //tableView.setEditAfterInsertion(true);
      }
    }
  }
}
