package com.foc.business.printing;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class PrnLayoutGuiBrowsePanel extends FListPanel {

	boolean listofAll = false;
	public static final int VIEW_PRINTING = 1;
	
	public PrnLayoutGuiBrowsePanel(FocList focList, int viewID){
		super("Layout list", FPanel.FILL_NONE);
		FocDesc focDesc = PrnLayoutDesc.getInstance();
		if(focDesc != null){
			if(focList == null){
				focList   = PrnLayoutDesc.getList(FocList.FORCE_RELOAD);
				listofAll = true;
			}
			if(focList != null){
				setFocList(focList);
				FTableView tableView = getTableView();

				if(listofAll){
					tableView.addColumn(focDesc, PrnLayoutDesc.FLD_CONTEXT, false);
				}
				tableView.addColumn(focDesc, PrnLayoutDesc.FLD_FILE_NAME, false);				
				tableView.addColumn(focDesc, PrnLayoutDesc.FLD_DESCRIPTION, false);
				
				construct();
				tableView.setColumnResizingMode(FTableView.COLUMN_WIDTH_FACTOR_MODE);

				if(listofAll || viewID == VIEW_PRINTING){
					FValidationPanel vPanel = showValidationPanel(true);
					vPanel.addSubject(focList);
				}
				
				showAddButton(false);
				showRemoveButton(false);
				showEditButton(false);
			}
		}
	}
}
