package com.foc.desc.xml;

import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.desc.field.FBoolField;
import com.foc.desc.field.FCloudStorageField;
import com.foc.desc.field.FCompanyField;
import com.foc.desc.field.FDateField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FIntField;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FMultipleChoiceStringField;
import com.foc.desc.field.FNumField;
import com.foc.desc.field.FObjectField;
import com.foc.desc.field.FReferenceField;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FTimeField;
import com.foc.list.FocListGroupBy;
import com.foc.list.filter.FocListFilter;
import com.foc.util.Utils;

public class XMLFocDescParser extends DefaultHandler implements FXMLDesc{
	private FocDescDeclaration_XMLBased declaration = null;
	
	private XMLFocDesc               xmlFocDesc     = null;
	private FField                   lastFieldAdded = null;
	private HashMap<String, XMLJoin> joinMap        = null;//Will remain null if not a join table
	private XMLJoin                  join           = null;
	private XMLFilter                xmlFilter      = null;//Will remain null if not a Filter

	private FocListGroupBy           groupBy        = null;
	
	public XMLFocDescParser(FocDescDeclaration_XMLBased declaration){
		this.declaration = declaration;
//		this.xmlFocDesc = focDesc;
	}
	
	public void dispose(){
		declaration    = null;
		xmlFocDesc     = null;
		lastFieldAdded = null;
	}
	
  public void startElement(String uri, String localName, String qName, Attributes att) {
    if (qName.equals(TAG_TABLE)) {
    	String forcedStorageName = getString(att, ATT_STORAGE_NAME);
    	xmlFocDesc = declaration.newFocDesc(forcedStorageName);
    	xmlFocDesc.setFocDescParser(this);
    	
    	if(getBoolean(att, ATT_WORKFLOW)) xmlFocDesc.initWorkflow();
    	if(getBoolean(att, ATT_TREE)) xmlFocDesc.setWithObjectTree();
    	boolean dbRes = getBoolean(att, ATT_DB_RESIDENT, true);
    	xmlFocDesc.setDbResident(dbRes);
    	boolean cached = getBoolean(att, ATT_CACHED, true);
    	xmlFocDesc.setListInCache(cached);
    	
    	String dbSourceKey = getString(att, ATT_DB_SOURCE);
    	if(!Utils.isStringEmpty(dbSourceKey)){
    		xmlFocDesc.setDbSourceKey(dbSourceKey);
    	}
    	
    	boolean inTableEditable = getBoolean(att, ATT_IN_TABLE_EDITABLE, false);
    	xmlFocDesc.setListInTableEditable(inTableEditable);
    	
    	boolean allowAdaptDataModel = getBoolean(att, ATT_ALLOW_ADAPT_DATA_MODEL, true);
    	xmlFocDesc.setAllowAdaptDataModel(allowAdaptDataModel);
    	
    } else if(qName.equals(TAG_FILTER)) {
    	String onTableName = getString(att, ATT_FILTER_ON_TABLE);
    	if(onTableName != null){
    		xmlFilter = new XMLFilter(onTableName);
    	}
    	String filterLevelStr = getString(att, ATT_FILTER_LEVEL);
    	if(filterLevelStr == null) filterLevelStr = FXMLDesc.VAL_FILTER_LEVEL_DATABASE;
    	if(filterLevelStr.equals(FXMLDesc.VAL_FILTER_LEVEL_DATABASE)){
    		xmlFilter.setFilterLevel(FocListFilter.LEVEL_DATABASE);	
    	}else if(filterLevelStr.equals(FXMLDesc.VAL_FILTER_LEVEL_MEMORY)){
    		xmlFilter.setFilterLevel(FocListFilter.LEVEL_MEMORY);
    	}
    	
    } else if(qName.equals(TAG_FILTER_CONDITION)){
    	if(xmlFilter != null){
    		xmlFilter.addCondition(att);
    	}
    } else if(qName.equals(TAG_JOINS)) {
    } else if(qName.equals(TAG_JOIN)) {
    	join = new XMLJoin(this, att);
    	if(joinMap == null) joinMap = new HashMap<String, XMLJoin>();
    	join.setOrder(joinMap.size());
    	joinMap.put(join.getAlias(), join);
    } else if(qName.equals(TAG_JOIN_FIELD)) {
    	if(joinMap != null && joinMap.size() > 0){
    		XMLRequestField requestField = new XMLRequestField(att);
    		join.addRequestFied(requestField);
    	}
    } else if(qName.equals(TAG_GROUP_BY)){
    } else if(qName.equals(TAG_GROUP_FIELD)){
    	FocListGroupBy groupBy = getGroupBy(true);
    	if(groupBy != null){
	    	String fieldName = getString(att, ATT_NAME);
	    	groupBy.addAtomicExpression(fieldName);
    	}    	
    } else if(qName.equals(TAG_CHOICE)) {
    	if(lastFieldAdded != null && lastFieldAdded instanceof FMultipleChoiceField){
    		try{
	    		int id = getInt(att, ATT_ID);
	    		String caption = getString(att, ATT_CAPTION);
	    		((FMultipleChoiceField) lastFieldAdded).addChoice(id, caption);
    		}catch(Exception e){
    			Globals.logException(e);
    		}
    	}else if(lastFieldAdded != null && lastFieldAdded instanceof FMultipleChoiceStringField){
    		try{
	    		String caption = getString(att, ATT_CAPTION);
	    		((FMultipleChoiceStringField) lastFieldAdded).addChoice(caption);
    		}catch(Exception e){
    			Globals.logException(e);
    		}
    	}
    } else {
    	FField fld = addField(uri, localName, qName, att);
    	if(fld != null){
    		lastFieldAdded = fld;
    		String groupByFormula = getString(att, ATT_GROUP_BY_FORMULA);
    		if(!Utils.isStringEmpty(groupByFormula)){
        	FocListGroupBy groupBy = getGroupBy(true);
        	groupBy.addField_FormulaSingleText(fld.getID(), fld.getDBName(), groupByFormula);
    		}
    	}
    }
  }

  public void endElement(String uri, String localName, String qName) {
  }

  public FField addField(String uri, String localName, String qName, Attributes att) {
  	FField fld = null;
  	if (qName.equals(TAG_REF)) {
  		FReferenceField refFld = (FReferenceField) xmlFocDesc.addReferenceField();
  		String refName = getName(att);
  		if(!Utils.isStringEmpty(refName)){
  			refFld.setName(refName);
  		}
  		fld = refFld;
  	} else if (qName.equals(TAG_COMPANY)){
  		fld = new FCompanyField(true, true);
  		xmlFocDesc.addField(fld);
    } else if (qName.equals(TAG_CODE)) {
    	fld = xmlFocDesc.addCodeField();
    	int size = getSize(att);
    	if(size > 0){
    		fld.setSize(size);
    	}    	
    } else if (qName.equals(TAG_EXTERNAL_CODE)) {
    	fld = xmlFocDesc.addExternalCodeField();
    	int size = getSize(att);
    	if(size > 0){
    		fld.setSize(size);
    	}    	
    } else if (qName.equals(TAG_NAME)) {
    	fld = xmlFocDesc.addNameField();
    	int size = getSize(att);
    	if(size > 0){
    		fld.setSize(size);
    	}
    	String key = att.getValue(ATT_KEY);
    	if(key != null){
    		fld.setKey(isKey(att));
    	}
    } else if (qName.equals(TAG_DESCRIPTION)) {
    	fld = xmlFocDesc.addDescriptionField();    	
    } else if (qName.equals(TAG_DATE)) {
    	fld = xmlFocDesc.addDateField();
    } else if (qName.equals(TAG_ORDER)) {
    	fld = xmlFocDesc.addOrderField();
    } else if (qName.equals(TAG_NOT_COMPLETED)){
    	fld = xmlFocDesc.addNotCompletedField();
    } else if (qName.equals(TAG_IS_SYSTEM)){
    	fld = xmlFocDesc.addIsSystemObjectField();
    } else if (qName.equals(TAG_TREE)){
    	FObjectField oFld = xmlFocDesc.setWithObjectTree();  
    	String forcedDBName = getString(att, ATT_FORCED_DB_NAME);
    	if(forcedDBName != null) oFld.setForcedDBName(forcedDBName);
    	fld = oFld;
    	
    } else if (qName.equals(TAG_STRING)) {
    	fld = new FStringField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att), getSize(att));
    	xmlFocDesc.addField(fld);
    } else if (qName.equals(TAG_BOOLEAN)) {
    	fld = new FBoolField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att));
    	xmlFocDesc.addField(fld);    	
    } else if (qName.equals(TAG_INTEGER)) {
    	boolean grouping = getBoolean(att, ATT_GROUPING, true);
    	fld = new FIntField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att), getSize(att), grouping);
    	((FIntField)fld).setDisplayZeroValues(false);
    	xmlFocDesc.addField(fld);    	
    } else if (qName.equals(TAG_DATE_FIELD)) {
    	fld = new FDateField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att));
    	xmlFocDesc.addField(fld);
    } else if (qName.equals(TAG_TIME_FIELD)) {
    	fld = new FTimeField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att));
    	xmlFocDesc.addField(fld);    	
    } else if (qName.equals(TAG_MULTIPLE_CHOICE)) {
    	fld = new FMultipleChoiceField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att), 2);
    	boolean sort = getBoolean(att, ATT_SORT_ITEMS, true);
    	((FMultipleChoiceField)fld).setSortItems(sort);
    	xmlFocDesc.addField(fld);
    } else if (qName.equals(TAG_MULTIPLE_CHOICE_STRING)) {
    	fld = new FMultipleChoiceStringField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att), getSize(att));
    	String sameCol = getString(att, ATT_SAME_COLUMN);
    	if(sameCol == null || sameCol.trim().toLowerCase().equals("true")){
    		((FMultipleChoiceStringField)fld).setChoicesAreFromSameColumn(xmlFocDesc);
    	}
    	xmlFocDesc.addField(fld);
    } else if (qName.equals(TAG_DOUBLE)) {
    	boolean grouping = getBoolean(att, ATT_GROUPING, true);
    	fld = new FNumField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), isKey(att), getSize(att), getDecimal(att), grouping);
    	((FNumField)fld).setDisplayZeroValues(false);
    	xmlFocDesc.addField(fld);
    } else if (qName.equals(TAG_BLOB)) {
    	int blobID         = xmlFocDesc.nextFldID();
    	int blobFileNameID = xmlFocDesc.nextFldID();
    	fld = new FCloudStorageField(getName(att), getTitle(att), blobID, false, blobFileNameID);
  		xmlFocDesc.addField(fld);
  		
  		fld = new FStringField(getName(att)+"_FILENAME", getTitle(att)+" File Name", blobFileNameID, false, 300);
  		xmlFocDesc.addField(fld);
    } else if (qName.equals(TAG_OBJECT)) {
    	boolean cascade          = getBoolean(att, ATT_CASCADE);
    	boolean detach           = getBoolean(att, ATT_DETACH);
    	boolean directlyEditable = !getBoolean(att, ATT_SAVE_ONE_BY_ONE, false);
    	FObjectField oFld = new FObjectField(getName(att), getTitle(att), xmlFocDesc.nextFldID(), null);
    	oFld.setFocDescStorageName(getString(att, ATT_TABLE), cascade, directlyEditable);
    	oFld.setWithList(getBoolean(att, ATT_CACHED_LIST, true));
    	if(detach && !cascade){
    		oFld.setReferenceChecker_PutToZeroWhenReferenceDeleted(true);
    	}else if(detach && cascade){
    		Globals.showNotification("Incompatible attributes", "Table: "+xmlFocDesc.getStorageName()+" Field: "+oFld.getName()+" cannot have both CASCADE and DETACH", IFocEnvironment.TYPE_WARNING_MESSAGE);
    	}
    	
    	String forcedDBName = getString(att, ATT_FORCED_DB_NAME);
    	if(forcedDBName != null) oFld.setForcedDBName(forcedDBName);
    	
    	boolean nullAllowed = getBoolean(att, ATT_NULL_VALUES_ALLOWED, true);
    	if(!nullAllowed){
    		oFld.setNullValueMode(FObjectField.NULL_VALUE_NOT_ALLOWED);
    	}

    	String filterProperty = getString(att, ATT_LIST_FILTER_PROPERTY);
    	if(filterProperty != null){
    		oFld.setSelectionFilter_PropertyDataPath(filterProperty);
    	}

    	String filterValue = getString(att, ATT_LIST_FILTER_VALUE);
    	if(filterValue != null){
    		oFld.setSelectionFilter_Propertyvalue(filterValue);
    	}
    	
    	xmlFocDesc.addField(oFld);
    	fld = oFld;
    }
  	
  	if(fld != null){
	  	String mandatory = getString(att, ATT_MANDATORY);
	  	if(mandatory != null){
	  		if(mandatory.equalsIgnoreCase("true")){
	  			fld.setMandatory(FField.MANDATORY_YES);
	  		}else if(mandatory.equalsIgnoreCase("weak")){
	  			fld.setMandatory(FField.MANDATORY_YES_BUT_CAN_FILL_LATER);
	  		}
	  	}
	  	
	  	boolean dbResident = getBoolean(att, ATT_DB_RESIDENT, true);
	  	fld.setDBResident(dbResident);
  	}
  	
  	return fld;
  }
  
  //Join Methods
  
  public XMLJoin getJoin(String alias){
  	return joinMap != null ? joinMap.get(alias) : null;
  }
  
  public Iterator<XMLJoin> newJoinIterator(){
  	return (joinMap != null && joinMap.size() > 0) ? joinMap.values().iterator() : null;
  }
  
//  public FocRequestDesc newFocRequestDesc(){
//  	FocRequestDesc requestDesc = null;
//    if(joinMap != null && joinMap.size() > 0){
//    	requestDesc = new FocRequestDesc();
//    	
//    	Iterator<XMLJoin> iter = joinMap.values().iterator();
//    	while(iter != null && iter.hasNext()){
//    		XMLJoin xmlJoin = iter.next();
//    		TableAlias tableAlias = xmlJoin.getTableAlias();
//    		requestDesc.addTableAlias(tableAlias);
//    	}
//
//    }
//    return requestDesc;
//  }
  
  //Helper Methods
  //--------------
  public String getName(Attributes att){
  	return att.getValue(ATT_NAME);
  }
  
  public String getTitle(Attributes attributes){
  	String title = attributes.getValue(ATT_TITLE);
  	if(title == null){
  		title = getName(attributes);
  	}
  	return title; 
  }
  
  public int getSize(Attributes attributes){
  	int value = 0;
  	String title = attributes.getValue(ATT_SIZE);
  	if(title != null){
  		value = Integer.valueOf(title);
  	}
  	return value; 
  }

  public int getDecimal(Attributes attributes){
  	int value = 0;
  	String title = attributes.getValue(ATT_DECIMALS);
  	if(title != null){
  		value = Integer.valueOf(title);
  	}
  	return value; 
  }
  
  public boolean isKey(Attributes att){
  	return getBoolean(att, ATT_KEY);
  }
  
  public boolean getBoolean(Attributes att, String name){
  	return getBoolean(att, name, false);
  }
  
  public static boolean getBoolean(Attributes att, String name, boolean defaultValue){
  	boolean value = defaultValue;
  	String title = att.getValue(name);
  	if(title != null){
  		value = Boolean.valueOf(title);
  	}
  	return value; 
  }
  
  public static int getInt(Attributes att, String name){
  	int value = 0;
  	String title = att.getValue(name);
  	if(title != null){
  		value = Integer.valueOf(title);
  	}
  	return value; 
  }
  
  public static String getString(Attributes att, String name){
  	String title = att.getValue(name);
  	return title; 
  }

	public XMLFocDesc getXmlFocDesc() {
		return xmlFocDesc;
	}

	public XMLFilter getXmlFilter() {
		return xmlFilter;
	}
	
	public FocListGroupBy getGroupBy(boolean createIfNeeded){
		if(groupBy == null){
			groupBy = new FocListGroupBy();
    	if(xmlFocDesc != null){
    		xmlFocDesc.setGroupBy(groupBy);
    	}			
		}	
		return groupBy;
	}
}

	