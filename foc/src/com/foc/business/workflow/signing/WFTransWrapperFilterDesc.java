package com.foc.business.workflow.signing;

import com.foc.desc.*;
import com.foc.desc.field.FFieldPath;
import com.foc.list.filter.*;

/**
 * @author 01Barmaja
 */
public class WFTransWrapperFilterDesc extends FocDescForFilter{

	public static final String COND_TYPE        = "TYPE"        ;
	public static final String COND_CODE        = "CODE"        ;
	public static final String COND_DESCRIPTION = "DESCRIPTION" ;
	
  public WFTransWrapperFilterDesc() {
    super(WFTransWrapperFilter.class, FocDesc.NOT_DB_RESIDENT, "WF_TRANSACTION_WRAPPER_FILTER", true);
  }

	@Override
	public FilterDesc getFilterDesc() {
    if(filterDesc == null){
      filterDesc = new FilterDesc(WFTransactionWrapperDesc.getInstance());

      StringCondition strCond = new StringCondition(FFieldPath.newFieldPath(WFTransactionWrapperDesc.FLD_TRANSACTION_TYPE), COND_TYPE);
      filterDesc.addCondition(strCond);

      strCond = new StringCondition(FFieldPath.newFieldPath(WFTransactionWrapperDesc.FLD_TRANSACTION_CODE), COND_CODE);
      filterDesc.addCondition(strCond);

      strCond = new StringCondition(FFieldPath.newFieldPath(WFTransactionWrapperDesc.FLD_TRANSACTION_DESCRIPTION), COND_DESCRIPTION);
      filterDesc.addCondition(strCond);

      filterDesc.setNbrOfGuiColumns(1);
    }
    return filterDesc;
	}
	
  private static WFTransWrapperFilterDesc focDesc = null;
  public static WFTransWrapperFilterDesc getInstance() {
    if(focDesc == null){
      focDesc = new WFTransWrapperFilterDesc();
    }
    return focDesc;
  }	
}



