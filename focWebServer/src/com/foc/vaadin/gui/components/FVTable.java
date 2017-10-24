package com.foc.vaadin.gui.components;

import java.text.Format;

import org.xml.sax.Attributes;

import com.foc.dataWrapper.FocDataWrapper;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FNumField;
import com.foc.list.FocList;
import com.foc.property.FInt;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVTable extends Table implements FocXMLGuiComponent, ITableTree{
  
  private FocListWrapper    focListWrapper         = null;   
  private TableTreeDelegate tableTreeDelegate      = null;
  private FocXMLGuiComponentDelegate delegate      = null;
  private boolean           focListWrapper_Owner   = false;
  
  public FVTable(Attributes attributes) {
  	getTableTreeDelegate(attributes);
  	delegate = new FocXMLGuiComponentDelegate(this);
    init(attributes);
//    addStyleName(ValoTheme.TABLE_COMPACT);
  }
    
  @Override
  public void dispose(){
    if(delegate != null){
      delegate.dispose();
      delegate = null;
    }
    if(focListWrapper != null){
    	if(focListWrapper_Owner) focListWrapper.dispose();
      focListWrapper = null;
    }
    focListWrapper_Owner = false;    
    if(tableTreeDelegate != null){
      tableTreeDelegate.dispose();
      tableTreeDelegate = null;
    }
  }

	private void init(Attributes attributes){
    getTableTreeDelegate().init();
  }
  
  public FocList getFocList(){
    return (focListWrapper != null) ? focListWrapper.getFocList() : null; 
  }
  
  public void applyFocListAsContainer(){
    //getDelegate().adjustGeneratedColumns();
  	getTableTreeDelegate().pushVisibleColumns();
  	getTableTreeDelegate().setContainerDataSource(focListWrapper);
//    setColumnCollapsed(focListWrapper, true);
    setAttributes(getAttributes());
    
  }
  
  @Override
  public Field getFormField() {
    return null;
  }
  
  @Override
  public Attributes getAttributes() {
    return getTableTreeDelegate().getAttributes();
  }
  
  @Override
  public FProperty getFocData() {
    return null;
  }
  
  @Override
  public String getXMLType() {
    return FXML.TAG_TABLE;
  }
  
  @Override
  protected String formatPropertyValue(Object rowId, Object colId, Property property) {
  	String ret = getTableTreeDelegate().formatPropertyValue(rowId, colId, property);
    return ret != null ? ret : super.formatPropertyValue(rowId, colId, property);
  }
  
  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {
  }

  public TableTreeDelegate getTableTreeDelegate() {
    return getTableTreeDelegate(null);
  }

  public TableTreeDelegate getTableTreeDelegate(Attributes attributes) {
    if(tableTreeDelegate == null){
      tableTreeDelegate = new TableTreeDelegate(this, attributes);
    }
    return tableTreeDelegate;
  }

  @Override
  public void setAttributes(Attributes attributes) {
    getTableTreeDelegate().setAttributes(attributes);

  }

  @Override
  public FVCheckBox getEditableCheckBox() {
    return getTableTreeDelegate().getEditableCheckBox();
  }
  
//  public FVTableColumn addColumn(String propertyID, String headerCaption) {
//    return getTableTreeDelegate().addColumn(propertyID, headerCaption);
//  }
  
//  @Override
//  public FVTableColumn addColumn(Attributes attributes) {
//    FVTableColumn tc = null;
//    try{
//      String name = attributes.getValue(FXML.ATT_NAME);
//      tc = addColumn(name, attributes.getValue(FXML.ATT_CAPTION));
//      
//      tc.setAttributes(attributes);
//      tableFooterFormula(attributes, name);
//      tableFooter_ComputeAll();
//    }catch(Exception e){
//      Globals.logException(e);
//    }
//    return tc;
////    FVCheckBox editableCheckBox = table.getEditableCheckBox();
////    if(editableCheckBox != null){
////      addComponent(editableCheckBox);
////    }
//  }
  
  @Override
  public FocDesc getFocDesc() {
    return getFocList() != null ? getFocList().getFocDesc() : null;
  }

	@Override
	public void open(FocObject focObject) {
	  getTableTreeDelegate().open(focObject);
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
    if(focData != null){
      if(focData instanceof FocListWrapper){
        this.focListWrapper = (FocListWrapper) focData;
        focListWrapper_Owner = false;
      }else if(focData instanceof FocList){
        FocList list = (FocList) focData;
        list.loadIfNotLoadedFromDB();
        this.focListWrapper = new FocListWrapper(list);
        focListWrapper_Owner = true;        
      }
      if(focListWrapper != null && getTableTreeDelegate() != null){
      	getTableTreeDelegate().adjustFocDataWrappers_FilterAndSroting();
      }
    }else{
      this.focListWrapper = null;
    }
  }

  @Override
  public String getValueString() {
    return null;
  }

  @Override
  public void setValueString(String value) {
  }

  @Override
  public void delete(long ref) {
    getFocList().removeItem(ref);
  }

  @Override
  public FVTableColumn addColumn(FocXMLAttributes attributes) {
    return getTableTreeDelegate().addColumn(attributes);
  }

	@Override
	public void computeFooter(FVTableColumn col) {
    String footerFormula = col.getFooterFormula();
    if(footerFormula != null){
      footerFormula = footerFormula.toUpperCase();
      if(footerFormula.equals("SUM")){
      	boolean isInteger = false;
        double value = 0;
        for(int i=0; i<getFocListWrapper().size(); i++){
          FocObject obj = getFocListWrapper().getAt(i);
          if(col.isColumnFormula()){
          	Object objValue = col.computeFormula_ForFocObject(obj);
          	try{
          		value += (Double)objValue; 
          	}catch(Exception e){
          		
          	}
          }else{
            IFocData focData = obj.iFocData_getDataByPath(col.getDataPath());
            if(focData instanceof FProperty){
              FProperty property = (FProperty)focData;
              if(property instanceof FInt) isInteger = true; 
              value += property != null ? property.getDouble() : 0;
            }
          }
        }
        Format format = isInteger ? FNumField.newNumberFormat(20, 0) : FNumField.newNumberFormat(20, 2);
        String strValue = value == 0 ? "" : format.format(value);
        FVTable.this.setColumnFooter(col.getName() , strValue);
      } else if(footerFormula.startsWith("PARENT.")){
        FocObject focObject = (FocObject) getFocList().getFatherSubject();
        String   path = footerFormula.substring(7);
        FProperty property = focObject.getFocPropertyForPath(path);
        FVTable.this.setColumnFooter(col.getName(), property.getString());
      }
    }
	}

  @Override
  public FocDataWrapper getFocDataWrapper() {
    return focListWrapper;
  }
  
  public FocListWrapper getFocListWrapper() {
    return (FocListWrapper) getFocDataWrapper();
  }
  
  @Override
  public void setItemIcon(Object itemId, Resource icon) {
  	super.setItemIcon(itemId, icon);
  	
  	if(icon != null){
  		getTableTreeDelegate().adjustColumnWidthForIcons();
  	}
  }
  
//	@Override
//	public void setEnabled(boolean enabled) {
//		getTableTreeDelegate().setEnabled(enabled);
//	}
  
	@Override
	public void refreshRowCache_Foc(){
		refreshRowCache();
	}

	@Override
	public void afterAddItem(FocObject fatherObject, FocObject newObject) {
	}
	
	public void refresh_CallContainerItemSetChangeEvent() {
		if(getTableTreeDelegate() != null) getTableTreeDelegate().refresh_CallContainerItemSetChangeEvent();
	}
	
	@Override
	public boolean setRefreshGuiDisabled(boolean disabled) {
		return getFocDataWrapper() != null ? getFocDataWrapper().setRefreshGuiDisabled(disabled) : false;
	}

	@Override
	public void refreshEditable() {
		if(getTableTreeDelegate() != null){
			getTableTreeDelegate().refreshEditable();
		}
	}
	
	@Override
	public void setEditable(boolean editable) {
		if(getDelegate() != null){
			getDelegate().setEditable(editable);
		}
		super.setEditable(editable);		
	}
	
	@Override
	public FocObject getSelectedObject() {
		return getTableTreeDelegate().getSelectedObject_ForVaadinTable();
	}

	@Override
	public void setSelectedObject(FocObject selectedObject) {
		if(getTableTreeDelegate() != null){
			getTableTreeDelegate().setSelectedObject_ForVaadinTable(selectedObject);
		}
	}
}