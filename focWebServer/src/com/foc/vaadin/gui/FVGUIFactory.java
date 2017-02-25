package com.foc.vaadin.gui;

import java.util.HashMap;

import com.fab.model.table.FieldDefinition;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.fields.FVBarCodeCreator;
import com.foc.vaadin.fields.FVBlobString;
import com.foc.vaadin.fields.FVBoolean;
import com.foc.vaadin.fields.FVDate;
import com.foc.vaadin.fields.FVDescFieldStringBased;
import com.foc.vaadin.fields.FVDouble;
import com.foc.vaadin.fields.FVEmail;
import com.foc.vaadin.fields.FVImage;
import com.foc.vaadin.fields.FVInteger;
import com.foc.vaadin.fields.FVMultipleChoice;
import com.foc.vaadin.fields.FVMultipleChoiceString;
import com.foc.vaadin.fields.FVObject;
import com.foc.vaadin.fields.FVPassword;
import com.foc.vaadin.fields.FVQrCodeCreator;
import com.foc.vaadin.fields.FVString;
import com.foc.vaadin.fields.FVTime;
import com.foc.vaadin.fields.FVXMLViewSelectorCreator;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.components.report.FVReportCreator;
import com.foc.vaadin.gui.manipulators.FVAbsoluteLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVButtonCreator;
import com.foc.vaadin.gui.manipulators.FVChartCreator;
import com.foc.vaadin.gui.manipulators.FVCustomLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVGridLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVHTMLLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVHorizontalLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVHorizontalSplitLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVHtmlTableCreator;
import com.foc.vaadin.gui.manipulators.FVIncludeXMLCreator;
import com.foc.vaadin.gui.manipulators.FVIncludeXMLForEachCreator;
import com.foc.vaadin.gui.manipulators.FVLabelCreator;
import com.foc.vaadin.gui.manipulators.FVLineCreator;
import com.foc.vaadin.gui.manipulators.FVMoreLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVNativeButtonCreator;
import com.foc.vaadin.gui.manipulators.FVPanelCreator;
import com.foc.vaadin.gui.manipulators.FVPivotLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVPivotTableCreator;
import com.foc.vaadin.gui.manipulators.FVTabLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVTableCreator;
import com.foc.vaadin.gui.manipulators.FVTableGridCreator;
import com.foc.vaadin.gui.manipulators.FVTreeGridCreator;
import com.foc.vaadin.gui.manipulators.FVTreeTableCreator;
import com.foc.vaadin.gui.manipulators.FVVerticalLayoutCreator;
import com.foc.vaadin.gui.manipulators.FVVerticalPanelCreator;
import com.foc.vaadin.gui.manipulators.FVVerticalSplitLayoutCreator;
import com.foc.vaadin.gui.xmlForm.FXML;

@SuppressWarnings("serial")
public class FVGUIFactory extends HashMap<String, FocXMLGuiComponentCreator> {
  private static FVGUIFactory instance = null;
  
  private FVGUIFactory() {
    put(FXML.TAG_HORIZONTAL_LAYOUT, new FVHorizontalLayoutCreator());
    put(FXML.TAG_VERTICAL_LAYOUT, new FVVerticalLayoutCreator());
    put(FXML.TAG_HTML_LAYOUT, new FVHTMLLayoutCreator());
    put(FXML.TAG_CUSTOM_LAYOUT, new FVCustomLayoutCreator());
    put(FXML.TAG_PIVOT_LAYOUT, new FVPivotLayoutCreator());
    put(FXML.TAG_MORE_LAYOUT, new FVMoreLayoutCreator());
    put(FXML.TAG_VERTICAL_PANEL, new FVVerticalPanelCreator());
    put(FXML.TAG_ABSOLUTE_LAYOUT, new FVAbsoluteLayoutCreator());
    put(FXML.TAG_GRID_LAYOUT, new FVGridLayoutCreator());
    put(FXML.TAG_TAB_LAYOUT, new FVTabLayoutCreator());
    put(FXML.TAG_BUTTON, new FVButtonCreator());
    put(FXML.TAG_NATIVE_BUTTON, new FVNativeButtonCreator());
    put(FXML.TAG_LABEL, new FVLabelCreator());
    put(FXML.TAG_TREE, new FVTreeTableCreator());
    put(FXML.TAG_TABLE, new FVTableCreator());
    put(FXML.TAG_TREE_GRID, new FVTreeGridCreator());
    put(FXML.TAG_TABLE_GRID, new FVTableGridCreator());
    put(FXML.TAG_HTML_TABLE, new FVHtmlTableCreator());
    put(FXML.TAG_PIVOT, new FVPivotTableCreator());
    put(FXML.TAG_QR_CODE, new FVQrCodeCreator());
    put(FXML.TAG_BAR_CODE, new FVBarCodeCreator());
    put(FXML.TAG_PANEL, new FVPanelCreator());
    put(FXML.TAG_LINE, new FVLineCreator());
    put(FXML.TAG_CHART, new FVChartCreator());
    put(FXML.TAG_REPORT, new FVReportCreator());
//    put(FXML.TAG_BLOB_DISPLAY, new FVBlobDisplayCreator());
    put(FXML.TAG_BLOB_DISPLAY, new FVImage());
    
    put(FXML.TAG_INTEGER_FIELD, new FVInteger());
    put(FXML.TAG_TEXT_FIELD, new FVString());
    put(FXML.TAG_EMAIL_FIELD, new FVEmail());
    put(FXML.TAG_DATE_FIELD, new FVDate());
    put(FXML.TAG_TIME_FIELD, new FVTime());
    put(FXML.TAG_OBJECT_SELECTOR_FIELD, new FVObject());
    put(FXML.TAG_MULTIPLE_CHOICE_FIELD, new FVMultipleChoice());
    put(FXML.TAG_MULTIPLE_CHOICE_FIELD_FOC_DESC, new FVDescFieldStringBased());
    put(FXML.TAG_MULTIPLE_CHOICE_STRING_BASED_FIELD_FOC_DESC, new FVMultipleChoiceString());
    put(FXML.TAG_XML_VIEW_SELECTOR, new FVXMLViewSelectorCreator());
    put(FXML.TAG_CHECK_BOX_FIELD, new FVBoolean());
    put(FXML.TAG_TEXT_AREA_FIELD, new FVBlobString());
    put(FXML.TAG_NUMERIC_FIELD, new FVDouble());
    put(FXML.TAG_IMAGE_FIELD, new FVImage());
    put(FXML.TAG_PASSWORD_FIELD, new FVPassword());
    put(FXML.TAG_INCLUDE_XML, new FVIncludeXMLCreator());
    put(FXML.TAG_INCLUDE_XML_FOR_EACH, new FVIncludeXMLForEachCreator());
    put(FXML.TAG_VERTICAL_SPLIT_LAYOUT, new FVVerticalSplitLayoutCreator());
    put(FXML.TAG_HORIZONTAL_SPLIT_LAYOUT, new FVHorizontalSplitLayoutCreator());
  }
  
	public String getKeyForProperty(IFocData focData) {
		String tag = null;

		FField field = null;
		if (focData instanceof FField) {
			field = (FField) focData;
		} else if (focData instanceof FProperty) {
			FProperty property = ((FProperty) focData);
			if (property != null && property.getAccessRight() == FocObject.PROPERTY_RIGHT_NONE) {
				tag = FXML.TAG_TEXT_FIELD;
			} else {
				field = ((FProperty) focData).getFocField();
			}
		}

		if (field != null) {
			switch (field.getFabType()) {
			case FieldDefinition.SQL_TYPE_ID_INT:
				tag = FXML.TAG_INTEGER_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_CHAR_FIELD:
				tag = FXML.TAG_TEXT_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_EMAIL_FIELD:
				tag = FXML.TAG_EMAIL_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_DATE:
				tag = FXML.TAG_DATE_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_TIME:
				tag = FXML.TAG_TIME_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_OBJECT_FIELD:
				tag = FXML.TAG_OBJECT_SELECTOR_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE:
				tag = FXML.TAG_MULTIPLE_CHOICE_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE_FOC_DESC:
				tag = FXML.TAG_MULTIPLE_CHOICE_FIELD_FOC_DESC;
				break;
			case FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE_STRING_BASED:
				tag = FXML.TAG_MULTIPLE_CHOICE_STRING_BASED_FIELD_FOC_DESC;
				break;
			case FieldDefinition.SQL_TYPE_ID_XML_VIEW_SELECTOR:
				tag = FXML.TAG_XML_VIEW_SELECTOR;
				break;
			case FieldDefinition.SQL_TYPE_ID_BOOLEAN:
				tag = FXML.TAG_CHECK_BOX_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_BLOB_STRING:
				tag = FXML.TAG_TEXT_AREA_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_DOUBLE:
				tag = FXML.TAG_NUMERIC_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_IMAGE:
				tag = FXML.TAG_IMAGE_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_PASSWORD:
				tag = FXML.TAG_PASSWORD_FIELD;
				break;
			case FieldDefinition.SQL_TYPE_ID_BLOB_FILE:
				tag = FXML.TAG_IMAGE_FIELD;
				break;
			}
		}
		return tag;
	}
  
  /*
  public FocXMLGuiComponentCreator get(int fieldSQLType){
  	String tag = null;
  	
  	switch(fieldSQLType){
  	case FieldDefinition.SQL_TYPE_ID_INT:
  		tag = FXML.TAG_INTEGER_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_CHAR_FIELD:
  		tag = FXML.TAG_TEXT_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_DATE:
  		tag = FXML.TAG_DATE_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_OBJECT_FIELD:
  		tag = FXML.TAG_OBJECT_SELECTOR_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE:
  		tag = FXML.TAG_MULTIPLE_CHOICE_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_BOOLEAN:
  		tag = FXML.TAG_CHECK_BOX_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_BLOB_STRING:
  		tag = FXML.TAG_TEXT_AREA_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_DOUBLE:
  		tag = FXML.TAG_NUMERIC_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_IMAGE:
  		tag = FXML.TAG_IMAGE_FIELD;
  		break;
  	case FieldDefinition.SQL_TYPE_ID_PASSWORD:
  		tag = FXML.TAG_PASSWORD_FIELD;
  		break;
  	}
  	
  	return tag != null ? get(tag) : null;
  }
  */
  
  public static FVGUIFactory getInstance() {
    if (instance == null)
      instance = new FVGUIFactory();
    
    return instance;
  }
}
