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

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.fab.gui.xmlView.IAddClickSpecialHandler;
import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.access.FocDataMap;
import com.foc.dataWrapper.FocDataWrapper.ListFilterUsingAFormulaExpression;
import com.foc.dataWrapper.FocDataWrapper.ListFilterUsingPropertyValue;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.objectSelectorPopupView.IObjectSelectWindowListener;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.dataModel.FocDataItem_ForComboBoxActions;
import com.foc.web.dataModel.FocListWrapper_ForObjectSelection;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_ObjectComboBox;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class FVObjectComboBox extends ComboBox implements FocXMLGuiComponent {//Needs to implement FocXMLGuiComponent because this is the component returned when event.
  
  private IFocData                    focData  = null;

  private FocXMLGuiComponentDelegate  delegate      = null;//The delegate is the one coming from the selector
  private boolean                     delegateOwner = false;
	private Attributes                  attributes    = null;
	private FocListWrapper              selectionWrapperList = null; 
	private ICentralPanel               openedCentralPanel = null;
	private IObjectSelectWindowListener iObjectSelectWindowListener = null;
	private ValueChangeListener         valueChangeListener = null;
	private UnitTestingRecorder_ObjectComboBox recorder = null;
//	private boolean                     disableChangeOfValue = false;
	
  public FVObjectComboBox(IFocData objProperty) {
    this(objProperty, (String)null);
    setImmediate(true);
    init();
  }
  
  public FVObjectComboBox(IFocData objProperty, String captionFieldName) {
  	this(objProperty, captionFieldName, null);
  }
  
  public FVObjectComboBox(IFocData objProperty, String captionFieldName, Attributes attributes) {
  	recorder = new UnitTestingRecorder_ObjectComboBox(this);
  	setAttributes(attributes);
    setFocProperty(objProperty, captionFieldName);
    setFilteringMode(FilteringMode.CONTAINS);
    addStyleName("component-margin");
    setImmediate(true);
    init();
  }

  //This is used when no Gear
  public FVObjectComboBox(IFocData objProperty, Attributes attributes) {
  	delegate = new FocXMLGuiComponentDelegate(this);
  	recorder = new UnitTestingRecorder_ObjectComboBox(this);
  	delegateOwner = true;
  	setAttributes(attributes);
  	setFocData(objProperty);
  	
  	if(			attributes != null 
  			&& 	attributes.getValue(FXML.ATT_FILTERING_MODE) != null
  			&& 	attributes.getValue(FXML.ATT_FILTERING_MODE).equals(FXML.VAL_FILTERING_MODE_STARTS_WITH)
  			){
  		setFilteringMode(FilteringMode.STARTSWITH);
  	}else{
  		setFilteringMode(FilteringMode.CONTAINS);
  	}
  	init();
  }
  
  public long getFocObjectRef_ForTheADDIcon(){
  	long ref = 0;
  	if(getSelectionWrapperList() != null && getSelectionWrapperList() instanceof FocListWrapper_ForObjectSelection){
  		ref = ((FocListWrapper_ForObjectSelection)getSelectionWrapperList()).getFocObjectRef_ForTheADDIcon();	
  	}
  	return ref;
  }
  
  public long getFocObjectRef_ForTheREFRESHIcon(){
  	long ref = 0;
  	if(getSelectionWrapperList() != null && getSelectionWrapperList() instanceof FocListWrapper_ForObjectSelection){
  		ref = ((FocListWrapper_ForObjectSelection)getSelectionWrapperList()).getFocObjectRef_ForTheREFRESHIcon();	
  	}
  	return ref;
  }
  
  private void addItemToFocListAction() {
		copyMemoryToGui();
		Object backupValue = getValue();
		addNewObject();
		Object newValue = getValue();
		if(newValue instanceof Long){
			long newSeletedValue = (Long) newValue;
			if(newSeletedValue == FocDataItem_ForComboBoxActions.REF_ADD){
				setValue(backupValue);
			}
		}
  }
  
  private void init(){
  	if(Globals.isValo()){
  		valueChangeListener = new ValueChangeListener() {
				@Override
				public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
//					if(disableChangeOfValue){
//						copyMemoryToGui();
//					}else 
					if(event != null && event.getProperty() != null && event.getProperty().getValue() instanceof Long){
						long valueInteger = Long.valueOf(event.getProperty().getValue()+"");
						
						if(valueInteger == getFocObjectRef_ForTheADDIcon() && getMainWindow() != null){
//						if(isSelected(getFocObjectRef_ForTheADDIcon()) && getMainWindow() != null){
//							addNewObject();
							//We need it like this when we add a new unit then we do cancel, the combo has the 'Add new item' selected.
							addItemToFocListAction();
						}else if(isSelected(getFocObjectRef_ForTheREFRESHIcon()) && getMainWindow() != null){
							copyMemoryToGui();
							Object previousValue = getValue();
							reloadList();
							setValue(previousValue);
//							copyMemoryToGui();
//							if(getDelegate() != null && getDelegate().getFocXMLLayout() != null){
//								getDelegate().getFocXMLLayout().refresh();
//							}
							if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue() instanceof Long){
								Long propertyReference = (Long) event.getProperty().getValue();
								if(propertyReference == FocDataItem_ForComboBoxActions.REF_REFRESH){
									unselect(propertyReference);
								}
							}
						}
					}
				}
			};
	  	
	  	addValueChangeListener(valueChangeListener);
  	}
  }
  
	public void dispose(){
		if(valueChangeListener != null){
			removeValueChangeListener(valueChangeListener);
			valueChangeListener = null;
		}
    focData = null;
    if(delegate != null){
    	if(delegateOwner) delegate.dispose();
    	delegate = null;
    }
    openedCentralPanel = null;
    attributes = null;
    dispose_selectionWrapperList();
    iObjectSelectWindowListener = null;
  }
  
	public void dispose_selectionWrapperList(){
		if(selectionWrapperList != null){
			selectionWrapperList.dispose();
			selectionWrapperList = null;
		}
	}
	
	public void unitTesting_AddItemSelected() throws Exception {
		long refOfAddIcon = getFocObjectRef_ForTheADDIcon();
		if(getItem(refOfAddIcon) == null) {
			throw new Exception("Could not find the ADD icon");
		}
				
		addItemToFocListAction();
	}
	
	public void reloadList(){
		FocList focList = getSelectionFocList();
		if(focList != null){
			focList.reloadFromDB();
			refreshGuiForContainerChanges();
		}
	}

	public void refreshGuiForContainerChanges(){
		if(getListWrapper() != null){
			getListWrapper().refreshGuiForContainerChanges();
		}
	}
	
	public FocObject getFocObject_Master(){
		FocObject masterFocObject = null;
		if(getFocData() != null && getFocData() instanceof FObject){
	    FObject objProperty = (FObject) getFocData();
	    if(objProperty != null){
	    	masterFocObject = objProperty.getFocObject();
	    }
		}
    return masterFocObject;
	}
	
  public FocList getSelectionFocList(){
		FocList focList = null;
		IFocData focData = getFocData(); 
		if(focData != null){
			if(focData instanceof FObject){
				focList = ((FObject)focData).getPropertySourceList();
			}else if(getFocData() instanceof FObjectField){
				focList = ((FObjectField)focData).getSelectionList();
			}
		}
		
		return focList;
  }
  
	private FocListWrapper newSelectionFocListWrapper(){
  	dispose_selectionWrapperList();
  	if(getFocData() instanceof FObject){
  		selectionWrapperList = new FocListWrapper_ForObjectSelection(this, (FObject) getFocData());
  	}else if(getFocData() instanceof FObjectField){
  		selectionWrapperList = new FocListWrapper_ForObjectSelection(this, (FObjectField) getFocData());
  	}
    return selectionWrapperList;
  }
	
  public void setFocProperty(IFocData objProperty, String captionFieldName){
  	focData = objProperty;
    if(objProperty != null){
      FObjectField objFld = null;
      if(objProperty instanceof FObjectField){
      	objFld = (FObjectField) objProperty;
      }else if(objProperty instanceof FObject){
      	objFld = (FObjectField) ((FObject)objProperty).getFocField();
      }
      
      if(objFld != null){          
      	if(objFld.getNullValueDisplayString() != null && !objFld.getNullValueDisplayString().isEmpty()){
      		setInputPrompt(objFld.getNullValueDisplayString());
      	}

      	// Preserver Filters - 2019-11-20
      	//The filters set by developers and other that the Formula expression and property equality filters
      	//Should be transported to the new wrapper list
      	ArrayList<Filter> filtersToPreserve = null;
      	if(selectionWrapperList != null) {
      		ArrayList<Filter> filterCollection = selectionWrapperList.getFilterArrayList(false);
      		if(filterCollection != null) {
      			for(int i=0; i<filterCollection.size(); i++) {
      				Filter filter = filterCollection.get(i);
      				if(    !(filter instanceof ListFilterUsingAFormulaExpression)
      						&& !(filter instanceof ListFilterUsingPropertyValue)) {
      					if(filtersToPreserve == null) filtersToPreserve = new ArrayList<Filter>();
      					filtersToPreserve.add(filter);
      				}
      			}
      		}
      	}
	      // -----------------
      	
	      FocListWrapper selectionWrapperList = newSelectionFocListWrapper();
	      
	      if(selectionWrapperList != null){
	      	// Preserver Filters - 2019-11-20
		      if(filtersToPreserve != null) {
		      	for(int i=0; i<filtersToPreserve.size(); i++) {
		      		selectionWrapperList.addContainerFilter(filtersToPreserve.get(i));
		      	}
		      }
		      // -----------------
	      	
	        if (captionFieldName == null) {
            int captionFieldID = objFld.getDisplayField();
            FField captionField = objFld.getFocDesc() != null ? objFld.getFocDesc().getFieldByID(captionFieldID) : null;
            captionFieldName = captionField != null ? captionField.getName() : null;
	        }
	        this.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	        this.setItemCaptionPropertyId(captionFieldName);
        	this.setContainerDataSource(selectionWrapperList);
	      }
      }
    }
		setItemIcon(getFocObjectRef_ForTheADDIcon(), FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_ADD));
		setItemIcon(getFocObjectRef_ForTheREFRESHIcon(), FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_REFRESH));
  }
  
  public FocListWrapper getListWrapper(){
  	return (FocListWrapper) getContainerDataSource();
  }
  	
  public IFocData getFocData() {
    return focData;
  }
	
	public FocObject getFocObject_FromGuiCombo(){
		FocObject obj = null;
		try{
			FocListWrapper listWrapper = (FocListWrapper) getContainerDataSource();
			obj = (FocObject) listWrapper.getItem(getValue());//searchByRealReferenceOnly(intReference);
		}catch(Exception e){
			Globals.logException(e);
		}
		return obj;
	}

  public boolean copyGuiToMemory() {
    if(focData instanceof FProperty){
    	boolean copy = true;
    	Object value = getValue();
    	if(    value instanceof Long
    			|| value instanceof Integer){
    		long longValue = value instanceof Long ? ((Long)value).longValue() : ((Integer)value).longValue();
    		copy = longValue != FocDataItem_ForComboBoxActions.REF_REFRESH && longValue != FocDataItem_ForComboBoxActions.REF_ADD;
    	}
    	if(copy){
    		((FProperty)focData).setValue(value);
    	}
    }
    return false;
  }
  
  public void copyMemoryToGui() {
    if(focData instanceof FProperty){
    	long ref = (long)((FProperty)focData).getValue();
    	if(ref == 0) {
    		setValue(null);
    	} else {
    		setValue(ref);
    	}
    }
  }
  
	public void applyAttributes(){
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
	}

  public void setFocData(IFocData focData) {
    this.focData = focData;
   	setFocProperty(focData, getCaptionProperty());
  }

	@Override
	public String getXMLType() {
		return null;
	}

	@Override
	public Field getFormField() {
		return this;
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
	public void setDelegate(FocXMLGuiComponentDelegate delegate) {
		this.delegate = delegate; 
	}

	@Override
	public FocXMLGuiComponentDelegate getDelegate() {
		return delegate;
	}
	
	@Override
  public String getValueString() {
  	String value = null;
  	try{
  		if(getValue() != null){
		  	long ref = (Long) getValue();
		    FocList list = getSelectionFocList();
		    if(list != null){
		    	FocObject obj = list.searchByReference(ref);
		    	FProperty prop = obj != null ? obj.getFocPropertyForPath(getItemCaptionPropertyId().toString()) : null;
		    	value = prop != null ? prop.getString() : null;
		    }
  		}
  	}catch(Exception e){
  		value = null;
  		Globals.logExceptionWithoutPopup(e);
  	}
    return value;
  }

  @Override
  public void setValueString(String value) {
  	FocListWrapper listWrapper = getListWrapper();
    if(listWrapper != null){int r=2;
    	FocObject obj = listWrapper.searchByPropertyValue(getItemCaptionPropertyId().toString(), value);
      if(obj == null){
    		select((long)0);
      }else{
    		select(obj.getReference().getLong());
      }
    }
  }
  
	private String getCaptionProperty(){
		String captionProp = null;
		Attributes att = getAttributes();
		if(att != null){
			captionProp = att.getValue(FXML.ATT_CAPTION_PROPERTY);
		}
		return captionProp;
	}

	@Override
	public void setDescription(String description) {
		super.setDescription(description);
	}
	
	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
//			disableChangeOfValue = !enabled;
		if(ConfigInfo.comboBoxShowDropDownEvenWhenDisabled() && !enabled){
			FocXMLLayout parentLayout = findAncestor(FocXMLLayout.class);
			if(parentLayout != null) parentLayout.addFieldToValueChangeListener(this);
//			setImmediate(true);
		}else super.setEnabled(enabled);
	}
	
	public FocObject addNewObject(){
		FocObject focObjToOpen = null;
		FocDesc desc = null;
		FocList list = getSelectionFocList();
		if(list != null){
			desc = list.getFocDesc();
			IAddClickSpecialHandler handler = XMLViewDictionary.getInstance().getAddClickSpecialHandler(desc.getStorageName());
			if(handler != null){
				handler.addClicked(getMainWindow(), this);
			}else{
				focObjToOpen = createNewItemAndOpenForm(true);
			}
		}
		return focObjToOpen;
	}
	
	public FocObject createNewItemAndOpenForm(boolean adaptViewKeyWhenObjectCreated){
		FocObject focObjToOpen = null;
		FocList list = getSelectionFocList();
		if(list != null){
			focObjToOpen = list.newEmptyItem();
			if(getSelectionWrapperList() != null){
				getSelectionWrapperList().adjustPropertiesForNewItemAccordingTofilter(focObjToOpen);
			}
				
			if(getiObjectSelectWindowListener() != null){
				getiObjectSelectWindowListener().beforeOpenForm(focObjToOpen);
			}
			openObjectDetailsPanel(focObjToOpen, adaptViewKeyWhenObjectCreated);
		}
		return focObjToOpen;
	}
	
	public void openObjectDetailsPanel(FocObject focObjToOpen, boolean adaptViewKeyWhenObjectCreated){
		XMLViewKey key = new XMLViewKey(focObjToOpen.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
		
		try{
			openedCentralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, focObjToOpen, true, true, adaptViewKeyWhenObjectCreated);
		}catch(Exception e){
			Globals.showNotification("View Open Error", "Could not open view", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
			Globals.logString("FVObjectComboBox.openObjectDetailsPanel");
			Globals.logExceptionWithoutPopup(e);//We already gave a more meaningful message to the user.
		}
		
		if(openedCentralPanel != null){
			FocXMLLayout.popupInDialog(openedCentralPanel);
//			getMainWindow().changeCentralPanelContent(openedCentralPanel, true);
			copyMemoryToGui();
			if(openedCentralPanel.getValidationLayout() != null){
				((FocXMLLayout) openedCentralPanel).getValidationLayout().addValidationListener(new IValidationListener() {
					
					@Override
					public void validationDiscard(FVValidationLayout validationLayout) {
					}
					
					@Override
					public boolean validationCheckData(FVValidationLayout validationLayout) {
						reloadList();		
						return false;
					}
		
					@Override
					public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
						FocObject newFocObject = null;
						
						IFocData focData = openedCentralPanel != null ? openedCentralPanel.getFocData() : null;
						if(focData != null){
							if(focData instanceof FocDataMap){
								focData = ((FocDataMap)focData).getMainFocData();
							}
							
							if(focData != null && focData instanceof FocObject){
								newFocObject = (FocObject) focData;
								if(newFocObject != null){
									long ref = newFocObject.getReference().getLong();
									
									if(getiObjectSelectWindowListener() != null){
										getiObjectSelectWindowListener().beforeSetValue(newFocObject);
									}
									
									select(ref);
								}
							}
						}
					}

					@Override
					public boolean validationCommit(FVValidationLayout validationLayout) {
						// TODO Auto-generated method stub
						return false;
					}
				});
			}
		}
	}
	
	public INavigationWindow getMainWindow(){
		return getWindow() != null ? getWindow() : findAncestor(FocCentralPanel.class);
	}

	public INavigationWindow getWindow(){
		return getDelegate() != null && getDelegate().getFocXMLLayout() != null ? getDelegate().getFocXMLLayout().getMainWindow() : null;
	}

	public FocListWrapper getSelectionWrapperList() {
		return selectionWrapperList;
	}
	
	@Override
	public void setValue(Object newValue) throws com.vaadin.data.Property.ReadOnlyException {
		super.setValue(newValue);
	}

	public IObjectSelectWindowListener getiObjectSelectWindowListener() {
		return iObjectSelectWindowListener;
	}

	public void setiObjectSelectWindowListener(IObjectSelectWindowListener iObjectSelectWindowListener) {
		this.iObjectSelectWindowListener = iObjectSelectWindowListener;
	}
}
