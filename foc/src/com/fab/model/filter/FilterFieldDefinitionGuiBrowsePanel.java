package com.fab.model.filter;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FilterFieldDefinitionGuiBrowsePanel extends FListPanel {
	
	public FilterFieldDefinitionGuiBrowsePanel(FocList focList, int viewID){
		super("Filter field defintion", FPanel.FILL_BOTH);
		FocDesc desc = FilterFieldDefinitionDesc.getInstance();

    if (desc != null) {
      if (focList != null) {
      	try{
      		setFocList(focList);
      	}catch(Exception e){
      		Globals.logException(e);
      	}
        FTableView tableView = getTableView();       

        tableView.addColumn(desc, FilterFieldDefinitionDesc.FLD_CONDITION_PROPERTY_PATH, false);
        //tableView.addColumn(desc, FilterFieldDefinitionDesc.FLD_CONDITION_PROPERTY_PATH_2222, false);
        construct();
        
        requestFocusOnCurrentItem();
        showEditButton(true);
        showDuplicateButton(false);
      }
    }
	}
}
