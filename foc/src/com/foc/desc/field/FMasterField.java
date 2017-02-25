package com.foc.desc.field;

import java.awt.Component;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.gui.table.cellControler.AbstractCellControler;
import com.foc.list.filter.FilterCondition;
import com.foc.property.FMaster;
import com.foc.property.FProperty;

public class FMasterField extends FField{
	FocDesc masterFocDesc = null;
	
	public FMasterField(FocDesc masterFocDesc){
		super("MASTER_MIRROR", "Master Mirror", MASTER_MIRROR_ID, false, 0, 0);
		setDBResident(false);
		this.masterFocDesc = masterFocDesc;
	}

	@Override
	public void addReferenceLocations(FocDesc pointerDesc) {
	}

	@Override
	public String getCreationString(String name) {
		return "";
	}

	@Override
	public FocDesc getFocDesc() {
		return masterFocDesc;
	}

	@Override
	public Component getGuiComponent(FProperty prop) {		
		return null;
	}

	@Override
	public int getSqlType() {
		return 0;
	}

	@Override
	public AbstractCellControler getTableCellEditor_ToImplement(FProperty prop) {
		return null;
	}

	@Override
	public boolean isObjectContainer() {
		return true;
	}

	@Override
	public FProperty newProperty_ToImplement(FocObject masterObj, Object defaultValue) {
		return new FMaster(masterObj);
	}

	@Override
	public FProperty newProperty_ToImplement(FocObject masterObj) {
		return new FMaster(masterObj);
	}
	
	protected FilterCondition getFilterCondition(FFieldPath fieldPath, String conditionPrefix){
		return null;
	}
}
