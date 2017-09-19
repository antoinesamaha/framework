package com.foc.desc.pojo;

import com.foc.business.workflow.implementation.FocWorkflowDesc;
import com.foc.desc.FocDescMap;
import com.foc.list.filter.FilterDesc;
import com.foc.list.filter.IFocDescForFilter;
import com.foc.shared.dataStore.AbstractDataStore;

public class PojoFocDesc extends FocWorkflowDesc implements IFocDescForFilter {

	public PojoFocDesc(Class<PojoFocObject> focObjectClass, boolean dbResident, String storageName, boolean isKeyUnique){
		super(focObjectClass, dbResident, storageName, isKeyUnique, false);
		addReferenceField();
	}
	
	public void dispose(){
		super.dispose();
	}
	
  protected void afterConstruction_1(){
  	super.afterConstruction_1();
  	/*
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
  	*/
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

	/*
	@Override
  public boolean isJoin(){
		boolean join = super.isJoin();
		if(!join){
			join = getFocRequestDesc(false) != null;
		}
  	return join;
  }
	
	//Is we want to check if isJoin() before the FocRequestDesc construction
	public boolean hasJoinNode(){
		boolean has = false;
		Iterator<XMLJoin> iter = focDescParser != null ? focDescParser.newJoinIterator() : null;
		has = iter != null && iter.hasNext();
		return has;
	}
	*/
	
	public static PojoFocDesc getInstance(String tableName){
		return (PojoFocDesc) (FocDescMap.getInstance() != null ? FocDescMap.getInstance().get(tableName, AbstractDataStore.TRANSACTION_TYPE_NONE) : null);
	}

	/*
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
*/

	@Override
	public FilterDesc getFilterDesc() {
		return null;
	}
/*
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
	*/
}
