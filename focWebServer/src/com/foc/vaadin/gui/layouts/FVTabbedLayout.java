package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.foc.Globals;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.windows.AddTabWindow;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVTabbedLayout extends TabSheet implements FVLayout {

  private String name;
  private String type;
  private boolean isDragDropEnabled;
  private Attributes attributes = null;
  
  private VerticalLayout addTabLayout;
  private Button addTabButton;
  
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVTabbedLayout(Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    addTabLayout = new VerticalLayout();
    if(delegate.isConstructionMode()){    
      addTabButton = new Button("Add Tab");
  
      addTabButton.addClickListener(new ClickListener() {
        
        @Override
        public void buttonClick(ClickEvent event) {
          if (isDragDropEnabled)
            addNewTab();
        }
      });
    
      addTabLayout.addComponent(addTabButton);

      addTab(addTabLayout, "+");
    }
    
    addStyleName(FVLayout.DEFAULT_STYLE);
  }
  
  @Override
  public IFocData getFocData() {
    return null;
  }

  @Override
  public String getXMLType() {
    return FXML.TAG_TAB_LAYOUT;
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
    
    if (attributes != null && attributes.getValue("border") != null) {
      addStyleName("border");
    }
    
    setCaption("");
  }

  @Override
  public void addComponent(Component comp, Attributes attributes) {
    comp.addStyleName("padding");
    
    String title = attributes.getValue(FXML.ATT_TITLE);
    if(title == null){
    	title = attributes.getValue(FXML.ATT_NAME);
    }
    
    String positionString = attributes.getValue(FXML.ATT_TAB_POSITION);
    if(positionString != null){
    	int position = 0;
    	try {
    		position = Integer.parseInt(positionString);
    	} catch (NumberFormatException e) {
    		Globals.logException(e);
    	}
    	
    	addTab(comp, title, null, position);
    }else{
    	addTab(comp, title);
    }
    
    if(delegate != null && delegate.isConstructionMode()){
      removeTab(getTab(addTabLayout));
      addTab(addTabLayout, "+");
    }
  }
  
  public void addNewTab() {
    getUI().addWindow(new AddTabWindow(this, addTabLayout));
  }

  @Override
  public void setDragDrop(boolean state) {
    if (state) {
      isDragDropEnabled = true;
    }
    else {
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
  public void setDelegate(FocXMLGuiComponentDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public FocXMLGuiComponentDelegate getDelegate() {
    return delegate;
  }
  
  @Override
  public void dispose(){
    name         = null;
    type         = null;
    attributes   = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
    addTabButton = null;
    addTabLayout = null;
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
