package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import fi.jasoft.dragdroplayouts.DDHorizontalSplitPanel;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

@SuppressWarnings("serial")
public class FVHorizontalSplitLayout extends DDHorizontalSplitPanel implements FVLayout {

  private String name = null;
  private String type = null;
  private Attributes attributes = null;
  private boolean isDragDropEnabled;
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVHorizontalSplitLayout(Attributes attributes){
    delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    setWidth("100%");
    setHeight("200px");
    addSplitterClickListener(new AbstractSplitPanel.SplitterClickListener() {
      @Override
      public void splitterClick(SplitterClickEvent event) {
        
      }
    });
    addStyleName(FVLayout.DEFAULT_STYLE);
    setDropHandler(new FVVerticalDropHandler(this));
  }
  
  @Override
  public void dispose() {
    attributes = null;
    delegate   = null;
    name       = null;
    type       = null;
  }

  @Override
  public IFocData getFocData() {
    return null;
  }

  @Override
  public void setFocData(IFocData focData) {
  }

  @Override
  public String getXMLType() {
    return FXML.TAG_VERTICAL_SPLIT_LAYOUT;
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
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
    if(attributes != null){
      String splitPosAttribute = attributes.getValue(FXML.ATT_SPLIT_POSITION);
      if (splitPosAttribute != null) {
      	String value = null;
      	Sizeable.Unit unit = Sizeable.Unit.PERCENTAGE;
      	if(splitPosAttribute.endsWith("%")){
      		value = splitPosAttribute.substring(0, splitPosAttribute.length() - 1);
      	}else if(splitPosAttribute.endsWith("px")){
      		value = splitPosAttribute.substring(0, splitPosAttribute.length() - 2);
      		unit  = com.vaadin.server.Sizeable.Unit.PIXELS;
      	}
      	
      	int valueInt = Integer.valueOf(value);
        setSplitPosition(valueInt, unit);
      }
    }
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
  public void addComponent(Component comp, Attributes attributes) {
    
    addComponent(comp);
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