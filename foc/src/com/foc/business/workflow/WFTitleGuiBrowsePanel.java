package com.foc.business.workflow;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WFTitleGuiBrowsePanel extends FListPanel {
	
	public static final int VIEW_SELECTION = 1; 
	
	public WFTitleGuiBrowsePanel(FocList focList, int viewID){
		super("Workflow Title List", FPanel.FILL_VERTICAL);
		FocDesc focDesc = WFTitleDesc.getInstance();
		if(focDesc != null){
			if(focList == null){
				focList = WFTitleDesc.getList(FocList.FORCE_RELOAD);
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
				tableView.addColumn(focDesc, WFTitleDesc.FLD_NAME, false);
				tableView.addColumn(focDesc, WFTitleDesc.FLD_DESCRIPTION, false);
				
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
