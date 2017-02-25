package com.foc.business.calendar;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableColumn;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FCalendarGuiBrowsePanel extends FPanel {
	
	public FCalendarGuiBrowsePanel(FocList list, int viewID){
		super("Calendar", FPanel.FILL_NONE);
		FocDesc desc = FCalendarDesc.getInstance();

    FListPanel selectionPanel = null;
    if (desc != null) {
    	if(list == null){
    		list = FCalendarDesc.getList(FocList.LOAD_IF_NEEDED);
    	}else{
    		list.loadIfNotLoadedFromDB();
    	}
      selectionPanel = new FListPanel(list);
      FTableView tableView = selectionPanel.getTableView();   
      
      selectionPanel.setDirectlyEditable(false);
      
      tableView.addColumn(desc, FCalendarDesc.FLD_NAME, false);
      FTableColumn col = tableView.addColumn(desc, FCalendarDesc.FLD_IS_DEFAULT, true);
      col.setSize(15);
      
      selectionPanel.construct();
      
      selectionPanel.showEditButton(true);
      selectionPanel.showDuplicateButton(false);
      selectionPanel.requestFocusOnCurrentItem();      
    }

    add(selectionPanel, 0, 0);
       
  	FValidationPanel savePanel = showValidationPanel(true);
    if (savePanel != null) {
      savePanel.addSubject(list);	
    }
	}  
}
