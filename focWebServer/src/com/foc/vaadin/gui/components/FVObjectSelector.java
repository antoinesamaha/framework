package com.foc.vaadin.gui.components;

import java.util.Collection;
import java.util.Iterator;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.desc.FocObject;
import com.foc.property.FObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.objectSelectorPopupView.FVObjectSelectorWindow;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVObjectSelector extends FVGearWrapper<FVObjectComboBox> implements FocXMLGuiComponent {
	private Attributes                 attributes         = null;
	private FocXMLGuiComponentDelegate delegate           = null;
	
	private PopupLinkButton addButton    = null;
	private PopupLinkButton openButton   = null;
	private PopupLinkButton reloadButton = null;
	private AddNewItemListener addNewItemListener = null;
	
	public FVObjectSelector(FObject property, Attributes attributes){
		super();
		delegate = new FocXMLGuiComponentDelegate(this);
		setAttributes(attributes);//We need the attributes here to get the captionProperty
		FVObjectComboBox comboBox = new FVObjectComboBox(property, getCaptionProperty(), getAttributes());
		comboBox.setDelegate(delegate);
		/*
		boolean showGear = true;
		if(attributes != null){
		  String hideGear = attributes.getValue(FXML.ATT_HIDE_GEAR);
		  if(hideGear != null && hideGear.equalsIgnoreCase("true")){
		    showGear = false;
		  }
		}
		*/
		
		addNewItemListener = new AddNewItemListener(comboBox);
		comboBox.setNewItemHandler(addNewItemListener);
		
		setComponent(comboBox, true);
		applyAttributes();//We also apply the attributes after the creation of the combo because the editable attrib acts on it
		addStyleName("component-margin");
	}
	
	public FVObjectComboBox getComboBox() {
		return getComponent();
	}

  @Override
  public void dispose(){
  	FVObjectComboBox comboBox = getComboBox(); 
  	if(comboBox != null){
  		comboBox.setNewItemHandler(null);
  	}
    super.dispose();
    attributes = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
    
    if(addButton != null){
    	addButton.dispose();
    	addButton = null;
    }
    if(openButton != null){
    	openButton.dispose();
    	openButton = null;
    }
    if(reloadButton != null){
    	reloadButton.dispose();
    	reloadButton = null;
    }
    if(addNewItemListener != null){
    	addNewItemListener.dispose();
    	addNewItemListener = null;
    }
  }
  
	@Override
	public FObject getFocData(){
		return getComboBox() != null ? (FObject) getComboBox().getFocData() : null;
	}
	
	@Override
	public Field getFormField() {
		Field fld = getComboBox();
		return fld; 
	}

	public String getCaptionProperty(){
		String captionProp = null;
		Attributes att = getAttributes();
		if(att != null){
			captionProp = att.getValue(FXML.ATT_CAPTION_PROPERTY);
		}
		return captionProp;
	}
	
	public void applyAttributes(){
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
	}
	
	@Override
	public void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		if(getComboBox() != null){
			getComboBox().setEnabled(enabled);
		}
	}
	
	@Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    applyAttributes();
  }
	
	@Override
  public Attributes getAttributes() {
    return attributes;
  }
	
	@Override
  public String getXMLType() {
    return FXML.TAG_FIELD;
  }
	
  @Override
  public boolean copyGuiToMemory() {
    return getComboBox() != null ? getComboBox().copyGuiToMemory() : false;
  }

  @Override
  public void copyMemoryToGui() {
  	FVObjectComboBox objComboBox = getComboBox();
  	if(objComboBox != null){
  		objComboBox.copyMemoryToGui();
  	}else{
  		//In Tecman DB when we change the view from Standard to Creation Tec then we change the value of any field (NAME fro example)
  		//We get a objComboBox == null.
  		int debug = 3;
  		debug++;
  	}
  }

	@Override
	public void fillMenu(VerticalLayout root) {
	  addButton = new PopupLinkButton("Add", new ClickListener() {
    	@Override
			public void buttonClick(ClickEvent event) {
    		if(getComboBox() != null){
    			getComboBox().addNewObject();
    		}
			}
		});
    root.addComponent(addButton);
    
    openButton = new PopupLinkButton("Open", new ClickListener() {
    	@Override
			public void buttonClick(ClickEvent event) {
    		FocObject focObjToOpen = getComboBox() != null ? getComboBox().getFocObject_FromGuiCombo() : null;
				if(focObjToOpen == null){
					Globals.showNotification("Field is empty", "a selection must be made to open object", IFocEnvironment.TYPE_WARNING_MESSAGE);
				}else{
					getComboBox().openObjectDetailsPanel(focObjToOpen, true);
				}
			}
		});
    root.addComponent(openButton);

    reloadButton = new PopupLinkButton("Reload", new ClickListener() {
    	@Override
			public void buttonClick(ClickEvent event) {
    		FVObjectComboBox objectComboBox = getComboBox();
    		if(objectComboBox != null){
    			objectComboBox.reloadList();
    		}
			}
		});
    root.addComponent(reloadButton);
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
    FVObjectComboBox oldComboBox = getComboBox();
    if(oldComboBox != null){
    	removeComponent(oldComboBox);
    }
    if(focData instanceof FObject){//Sometimes we get the FObectField when the path leads to null object. So in this case we cannot draw a component.
      FVObjectComboBox comboBox = new FVObjectComboBox((FObject) focData, getCaptionProperty());      
      if(oldComboBox != null){
      	Collection valueChangeListener = (Collection) oldComboBox.getListeners(ValueChangeEvent.class);
        Iterator listenerItr = valueChangeListener.iterator();
        while(listenerItr.hasNext()){
      		comboBox.addValueChangeListener((ValueChangeListener) listenerItr.next());
        }
        
        addNewItemListener = new AddNewItemListener(comboBox);
    		comboBox.setNewItemHandler(addNewItemListener);
      }
      if(getAttributes() != null){
        String isImmediate = getAttributes().getValue(FXML.ATT_IMMEDIATE);
        if(isImmediate != null && !isImmediate.isEmpty() && isImmediate.equals("true")){
          comboBox.setImmediate(true);
        }
      }
	    setComponent(comboBox, false);
    }
    setAttributes(getAttributes());
  }
  
  @Override
  public String getValueString() {
    return getComboBox().getItemCaption(getComboBox().getValue());
  }

  @Override
  public void setValueString(String value) {
  	if(getComboBox() != null){
	    getComboBox().setValueString(value);
  	}
  }
  
  public PopupLinkButton getAddButton(){
    return addButton;
  }
  
  public PopupLinkButton getOpenButton(){
    return openButton;
  }
  
	@Override
	public void refreshEditable() {
		boolean editable = getDelegate() != null ? getDelegate().isEditable() : true;
		if(getComponent() != null){
			getComponent().setEnabled(editable);
		}
		setEnabled(editable);
	}
	
	private class AddNewItemListener implements NewItemHandler{

		private FVObjectSelectorWindow fvObjectSelectorWindow = null;
  	
  	public AddNewItemListener(FVObjectComboBox comboBox) {
  		if(comboBox != null){
  			comboBox.setNewItemsAllowed(true);
  		}
		}
  	
  	public void dispose(){
  		if(fvObjectSelectorWindow != null){
  			fvObjectSelectorWindow.dispose();
  			fvObjectSelectorWindow = null;
  		}
  	}

		@Override
		public void addNewItem(String newItemCaption) {
			fvObjectSelectorWindow = new FVObjectSelectorWindow(FVObjectSelector.this, newItemCaption);
			fvObjectSelectorWindow.show();
		}
  	
  }
}
