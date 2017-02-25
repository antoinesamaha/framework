package com.foc.pivot;

import com.foc.list.FocList;

public class FPivotTable extends FPivotRowTree implements FPivotConst {
  
  public FPivotTable(FocList nativeDataFocList) {
  	super(nativeDataFocList);
  }

  public void dispose() {
  	super.dispose();
  }

}
