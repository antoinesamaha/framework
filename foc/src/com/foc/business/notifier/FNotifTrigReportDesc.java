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
