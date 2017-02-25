package com.foc.business.workflow.signing;

import com.foc.desc.*;
import com.foc.list.filter.*;

/**
 * @author 01Barmaja
 */
public class WFTransWrapperFilter extends FocListFilterBindedToList{

  public WFTransWrapperFilter(FocConstructor constr) {
    super(constr);
    newFocProperties();
    setFilterLevel(FocListFilter.LEVEL_MEMORY);
  }

  /*
  public void setIsDriver(int operation, boolean lock){
    BooleanCondition transactionTypeCondition = (BooleanCondition) findFilterCondition(WFTransWrapperFilterDesc.COND_IS_DRIVER);
    if(lock){
      transactionTypeCondition.forceToValue(this, operation);
    }else{
      transactionTypeCondition.setValue(this, operation);  
    }
  }

  public void setIsStoreKeeper(int operation, boolean lock){
    BooleanCondition transactionTypeCondition = (BooleanCondition) findFilterCondition(WFTransWrapperFilterDesc.COND_IS_STORE_KEEPER);
    if(lock){
      transactionTypeCondition.forceToValue(this, operation);
    }else{
      transactionTypeCondition.setValue(this, operation);  
    }
  }
  */
}



