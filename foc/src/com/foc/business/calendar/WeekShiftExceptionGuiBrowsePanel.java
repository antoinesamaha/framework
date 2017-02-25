package com.foc.business.calendar;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WeekShiftExceptionGuiBrowsePanel extends FPanel {

	public WeekShiftExceptionGuiBrowsePanel(FocList list, int viewID){
		super("Week Shift Exceptions", FPanel.FILL_NONE, FPanel.FILL_NONE);
		FocDesc desc = WeekShiftExceptionDesc.getInstance();

    FListPanel selectionPanel = null;
    if (desc != null) {
    	if(list == null){
    		list = WeekShiftExceptionDesc.getList(FocList.LOAD_IF_NEEDED);
    	}else{
    		list.loadIfNotLoadedFromDB();
    	}
      selectionPanel = new FListPanel(list);
      FTableView tableView = selectionPanel.getTableView();       
      
      tableView.addColumn(desc, WeekShiftExceptionDesc.FLD_START_DATE, true);
      tableView.addColumn(desc, WeekShiftExceptionDesc.FLD_END_DATE, true);
      tableView.addColumn(desc, WeekShiftExceptionDesc.FLD_WEEK_SHIFT, true);
      
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
