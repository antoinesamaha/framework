package com.foc.gui.table.view;

import com.foc.gui.FListPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class ViewConfigGuiBrowsePanel extends FListPanel{
	
	public final static int VIEW_NO_EDIT = 1;
	
	public ViewConfigGuiBrowsePanel(FocList list, int viewID){
		super("Table Views", FILL_BOTH);

    //setWithScroll(false);
		setFocList(list);
		
		FTableView tableView = getTableView();
		tableView.addColumn(list.getFocDesc(), ViewConfigDesc.FLD_CODE, false);
		tableView.setDetailPanelViewIDForNewItem(ViewConfigGuiDetailsPanel.VIEW_CREATION);
		construct();
		
		tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);

		showModificationButtons(viewID != VIEW_NO_EDIT);
		showEditButton(false);
		showDuplicateButton(true);
	}
}
