package com.foc.business.workflow;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WFOperatorGuiBrowsePanel extends FListPanel {

	boolean listofAll = false;
	
	public WFOperatorGuiBrowsePanel(FocList focList, int viewID){
		super("Operator List", FPanel.FILL_BOTH);
		FocDesc focDesc = WFOperatorDesc.getInstance();
		if(focDesc != null){
			if(focList == null){
				focList   = WFOperatorDesc.getList(FocList.FORCE_RELOAD);
				listofAll = true;
			}
			if(focList != null){
				setFocList(focList);
				FTableView tableView = getTableView();

				if(listofAll){
					tableView.addColumn(focDesc, WFOperatorDesc.FLD_AREA, false);
				}
				tableView.addColumn(focDesc, WFOperatorDesc.FLD_USER, !listofAll);				
				tableView.addColumn(focDesc, WFOperatorDesc.FLD_TITLE, !listofAll);
				
				construct();
				tableView.setColumnResizingMode(FTableView.COLUMN_WIDTH_FACTOR_MODE);

				if(listofAll){
					FValidationPanel vPanel = showValidationPanel(true);
					vPanel.addSubject(focList);
				}
				
				showAddButton(true);
				showRemoveButton(true);
				showEditButton(false);
			}
		}
	}
}
