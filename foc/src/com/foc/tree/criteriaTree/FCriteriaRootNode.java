package com.foc.tree.criteriaTree;

import com.foc.tree.FTree;

public class FCriteriaRootNode extends FCriteriaNode {
  private FTree tree = null;
  
  public FCriteriaRootNode(String title, FNodeLevel info, FTree tree){
    super(title, info, null);
    setTree(tree);
  }

  public void dispose(){
  	super.dispose();
  	tree = null;
  }
  
  public void setTree(FTree tree) {
    this.tree = tree;
  }
  
  public FTree getTree() {
    return tree;
  }
  
  public void setTitle(String title){
    this.title = title;
  }
}
