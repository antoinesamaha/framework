package com.foc.vaadin.gui.components.grid;

import java.util.ArrayList;

import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.web.gui.INavigationWindow;

@SuppressWarnings("serial")
public class FVGrid extends FVVerticalLayout {

	private INavigationWindow mainWindow = null;
	
	private ArrayList<FVGridColumn> colArray = null;
	private ArrayList<FVGridRow>    rowArray = null;
	
	public FVGrid(INavigationWindow mainWindow) {
		FocXMLAttributes tableLayoutAttributes = new FocXMLAttributes();
		tableLayoutAttributes.addAttribute(FXML.ATT_CAPTION_MARGIN, "0");
		tableLayoutAttributes.addAttribute(FXML.ATT_SPACING, "false");
		tableLayoutAttributes.addAttribute(FXML.ATT_MARGIN, "false");
		tableLayoutAttributes.addAttribute(FXML.ATT_WIDTH, "100%");
		setAttributes(tableLayoutAttributes);
		
		rowArray = new ArrayList<FVGridRow>();
		colArray = new ArrayList<FVGridColumn>();
	}
	
	public void dispose(){
		super.dispose();
		if(rowArray != null){
			for(int i=0; i<rowArray.size(); i++){
				FVGridRow row = rowArray.get(i);
				if(row != null) row.dispose();
			}
			rowArray.clear();
			rowArray = null;
		}
		if(colArray != null){
			for(int i=0; i<colArray.size(); i++){
				FVGridColumn col = colArray.get(i);
				if(col != null) col.dispose();
			}
			colArray.clear();
			colArray = null;
		}
		mainWindow = null;
	}

	public FVGridRow addRow(FVGridRow rowLayout){
		addComponent(rowLayout);
		rowArray.add(rowLayout);
		return rowLayout;
	}
	
	public int getRowCount(){
		return (rowArray != null) ? rowArray.size() : 0;
	}
	
	public FVGridRow getRowAt(int at){
		return rowArray != null ? rowArray.get(at) : null;
	}
	
	public int getRowIndex(FVGridRow row){
		return rowArray != null ? rowArray.indexOf(row) : -1;
	}
	
	public void addCol(FVGridColumn col){
		if(colArray != null) colArray.add(col);
	}
	
	public int getColCount(){
		return (colArray != null) ? colArray.size() : 0;
	}
	
	public FVGridColumn getColAt(int at){
		return colArray != null ? colArray.get(at) : null;
	}
	
	public INavigationWindow getMainWindow() {
		return mainWindow;
	}
	
	public void adjustDimensions(){
		for(int i=0; i<getRowCount(); i++){
			FVGridRow row = getRowAt(i);
			if(row != null){
				row.adjustDimensions();
			}
		}
	}
}