package com.foc.link;

import java.util.HashMap;

public class FocOperationFactory{
	
	private HashMap<String, IFocOperation> operationMap = null;

	private FocOperationFactory(){
		operationMap = new HashMap<String, IFocOperation>();
	}
	
  public void dispose() {
    instance     = null;
    operationMap = null;
  }

  public void putOperation(String name, IFocOperation operation) {
  	operationMap.put(name, operation);
  }

  public IFocOperation getOperationByName(String name) {
    return operationMap.get(name);
  }

  public int getOperationCount() {
    return operationMap.size();
  }

  // ----------------------------------------------------------
  // Instance
  // ----------------------------------------------------------
  private static FocOperationFactory instance = null;

  public static FocOperationFactory getInstance() {
    if (instance == null)
      instance = new FocOperationFactory();
    return instance;
  }


}
