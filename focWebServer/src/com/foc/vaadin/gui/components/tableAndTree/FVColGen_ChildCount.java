package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.desc.FocObject;
import com.foc.tree.FNode;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.FVTreeTable;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVColGen_ChildCount extends FVColumnGenerator {
	
	public FVColGen_ChildCount(FVTableColumn tableColumn) {
		super(tableColumn);
	}
	
	private int childCount(FVTreeTable tableTree, FNode node){
		int leafChildTotal = 0;
		if(node != null){
			int childCount = node.getChildCount();
			for(int i=0; i<childCount; i++){
				FNode     childNode = node.getChildAt(i);
				FocObject focObject = childNode != null && childNode.getObject() instanceof FocObject ? (FocObject) childNode.getObject() : null;
				
				if(childNode.isLeaf() && focObject != null && tableTree.getFocDataWrapper().containsId(focObject.getReference().getInteger())){
					leafChildTotal = leafChildTotal + 1;
				}else{
					leafChildTotal = leafChildTotal + childCount(tableTree, childNode);
				}
			}
		}
		return leafChildTotal;
	}
	
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Object obj = "";
		FVTreeTable tableTree = (FVTreeTable) getTreeOrTable();
		if(tableTree != null && tableTree.hasChildren(itemId)){
			FNode node = tableTree.getFocTreeWrapper() != null && tableTree.getFocTreeWrapper().getFTree() != null ? tableTree.getFocTreeWrapper().getFTree().findNode(tableTree.getItem(itemId)) : null;
			if(node != null){
				obj = childCount(tableTree, node);
			}else{
//				obj = tableTree.getChildren(itemId).size();
			}
		}
		return obj;
	}
}
