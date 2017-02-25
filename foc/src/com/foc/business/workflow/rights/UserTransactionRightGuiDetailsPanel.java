package com.foc.business.workflow.rights;

import com.foc.business.workflow.map.WFTransactionConfig;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.desc.FocObject;
import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class UserTransactionRightGuiDetailsPanel extends FPanel{
	
	public UserTransactionRightGuiDetailsPanel(FocObject obj, int viewID){
		WFTransactionConfig assignement = (WFTransactionConfig) obj;
		add(assignement, WFTransactionConfigDesc.FLD_TRANSACTION, 0, 0);
		add(assignement, WFTransactionConfigDesc.FLD_MAP, 0, 1);
		add(assignement, WFTransactionConfigDesc.FLD_DEFAULT_AREA, 0, 2);
		
		//WFAssignFunctionalStageCorrespondanceGuiBrowsePanel browse = new WFAssignFunctionalStageCorrespondanceGuiBrowsePanel(assignement.getFucntionalStageList(), FocObject.DEFAULT_VIEW_ID);
		//add(browse, 0, 3, 2, 1);
	}
}
