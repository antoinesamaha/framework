package com.foc.web.modules.workflow;

import com.foc.business.workflow.map.WFMap;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class WFMap_Form extends FocXMLLayout{
	public void dispose() {
		//This is important to reload in the cache the map after editing on screen 
		WFMap map = (WFMap) getFocObject();
		if(map != null){
			map.resetPreviousStage2SignatureMap();
		}
		super.dispose();
	}
}
