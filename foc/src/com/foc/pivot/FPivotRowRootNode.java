package com.foc.pivot;

public class FPivotRowRootNode extends FPivotRowNode {

  private FPivotRowTree tree = null;
  
  public FPivotRowRootNode(String title, FPivotRowTree tree) {
    super(title, null);
    setTree(tree);
  }
  
  @Override
  public void dispose(){
    super.dispose();
    tree = null;
  }
  
  public void setTree(FPivotRowTree tree){
    this.tree = tree;
  }
  
  public FPivotRowTree getTree(){
    return tree;
  }

}
