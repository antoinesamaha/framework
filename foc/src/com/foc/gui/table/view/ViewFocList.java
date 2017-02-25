package com.foc.gui.table.view;

import com.foc.db.SQLFilter;
import com.foc.desc.FocConstructor;
import com.foc.gui.table.FTableView;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class ViewFocList extends FocList{

	private FTableView tableView = null;

	public ViewFocList(String key) {
		this(key, true);
	}
	
	public ViewFocList(String key, boolean load) {
		super(new FocLinkSimple(ViewConfigDesc.getInstance()));
		initFilter(key);
		if(load){
			loadIfNotLoadedFromDB();
		}
	}
		
	private void initFilter(String key){
		ViewConfig config = new ViewConfig(new FocConstructor(ViewConfigDesc.getInstance(), null));
		config.setViewKey(key);
		SQLFilter filter = getFilter();
		filter.setFilterFields(SQLFilter.FILTER_ON_SELECTED);
		filter.addSelectedField(ViewConfigDesc.FLD_VIEW_KEY);
		filter.setObjectTemplate(config);
		setDirectlyEditable(false);
		setDirectImpactOnDatabase(true);
		setListOrder(new FocListOrder(ViewConfigDesc.FLD_CODE));
	}

	@Override
	public void dispose(){
		super.dispose();
		tableView = null;
	}

	public FTableView getTableView() {
		return tableView;
	}

	public void setTableView(FTableView tableView) {
		this.tableView = tableView;
	}
}
