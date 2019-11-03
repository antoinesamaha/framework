/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.web.dataModel;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.property.FObject;
import com.foc.vaadin.gui.components.FVObjectComboBox;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.data.Item;

@SuppressWarnings("serial")
public class FocListWrapper_ForObjectSelection extends FocListWrapper {

	//public static final int REF_ADD_NEW_ITEM = -99999; 
	
	private FVObjectComboBox comboBox = null;
	//private FocObject focObjectFor_Add = null;
	private FocDataItem_ForComboBoxActions item4AddAction     = null;
	private FocDataItem_ForComboBoxActions item4RefreshAction = null;
	
	public FocListWrapper_ForObjectSelection(FVObjectComboBox comboBox, FObjectField objectField){
		super(objectField != null ? objectField.getSelectionList() : null, false);
		
		this.comboBox = comboBox;
		
    if(objectField != null){
      String  filterExpression = objectField.getSelectionFilterExpression();
      if(filterExpression != null && !filterExpression.isEmpty()){          
        addFilterByExpression(null, filterExpression);
      }
      
      String filter_PropertyPath = objectField.getSelectionFilter_PropertyDataPath();
      if(filter_PropertyPath != null && !filter_PropertyPath.isEmpty()){
      	Object filter_PropertyValue = objectField.getSelectionFilter_PropertyValue();
        addFilterByPropertyValue(filter_PropertyPath, filter_PropertyValue);
      }
    }
	}
	
	public FocListWrapper_ForObjectSelection(FVObjectComboBox comboBox, FObject objectProperty){
		super(objectProperty != null ? objectProperty.getPropertySourceList() : null, false);
		
		this.comboBox = comboBox;
		
    if(objectProperty != null){
      String  filterExpression = objectProperty.getSelectionFilterExpression();
      if(filterExpression != null && !filterExpression.isEmpty()){          
        addFilterByExpression(objectProperty.getFocObject(), filterExpression);
      }
      
      String filter_PropertyPath = objectProperty.getSelectionFilter_PropertyDataPath();
      if(filter_PropertyPath != null && !filter_PropertyPath.isEmpty()){
      	Object filter_PropertyValue = objectProperty.getSelectionFilter_PropertyValue();
        addFilterByPropertyValue(filter_PropertyPath, filter_PropertyValue);
      }
      
      FocObject initialObjectSelected = objectProperty.getObject_CreateIfNeeded();
      if(initialObjectSelected != null){
        setInitialValue(initialObjectSelected);
      }
      
			if (getFocDesc() != null) {
				FBoolField depricatedFld = (FBoolField) getFocDesc().getFieldByID(FField.FLD_DEPRECATED_FIELD);
				if (depricatedFld != null) {
					addFilterByPropertyValue(depricatedFld.getName(), false);
				}
			}
    }
	}
	
	public void dispose(){
		super.dispose();
		if(item4AddAction != null){
			item4AddAction.dispose();
			item4AddAction = null;
		}
		if(item4RefreshAction != null){
			item4RefreshAction.dispose();
			item4RefreshAction = null;
		}
		comboBox = null;
	}
	
	public FocDesc getFocDesc(){
		return getFocList() != null ? getFocList().getFocDesc() : null;
	}
	
	protected boolean isWithNewItemOptionInList(){
		return Globals.isValo() && Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null && !Globals.getApp().getUser_ForThisSession().isGuest() && Globals.getApp().getUser_ForThisSession().hasRightsToAddItemsFor(getFocDesc());
	}
	
	private FocDataItem_ForComboBoxActions getItemForAddAction(){
		if(item4AddAction == null && isWithNewItemOptionInList() && comboBox != null && isAddNewItemEnabled()){
			String columnName = (String) comboBox.getItemCaptionPropertyId();
			item4AddAction = new FocDataItem_ForComboBoxActions(columnName, FocDataItem_ForComboBoxActions.ACTION_TYPE_ADD);
		}
		return item4AddAction;
	}
	
	private FocDataItem_ForComboBoxActions getItemForRefreshAction(){
		if(item4RefreshAction == null && isWithNewItemOptionInList() && comboBox != null && isRefreshEnabled()){
			String columnName = (String) comboBox.getItemCaptionPropertyId();
			item4RefreshAction = new FocDataItem_ForComboBoxActions(columnName, FocDataItem_ForComboBoxActions.ACTION_TYPE_REFRESH);
		}
		return item4RefreshAction;
	}
	
	private boolean isRefreshEnabled(){
		boolean isRefreshEnabled = !isGearEnabled();//When there is a gear we do not put items in the combo itself to do 'Refresh' 'Add'...
		if(isRefreshEnabled && comboBox != null && comboBox.getAttributes() != null){
			String value = comboBox.getAttributes().getValue(FXML.ATT_REFRESH_ENABLED);
			isRefreshEnabled = false;
			if(value != null && !value.isEmpty() && (value.equalsIgnoreCase("true") || value.equals("1"))){
				isRefreshEnabled = true;
			}
		}
		return isRefreshEnabled;
	}
	
	private boolean isAddNewItemEnabled(){
		boolean isAddNewItemEnabled = !isGearEnabled();
		if(isAddNewItemEnabled && comboBox != null && comboBox.getAttributes() != null){
			String value = comboBox.getAttributes().getValue(FXML.ATT_ADD_ENABLED);
			isAddNewItemEnabled = false;
			if(value != null && !value.isEmpty() && (value.equalsIgnoreCase("true") || value.equals("1"))){
				isAddNewItemEnabled = true;
			}
		}
		return isAddNewItemEnabled;
	}

	private boolean isGearEnabled(){
		boolean isGearEnabled =    comboBox != null && comboBox.getAttributes() != null 
				                    && comboBox.getAttributes().getValue(FXML.ATT_GEAR_ENABLED) != null 
										        && comboBox.getAttributes().getValue(FXML.ATT_GEAR_ENABLED).equalsIgnoreCase("true");
		return isGearEnabled;
	}
	
  protected ArrayList<FocObject> getVisibleListElements(boolean create){
  	ArrayList<FocObject> array = super.getVisibleListElements(false);
  	if(array == null && create){
  		array = super.getVisibleListElements(create);
  		item4AddAction = getItemForAddAction();
  		if(item4AddAction != null){
  			array.add(0, item4AddAction);
  		}
  		item4RefreshAction = getItemForRefreshAction();
  		if(item4RefreshAction != null){
  			array.add(1, item4RefreshAction);
  		}
  		
//	      FocList list = getFocList();
//	      if(item4AddAction == null && list != null && isWithNewItemOptionInList()){
//	      	FocDesc focDesc = list.getFocDesc();
//	      	if(focDesc != null){
//	      		FocConstructor constr = new FocConstructor(focDesc, null);
//	      		item4AddAction = constr.newItem();
//
//		      	String columnName = (String) comboBox.getItemCaptionPropertyId();
//		      	FProperty prop = focObjectFor_Add.getFocPropertyForPath(columnName);
//		      	if(prop != null && prop instanceof FString){
//			      	prop.setString("Add new");
//		      	}
//	      	}
//	      	//focObjectFor_Add = list.newEmptyDisconnectedItem();
//	      	//focObjectFor_Add.setReference(REF_ADD_NEW_ITEM);
//	      }
//  		}
  	}
  	return array;
  }
  
  public long getFocObjectRef_ForTheADDIcon(){
  	FocDataItem_ForComboBoxActions item4AddAction = getItemForAddAction();
  	return (item4AddAction != null) ? item4AddAction.getReference().getLong() : 0;
  }
  
  public long getFocObjectRef_ForTheREFRESHIcon(){
  	FocDataItem_ForComboBoxActions item4RefreshAction = getItemForRefreshAction();
  	return (item4RefreshAction != null) ? item4RefreshAction.getReference().getLong() : 0;
  }
  
  @Override
  public Item getItem(Object itemId) {
  	Item item = null;
  	long itemIdlong = itemId != null ? (Long)itemId : 0;
  	if(itemIdlong != 0 && itemIdlong == getFocObjectRef_ForTheADDIcon() && isWithNewItemOptionInList()){
  		item = item4AddAction;
  	}else if(itemIdlong != 0 && itemIdlong == getFocObjectRef_ForTheREFRESHIcon() && isWithNewItemOptionInList()){
  		item = item4RefreshAction;
  	}else{
  		item = super.getItem(itemId);
  	}
    return item;
  }
  
  @Override
  protected FocObject getContainerProperty_GetFocObject(Object itemId){
  	FocObject focObj = null;
  	long itemIdlong = itemId != null ? (Long)itemId : 0; 
  	if(itemIdlong != 0 && itemIdlong == getFocObjectRef_ForTheADDIcon() && isWithNewItemOptionInList()){
  		focObj = item4AddAction;
  	}else if(itemIdlong != 0 && itemIdlong == getFocObjectRef_ForTheREFRESHIcon() && isWithNewItemOptionInList()){
  		focObj = item4RefreshAction;
  	}else{
  		focObj = super.getContainerProperty_GetFocObject(itemId);
  	}
  	return focObj;
  }
}
