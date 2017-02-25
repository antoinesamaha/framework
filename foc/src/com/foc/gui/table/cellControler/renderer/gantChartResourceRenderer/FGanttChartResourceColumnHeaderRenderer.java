package com.foc.gui.table.cellControler.renderer.gantChartResourceRenderer;

import java.awt.Component;
import javax.swing.JTable;

import com.foc.gui.table.cellControler.renderer.gantChartRenderer.BasicGanttScale;
import com.foc.gui.table.cellControler.renderer.gantChartRenderer.GanttColumnHeaderPanel;
public class FGanttChartResourceColumnHeaderRenderer extends FGanttChartResourceCellRenderer {
	public FGanttChartResourceColumnHeaderRenderer(BasicGanttScale gantScale){
		super(gantScale);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		GanttColumnHeaderPanel panel = new GanttColumnHeaderPanel(getGantScale());
		panel.set(table);
    return panel;
	}

}
