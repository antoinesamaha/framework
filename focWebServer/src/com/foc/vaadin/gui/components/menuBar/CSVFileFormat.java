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
