package com.foc.desc.dataModelTree;

import com.foc.desc.field.FField;
import com.foc.tree.objectTree.FObjectRootNode;
import com.foc.tree.objectTree.FObjectTree;

@SuppressWarnings("serial")
public class DataModelNodeTree extends FObjectTree {

  public DataModelNodeTree(DataModelNodeList list){
    setFatherNodeId(FField.FLD_FATHER_NODE_FIELD_ID);
    setDisplayFieldId(FField.FLD_NAME);
    growTreeFromFocList(list);
    sort();
    FObjectRootNode rootNode = (FObjectRootNode) getRoot();
    if(rootNode != null && list != null && list.getRootDesc() != null){
      rootNode.setTitle(list.getRootDesc().getStorageName());
    }
  }  
}
