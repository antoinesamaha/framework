package com.fab.model.project;

import com.fab.FocApplicationBuilder;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FabWorkspaceGuiBrowsePanel extends FListPanel {
	
	public FabWorkspaceGuiBrowsePanel(FocList list, int viewID){
		super("Workspaces", FPanel.FILL_BOTH);
		boolean allowEdit = viewID != FocApplicationBuilder.VIEW_NO_EDIT;
		FocDesc desc = FabWorkspaceDesc.getInstance();

    if(desc != null && list != null) {
    	list.loadIfNotLoadedFromDB();
    	try{
    		setFocList(list);
    	}catch(Exception e){
    		Globals.logException(e);
    	}
      FTableView tableView = getTableView();       
      tableView.addColumn(desc, FabWorkspaceDesc.FLD_NAME, allowEdit);
      tableView.addColumn(desc, FabWorkspaceDesc.FLD_WORKSPACE_PATH, allowEdit);
      construct();
      
      tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
      
      requestFocusOnCurrentItem();
      showEditButton(false);
      showDuplicateButton(false);
      showAddButton(allowEdit);
      showRemoveButton(allowEdit);
      
      FValidationPanel vPanel = showValidationPanel(true);
      vPanel.addSubject(list);
    }
	}
}
