package com.foc.business.calendar;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WorkShiftGuiBrowsePanel extends FListPanel {
	
	public WorkShiftGuiBrowsePanel(FocList list, int viewID){
		super("Work Shifts", FPanel.FILL_VERTICAL);
		FocDesc desc = WorkShiftDesc.getInstance();

    if (desc != null && list != null) {
    	list.loadIfNotLoadedFromDB();

    	setFocList(list);
      FTableView tableView = getTableView();   
      
      tableView.addColumn(desc, WorkShiftDesc.FLD_START_TIME, true);
      tableView.addColumn(desc, WorkShiftDesc.FLD_END_TIME, true);
      
      construct();
      
      tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
      
      showEditButton(false);
      showDuplicateButton(false);
      requestFocusOnCurrentItem();      
    }
	}  
}
