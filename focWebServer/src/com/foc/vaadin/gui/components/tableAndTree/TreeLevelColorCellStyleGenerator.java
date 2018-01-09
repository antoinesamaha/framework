package com.foc.vaadin.gui.components.tableAndTree;

import com.foc.Globals;
import com.foc.tree.FNode;
import com.foc.tree.FTree;
import com.foc.web.dataModel.FocTreeWrapper;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class TreeLevelColorCellStyleGenerator implements Table.CellStyleGenerator{

	private FocTreeWrapper treeWrapper          = null;
	private boolean        alternateLeafCoulors = true;
	
	public TreeLevelColorCellStyleGenerator(FocTreeWrapper treeWrapper){
		this.treeWrapper = treeWrapper;
	}
	
	public void dispose(){
		treeWrapper = null;
	}
	
	public String getStyle(FNode node){
		String style = null;
		
		if(node.isLeaf() && isAlternateLeafCoulors()){
			if((node.getIndexInChildrenArray() % 2) == 0){
				style="Leaf";	
			}else{
				style="LeafOdd";
			}
			
		}else{
			int depth = node.getNodeDepth();
			
			depth = depth % 5;
			depth++;
			
			style = "Level"+depth;
		}
		return style;
	}
	
	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		String style = null;

		try{
			long ref = ((Long)itemId).longValue();
			FTree tree = (FTree) treeWrapper.getFocData();
			FNode node = tree.findNode(ref);
			style = getStyle(node);
		}catch(Exception e){
			Globals.logString("Could not get style for tree node itemId:"+itemId);
		}
		return style;
	}

	public boolean isAlternateLeafCoulors() {
		return alternateLeafCoulors;
	}

	public void setAlternateLeafCoulors(boolean alternateLeafCoulors) {
		this.alternateLeafCoulors = alternateLeafCoulors;
	}

}
