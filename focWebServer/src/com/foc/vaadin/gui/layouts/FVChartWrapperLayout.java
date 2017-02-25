package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.components.chart.FVChart;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVChartWrapperLayout extends FVVerticalLayout implements FocXMLGuiComponent {

	private Panel      chartPanel  = null;
	private FVChart    chart       = null;
	
	public FVChartWrapperLayout() {
		super(null);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if(chart != null){
			chart.dispose();
			chart = null;
		}
		chartPanel = null;
	}
	
	private void setChart(FVChart chart){
		this.chart = chart;
		if(chart != null){
      chartPanel = new Panel();
      
      VerticalLayout vLay = new VerticalLayout();
      vLay.setMargin(false);
      vLay.setSpacing(false);
      chartPanel.setContent(vLay);
      chartPanel.setContent(chart);
      addComponent(chart);
    }
	}
	
	public FVChart getFVChart(){
		return chart;
	}
	
	@Override
  public void setAttributes(Attributes attributes) {
		if(chart != null){
			chart.setAttributes(attributes);
		}
  }

	@Override
	public Attributes getAttributes() {
		Attributes attributes = null;
		if(chart != null){
			attributes = chart.getAttributes(); 
		}
		return attributes;
	}
	
	public void setTableOrTree(FocXMLLayout xmlLayout, ITableTree tableOrTree) {
		setChart((FVChart) tableOrTree);
	}
	
	public ITableTree getTableOrTree() {
    ITableTree result = null;
    
    if(getFVChart() != null && getFVChart().getPivotChartDelegate() != null){
    	result = getFVChart().getPivotChartDelegate().getTableTree();
    }
    
    return result;
		
  }
}
