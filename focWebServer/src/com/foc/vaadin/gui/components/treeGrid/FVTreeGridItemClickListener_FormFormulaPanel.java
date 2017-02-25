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
