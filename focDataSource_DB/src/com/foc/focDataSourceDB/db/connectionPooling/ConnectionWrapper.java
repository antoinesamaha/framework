package com.foc.focDataSourceDB.db.connectionPooling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import com.foc.ConfigInfoWizardPanel;
import com.foc.Globals;
import com.foc.GuiConfigInfo;
import com.foc.focDataSourceDB.db.DBManagerServer;
import com.foc.performance.PerfManager;

public class ConnectionWrapper {
	private ConnectionPool pool       = null; 
	private Connection     connection = null;
	private boolean        autoCommit = true;
	
  private HashMap<StatementWrapper, StatementWrapper> busyStatements = null;
  private ArrayList<StatementWrapper>                 freeStatements = null;

	public ConnectionWrapper(ConnectionPool pool){
		this.pool = pool;
    busyStatements = new HashMap<StatementWrapper, StatementWrapper>();
    freeStatements = new ArrayList<StatementWrapper>();
	}
	
	public void dispose(){
		if(busyStatements != null){
			for(int i=0; i>busyStatements.size(); i++){
				busyStatements.get(i).close();
			}
			busyStatements.clear();
			busyStatements = null;
		}
		if(freeStatements != null){
			for(int i=0; i>freeStatements.size(); i++){
				freeStatements.get(i).close();
			}
			freeStatements.clear();
			freeStatements = null;
		}		
		closeConnection();
		pool = null;
	}
	
	public Connection getConnection(){
  	if(connection == null){
  		openConnection();
  		setAutoCommit(autoCommit);
  	}
		return connection;
	}
	
	public void setAutoCommit(boolean autoCommit){
		this.autoCommit = autoCommit;
		try{
			getConnection().setAutoCommit(autoCommit);
			if(!autoCommit){
				getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			}
		}catch (SQLException e){
			Globals.logException(e);
		}
	}
	
	public boolean isAutoCommit(){
		try{
			return getConnection().getAutoCommit();
		}catch (SQLException e){
			Globals.logException(e);
		}
		return false;
	}
	
	public ConnectionPool getPool(){
		return pool;
	}
	
//	public DBManagerServer getDBManagerServer(){
//		return getPool() != null ? getPool().getDbManagerServer() : null;
//	}
	
	public int getProvider(){
		return getPool() != null ? getPool().getProvider() : 0;
	}
	
	public boolean isValid() {
		boolean valid = false;
		try {
			valid = getConnection().isValid(5);
		} catch(Exception e) {
			Globals.logException(e);
		}
		return valid;
	}
	
  public synchronized StatementWrapper lockStatement() {
  	StatementWrapper stmtWrapper = null;
    if (freeStatements.size() > 0) {
    	stmtWrapper = (StatementWrapper) freeStatements.get(0);
      freeStatements.remove(0);
    } else {
      try {
      	Statement stm = getConnection().createStatement();
      	stmtWrapper = new StatementWrapper(this, stm);
      } catch (Exception e) {
        Globals.logException(e);
      }
    }
    if (stmtWrapper != null) {
      busyStatements.put(stmtWrapper, stmtWrapper);
    }
    
    return stmtWrapper;
  }
  
  public synchronized void unlockStatement(StatementWrapper stmtWrapper) {
    if (stmtWrapper != null) {
      busyStatements.remove(stmtWrapper);
      if(freeStatements.size() < 5){
      	freeStatements.add(stmtWrapper);	
      }else{
      	try{
      		stmtWrapper.close();
      	}catch(Exception e){
      		Globals.logException(e);
      	}
      }
    }
  }
	
  private void openConnection() {
  	ConnectionCredentials credentials = getPool() != null ? getPool().getCredentials() : null;
    try {
      Globals.logString("drivers "+credentials.getDrivers());
      Class.forName(credentials.getDrivers()).newInstance();
      
      Properties props = new Properties();
      props.put("user", credentials.getUsername());
      props.put("password", credentials.getPassword());
      props.put("characterEncoding", "UTF-8");
      
      Globals.logString("Connection URL "+credentials.getUrl());
      connection = DriverManager.getConnection(credentials.getUrl(), props);
    } catch (Exception e) {
    	String message = "Could not connect to Database: "+(credentials.getUrl() != null ? credentials.getUrl() : "")+"\nwith Username: "+(credentials.getUsername() != null ? credentials.getUsername() : ""); 
      if(Globals.getApp().isWithRegistry() && Globals.getDisplayManager() != null){
      	message += "\nYou will be redirected to change Environment";
      	Globals.getDisplayManager().popupMessage(message);
	      ConfigInfoWizardPanel panel = new ConfigInfoWizardPanel(new GuiConfigInfo(), ConfigInfoWizardPanel.STATE_DIRECTORY);
	      panel.setWithRestart(true);
	      Globals.getDisplayManager().popupDialog(panel, "", true);    
      }else{
	    	if(Globals.getDisplayManager() != null){
	    		Globals.getDisplayManager().popupMessage(message);
	    	}
	      Globals.logException(e);
	      System.exit(0);
      }
    }
  }

  private void closeConnection() {
    try {
    	if(connection != null){
    		destroyBusyStatments();
	      destroyFreeStatments();
	      connection.close();
	      connection = null;
    	}
    } catch (Exception e) {
      Globals.logException(e);
    }
  }
	
  public void refreshConnection(){
  	closeConnection();
  	openConnection();
  }

	public void destroyBusyStatments(){
		if(busyStatements != null){
			Iterator<StatementWrapper> iter = (Iterator<StatementWrapper>) busyStatements.values().iterator();
			while(iter.hasNext()){
				StatementWrapper stmt = iter.next();
				if(stmt != null){
					stmt.close();
				}
			}
			busyStatements.clear();
		}
	}
	
  public void destroyFreeStatments(){
    if(freeStatements != null){
      for(int i=freeStatements.size()-1; i>=0; i--){
        Statement stm = (Statement) freeStatements.get(i);
        try{
          stm.close();
          freeStatements.remove(i);
        }catch(Exception e){
          Globals.logException(e);
        }
      }
    }
  }

  public synchronized void commitTransaction(){
    try{
    	Globals.logString("Transaction Commit");
    	PerfManager.start("Commit_Transaction");
      getConnection().commit();
      PerfManager.end();
    }catch(Exception e){
      Globals.logException(e);
//      Globals.getApp().exit();
    }
  }

  /*
  public synchronized void rollbackTransaction(Savepoint savepoint){
    try{
      if(nbrOfBeginTransactionsInTransactionThread > 0 && transactionThread == Thread.currentThread()){
        nbrOfBeginTransactionsInTransactionThread--;
        if(savepoint != null){
        	getConnection().rollback(savepoint);
        }
      }else{
        while(getConnection().getAutoCommit()){
          wait();
        }      
        getConnection().rollback();
        getConnection().setAutoCommit(true);
        transactionThread = null;
        notifyAll();
      }
    }catch(Exception e){
      Globals.logException(e);
      Globals.getApp().exit();
    }
  }
  */
  
  public String getDBSourceKey(){
  	return pool != null ? pool.getDBSourceKey() : null;
  }

}
