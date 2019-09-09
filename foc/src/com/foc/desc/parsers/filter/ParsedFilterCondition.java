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
package com.foc.desc.parsers.filter;

import org.xml.sax.Attributes;

import com.foc.annotations.model.FocFilterCondition;
import com.foc.desc.parsers.xml.FXMLDesc;
import com.foc.desc.parsers.xml.XMLFocDescParser;
import com.foc.list.filter.FocListFilter;

public class ParsedFilterCondition {
	private String  fieldPath       = null;
	private String  prefix          = null;
	private String  caption         = null;
	private String  captionProperty = null;
	private int     level           = FocListFilter.LEVEL_DATABASE;

	public ParsedFilterCondition(FocFilterCondition filterCondAnnotation){
		this.fieldPath = filterCondAnnotation.fieldPath();
		this.prefix = filterCondAnnotation.prefix();
		this.caption = filterCondAnnotation.caption();
		this.captionProperty = filterCondAnnotation.captionProperty();
		this.level = filterCondAnnotation.level();
	}
	
	public ParsedFilterCondition(Attributes att){
		this.fieldPath = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_ON_FIELD);
		this.prefix = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_CONDITION_PREFIX);
		this.caption = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_CONDITION_CAPTION);
		this.captionProperty = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_CONDITION_CAPTION_PROPERTY);
		this.level = FocListFilter.LEVEL_DATABASE;
		String levelStr = XMLFocDescParser.getString(att, FXMLDesc.ATT_FILTER_LEVEL);
		if(levelStr != null && levelStr.equals(FXMLDesc.VAL_FILTER_LEVEL_MEMORY)) {
			this.level = FocListFilter.LEVEL_MEMORY;
		}else if(levelStr != null && levelStr.equals(FXMLDesc.VAL_FILTER_LEVEL_DATABASE)) {
			this.level = FocListFilter.LEVEL_DATABASE;
		}else if(levelStr != null && levelStr.equals(FXMLDesc.VAL_FILTER_LEVEL_DATABASE_AND_MEMORY)) {
			this.level = FocListFilter.LEVEL_DATABASE_AND_MEMORY;
		}			
	}

	public String getFieldPath() {
		return fieldPath;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getCaption() {
		return caption;
	}

	public String getCaptionProperty() {
		return captionProperty;
	}

	public void setCaptionProperty(String captionProperty) {
		this.captionProperty = captionProperty;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
