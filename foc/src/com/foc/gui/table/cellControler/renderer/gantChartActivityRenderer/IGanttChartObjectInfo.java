package com.foc.gui.table.cellControler.renderer.gantChartActivityRenderer;

import java.sql.Date;

public interface IGanttChartObjectInfo {
	public static final int GANTT_NODE_TYPE_ACTIVITY       = 0;
	public static final int GANTT_NODE_TYPE_SUPER_ACTIVITY = 1;
	public static final int GANTT_NODE_TYPE_SUB_ACTIVITY   = 2;
	
	public Date       getMinimumStartDate();
  public Date       getMinimumEndDate();
  public Date       getMaximumStartDate();
  public Date       getMaximumEndDate();
	public Date       getForecastStartDate();
  public Date       getForecastEndDate();
	public Date       getActualStartDate();
  public Date       getActualEndDate();  
  public int        getGanttNodeType();
  public boolean    isInCriticalPath();
  public double     getPercentCompletion();
  public GanttStyle getGanttStyle();
  public String     getTextOnBarLeftSide();
  public String     getTextOnBarRightSide();
}
