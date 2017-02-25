package com.foc.web.modules.business;

import java.util.ArrayList;

import org.jfree.chart.JFreeChart;

import com.foc.vaadin.gui.components.chart.JFreeChartWrapper;
import com.foc.vaadin.gui.components.chart.JFreeChartWrapper.RenderingMode;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class Chart_Form extends FocXMLLayout {
	
	private ArrayList<JFreeChartWrapper> array = null;
	
	@Override
	public void dispose(){
		if(array != null){
			for(int i=0; i<array.size(); i++){
				if(array.get(i) != null) array.get(i).detach();
			}
		}
		array.clear();
		super.dispose();
	}

	public void addChart(JFreeChart chartToBeWrapped){
		JFreeChartWrapper chartWrapper = new JFreeChartWrapper(chartToBeWrapped, RenderingMode.PNG, this);
		chartWrapper.attach();
		
		if(array == null) array = new ArrayList<JFreeChartWrapper>();
	}
	
}
