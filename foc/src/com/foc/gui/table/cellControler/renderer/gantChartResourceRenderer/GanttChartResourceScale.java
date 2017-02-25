package com.foc.gui.table.cellControler.renderer.gantChartResourceRenderer;

import java.sql.Date;
import java.util.Calendar;

import com.foc.business.calendar.FCalendar;
import com.foc.gui.table.cellControler.renderer.gantChartRenderer.BasicGanttScale;
import com.foc.list.FocList;

public class GanttChartResourceScale extends BasicGanttScale {
	
	public GanttChartResourceScale(FCalendar calendar, int scaleView){
		super(calendar, scaleView);
	}
  
  public void calculateStartAndEndDates(FocList focList){
    long minDate = Long.MAX_VALUE;
    long maxDate = 0;
    
    for(int i = 0; i < focList.size(); i++){
      IGantChartResourceDrawingInfo drawingInfo = (IGantChartResourceDrawingInfo)focList.getFocObject(i);
      for(int j = 0; j < drawingInfo.getActivityIntervalCount(); j++){
        Date startDate = drawingInfo.getActivityIntervalStartDateAt(j);
        Date endDate = drawingInfo.getActivityIntervalEndDateAt(j);
        if(startDate.getTime() < minDate){
          minDate = startDate.getTime();
        }
        
        if(endDate.getTime() > maxDate){
          maxDate = endDate.getTime();
        }
      }
    }

    setStartDate(new Date(minDate));
    setEndDate(new Date(maxDate));
    Calendar cal = FCalendar.getInstanceOfJavaUtilCalandar();
    
    cal.setTime(getStartDate());
    cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
    cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
    cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
    setStartDate(new Date(cal.getTimeInMillis()));
    
    cal.setTime(getEndDate());
    cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
    cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
    cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
    setEndDate(new Date(cal.getTimeInMillis()));
    
    //Globals.logString("Start End Dates Calculated : "+getStartDate().toString()+" - "+getEndDate().toString());
  }
}
