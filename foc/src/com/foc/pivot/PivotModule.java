package com.foc.pivot;

import com.foc.admin.FocVersion;
import com.foc.desc.FocModule;

public class PivotModule extends FocModule {

  public PivotModule() {
    FocVersion.addVersion("PIVOT", "pivot 1.0", 1000);
  }

  @Override
  public void declareFocObjectsOnce() {
    declareFocDescClass(FPivotValueDesc.class);
    declareFocDescClass(FPivotBreakdownDesc.class);
    declareFocDescClass(FPivotViewDesc.class);
  }
  
  
  private static PivotModule pivotModule = null;
  public static PivotModule getInstance(){
    if(pivotModule == null){
      pivotModule = new PivotModule();
    }
    return pivotModule;
  }
}
