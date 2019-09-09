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
