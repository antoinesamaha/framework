package com.foc.desc.pojo.fields;

import java.lang.reflect.Field;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.annotations.model.fields.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.util.Utils;

public class FTypeObject extends FocFieldTypAbstract<FocObject> {

	@Override
	public String getTypeName() {
		return TYPE_OBJECT;
	}

	@Override
	public FField newFField(Class focObjClass, Field f, FocObject a) {
  	boolean cascade          = a.cascade();
  	boolean detach           = a.detach();
  	boolean directlyEditable = !a.saveOnebyOne();
  	FObjectField focField = new FObjectField(getDBFieldName(f), getFieldTitle(f), FField.NO_FIELD_ID, null);
  	focField.setFocDescStorageName(a.table(), cascade, directlyEditable);
  	focField.setWithList(a.cachedList());
  	if(detach && !cascade){
  		focField.setReferenceChecker_PutToZeroWhenReferenceDeleted(true);
  	}else if(detach && cascade){
  		Globals.showNotification("Incompatible attributes", "Table: "+"???"+" Field: "+getDBFieldName(f)+" cannot have both CASCADE and DETACH", IFocEnvironment.TYPE_WARNING_MESSAGE);
  	}
  	
  	String forcedDBName = a.dbName();
  	if(!Utils.isStringEmpty(forcedDBName)) focField.setForcedDBName(forcedDBName);
  	
  	boolean nullAllowed = a.allowNull();
  	if(!nullAllowed){
  		focField.setNullValueMode(FObjectField.NULL_VALUE_NOT_ALLOWED);
  	}

  	String filterProperty = a.listFilterProperty();
  	
  	if(!Utils.isStringEmpty(filterProperty)){
  		focField.setSelectionFilter_PropertyDataPath(filterProperty);
  	}

  	String filterValue = a.listFilterValue();
  	if(!Utils.isStringEmpty(filterValue)){
  		focField.setSelectionFilter_Propertyvalue(filterValue);
  	}
		
		return focField;
	}

}
