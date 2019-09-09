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
package com.foc.vaadin.gui.components.pivot;

import org.xml.sax.Attributes;

import com.foc.desc.FocDesc;
import com.foc.list.FocList;
import com.foc.pivot.FPivotTable;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.IPivotGrid;
import com.foc.vaadin.gui.components.treeGrid.FVTreeGrid;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

@SuppressWarnings("serial")
public class FVPivotGrid extends FVTreeGrid implements IPivotGrid{

	private PivotChartDelegate pivotChartDelegate = null;
	private FPivotTable        pivotTable = null;

	public void dispose(){
		super.dispose();
		if(pivotChartDelegate != null){
			pivotChartDelegate.dispose();
			pivotChartDelegate = null;
		}
		if(pivotTable != null){
			pivotTable.dispose();
			pivotTable = null;
		}
	}
	
	public int getOperation() {
		return getPivotChartDelegate().getOperation();
	}

	public void setOperation(int operation) {
		getPivotChartDelegate().setOperation(operation);
	}

	public boolean shouldCollapse() {
		return getPivotChartDelegate().shouldCollapse();
	}

	public void setCollapsed(boolean collapsed) {
		getPivotChartDelegate().setCollapsed(collapsed);
	}

	public boolean shouldRemoveEmptyNodes() {
		return getPivotChartDelegate().shouldRemoveEmptyNodes();
	}

	public void setRemoveEmptyNodes(boolean remove) {
		getPivotChartDelegate().setRemoveEmptyNodes(remove);
	}
	
	public boolean shouldRemoveOnlyChild() {
		return getPivotChartDelegate().shouldRemoveOnlyChild();
	}
	
	public void setRemoveOnlyChild(boolean remove){
		getPivotChartDelegate().setRemoveOnlyChild(remove);
	}

	public FVPivotGrid(Attributes attributes) {
		super(null, attributes);
		getPivotChartDelegate().init(attributes, FVPivotGrid.this);
		setAttributes(attributes);

		String attOperation = attributes.getValue(FXML.ATT_PIVOT_OPERATION);

		if(attOperation != null && !attOperation.equals("")){
			// Add more operations here.

			if(attOperation.equalsIgnoreCase("sum")){
				setOperation(PivotChartDelegate.PIVOT_OPERATION_SUM);
			}

		}

		String attCollapsed = attributes.getValue(FXML.ATT_PIVOT_COLLAPSED);

		if(attCollapsed != null && !attCollapsed.equals("")){
			if(attCollapsed.equalsIgnoreCase("true")){
				setCollapsed(true);
			}
		}

		String attRemoveEmpty = attributes.getValue(FXML.ATT_PIVOT_REMOVE_EMPTY);
		if(attRemoveEmpty != null && !attRemoveEmpty.equals("")){
			if(attRemoveEmpty.equalsIgnoreCase("false")){
				setRemoveEmptyNodes(false);
			}
		}
		
		String attRemoveOnly = attributes.getValue(FXML.ATT_PIVOT_REMOVE_ONLY);
		if(!Utils.isStringEmpty(attRemoveOnly) && attRemoveOnly.equalsIgnoreCase("true")){
			setRemoveOnlyChild(true);
		}

	}

	public FocList getDataFocList() {
		return pivotTable != null ? pivotTable.getNativeDataFocList() : null;
	}

	@Override
	public void setDataFocList(FocList list) {
		if(pivotTable == null){
			pivotTable = new FPivotTable(list);
		}else{
			pivotTable.dispose_Contents();
			pivotTable.setNativeDataFocList(list);
		}
	}

	@Override
	public FocDesc getFocDesc() {
		return getPivotTable() != null ? getPivotTable().getRowFocDesc() : null;
	}

	@Override
	public void applyFocListAsContainer() {
		getPivotChartDelegate().applyFocListAsContainer();
		if(getPivotTable() != null){
			setFocData(getPivotTable().getTree());
		}
  	super.applyFocListAsContainer();
	}

	@Override
	public FVTableColumn addColumn(FocXMLAttributes attributes) {
		getPivotChartDelegate().addColumn(attributes);
		return super.addColumn(attributes);
	}
	
	@Override
  public String getXMLType() {
    return FXML.TAG_PIVOT;
  }
	
	public PivotChartDelegate getPivotChartDelegate() {
    if(pivotChartDelegate == null){
    	pivotChartDelegate = new PivotChartDelegate();
    }
    return pivotChartDelegate;
  }
	
	@Override
	public FPivotTable getPivotTable() {
		return pivotTable;
	}
	
	@Override
	public void computeFooter(FVTableColumn col) {
		/*
		String footerFormula = col.getFooterFormula();
    if(footerFormula != null){
      footerFormula = footerFormula.toUpperCase();
      if(footerFormula != null && footerFormula.equals("SUM_VISIBLE")){
        double value = 0;
        FTree tree = getFTree();
        if(tree != null){
        	FocList focList = tree.getFocList();
        	if(focList != null){
        		for(int i=0; i<focList.size(); i++){
        			FocObject focObject = focList.getFocObject(i);
        			if(focObject != null){
	        			IFocData focData = focObject.iFocData_getDataByPath(col.getDataPath());
	              if(focData instanceof FProperty){
	                FProperty property = (FProperty)focData;
	                value += property != null ? property.getDouble() : 0;
	              }
        			}
        		}
        	}
        }
        
        Format format = FNumField.newNumberFormat(20, 2);
        setColumnFooter(col.getName() , format.format(value));
      }else{
      	super.computeFooter(col);
      }
    }
    */
	}

	@Override
	public void setSelectable(boolean selectable) {
		// TODO Auto-generated method stub
		
	}
}
