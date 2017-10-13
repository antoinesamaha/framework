package com.foc.web.modules.business;

import com.foc.business.units.UnitDesc;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

@SuppressWarnings("serial")
public class Dimension_Table extends FocXMLLayout {
	
	private FocObject unitObject     = null;
	public FVObjectComboBox comboBox = null;
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		boolean error = super.validationCheckData(validationLayout);
		UnitDesc.getList(FocList.FORCE_RELOAD);
    return error;
	}
	
	public FVObjectComboBox getComboBox() {
		return comboBox;
	}

	public void setComboBox(FVObjectComboBox comboBox) {
		this.comboBox = comboBox;
	}
	
	public FocObject getUnitObject() {
		return unitObject;
	}

	public void setUnitObject(FocObject focObject) {
		this.unitObject = focObject;
	}
	
	@Override
	public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
		super.validationAfter(validationLayout, commited);
		if(getUnitObject() != null && getComboBox() != null){
			getUnitObject().validate(true);
			getComboBox().reloadList();
			getComboBox().select(getUnitObject().getReferenceInt());
		}
	}
}
