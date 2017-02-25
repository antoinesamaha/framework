package com.foc.business.workflow.map;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WFSignatureGuiBrowsePanel extends FListPanel {

	boolean listofAll = false;
	
	public WFSignatureGuiBrowsePanel(FocList focList, int viewID){
		super("Signature List", FPanel.FILL_BOTH);
		FocDesc focDesc = WFSignatureDesc.getInstance();
		if(focDesc != null){
			if(focList == null){
				focList   = WFSignatureDesc.getList(FocList.FORCE_RELOAD);
				listofAll = true;
			}
			if(focList != null){
				setFocList(focList);
				FTableView tableView = getTableView();

				if(listofAll){
					tableView.addColumn(focDesc, WFSignatureDesc.FLD_MAP, true);
				}
				tableView.addColumn(focDesc, WFSignatureDesc.FLD_PREVIOUS_STAGE, true);
				tableView.addColumn(focDesc, WFSignatureDesc.FLD_TARGET_STAGE, true);
				
				for(int i=0; i<WFSignatureDesc.FLD_TITLE_COUNT; i++){
					tableView.addColumn(focDesc, WFSignatureDesc.FLD_TITLE_FIRST+i, true);
				}
			
				construct();
				tableView.setColumnResizingMode(FTableView.COLUMN_WIDTH_FACTOR_MODE);
				getTable().setDropable(true);

				if(listofAll){
					FValidationPanel vPanel = new FValidationPanel();
					vPanel.addSubject(focList);
				}
				
				showAddButton(true);
				showRemoveButton(true);
				showEditButton(false);
			}
		}
	}
}
