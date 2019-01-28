package com.foc.business.workflow.implementation.join;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.list.filter.FocListFilter;
import com.foc.list.filter.FocListFilterBindedToList;

@SuppressWarnings("serial")
public class WFLogJoinFilter extends FocListFilterBindedToList {

  public WFLogJoinFilter(FocDesc filterDesc) {
  	super(new FocConstructor(filterDesc, null));
  }
  
  public WFLogJoinFilter(FocConstructor constr) {
    super(constr);
    newFocProperties();
    setFilterLevel(FocListFilter.LEVEL_DATABASE);
  }
}



