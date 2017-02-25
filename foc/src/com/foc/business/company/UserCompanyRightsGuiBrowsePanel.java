package com.foc.business.company;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class UserCompanyRightsGuiBrowsePanel extends FListPanel{

  /**
   * @author 01Barmaja
   */
  
  private UserCompanyRightsDisplayList displayList = null;
  
  public UserCompanyRightsGuiBrowsePanel(FocList realList, int viewID){
    FocDesc desc = UserCompanyRightsDesc.getInstance();
    if(desc != null){
      realList.loadIfNotLoadedFromDB();
      displayList = new UserCompanyRightsDisplayList(realList);
      FocList list = displayList.getDisplayList();
      try {
        setFocList(list);
      } catch (Exception e) {
        Globals.logException(e);
      }
      FTableView tableView = getTableView();       
      tableView.addColumn(desc, UserCompanyRightsDesc.FLD_USER, false);
      tableView.addColumn(desc, UserCompanyRightsDesc.FLD_ACCESS_RIGHT, true);
      
      construct();
      //tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);

      showModificationButtons(false);
      showEditButton(false);
      setMainPanelSising(FPanel.MAIN_PANEL_FILL_BOTH);
      setFill(FPanel.FILL_BOTH);
    }
  }
  
  public void updateRealList(){
    displayList.updateRealList(); 
  }
  
  public void dispose(){
    super.dispose();
    if(displayList != null){
      displayList.dispose();
      displayList = null;
    }
  }
}
