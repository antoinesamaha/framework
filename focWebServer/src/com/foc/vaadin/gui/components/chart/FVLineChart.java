/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin.gui.components.chart;

import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.xml.sax.Attributes;

import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.pivot.FPivotRow;
import com.foc.pivot.FPivotRowDesc;
import com.foc.pivot.FPivotRowNode;
import com.foc.pivot.FPivotRowRootNode;
import com.foc.pivot.FPivotRowTree;
import com.foc.pivot.FPivotTable;
import com.foc.pivot.FPivotValue;
import com.foc.pivot.FPivotView;
import com.foc.vaadin.gui.components.pivot.PivotChartDelegate;
import com.foc.vaadin.gui.xmlForm.FXML;

@SuppressWarnings("serial")
public class FVLineChart extends FVChart{

	private String xAxisName = "";
	private String yAxisName = "";
	
	public FVLineChart(Attributes attributes) {
		super(attributes);
		this.yAxisName = attributes.getValue(FXML.TAG_CHART_XAXIS);
		this.xAxisName = attributes.getValue(FXML.TAG_CHART_YAXIS);
	}
	
	@Override
	public int getChartType(){
		return FVChart.CHART_TYPE_LINE;
	}

	@Override
	public void drawFVChart() {
//		drawJFreeChart();
		getLineChartValues();
	}

	private void pushValuesForNode(FPivotRowNode node, String xTitle, String titleComingFromNode, FocList columnList, DefaultCategoryDataset lineDataset){
		FPivotRow pivotRow = node.getObject();
		
		for (int i = 0; i < columnList.size(); i++) {
      FPivotValue column = (FPivotValue) columnList.getFocObject(i);
      
      String dataPath = column.getDatapath();
      if(!dataPath.equals(FPivotRowDesc.FNAME_TITLE) && !dataPath.equals(FPivotRowDesc.FNAME_DESCRIPTION)){
	      double value = pivotRow.getDoubleforColumn(column);
	
	    	String title = titleComingFromNode;
	    	if(title == null){
	    		title = column.getTitle();
	    		lineDataset.addValue(value, title, xTitle);        		
	    		break;
	    	}else{
	    		lineDataset.addValue(value, title, xTitle);
	    	}
      }

/*      
      String dataPath = column.getDatapath();
      if(!dataPath.equals(FPivotRowDesc.FNAME_TITLE) && !dataPath.equals(FPivotRowDesc.FNAME_DESCRIPTION)){
        FProperty currentProperty = pivotRow.getPropertyForColumn(dataPath);
  				
        if (currentProperty instanceof FDouble || currentProperty instanceof FInt) {
        	double value = currentProperty.getDouble();
        	
        	String title = titleComingFromNode;
        	if(title == null){
        		title = column.getTitle();
        		lineDataset.addValue(value, title, xTitle);        		
        		break;
        	}else{
        		lineDataset.addValue(value, title, xTitle);
        	}
        }
      }
      */
    }
	}
	
  private HashMap<String, Double> getLineChartValues(){
		HashMap<String, Double> lineChartValues = new HashMap<String, Double>();
		
		PivotChartDelegate pivotChartDelegate = getPivotChartDelegate();
		FPivotRowTree pivotRowTree  = pivotChartDelegate != null ? pivotChartDelegate.getFPivotRowTree() : null;
		FPivotTable   pivotTable    = pivotChartDelegate != null ? pivotChartDelegate.getFPivotTable()   : null;
		
		if(pivotTable != null && pivotRowTree != null){
			FocList dataList = pivotTable.getNativeDataFocList();
			FPivotView pivotView  = pivotTable.getPivotView();
		
			DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();

			FPivotRowTree rowTree = pivotTable.getTree();
			FPivotRowRootNode rootNode = (FPivotRowRootNode) rowTree.getRoot();
			for(int x=0; x<rootNode.getChildCount(); x++){
				FPivotRowNode xNode = rootNode.getChildAt(x);
				
				String xTitle = xNode.getObject() != null ? xNode.getObject().getTitle() : "";
				
				//2 cases:
				//1 - If there are no sub children to the xAxis node level, then we go to the Columns and we consider 
				//The different columns to be the different curves
				//2 - If there are sub children the the xAxis node level, then we scan them and we consider that these
				//are the different curves and there is only one column.
				if(xNode.getChildCount() == 0){
					pushValuesForNode(xNode, xTitle, null, pivotView.getValueList(), lineDataset);
				}else{
					for(int l=0; l<xNode.getChildCount(); l++){
						FPivotRowNode lNode = xNode.getChildAt(l);
						String lTitle = lNode.getObject() != null ? lNode.getObject().getTitle() : "";
						
						pushValuesForNode(lNode, xTitle, lTitle, pivotView.getValueList(), lineDataset);
					}
				}
			}
			
			String chartTitle = getChartTitle() != null ? getChartTitle() : "JFree Chart";
			JFreeChart chartToBeWrapped = ChartFactory.createLineChart(chartTitle, getxAxisName(), getyAxisName(), lineDataset, PlotOrientation.VERTICAL, true, true, false);
			
			attachChart(chartToBeWrapped);
		}
		return lineChartValues;
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

	public String getxAxisName() {
		return xAxisName;
	}

	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getyAxisName() {
		return yAxisName;
	}

	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}	
}
