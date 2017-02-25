package com.foc.business.workflow.rights;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class RightLevelGuiBrowsePanel extends FListPanel implements RightLevelConst {

	public RightLevelGuiBrowsePanel(FocList focList, int viewID){
		super("Right Levels", FPanel.FILL_BOTH);
		FocDesc focDesc = RightLevelDesc.getInstance();
		if(focDesc != null){
			if(focList == null){
				focList   					= RightLevelDesc.getList(FocList.FORCE_RELOAD);
			}
			if(focList != null){
				setFocList(focList);
				FTableView tableView = getTableView();

				boolean editable = true;
				
				tableView.addColumn(focDesc, FField.FLD_NAME         , "Right|Level|Name", editable);
				tableView.addColumn(focDesc, FLD_READ                , editable);				
				tableView.addColumn(focDesc, FLD_INSERT              , editable);
				tableView.addColumn(focDesc, FLD_MODIFY_DRAFT        , editable);
				tableView.addColumn(focDesc, FLD_MODIFY_APPROVED     , editable);
				tableView.addColumn(focDesc, FLD_DELETE_DRAFT        , editable);
				tableView.addColumn(focDesc, FLD_DELETE_APPROVED     , editable);
				
				tableView.addColumn(focDesc, FLD_APPROVE             , editable);
				tableView.addColumn(focDesc, FLD_CLOSE               , editable);
				tableView.addColumn(focDesc, FLD_CANCEL              , editable);
				tableView.addColumn(focDesc, FLD_PRINT_DRAFT         , editable);
				tableView.addColumn(focDesc, FLD_PRINT_APPROVE       , editable);
				tableView.addColumn(focDesc, FLD_MODIFY_CODE_DRAFT   , editable);
				tableView.addColumn(focDesc, FLD_MODIFY_CODE_APPROVED, editable);
				tableView.addColumn(focDesc, FLD_UNDO_SIGNATURE      , editable);
				
				construct();
				tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);

				FValidationPanel vPanel = showValidationPanel(true);
				vPanel.addSubject(focList);
				
				showAddButton(true);
				showRemoveButton(true);
				//showEditButton(false);
			}
		}
	}
}
