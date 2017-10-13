package com.foc.web.modules.fab;

import com.fab.FabStatic;
import com.fab.model.table.TableDefinition;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class FabTableDefinition_Form extends FocXMLLayout {
	
  @Override
  public boolean validationCheckData(FVValidationLayout validationLayout) {
  	boolean validation = super.validationCheckData(validationLayout);
  	TableDefinition tableDef = (TableDefinition) getFocData();

  	if(tableDef != null){
	  	tableDef.adjustIFocDescDeclaration();
	  	
	    FocDesc focDescToAdapt = Globals.getApp().getFocDescByName(tableDef.getName());
	    if(focDescToAdapt != null){
		    focDescToAdapt.adaptTableAlone();
	    }
  	}
  	
  	FabStatic.refreshAllTableFieldChoices();
  	
  	return validation;
  }

}
