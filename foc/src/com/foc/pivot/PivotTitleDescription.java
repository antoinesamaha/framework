package com.foc.pivot;

import com.foc.util.Utils;

public class PivotTitleDescription {
	private String title       = "";
	private String description = "";
	private String groupBy     = "";
	private String sortBy      = "";
	
	public PivotTitleDescription(String groupBy, String description){
		this.title       = groupBy;
		this.groupBy     = groupBy;
		this.sortBy      = groupBy;
		this.description = description;
		if(Utils.isStringEmpty(description)) this.description = title;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
}