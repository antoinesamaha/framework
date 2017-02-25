package com.foc.business.country.city;

import com.foc.desc.FocObject;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;

@SuppressWarnings("serial")
public class CityGuiDetailsPanel extends FPanel{

	public CityGuiDetailsPanel(FocObject obj, int viewID){
		setInsets(0, 0, 0, 0);
		FGTextField comp = (FGTextField) obj.getGuiComponent(CityDesc.FLD_CITY_NAME);
		comp.setEditable(false);
		add(comp, 0, 0);
	}
}
