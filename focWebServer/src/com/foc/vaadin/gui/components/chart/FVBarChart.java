package com.foc.vaadin.gui.components.chart;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.data.category.DefaultCategoryDataset;
import org.xml.sax.Attributes;

import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.pivot.FPivotBreakdown;
import com.foc.pivot.FPivotRowTree;
import com.foc.pivot.FPivotTable;
import com.foc.pivot.FPivotView;
import com.foc.vaadin.gui.components.pivot.PivotChartDelegate;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

@SuppressWarnings("serial")
public class FVBarChart extends FVChart{

	public FVBarChart(Attributes attributes) {
		super(attributes);
	}
	
	@Override
	public int getChartType(){
		return FVChart.CHART_TYPE_BAR;
	}

	@Override
	public void drawFVChart() {
		drawJFreeChart();
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
	
	private void drawJFreeChart(){
  	DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		
  	HashMap<String, Double> barChartValues = getBarChartValues();
		Iterator<String> keyItr = barChartValues.keySet().iterator();
		while(keyItr != null && keyItr.hasNext()){
			String key = keyItr.next();
			Double value = barChartValues.get(key);
			if(value != null){
//				categoryDataset.addValue(value.doubleValue(), key, );
			}
		}
		String chartTitle = getChartTitle() != null ? getChartTitle() : "JFree Chart";
//		JFreeChart chartToBeWrapped = ChartFactory.createBarChart3D(chartTitle, categoryAxisLabel, valueAxisLabel, categoryDataset, orientation, true, true, false);
//		CategoryPlot plot = (CategoryPlot) chartToBeWrapped.getPlot();
//		plot.setForegroundAlpha(0.5f);
//		plot.setNoDataMessage("No data to display");
//		JFreeChartWrapper chartWrapper = new JFreeChartWrapper(chartToBeWrapped, RenderingMode.PNG, this);
//		chartWrapper.attach();
	}
	
	private HashMap<String, Double> getBarChartValues(){
		HashMap<String, Double> barChartValues = new HashMap<String, Double>();
		
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
	      		String propeprtyValue = currentBreakdown.getGroupByString(currentObject);
	          if(propeprtyValue != null){
	            if(barChartValues.containsKey(propeprtyValue.toString())){
	            	double sum = barChartValues.get(propeprtyValue);
	            	sum +=1;
	            	barChartValues.put(propeprtyValue, sum);
	            }else{
	            	barChartValues.put(propeprtyValue, 1.0);
	            }
	          }
	      	}
	      }
			}

		}
		return barChartValues;
	}
}
