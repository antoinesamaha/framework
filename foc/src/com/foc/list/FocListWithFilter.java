package com.foc.list;

import com.foc.db.SQLFilter;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.filter.FilterCondition;
import com.foc.list.filter.FilterDesc;
import com.foc.list.filter.FocDescForFilter;
import com.foc.list.filter.FocListFilterBindedToList;
import com.foc.shared.dataStore.IFocData;

public class FocListWithFilter extends FocList {
	private FocListFilterBindedToList filter = null;

  private void init(FocDescForFilter filterFocDesc){
    if(filterFocDesc != null){
      FocConstructor constr = new FocConstructor(filterFocDesc, null);
      setFocListFilter((FocListFilterBindedToList)constr.newItem());
    }
  }
  
	public FocListWithFilter(FocDescForFilter filterFocDesc, FocLink focLink) {
		super(focLink);
    init(filterFocDesc);
	}
	
	public FocListWithFilter(FocDescForFilter filterFocDesc, FocObject masterObject, FocLink focLink, SQLFilter filter) {
    super(masterObject, focLink, filter);
    init(filterFocDesc);    
  }

  public void dispose(){
		super.dispose();
		disposeFilter();
	}
	
	private void disposeFilter(){
		if(filter != null){
			filter.dispose();
			filter = null;
		}
	}
	
	public void setFocListFilter(FocListFilterBindedToList filter){
		disposeFilter();
		this.filter = filter;
		if(this.filter != null){
			this.filter.setGuiFocList(this);
		}
	}
	
	public void reloadFromDB_Super(){
		super.reloadFromDB();
	}

	@Override
	public boolean includeObject_ByListFilter(FocObject obj){
		return this.filter != null && obj != null ? this.filter.includeObject(obj) : false;
	}
	
  public void reloadFromDB() {
  	if(filter != null){
  		if(filter.getFilterLevel() == FocListFilterBindedToList.LEVEL_MEMORY){
  			reloadFromDB_Super();
  		}
//      int filterLevel = filter.getFilterLevel();
//      filter.setFilterLevel(FocListFilterBindedToList.LEVEL_DATABASE);
  		filter.setActive(this, true);
//      filter.setFilterLevel(filterLevel);
  	}else{
  		reloadFromDB_Super();
  	}
  }
  
  public FocListFilterBindedToList loadFilterByReference(int filterRef){
  	if(filter != null){
	  	filter.setReference(filterRef);
	  	filter.load();
  	}
  	return filter;
  }
  
  public FocListFilterBindedToList getFocListFilter(){
  	return filter;
  }

  @Override
  protected void fillForeignObjectsProperties(FocObject newFocObj){
  	super.fillForeignObjectsProperties(newFocObj);
    FocListFilterBindedToList focListFilter = getFocListFilter();
    if(focListFilter != null){
	    FilterDesc filterDesc = focListFilter.getThisFilterDesc();
	    if(filterDesc != null && focListFilter.isActive()){
	      for(int i=0; i<filterDesc.getConditionCount(); i++){
	        FilterCondition cond = filterDesc.getConditionAt(i);
	        cond.forceFocObjectToConditionValueIfNeeded(focListFilter, newFocObj);
	      }
	    }
    }  	
  }
  
  @Override
  public FocObject newEmptyItem() {
    FocObject newFocObj = super.newEmptyItem();
    return newFocObj;
  }
  
	@Override
  public IFocData iFocData_getDataByPath(String path){
		IFocData result = null; 
		if(path != null && path.startsWith("FILTER")){
			String remainingPath = path.substring(6);
			if(remainingPath.length() > 0 && remainingPath.startsWith(".")){
				result = filter;
				result = result.iFocData_getDataByPath(remainingPath); 
			}else if(remainingPath.length()==0){
				result = filter;
			}else{
				result = super.iFocData_getDataByPath(path);
			}
		}else{
			result = super.iFocData_getDataByPath(path);
		}
    return result;
  }
}
