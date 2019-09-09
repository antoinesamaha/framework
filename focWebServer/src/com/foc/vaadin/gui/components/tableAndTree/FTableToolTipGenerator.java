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
package com.foc.vaadin.gui.components.tableAndTree;

import java.util.HashMap;

import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Component;

public class FTableToolTipGenerator implements ItemDescriptionGenerator {

	public HashMap<String, String> tooltipMap = null;
	
	public FTableToolTipGenerator(){
		tooltipMap = new HashMap<String, String>();
	}
	
	public void dispose(){
		if(tooltipMap != null){
			tooltipMap.clear();
			tooltipMap = null;
		}
	}
	
	private String buildKey(Object itemId, Object propertyId){
		return itemId+"|"+propertyId;
	}
	
	public void addTooltip(Object itemId, Object propertyId, String value){
		if(tooltipMap != null){
			String key = buildKey(itemId, propertyId);
			if(tooltipMap.get(key) == null){
				tooltipMap.put(key, value);
			}
		}
	}
	
	@Override
	public String generateDescription(Component source, Object itemId, Object propertyId) {
		String ttt = null;
		String key = buildKey(itemId, propertyId);
		if(key != null && tooltipMap != null){
			ttt = tooltipMap.get(key);
		}
		return ttt;
	}

}
