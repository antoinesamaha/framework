package com.foc.business.country;

import com.foc.business.country.region.RegionGuiBrowsePanel;
import com.foc.desc.FocObject;
import com.foc.gui.FGCurrentItemPanel;
import com.foc.gui.FGTextField;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class CountryGuiDetailsPanel extends FPanel implements CountryConst {

	public static final int VIEW_SELECTION = 1;
	
	public CountryGuiDetailsPanel(FocObject obj, int viewID){
		if(viewID == VIEW_SELECTION){
			setInsets(0, 0, 0, 0);
			FGTextField comp = (FGTextField) obj.getGuiComponent(CountryDesc.FLD_COUNTRY_NAME);
			comp.setEditable(false);
			add(comp, 0, 0);
		}else{
			Country country = (Country) obj;
			add(country, FLD_COUNTRY_NAME, 0, 0);
			if(!country.isCreated()){
				FPanel details = new FPanel();
				add(details, 0, 1, 2, 1);
				FocList              list   = country.getRegionList();
				RegionGuiBrowsePanel browse = new RegionGuiBrowsePanel(list, FocObject.DEFAULT_VIEW_ID);
				details.add(browse, 0, 0);
				
				FGCurrentItemPanel currPanel = new FGCurrentItemPanel(browse, FocObject.DEFAULT_VIEW_ID);
				details.add(currPanel, 1, 0);
			}
		}
		FValidationPanel vPanel = showValidationPanel(true);
		vPanel.addSubject(obj);
	}
}
