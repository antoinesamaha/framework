package com.foc.access;

import com.foc.tree.objectTree.FObjectNode;

public class FocLogLineNode extends FObjectNode<FocLogLineNode, FocLogLine>{

	public FocLogLineNode(String title, FocLogLineNode fatherNode) {
		super(title, fatherNode);
	}

	@Override
	public FocLogLineNode createChildNode(String childTitle) {
		return new FocLogLineNode(childTitle, this); 
	}
	
}
