package com.foc.web.modules.workflow.gui;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVObjectComboBox;

@SuppressWarnings("serial")
public class WF_LOG_Form extends WF_LOG_NoWorkflow_Standard_Form {

	@Override
	protected String getLogContext() {
		return XMLViewKey.CONTEXT_DEFAULT;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FVObjectComboBox comp = (FVObjectComboBox) getComponentByName("WF_CURRENT_STAGE");
		if (comp != null) {
			boolean editable = false;
			FocObject focObject = (FocObject) getFocData();
			if (focObject != null) {
				FocDesc focDesc = focObject.getThisFocDesc();
				if (focDesc != null) {
					editable = focDesc.workflow_IsAllowSignatureStageModification();
				}
			}
			comp.setEnabled(editable);
		}
	}
}
