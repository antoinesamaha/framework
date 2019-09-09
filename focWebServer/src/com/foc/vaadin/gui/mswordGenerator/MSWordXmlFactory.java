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
package com.foc.vaadin.gui.mswordGenerator;

import java.util.HashMap;

import com.fab.model.table.FieldDefinition;
import com.foc.desc.field.FField;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.gui.xmlForm.FXML;

@SuppressWarnings("serial")
public class MSWordXmlFactory extends HashMap<String, String> {
  private static MSWordXmlFactory instance = null;
  
  private MSWordXmlFactory() {
    put(FXML.TAG_HORIZONTAL_LAYOUT, FocMSWordHorizontalLayout.class.getName());
    put(FXML.TAG_VERTICAL_LAYOUT, FocMSWordVerticalLayout.class.getName());
//    put(FXML.TAG_MORE_LAYOUT, new FVMoreLayoutCreator());
//    put(FXML.TAG_VERTICAL_PANEL, new FVVerticalPanelCreator());
//    put(FXML.TAG_ABSOLUTE_LAYOUT, new FVAbsoluteLayoutCreator());
//    put(FXML.TAG_GRID_LAYOUT, new FVGridLayoutCreator());
//    put(FXML.TAG_TAB_LAYOUT, new FVTabLayoutCreator());
//    put(FXML.TAG_BUTTON, new FVButtonCreator());
    put(FXML.TAG_LABEL, FocMSWordLabel.class.getName());
//    put(FXML.TAG_TREE, new FVTreeTableCreator());
    put(FXML.TAG_TABLE, FocMSWordTable.class.getName());
    put(FXML.TAG_TABLE_COLUMN, FocMSWordTableColumn.class.getName());
//    put(FXML.TAG_PIVOT, new FVPivotTableCreator());
//    put(FXML.TAG_PANEL, new FVPanelCreator());
//    put(FXML.TAG_LINE, new FVLineCreator());
//    put(FXML.TAG_CHART, new FVChartCreator());
//    put(FXML.TAG_BLOB_DISPLAY, new FVBlobDisplayCreator());
//    
//    put(FXML.TAG_INTEGER_FIELD, new FVInteger());
//    put(FXML.TAG_TEXT_FIELD, new FVString());
//    put(FXML.TAG_EMAIL_FIELD, new FVEmail());
//    put(FXML.TAG_DATE_FIELD, new FVDate());
//    put(FXML.TAG_OBJECT_SELECTOR_FIELD, new FVObject());
//    put(FXML.TAG_MULTIPLE_CHOICE_FIELD, new FVMultipleChoice());
//    put(FXML.TAG_MULTIPLE_CHOICE_FIELD_FOC_DESC, new FVDescFieldStringBased());
//    put(FXML.TAG_MULTIPLE_CHOICE_STRING_BASED_FIELD_FOC_DESC, new FVMultipleChoiceStringBased());
//    put(FXML.TAG_CHECK_BOX_FIELD, new FVBoolean());
//    put(FXML.TAG_TEXT_AREA_FIELD, new FVBlobString());
//    put(FXML.TAG_NUMERIC_FIELD, new FVDouble());
//    put(FXML.TAG_IMAGE_FIELD, new FVImage());
//    put(FXML.TAG_PASSWORD_FIELD, new FVPassword());
//    put(FXML.TAG_INCLUDE_XML, new FVIncludeXMLCreator());
    put(FXML.TAG_FIELD, FocMSWordLabel.class.getName());
    put(FXML.TAG_INCLUDE_XML, FocMSWordIncludeXML.class.getName());
//    put(FXML.TAG_INCLUDE_XML_FOR_EACH, new FVIncludeXMLForEachCreator());
//    put(FXML.TAG_VERTICAL_SPLIT_LAYOUT, new FVVerticalSplitLayoutCreator());
//    put(FXML.TAG_HORIZONTAL_SPLIT_LAYOUT, new FVHorizontalSplitLayoutCreator());
  }
  
  public String getKeyForProperty(IFocData focData){
    String tag = null;
    
    FField field = null;
    if(focData instanceof FField){
      field = (FField) focData;
    }else if(focData instanceof FProperty){
      field = ((FProperty)focData).getFocField();
    }
  	
  	if(field != null){
	  	switch(field.getFabType()){
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
  
  public static MSWordXmlFactory getInstance() {
    if (instance == null)
      instance = new MSWordXmlFactory();
    
    return instance;
  }
}
