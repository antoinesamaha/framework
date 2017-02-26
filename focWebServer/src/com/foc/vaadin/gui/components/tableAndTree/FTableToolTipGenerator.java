package com.foc.vaadin.gui.components.tableAndTree;

import java.util.HashMap;

import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Component;

public class FTableToolTipGenerator implements ItemDescriptionGenerator {

	public HashMap<String, String> tooltipMap = null;
	
	public FTableToolTipGenerator(){
		tooltipMap = new HashMap<String, String>();
	}
	
	public void dispose(){
		if(tooltipMap != null){
			tooltipMap.clear();
			tooltipMap = null;
		}
	}
	
	private String buildKey(Object itemId, Object propertyId){
		return itemId+"|"+propertyId;
	}
	
	public void addTooltip(Object itemId, Object propertyId, String value){
		if(tooltipMap != null){
			String key = buildKey(itemId, propertyId);
			if(tooltipMap.get(key) == null){
				tooltipMap.put(key, value);
			}
		}
	}
	
	@Override
	public String generateDescription(Component source, Object itemId, Object propertyId) {
		String ttt = null;
		String key = buildKey(itemId, propertyId);
		if(key != null && tooltipMap != null){
			ttt = tooltipMap.get(key);
		}
		return ttt;
	}

}
