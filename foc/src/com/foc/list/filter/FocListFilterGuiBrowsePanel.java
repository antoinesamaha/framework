package com.foc.list.filter;

import com.fab.model.filter.UserDefinedFilter;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FocListFilterGuiBrowsePanel extends FListPanel {
		
	private FocListFilter                   filter                = null;
	private FocListFilterValidationListener filterValidationPanel = null;
	
	public FocListFilterGuiBrowsePanel(FocList focList, int viewID){
		this(null, focList, viewID);
	}
	
	public FocListFilterGuiBrowsePanel(FocListFilter filter, FocList focList, int viewID){
		super("Filters", FPanel.FILL_BOTH);
		if(focList != null){
			this.filter           = filter;
			FocDesc filterFocDesc = focList.getFocDesc();
			if(filterFocDesc != null){
				setFocList(focList);
				FTableView tableView = getTableView();
				tableView.setDetailPanelViewID(filter == null ? viewID : UserDefinedFilter.VIEW_FOR_FILTER_CREATION);
				tableView.addColumn(filterFocDesc, FField.FLD_NAME, false);
				
				construct();
				
				if(filter != null){
					FValidationPanel validPanel = showValidationPanel(true);
					if(validPanel != null){
						filterValidationPanel = new FocListFilterValidationListener(this, getFilter(), validPanel);
					}
				}
				setFrameTitle("User defined filter");
				setMainPanelSising(FPanel.FILL_BOTH);
				showAddButton(true);
				showRemoveButton(true);
				showEditButton(true);
			}
		}
	}
	
	public void dispose(){
		super.dispose();
		filter = null;
		if(filterValidationPanel != null){
			filterValidationPanel.dispose();
			filterValidationPanel = null;
		}
	}

	public FocListFilter getFilter() {
		return filter;
	}
}
