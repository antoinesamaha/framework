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
package com.foc.vaadin.gui.components;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FProperty;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FVRedirectWindow extends Window {

	private FVTextField textFieldSelectedObject = null;	
	private FVComboBox  comboBoxToSelectFrom    = null;
	private FocObject   selectedObject          = null;
	private FocList     listToChooseFrom        = null;
	private FVButton    replaceButton           = null;
	private FVButton    cancelButton            = null;
	private String      dataPath                = null;
	
	public FVRedirectWindow(FocList listToChooseFrom, FocObject selectedObject, String dataPath) {
		this.listToChooseFrom = listToChooseFrom;
		this.selectedObject = selectedObject;
		this.dataPath = dataPath;
		init();
	}
	
	private void init(){
		setWidth("600px");
		setHeight("-1px");
		setCaption("Replace Object references");
		setModal(true);
	}
	
	public void dispose(){
		if(textFieldSelectedObject != null){
			textFieldSelectedObject.dispose();
			textFieldSelectedObject = null;
		}
		if(comboBoxToSelectFrom != null){
			comboBoxToSelectFrom.dispose();
			comboBoxToSelectFrom = null;
		}
		selectedObject = null;
		listToChooseFrom = null;
		if(replaceButton != null){
			replaceButton.dispose();
			replaceButton = null;
		}
		if(cancelButton != null){
			cancelButton.dispose();
			cancelButton = null;
		}
		dataPath = null;
	}
	
	public void popup(){
		FVHorizontalLayout buttonsHorizontalLayout = new FVHorizontalLayout(null);
		buttonsHorizontalLayout.addComponent(getReplaceButton());
		buttonsHorizontalLayout.addComponent(getCancelButton());
		
		FVVerticalLayout fieldsHorizontalLayout = new FVVerticalLayout(null);
		fieldsHorizontalLayout.addComponent(getSelectedObjectTextField());
		fieldsHorizontalLayout.addComponent(getListToSelectFromComboBox());

		FVVerticalLayout mainVerticalLayout = new FVVerticalLayout();
		mainVerticalLayout.addComponent(fieldsHorizontalLayout);
		mainVerticalLayout.addComponent(buttonsHorizontalLayout);
		mainVerticalLayout.setMargin(true);
		setContent(mainVerticalLayout);
		
		FocWebApplication.getInstanceForThread().addWindow(this);
	}
	
	private FVComboBox getListToSelectFromComboBox(){
		if(comboBoxToSelectFrom == null){
			comboBoxToSelectFrom = new FVComboBox();
			comboBoxToSelectFrom.setWidth("250px");
			comboBoxToSelectFrom.setItemCaptionMode(ItemCaptionMode.ITEM);
			comboBoxToSelectFrom.setCaption("New Replacement");
			comboBoxToSelectFrom.addContainerProperty(dataPath, Object.class, "");
			comboBoxToSelectFrom.setContainerDataSource(listToChooseFrom);			
			comboBoxToSelectFrom.setItemCaptionPropertyId(dataPath);
		}
		return comboBoxToSelectFrom;
	}
	
	private FVTextField getSelectedObjectTextField(){
		if(textFieldSelectedObject == null){
			textFieldSelectedObject = new FVTextField("Selected Object");
			textFieldSelectedObject.setWidth("250px");
		}
		if(selectedObject != null && dataPath != null){
			FProperty property = selectedObject.getFocPropertyForPath(dataPath);
			Object    value    = property != null ? property.getValue() : null;
			if(value != null){
				textFieldSelectedObject.setValue(String.valueOf(value));
			}
		}
		return textFieldSelectedObject;
	}
	
	private FVButton getReplaceButton(){
		if(replaceButton == null){
			replaceButton = new FVButton("Replace");
			replaceButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					if(listToChooseFrom != null && getListToSelectFromComboBox() != null && (Long) getListToSelectFromComboBox().getValue() != null){
						FocObject focObjectToRedirectFrom = selectedObject;
						FocObject focObjectToRedirectTo   = listToChooseFrom.searchByReference((Long) getListToSelectFromComboBox().getValue());
						if(focObjectToRedirectFrom != null){
							focObjectToRedirectFrom.referenceCheck_RedirectToNewFocObject(focObjectToRedirectTo);
						}
					}
					close();
				}
			});
		}
		return replaceButton;
	}
	
	private FVButton getCancelButton(){
		if(cancelButton == null){
			cancelButton = new FVButton("Cancel");
			cancelButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
			  	close();
				}
			});
		}
		return cancelButton;
	}
}
