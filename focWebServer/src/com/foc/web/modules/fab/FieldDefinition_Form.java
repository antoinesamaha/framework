package com.foc.web.modules.fab;

import com.fab.model.table.FieldDefinition;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class FieldDefinition_Form extends FocXMLLayout {
	
	public FieldDefinition_Form(){
	}
	
	public FieldDefinition getFieldDefinition(){
		return (FieldDefinition) getFocData();
	}
	
  public void afterApplyClick(){
  	/*
    FieldDefinition fieldDef = (FieldDefinition) getFocData();

    FocDesc focDescToAdapt = Globals.getApp().getFocDescByName(fieldDef.getTableDefinition().getName());
    if(focDescToAdapt != null){
	    fieldDef.addToFocDesc(focDescToAdapt);
	    focDescToAdapt.adaptTableAlone();
	    //getRightPanel().refresh();
	    //focDescToAdapt.allFocObjectArray_AdjustPropertyArray();
    }
    */
  }

	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();		
		FieldDefinition fieldDefinition = getFieldDefinition();
		if(fieldDefinition.isCreated()){
			fieldDefinition.setID_ToMax();
		}
	}
  
}
