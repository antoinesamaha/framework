package com.foc.desc.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.db.SQLJoin;
import com.foc.desc.FocDesc;
import com.foc.desc.FocFieldEnum;
import com.foc.desc.field.FBlobStringField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FReferenceField;
import com.foc.join.FocRequestField;
import com.foc.join.Join;
import com.foc.join.JoinUsingObjectField;
import com.foc.join.TableAlias;
import com.foc.util.Utils;

public class XMLJoin implements FXMLDesc {
	private int    order   = -1;
	private String alias   = null;
	private String table   = null;
	private String type    = null;
	private String on      = null;
	private String where   = null;
	
	public String getWhere() {
		return where;
	}

	private String otherAlias = null;
	private String otherFieldName = null;
	private String thisFieldName  = null;

	private XMLFocDescParser focDescParser = null;
	private ArrayList<XMLRequestField> array = null;
	
	private TableAlias tableAlias = null;
	
	public XMLJoin(XMLFocDescParser focDescParser, Attributes att){
		this.focDescParser = focDescParser;
  	table = XMLFocDescParser.getString(att, ATT_JOIN_TABLE);
  	alias = XMLFocDescParser.getString(att, ATT_JOIN_ALIAS);
  	on    = XMLFocDescParser.getString(att, ATT_JOIN_ON);
  	type  = XMLFocDescParser.getString(att, ATT_JOIN_TYPE);
  	where = XMLFocDescParser.getString(att, ATT_JOIN_WHERE);
  	
  	parseOn();
	}
	
	public XMLFocDesc getFocDesc(){
  	return focDescParser != null ? focDescParser.getXmlFocDesc() : null;
	}
	
	public FocDesc getThisFocDesc(){
		return Globals.getApp().getFocDescByName(table);
	}

	public FField getThisField(){
		FocDesc focDesc = getThisFocDesc();
		FField field = focDesc.getFieldByName(getThisFieldName());
		return field;
	}

	public FocDesc getOtherFocDesc(){
		XMLJoin otherJoin = focDescParser.getJoin(otherAlias);
		return otherJoin != null ? otherJoin.getThisFocDesc() : null;
	}

	public FField getOtherField(){
		FocDesc focDesc = getOtherFocDesc();
		FField field = focDesc.getFieldByName(getOtherFieldName());
		if(field == null){
			field = focDesc.getFieldByName(getOtherFieldName());
		}
		return field;
	}

	public TableAlias getTableAlias(){
		if(tableAlias == null){
			FocDesc focDesc = getThisFocDesc();
			tableAlias = new TableAlias(getAlias(), focDesc);
			
			if(!Utils.isStringEmpty(otherAlias) && !Utils.isStringEmpty(otherFieldName) && !Utils.isStringEmpty(thisFieldName)){
				XMLJoin otherJoin = focDescParser.getJoin(otherAlias);
				if(otherJoin != null && otherJoin.getTableAlias() != null){
					boolean fieldInSource = true;
					FField fld = getOtherField();
					if(fld instanceof FReferenceField){
						fld = getThisField();
						if(fld == null) fld = getThisField();
						fieldInSource = false;
					}
					if(fld != null){
					  Join join = new JoinUsingObjectField(otherJoin.getTableAlias(), fld.getID(), fieldInSource);
					  
					  if(type.equals("left")){
					  	join.setType(SQLJoin.JOIN_TYPE_LEFT);
					  }else if(type.equals("right")){
					  	join.setType(SQLJoin.JOIN_TYPE_RIGHT);
					  }
					  if(Utils.isStringEmpty(getWhere())){
					  	join.setAdditionalWhere(getWhere());
					  }
					  
					  tableAlias.addJoin(join);
					}else{
						if(getFocDesc() != null){
							Globals.logString("MODEL ERROR -------------------------");
							Globals.logString("MODEL ERROR : In Table "+getFocDesc().getStorageName()+" Reference field not found for table Alias:"+otherAlias+" for Table Name "+ otherJoin.getTable());
							Globals.logString("-------------------------------------");							
						}
					}
				}else{
					if(otherJoin == null){
						if(getFocDesc() != null){
							Globals.logString("MODEL ERROR -------------------------");
							Globals.logString("MODEL ERROR : In Table "+getFocDesc().getStorageName()+" Join not found for :"+otherAlias);
							Globals.logString("-------------------------------------");							
						}
					}
				}
			}

			for(int f=0; f<array.size(); f++){
				XMLRequestField xmlReqFld = array.get(f);
				
				if(xmlReqFld.getSourceFieldName().equals("*")){
					FocFieldEnum fieldEnum = new FocFieldEnum(getThisFocDesc(), FocFieldEnum.CAT_ALL, FocFieldEnum.LEVEL_PLAIN);
			    while(fieldEnum != null && fieldEnum.hasNext()){
			    	FField fld = (FField) fieldEnum.next();
			    	if(fld != null && fld.isDBResident() && !(fld instanceof FBlobStringField)){
//			    		int joinFieldID = fld.getID() == FField.REF_FIELD_ID ? FLD_VOUCHER_TRANSACTION_REF : fld.getID();  
			    		FocRequestField reqFld = new FocRequestField(getFocDesc().nextFldID(), tableAlias, fld.getID());
			    		if(getFocDesc() != null && getFocDesc().getFocRequestDesc() != null) getFocDesc().getFocRequestDesc().addField(reqFld);
//			    		focDescParser.getFocRequestDesc().addField(reqFld);
			    	}
			    }
				}else{
					if(getThisFocDesc() == null){
						Globals.showNotification("Could not find Table", "Table name : "+table, IFocEnvironment.TYPE_ERROR_MESSAGE);
					}else{
						FField fld = getThisFocDesc().getFieldByName(xmlReqFld.getSourceFieldName());
						if(fld == null){
							Globals.showNotification("Could not find Field", getThisFocDesc().getStorageName()+"."+xmlReqFld.getSourceFieldName(), IFocEnvironment.TYPE_ERROR_MESSAGE);
						}else{
							if(getFocDesc() != null && getFocDesc().getFocRequestDesc() != null){
								FocRequestField reqFld = new FocRequestField(getFocDesc().nextFldID(), tableAlias, fld.getID());
								getFocDesc().getFocRequestDesc().addField(reqFld);
							}
						}
					}
				}
			}
		}
		
		return tableAlias;
	}
	
	public void addRequestFied(XMLRequestField requestField){
		if(array == null){
			array = new ArrayList<XMLRequestField>();
		}
		array.add(requestField);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getSide() {
		return type;
	}

	public void setSide(String side) {
		this.type = side;
	}

	public String getOn() {
		return on;
	}

	public void parseOn() {
		if(!Utils.isStringEmpty(on)){
			int indexOfEqual = on.indexOf("=");
			if(indexOfEqual > 0){
				String before = on.substring(0, indexOfEqual);
				String after = on.substring(indexOfEqual+1, on.length());
			
				int indexOfDotBefore = before.indexOf(".");
				int indexOfDotAfter  = after.indexOf(".");
				
				if(indexOfDotBefore > 0 && indexOfDotAfter > 0){
					String beforeAlias = before.substring(0, indexOfDotBefore);
					String beforeField = before.substring(indexOfDotBefore+1, before.length());
					
					String afterAlias  = after.substring(0, indexOfDotAfter);
					String afterField  = after.substring(indexOfDotAfter+1, after.length());
					
					if(beforeAlias.equals(alias)){
						otherAlias = afterAlias;
						otherFieldName = afterField;
						thisFieldName  = beforeField;
					}else if(afterAlias.equals(alias)){
						otherAlias = beforeAlias;
						otherFieldName = beforeField;
						thisFieldName  = afterField;						
					}
				}
			}
		}
	}

	public String getOtherAlias() {
		return otherAlias;
	}

	public String getThisFieldName() {
		return thisFieldName;
	}

	public String getOtherFieldName() {
		return otherFieldName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
