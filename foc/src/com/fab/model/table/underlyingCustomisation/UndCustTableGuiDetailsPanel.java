package com.fab.model.table.underlyingCustomisation;

import com.fab.model.table.FieldDefinitionGuiBrowsePanel;
import com.fab.model.table.TableDefinitionGuiDetailsPanel;
import com.foc.desc.FocObject;
import com.foc.gui.FGTabbedPane;
import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class UndCustTableGuiDetailsPanel extends TableDefinitionGuiDetailsPanel {
	
	public UndCustTableGuiDetailsPanel(FocObject tableDefinition, int viewID){
		super(tableDefinition, viewID, false);
	}
	
	@Override
	public FieldDefinitionGuiBrowsePanel newFieldBrowsePanel(int viewID, boolean withBrowseDetailsDictionary){
		return new UndCustFieldGuiBrowsePanel(getTableDefinition().getFieldDefinitionList(), viewID, withBrowseDetailsDictionary);
	}
	
	@Override
	public void postValidationAction(){
		
	}
}
