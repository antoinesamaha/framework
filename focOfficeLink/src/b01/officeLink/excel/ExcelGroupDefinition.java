package b01.officeLink.excel;

import java.util.ArrayList;

public class ExcelGroupDefinition {
	ArrayList<Integer> rowArray = null;
	
	public ExcelGroupDefinition(){
		rowArray = new ArrayList<Integer>();
	}
	
	public void dispose(){
	}

	public void addRow(int row){
		rowArray.add(row);
	}
	
	public int getRowCount(){
		return rowArray.size();
	}
	
	public int getRowAt(int i){
		return rowArray.get(i);
	}
}
