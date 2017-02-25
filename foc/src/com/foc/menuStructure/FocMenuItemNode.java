package com.foc.menuStructure;

import com.foc.tree.objectTree.FObjectNode;

public class FocMenuItemNode extends FObjectNode<FocMenuItemNode, FocMenuItem>{

	public FocMenuItemNode(String title, FocMenuItemNode fatherNode) {
		super(title, fatherNode);
	}
	
	@Override
	public FocMenuItemNode createChildNode(String childTitle) {
		return new FocMenuItemNode(childTitle, this); 
	}
	
	@Override
	public boolean isVisible(){
		boolean vis = super.isVisible();
		return vis;
	}

}
