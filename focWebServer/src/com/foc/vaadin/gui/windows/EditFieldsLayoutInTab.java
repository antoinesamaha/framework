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
package com.foc.vaadin.gui.windows;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.desc.field.FField;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.XMLAttributesContainer;
import com.foc.vaadin.gui.XMLAttributesModel;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.layouts.FVLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class EditFieldsLayoutInTab extends VerticalLayout {
   
  private Table attrTable = null;
  
  private ArrayList<XMLAttributesModel> attributesList = null;
  private XMLAttributesContainer        container      = null;
  
  private Component componentEdited = null;
  
  private FocXMLLayout xmlLayout  = null;
  private Attributes   attributes = null;
  private String       name       = null;
  private boolean      isPalette  = false;
  private FVLayout     layout     = null;
  
  private static final String[] NON_DISPLAYABLE_ATT = {FXML.ATT_NAME, FXML.ATT_ROW, FXML.ATT_COL, FXML.ATT_IDX};
  
  //This is to allow auto assignment of the attribute captionPos  
  private static String lastCaptionPos   = null;
  private static String lastCaptionWidth = null;
  
  public EditFieldsLayoutInTab(FocXMLLayout xmlLayout, Component componentEdited, FVLayout layout, String name, Attributes attributes, boolean isPalette) {
  	this.xmlLayout       = xmlLayout;
    this.layout          = layout;
  	this.componentEdited = componentEdited;
  	this.attributes      = attributes;
  	this.attributesList  = new ArrayList<XMLAttributesModel>();
  	this.name            = name;
  	this.isPalette       = isPalette;
	
  	try {
      container = new XMLAttributesContainer();
    } catch (InstantiationException e) {
      Globals.logException(e);
    } catch (IllegalAccessException e) {
    	Globals.logException(e);
    }
  	
  	if (componentEdited == null) {
  	  if (isPalette) {
  	    if (name.equals(FXML.TAG_LABEL)) {
  	      loadLabelAttributes();
  	    } else {
  	      loadLayoutAttributes();
  	    }
  	  } else {
  	    loadGuiFieldAttributes();
  	  }
  	} else {
  	  loadAttributes();
  	}
  	
    attrTable = new Table();
    //attrTable.addStyleName(Reindeer.TABLE_BORDERLESS);
    initTable();
    attrTable.setSizeFull();
   
    setSpacing(true);
       
    addComponent(attrTable);
    setComponentAlignment(attrTable, Alignment.MIDDLE_CENTER);
  }
  
  public ICentralPanel getCentralPanel(){
    ICentralPanel centralPanel = null;
    Component comp = (Component) componentEdited;      
    while(comp != null && comp.getParent() != null){
      if(comp instanceof ICentralPanel){
        centralPanel = (ICentralPanel) comp;
      }
      comp = comp.getParent();
    }
    return centralPanel;
  }

  private void initTable() {
    container.addAll(attributesList);
    attrTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    attrTable.setContainerDataSource(container);
    attrTable.setEditable(true);
  }
    
  private void loadAttributes() {
    outer:
    for (int i = 0; i < attributes.getLength(); i++) {
      String attrName = attributes.getQName(i);
      
      for (int j = 0; j < NON_DISPLAYABLE_ATT.length; j++) {
        if (attrName.equals(NON_DISPLAYABLE_ATT[j])) {
          continue outer;
        }
      }
      
      String value = "";
      
      if (attributes instanceof FocXMLAttributes) {
        value = ((FocXMLAttributes) attributes).getValueWithoutResolve(attributes.getIndex(attrName));
      } else {
        value = attributes.getValue(attributes.getIndex(attrName));
      }
      
      attributesList.add(new XMLAttributesModel(attributes.getQName(i), value));
    }
  }
  
  private void loadLabelAttributes() {
    attributesList.add(new XMLAttributesModel("value", ""));
    attributesList.add(new XMLAttributesModel("style", ""));
  }
  
  private void loadLayoutAttributes() {
    attributesList.add(new XMLAttributesModel(FXML.ATT_WIDTH, ""));
    attributesList.add(new XMLAttributesModel(FXML.ATT_HEIGHT, ""));
    attributesList.add(new XMLAttributesModel("border", ""));
    if(name.equals(FXML.TAG_GRID_LAYOUT)){
    	attributesList.add(new XMLAttributesModel(FXML.ATT_ROWS, ""));
    	attributesList.add(new XMLAttributesModel(FXML.ATT_COLS, ""));	
    }
  }
  
  private void loadGuiFieldAttributes() {
  	String defaultCaption = "";
	  FField field = getFocField();
		if(field != null){
			defaultCaption = field.getTitleForGuiDetailsPanel();
		}
    attributesList.add(new XMLAttributesModel(FXML.ATT_CAPTION, defaultCaption));
    attributesList.add(new XMLAttributesModel(FXML.ATT_CAPTION_POSITION, lastCaptionPos != null ? lastCaptionPos : "top"));
    attributesList.add(new XMLAttributesModel(FXML.ATT_CAPTION_WIDTH, lastCaptionWidth != null ? lastCaptionWidth : "-1px"));
    attributesList.add(new XMLAttributesModel(FXML.ATT_WIDTH, "-1px"));
    attributesList.add(new XMLAttributesModel(FXML.ATT_HEIGHT, "-1px"));
    if(name.contains(".")){
    	attributesList.add(new XMLAttributesModel(FXML.ATT_EDITABLE, "false"));
    }
  }
  
  public IFocData getFocData(){
  	IFocData focData = null;
  	if(componentEdited != null){
  		focData = ((FocXMLGuiComponent)componentEdited).getFocData();
  	}else if(xmlLayout != null && xmlLayout.getFocData() != null){
  		focData = xmlLayout.getFocData().iFocData_getDataByPath(name);
  	}
  	return focData;
  }
  
  public FProperty getFocProperty(){
  	FProperty property = null;
  	IFocData  focData  = getFocData();
  	if(focData != null && focData instanceof FProperty){
  		property = (FProperty) focData;
  	}
  	return property;
  }

  public FField getFocField(){
  	FField fld = null;
  	
  	IFocData focData = getFocData();
  	if(focData != null){
	  	if(focData instanceof FProperty){
	  		fld = ((FProperty)focData).getFocField();
	  	}else if(focData instanceof FField){
	  		fld = (FField) focData; 
	  	}
  	}
  	
  	return fld;
  }
  
  public void saveChanges(){
    FocXMLAttributes attributes = new FocXMLAttributes(null, this.attributes);

    for (int i = 0; i < attributesList.size(); i++) {
      String key   = attributesList.get(i).getKey();//.equals(attributesList.get(i).getCaption()) ? attributesList.get(i).getCaption(): attributesList.get(i).getKey();
      String value = attributesList.get(i).getValue();
      
      if (attributes.getValue(key) != null) {
        attributes.setValue(attributes.getIndex(key), value);
      } else {
        attributes.addAttribute("", key, key, "CDATA", value);
      }
    }
    
    if (componentEdited != null) {
      FocXMLGuiComponent guiType = (FocXMLGuiComponent) componentEdited;
      guiType.setAttributes(attributes);

      ICentralPanel centralPanel = getCentralPanel();
      ArrayList<FVLayout> layouts = centralPanel.getLayouts();
      ((FocXMLGuiComponent)componentEdited).setAttributes(attributes);
      XMLBuilder xmlBuilder = new XMLBuilder(layouts.get(0));
      centralPanel.getXMLView().saveXML(xmlBuilder.getXMLString());
    } else {
      if (isPalette) {
        xmlLayout.newGuiPaletteComponent(layout, name, name, null, null, attributes);
      } else {
        xmlLayout.newGuiField(layout, name, name, attributes);
      }
    }
  }
 
}
