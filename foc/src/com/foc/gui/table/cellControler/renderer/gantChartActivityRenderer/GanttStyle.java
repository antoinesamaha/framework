package com.foc.gui.table.cellControler.renderer.gantChartActivityRenderer;

import java.awt.Color;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

public class GanttStyle extends FocObject{

  public GanttStyle(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public int getBarPositions(){
  	return getPropertyMultiChoice(GanttStyleDesc.FLD_BAR_POSITIONS);
  }
  
  public int getFirstBar(){
  	return getPropertyMultiChoice(GanttStyleDesc.FLD_FIRST_BAR);
  }

  public int getFirstBarFiller(){
  	return getPropertyMultiChoice(GanttStyleDesc.FLD_FIRST_BAR_FILLER);
  }

  public Color getFirstBarColor(){
  	return getPropertyColor(GanttStyleDesc.FLD_FIRST_BAR_COLOR);
  }

  public Color getFirstBarFillerColor(){
  	return getPropertyColor(GanttStyleDesc.FLD_FIRST_BAR_FILLER_COLOR);
  }
  
  public int getSecondBar(){
  	return getPropertyMultiChoice(GanttStyleDesc.FLD_SECOND_BAR);
  }

  public int getSecondBarFiller(){
  	return getPropertyMultiChoice(GanttStyleDesc.FLD_SECOND_BAR_FILLER);
  }

  public Color getSecondBarColor(){
  	return getPropertyColor(GanttStyleDesc.FLD_SECOND_BAR_COLOR);
  }

  public Color getSecondBarFillerColor(){
  	return getPropertyColor(GanttStyleDesc.FLD_SECOND_BAR_FILLER_COLOR);
  }

  public int getRelationshipMode(){
  	return getPropertyMultiChoice(GanttStyleDesc.FLD_RELATIONSHIP_MODE);
  }

  public Color getRelationshipColor(){
  	return getPropertyColor(GanttStyleDesc.FLD_RELATIONSHIP_COLOR);
  }
}
