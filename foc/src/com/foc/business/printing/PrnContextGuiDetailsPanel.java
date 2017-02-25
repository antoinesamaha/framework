package com.foc.business.printing;

import com.foc.desc.FocObject;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class PrnContextGuiDetailsPanel extends FPanel{

	public static final int VIEW_STANDARD  = FocObject.DEFAULT_VIEW_ID;
	public static final int VIEW_SELECTION = 2;
	
	public PrnContextGuiDetailsPanel(FocObject focObj, int view){
		super("Workflow Area", FILL_BOTH);
		PrnContext area = (PrnContext) focObj;
		
		if(view == VIEW_SELECTION){
			FGTextField tf = (FGTextField) add(area, PrnContextDesc.FLD_NAME, 0, 0);
			tf.setEditable(false);
			
			tf = (FGTextField) add(area, PrnContextDesc.FLD_DESCRIPTION, 0, 1);
			tf.setEditable(false);
		}else{
			setMainPanelSising(FILL_BOTH);
			
			add(area, PrnContextDesc.FLD_NAME, 0, 0);
			add(area, PrnContextDesc.FLD_DESCRIPTION, 0, 1);
	
			FocList list = area.getLayoutList();
			PrnLayoutGuiBrowsePanel browse = new PrnLayoutGuiBrowsePanel(list, FocObject.DEFAULT_VIEW_ID);
			add(browse, 0, 2, 2, 1);
			
			FValidationPanel validPanel = showValidationPanel(true);
			validPanel.addSubject(area);
		}
	}
}
