package com.foc.gui.dateInterval;
 
import java.awt.Component;
import java.sql.Date;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class MonthlyIntervalGuiDetailsPanel extends FPanel{

  private DateInterval stockreport = null;
  
  public MonthlyIntervalGuiDetailsPanel(FocObject object, int viewID){
  	this(object, false, viewID);
  }
  
  public MonthlyIntervalGuiDetailsPanel(FocObject object, boolean withMonthPart, int viewID){
    super();
    stockreport = (DateInterval)object;
    setName("By Month");
    int x = 0;
    
    add(stockreport, DateIntervalDesc.FLD_MONTH, x, 0);
    x += 2;    
    add(stockreport, DateIntervalDesc.FLD_YEAR, x, 0);
    x += 2;
    if(withMonthPart){
    	add(stockreport, DateIntervalDesc.FLD_MONTH_PART, x, 0);
    	x += 2;
    }
    
    Component comp = add(stockreport, DateIntervalDesc.FLD_FDATE, 0, 1);
    comp.setEnabled(true);
    comp = add(stockreport, DateIntervalDesc.FLD_LDATE, 2, 1);
    comp.setEnabled(true);
  }

  public void dispose(){
    super.dispose();
    stockreport = null;
  }
  
  public DateInterval getStockreport() {
    return stockreport;
  }
  
  protected Date getFirstDate(){
    return stockreport.getPropertyDate(DateIntervalDesc.FLD_FDATE); 
  }
  
  protected Date getLastDate(){
    return stockreport.getPropertyDate(DateIntervalDesc.FLD_LDATE); 
  }
  
}
