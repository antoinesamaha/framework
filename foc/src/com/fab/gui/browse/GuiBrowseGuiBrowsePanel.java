package com.fab.gui.browse;

import com.fab.FocApplicationBuilder;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FGCurrentItemPanel;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class GuiBrowseGuiBrowsePanel extends FListPanel{
	
	public GuiBrowseGuiBrowsePanel(FocList list, int viewId){
		super("Browse views", FPanel.FILL_BOTH);
		boolean allowEdit = viewId != FocApplicationBuilder.VIEW_NO_EDIT;
		FocDesc desc = GuiBrowseDesc.getInstance();

    if (desc != null) {
      if (list != null) {
      	try{
      		setFocList(list);
      	}catch(Exception e){
      		Globals.logException(e);
      	}
        FTableView tableView = getTableView();       
        
        tableView.addLineNumberColumn();
        //tableView.addColumn(desc, GuiBrowseDesc.FLD_VIEW_ID, 10, allowEdit);
        tableView.addColumn(desc, GuiBrowseDesc.FLD_LABEL, allowEdit);
        tableView.addColumn(desc, GuiBrowseDesc.FLD_TITLE, allowEdit);
        tableView.addColumn(desc, GuiBrowseDesc.FLD_DETAILS_VIEW_WHEN_EDIT, allowEdit);
        tableView.addColumn(desc, GuiBrowseDesc.FLD_DETAILS_VIEW_WHEN_INSERT, allowEdit);
        tableView.addColumn(desc, GuiBrowseDesc.FLD_SHOW_EDIT_BUTTON, allowEdit);
        tableView.addColumn(desc, GuiBrowseDesc.FLD_SHOW_VALIDATION_PANEL, allowEdit);
        tableView.addColumn(desc, GuiBrowseDesc.FLD_COLUMN_AUTO_RESIZE, allowEdit);
        tableView.addColumn(desc, GuiBrowseDesc.FLD_BROWSE_FILL, allowEdit);
        
        construct();
        tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
        
        FGCurrentItemPanel currentItemPanel = new FGCurrentItemPanel(this, viewId);
        add(currentItemPanel, 1, 3);
        
        requestFocusOnCurrentItem();
        showEditButton(false);
        showDuplicateButton(false);
        showAddButton(allowEdit);
        showRemoveButton(allowEdit);
      }
    }
	}
}
