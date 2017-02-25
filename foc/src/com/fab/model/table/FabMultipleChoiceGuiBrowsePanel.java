package com.fab.model.table;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FabMultipleChoiceGuiBrowsePanel extends FListPanel {
	
	public FabMultipleChoiceGuiBrowsePanel(FocList list, int viewID){
		super("Dictionary groups", FPanel.FILL_VERTICAL);
		FocDesc desc = FabMultipleChoiceDesc.getInstance();

    if(desc != null && list != null) {
    	list.loadIfNotLoadedFromDB();
    	try{
    		setFocList(list);
    	}catch(Exception e){
    		Globals.logException(e);
    	}
      FTableView tableView = getTableView();       
      tableView.addColumn(desc, FabMultipleChoiceDesc.FLD_INT_VALUE, true);
      tableView.addColumn(desc, FabMultipleChoiceDesc.FLD_DISPLAY_TEXT, true);
      construct();
      
      requestFocusOnCurrentItem();
      showEditButton(false);
      showDuplicateButton(false);
      showAddButton(true);
      showRemoveButton(true);
    }
	}
}
