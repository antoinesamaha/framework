package com.foc.focDataSourceDB.db.connectionPooling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.foc.Globals;

public class StatementWrapper {
	private ConnectionWrapper connectionWrapper = null;
	private Statement         statement         = null;
	
	public StatementWrapper(ConnectionWrapper connectionWrapper, Statement statement){
		this.connectionWrapper = connectionWrapper;
		this.statement = statement;
	}
	
	public void dispose(){
		connectionWrapper = null;
		statement = null;
	}
	
	public void close(){
  	try{
  		if(statement != null){
  			statement.close();
  		}
  	}catch(Exception e){
  		Globals.logException(e);
  	}
	}
	
	public void unlockStatement() {
		if(connectionWrapper != null){
			connectionWrapper.unlockStatement(this);
		}
	}
	
	public Statement getStatement(){
		return statement;
	}
	
	public ResultSet getResultSet() throws SQLException {
		ResultSet resultSet = null;
		Statement stmt = getStatement();
		resultSet = stmt.getResultSet();
		return resultSet; 
	}
	
	public String getDBSourceKey(){
		return connectionWrapper != null ? connectionWrapper.getDBSourceKey() : null;
	}
	
	public void executeUpdate(String req) throws SQLException {
		getStatement().executeUpdate(req);
	}

	public ResultSet executeQuery(String req) throws SQLException {
		return getStatement() != null ? getStatement().executeQuery(req) : null;
	}
	
	public void execute(String req) throws SQLException{
		if(getStatement() != null){
			getStatement().execute(req);
		}
	}
}
