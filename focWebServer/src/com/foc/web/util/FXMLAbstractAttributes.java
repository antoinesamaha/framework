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
package com.foc.web.util;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import com.foc.dataDictionary.FocDataDictionary;

public abstract class FXMLAbstractAttributes extends AttributesImpl {

  protected abstract String resolveValue(String value);
  
	public FXMLAbstractAttributes(Attributes attributes){
		super(attributes);
		if(attributes instanceof FXMLAbstractAttributes){
		  for(int i=0; i<attributes.getLength(); i++){
		    addAttribute(attributes.getQName(i), ((FXMLAbstractAttributes)attributes).getValueWithoutResolve(i));		    
		  }
		}
	}
	
	public FXMLAbstractAttributes(){
	}
	
	public void dispose(){
	}
	
  private ArrayList<String> getFieldsUsed(String value){
    ArrayList<String> array = null;
    array = FocDataDictionary.getInstance().getFieldsUsed(value);
    return array;
  }
  
	public void addAttribute(String tag, String value){
		int idx = getIndex(tag);
		if(idx >= 0){
			setAttribute(idx, "", tag, tag, "CDATA", value);
		}else{
			addAttribute("", tag, tag, "CDATA", value);
		}
	}
	
	@Override
	public String getValue(int arg0) {
		String value = super.getValue(arg0);
		value = resolveValue(value);
		return value;
	}
	
	public String getValueWithoutResolve(int arg0) {
    String value = super.getValue(arg0);
    return value;
  }

	@Override
	public String getValue(String arg0) {
		String value = super.getValue(arg0);
		value = resolveValue(value);
		return value;
	}

	@Override
	public String getValue(String arg0, String arg1) {
		String value = super.getValue(arg0, arg1);
		value = resolveValue(value);
		return value;
	}
	
	public ArrayList<String> getArrayOfFieldsUsed(int idx){
	  String value = getValueWithoutResolve(idx);
	  return getFieldsUsed(value);
	}
}
