package com.fab.model.table;

import com.foc.list.FocList;
import com.foc.tree.objectTree.FObjectTree;

public class TableDefinitionTree extends FObjectTree {
	
	public static final String ROOT_TITLE = "Object Structure";
	public static TableDefinitionTree tree = null;
	
	public TableDefinitionTree(FocList list){
    super();
    //FocList list = DepartmentList.getInstance();
  	//list.loadIfNotLoadedFromDB();
    setDisplayFieldId(TableDefinitionDesc.FLD_NAME);
    growTreeFromFocList(list);
    getRoot().setTitle(ROOT_TITLE);
  }
	
	public static TableDefinitionTree getInstance(){
		if(tree == null){
			tree = new TableDefinitionTree(TableDefinitionDesc.getList(FocList.LOAD_IF_NEEDED));
		}
		return tree;
	}
	
}
