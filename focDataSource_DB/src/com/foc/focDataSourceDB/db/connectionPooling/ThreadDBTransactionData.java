package com.foc.focDataSourceDB.db.connectionPooling;

public class ThreadDBTransactionData {
	private ConnectionPool pool = null;
	
  private boolean transaction_Began                         = false;
  private int     nbrOfBeginTransactionsInTransactionThread = 0    ;
  
  public ThreadDBTransactionData(ConnectionPool pool){
  	this.pool = pool;
  }
  
  public void dispose(){
  	pool = null;
  }
  
	public boolean transaction_BeginTransactionIfRequestIsToBeExecuted(){
		boolean shouldBegin = false;
		if(!transaction_Began){
			transaction_Began = true;
			shouldBegin = true;//beginTransaction();
		}
		return shouldBegin;
	}
	
	public void transaction_setShouldSurroundWithTransactionIfRequest(){
		if(nbrOfBeginTransactionsInTransactionThread == 0){
			transaction_Began = false;
		}
		nbrOfBeginTransactionsInTransactionThread++;
	}
	
	public boolean transaction_SeeIfShouldCommit(){
		boolean shouldCommit = false;
    if(nbrOfBeginTransactionsInTransactionThread > 0){
      nbrOfBeginTransactionsInTransactionThread--;
    }
    if(nbrOfBeginTransactionsInTransactionThread == 0){
    	if(transaction_Began){
    		shouldCommit = true;
    	}
    }
    return shouldCommit;
	}
}
