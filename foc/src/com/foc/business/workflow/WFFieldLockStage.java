package com.foc.business.workflow;

import com.foc.business.workflow.map.WFStage;
import com.foc.business.workflow.map.WFTransactionConfig;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.property.FMultipleChoiceString;

@SuppressWarnings("serial")
public class WFFieldLockStage extends FocObject{

	public WFFieldLockStage(FocConstructor constr){
		super(constr);
		newFocProperties();
	}
	
	public WFTransactionConfig getWFTransactionConfig(){
		return (WFTransactionConfig) getPropertyObject(WFFieldLockStageDesc.FLD_WF_TRANSACTION_CONFIG);
	}
	
	public void setWFTransactionConfig(WFTransactionConfig wfTransactionConfig){
		setPropertyObject(WFFieldLockStageDesc.FLD_WF_TRANSACTION_CONFIG, wfTransactionConfig);
	}
	
	public String getFieldName(){
		return (String) getPropertyString(WFFieldLockStageDesc.FLD_FIELD_NAME);
	}
	
	public void setFieldName(String fieldName){
		setPropertyString(WFFieldLockStageDesc.FLD_FIELD_NAME, fieldName);
	}
	
	public WFStage getWFStage(){
		return (WFStage) getPropertyObject(WFFieldLockStageDesc.FLD_WF_LOCK_STAGE);
	}
	
	public void setWFStage(WFStage wfStage){
		setPropertyObject(WFFieldLockStageDesc.FLD_WF_LOCK_STAGE, wfStage);
	}
	
	public void fillChoicesForStagesProperty(){
		FMultipleChoiceString property = (FMultipleChoiceString) getFocProperty(WFFieldLockStageDesc.FLD_FIELD_NAME);
		if(property != null){
			property.resetLocalSourceList();
			WFTransactionConfig transactionConfig = getWFTransactionConfig();
			FocDesc       desc              = (FocDesc) transactionConfig.getWorkflowDesc();
			if(desc != null){
				for(int i=0; i<desc.getFieldsSize(); i++){
					FField fld = desc.getFieldAt(i);
					property.addLocalChoice(fld.getName());	
				}
			}
		}
	}
}
