package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.Field;
import com.vaadin.ui.PopupView;

@SuppressWarnings("serial")
public class FVExpandingTextAreaPopupView extends PopupView implements FocXMLGuiComponent{

	private FocXMLGuiComponentDelegate delegate = null;
	private Attributes attributes = null;
	
	public FVExpandingTextAreaPopupView(FProperty property, Attributes attributes) {
		super(new FVExpandingTextAreaPopupViewContent(property, attributes));
		getPopupViewContent().setPopupView(this);
		
		this.attributes = attributes;
		delegate = new FocXMLGuiComponentDelegate(this);
    
    setFocData(property);
    setAttributes(attributes);
	}
	
	@Override
	public void dispose() {
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}
		FVExpandingTextAreaPopupViewContent choiceOptionGroupPopupViewContent = getPopupViewContent();
		if(choiceOptionGroupPopupViewContent != null){
			choiceOptionGroupPopupViewContent.dispose();
			choiceOptionGroupPopupViewContent = null;
		}
		attributes = null;		
	}

	public FVExpandingTextAreaPopupViewContent getPopupViewContent(){
		return (FVExpandingTextAreaPopupViewContent) getContent();
	}
	
	@Override
	public IFocData getFocData() {
		return getPopupViewContent() != null ? getPopupViewContent().getFocData() : null;
	}

	@Override
	public void setFocData(IFocData focData) {
		if(getPopupViewContent() != null){
			getPopupViewContent().setFocData(focData);
		}		
	}

	@Override
	public String getXMLType() {
		return FXML.TAG_FIELD;
	}

	@Override
	public Field getFormField() {
		return null;
	}
	
	public FVExpandingTextArea getFVExpandingTextArea(boolean createIfNeeded){
		FVExpandingTextArea expandingTextArea = null;
		if(getPopupViewContent() != null && getPopupViewContent().getPopupComponent(createIfNeeded) != null){
			expandingTextArea = (FVExpandingTextArea) getPopupViewContent().getPopupComponent();
		}
		return expandingTextArea;
	}

	@Override
	public boolean copyGuiToMemory() {
		FVExpandingTextArea group = getFVExpandingTextArea(false);
		if(group != null && getFocData() instanceof FProperty){
	    ((FProperty)getFocData()).setValue(group.getValue());
	  }
		return false;
	}

	@Override
	public void copyMemoryToGui() {
		if(getFocData() instanceof FProperty){
			FVExpandingTextArea group = getFVExpandingTextArea(false);
			if(group != null){
				String value = "";
				if(getFocData() instanceof FProperty){
					Object value_obj = ((FProperty)getFocData()).getValue();
					value = String.valueOf(value_obj);
				}
				group.setValue(value);
			}
    }		
	}

	@Override
	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);		
	}

	@Override
	public String getValueString() {
		String value = "";
		FVExpandingTextAreaPopupViewContent content = getPopupViewContent();
		FVExpandingTextArea label = (FVExpandingTextArea) (content != null ? content.getPopupComponent() : null);
		if(label != null){
			value = label.getValueString();
		}
		return value;
	}

	@Override
	public void setValueString(String value) {
		FVExpandingTextAreaPopupViewContent content = getPopupViewContent();
		FVExpandingTextArea label = (FVExpandingTextArea) (content != null ? content.getPopupComponent() : null);
		if(label != null){
			label.setValueString(value);
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
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);		
	}

}
