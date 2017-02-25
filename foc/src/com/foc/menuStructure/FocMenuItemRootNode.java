package com.foc.menuStructure;

public class FocMenuItemRootNode extends FocMenuItemNode{
	private FocMenuItemTree tree = null;
	
	public FocMenuItemRootNode(String title, FocMenuItemTree tree){
		super(title, null);
		this.tree = tree;
	}
	
	public void dispose(){
		super.dispose();
		tree = null;
	}
	
	public FocMenuItemTree getTree(){
		return tree;
	}
}
