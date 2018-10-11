package com.foc.business.workflow.implementation;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.shared.json.B01JsonBuilder;

public class JSONLog {
	private FocObject focObject = null;
	private String    json      = null;
	
	public JSONLog(FocObject object) {
		focObject = object;
		
		if(focObject.workflow_IsLoggable()){
			B01JsonBuilder builder = new B01JsonBuilder();
			try {
				builder.setModifiedOnly(true);
				builder.setPrintObjectNamesNotRefs(true);
				builder.setScanSubList(true);
				builder.setPrintRootRef(false);
				focObject.toJson(builder);
				json = builder.toString();
				if(json.length() >= WFLogDesc.LEN_FLD_CHANGES) {
					json = json.substring(0, WFLogDesc.LEN_FLD_CHANGES-1);
				}
			} catch(Exception e) {
				Globals.logException(e);
			}
			builder.dispose();
		}
	}
	
	public void dispose() {
		focObject = null;
		json      = null;
	}
	
	public String getJson() {
		return json;
	}

	public FocObject getFocObject() {
		return focObject;
	}
	
	public class SQLBucket {
		private ArrayList<String> sqlArray = null;
		
		public SQLBucket() {
			
		}
		
		public void addSQL(String sql) {
			if(sql != null) {
				if(sqlArray == null) sqlArray = new ArrayList<>();
				sqlArray.add(sql);
			}
		}
	}
}