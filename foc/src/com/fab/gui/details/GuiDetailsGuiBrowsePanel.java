package com.fab.gui.details;

import com.fab.FocApplicationBuilder;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FGCurrentItemPanel;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class GuiDetailsGuiBrowsePanel extends FListPanel {
	
	public GuiDetailsGuiBrowsePanel(FocList list, int viewId){
		super("Details views", FPanel.FILL_BOTH);
		boolean allowEdit = viewId != FocApplicationBuilder.VIEW_NO_EDIT;
		FocDesc desc = GuiDetailsDesc.getInstance();

    if (desc != null) {
      if (list != null) {
      	try{
      		setFocList(list);
      	}catch(Exception e){
      		Globals.logException(e);
      	}
        FTableView tableView = getTableView();       
        
        tableView.addLineNumberColumn();
        //tableView.addColumn(desc, GuiDetailsDesc.FLD_VIEW_ID, 20, allowEdit);
        tableView.addColumn(desc, GuiDetailsDesc.FLD_DESCRIPTION, 20, allowEdit);
        tableView.addColumn(desc, GuiDetailsDesc.FLD_TITLE, 20, allowEdit);
        tableView.addColumn(desc, GuiDetailsDesc.FLD_ADD_SUBJECT_TO_VALIDATION_PANEL, 20, allowEdit);
        tableView.addColumn(desc, GuiDetailsDesc.FLD_SHOW_VALIDATION_PANEL, 20, allowEdit);
        tableView.addColumn(desc, GuiDetailsDesc.FLD_VIEW_MODE, 20, allowEdit);
        tableView.addColumn(desc, GuiDetailsDesc.FLD_IS_DEFAULT_VIEW, 20, allowEdit);
        tableView.addColumn(desc, GuiDetailsDesc.FLD_IS_SUMMARY_VIEW, 20, allowEdit);
        
        construct();
        
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
