package com.fab.gui.details;

import com.fab.FocApplicationBuilder;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class GuiDetailsComponentGuiBrowsePanel extends FListPanel{
	
	public GuiDetailsComponentGuiBrowsePanel(FocList list, int viewId){
		//super("Browse Column", FPanel.FILL_VERTICAL);
		super("Browse Column", FPanel.FILL_BOTH);
		FocDesc desc = GuiDetailsComponentDesc.getInstance();
		boolean allowEdit = viewId != FocApplicationBuilder.VIEW_NO_EDIT;

    if (desc != null) {
      if (list != null) {
      	try{
      		setFocList(list);
      	}catch(Exception e){
      		Globals.logException(e);
      	}
        FTableView tableView = getTableView();       
        
        tableView.addLineNumberColumn();
        tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_FIELD_DEFINITION, allowEdit);
        tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_X, allowEdit);
        tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_Y, allowEdit);
        tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_GRID_WIDTH, allowEdit);
        tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_GRID_HEIGHT, allowEdit);
        tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_COLUMNS, allowEdit);        
        //tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_VIEW_ID, 20, allowEdit);
        tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_COMPONENT_GUI_DETAILS, allowEdit);
        tableView.addColumn(desc, GuiDetailsComponentDesc.FLD_COMPONENT_GUI_BROWSE, allowEdit);
        construct();
        
        tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
        
        requestFocusOnCurrentItem();
        showEditButton(false);
        showDuplicateButton(false);
        showAddButton(allowEdit);
        showRemoveButton(allowEdit);
      }
    }
	}
}
