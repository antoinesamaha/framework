package com.foc.wrapper;

import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.tree.FTree;
import com.foc.tree.objectTree.FObjectTree;

public class WrapperTree extends FObjectTree{

  public int viewID = 0;
  public WrapperTree(FocList list, int viewID){
    super(true);
    setViewID(viewID);
    setFatherNodeId(FField.FLD_FATHER_NODE_FIELD_ID);
    setDisplayFieldId(FField.FLD_NAME);
    growTreeFromFocList(list);
    setColorMode(FTree.COLOR_MODE_PREDEFINED);
  }
  
  public int getViewID() {
    return viewID;
  }
  
  public void setViewID(int viewID) {
    this.viewID = viewID;
  }
}
