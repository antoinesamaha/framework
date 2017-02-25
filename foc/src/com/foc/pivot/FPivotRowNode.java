package com.foc.pivot;

import java.util.ArrayList;

import com.foc.tree.objectTree.FObjectNode;

public class FPivotRowNode extends FObjectNode<FPivotRowNode, FPivotRow> {

  public FPivotRowNode(String title, FPivotRowNode fatherNode) {
    super(title, fatherNode);
  }

  @Override
  public FPivotRowNode createChildNode(String childTitle){
    return new FPivotRowNode(childTitle, this);
  }

	public String getTitle(){
    return getObject() != null ? getObject().getTitle() : super.getTitle();
	}

  public ArrayList<FPivotRowNode> getChildrenList(){
    ArrayList<FPivotRowNode> result = new ArrayList<FPivotRowNode>();
    
    for(int i=0; i<getChildCount(); i++){
      result.add(getChildAt(i));
    }
    
    return result;
  }

  public boolean isBreakdown_Descendent(String groupBy){
  	boolean isDescendent = false;
		FPivotRow curr = getObject();
		if(curr != null){
			isDescendent = curr.isBreakdown_Descendent(groupBy);
		}
		return isDescendent;
  }
  
  public boolean isBreakdown(String groupBy){
  	boolean isDescendent = false;
		FPivotRow curr = getObject();
		if(curr != null){
			isDescendent = curr.isBreakdown(groupBy);
		}
		return isDescendent;
  }
}
