package com.foc.business.division;

import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.tree.TreeScanner;
import com.foc.tree.objectTree.FObjectNode;
import com.foc.tree.objectTree.FObjectTree;

@SuppressWarnings("serial")
public class DivisionTree extends FObjectTree {

	public static final String ROOT_TITLE = "Departments";
	public static DivisionTree departmentTree = null;
	
	public DivisionTree(){
    super();
  	FocList list = DivisionDesc.getInstance().getFocList();
  	list.loadIfNotLoadedFromDB();
    setDisplayFieldId(DivisionDesc.FLD_NAME);
    growTreeFromFocList(list);
    getRoot().setTitle(ROOT_TITLE);
  }
	
	public void compute(){
	  scan(new TreeScanner<FObjectNode>() {
      @Override
      public boolean beforChildren(FObjectNode node) {
      	Division department = (Division) node.getObject();
        if(department != null){
          FProperty property =  department.getFocProperty(DivisionDesc.FLD_END_DIVISION);
          property.setValueLocked(!node.isLeaf() || department.isEndDivision());
        }
        return true;
      }

      @Override
      public void afterChildren(FObjectNode node) {
      }
    });
	}
	
	public static DivisionTree getInstance(){
		if(departmentTree == null){
			departmentTree = new DivisionTree();
		}
		return departmentTree;
	}
}
