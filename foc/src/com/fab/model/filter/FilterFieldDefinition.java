package com.fab.model.filter;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FFieldPath;
import com.foc.list.filter.FilterCondition;
import com.foc.list.filter.FilterDesc;
import com.foc.property.PropertyFocObjectLocator;

public class FilterFieldDefinition extends FocObject {

	public FilterFieldDefinition(FocConstructor constr) {
		super(constr);
		newFocProperties();
	}
	
	public void setFilterDefinition(FilterDefinition filterDefintion){
		setPropertyObject(FilterFieldDefinitionDesc.FLD_FILTER_DEFINITION, filterDefintion);
	}
	
	public FilterDefinition getFilterDefinition(){
		return (FilterDefinition)getPropertyObject(FilterFieldDefinitionDesc.FLD_FILTER_DEFINITION);
	}

	private FFieldPath getFieldPath(){
    PropertyFocObjectLocator propertyFocObjectLocator = new PropertyFocObjectLocator();
    propertyFocObjectLocator.parsePath(getPropertyString(FilterFieldDefinitionDesc.FLD_CONDITION_PROPERTY_PATH), getFilterDefinition().getBaseFocDesc(), null, null);
    return propertyFocObjectLocator.getLocatedPath();
	}
	
	private FField getConditionLastField(){
    PropertyFocObjectLocator propertyFocObjectLocator = new PropertyFocObjectLocator();
    propertyFocObjectLocator.parsePath(getPropertyString(FilterFieldDefinitionDesc.FLD_CONDITION_PROPERTY_PATH), getFilterDefinition().getBaseFocDesc(), null, null);
    return propertyFocObjectLocator.getLocatedField();
	}
	
	public void addConditionToFilterDesc(FilterDesc filterDesc){
		if(filterDesc != null){
			FField conditionField = getConditionLastField();
			if(conditionField != null){
				FocDesc baseFocDesc = getFilterDefinition().getBaseFocDesc();
				FilterCondition filterCondition = conditionField.getFilterCondition(getFieldPath(), baseFocDesc);
				if(filterCondition != null){
					filterDesc.addCondition(filterCondition);
				}
			}else{
				conditionField = getConditionLastField();
				Globals.getDisplayManager().popupMessage("Filter condition not added for " + getFilterDefinition().getBaseFocDescName() + " filter");
			}
		}
	}
}
