package com.foc.db;

public class SQLJoin {
  private String newAlias     = null ;//New alias to point to this new table
  private String newTableName = null;//New table name
  private int    type         = JOIN_TYPE_NONE;
  private String prevAlias    = null ;
  private String sourceField  = "";
  private String targetField  = "";
  
  private String additionalWhere = null;
  private int    provider        = DBManager.PROVIDER_MYSQL;
  
  public static String MAIN_TABLE_ALIAS = "M";

  public static final int JOIN_TYPE_NONE  = 0;
  public static final int JOIN_TYPE_LEFT  = 1;
  public static final int JOIN_TYPE_RIGHT = 2;
  
  public SQLJoin(int provider, String newTableName, String prevAlias, String sourceField, String targetField){
  	this.provider     = provider;
    this.newTableName = newTableName;
    this.prevAlias    = prevAlias;
    this.sourceField  = sourceField;
    this.targetField  = targetField;
  }

  public String getKey() {
    return prevAlias+"|"+sourceField+"|"+newTableName+"|"+targetField;
  }
  
  public String getLinkCondition() {
  	if(provider == DBManager.PROVIDER_ORACLE || provider == DBManager.PROVIDER_H2){
  		return prevAlias+".\""+sourceField+"\"="+newAlias+".\""+targetField+"\"";
  	}else{
  		return prevAlias+"."+sourceField+"="+newAlias+"."+targetField;
  	}
  }

  public String getSQLString(){
  	StringBuffer str = new StringBuffer();
  	switch(type){
  	case JOIN_TYPE_RIGHT:
  		str.append(" RIGHT JOIN ");
  		break;
  	case JOIN_TYPE_LEFT:
  		str.append(" LEFT JOIN ");  		
  		break;
  	case JOIN_TYPE_NONE:
  		str.append(" JOIN ");
  		break;
  	}
  	
  	str.append(getNewTableName());
  	str.append(' ');
  	str.append(getNewAlias());
  	str.append(" ON ");
  	str.append(getLinkCondition());
  	if(getAdditionalWhere() != null){
  		str.append(" AND (");
  		str.append(getAdditionalWhere());
  		str.append(")");
  	}
  	return str.toString();
  }
  
  public String getNewAlias() {
    return newAlias;
  }

  public void setNewAlias(String newAlias) {
    this.newAlias = newAlias;
  }

  public String getNewTableName() {
    return newTableName;
  }

  public String getPrevAlias() {
    return prevAlias;
  }

  public void setPrevAlias(String prevAlias) {
    this.prevAlias = prevAlias;
  }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAdditionalWhere() {
		return additionalWhere;
	}

	public void setAdditionalWhere(String additionalWhere) {
		this.additionalWhere = additionalWhere;
	}
}
