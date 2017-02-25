package com.fab.model.table;

import com.fab.FocApplicationBuilder;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FabDictionaryGroupGuiBrowsePanel extends FListPanel {
	
	public FabDictionaryGroupGuiBrowsePanel(FocList list, int viewID){
		super("Dictionary groups", FPanel.FILL_VERTICAL);
		boolean allowEdit = viewID != FocApplicationBuilder.VIEW_NO_EDIT;
		FocDesc desc = FabDictionaryGroupDesc.getInstance();

    if(desc != null && list != null) {
    	list.loadIfNotLoadedFromDB();
    	try{
    		setFocList(list);
    	}catch(Exception e){
    		Globals.logException(e);
    	}
      FTableView tableView = getTableView();       
      tableView.addColumn(desc, FabDictionaryGroupDesc.FLD_NAME, allowEdit);
      construct();
      
      requestFocusOnCurrentItem();
      showEditButton(false);
      showDuplicateButton(false);
      showAddButton(allowEdit);
      showRemoveButton(allowEdit);
    }
	}
}
