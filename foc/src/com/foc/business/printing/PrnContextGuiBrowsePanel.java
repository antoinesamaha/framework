package com.foc.business.printing;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class PrnContextGuiBrowsePanel extends FListPanel {
	
	public static final int VIEW_SELECTION = 1; 
	
	public PrnContextGuiBrowsePanel(FocList focList, int viewID){
		super("Workflow Area List", FPanel.FILL_VERTICAL);
		FocDesc focDesc = PrnContextDesc.getInstance();
		if(focDesc != null){
			if(focList == null){
				focList = PrnContextDesc.getList(FocList.FORCE_RELOAD);
			}
			if(focList != null){
				try {
					setFocList(focList);
				} catch (Exception e) {
					Globals.logException(e);
				}
				FTableView tableView = getTableView();

				if(viewID == VIEW_SELECTION){
					tableView.addSelectionColumn();
				}
				tableView.addColumn(focDesc, PrnContextDesc.FLD_NAME, false);
				tableView.addColumn(focDesc, PrnContextDesc.FLD_DESCRIPTION, false);
				
				construct();
				tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
				
				FValidationPanel validPanel = showValidationPanel(true);
				if(validPanel != null){
					if(viewID == VIEW_SELECTION){
						validPanel.setValidationButtonLabel("Ok");
					}else{
						validPanel.addSubject(focList);
					}
				}
				
				showAddButton(viewID != VIEW_SELECTION);
				showRemoveButton(viewID != VIEW_SELECTION);
				showEditButton(viewID != VIEW_SELECTION);
			}
		}
	}
}
