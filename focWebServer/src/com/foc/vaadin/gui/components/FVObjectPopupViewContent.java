package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.property.FObject;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.web.dataModel.FocListWrapper_ForObjectSelection;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;

@SuppressWarnings("serial")
public class FVObjectPopupViewContent implements PopupView.Content{

//	private FVObjectComboBox objectComboBox = null;
	private FVObjectPopupView    popupView    = null;
	private FObject              objProperty  = null;
	private Attributes           attributes   = null;
	private FVTableWrapperLayout tableWrapper = null;
	private String               minimizedStringValue = null;
	
	public FVObjectPopupViewContent(FObject objProperty, Attributes attributes) {
		this.objProperty = objProperty;
		this.attributes  = attributes;
	}
	
	public void dispose(){
		dispose_component();
		objProperty    = null;
		attributes     = null;
		popupView      = null;
	}

	public void dispose_component(){
		if(tableWrapper != null){
			tableWrapper.dispose();
			tableWrapper = null;
		}
	}

	public IFocData getFocData(){
		return objProperty;
	}

	public void setFocData(IFocData focData){
		if(objProperty != focData){
			try{
				if(focData instanceof FObject){
					objProperty = (FObject) focData;
				}
				if(tableWrapper != null){
					dispose_component();
				}
			}catch(Exception e){
				Globals.logExceptionWithoutPopup(e);
			}
		}
	}

	public String getColumn1_DataPath(boolean priorityToCaptionProperty){
		String fieldName = getColumn_1_2_DataPath(priorityToCaptionProperty, FXML.ATT_COLUMN_1);
		return fieldName;
	}
	
	public String getColumn2_DataPath(boolean priorityToCaptionProperty){
		String fieldName = getColumn_1_2_DataPath(priorityToCaptionProperty, FXML.ATT_COLUMN_2);
		return fieldName;
	}
	
	private String getColumn_1_2_DataPath(boolean priorityToCaptionProperty, String columnAttName){
		String fieldName = null;
		if(attributes != null){
			if(priorityToCaptionProperty){
				fieldName = attributes.getValue(FXML.ATT_CAPTION_PROPERTY);
				if(fieldName == null || fieldName.isEmpty()){
					fieldName = attributes.getValue(columnAttName);
				}
			}else{
				fieldName = attributes.getValue(columnAttName);
				if(fieldName == null || fieldName.isEmpty()){
					fieldName = attributes.getValue(FXML.ATT_CAPTION_PROPERTY);
				}				
			}
		}
		if(fieldName == null || fieldName.isEmpty()){
			if(getObjectProperty() != null){
				FObjectField objFld = (FObjectField) getObjectProperty().getFocField();
				if(objFld != null){
		      int captionFieldID = objFld.getDisplayField();
		      FField captionField = objFld.getFocDesc() != null ? objFld.getFocDesc().getFieldByID(captionFieldID) : null;
		      fieldName = captionField != null ? captionField.getName() : null;
				}
			}
		}
		return fieldName;
	}
	
	/*public String getColumn1_DataPath(boolean priorityToCaptionProperty){
		String fieldName = null;
		if(attributes != null){
			if(priorityToCaptionProperty){
				fieldName = attributes.getValue(FXML.ATT_CAPTION_PROPERTY);
				if(fieldName == null || fieldName.isEmpty()){
					fieldName = attributes.getValue(FXML.ATT_COLUMN_1);
				}
			}else{
				fieldName = attributes.getValue(FXML.ATT_COLUMN_1);
				if(fieldName == null || fieldName.isEmpty()){
					fieldName = attributes.getValue(FXML.ATT_CAPTION_PROPERTY);
				}				
			}
		}
		if(fieldName == null || fieldName.isEmpty()){
			if(getObjectProperty() != null){
				FObjectField objFld = (FObjectField) getObjectProperty().getFocField();
				if(objFld != null){
		      int captionFieldID = objFld.getDisplayField();
		      FField captionField = objFld.getFocDesc() != null ? objFld.getFocDesc().getFieldByID(captionFieldID) : null;
		      fieldName = captionField != null ? captionField.getName() : null;
				}
			}
		}
		return fieldName;
	}*/
	
	@Override
	public String getMinimizedValueAsHTML() {
		minimizedStringValue = "";
//		if(objectComboBox != null && objectComboBox.getValueString() != null && !objectComboBox.getValueString().isEmpty()){
//			value = objectComboBox.getValueString(); 
//		}

		FObject objProp = (FObject) getFocData();
		if(objProp != null){
			FocObject valueObj = objProp.getObject_CreateIfNeeded();
			if(valueObj == null){
				String nullValue = objProp.getNullValueDisplayString();
				minimizedStringValue = nullValue == null || nullValue.isEmpty() ? minimizedStringValue : nullValue;
			}else{
				String fieldName = getColumn1_DataPath(true);
				if(fieldName != null){
					IFocData valueFocData = valueObj.iFocData_getDataByPath(fieldName);
					if(valueFocData != null){
						minimizedStringValue = (String) valueFocData.iFocData_getValue();
					}
				}
			}
		}

		String html = getPopupDorpdownHtmlImg(minimizedStringValue);
		return html;
	}
	
	@Override
	public Component getPopupComponent() {
		if(getPopupView() != null && getPopupView().getDelegate() != null){
			getPopupView().getDelegate().refreshFocData();
		}
		if(tableWrapper == null){
			FocListWrapper_ForObjectSelection listWrapper = new FocListWrapper_ForObjectSelection(null, objProperty);
			if(listWrapper != null){
				FVTable table = new FVTable(attributes);
				table.setSelectable(true);
				if(table.getTableTreeDelegate() != null){
					table.getTableTreeDelegate().setAddEnabled(false);
					table.getTableTreeDelegate().setOpenEnabled(false);
					table.getTableTreeDelegate().setDeleteEnabled(false);
				}
				
				table.addItemClickListener(new ItemClickListener() {
					@Override
					public void itemClick(ItemClickEvent event) {
		//				Integer intValue = (Integer) event.getItemId();
		//				int ref = intValue != null ? intValue.intValue() : 0;
		//				FocObject value = (objProperty != null && objProperty.getPropertySourceList() != null) ? objProperty.getPropertySourceList().searchByReference(ref) : null;
						FocObject value = event != null ? (FocObject) event.getItem() : null;
						if(getPopupView() != null){
							getPopupView().setPopupVisible(false);
							((Property)getPopupView()).setValue(value);
						}
					}
				});
				
				table.setFocData(listWrapper);
		    table.getTableTreeDelegate().fillButtonsAndPopupMenus();//Has to called after the setFocData so that we have the FocDesc for rights
		    //------------------------------------------------------------//
		    if(objProperty != null){
		    	String column1 = getColumn1_DataPath(false);
					String column2 = attributes.getValue(FXML.ATT_COLUMN_2);
					String column3 = attributes.getValue(FXML.ATT_COLUMN_3);
					
					if(column1 != null){			
						FocXMLAttributes columnAttributes = new FocXMLAttributes();
						columnAttributes.addAttribute(FXML.ATT_NAME, column1);
						columnAttributes.addAttribute(FXML.ATT_CAPTION, column1);
						table.getTableTreeDelegate().getTreeOrTable().addColumn((FocXMLAttributes) columnAttributes);
					}
		
					if(column2 != null){
						FocXMLAttributes columnAttributes = new FocXMLAttributes();
						columnAttributes.addAttribute(FXML.ATT_NAME, column2);
						columnAttributes.addAttribute(FXML.ATT_CAPTION, column2);
						table.getTableTreeDelegate().getTreeOrTable().addColumn((FocXMLAttributes) columnAttributes);
					}
					
					if(column3 != null){
						FocXMLAttributes columnAttributes = new FocXMLAttributes();
						columnAttributes.addAttribute(FXML.ATT_NAME, column3);
						columnAttributes.addAttribute(FXML.ATT_CAPTION, column3);
						table.getTableTreeDelegate().getTreeOrTable().addColumn((FocXMLAttributes) columnAttributes);
					}
					table.applyFocListAsContainer();
				}
		    
				/*    
				if(objectComboBox == null){
					objectComboBox = new FVObjectComboBox(objProperty, attributes);
			  	if(objectComboBox != null){
			  		objectComboBox.setAttributes(attributes);
			  		objectComboBox.setFocData(objProperty);
			  		objectComboBox.setFilteringMode(FilteringMode.CONTAINS);
			  	}	
				}
				*/
		    
		    tableWrapper = new FVTableWrapperLayout();
		    tableWrapper.addHeaderComponent_AsFirst(new Button("Empty", new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						if(getObjectProperty() != null) getObjectProperty().setObject(null);
//						FVObjectPopupViewContent.this.setFocData(null);
						getMinimizedValueAsHTML();
					}
				}));
		    tableWrapper.setTableOrTree(null, table);
		    tableWrapper.setHeight("400px");
				table.setWidth("100%");
				tableWrapper.setWidth("100%");
			}
		}
		
		return tableWrapper;
	}
	
	public Component getPopupComponent_WithoutCreation() {
		return tableWrapper;
	}

	public FObject getObjectProperty() {
		return objProperty;
	}

	public FVObjectPopupView getPopupView() {
		return popupView;
	}

	public void setPopupView(FVObjectPopupView popupView) {
		this.popupView = popupView;
	}
	
	public static String getPopupDorpdownHtmlImg(String strValue){
		/*
		URL url = null;
		UI app = FocWebApplication.getInstanceForThread();
		try{
			url = app.getPage().getLocation().toURL();
		}catch (MalformedURLException e){
			Globals.logException(e);
		}
		String path = url != null ? url.getProtocol()+"://"+url.getAuthority()+url.getPath() : null;
		String html = strValue+"<img src=\""+path+"VAADIN/themes/focVaadinTheme/icons/drop_down.png\" width=\"20\" height=\"20\" align=\"right\" style=\"float: right\"></img>";
		*/
		String html = strValue+"&nbsp;";//"&nbsp;&nbsp;&nbsp;&nbsp;.";
		return html;
	}
	
	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public String getMinimizedStringValue() {
		return minimizedStringValue;
	}
}
