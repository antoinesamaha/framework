package com.foc.business.calendar;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class HolidayGuiBrowsePanel extends FPanel {

	public HolidayGuiBrowsePanel(FocList list, int viewID){
		super("Holiday", FPanel.FILL_NONE, FPanel.FILL_NONE);
		FocDesc desc = HolidayDesc.getInstance();

    FListPanel selectionPanel = null;
    if (desc != null) {
    	if(list == null){
    		list = HolidayDesc.getList(FocList.LOAD_IF_NEEDED);
    	}else{
    		list.loadIfNotLoadedFromDB();
    	}
      selectionPanel = new FListPanel(list);
      FTableView tableView = selectionPanel.getTableView();       
      
      tableView.addColumn(desc, HolidayDesc.FLD_START_DATE, true);
      tableView.addColumn(desc, HolidayDesc.FLD_END_DATE, true);
      tableView.addColumn(desc, HolidayDesc.FLD_REASON, true);
      
      selectionPanel.construct();
      //tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
              
      selectionPanel.requestFocusOnCurrentItem();
      selectionPanel.showEditButton(false);
      selectionPanel.showDuplicateButton(false);
    }
    add(selectionPanel,0,0);
    
    /*FValidationPanel savePanel = showValidationPanel(true);
    if (savePanel != null) {
      savePanel.addSubject(list);	
    }*/
  }
	
}
