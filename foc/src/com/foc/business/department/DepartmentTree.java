package com.foc.business.department;

import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.tree.TreeScanner;
import com.foc.tree.objectTree.FObjectNode;
import com.foc.tree.objectTree.FObjectTree;

public class DepartmentTree extends FObjectTree {
	
	public static final String ROOT_TITLE = "Departments";
	public static DepartmentTree departmentTree = null;
	
	public DepartmentTree(){
    super();
  	FocList list = DepartmentDesc.getInstance().getFocList();
  	list.loadIfNotLoadedFromDB();
    setDisplayFieldId(DepartmentDesc.FLD_NAME);
    growTreeFromFocList(list);
    getRoot().setTitle(ROOT_TITLE);
  }
	
	public void compute(){
	  scan(new TreeScanner<FObjectNode>() {
      @Override
      public boolean beforChildren(FObjectNode node) {
      	Department department = (Department) node.getObject();
        if(department != null){
          FProperty property =  department.getFocProperty(DepartmentDesc.FLD_END_DEPARTMENT);
          property.setValueLocked(!node.isLeaf() || department.isEndDepartment());
        }
        return true;
      }

      @Override
      public void afterChildren(FObjectNode node) {
      }
    });
	}
	
	public static DepartmentTree getInstance(){
		if(departmentTree == null){
			departmentTree = new DepartmentTree();
		}
		return departmentTree;
	}
	
}