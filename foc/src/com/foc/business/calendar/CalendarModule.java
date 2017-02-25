package com.foc.business.calendar;

import com.foc.*;
import com.foc.desc.*;
import com.foc.menu.FMenuAction;
import com.foc.menu.FMenuItem;
import com.foc.menu.FMenuList;

/**
 * @author 01Barmaja
 */
public class CalendarModule extends FocModule {

  public CalendarModule() {
  }
  
  public static FMenuItem addMenu(FMenuList list){
    FMenuItem calendarItem = new FMenuItem("Calendar", 'C', new FMenuAction(FCalendarDesc.getInstance(), true));
    list.addMenu(calendarItem);
    return calendarItem;
  }

  @Override
  public void declareFocObjectsOnce() {
    Application app = Globals.getApp();
    declareFocDescClass(FCalendarDesc.class);
    declareFocDescClass(WeekShiftExceptionDesc.class);
    declareFocDescClass(HolidayDesc.class);
    declareFocDescClass(WeekShiftDesc.class);
    declareFocDescClass(DayShiftDesc.class);
    declareFocDescClass(WorkShiftDesc.class);
  }
  
  public void declare() {
    Application app = Globals.getApp();
    app.declareModule(this);
  }

  public void addApplicationMenu(FMenuList menuList) {
  }

  public void addConfigurationMenu(FMenuList menuList) {
  }

  public void afterAdaptDataModel() {
  }

  public void afterApplicationEntry() {
  }

  public void afterApplicationLogin() {
  }

  public void beforeAdaptDataModel() {
  }

  public void dispose() {
  }
  
  private static CalendarModule module = null;
  public static CalendarModule getInstance(){
    if(module == null){
      module = new CalendarModule();
    }
    return module;
  }
}
