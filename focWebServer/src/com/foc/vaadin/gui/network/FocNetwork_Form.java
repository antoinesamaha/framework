package com.foc.vaadin.gui.network;

import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.layouts.FVWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public abstract class FocNetwork_Form extends FocXMLLayout {

	public abstract FocNetwork newNetwork(); 
	
	private   FocNetwork       network = null;
	protected FocObjectNetwork objectNetwork = null;
	
	@Override
	public void dispose() {
		super.dispose();
		if (network != null) {
			network.dispose();
			network = null;
		}
		if(objectNetwork != null){
			objectNetwork.dispose();
			objectNetwork = null;
		}
	}

	public FVTextField getMaxLevelComponent() {
		FVWrapperLayout wrapperLayout = (FVWrapperLayout) getComponentByName("MAX_LEVEL");
		FVTextField intField = wrapperLayout != null ? (FVTextField) wrapperLayout.getFormField() : null;
		return intField;
	}

	public int getMaxLevelFromGui() { 
		FVTextField intField = getMaxLevelComponent();
		String v = intField != null ? intField.getValueString() : "-1";
		int value = Utils.parseInteger(v, -1);
		return value;
	}
	
	public void setMaxLevelFromGui(int maxLevel) { 
		FVTextField intField = getMaxLevelComponent();
		if(intField != null) intField.setValueString(""+maxLevel);
	}
	
	public FVButton getApplyButton() {
		FVButton button = (FVButton) getComponentByName("APPLY");
		return button;
	}
	
	private void rebuildNetwork() {
		FVVerticalLayout vLay = (FVVerticalLayout) getComponentByName("MAIN");
		if (vLay != null) {
			vLay.removeAllComponents();
			if(network != null) network.dispose();
			network = newNetwork();
			network.setSizeFull();
			vLay.addComponent(network);
		}
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		rebuildNetwork();
		
		if(network != null) {
			setMaxLevelFromGui(network.getMaxLevel());
		}
			
		FVButton button = getApplyButton();
		if(button != null) {
			button.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
//					int maxLevel = getMaxLevelFromGui();
					
//					if (network != null && objectNetwork != null && maxLevel > network.getMaxLevel()) {
//						network.setMaxLevel(maxLevel);
//						objectNetwork.fill();
//					} else {
						rebuildNetwork();
//					}
				}
			});
		}
	}

}
