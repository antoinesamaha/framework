package com.foc.business.workflow.map;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class WFStageDesc extends FocDesc {
	public static final int FLD_NAME        = FField.FLD_NAME;
	public static final int FLD_DESCRIPTION = FField.FLD_DESCRIPTION;
	public static final int FLD_IS_APPROVAL = 1;
	
	public static final String DB_TABLE_NAME = "WF_STAGE";
  
	public WFStageDesc(){
		super(WFStage.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, true);
		setGuiBrowsePanelClass(WFStageGuiBrowsePanel.class);
		setGuiDetailsPanelClass(WFStageGuiDetailsPanel.class);
		addReferenceField();
		
		addNameField();
		addDescriptionField();
		
		FBoolField bFld = new FBoolField("IS_APPROVAL", "In Approval", FLD_IS_APPROVAL, false);
		addField(bFld);
  }
	
	public static FObjectField newFieldStage(String name, int fieldID){
		FObjectField objFld = new FObjectField(name, "Stage", fieldID, WFStageDesc.getInstance());
		objFld.setSelectionList(WFStageDesc.getList(FocList.NONE));
		objFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
		objFld.setComboBoxCellEditor(WFStageDesc.FLD_NAME);
		objFld.setDisplayField(WFStageDesc.FLD_NAME);
		return objFld;
	}
	
	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE LIST
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
	
	public static FocList getList(int mode){
    return getInstance().getFocList(mode);
  }
  
  @Override
  public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(false);
    list.setDirectImpactOnDatabase(true);
    if(list.getListOrder() == null){
      FocListOrder order = new FocListOrder(FLD_NAME);
      list.setListOrder(order);
    }
    return list;
  }
	
	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

	public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, WFStageDesc.class);    
  }
}
