package com.fab.model.filter;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class FilterDefinitionDesc extends FocDesc {
	public static final int FLD_TITLE = 1;
	public static final int FLD_BASE_FOC_DESC = 2;
	public static final int FLD_FILTER_FIELD_DEFINITION_LIST = 3;
	
	public static final String DB_TABLE_NAME = "FILTER_DEFINITION";
	
	public FilterDefinitionDesc(){
		super(FilterDefinition.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);
		setGuiBrowsePanelClass(FilterDefinitionGuiBrowsePanel.class);
		setGuiDetailsPanelClass(FilterDefinitionGuiDetailsPanel.class);
		FField fld = addReferenceField();
		
		fld = new FStringField("TITLE", "Title", FLD_TITLE, false, 50);
		addField(fld);
		
		fld = new FDescFieldStringBased("BASE_FOC_DESC", "Base foc desc", FLD_BASE_FOC_DESC, false);
		fld.setMandatory(true);
		fld.setLockValueAfterCreation(true);
		addField(fld);
	}
	
	protected void afterConstruction(){
		FDescFieldStringBased descFld = (FDescFieldStringBased)getFieldByID(FilterDefinitionDesc.FLD_BASE_FOC_DESC);
		if(descFld != null){
			descFld.fillWithAllDeclaredFocDesc();
		}
		//FilterDefinition.fillFDescFieldChoices(descFld);
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
    if(list.getListOrder() == null){
      FocListOrder order = new FocListOrder(FLD_TITLE);
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
    return getInstance(DB_TABLE_NAME, FilterDefinitionDesc.class);
  }

}
