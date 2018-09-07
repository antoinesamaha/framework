package com.foc.business.workflow.implementation;

import java.util.ArrayList;
import java.util.Stack;

import com.foc.FocThreadLocal;

public class SQLLogCumulator {
	
	private Stack<SQLBucket> bucketStack = null;

	public SQLLogCumulator() {
		bucketStack = new Stack<SQLBucket>();
	}
	
	public void dispose() {
		if(bucketStack != null) {
			bucketStack.clear();
			bucketStack = null;
		}
	}
	
	public static SQLLogCumulator getInstanceForThread(){
		return (SQLLogCumulator) FocThreadLocal.getSQLLogCumulator();
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
