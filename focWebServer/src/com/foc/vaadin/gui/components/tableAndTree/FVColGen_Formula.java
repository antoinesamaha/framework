package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVColGen_Formula extends FVColumnGenerator {

	public FVColGen_Formula(FVTableColumn tableColumn) {
		super(tableColumn);
	}

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		FVTableColumn tableColumn = getTableColumn();
		Object obj = null;
		if(tableColumn != null && getTreeOrTable() != null){
			FocList focList = getTreeOrTable().getFocList();
			FocObject focObject = focList != null ? focList.searchByRealReferenceOnly((Integer) itemId) : null;
			if(focObject != null){
				obj = tableColumn.computeFormula_ForFocObject(focObject);
				
				if(obj instanceof Double || obj instanceof Integer){
					FVLabel lbl = new FVLabel("");
					lbl.addStyleName("foc-text-right");
					lbl.setValue(obj+"");
					obj = lbl;
				}
			}
			if(tableColumn.isHtml()){
				obj = new FVLabel(obj+"", ContentMode.HTML);
			}
		}
		return obj;
	}
}
