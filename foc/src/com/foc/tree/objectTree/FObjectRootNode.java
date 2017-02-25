package com.foc.tree.objectTree;

import com.foc.tree.FTree;

public class FObjectRootNode extends FObjectNode{
  private FTree tree = null;
  
  public FObjectRootNode(String title, FTree tree){
    super(title, null);
    setTree(tree);
  }

  public void setTree(FTree tree) {
    this.tree = tree;
  }
  
  public FTree getTree() {
    return tree;
  }
}
