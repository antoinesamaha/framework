package com.foc.desc.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.business.workflow.implementation.FocWorkflowDesc;
import com.foc.desc.FocDesc;
import com.foc.desc.FocDescMap;
import com.foc.desc.FocModule;
import com.foc.desc.field.FField;
import com.foc.desc.field.FFieldPath;
import com.foc.join.FocRequestDesc;
import com.foc.join.TableAlias;
import com.foc.list.FocList;
import com.foc.list.filter.FilterCondition;
import com.foc.list.filter.FilterConditionFactory;
import com.foc.list.filter.FilterDesc;
import com.foc.list.filter.IFocDescForFilter;
import com.foc.list.filter.ObjectCondition;
import com.foc.shared.dataStore.AbstractDataStore;
import com.foc.util.Utils;

public class XMLFocDesc extends FocWorkflowDesc implements IFocDescForFilter {

	private boolean parsed = false;
	
	private int nextFldID = 1;
	
	boolean listInTableEditable = false;
	
	private FocRequestDesc requestDesc = null;//Will remain null if not a join table
	private FilterDesc     filterDesc  = null;//Will remain null if not a filter 
	
	private XMLFocDescParser focDescParser = null;
	
	public XMLFocDesc(FocModule module, String storageName, String xmlFullFileName, Class<XMLFocObject> focObjectClass){
		super(focObjectClass, DB_RESIDENT, storageName, false, false);
		
//		parse(xmlFullFileName);
	}
	
	public void dispose(){
		super.dispose();
	}
	
	public void afterXMLParsing(){
	}
	
	@Override
	public int nextFldID(){
		return nextFldID++;
	}
	
  protected void afterConstruction_1(){
  	super.afterConstruction_1();
  	if(focDescParser != null){
	    FocRequestDesc reqDesc = getFocRequestDesc(true);
	    if(reqDesc != null){
	    	//This is the Join Case
	    	reqDesc.fillFocDesc(this);
	    }
  	}
  	
  	filterDesc = getFilterDesc();
  	if(filterDesc != null){
  		filterDesc.fillDesc(this, nextFldID());
  	}
  	
//  	FocListGroupBy groupBy = getGroupBy();
//  	if(groupBy != null){
//  		FocFieldEnum enumer = newFocFieldEnum(FocFieldEnum.CAT_ALL_DB, FocFieldEnum.LEVEL_PLAIN);
//  		while(enumer != null){
//  			enumer.getFieldCompleteName(this);
//  		}
//  	}
  }
  
  //Implementation of the Workflow
  //------------------------------
	@Override
	public boolean workflow_IsWorkflowSubject() {
		return iWorkflow_getWorkflowDesc() != null && super.workflow_IsWorkflowSubject();
	}

	@Override
	public int iWorkflow_getFieldIDShift() {
		return 2000;
	}

	@Override
	public String iWorkflow_getDBTitle() {
		return getStorageName();
	}

	@Override
	public String iWorkflow_getCodePrefix() {
		return "";
	}

	@Override
	public String iWorkflow_getCodePrefix_ForProforma() {
		return "";
	}
	//-----------------------------------
	
	@Override
  public boolean isJoin(){
		boolean join = super.isJoin();
		if(!join){
			join = getFocRequestDesc(false) != null;
		}
  	return join;
  }
	
	public boolean isListInTableEditable(){
		return listInTableEditable;
	}
	
	public void setListInTableEditable(boolean inTableEditable){
		listInTableEditable = inTableEditable;
	}
	
	protected FocList newFocList_Creation(){
		FocList list = super.newFocList();
		return list;
	}
	
	public FocList newFocList(){
		FocList list = newFocList_Creation();
		afterNewFocList(list);
		return list;
	}

	public void afterNewFocList(FocList list){
		if(list != null){
			list.setInTableEditable(isListInTableEditable());
		}
	}
	
	public static XMLFocDesc getInstance(String tableName){
		return (XMLFocDesc) (FocDescMap.getInstance() != null ? FocDescMap.getInstance().get(tableName, AbstractDataStore.TRANSACTION_TYPE_NONE) : null);
	}

	public FocRequestDesc getFocRequestDesc() {
		return getFocRequestDesc(false);
	}
	
	public FocRequestDesc getFocRequestDesc(boolean create) {
    if(requestDesc == null && create){
    	requestDesc = null;
    	
    	ArrayList<XMLJoin> joinArray = new ArrayList<XMLJoin>();
    	
    	Iterator<XMLJoin> iter = focDescParser.newJoinIterator();
    	if(iter != null && iter.hasNext()){
    		requestDesc = new FocRequestDesc();
	    	while(iter != null && iter.hasNext()){
	    		XMLJoin xmlJoin = iter.next();

	    		joinArray.add(xmlJoin);
	    	}
    	}

    	if(joinArray != null && joinArray.size() > 0){
    		Collections.sort(joinArray, new Comparator<XMLJoin>() {
					@Override
					public int compare(XMLJoin o1, XMLJoin o2) {
						return o1.getOrder() - o2.getOrder();
					}
    		});
    		
    		for(int i=0; i<joinArray.size(); i++){
    			XMLJoin xmlJoin = joinArray.get(i);
    			if(xmlJoin != null){
			  		TableAlias tableAlias = xmlJoin.getTableAlias();
			  		requestDesc.addTableAlias(tableAlias);
    			}
    		}
    	}
    }
		return requestDesc;
	}

	@Override
	public FilterDesc getFilterDesc() {
		if(filterDesc == null && focDescParser != null && focDescParser.getXmlFilter() != null){
			XMLFilter xmlFilter = focDescParser.getXmlFilter();
			
			FocDesc subjectFocDesc = Globals.getApp().getFocDescByName(xmlFilter.getTableName());
			if(subjectFocDesc != null){
				filterDesc = new FilterDesc(subjectFocDesc);
				
				for(int i=0; i<xmlFilter.getConitionCount(); i++){
					XMLFilterCondition condition = xmlFilter.getConitionAt(i);

					FField fld = (FField) subjectFocDesc.iFocData_getDataByPath(condition.getFieldPath());
					if(fld != null){
						FFieldPath fieldPath = FFieldPath.newFFieldPath(subjectFocDesc, condition.getFieldPath());
						FilterCondition cond = FilterConditionFactory.newConditionForField(fld, fieldPath, condition.getPrefix());
						if(cond != null){
							if(!Utils.isStringEmpty(condition.getCaption())){
								cond.setFieldLabel(condition.getCaption());
							}
							if(!Utils.isStringEmpty(condition.getCaptionProperty())){
								if(cond instanceof ObjectCondition){
									((ObjectCondition) cond).setCaptionProperty(condition.getCaptionProperty()); 
								}
							}
							filterDesc.addCondition(cond);
						}
					}else{
						Globals.logString("FocDesc not found for filter Subject Table "+xmlFilter.getTableName());
					}
				}
			}else{
				Globals.logString("FocDesc not found for filter Subject Table "+xmlFilter.getTableName());
			}
		}
		return filterDesc;
	}

	public XMLFocDescParser getFocDescParser() {
		return focDescParser;
	}

	public void setFocDescParser(XMLFocDescParser focDescParser) {
		this.focDescParser = focDescParser;
	}
}
