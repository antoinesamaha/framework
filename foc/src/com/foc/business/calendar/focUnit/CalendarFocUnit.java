package com.foc.business.calendar.focUnit;

import com.foc.fUnit.FocTestCase;
import com.foc.fUnit.FocTestSuite;

public class CalendarFocUnit extends FocTestCase {

  public CalendarFocUnit(FocTestSuite testSuite, String functionName){
    super(testSuite, functionName);
  }

  protected void menuBarOpenCurrencyGeneralConfiguration(){
    menu_ClickByPath(new String[]{"Currency", "Configuration"});
  }
}
