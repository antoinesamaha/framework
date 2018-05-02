package com.foc.business.workflow.implementation;

import com.foc.admin.FocUser;
import com.foc.admin.FocUserDesc;
import com.foc.business.workflow.WFTitleDesc;
import com.foc.business.workflow.WorkflowTransactionFactory;
import com.foc.business.workflow.map.WFStageDesc;
import com.foc.business.workflow.signing.WFTransactionWrapperDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FBlobStringField;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FDateTimeField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FObjectField;
import com.foc.desc.field.FReferenceField;
import com.foc.list.FocList;

public class WFLogDesc extends FocDesc {
	public static final int FLD_MASTER           = 1;
	public static final int FLD_USER             = 2;
	public static final int FLD_DATE_TIME        = 3;
	public static final int FLD_TITLE            = 4;
	public static final int FLD_PREVIOUS_STAGE   = 5;
	public static final int FLD_TARGET_STAGE     = 6;
	public static final int FLD_EVENT_UNDONE     = 7;
	public static final int FLD_EVENT_TYPE       = 8;
	public static final int FLD_ON_BEHALF_OF     = 9;
	public static final int FLD_DESCERIPTION     = FField.FLD_DESCRIPTION;
	public static final int FLD_COMMENT          = 10;
	public static final int FLD_SIGNED_TRANSACTION_XML = 11;
	
	public static final int EVENT_NONE            = 0;
	public static final int EVENT_SIGNATURE       = 1;
	public static final int EVENT_CREATION        = 2;
	public static final int EVENT_CANCELLATION    = 3;
	public static final int EVENT_MODIFICATION    = 4;
	public static final int EVENT_APPROVED        = 5;
	public static final int EVENT_CLOSED          = 6;
	public static final int EVENT_UNDO_SIGNATURE  = 7;
	public static final int EVENT_CUSTOM          = 8;
	public static final int EVENT_COMMENT         = 9;
	public static final int EVENT_REJECT          = 10;
	
	public static final int LEN_FLD_COMMENT      = 400;
	
	public static final String WF_LOG_VIEW_KEY = "WF_LOG";
	
	public WFLogDesc(IWorkflowDesc iWFDesc){
		this(iWFDesc, ((FocDesc) iWFDesc).getStorageName());
	}
		
	public WFLogDesc(IWorkflowDesc iWFDesc, String dbTableSuffix){
		super(WFLog.class, FocDesc.DB_RESIDENT, getStorageName_ForTransactionStorageName(dbTableSuffix), false);
		setGuiBrowsePanelClass(WFLogGuiBrowsePanel.class);
		addReferenceField();
		
		WorkflowTransactionFactory.getInstance().add(iWFDesc);
		
		addDescriptionField();
		
		FocDesc masterFocDesc = (FocDesc) iWFDesc;
		
		FObjectField objFld = new FObjectField("WF_MASTER", "MASTER", FLD_MASTER, false, masterFocDesc, "MASTER_", this, iWFDesc.iWorkflow_getWorkflowDesc().getFieldID_LogList());
		objFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
		objFld.setDisplayField(FField.REF_FIELD_ID);
		objFld.setComboBoxCellEditor(FField.REF_FIELD_ID);
		objFld.setWithList(false);
		FReferenceField refFld = (FReferenceField) masterFocDesc.getFieldByID(FField.REF_FIELD_ID);
		if(refFld != null){
			if(refFld.getName() != FField.REF_FIELD_NAME){
				objFld.setForcedDBName("MASTER_"+refFld.getName());
			}
			addField(objFld);
		}else{
			int debug = 3;
			debug++;
		}
		
		FMultipleChoiceField mFld = new FMultipleChoiceField("EVENT_TYPE", "Event Type", FLD_EVENT_TYPE, false, 2);
		mFld.addChoice(EVENT_NONE, "none");
		mFld.addChoice(EVENT_SIGNATURE, "Signature");
		mFld.addChoice(EVENT_CANCELLATION, "Cancellation");
		mFld.addChoice(EVENT_MODIFICATION, "Modification");
		mFld.addChoice(EVENT_CREATION, "Creation");
		mFld.addChoice(EVENT_APPROVED, "Approval");
		mFld.addChoice(EVENT_CLOSED, "Closure");
		mFld.addChoice(EVENT_UNDO_SIGNATURE, "Undo Signature");
		mFld.addChoice(EVENT_CUSTOM, "Custom");
		mFld.addChoice(EVENT_COMMENT, "Comment");
		mFld.addChoice(EVENT_REJECT, "Reject");
		addField(mFld);

    FDateTimeField dateTimeFld = new FDateTimeField("DATE_TIME", "Date|Time", FLD_DATE_TIME, false);
    dateTimeFld.setMandatory(true);    
    dateTimeFld.setTimeRelevant(true);
    dateTimeFld.setAllwaysLocked(true);
    addField(dateTimeFld);
		
    FObjectField fObjectFld = new FObjectField("USER", "User", FLD_USER, false, FocUser.getFocDesc(), "USER_");
    fObjectFld.setComboBoxCellEditor(FocUserDesc.FLD_NAME);
    fObjectFld.setDisplayField(FocUserDesc.FLD_NAME);
    fObjectFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
    fObjectFld.setMandatory(false);
//    fObjectFld.setAllowLoadListFromFocDesc(false);
    fObjectFld.setSelectionList(FocUserDesc.getList(FocList.NONE));
    fObjectFld.setAllwaysLocked(true);
    addField(fObjectFld);
		
		objFld = new FObjectField("TITLE", "Title", FLD_TITLE, false, WFTitleDesc.getInstance(), "TITLE_");
		objFld.setSelectionList(WFTitleDesc.getList(FocList.NONE));
		objFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
		objFld.setComboBoxCellEditor(WFTitleDesc.FLD_NAME);
		objFld.setDisplayField(WFTitleDesc.FLD_NAME);
		addField(objFld);

		objFld = WFStageDesc.newFieldStage("TARGET_STAGE", FLD_TARGET_STAGE);
		addField(objFld);
		
		objFld = WFStageDesc.newFieldStage("PREVIOUS_STAGE", FLD_PREVIOUS_STAGE);
		addField(objFld);

		addField(WFTransactionWrapperDesc.newOnBehalfOfField(FLD_ON_BEHALF_OF));
		
		/*
		objFld = new FObjectField("TARGET_STAGE", "Target|Stage", FLD_TARGET_STAGE, false, WFStageDesc.getInstance(), "TARGET_STAGE_");
		objFld.setSelectionList(WFStageDesc.getList(FocList.NONE));
		objFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
		objFld.setComboBoxCellEditor(WFStageDesc.FLD_NAME);
		objFld.setDisplayField(WFStageDesc.FLD_NAME);

		objFld = new FObjectField("PREVIOUS_STAGE", "Previous|Stage", FLD_PREVIOUS_STAGE, false, WFStageDesc.getInstance(), "PREVIOUS_STAGE_");
		objFld.setSelectionList(WFStageDesc.getList(FocList.NONE));
		objFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
		objFld.setComboBoxCellEditor(WFStageDesc.FLD_NAME);
		objFld.setDisplayField(WFStageDesc.FLD_NAME);
		addField(objFld);
		*/
		
		FBoolField bFld = new FBoolField("EVENT_UNDONE", "Event Undone", FLD_EVENT_UNDONE, false);
		addField(bFld);
		
		FStringField chfld = new FStringField("COMMENT", "Comment", FLD_COMMENT, false, LEN_FLD_COMMENT);
    addField(chfld);

    FBlobStringField focFld = new FBlobStringField("SIGNED_TRANSACTION_XML", "Signed Transaction XML", FLD_SIGNED_TRANSACTION_XML, false, 300, 5);
    addField(focFld);
  }
	
	public static String getStorageName_ForWorkflowDesc(IWorkflowDesc iWFDesc){
		return getStorageName_ForTransactionStorageName(((FocDesc) iWFDesc).getStorageName());
	}
	
	public static String getStorageName_ForTransactionStorageName(String transStorageName){
		return "WF_LOG_"+transStorageName;
	}
}
