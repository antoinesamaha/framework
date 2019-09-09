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
package com.foc.vaadin.gui.components.grid;

import java.util.ArrayList;

import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

@SuppressWarnings("serial")
public class FVGridRow extends FVHorizontalLayout{
		
	private FVGrid grid = null;
	private ArrayList<FVGridCell> cellArray = null;
	private int gridHeight = 0;
	
	public FVGridRow(FVGrid grid, int height) {
		super(null);
		this.grid = grid;
		this.gridHeight = height;
 
		FocXMLAttributes tableRowLayoutAttributes = new FocXMLAttributes();
		tableRowLayoutAttributes.addAttribute(FXML.ATT_CAPTION_MARGIN, "0");
		tableRowLayoutAttributes.addAttribute(FXML.ATT_WIDTH, "-1px");
		tableRowLayoutAttributes.addAttribute(FXML.ATT_MARGIN, "false");
		tableRowLayoutAttributes.addAttribute(FXML.ATT_SPACING, "false");
		setAttributes(tableRowLayoutAttributes);
		
		cellArray = new ArrayList<FVGridCell>();
	}
	
	public void dispose(){
		super.dispose();
		grid = null;
		if(cellArray != null){
			cellArray.clear();
			cellArray = null;
		}
	}

	public int getCellCount(){
		return (cellArray != null) ? cellArray.size() : 0;
	}
	
	public FVGridCell getCellAt(int at){
		return cellArray != null ? cellArray.get(at) : null;
	}

	public int getNextColumnIndex(){
		int colIdex = 0;
		for(int i=0; i<getCellCount(); i++){
			FVGridCell cell = getCellAt(i);
			colIdex += cell.getColPadding();
		}
		return colIdex;
	}
	
//	public void addCell(String strValue){
//		FVGridCell cell = new FVGridCell(this);
//		FVLabel lbl = new FVLabel(strValue);
//		cell.addComponent(lbl);		
//		addCell(cell);
//	}
		
	public void addCell(FVGridCell cell){
		cell.setHeight(gridHeight+"px");
		addComponent(cell);
		int colIndex = getNextColumnIndex();
		cellArray.add(cell);
		cell.setColIndex(colIndex);
	}
	
	public FVGrid getGrid() {
		return grid;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}
	
	public void adjustDimensions(){
		for(int i=0; i<getCellCount(); i++){
			FVGridCell cell = getCellAt(i);
			if(cell != null){
				cell.adjustDimensions();
			}
		}
	}
	
	public ArrayList<FVGridCell> getCellArray(){
		return cellArray;
	}
}
