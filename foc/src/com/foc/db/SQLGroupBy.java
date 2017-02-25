/*
 * Created on Aug 2009 By Antoine SAMAHA
 */
package com.foc.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 01Barmaja
 */
public class SQLGroupBy {
	private ArrayList<String>                     arrayOfAtomicExpressions = null;
	private String                                groupByExpression        = null;
	private HashMap<Integer, GroupByFieldFormula> fieldsInFormulas         = null;
	
	public SQLGroupBy(){
		fieldsInFormulas = new HashMap<Integer, GroupByFieldFormula>();
	}
	
	public void dispose(){
		if(fieldsInFormulas != null){
			Iterator<GroupByFieldFormula> iter = fieldsInFormulas.values().iterator();
			while(iter != null && iter.hasNext()){
				GroupByFieldFormula frml = iter.next();
				frml.dispose();
			}
			fieldsInFormulas.clear();
		}
		groupByExpression = null;
		if(arrayOfAtomicExpressions != null){
			arrayOfAtomicExpressions.clear();
			arrayOfAtomicExpressions = null;
		}
	}

	public ArrayList<String> getArrayOfAtomicExpressions(boolean create){
		if(arrayOfAtomicExpressions == null && create){
			arrayOfAtomicExpressions = new ArrayList<String>(); 
		}
		return arrayOfAtomicExpressions;
	}
	
	public void addAtomicExpression(String atomic){
		getArrayOfAtomicExpressions(true).add(atomic);
	}
	
	private void buildExpressionFromAtomicArrayOfExpressions(){
		ArrayList<String> arr = getArrayOfAtomicExpressions(false);
		
		StringBuffer buff = new StringBuffer();
		if(arr.size() == 1){
			buff.append(arr.get(0));
		}else if(arr.size() > 1){
			buff.append("concat(");
			for(int i=0; i<arr.size(); i++){
				if(i>0) buff.append(",'|',");
				buff.append(arr.get(i));
			}
			buff.append(")");
		}
		setGroupByExpression(buff.toString());
	}
	
	public void addField_Formulas(int fieldName, String formulaFullExpression){
		GroupByFieldFormula groupByFormula = new GroupByFieldFormula(formulaFullExpression); 
		fieldsInFormulas.put(fieldName, groupByFormula);
	}
	
	public void addField_Formulas(int fieldName, String formulaPartBefore, String formulaPartAfter){
		GroupByFieldFormula groupByFormula = new GroupByFieldFormula(formulaPartBefore, formulaPartAfter); 
		fieldsInFormulas.put(fieldName, groupByFormula);
	}
	
	/*
	public void addField_Formulas(String alias, FocDesc focDesc, int fieldID, String formulaPartBefore, String formulaPartAfter){
		FField fld = focDesc.getFieldByID(fieldID);
		
		addField_Formulas(alias+"."+fld.getName(), formulaPartBefore, formulaPartAfter);
	}
	*/

	public String getGroupByExpression() {
		if(groupByExpression == null || groupByExpression.isEmpty()){
			buildExpressionFromAtomicArrayOfExpressions();
		}
		return groupByExpression;
	}

	public void setGroupByExpression(String groupByExpression) {
		this.groupByExpression = groupByExpression;
	}

	public String addFormulaToFieldName(int key, String str){
		String ret = str;
		GroupByFieldFormula formula = fieldsInFormulas.get(key);
		if(formula != null){
			ret = formula.composeFormulaForFieldName(str);
		}
		return ret;
	}
	
	private class GroupByFieldFormula {
		private String     formulaBefore         = null;
		private String     formulaAfter          = null;
		private String     formulaFullExpression = null;
		
		private GroupByFieldFormula(String formulaFullExpression){
			this.formulaFullExpression = formulaFullExpression;
		}
		
		private GroupByFieldFormula(String formulaBefore, String formulaAfter){
			this.formulaBefore = formulaBefore;
			this.formulaAfter  = formulaAfter;
		}
		
		public void dispose(){
			formulaBefore         = null;
			formulaAfter          = null;
			formulaFullExpression = null;
		}

		public String composeFormulaForFieldName(String str){
			String ret = null;
			if(formulaFullExpression != null){
				ret = formulaFullExpression;
			}else{
				ret = formulaBefore + str + formulaAfter;
			}
			return ret;
		}
	}

	/*
	private ArrayList<FFieldPath>          fieldsInGroupBy  = null;
	private ArrayList<GroupByFieldFormula> fieldsInFormulas = null;

	public SQLGroupBy(){
    fieldsInGroupBy  = new ArrayList<FFieldPath>();
		fieldsInFormulas = new ArrayList<GroupByFieldFormula>();
	}

	public void dispose(){
		if(fieldsInGroupBy != null){
			for(int i=0; i<fieldsInGroupBy.size(); i++){
				fieldsInGroupBy.get(i).dispose();
			}
			fieldsInGroupBy.clear();
			fieldsInGroupBy = null;
		}
		if(fieldsInFormulas != null){
			for(int i=0; i<fieldsInFormulas.size(); i++){
				fieldsInFormulas.get(i).dispose();
			}
			fieldsInFormulas.clear();
			fieldsInFormulas = null;
		}
	}
	
	public void addField_GroupBy(FFieldPath path){
		fieldsInGroupBy.add(path);
	}

	public void addField_Formulas(FFieldPath path, String formulaPartBefore, String formulaPartAfter){
		GroupByFieldFormula groupByFormula = new GroupByFieldFormula(path, formulaPartBefore, formulaPartAfter); 
		fieldsInFormulas.add(groupByFormula);
	}
	
	public GroupByFieldFormula getField_Formula(FFieldPath path){
		
	}
	
	public String buidGroupByExpression(){
		String exp = null; 
		if(fieldsInGroupBy.size() > 0){
			StringBuffer buff = new StringBuffer("GROUP BY (");
			buff.append(str);
			buff.append(")");
		}
		return exp;
	}
	
	private class GroupByFieldFormula {
		private FFieldPath fieldPath     = null;
		private String     formulaBefore = null;
		private String     formulaAfter  = null;
		
		private GroupByFieldFormula(FFieldPath fieldPath, String formulaBefore, String formulaAfter){
			this.formulaBefore = formulaBefore;
			this.formulaAfter  = formulaAfter;
			this.fieldPath     = fieldPath;
		}
		
		public void dispose(){
			formulaBefore = null;
			formulaAfter  = null;
			if(fieldPath != null){
				fieldPath.dispose();
				fieldPath = null;
			}
		}
	}
	*/
}