package com.foc.desc.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;

public class XMLFilter {
	
	private String tableName = null;
	
	private ArrayList<XMLFilterCondition> conditionArray = null;
	
	public XMLFilter(String tableName){
		this.tableName = tableName;
	}
	
	public void addCondition(Attributes att){
		
		if(conditionArray == null){
			conditionArray = new ArrayList<XMLFilterCondition>();
		}
		
		conditionArray.add(new XMLFilterCondition(att));
	}

	public String getTableName() {
		return tableName;
	}
	
	public int getConitionCount(){
		return conditionArray != null ? conditionArray.size() : 0;
	}
	
	public XMLFilterCondition getConitionAt(int index){
		return (XMLFilterCondition) (conditionArray != null ? conditionArray.get(index) : null);
	}
}
