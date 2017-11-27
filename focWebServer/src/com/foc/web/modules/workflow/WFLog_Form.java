package com.foc.web.modules.workflow;

import com.foc.business.workflow.implementation.IWorkflow;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.business.workflow.implementation.Workflow;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class WFLog_Form extends FocXMLLayout {

	IWorkflow getIWorkflow() {
		return (IWorkflow) getFocData();
	}

	Workflow getWorkflow() {
		Workflow workflow = null;
		IWorkflow iWorkflow = getIWorkflow();
		if (iWorkflow != null) {
			workflow = getIWorkflow().iWorkflow_getWorkflow();
		}
		return workflow;
	}

	FocList getLogList() {
		Workflow workflow = getWorkflow();
		FocList logList = null;
		if (workflow != null) {
			logList = workflow.getLogList();
		}
		return logList;
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
		FocList logList = getLogList();
		if (logList != null) {
			XMLViewKey key = new XMLViewKey(WFLogDesc.WF_LOG_VIEW_KEY, XMLViewKey.TYPE_TABLE, XMLViewKey.CONTEXT_DEFAULT, XMLViewKey.VIEW_DEFAULT);
			ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, logList);
			addComponent((Component) centralPanel);
			// getMainWindow().changeCentralPanelContent(centralPanel, true);
		}
	}
}
