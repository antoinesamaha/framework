package com.foc.business.department;

import com.foc.desc.field.FBoolField;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class DepartmentList extends FocList{
	
	public DepartmentList(boolean endDepartmentsOnly){
		super(new FocLinkSimple(DepartmentDesc.getInstance()));
		if(endDepartmentsOnly){
			FBoolField bFld = (FBoolField) DepartmentDesc.getInstance().getFieldByID(DepartmentDesc.FLD_END_DEPARTMENT);
			getFilter().putAdditionalWhere("END_DEP", bFld.getName()+"=1");
		}
	  setDirectlyEditable(false);
	  setDirectImpactOnDatabase(true);
	}
	
	private static DepartmentList endDepartmentList = null;
	@Deprecated
	public static DepartmentList getInstance_ForEndDepartments(){
		if(endDepartmentList == null){
			endDepartmentList = new DepartmentList(true);
		}
		return endDepartmentList;
	}
}
