package com.foc.vaadin.gui.xmlForm;

import com.foc.Globals;
import com.foc.vaadin.gui.components.FVTable;
import com.foc.vaadin.gui.components.tableAndTree.FVTableDropHandler;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Table.TableDragMode;

public abstract class FocXMLLayout_OrderedTable extends FocXMLLayout {
	
	protected abstract String getTableName();

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		addDropListener();
	}
	
  public FVTableWrapperLayout getTableWrapperLayout(){
  	FVTableWrapperLayout tableWrapperLayout = null;
  	tableWrapperLayout = (FVTableWrapperLayout) getComponentByName(getTableName());
    return tableWrapperLayout;
  }
  
  public FVTable getTable(){
  	FVTable table = null;
  	FVTableWrapperLayout tableWrapperLayout  = getTableWrapperLayout();
  	if(tableWrapperLayout != null){
  		table = (FVTable) tableWrapperLayout.getTableOrTree();
  	}
    return table;
  }
  
  private void addDropListener(){
    if(getTable() != null){
      getTable().setDragMode(TableDragMode.ROW);

      getTable().setDropHandler(new FVTableDropHandler(getTable()) {
    	
        public AcceptCriterion getAcceptCriterion() {
          return com.vaadin.event.dd.acceptcriteria.AcceptAll.get();
        }
        
        public void drop(DragAndDropEvent dropEvent) {
        	try{
        		copyGuiToMemory();
        		super.drop(dropEvent);
            copyMemoryToGui();
          }catch(Exception e){
          	Globals.logException(e);
          }
        }
      });
    }
  }

}