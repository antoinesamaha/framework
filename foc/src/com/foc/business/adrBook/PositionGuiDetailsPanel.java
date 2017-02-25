package com.foc.business.adrBook;

import com.foc.desc.FocObject;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class PositionGuiDetailsPanel extends FPanel{

	public final static int VIEW_SELECTION = 2; 
	
	public PositionGuiDetailsPanel(FocObject obj, int viewID){
		if(viewID == VIEW_SELECTION){
			setInsets(0, 0, 0, 0);
			FGTextField comp = (FGTextField) obj.getGuiComponent(PositionDesc.FLD_NAME);
			comp.setEditable(false);
			add(comp, 1, 0);
		}
	}
}
