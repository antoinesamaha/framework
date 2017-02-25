package com.foc.tree.objectTree;

import com.foc.desc.FocObject;
import com.foc.tree.FNode;

public class FObjectNode<N extends FObjectNode, O extends FocObject> extends FNode<N, O>{

	public FObjectNode(String title, N fatherNode){
		super(title, fatherNode);
	}

	public void dispose(){
    super.dispose();
	}
  
	@SuppressWarnings("unchecked")
	@Override
  public N createChildNode(String childTitle) {
    return (N) new FObjectNode(childTitle, this);
  }

  @Override
  public int getLevelDepth() {
    return 0;
  }
  
  @Override
  public boolean isSortable(){
  	FObjectTree tree = (FObjectTree) getTree();
  	return (tree != null) ? tree.isSortable() : false;
  }
}
