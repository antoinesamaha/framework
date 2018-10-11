package com.foc.business.notifier;

import com.foc.Globals;
import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocForeignEntity;
import com.foc.annotations.model.fields.FocMultipleChoiceString;
import com.foc.annotations.model.fields.FocReference;
import com.foc.annotations.model.fields.FocTableName;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;
import com.foc.list.FocList;
import com.foc.property.FObject;
import com.foc.util.Utils;

@FocEntity
@SuppressWarnings("serial")
public class FNotifTrigReport extends PojoFocObject implements FocNotificationConst {
    
  public static final String DBNAME = "FNotifTrigReport";

	@FocForeignEntity(mandatory = true, table = "FNotifTrigger", cascade = true, cachedList = true, saveOnebyOne = true)
	public static final String FIELD_FNotifTrigger = "FNotifTrigger";
	
	@FocTableName(size = 200)
	public static final String FIELD_ReportTableName = "ReportTableName";

	@FocForeignEntity(dbResident=false)
	public static final String FIELD_ReportConfiguration = "ReportConfiguration";

	@FocReference()
	public static final String FIELD_ReportReference = "ReportReference";
	
	@FocMultipleChoiceString(size = 200)
	public static final String FIELD_ReportLayout = "ReportLayout";

  public FNotifTrigReport(FocConstructor constr){
    super(constr);
  }
  
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}

  @Override
  public FocList getObjectPropertySelectionList(int fieldID) {
  	FocList list = null;
  	FField field = getThisFocDesc() != null ? getThisFocDesc().getFieldByName(FIELD_ReportConfiguration) : null;
  	if(field != null && field.getID() == fieldID) {
  		FocDesc focDesc = getReportConfigFocDesc();
			if(focDesc != null) {
				list = focDesc.getFocList(FocList.LOAD_IF_NEEDED);
			}
  	}else{
  		list = super.getObjectPropertySelectionList(fieldID);
  	}
  	return list;
  }

	public FocDesc getReportConfigFocDesc() {
		FocDesc focDesc = null;
		String storageName = getReportTableName();
		if(!Utils.isStringEmpty(storageName)) {
			focDesc = Globals.getApp().getFocDescByName(storageName);
		}
		return focDesc;
	}
		
	public String getReportTableName() {
		return getPropertyString(FIELD_ReportTableName);
	}

	public void setReportTableName(String value) {
		setPropertyString(FIELD_ReportTableName, value);
	}

	public long getReportReference() {
		return getPropertyLong(FIELD_ReportReference);
	}

	public void setReportReference(long value) {
		setPropertyLong(FIELD_ReportReference, value);
	}

	public String getReportLayout() {
		return getPropertyString(FIELD_ReportLayout);
	}

	public void setReportLayout(String value) {
		setPropertyString(FIELD_ReportLayout, value);
	}

	public FocObject getReportConfiguration() {
		return getPropertyObject(FIELD_ReportConfiguration);
	}

	public void setReportConfiguration(FocObject value) {
		setPropertyObject(FIELD_ReportConfiguration, value);
	}

	public void setReportConfigurationRef(long value) {
		FObject objProp = (FObject) getFocPropertyByName(FIELD_ReportConfiguration);
		objProp.setLocalReferenceInt_WithoutNotification(value);
	}
	
	public long getReportConfigurationRef() {
		FObject objProp = (FObject) getFocPropertyByName(FIELD_ReportConfiguration);
		return objProp.getLocalReferenceInt();
	}

	public void copyReportConfig_Ref2Object() {
		setReportConfigurationRef(getReportReference()); 
	}
	
	public void copyReportConfig_Object2Ref() {
		setReportReference(getReportConfigurationRef());
	}
	
}
