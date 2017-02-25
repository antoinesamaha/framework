package com.foc.business.workflow.map;

import com.foc.desc.FocObject;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WFMapGuiDetailsPanel extends FPanel{

	public static final int VIEW_STANDARD  = FocObject.DEFAULT_VIEW_ID;
	public static final int VIEW_SELECTION = 2;
	
	public WFMapGuiDetailsPanel(FocObject focObj, int view){
		super("Workflow Map", FILL_BOTH);
		WFMap area = (WFMap) focObj;
		
		if(view == VIEW_SELECTION){
			FGTextField tf = (FGTextField) add(area, WFMapDesc.FLD_NAME, 0, 0);
			tf.setEditable(false);
			
			tf = (FGTextField) add(area, WFMapDesc.FLD_DESCRIPTION, 0, 1);
			tf.setEditable(false);
		}else{
			setMainPanelSising(FILL_BOTH);
			
			add(area, WFMapDesc.FLD_NAME, 0, 0);
			add(area, WFMapDesc.FLD_DESCRIPTION, 0, 1);
			/*
			add(area, WFMapDesc.FLD_CREATION_TITLE, 0, 2);
			add(area, WFMapDesc.FLD_MODIFICATION_TITLE, 0, 3);
			add(area, WFMapDesc.FLD_CANCELATION_TITLE, 0, 4);
			*/
	
			FocList list = area.getSignatureList();
			WFSignatureGuiBrowsePanel browse = new WFSignatureGuiBrowsePanel(list, FocObject.DEFAULT_VIEW_ID);
			add(browse, 0, 5, 2, 1);
			
			FValidationPanel validPanel = showValidationPanel(true);
			validPanel.addSubject(area);
		}
	}
}
