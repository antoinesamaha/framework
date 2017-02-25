package com.foc.vaadin.gui.components;

import java.util.ArrayList;
import java.util.Collection;

import org.xml.sax.Attributes;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocObject;
import com.foc.property.FObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;
import com.vaadin.ui.PopupView;

@SuppressWarnings("serial")
public class FVObjectPopupView extends PopupView implements FocXMLGuiComponent, Field {//Needs to implement FocXMLGuiComponent because this is the component returned when event.
  
  private FocXMLGuiComponentDelegate     delegate      = null;//The delegate is the one coming from the selector
  private boolean                        delegateOwner = false;
  private ArrayList<ValueChangeListener> valueChangeListenerArray = null;
  private boolean priorityToCaptionProperty = true;
  
	public FVObjectPopupView(FObject objProperty, Attributes attributes) {
		super(new FVObjectPopupViewContent(objProperty, attributes));
		
		getPopupViewContent().setPopupView(this);
		delegate      = new FocXMLGuiComponentDelegate(this);
  	delegateOwner = true;
  	setStyleName("focPopupView_DropDownList");
	}
	
	public FVObjectPopupViewContent getPopupViewContent(){
		return (FVObjectPopupViewContent) getContent();
	}

	@Override
	public void dispose() {
		if(getContent() != null){
			((FVObjectPopupViewContent)getContent()).dispose();
		}
    if(delegate != null){
    	if(delegateOwner) delegate.dispose();
    	delegate = null;
    }
    if(valueChangeListenerArray != null){
    	valueChangeListenerArray.clear();
    	valueChangeListenerArray = null;
    }
	}

	@Override
	public IFocData getFocData() {
		FVObjectPopupViewContent content = getPopupViewContent();		
		return content != null ? content.getFocData() : null;
	}

	@Override
	public void setFocData(IFocData focData) {
		FVObjectPopupViewContent content = getPopupViewContent();		
		if(content != null){
			content.setFocData(focData);
		}
	}

	@Override
	public String getXMLType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Field getFormField() {
		return this;
	}

	@Override
	public boolean copyGuiToMemory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void copyMemoryToGui() {
		// TODO Auto-generated method stub
	}

	@Override
	public Attributes getAttributes() {
		return getPopupViewContent() != null ? getPopupViewContent().getAttributes() : null;
	}

	@Override
	public void setAttributes(Attributes attributes) {
		if(getPopupViewContent() != null){
			getPopupViewContent().setAttributes(attributes);
		}
	}

	@Override
	public String getValueString() {
		String valueStr = "";
		FocObject objValue = (FocObject) getValue();
		if(objValue != null){
			FVObjectPopupViewContent content = getPopupViewContent();
			String captionProperty = content != null ? content.getColumn1_DataPath(true) : null;		
			if(content != null && captionProperty != null){
				valueStr = objValue.getPropertyString(captionProperty);
			}
		}
		return valueStr;
	}
	
	//hadi_28 start
	@Override
	public void setValueString(String value) {
		//This is mainly for the UnitTesting
		//The aim is to get as close as possible to the GUI behavior.
		
		FVObjectPopupViewContent content = getPopupViewContent();
		FocObject foundObject = getValueObject(content, value, false);
		setValue(foundObject);
	}
	
	private FocObject getValueObject(FVObjectPopupViewContent content, String value, boolean checkForColumn2DataPath) {
		FocObject foundObject = null;
		String captionProperty = !checkForColumn2DataPath && content != null ? content.getColumn1_DataPath(isPriorityToCaptionProperty()) : null;
		if(checkForColumn2DataPath){
			captionProperty = content != null ? content.getColumn2_DataPath(isPriorityToCaptionProperty()) : null;
		}
		if(content != null && captionProperty != null){
			FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) content.getPopupComponent();
			ITableTree table = tableWrapper != null ? tableWrapper.getTableOrTree() : null;
			
			if(table != null){
				FocDataWrapper wrapper = table.getFocDataWrapper();
				for(int i=0; i<wrapper.size(); i++){
					FocObject currentFocObj = wrapper.getAt(i);
					String currentValue = currentFocObj.getPropertyString(captionProperty);
					if(currentValue != null && currentValue.equals(value)){
						foundObject = currentFocObj;
						break;
					}
				}
				
				if(foundObject == null && !checkForColumn2DataPath){
					foundObject = getValueObject(content, value, true);
				}
//				setValue(foundObject);
			}
		}
		return foundObject;
	}
	//hadi_28 end

	/*@Override
	public void setValueString(String value) {
		//This is mainly for the UnitTesting
		//The aim is to get as close as possible to the GUI behavior.
		
		FVObjectPopupViewContent content = getPopupViewContent();
		String captionProperty = content != null ? content.getColumn1_DataPath(true) : null;
		if(content != null && captionProperty != null){
			FVTableWrapperLayout tableWrapper = (FVTableWrapperLayout) content.getPopupComponent();
			ITableTree table = tableWrapper != null ? tableWrapper.getTableOrTree() : null;
			
			if(table != null){
				FocObject foundObject = null;
				FocDataWrapper wrapper = table.getFocDataWrapper();
				for(int i=0; i<wrapper.size(); i++){
					FocObject currentFocObj = wrapper.getAt(i);
					String currentValue = currentFocObj.getPropertyString(captionProperty);
					if(currentValue != null && currentValue.equals(value)){
						foundObject = currentFocObj;
						break;
					}
				}
				
				setValue(foundObject);
			}
		}
	}*/

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

	public void focus(){
		super.focus();
	}
	
	//---------------------------------------
	//---------------------------------------
	//---------------------------------------
	// Field implementation
	//---------------------------------------
	//---------------------------------------
	//---------------------------------------
	
	@Override
	public boolean isInvalidCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInvalidCommitted(boolean isCommitted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discard() throws SourceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBuffered(boolean buffered) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBuffered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isModified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addValidator(Validator validator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeValidator(Validator validator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllValidators() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Validator> getValidators() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate() throws InvalidValueException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInvalidAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getValue() {
		FocObject objValue = null;
		FVObjectPopupViewContent content = getPopupViewContent();
		if(content != null && content.getObjectProperty() != null){
			objValue = (FocObject) content.getObjectProperty().getObject();
		}
		return objValue;
	}

	@Override
	public void setValue(Object newValue) throws ReadOnlyException {
		FVObjectPopupViewContent content = getPopupViewContent();
		if(content != null && content.getObjectProperty() != null){
			content.getObjectProperty().setObject(newValue);
		}
		fireValueChanged();
	}

	@Override
	public Class getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addValueChangeListener(ValueChangeListener listener) {
		if(listener != null){
			if(valueChangeListenerArray == null){
				valueChangeListenerArray = new ArrayList<Property.ValueChangeListener>();
			}
			if(!valueChangeListenerArray.contains(listener)){
				valueChangeListenerArray.add(listener);
			}
		}
	}

	@Override
	@Deprecated
	public void addListener(ValueChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeValueChangeListener(ValueChangeListener listener) {
		if(listener != null && valueChangeListenerArray != null){
			valueChangeListenerArray.remove(listener);
		}		
	}

	@Override
	@Deprecated
	public void removeListener(ValueChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Property getPropertyDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTabIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTabIndex(int tabIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRequired(boolean required) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRequiredError(String requiredMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRequiredError() {
		// TODO Auto-generated method stub
		return null;
	}
	// Field implementation - END
	//---------------------------------------
	
	public void fireValueChanged(){
		if(valueChangeListenerArray != null){
			ValueChangeEvent event = null;
			for(int i=0; i<valueChangeListenerArray.size(); i++){
				ValueChangeListener listener = valueChangeListenerArray.get(i);
				if(event == null){
					event = new ValueChangeEvent(this);
				}
				listener.valueChange(event);
			}
		}
	}
	
	public void setPriorityToCaptionProperty(boolean priorityToCaptionProperty){
		this.priorityToCaptionProperty = priorityToCaptionProperty;
	}
	
	public boolean isPriorityToCaptionProperty(){
		return priorityToCaptionProperty;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void clear() {
		
	}
}
