package com.foc.formula;

public class FFormulaRootNode extends FFormulaNode{

	private FFormulaTree tree = null;

	public FFormulaRootNode(FFormulaTree tree){
		super();
		setTree(tree);
	}
	
	public void dispose(){
		super.dispose();
		tree = null;
	}
	
	public void setTree(FFormulaTree tree) {
		this.tree = tree;
	}

	public FFormulaTree getTree(){
		return tree;
	}
}
