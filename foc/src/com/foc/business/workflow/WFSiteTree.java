package com.foc.business.workflow;

import com.foc.Globals;
import com.foc.business.company.Company;
import com.foc.list.FocList;
import com.foc.tree.objectTree.FObjectTree;

public class WFSiteTree extends FObjectTree {
	
	public static final String ROOT_TITLE = "Site Hierarchy";
	public static WFSiteTree wFAreaTree = null;
	
	private WFSiteTree(){
    super();
    Company comp = Globals.getApp().getCurrentCompany();
    if(comp != null){
//	  	FocList list = comp.getSiteList();
//    	FocListWrapper list = comp.newFocListWrapperForCurrentCompany();
    	FocList list = WFSiteDesc.getInstance().getFocList(FocList.LOAD_IF_NEEDED);
    	
	  	list.loadIfNotLoadedFromDB();
	    setDisplayFieldId(WFSiteDesc.FLD_NAME);
	    growTreeFromFocList(list);
	    getRoot().setTitle(ROOT_TITLE);
    }
  }
	
	public static WFSiteTree newInstance(){
		WFSiteTree wFAreaTree = new WFSiteTree();
		return wFAreaTree;
	}
}
