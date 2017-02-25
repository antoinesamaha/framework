package com.foc.business.division;

import com.foc.desc.field.FBoolField;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class DivisionList extends FocList{

	public DivisionList(boolean endDepartmentsOnly){
		super(new FocLinkSimple(DivisionDesc.getInstance()));
		if(endDepartmentsOnly){
			FBoolField bFld = (FBoolField) DivisionDesc.getInstance().getFieldByID(DivisionDesc.FLD_END_DIVISION);
			getFilter().putAdditionalWhere("END_DEP", bFld.getName()+"=1");
		}
	  setDirectlyEditable(false);
	  setDirectImpactOnDatabase(true);
	}
	
	private static DivisionList endDepartmentList = null;
	@Deprecated
	public static DivisionList getInstance_ForEndDepartments(){
		if(endDepartmentList == null){
			endDepartmentList = new DivisionList(true);
		}
		return endDepartmentList;
	}
}
