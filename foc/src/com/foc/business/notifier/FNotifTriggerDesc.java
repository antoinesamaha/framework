package com.foc.business.notifier;

import com.foc.business.workflow.WorkflowTransactionFactory;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FMultipleChoiceStringField;
import com.foc.desc.parsers.pojo.PojoFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;
import com.foc.util.Utils;

public class FNotifTriggerDesc extends PojoFocDesc implements FocNotificationConst {
   
  public FNotifTriggerDesc(Class<PojoFocObject> focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique){
    super(focObjectClass, FocDesc.DB_RESIDENT, FNotifTrigger.DBNAME, false);
  }
  
  @Override
  protected void afterConstruction() {
    super.afterConstruction();
    
    FMultipleChoiceStringField fld = (FMultipleChoiceStringField) getFieldByName(FNotifTrigger.FIELD_Transaction);
    for(int i=0; i<WorkflowTransactionFactory.getInstance().getFocDescCount(); i++){
      IWorkflowDesc workflowDesc = WorkflowTransactionFactory.getInstance().getIWorkflowDesc(i);
      if(workflowDesc != null){
        fld.addChoice(workflowDesc.iWorkflow_getDBTitle());
      }
    }
    
    FDescFieldStringBased descField = (FDescFieldStringBased) getFieldByName(FNotifTrigger.FIELD_TABLE_NAME);
    descField.fillWithAllDeclaredFocDesc();
    
    descField = (FDescFieldStringBased) getFieldByName(FNotifTrigger.FIELD_ReportTableName);
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
  
  @Deprecated
  public static FocDesc getInstance() {
    return FNotifTrigger.getFocDesc();
  }
}
