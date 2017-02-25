package com.fab.model;

import com.fab.model.table.TableDefinitionDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FIntField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class FocListDefinitionDesc extends FocDesc {
	public static final int FLD_FOCLIST_ID = 1;
	public static final int FLD_FOCLIST_TITLE = 2;
	public static final int FLD_TABLE_DEFINITION = 3;

	public static final String DB_TABLE_NAME = "FOCLIST_DEFINITION";
	
	/**
	 * 
	 */
	public FocListDefinitionDesc(){
		super(FocListDefinition.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);
		FField fld = addReferenceField();
		
		fld = new FIntField("FOCLIST_ID", "List id", FLD_FOCLIST_ID, false, 2);
		addField(fld);
		
		fld = new FStringField("FOCLIST_TITLE", "List title", FLD_FOCLIST_TITLE, false, 30);
		addField(fld);
		
		FObjectField objFld = new FObjectField("TABLE_DEFINITION", "Table definiton", FLD_TABLE_DEFINITION, false, TableDefinitionDesc.getInstance(), "TABLE_DEF_", this, TableDefinitionDesc.FLD_FOCLIST_LIST);
		objFld.setNullValueMode(FObjectField.NULL_VALUE_ALLOWED_AND_SHOWN);
		objFld.setComboBoxCellEditor(TableDefinitionDesc.FLD_NAME);
		objFld.setDisplayField(TableDefinitionDesc.FLD_NAME);
		objFld.setSelectionList(TableDefinitionDesc.getList(FocList.NONE));
		addField(objFld);
	}
	
	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE LIST
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
	
	public static FocList getList(int mode){
		return getInstance().getFocList(mode);
	}
	
	public FocList newFocList(){
		FocList list = super.newFocList();
		list.setDirectlyEditable(true);
		list.setDirectImpactOnDatabase(false);
		FocListOrder order = new FocListOrder(FLD_FOCLIST_ID);
		list.setListOrder(order);
		return list;		
	}
	
  //ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  public static FocDesc getInstance() {
  	return getInstance(DB_TABLE_NAME, FocListDefinitionDesc.class);
  }

}
