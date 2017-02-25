package com.foc.list.filter;

import java.awt.Color;

import com.foc.desc.field.FField;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;

public class ColorPropertyListener implements FPropertyListener /*AbstractAction*/{
	private FilterCondition condition           = null;
	private int             operationPropertyId = FField.NO_FIELD_ID;
	
	public ColorPropertyListener(FilterCondition condition, int operationPropertyId){
		this.condition           = condition          ;
		this.operationPropertyId = operationPropertyId;
	}
	
	public void dispose() {
	}

	public void propertyModified(FProperty property) {
		FocListFilter filter = (FocListFilter) property.getFocObject();
				
		StringBuffer buff = condition.buildSQLWhere(filter, "RIEN");
		if(buff != null && buff.length() > 0){
			FProperty prop = filter.getFocProperty(operationPropertyId);
			prop.setBackground(Color.RED);
		}else{
			FProperty prop = filter.getFocProperty(operationPropertyId);
			prop.setBackground(null);
		}
	}
}
