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
package com.foc.vaadin.gui;

import org.xml.sax.Attributes;

import com.foc.access.AccessSubject;
import com.foc.access.FocDataMap;
import com.foc.admin.UserSession;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

public class FocXMLGuiComponentDelegate {
  private FocXMLGuiComponent component       = null;
  private String             dataPath        = null;
  private IFocData           rootFocData     = null;
  private FocXMLGuiComponent parentComponent = null;
  private boolean            editable        = true;
  private String             nameInMap       = null;
  private boolean            suspended       = false;
  
  //These are used in case the component is located in a table.
  private Object columnId = null;
  private Object rowId    = null;

//  private UnitTestingRecorder_CommonField recorder = null;

	public FocXMLGuiComponentDelegate(FocXMLGuiComponent component){
    this.component = component;
    if(UserSession.getInstanceForThread() != null && UserSession.getInstanceForThread().getRightToLeft())
    	if(component instanceof Component) ((Component)component).addStyleName("foc-rtl-style");
  }
  
  public void dispose(){
//  	if(recorder != null) {
//  		recorder.dispose();
//  		recorder = null;
//  	}
    component   = null;
    rootFocData = null;
    parentComponent = null;
    columnId = null;
    rowId = null;
    dataPath = null;
  }
  
  public boolean hasNoRight(){
    if(getFocData() != null && getFocData() instanceof FProperty && ((FProperty)getFocData()).getAccessRight() == FocObject.PROPERTY_RIGHT_NONE){
    	return true;
    }
    return false;
  }
  
  public String getNameFromAttributes(){
  	String name = null;
  	if(component != null){
  		Attributes attributes = component.getAttributes();
  		name = attributes.getValue(FXML.ATT_NAME);  		
  	}
  	
  	return name;
  }
  
  public String getCaptionFromAttributes(){
  	String caption = null;
  	if(component != null){
  		Attributes attributes = component.getAttributes();
  		if(attributes != null){
  			caption = attributes.getValue(FXML.ATT_CAPTION);
  		}
  	}
  	
  	return caption;
  }
  
  public IFocData getFocData(){
  	return component != null ? component.getFocData() : null;
  }

  public FocXMLLayout getFocXMLLayout(){
    FocXMLLayout layout = null;
    if(component != null){
      Component cmp = (Component)component;
      while(cmp != null && layout == null){
        cmp = cmp.getParent();
        if(cmp instanceof FocXMLLayout){
          layout = (FocXMLLayout) cmp; 
        }
      }
    }
    return layout;
  }
  
  public boolean isConstructionMode(){
    FocXMLLayout layout = getFocXMLLayout();
    return layout != null ? layout.isConstructionMode() : false;
  }
  
  public void setDataPathWithRoot(IFocData rootFocData, String dataPath){
    setRootFocData(rootFocData);
    setDataPath(dataPath);
  }
  
  public FocObject getRootFocObject(){
  	FocObject focObject = null;
  	if(rootFocData instanceof FocObject){
  		focObject = (FocObject) rootFocData;
  	}else if(rootFocData instanceof FocDataMap){
  		IFocData mainFocData = ((FocDataMap) rootFocData).getMainFocData();
  		focObject = (FocObject) (mainFocData instanceof FocObject ? mainFocData : null); 
  	}
  	return focObject;
  }
  
  public void setEditable(boolean editable){
  	this.editable = editable;
  }
  
  public boolean isEditable(){
  	boolean editable = this.editable; 
  
    //Check if the Gui field in XML is set to notEditable
    if(editable){
      editable = isEditable_AttributeFlagCheckOnly();
    }
  	
  	//If the Component does not have a Field then it is a palette component and should always be Editable.
  	if(editable){//&& component != null && component.getFormField() != null
	  	
	  	//First we check if programmatically the FocXMLLayout has been set as not editable
  		FocXMLLayout focXMLayout = getFocXMLLayout(); 
	    editable = focXMLayout != null ? focXMLayout.isEditable() : true;
	    
	    //Check if the Property Value is Locked
	    //1- Either by flag
	    //2- Or by implementation of getPropertyAccessRight in FocObject
	  	if(editable){
		  	IFocData focData = getFocData();
		  	if(focData instanceof FProperty){
		  		FProperty property = (FProperty) focData;
		  		editable = property != null && !property.isValueLocked();
		  		if(editable && property != null && property.getFocObject() != null && property.getFocField() != null){
		  			editable = editable && property.getFocObject().getPropertyAccessRight(property.getFocField().getID()) == FocObject.PROPERTY_RIGHT_READ_WRITE;
		  		}
		  	}
	  	}
	  	
	  	if(rootFocData != null && dataPath != null){
		    if(editable){
		      String pathBeforeDot = null;
		      String pathAfterDot  = dataPath;
		      int    idxOfDot      = 0; 
		  
		      FocObject rootFocObject = getRootFocObject();
		    	if(rootFocObject != null){
		  			editable = rootFocObject.isEditable_AtomicNoDotLookup(dataPath);
		  		}
		      
		      while(editable && pathAfterDot != null){
			      idxOfDot = dataPath.indexOf(".", idxOfDot);
			      if(idxOfDot >= 0){
			        pathBeforeDot = dataPath.substring(0, idxOfDot);
			        pathAfterDot  = dataPath.substring(idxOfDot + 1, dataPath.length());
			        
			        if(pathBeforeDot != null){
				        IFocData focData_HalfWay = rootFocData.iFocData_getDataByPath(pathBeforeDot);

					      if(focData_HalfWay != null){
					      	FocObject focObject = null;
					      	if(focData_HalfWay instanceof FocObject){
					      		focObject = (FocObject) focData_HalfWay;
					      	}else if(focData_HalfWay instanceof FObject){
					      		focObject = (FocObject) ((FObject)focData_HalfWay).getObject();
					      	}
					      
					      	if(focObject != null){
					      		editable = focObject.isEditable_AtomicNoDotLookup(pathAfterDot);
					      	}
					      }
			        }
			      }else{
			      	pathAfterDot = null;
			      }
			      idxOfDot++;
		      }
		    }
	  	}
	  	
	  	//This case is when the Component is a Table, the FocData is then a FocList. And we can see if it is the direct slave of a locked object...
	  	//This does not manage the case where we have One2One relation like WBSPointer and WBS...
	  	//But it was written for the ManufacturingOrder Master to ManufacturingOrderOutput
	  	if(editable && rootFocData instanceof FocList){
	  		AccessSubject accessSubject = ((AccessSubject)rootFocData).getFatherSubject();
	  		if(accessSubject != null && accessSubject instanceof FocObject){
		  		FocObject rootFocObject = (FocObject) accessSubject; 
		  		String    dataPath      = ((FocList)rootFocData).getFocDesc().getFieldName_ForList();
		  		editable = rootFocObject.isEditable_AtomicNoDotLookup(dataPath);
	  		}
	  	}
    }
  	
    return editable;
  }
  
  private boolean isEditable_AttributeFlagCheckOnly(){
	  boolean    editable   = true;
	  Attributes attributes = component.getAttributes();
		if(attributes != null && attributes.getValue(FXML.ATT_EDITABLE) != null && attributes.getValue(FXML.ATT_EDITABLE).equals("false")) {
		  editable = false;
		}
		return editable;
  }
  
  public boolean isShowDescription(){  	
  	return showDescription_AttributeFlagCheckOnly();   
  }
  
	private boolean showDescription_AttributeFlagCheckOnly() {
		boolean showDesc = false;
		Attributes attributes = component.getAttributes();
		if (attributes != null && attributes.getValue(FXML.ATT_SHOWDESCRIPTION) != null && attributes.getValue(FXML.ATT_SHOWDESCRIPTION).equals("true")) {
			showDesc = true;
		}
		return showDesc;
	}
  
  public void setDataPath(String dataPath){
    this.dataPath = dataPath;
  }

  public String getDataPath(){
    return this.dataPath;
  }
  
  

  public IFocData getRootFocData() {
    return rootFocData;
  }

  public void setRootFocData(IFocData rootFocData) {
    this.rootFocData = rootFocData;
  }
  
  public IFocData refreshFocData(){
    IFocData data    = null;
    
    String   path    = getDataPath();
    IFocData focData = rootFocData;
    
    if(path != null && focData != null){
      if(path.equals(FXML.DATA_ROOT)){
        data = focData;
      }else{
        data = focData != null ? focData.iFocData_getDataByPath(path) : null;
      }
    }

    boolean backupReadOnly = false;
    Field fld = component.getFormField();
    if(fld != null){
    	backupReadOnly = fld.isReadOnly();
    	fld.setReadOnly(false);
    }
    boolean pause = FocUnitRecorder.pause();
    if(!isSuspended()) {
    	component.setFocData(data);
    }
    FocUnitRecorder.resume(pause);
    if(fld != null && backupReadOnly){
    	fld.setReadOnly(backupReadOnly);
    }
    return data;
  }

	public FocXMLGuiComponent getParentComponent() {
		return parentComponent;
	}

	public void setParentComponent(FocXMLGuiComponent parentComponent) {
		this.parentComponent = parentComponent;
	}
	
	
	public Object getColumnId() {
		return columnId;
	}

	public void setColumnId(Object columnId) {
		this.columnId = columnId;
	}
	
	public Object getRowId() {
		return rowId;
	}

	public void setRowId(Object rowId) {
		this.rowId = rowId;
	}

	public String getNameInMap() {
		return nameInMap;
	}

	public void setNameInMap(String nameInMap) {
		this.nameInMap = nameInMap;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public boolean setSuspended(boolean suspended) {
		boolean backup = this.suspended; 
		this.suspended = suspended;
		return backup;
	}

//  public UnitTestingRecorder_CommonField getRecorder() {
//		return recorder;
//	}
//
//	public void setRecorder(UnitTestingRecorder_CommonField recorder) {
//		this.recorder = recorder;
//	}
}
