package com.foc.focDataSourceDB.db.connectionPooling;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.focDataSourceDB.db.DBManagerServer;

public class ConnectionPool {
	private ConnectionCredentials        credentials     = null;
	
	private ConnectionWrapper            defaultConnectionWrapper = null;
	
	private ArrayList<ConnectionWrapper> busyConnections = null;//These Connections are autocommit=false
	private ArrayList<ConnectionWrapper> freeConnections = null;

	public ConnectionPoolThreadLocal threadLocal_Connections = null;
//	public ThreadLocal<ThreadDBTransactionData> threadLocal_TransactionData = null;
	
	public ConnectionPool(){
		this(null);
	}
	
	public ConnectionPool(ConnectionCredentials credentials){
		this.credentials = credentials;
	
		threadLocal_Connections = new ConnectionPoolThreadLocal();
//		threadLocal_TransactionData = new ThreadLocal<ThreadDBTransactionData>();
		
		busyConnections = new ArrayList<ConnectionWrapper>();
		freeConnections = new ArrayList<ConnectionWrapper>();
	}
	
	public void dispose(){
		if(busyConnections != null){
			for(int i=0; i<busyConnections.size(); i++){
				busyConnections.get(i).dispose();
			}
			busyConnections.clear();
			busyConnections = null;
		}
		
		if(freeConnections != null){
			for(int i=0; i<freeConnections.size(); i++){
				freeConnections.get(i).dispose();
			}
			freeConnections.clear();
			freeConnections = null;
		}
		
		if(credentials != null){
			credentials.dispose();
			credentials = null;
		}

		if(threadLocal_Connections != null){
			//TODO Should dispose this ThreadLocal local like it deserves to be disposed
			threadLocal_Connections = null;
		}
		
//		if(threadLocal_TransactionData != null){
//			threadLocal_TransactionData = null;
//		}
		
		dispose_DefaultConnectionWrapper();
	}
	
	public void dispose_DefaultConnectionWrapper(){
		if(defaultConnectionWrapper != null){
			defaultConnectionWrapper.dispose();
			defaultConnectionWrapper = null;
		}
	}

  private synchronized ConnectionWrapper lockConnectionWrapper() {
    ConnectionWrapper connection = null;
    if (freeConnections.size() > 0) {
      connection = (ConnectionWrapper) freeConnections.get(0);
      freeConnections.remove(0);
    } else {
    	connection = newConnectionWrapper();
    	connection.setAutoCommit(false);//All connections n the pool are not autocomit
    }
    
    if (connection != null) {
    	threadLocal_Connections.set(connection);
      busyConnections.add(connection);
    }
    return connection;
  }
  
  public synchronized void unlockConnectionWrapper(ConnectionWrapper connection) {
    if (connection != null) {
      busyConnections.remove(connection);
      if(freeConnections.size() < 5){
      	freeConnections.add(connection);	
      }else{
      	try{
      		connection.dispose();
      	}catch(Exception e){
      		Globals.logException(e);
      	}
      }
    	threadLocal_Connections.set(null);
    }
  }
	
	public int getProvider(){
		return getCredentials() != null ? getCredentials().getProvider() : 0;
	}
	/*
	public DBManagerServer getDbManagerServer() {
		return dbManagerServer;
	}

	public void setDbManagerServer(DBManagerServer dbManagerServer) {
		this.dbManagerServer = dbManagerServer;
	}
*/
	public ConnectionCredentials getCredentials() {
		if(credentials == null){
			credentials = new ConnectionCredentials();
			credentials.fillCredentialsIfNeeded();
		}
		return credentials;
	}

	public void setCredentials(ConnectionCredentials credentials) {
		this.credentials = credentials;
	}

	/**
	 * Checks if the current thread has already a Connection related to it and returns it.
	 * If not it will return the AUTOCOMMIT connection
	 * 
	 * @return {@link ConnectionWrapper}
	 */
	public ConnectionWrapper getConnectionWrapper(){
		return getConnectionWrapper(false); 
	}
	
	public void closeReopenConnection(){
		getConnectionWrapper(true);
	}

	private ConnectionWrapper recreateConnectionWrapper(ConnectionWrapper connectionWrapper){
		if(connectionWrapper != null){
			connectionWrapper.dispose();
			connectionWrapper = null;
			connectionWrapper = newConnectionWrapper();
		}
		return connectionWrapper;
	}

	private ConnectionWrapper getConnectionWrapper_IfDedicated(){
		ConnectionWrapper connectionWrapper = threadLocal_Connections.get();
		return connectionWrapper;
	}
	
	private ConnectionWrapper getConnectionWrapper(boolean closeReopenBecauseExpired){
		ConnectionWrapper connectionWrapper = getConnectionWrapper_IfDedicated();
		if(closeReopenBecauseExpired && connectionWrapper != null){
			threadLocal_Connections.set(recreateConnectionWrapper(connectionWrapper));
		}
		if(connectionWrapper == null){
			connectionWrapper = getDefaultConnectionWrapper();
			if(closeReopenBecauseExpired && connectionWrapper != null){
				dispose_DefaultConnectionWrapper();
				connectionWrapper = getDefaultConnectionWrapper();
			}
		}
		return connectionWrapper;
	}
		
	public Connection getConnection(){
		ConnectionWrapper cw = getConnectionWrapper();
		return cw != null ? cw.getConnection() : null;
	}

	private ConnectionWrapper newConnectionWrapper(){
		return new ConnectionWrapper(this);
	}
	
	private ConnectionWrapper getDefaultConnectionWrapper(){
		if(defaultConnectionWrapper == null){
			defaultConnectionWrapper = newConnectionWrapper();
		}
		return defaultConnectionWrapper; 
	}
	
	public StatementWrapper lockStatement(){
		return getConnectionWrapper() != null ? getConnectionWrapper().lockStatement() : null;
	}
	
	public void unlockStatement(StatementWrapper statement){
		if(getConnectionWrapper() != null){
			getConnectionWrapper().unlockStatement(statement);
		}
	}
	
	//--------------------------------------------------
	// Transaction
	//--------------------------------------------------
	
	public void transaction_BeginTransactionIfRequestIsToBeExecuted(){
		/*
		ThreadDBTransactionData transData = threadLocal_TransactionData.get();
		if(transData != null && transData.transaction_BeginTransactionIfRequestIsToBeExecuted()){
			Globals.logString("transaction_BeginTransactionIfRequestIsToBeExecuted");
			Globals.logString("FREE CONNECTIONS:"+freeConnections.size());
			Globals.logString("BUSY CONNECTIONS:"+busyConnections.size());

			//Make sure I have a dedicated transactional connection
			ConnectionWrapper connWrap = getConnectionWrapper_IfDedicated();
			if(connWrap == null){
				connWrap = lockConnectionWrapper();
			}
			
			Globals.logString("FREE CONNECTIONS:"+freeConnections.size());
			Globals.logString("BUSY CONNECTIONS:"+busyConnections.size());		
			
//		if(transaction_ShouldSurroundWithTransactionIfRequest && !transaction_Began){
//	    transaction_Began = true;
//    	beginTransaction();
//    }
		}
		*/
	}
	
	public void transaction_setShouldSurroundWithTransactionIfRequest(){
		/*
		ThreadDBTransactionData data = threadLocal_TransactionData.get();
		if(data == null){
			data = new ThreadDBTransactionData(this);
			threadLocal_TransactionData.set(data);
		}
		data.transaction_setShouldSurroundWithTransactionIfRequest();
		*/
	}
	
	public void transaction_SeeIfShouldCommit(){
		/*
		ThreadDBTransactionData transData = threadLocal_TransactionData.get();
//		Globals.logString("transaction_SeeIfShouldCommit - Point1");
		if(transData != null){
//			Globals.logString("transaction_SeeIfShouldCommit - Point2");
			if(transData.transaction_SeeIfShouldCommit()){
				Globals.logString("transaction_SeeIfShouldCommit");		
				Globals.logString("FREE CONNECTIONS:"+freeConnections.size());
				Globals.logString("BUSY CONNECTIONS:"+busyConnections.size());		
				
				getConnectionWrapper().commitTransaction();
				unlockConnectionWrapper(getConnectionWrapper());
				transData.dispose();
				threadLocal_TransactionData.remove();
//				commitTransaction();
				
				Globals.logString("FREE CONNECTIONS:"+freeConnections.size());
				Globals.logString("BUSY CONNECTIONS:"+busyConnections.size());
			}
		}
		*/
	}
	
	public String getDBSourceKey(){
		return getCredentials() != null ? getCredentials().getDbSourceKey() : null;
	}
	
	
	
	
	
  public Hashtable<String, String> newActualTables(Hashtable<String, String> exeTablesMap){
  	Hashtable<String, String> allTables = new Hashtable<String, String>();
    try {
    	Connection connection = getConnection();
    	if(connection != null){
	      DatabaseMetaData dmt = connection.getMetaData();
	      if (dmt != null) {
	        ResultSet resultSet = null;
	        if(getProvider() == DBManager.PROVIDER_ORACLE){
	        	//Instead of Username we should use the TABLESPACE Name
	        	resultSet = dmt.getTables(null, getCredentials().getUsername(), null, new String[] { "TABLE" });
	        }else{
	        	resultSet = dmt.getTables(null, null, null, new String[] { "TABLE" });
	        }
	        
	        if (resultSet != null) {
	          while (resultSet.next()) {
	            String tableName = resultSet.getString(3);
	            
	            Globals.logString(" TABLE : "+resultSet.getString(1)+" | "+resultSet.getString(2)+" | "+resultSet.getString(3));
	            
	            //String upperTableName = tableName.toUpperCase();
	            allTables.put(tableName, tableName);
	            if(exeTablesMap != null) exeTablesMap.put(tableName, tableName);
	          }
	          resultSet.close();
	        }
	      }
    	}
    } catch (Exception e) {
      Globals.logException(e);
    }
    return allTables;
  }

	
	
	
	public class ConnectionPoolThreadLocal extends ThreadLocal<ConnectionWrapper> {
		@Override
		public void remove() {
			super.remove();
		}
	}
}
