package com.foc.vaadin.gui.components.grid;

import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class FVGridCell extends FVHorizontalLayout{
	
	private FVGridRow    row        = null;
	private int          colIndex   = -1;
	private int          colPadding = 1;
	private int          rowPadding = 1;
	private Component    component  = null;
	
	public FVGridCell(FVGridRow row) {
		super(null);

		this.row = row;
		
		FocXMLAttributes tableCellLabelAttributes = new FocXMLAttributes();
		tableCellLabelAttributes.addAttribute(FXML.ATT_BORDER, "true");
		tableCellLabelAttributes.addAttribute(FXML.ATT_CAPTION_MARGIN, "0");
		tableCellLabelAttributes.addAttribute(FXML.ATT_SPACING, "false");
//		tableCellLabelAttributes.addAttribute(FXML.ATT_WIDTH, "130px");
		tableCellLabelAttributes.addAttribute(FXML.ATT_MARGIN, "false");
		
		setAttributes(tableCellLabelAttributes);
	}
	
	public void dispose(){
		super.dispose();
		row    = null;
		component = null;
	}
	
	public void addInnerContent(Component component, Alignment alignment){
		addInnerContent(component);
		setComponentAlignment(component, alignment);
	}
	
	public void addInnerContent(Component component){
		this.component = component;
		addComponent(component);
	}
	
	public void addInnerContent(Component component, String height){
		component.setHeight(height);
		addInnerContent(component);
	}
	
	public Component getInnerContent(){
		return component;
	}

	public FVGridRow getRow() {
		return row;
	}

	public FVGrid getGrid() {
		return getRow() != null ? getRow().getGrid() : null;
	}

	public FVGridColumn getColumn() {
		return getGrid() != null ? getGrid().getColAt(colIndex) : null;
	}

	public int getColPadding() {
		return colPadding;
	}

	public void setColPadding(int colPadding) {
		this.colPadding = colPadding;
	}

	public int getRowPadding() {
		return rowPadding;
	}

	public void setRowPadding(int rowPadding) {
		this.rowPadding = rowPadding;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}
	
	public void adjustDimensions(){
		int height = 0;
		int rowPadding = getRowPadding(); 
		int rowIndex = getGrid().getRowIndex(getRow());		
		while(rowPadding > 0 && rowIndex < getGrid().getRowCount()){
			FVGridRow row = getGrid().getRowAt(rowIndex);
			height += row.getGridHeight();
			rowPadding--;
			rowIndex++;
		}
		setHeight(height, Unit.PIXELS);

		int width = 0;
		int colIndex = getColIndex();
		int colPadding = getColPadding();
		while(colPadding > 0 && colIndex < getGrid().getColCount()){
			FVGridColumn col= getGrid().getColAt(colIndex);
			width += col.getWidth();
			colPadding--;
			colIndex++;
		}
		setWidth(width, Unit.PIXELS);
	}

}