package com.foc.focDataSourceDB;

import com.foc.db.SQLFilter;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.desc.ReferenceChecker;

public class ReferenceChecker_VoidObject {
	
	private SQLFilter sqlFilter = null;
	
	public ReferenceChecker_VoidObject(){
		
	}
	
	public void dispose(){
		if(sqlFilter != null){
			sqlFilter.dispose();
			sqlFilter = null;	
		}
	}
	
	public SQLFilter getSqlFilter() {
		return sqlFilter;
	}

	public void setSqlFilter(SQLFilter sqlFilter) {
		this.sqlFilter = sqlFilter;
	}

	
	public static ReferenceChecker_VoidObject getReferenceChecker_VoidObject(ReferenceChecker refCheck){
		ReferenceChecker_VoidObject voidObj = null;
		if(refCheck != null){
			voidObj = (ReferenceChecker_VoidObject) refCheck.getVoidObject();
			if(voidObj == null){
				voidObj = new ReferenceChecker_VoidObject();
				refCheck.setVoidObject(voidObj);
			}
		}
		return voidObj;
	}
	
	public static SQLFilter getReferenceChecker_getSQLFilter(ReferenceChecker refCheck){
		ReferenceChecker_VoidObject voidObj 	= getReferenceChecker_VoidObject(refCheck);
		SQLFilter 									sqlFilter = voidObj != null ? voidObj.getSqlFilter() : null;
		if(sqlFilter == null){
	    FocConstructor constr         = new FocConstructor(refCheck.getFocDesc(), null, null);
	    FocObject 		 templateFocObj = constr.newItem();
	    if(templateFocObj != null){
	    	templateFocObj.setDbResident(false);
	    	sqlFilter = new SQLFilter(templateFocObj, SQLFilter.FILTER_ON_SELECTED);
	    	sqlFilter.addSelectedField(refCheck.getObjectFieldID());
	    	voidObj.setSqlFilter(sqlFilter);
	    }
		}
		return sqlFilter;
	}
}
