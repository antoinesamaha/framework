package com.foc.business.notifier;

import com.foc.business.workflow.WorkflowTransactionFactory;
import com.foc.business.workflow.implementation.IWorkflowDesc;
import com.foc.business.workflow.map.WFTransactionConfigDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FMultipleChoiceStringField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;

public class FocNotificationEventConfiguratorDesc extends FocDesc implements FocNotificationConst {
   
  public static final String DB_TABLE_NAME = "NOTIF_EVENT_CONFIGURATOR";
  
  public FocNotificationEventConfiguratorDesc(){
    super(FocNotificationEventConfigurator.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);
    
    addReferenceField();
    
    FMultipleChoiceField fld = new FMultipleChoiceField("EVENT", "Event", FLD_EVENT, true, 2);
    fld.addChoice(EVT_CREATE_USER_FROM_CONTACT, "User Creation From Contact");
    fld.addChoice(EVT_TABLE_ADD, "Table Insert");
    fld.addChoice(EVT_TABLE_DELETE, "Table Delete");
    fld.addChoice(EVT_TABLE_UPDATE, "Table Update");
    addField(fld);
    
    FMultipleChoiceStringField sFld = new FMultipleChoiceStringField("TRANSACTION", "Transaction", FLD_TRANSACTION, true, WFTransactionConfigDesc.LEN_TRANSACTION_NAME);
    sFld.setAllwaysLocked(true);
    addField(sFld);
    
    FObjectField oFld = new FObjectField(FocNotificationEmailTemplateDesc.DB_TABLE_NAME, "Template", FLD_TEMPLATE_OBJECT, FocNotificationEmailTemplateDesc.getInstance());
    FocList focList = FocNotificationEmailTemplateDesc.getInstance().getFocList(FocList.NONE);
    oFld.setSelectionList(focList);
    addField(oFld);
    
    FField ffld = new FDescFieldStringBased("TABLE_NAME", "Table", FLD_TABLE_NAME, false);
    ffld.setLockValueAfterCreation(true);
    addField(ffld);    
  }
  
  @Override
  protected void afterConstruction() {
    super.afterConstruction();
    FMultipleChoiceStringField fld = (FMultipleChoiceStringField) getFieldByID(FLD_TRANSACTION);
    for(int i=0; i<WorkflowTransactionFactory.getInstance().getFocDescCount(); i++){
      IWorkflowDesc workflowDesc = WorkflowTransactionFactory.getInstance().getIWorkflowDesc(i);
      if(workflowDesc != null){
        fld.addChoice(workflowDesc.iWorkflow_getDBTitle());
      }
    }
    
    FDescFieldStringBased descField = (FDescFieldStringBased) getFieldByID(FLD_TABLE_NAME);
    descField.fillWithAllDeclaredFocDesc();
  }
  
  public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, FocNotificationEventConfiguratorDesc.class);
  }
}
