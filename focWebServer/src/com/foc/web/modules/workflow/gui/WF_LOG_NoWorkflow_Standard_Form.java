package com.foc.web.modules.workflow.gui;

import com.foc.business.workflow.implementation.ILoggable;
import com.foc.business.workflow.implementation.Loggable;
import com.foc.business.workflow.implementation.WFLogDesc;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class WF_LOG_NoWorkflow_Standard_Form extends FocXMLLayout {

	private ILoggable getILoggable() {
		return (ILoggable) getFocData();
	}

	private Loggable getLoggable() {
		Loggable loggable = null;
		ILoggable iloggable = getILoggable();
		if (iloggable != null) {
			loggable = getILoggable().iWorkflow_getWorkflow();
		}
		return loggable;
	}
	
	FocList getLogList() {
		Loggable loggable = getLoggable();
		FocList logList = null;
		if (loggable != null) {
			logList = loggable.getLogList();
		}
		return logList;
	}

	protected String getLogContext() {
		return WorkflowWebModule.CTXT_WF_NO_WORKFLOW__LOG_ONLY;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		FocList logList = getLogList();
		if (logList != null) {
			XMLViewKey key = new XMLViewKey(WFLogDesc.WF_LOG_VIEW_KEY, XMLViewKey.TYPE_TABLE, getLogContext(), XMLViewKey.VIEW_DEFAULT);
			ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, logList);
			addComponent((Component) centralPanel);
			// getMainWindow().changeCentralPanelContent(centralPanel, true);
		}
	}
}
