package com.foc.business.workflow;

import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class WFSiteList extends FocList{

	private WFSiteList(boolean rootsOnly){
		super(new FocLinkSimple(WFSiteDesc.getInstance()));

	  setDirectlyEditable(false);
	  setDirectImpactOnDatabase(true);
		
	  if(getListOrder() == null){
	    FocListOrder order = new FocListOrder(WFSiteDesc.FLD_NAME);
	    setListOrder(order);
	  }
	}

	private static WFSiteList list = null;
	public static WFSiteList getInstance(boolean loadIfNotLoaded){
		if(list == null){
			list = new WFSiteList(false);
		}
		if(loadIfNotLoaded){
			list.loadIfNotLoadedFromDB();
		}
		return list;
	}
	
	public static WFSiteList getInstance(){
		return getInstance(false);
	}
}
