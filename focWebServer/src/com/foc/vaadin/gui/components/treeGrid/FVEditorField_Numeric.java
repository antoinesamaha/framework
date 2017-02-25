package com.foc.vaadin.gui.components.treeGrid;

import com.foc.vaadin.gui.components.FVTableColumn;
import com.vaadin.ui.TextField;

public class FVEditorField_Numeric extends TextField {

	private FVTableColumn tableColumn = null;
	
	public FVEditorField_Numeric(FVTableColumn tableColumn){
		this.tableColumn = tableColumn;
	}
	
	public void dispose(){
		tableColumn = null;
	}

	public boolean isColumnEditable(){
		return tableColumn == null || tableColumn.isColumnEditable();
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		//The read only parameter takes into  account what FProperty says about isReadOnly() which takes into account 
		//1- The Property.isValueLocked()
		//2- The FField.isEditable()
		//3- The FocObject.isPropertyLocked()
		
		//We want the set enabled to take into account the readOnly + the GuiColumn if it says editable="false"
		//Even for the the read only coming from properies the setEnabled(false) is more elegant because it shows that the 
		//component is dull.
		setEnabled(isColumnEditable() && !readOnly);
		
		super.setReadOnly(readOnly);
	}
	
	/*
	private FProperty getFProperty(){
		FProperty fProp = null;
		
		Property prop = getPropertyDataSource();
		if(prop instanceof TransactionalPropertyWrapper){
			TransactionalPropertyWrapper trnsactional = (TransactionalPropertyWrapper) prop;
			fProp = (FProperty) trnsactional.getWrappedProperty();
		}
		return fProp;
	}
	*/
}
