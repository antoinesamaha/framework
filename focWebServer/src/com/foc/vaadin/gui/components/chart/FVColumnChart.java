package com.foc.vaadin.gui.components.chart;

import org.xml.sax.Attributes;

import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class FVColumnChart extends FVChart{
	
	public FVColumnChart(Attributes attributes) {
		super(attributes);
	}

	@Override
	public int getChartType(){
		return FVChart.CHART_TYPE_COLUMN;
	}

	@Override
	public void drawFVChart() {

	}

	@Override
	public void afterAddItem(FocObject fatherObject, FocObject newObject) {
	}

	@Override
	public boolean setRefreshGuiDisabled(boolean disabled) {
		return false;
	}

	@Override
	public void refreshEditable() {
	}
}
