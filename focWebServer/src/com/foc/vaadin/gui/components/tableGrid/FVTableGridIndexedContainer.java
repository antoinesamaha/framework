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
package com.foc.vaadin.gui.components.tableGrid;

import java.util.Collection;

import com.foc.dataWrapper.FocListWrapper;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

@SuppressWarnings("serial")
public class FVTableGridIndexedContainer extends IndexedContainer{

	private FocListWrapper focListWrapper = null;
	private FVTableGrid    tableGrid      = null;
	
	public FVTableGridIndexedContainer(FVTableGrid tableGrid, FocListWrapper focListWrapper) {
		super(focListWrapper.getItemIds());
		this.focListWrapper = focListWrapper;
		this.tableGrid      = tableGrid;
	}
	
	public void dispose(){
		focListWrapper = null;
	}
	
	@Override
	public Item getItem(Object itemId) {
		return getFocListWrapper() != null ? getFocListWrapper().getItem(itemId) : null;
	}
	
	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return getContainerPropertyIds();
	}

	@Override
	public Collection<?> getContainerPropertyIds() {
		return getTableTreeDelegate() != null ? getTableTreeDelegate().newVisibleColumnIds() : null;
	}
	
	private TableTreeDelegate getTableTreeDelegate(){
		return getTableGrid() != null ? getTableGrid().getTableTreeDelegate() : null;
	}
	
	private FVTableGrid getTableGrid(){
		return tableGrid;
	}
	
	private FocListWrapper getFocListWrapper(){
		return focListWrapper;
	}
}
