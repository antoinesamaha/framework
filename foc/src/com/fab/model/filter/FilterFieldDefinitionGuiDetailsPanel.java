package com.fab.model.filter;

import com.foc.desc.FocObject;
import com.foc.gui.FGFormulaEditorPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;

@SuppressWarnings("serial")
public class FilterFieldDefinitionGuiDetailsPanel extends FPanel {
	
	public FilterFieldDefinitionGuiDetailsPanel(FocObject focObject, int viewID){
		super("Filter field", FPanel.FILL_NONE);
		if(focObject != null){
			FilterFieldDefinition fieldFieldDef = (FilterFieldDefinition) focObject;
			FGFormulaEditorPanel comp = (FGFormulaEditorPanel) focObject.getGuiComponent(FilterFieldDefinitionDesc.FLD_CONDITION_PROPERTY_PATH);
			comp.setOriginDesc(fieldFieldDef.getFilterDefinition().getBaseFocDesc());
			add(comp, 0, 0);
			
			FValidationPanel validPanel = showValidationPanel(true);
			validPanel.setValidationType(FValidationPanel.VALIDATION_OK);
		}
	}
}
