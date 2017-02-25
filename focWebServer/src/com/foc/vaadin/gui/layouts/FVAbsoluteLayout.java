package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

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
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

@SuppressWarnings("serial")
public class FVAbsoluteLayout extends DDAbsoluteLayout implements FVLayout {

  private String name;
  private String type;
  private boolean isDragDropEnabled;
  private Attributes attributes = null;
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVAbsoluteLayout(Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    
    if (attributes.getValue("border") != null) {
      addStyleName("border");
    }
    
    setCaption("");
    addLayoutClickListener(listener);
    
    setDropHandler(new FVAbsoulteDropHandler(this));
    addStyleName(FVLayout.DEFAULT_STYLE);
  }
  
  private LayoutClickListener listener = new LayoutClickListener() {
    
    @Override
    public void layoutClick(LayoutClickEvent event) {
      Component child = event.getChildComponent();
      
      if (event.isDoubleClick()) {
        if (child != null && isDragDropEnabled) {
          EditFieldsWindow editFieldsWindow = EditFieldsWindow.getInstanceForThread();
          editFieldsWindow.addTab(null, child, FVAbsoluteLayout.this, name, ((FocXMLGuiComponent) child).getAttributes(), false);
        }
      }
    }
  };

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public void addComponent(Component comp, Attributes attributes) {
    String location = "";
    String topValue = attributes.getValue(FXML.ATT_TOP);
    if(topValue != null){
      location = "top:"+topValue+";";
    }
    String bottomValue = attributes.getValue("bottom");
    if(bottomValue != null){
      location += "bottom:"+bottomValue+";";
    }
    String leftValue = attributes.getValue(FXML.ATT_LEFT);
    if(leftValue != null){
      location += "left:"+leftValue+";";
    }
    String rightValue = attributes.getValue("right");
    if(rightValue != null){
      location += "right:"+rightValue+";";
    }
    
    addComponent(comp, location);
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
  public String getXMLType() {
    return FXML.TAG_ABSOLUTE_LAYOUT;
  }
  
  @Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
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
    delegate   = null;
  }

  @Override
  public void setFocData(IFocData focData) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getValueString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setValueString(String value) {
    // TODO Auto-generated method stub
    
  }

	@Override
	public FocCentralPanel getWindow() {
		return findAncestor(FocCentralPanel.class);
	}

	@Override
	public void refreshEditable() {
	}
}
