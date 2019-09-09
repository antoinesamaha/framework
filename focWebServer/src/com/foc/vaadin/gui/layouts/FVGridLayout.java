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
package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.windows.EditFieldsWindow;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;

import fi.jasoft.dragdroplayouts.DDGridLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

@SuppressWarnings("serial")
public class FVGridLayout extends DDGridLayout implements FVLayout {
  private String name;
  private String type;
  private boolean isDragDropEnabled;
  private Attributes attributes = null;
  
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVGridLayout(Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    addLayoutClickListener(listener);
    addStyleName("bg-white");
    setSpacing(true);
    setMargin(true);
  }
  
  @Override
  public void setWYSIWYGDropHandler() {
    setDropHandler(new FVGrid_WYSIWYG_DropHandler(this, null));
  }
  
  private LayoutClickListener listener = new LayoutClickListener() {
      
    @Override
    public void layoutClick(LayoutClickEvent event) {
      Component child = event.getChildComponent();
      
      if (event.isDoubleClick()) {
        if (child != null && isDragDropEnabled) {
          EditFieldsWindow editWin = EditFieldsWindow.getInstanceForThread();
          editWin.addTab(null, child, FVGridLayout.this, name, ((FocXMLGuiComponent) child).getAttributes(), false);
        }
      }
    }
  };
  
  @Override
  public String getXMLType() {
    return FXML.TAG_GRID_LAYOUT;
  }

  @Override
  public void addComponent(Component comp, Attributes attributes) {
  	String col_str_1 = attributes.getValue("col1");
  	if(col_str_1 == null || col_str_1.isEmpty()){
  		col_str_1 = attributes.getValue("col");
  	}

  	String row_str_1 = attributes.getValue("row1");
  	if(row_str_1 == null || row_str_1.isEmpty()){
  		row_str_1 = attributes.getValue("row");
  	}
  	
  	String col_str_2 = attributes.getValue("col2");
  	String row_str_2 = attributes.getValue("row2");

    int col1 = col_str_1 != null ? Integer.parseInt(col_str_1) : -1;
    int row1 = row_str_1 != null ? Integer.parseInt(row_str_1) : -1;
    int col2 = col_str_2 != null ? Integer.parseInt(col_str_2) : col1;
    int row2 = row_str_2 != null ? Integer.parseInt(row_str_2) : row1;
        
    try{
    	if(row1 >= 0 && col1 >=0 && row2>=0 && col2>=0){
    		addComponent(comp, col1, row1, col2, row2);
    	}
    }catch(GridLayout.OverlapsException e){
    	Globals.logException(e);
    	Globals.showNotification("Overlap at", "row : "+row1+" col : "+col1, IFocEnvironment.TYPE_ERROR_MESSAGE);
    }
    
    String columExpandRatioStr = attributes.getValue("columnExpandRatio");
    if(columExpandRatioStr != null){
    	float exp = Float.parseFloat(columExpandRatioStr);
    	setColumnExpandRatio(col1, exp);	
    }
  }

  @Override
  public void setDragDrop(boolean state) {
    if (state) {
      setDragMode(LayoutDragMode.CLONE);
      isDragDropEnabled = true;
    }
    else {
      setDragMode(LayoutDragMode.NONE);
      isDragDropEnabled = false;
    }
  }

  @Override
  public boolean isDragDrop() {
    return isDragDropEnabled;
  }

  @Override
  public ComponentPosition getPosition(Component comp) {
    return null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
    
    if(attributes != null) {
	    if (attributes.getValue("border") != null) {
	      addStyleName("border");
	    }
	    
	    if (attributes.getValue(FXML.ATT_ROWS) != null) {
	      setRows(Integer.parseInt(attributes.getValue(FXML.ATT_ROWS)));
	    }
	    
	    if (attributes.getValue(FXML.ATT_COLS) != null) {
	      setColumns(Integer.parseInt(attributes.getValue(FXML.ATT_COLS)));
	    }
	    
	    if (attributes.getValue(FXML.ATT_INNER_GRID_LAYOUT_BORDER) != null && attributes.getValue(FXML.ATT_INNER_GRID_LAYOUT_BORDER).equals("true")) {
	    	addStyleName("innerGridLayoutBorder");
	    }
    }
    setCaption("");
  }

  @Override
  public Attributes getAttributes() {
    return attributes;
  }

  @Override
  public boolean isXMLLeaf() {
    return false;
  }

  @Override
  public void fillXMLNodeContent(XMLBuilder builder) {
  }

	@Override
	public IFocData getFocData() {
		return null;
	}

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public boolean copyGuiToMemory() {
		return false;
	}

	@Override
	public void copyMemoryToGui() {
	}
	
  @Override
  public void setDelegate(FocXMLGuiComponentDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public FocXMLGuiComponentDelegate getDelegate() {
    return delegate;
  }
  
  @Override
  public void dispose(){
    name       = null;
    type       = null;
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
    removeAllComponents();
  }

  @Override
  public void setFocData(IFocData focData) {
  }
  
  @Override
  public String getValueString() {
    return null;
  }

  @Override
  public void setValueString(String value) {
  }
  
	@Override
	public FocCentralPanel getWindow() {
		return findAncestor(FocCentralPanel.class);
	}

	@Override
	public void refreshEditable() {
	}
}
