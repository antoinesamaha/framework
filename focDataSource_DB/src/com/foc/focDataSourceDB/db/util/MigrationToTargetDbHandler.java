/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.focDataSourceDB.db.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.fab.FabModule;
import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.db.DBManager;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.file.FocFileReader;
import com.foc.file.FocLineReader;
import com.foc.focDataSourceDB.db.DBManagerServer;
import com.foc.focDataSourceDB.db.SQLRequest;
import com.foc.focDataSourceDB.db.connectionPooling.StatementWrapper;
import com.foc.property.FBlobStringProperty;
import com.foc.property.FProperty;
import com.foc.util.Utils;

public class MigrationToTargetDbHandler {

	private final static String CONFIG_PREFIX = "migration.target."; 
	
	private final static String NULL_VALUE = "null";
	
	private String logFile = null;
	
	public MigrationToTargetDbHandler() {
	}
	
	public MigrationToTargetDbHandler(String logFile) {
		this.logFile = logFile;
	}

	public void dispose(){
		logFile = null;
	}
	
	public void startMigration() throws Exception{
		startMigratingData();
//		if(copyDirection == COPY_DIRECTION_DB_TO_ASCII){
//			copyDB2ASCII();
//		}else{
//			copyASCII2DB();
//		}
	}
	
	private void startMigratingData() {
		boolean doBackup = true;
		if(Globals.getDisplayManager() != null){
      int choice = JOptionPane.showConfirmDialog(Globals.getDisplayManager().getMainFrame(), 
          "Do you realy want to migrate the Data of your current DB into " + ConfigInfo.getProperty(CONFIG_PREFIX+ ConfigInfo.JDBC_URL) + " ?", 
          "Data Migrate To Target DB",
          JOptionPane.YES_NO_OPTION);
      doBackup = choice == JOptionPane.YES_OPTION;
		}
		
		if(doBackup){			
			Hashtable allTables = getTablesToCopy();
			Iterator iter = allTables.values().iterator();
			while(iter != null && iter.hasNext()){
				String tableName = (String) iter.next();
				if(!Utils.isStringEmpty(tableName)) migrateTableTable(tableName);
			}
			if(Globals.getDisplayManager() != null) Globals.getDisplayManager().popupMessage("Migration for database " + ConfigInfo.getJdbcURL() + " To database  " + ConfigInfo.getProperty(CONFIG_PREFIX+ ConfigInfo.JDBC_URL) + " completed successfully!");
		}
	}
	
	private FocDesc findFocDesc(String tableName){
		FocDesc foundFocDesc = null;
		tableName = tableName.toUpperCase();
		
		Iterator iter = Globals.getApp().getFocDescDeclarationIterator();
		while(iter != null && iter.hasNext() && foundFocDesc == null){
			IFocDescDeclaration descDeclaration = (IFocDescDeclaration) iter.next();
			if(descDeclaration != null){
				FocDesc focDesc = descDeclaration.getFocDescription();
				if(focDesc != null && focDesc.getStorageName().toUpperCase().compareTo(tableName) == 0){
					foundFocDesc = focDesc;
				}
			}
		}

		return foundFocDesc;
	}
	
	public Hashtable getTablesToCopy(){
		return(Hashtable) DBManagerServer.getInstance().newAllRealTables();
	}
	
	private void migrateTableTable(String tableName) {
//		FocDesc focDesc = findFocDesc(tableName);
//		if(focDesc != null){
//			FocConstructor constr = new FocConstructor(focDesc, null);
//			FocObject	tempObject = constr.newItem();
//			
//			StatementWrapper stmt = DBManagerServer.getInstance().lockStatement();
//			try{
//				stmt = DBManagerServer.getInstance().executeQuery_WithMultipleAttempts(stmt, "SELECT * FROM \""+tableName+"\"");
//				ResultSet resultSet = stmt != null ? stmt.getResultSet() : null; 
//		    //ResultSet resultSet = stmt.executeQuery("SELECT * FROM "+tableName);
//		    
//				Globals.logString("Starting table export for : "+tableName);
//		    while(resultSet.next()){
//		    	StringBuffer line = new StringBuffer(tableName+"|");
//		    	ResultSetMetaData metaData= resultSet.getMetaData();
//		    	for(int c=1; c<metaData.getColumnCount()+1; c++){
//		      	String columnName = metaData.getColumnName(c);
//		      	FField field = focDesc.getFieldByDBCompleteName_GetDBLevelField(columnName);//.toUpperCase());
//		      	if(field == null) Globals.logString("Field not found : "+tableName+" "+columnName);
//		      	FProperty prop = tempObject.getFocProperty(field.getID());//field.newProperty(null);
//            if( prop == null ){
//              prop = field.newProperty(null);
//            }
//		      	try {
//		      		if(!shouldSkipAttribute(tableName, columnName)) {
//		      			if(prop != null && prop instanceof FBlobStringProperty) {
//		      				prop.setSqlString("");
//		      			}	else {
//		      				prop.setSqlString(resultSet.getString(c));		      					      				
//		      			}
//		      		}
//		      	} catch (Exception e) {
//		      		Globals.logString("Field : "+tableName+" "+columnName);
//		      		Globals.logException(e);
////		      		prop.setSqlString(resultSet.getString(c));
//		      	} 
//            
//            String value = prop.getString();
//		      	if(value.compareTo("") == 0){
//		      		value = NULL_VALUE;
//		      	}
//		      	if(value.contains("VerticalLayout")){
//							int i=0; 
//							i++;
//		      	}
//		      	line.append(columnName+"|"+value+"|");
//		    	}
////		    	fileToPostWriter.write(line.toString());
////		    	fileToPostWriter.newLine();
//		    }
//		    resultSet.close();
//			}catch(Exception e){
//				Globals.logException(e);
//			}
//	    DBManagerServer.getInstance().unlockStatement(stmt);
//	    tempObject.dispose();
//			tempObject = null;
//		}
//	}
//	
//  public boolean validate(boolean checkValidity){
//  	return isDbResident() ? super.validate(checkValidity) : true;
  }
  
  private void executeMigrationCommand(StringBuffer buffer) {
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("UPDATE \"IncidentType\"       SET \"Code\"='-1' WHERE \"Code\" IS NULL ");
  	
  	
		Globals.getApp().getDataSource().command_ExecuteRequest(buffer);
  }
/*
	
	private void copyDB2ASCII() throws Exception{
		File fileToPost = new File(fileName);
		
		boolean doBackup = true;
		if(fileToPost.exists() && Globals.getDisplayManager() != null){
      int choice = JOptionPane.showConfirmDialog(Globals.getDisplayManager().getMainFrame(), 
          "Do you realy want to overite the file : "+fileName+" ?", 
          "Backup",
          JOptionPane.YES_NO_OPTION);
      doBackup = choice == JOptionPane.YES_OPTION;
		}
		
		if(doBackup){
			fileToPost.delete();
			fileToPost.createNewFile();
	
			BufferedWriter fileToPostWriter = new BufferedWriter(new FileWriter(fileToPost, true));
			
			Hashtable allTables = getTablesToCopy();
			Iterator iter = allTables.values().iterator();
			while(iter != null && iter.hasNext()){
				String tableName = (String) iter.next();
				if(tableName != null && tableName.trim().compareTo("") != 0){
					copyTable(tableName, fileToPostWriter);
				}
			}
			
			fileToPostWriter.flush();
			fileToPostWriter.close();
			
			if(Globals.getDisplayManager() != null){
				Globals.getDisplayManager().popupMessage("Backup finished for database : "+ConfigInfo.getJdbcURL()+"\nTo FOC Backup File : "+fileToPost);
			}
		}
	}	

	private void copyASCII2DB() throws Exception{
		InputStream inputStream = Globals.getInputStream(fileName);
		boolean doRestore = inputStream != null;

		if(doRestore){
			if(Globals.getDisplayManager() != null){
				String message = "Do you realy want to overwrite database : "+ConfigInfo.getJdbcURL()+"\nWith the FOC Backup File : "+fileName+" ?";
	      int choice = JOptionPane.showConfirmDialog(Globals.getDisplayManager().getMainFrame(), 
	      		message, 
	          "Database restore", 
	          JOptionPane.YES_NO_OPTION);
	      if( choice == JOptionPane.YES_OPTION){
	      	doRestore = true;
	      }else{
	      	doRestore = false;
	      }
			}
			if(!doRestore && Globals.getDisplayManager() != null){
				Globals.getDisplayManager().popupMessage("Restore aborted.");
			}
		}
		
		if(doRestore){
//			DBManagerServer.getInstance().beginTransaction();

	    RestoreFileReader fileReader = new RestoreFileReader(inputStream, '|');
	    fileReader.setReadTableNamesOnly(true);
	    fileReader.readFile();
	    Iterator<String> iter = fileReader.newIterator_TablesInThisFile();
	    while(iter != null && iter.hasNext()){
	    	String tableName = iter.next();
        StatementWrapper stmt = DBManagerServer.getInstance().lockStatement();
        try {
          StringBuffer request = new StringBuffer("DELETE FROM "+tableName);
          Globals.logString(request);
          stmt.executeUpdate(request.toString());
        } catch (SQLException e) {
          Globals.logException(e);
          throw e;
        }
        DBManagerServer.getInstance().unlockStatement(stmt);
	    }
			fileReader.dispose();
				    
			inputStream = Globals.getInputStream(fileName);
	    fileReader = new RestoreFileReader(inputStream, '|', true);
	    fileReader.readFile();
			fileReader.dispose();
			
//			DBManagerServer.getInstance().commitTransaction();
			
			if(Globals.getDisplayManager() != null){
				Globals.getDisplayManager().popupMessage("Restore finished.");
			}
		}
	}

	
	public class RestoreFileReader extends FocFileReader{

		private String            tableName           = null;
		private FocDesc           focDesc             = null;
		private FocObject	        tempObject          = null;
		private String            fieldName           = null;
		private StringBuffer      fields              = null;
		private StringBuffer      values              = null;
		
		//Used only for table names gathering
		private Hashtable<String, String> allTablesInThisFile = null ;
		private boolean                   readTableNamesOnly  = false;
		private boolean           shouldRevertNewLineChanges  = false;
		
		public RestoreFileReader(File file, char fieldDelimiter) {
			super(file, fieldDelimiter);
		}
		
    public RestoreFileReader(InputStream inputStream, char fieldDelimiter) {
      super(inputStream, fieldDelimiter);
    }
    
    public RestoreFileReader(InputStream inputStream, char fieldDelimiter, boolean shouldRevertNewLineChanges) {
      super(inputStream, fieldDelimiter);
      this.shouldRevertNewLineChanges = shouldRevertNewLineChanges;
    }

    public boolean shouldRevertNewLineChanges() {
    	return shouldRevertNewLineChanges;
    }
    
    public void dispose(){
			disposeTempObject();
			if(allTablesInThisFile != null){
				allTablesInThisFile.clear();
				allTablesInThisFile = null;
			}
			super.dispose();
		}

		public void disposeTempObject(){
			if(tempObject != null){
				tempObject.dispose();
				tempObject = null;
			}
		}
		
		public Iterator<String> newIterator_TablesInThisFile(){
			return allTablesInThisFile.values().iterator();
		}
		
		public void pushTableName(String name){
			if(isReadTableNamesOnly()){
				if(allTablesInThisFile == null){
					allTablesInThisFile = new Hashtable<String, String>();
				}
				allTablesInThisFile.put(name, name);
			}
		}
		
		private String current_table_restored;
		private long lineNumber;
		
		@Override
		public void readLine(StringBuffer buffer) {
			fields = new StringBuffer();
			values = new StringBuffer();
			
			scanTokens(buffer);
			
			if(isReadTableNamesOnly()){
				pushTableName(tableName);
			}else{
				if(fields != null && fields.toString().trim().compareTo("") != 0){
					StatementWrapper stmt = DBManagerServer.getInstance().lockStatement();
					
					StringBuffer request = new StringBuffer("INSERT INTO "+tableName+" ("+fields+") VALUES ("+values+")");
					
					try {
						Globals.logString(request.toString());
						stmt.executeUpdate(SQLRequest.adapteRequestToDBProvider(request));
					} catch (SQLException e) {
						Globals.logException(e);
					}
					DBManagerServer.getInstance().unlockStatement(stmt);				
				}
			}			
		}
		
		private boolean shouldSave(String tableName, long lineNumber) {
			boolean shouldSave = false;
			if(Utils.isStringEmpty(current_table_restored)) current_table_restored = tableName;
			if((!Utils.isStringEmpty(tableName) && !Utils.isStringEmpty(current_table_restored) && !current_table_restored.equals(tableName))
				|| ((lineNumber % 10000) == 0)) shouldSave = true;
			return shouldSave;
		}

		@Override
		public void readToken(String token, int pos) {
			if(pos == 0){
				if(tableName == null || token.compareTo(tableName) != 0){
					tableName = token;
					focDesc = findFocDesc(tableName);
					if(focDesc == null) {
						if(Globals.getDisplayManager() != null) {
							Globals.getDisplayManager().popupMessage("Desc not found for table : "+tableName);							
						}
					}
					disposeTempObject();
					FocConstructor constr = new FocConstructor(focDesc, null);
					tempObject = constr.newItem();
				}
			}else{
				switch((pos-1) % 2){
				case 0:
					fieldName = token;
					break;
				case 1:
					if(!shouldSkipAttribute(tableName, fieldName)) {
						if(token.compareTo(NULL_VALUE) == 0) token = ""; 
						FField field = focDesc.getFieldByDBCompleteName_GetDBLevelField(fieldName); //.toUpperCase());
						if(field == null){
							Globals.logString(" Field not found "+fieldName+" table "+tableName);
						}
						if(field != null){
	            FProperty prop = tempObject.getFocProperty(field.getID());
	            
	            if( prop == null ){
	              prop = field.newProperty(null);
	            }
	            prop.setString(token);  
	            
	            if(values.length() > 0) values.append(',');
							values.append(prop.getSqlString());
							
							if(fields.length() > 0) fields.append(','); 
							fields.append(fieldName);
						}
						break;
					}
				}
			}
		}

		public boolean isReadTableNamesOnly() {
			return readTableNamesOnly;
		}

		public void setReadTableNamesOnly(boolean readTableNamesOnly) {
			this.readTableNamesOnly = readTableNamesOnly;
		}
	}
*/
}
