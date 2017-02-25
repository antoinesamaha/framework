package com.foc.admin;

import java.util.Comparator;

import com.foc.list.FocList;
import com.foc.tree.objectTree.FObjectNode;
import com.foc.tree.objectTree.FObjectTree;

public class MenuAccessRightObjectTree extends FObjectTree{

  @SuppressWarnings("unchecked")
	public MenuAccessRightObjectTree(FocList list, int viewID){
    super();
    setFatherNodeId(MenuRightsDesc.FLD_FATHER_MENU_RIGHT);
    setDisplayFieldId(MenuRightsDesc.FLD_MENU_TITLE);
    
    growTreeFromFocList(list);
    setSortable(new Comparator<FObjectNode>(){
			public int compare(FObjectNode o1, FObjectNode o2) {
				int compare = 0;
				MenuRights r1 = (MenuRights) o1.getObject();
				MenuRights r2 = (MenuRights) o2.getObject();
				compare = r1.getTitle().compareTo(r2.getTitle());
				return compare;
			}
    });
    sort();
  }
}
