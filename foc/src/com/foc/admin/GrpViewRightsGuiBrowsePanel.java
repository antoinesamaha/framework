package com.foc.admin;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class GrpViewRightsGuiBrowsePanel extends FPanel{
  
  public GrpViewRightsGuiBrowsePanel(FocList list, int viewID, boolean readOnly){
    super("Views Rights", FPanel.FILL_BOTH);
    setFill(FPanel.FILL_HORIZONTAL);
    setMainPanelSising(FPanel.MAIN_PANEL_FILL_HORIZONTAL);
    
    FocDesc desc = GrpViewRightsDesc.getInstance();
    if (desc != null) {
      if (list != null) {
        FListPanel selectionPanel = new FListPanel(list);
        FTableView tableView = selectionPanel.getTableView();

        tableView.addColumn(list.getFocDesc(), GrpViewRightsDesc.FLD_VIEW_KEY, false);
        tableView.addColumn(list.getFocDesc(), GrpViewRightsDesc.FLD_VIEW_CONTEXT, false);
        tableView.addColumn(list.getFocDesc(), GrpViewRightsDesc.FLD_VIEW_RIGHT, !readOnly);
        tableView.addColumn(list.getFocDesc(), GrpViewRightsDesc.FLD_VIEW_CONFIG, !readOnly);
        
        selectionPanel.construct();
        tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
        
        selectionPanel.showEditButton(false);
        selectionPanel.showAddButton(false);
        selectionPanel.showRemoveButton(false);
        
        add(selectionPanel,0,0);
      }
    }
  }
}
