package com.foc.desc.parsers.filter;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.foc.list.filter.FocListFilter;

public class ParsedFilter {
	
	private String tableName   = null;
	private int    filterLevel = FocListFilter.LEVEL_DATABASE;

	private ArrayList<ParsedFilterCondition> conditionArray = null;
	
	public ParsedFilter(String tableName){
		this.tableName = tableName;
	}
	
	public void addCondition(ParsedFilterCondition condition){
		if(conditionArray == null){
			conditionArray = new ArrayList<ParsedFilterCondition>();
		}
		
		conditionArray.add(condition);
	}
	
	public void addCondition(Attributes att){
		addCondition(new ParsedFilterCondition(att));
	}

	public String getTableName() {
		return tableName;
	}
	
	public int getConitionCount(){
		return conditionArray != null ? conditionArray.size() : 0;
	}
	
	public int getFilterLevel() {
		return filterLevel;
	}

	public void setFilterLevel(int filterLevel) {
		this.filterLevel = filterLevel;
	}
	
	public ParsedFilterCondition getConitionAt(int index){
		return (ParsedFilterCondition) (conditionArray != null ? conditionArray.get(index) : null);
	}
}
