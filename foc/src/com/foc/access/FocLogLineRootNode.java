package com.foc.access;

public class FocLogLineRootNode extends FocLogLineNode{

	private FocLogLineTree tree = null;
	
	public FocLogLineRootNode(String title, FocLogLineTree tree) {
		super(title, null);
		setTree(tree);
	}

	@Override
	public void dispose(){
		super.dispose();
		tree = null;
	}

	public void setTree(FocLogLineTree tree){
		this.tree = tree;
	}
	
	public FocLogLineTree getTree(){
		return tree;
	}
}
