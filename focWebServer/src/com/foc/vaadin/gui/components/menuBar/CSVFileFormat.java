package com.foc.vaadin.gui.components.menuBar;

import java.util.ArrayList;

public class CSVFileFormat {
	private ArrayList<CSVColumnFormat> colArray = null;
	
	public CSVFileFormat(){
		colArray = new ArrayList<CSVColumnFormat>();
	}
	
	public void dispose(){
		if(colArray != null){
			for(int i=0; i<colArray.size(); i++){
				CSVColumnFormat colFormat = colArray.get(i);
				if(colFormat != null) colFormat.dispose();
			}
			colArray.clear();
			colArray = null;
		}
	}
	
	public CSVColumnFormat addColumn(String title, String explanation, String sample){
		CSVColumnFormat format = new CSVColumnFormat(title, explanation, sample);
		colArray.add(format);
		return format;
	}
	
  public int getColumnCount(){
  	return colArray != null ? colArray.size() : 0;
  }
  
  public CSVColumnFormat getColumnAt(int index){
  	CSVColumnFormat col = null;
  	if(colArray != null && index < getColumnCount()){
  		col = colArray.get(index);
  	}
  	return col;
  }
}
