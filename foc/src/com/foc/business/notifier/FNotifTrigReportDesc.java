package com.foc.business.notifier;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.parsers.pojo.PojoFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;
import com.foc.util.Utils;

public class FNotifTrigReportDesc extends PojoFocDesc implements FocNotificationConst {
   
  public FNotifTrigReportDesc(Class<PojoFocObject> focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique){
    super(focObjectClass, FocDesc.DB_RESIDENT, FNotifTrigReport.DBNAME, false);
  }
  
  @Override
  protected void afterConstruction() {
    super.afterConstruction();
    
    FDescFieldStringBased descField = (FDescFieldStringBased) getFieldByName(FNotifTrigReport.FIELD_ReportTableName);
    descField.setTableNameFilter(new FDescFieldStringBased.ITableNameFilter() {
			@Override
			public boolean includeFocDesc(FocDesc focDesc) {
				boolean included = false;
				if(!Utils.isStringEmpty(focDesc.getReportContext())) {
					included = true;
				}
				return included;
			}
			
			@Override
			public void dispose() {
			}
		});
    descField.fillWithAllDeclaredFocDesc();
  }
}
