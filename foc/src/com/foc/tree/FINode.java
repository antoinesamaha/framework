package com.foc.tree;

public interface FINode<N extends FINode, O extends Object> {
  public void    dispose();
  public String  getTitle();
  public void    setTitle(String title);
  
  public N       getFatherNode();
  public void    setFatherNode(N fatherNode);
  
  public int     getNodeDepth();
  public boolean isLeaf();  
  public boolean isRoot();
  public N       getRootNode();
  public FTree   getTree();
  
  public N       addChild(String childTitle);
  
  public O       getObject();
  public void    setObject(O object);
  public void    scan(TreeScanner scanner);
  public void    scanVisible(TreeScanner scanner);
  
  public int     getChildCount();
  public N       getChildAt(int i);
}
