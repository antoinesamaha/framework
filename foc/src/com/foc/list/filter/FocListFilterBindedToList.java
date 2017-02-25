// MEMORY LEVEL
// DATABASE LEVEL
// COMMON LEVEL

/*
 * Created on Jul 9, 2005
 */
package com.foc.list.filter;

import com.foc.desc.FocConstructor;
import com.foc.list.FocList;
import com.foc.list.FocListWithFilter;

/**
 * @author 01Barmaja
 */
public class FocListFilterBindedToList extends FocListFilter {  
	private static final String FILTER_TABLE_NAME_SUFFIX = "_FILTER";
  private FocListWithFilter focList = null;
  
  public FocListFilterBindedToList(FocConstructor constr){
    super(constr);
    setFilterLevel(FocListFilter.LEVEL_DATABASE);
  }

	public void dispose(){
  	super.dispose();
  	focList = null;
  }

  public FocList getGuiFocList(){ // It was getFocList()changed to getGuiFocList because of getSelectionPanel = null   
  	return focList;
  }

  public void setGuiFocList(FocListWithFilter focList){ // It was getFocList()changed to getGuiFocList because of getSelectionPanel = null   
  	this.focList = focList;
  }

  public void setActive(FocListWithFilter focList, boolean active) {
  	setGuiFocList(focList);
  	setActive(active);
  }
  
  @Override
	public void reloadListFromDatabase() {
		focList.reloadFromDB_Super();
	}
	
 /* public void refreshDisplay(){
  }*/
  
	public static String getFilterTableName(String baseTableName){
		return baseTableName == null ? null : baseTableName + FocListFilterBindedToList.FILTER_TABLE_NAME_SUFFIX;
	}

	@Override
	public FilterDesc getThisFilterDesc() {
		FocDescForFilter focDesc = (FocDescForFilter) getThisFocDesc();
		return focDesc.getFilterDesc();
	}
}