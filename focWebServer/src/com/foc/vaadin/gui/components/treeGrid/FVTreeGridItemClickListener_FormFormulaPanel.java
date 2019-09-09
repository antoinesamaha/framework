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
package com.foc.vaadin.gui.components.treeGrid;

import com.foc.property.FProperty;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

@SuppressWarnings("serial")
public class FVTreeGridItemClickListener_FormFormulaPanel implements ItemClickListener{

	private FVTreeGrid treeGrid = null;
	
	public FVTreeGridItemClickListener_FormFormulaPanel(FVTreeGrid treeGrid) {
		this.treeGrid = treeGrid;
	}
	
	public void dispose(){
		treeGrid = null;
	}
	
	@Override
	public void itemClick(ItemClickEvent event) {
		if(getTableTreeDelegate() != null && getTableTreeDelegate().getFormulaForm() != null){
			//We need to select a row 
			if(getTreeGrid() != null){
				getTreeGrid().select(event.getItemId());
			}
			
			FProperty property = getTableTreeDelegate().getSelectCellProperty(event);
			getTableTreeDelegate().getFormulaForm().triggerFormulaChanges(property, event.getItemId(), event.getPropertyId());
		}
	}
	
	private TableTreeDelegate getTableTreeDelegate(){
		return getTreeGrid() != null ? getTreeGrid().getTableTreeDelegate() : null;
	}
	
	private FVTreeGrid getTreeGrid(){
		return treeGrid;
	}

}
