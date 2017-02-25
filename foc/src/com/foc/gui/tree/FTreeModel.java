package com.foc.gui.tree;

import javax.swing.tree.TreePath;

import com.foc.Globals;
import com.foc.tree.FNode;
import com.foc.tree.FTree;

public class FTreeModel extends FAbstractTreeModel {

	private FTree tree = null;
	
	public FTreeModel(FTree tree){
		this.tree = tree;
  }
	
	public FTree getTree(){
		return tree;
	}
	
  public void dispose(){
    tree = null;
  }
  
	public Object getChild(Object parent, int index) {
		FNode parentNode = (FNode) parent;
    FNode child = parentNode.getVisibleChildAt(index);
		return child;
	}

	public int getChildCount(Object parent) {
		FNode parentNode = (FNode) parent;
    int count = (parentNode == null || parentNode.getNodeDepth() >= tree.getDepthVisibilityLimit()) ? 0 : parentNode.getVisibleChildCount();    
    return count;
	}

	public int getIndexOfChild(Object parent, Object child) {
		FNode parentNode = (FNode) parent;
		return parentNode.findChildIndex((FNode)child);
	}

	public Object getRoot() {		
		return tree.getRoot();
	}

	public boolean isLeaf(Object node){
    FNode parentNode = (FNode) node;
    boolean isLeaf = false;
    try{
      isLeaf = parentNode.getNodeDepth() >= tree.getDepthVisibilityLimit() ? true : parentNode.isLeaf();
    }catch(Exception e){
      Globals.logException(e);
    }
		return isLeaf;
  }

	public void valueForPathChanged(TreePath path, Object newValue) {
	}

}
