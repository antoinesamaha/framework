package com.fab.model.table.underlyingCustomisation;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.menu.FAbstractMenuAction;
import com.foc.menu.FMenuItem;
import com.foc.menu.FMenuList;

public class UndCustMenu {
	@SuppressWarnings("serial")
	public static void addMenu(FMenuList list){
		list.addMenu(new FMenuItem("Underlying Custom Data", 'U', new FAbstractMenuAction(null, true){
			@Override
			public FPanel generatePanel() {
				UndCustTable                undCustTable = UndCustTable.getInstance();
				UndCustTableGuiDetailsPanel detailsPanel = new UndCustTableGuiDetailsPanel(undCustTable, FocObject.DEFAULT_VIEW_ID);
				return detailsPanel;
			}
		}));
	}
}
