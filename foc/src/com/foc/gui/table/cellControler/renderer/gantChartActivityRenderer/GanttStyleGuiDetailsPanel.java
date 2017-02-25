package com.foc.gui.table.cellControler.renderer.gantChartActivityRenderer;

import com.foc.desc.FocObject;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class GanttStyleGuiDetailsPanel extends FPanel{

	public GanttStyleGuiDetailsPanel(FocObject obj, int viewID){
		super("Gantt Style", FILL_NONE);
		GanttStyle ganttStyle = (GanttStyle) obj;
		
		int x = 0;
		int y = 0;
		
		add(ganttStyle, GanttStyleDesc.FLD_NAME         , x, y++);
		add(ganttStyle, GanttStyleDesc.FLD_BAR_POSITIONS, x, y++);
		
		add(ganttStyle, GanttStyleDesc.FLD_FIRST_BAR, x, y);
		addField(ganttStyle, GanttStyleDesc.FLD_FIRST_BAR_COLOR, x+2, y++);
		
		add(ganttStyle, GanttStyleDesc.FLD_FIRST_BAR_FILLER, x, y);
		addField(ganttStyle, GanttStyleDesc.FLD_FIRST_BAR_FILLER_COLOR, x+2, y++);

		add(ganttStyle, GanttStyleDesc.FLD_SECOND_BAR, x, y);
		addField(ganttStyle, GanttStyleDesc.FLD_SECOND_BAR_COLOR, x+2, y++);
		
		add(ganttStyle, GanttStyleDesc.FLD_SECOND_BAR_FILLER, x, y);
		addField(ganttStyle, GanttStyleDesc.FLD_SECOND_BAR_FILLER_COLOR, x+2, y++);

		add(ganttStyle, GanttStyleDesc.FLD_RELATIONSHIP_MODE, x, y);
		addField(ganttStyle, GanttStyleDesc.FLD_RELATIONSHIP_COLOR, x+2, y++);

		FValidationPanel vPanel = showValidationPanel(true);
		vPanel.addSubject(ganttStyle);
	}
}
