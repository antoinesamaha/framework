package com.foc.business.calendar;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class DayShiftGuiBrowsePanel extends FListPanel {
	
	public DayShiftGuiBrowsePanel(FocList list, int viewID){
		super("Day Shifts", FPanel.FILL_NONE);
		FocDesc desc = DayShiftDesc.getInstance();

    if (desc != null) {
    	if(list == null){
    		list = DayShiftDesc.getList(FocList.LOAD_IF_NEEDED);
    	}else{
    		list.loadIfNotLoadedFromDB();
    	}
      setFocList(list);
      FTableView tableView = getTableView();   
      
      setDirectlyEditable(false);
      
      tableView.addColumn(desc, DayShiftDesc.FLD_NAME, false);
      
      construct();
      
      showEditButton(true);
      showDuplicateButton(false);
      requestFocusOnCurrentItem();      
    }

  	FValidationPanel savePanel = showValidationPanel(true);
  	savePanel.setValidationType(FValidationPanel.VALIDATION_SAVE_CANCEL);
    if (savePanel != null) {
      savePanel.addSubject(list);	
    }
	}  
}
