package com.foc.gui.tree;

import com.foc.gui.treeTable.FGTreeInTable;
import com.foc.gui.treeTable.FTreeTableModel;
import com.foc.tree.FNode;
import com.foc.tree.TreeScanner;

public class TreeCollapseScanner implements TreeScanner<FNode>{

	private FTreeTableModel treeTableModel = null;
	private int             level          = -1;
	
	public TreeCollapseScanner(FTreeTableModel treeTableModel, int level){
		this.level          = level;
		this.treeTableModel = treeTableModel;
	}
	
	public void dispose(){
		treeTableModel = null;
	}
	
  public void afterChildren(FNode node) {
    
  }

  public boolean beforChildren(FNode node) {
  	if(treeTableModel != null){
	    FGTreeInTable guiTree = treeTableModel.getGuiTree();    
	    if(node.getNodeDepth() == level){
	      int row = treeTableModel.getRowForNode(node);
	      guiTree.collapseRow(row);
	    }
  	}    
    return true;
  }
}
