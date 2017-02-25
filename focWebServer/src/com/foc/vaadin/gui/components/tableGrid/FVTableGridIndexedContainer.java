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
