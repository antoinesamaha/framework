package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.desc.field.FField;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.data.Container;
import com.vaadin.ui.Field;
import com.vaadin.ui.Tree;

import fi.jasoft.dragdroplayouts.drophandlers.DefaultAbsoluteLayoutDropHandler;

@SuppressWarnings("serial")
public class FVTree extends Tree implements FocXMLGuiComponent {
  
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVTree() {
  	delegate = new FocXMLGuiComponentDelegate(this);
    initTree();
  }
  
  @Override
  public void dispose(){
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
  }
  
  private void initTree() {
    setDragMode(TreeDragMode.NODE);
    setDropHandler(new DefaultAbsoluteLayoutDropHandler());
  }
  
  private static String getTreeNodeName(Container.Hierarchical source, Object sourceId) {
    return (String) source.getItem(sourceId).getItemProperty(FField.FLD_NAME).getValue();
  }

  @Override
  public Field getFormField() {
    return null;
  }
  
  @Override
  public void setAttributes(Attributes attributes) {
  	FocXMLGuiComponentStatic.applyAttributes(this, attributes);
  }
  
  @Override
  public Attributes getAttributes() {
    return null;
  }
  
  @Override
  public FProperty getFocData() {
    return null;
  }
  
  @Override
  public String getXMLType() {
    return FXML.TAG_FIELD;
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
	public void refreshEditable() {
	}
}
