package com.fab.model.table;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FabMultiChoiceSetGuiBrowsePanel extends FListPanel {
	
	public FabMultiChoiceSetGuiBrowsePanel(FocList list, int viewID){
		super("Multiple Choice Set", FPanel.FILL_VERTICAL);

		FocDesc desc = FabMultiChoiceSetDesc.getInstance();

		if(list == null){
			list = FabMultiChoiceSetDesc.getList(FocList.LOAD_IF_NEEDED);
		}
		
    if(desc != null && list != null) {
   		setFocList(list);

   		FTableView tableView = getTableView();       
      tableView.addColumn(desc, FabMultiChoiceSetDesc.FLD_NAME, false);
      construct();
      
      requestFocusOnCurrentItem();
      showEditButton(true);
      showDuplicateButton(false);
      showAddButton(true);
      showRemoveButton(true);
    }
    
    FValidationPanel vPanel = showValidationPanel(true);
    vPanel.addSubject(list);
	}
}
