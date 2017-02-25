package com.foc.vaadin.gui.components.chart;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import org.xml.sax.Attributes;

import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.pivot.FPivotBreakdown;
import com.foc.pivot.FPivotRowTree;
import com.foc.pivot.FPivotTable;
import com.foc.pivot.FPivotValue;
import com.foc.pivot.FPivotView;
import com.foc.property.FDouble;
import com.foc.property.FInt;
import com.foc.property.FProperty;
import com.foc.vaadin.gui.components.pivot.PivotChartDelegate;

@SuppressWarnings("serial")
public class FVPieChart extends FVChart{

	public FVPieChart(Attributes attributes) {
		super(attributes);
	}
	
	@Override
	public int getChartType(){
		return FVChart.CHART_TYPE_PIE;
	}
	
	@Override
	public void drawFVChart() {
		drawJFreeChart();
	}
	
	private HashMap<String, Double> getPieChartValues(){
		HashMap<String, Double> pieChartValues = new HashMap<String, Double>();
		
		PivotChartDelegate pivotChartDelegate = getPivotChartDelegate();
		FPivotRowTree pivotRowTree  = pivotChartDelegate != null ? pivotChartDelegate.getFPivotRowTree() : null;
		FPivotTable   pivotTable    = pivotChartDelegate != null ? pivotChartDelegate.getFPivotTable()   : null;
		
		if(pivotTable != null && pivotRowTree != null){
			FocList dataList = pivotTable.getNativeDataFocList();
			FPivotView pivotView  = pivotTable.getPivotView();
			FocList breakdownList = pivotView.getBreakdownList();
			
			for(int i=0; i<dataList.size(); i++){
				FocObject currentObject = dataList.getFocObject(i);
	      if(currentObject != null) {
	      	for(int j=0; j<breakdownList.size(); j++){
	      		FPivotBreakdown currentBreakdown      = (FPivotBreakdown) breakdownList.getFocObject(j);
//	          Object          currentPropertyObject = currentObject.iFocData_getDataByPath(currentBreakdown.getGroupBy());
	      		String proeprtyValue = currentBreakdown.getGroupByString(currentObject);
	      		
	      		FocList focList = pivotView != null ? pivotView.getValueList() : null;
      			if(focList != null && focList.size() > 0){
      				FPivotValue pivotValue = (FPivotValue) focList.getFocObject(0);
      				if(pivotValue != null && proeprtyValue != null){
	      				double value = 0;
	      				
	      				FProperty property = currentObject.getFocPropertyForPath(pivotValue.getDatapath());
	      				if(property instanceof FDouble || property instanceof FInt){
	      					value = property.getDouble(); 
	      				}else{
	      					value = 1;
	      				}

		            if(pieChartValues.containsKey(proeprtyValue.toString())){
		            	double sum = pieChartValues.get(proeprtyValue);
		            	sum +=value;
		            	pieChartValues.put(proeprtyValue, sum);
		            }else{
		            	pieChartValues.put(proeprtyValue, value);
		            }
      				}
      				
      				/*
      				double value = pivotValue != null ? currentObject.getPropertyDouble(pivotValue.getDatapath()) : 0;
      				if(proeprtyValue != null){
		            if(pieChartValues.containsKey(proeprtyValue.toString())){
		            	double sum = pieChartValues.get(proeprtyValue);
		            	sum +=value;
		            	pieChartValues.put(proeprtyValue, sum);
		            }else{
		            	pieChartValues.put(proeprtyValue, value);
		            }
		          }
		          */
	      		}
	      	}
	      }
			}

		}
		return pieChartValues;
	}
	
	private void drawJFreeChart(){
  	DefaultPieDataset pieDataset = new DefaultPieDataset();
  	//VaadinChart
    //DataSeries dataSeries = new DataSeries();
		
  	HashMap<String, Double> pieChartValues = getPieChartValues();
		Iterator<String> keyItr = pieChartValues.keySet().iterator();
		while(keyItr != null && keyItr.hasNext()){
			String key = keyItr.next();
			Double value = pieChartValues.get(key);
			if(value != null){
//					dataSeries.add(new DataSeriesItem(key, value.intValue())); for vaadin chart
				pieDataset.setValue(key, value.doubleValue());
			}
		}

		//Creating the Chart
		String chartTitle = getChartTitle() != null ? getChartTitle() : "JFree Chart";
		JFreeChart chartToBeWrapped = ChartFactory.createPieChart3D(chartTitle, pieDataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chartToBeWrapped.getPlot();
		plot.setStartAngle(290);
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setNoDataMessage("No data to display");
		
		attachChart(chartToBeWrapped);
		
//		org.vaadin.addon.JFreeChartWrapper jFreeChartWrapper = new org.vaadin.addon.JFreeChartWrapper(chartToBeWrapped){
//			
//			@Override
//			public UI getUI() {
//				return UI.getCurrent();
//			}
//			
//			@Override
//      public void attach() {
//          super.attach();
//          setResource("src", getSource());
//      }
//		};
//		
//		jFreeChartWrapper.attach();
//		addComponent(jFreeChartWrapper);
//		JFreeChartWrapper chartWrapper = new JFreeChartWrapper(chartToBeWrapped, RenderingMode.PNG, this);
//		chartWrapper.attach();
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
