package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.Globals;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVColGen_TreeNodeTitle extends FVColumnGenerator {
	
	public FVColGen_TreeNodeTitle(FVTableColumn tableColumn) {
		super(tableColumn);
	}

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		String propertyString = null;
		try{
//			FTree tree = null;
//			tree = ((FVTreeTable) getTreeOrTable()).getFTree();
//			FNode node = tree.vaadin_FindNode(itemId);
//			FProperty property = tree != null && node != null ? tree.getTreeSpecialProperty(node) : null;
//			if(property != null){
//				propertyString = (String) property.vaadin_TableDisplayObject(null, null);
//			}else if(node != null){
//				propertyString = node.getDisplayTitle();
//			}
			propertyString = getTableTreeDelegate() != null ? getTableTreeDelegate().getNodeTitleDisplayStringForObjectRef(itemId) : null;
		}catch (Exception e){
			Globals.logException(e);
		}
		return propertyString;
	}
}
