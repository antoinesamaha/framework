package com.foc.gui.table.cellControler.renderer.gantChartResourceRenderer;

import java.awt.Color;
import java.sql.Date;

public interface IGantChartResourceDrawingInfo{
	/*public int getActivityCount();
  	public double getActivityDurationAt(int i);
	  public Date getActivityStartDateAt(int i);
  	public String getActivityLabelAt(int i);
  	public ArrayList<Color> getColorArrayForActivityAt(int i);*/
  
  public int    getActivityIntervalCount();
  public Date   getActivityIntervalStartDateAt(int index);
  public Date   getActivityIntervalEndDateAt(int index);
  
  public int    getResourceTaskOccupationCountAt(int resourceEstimateQuantityIndex);
  public double getPercentageOccupationAt(int resourceEstimateQuantityIndex, int resourceTaskOccupationIndex);
  
  public Color  getColorForActivityIntervalAt(int timeInterval, int occupationIndex);
}
