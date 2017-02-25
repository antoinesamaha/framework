package com.foc.web.modules.workflow;

import com.foc.business.workflow.WFFieldLockStage;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLView;

@SuppressWarnings("serial")
public class WFFieldLockStage_Form extends FocXMLLayout{

	@Override
	public void init(INavigationWindow window, XMLView xmlView, IFocData focData) {
		super.init(window, xmlView, focData);
		
		WFFieldLockStage fieldLockStage = getFieldLockStage();
		if(fieldLockStage != null){
			fieldLockStage.fillChoicesForStagesProperty();
		}
		
	}
	
	public WFFieldLockStage getFieldLockStage(){ 
		return (WFFieldLockStage) getFocData();
	}

}
