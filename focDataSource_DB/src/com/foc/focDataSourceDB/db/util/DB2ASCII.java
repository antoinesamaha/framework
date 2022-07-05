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
import java.util.ArrayList;
import java.util.Collections;
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

public class DB2ASCII {

	private final static String NULL_VALUE = "null";
	
	private String fileName = null;
	private String fileTableName = null;
	private int copyDirection = COPY_DIRECTION_DB_TO_ASCII;

	public final static int COPY_DIRECTION_DB_TO_ASCII = 1;
	public final static int COPY_DIRECTION_ASCII_TO_DB = 2;
	
	private boolean fabOnly = false;
	private boolean replaceNewLine = false;
	private Map<String, List<String>> attributesToSkip = null;
	
	public DB2ASCII(String fileName, String fileTableName, int copyDirection) throws Exception {
		this.fileName = fileName;
		this.fileTableName = fileTableName;
		this.copyDirection = copyDirection;
	}
		
	public DB2ASCII(String fileName, String fileTableName, int copyDirection, boolean replaceNewLine, Map<String, List<String>> attributesToSkip) throws Exception {
		this.fileName = fileName;
		this.fileTableName = fileTableName;
		this.copyDirection = copyDirection;
		this.replaceNewLine = replaceNewLine;
		this.attributesToSkip = attributesToSkip;
	}

	public void dispose(){
		fileName = null;
		fileTableName = null;
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
	
	private void copyTable(String tableName, BufferedWriter fileToPostWriter) {
		FocDesc focDesc = findFocDesc(tableName);
		if(focDesc != null){
			FocConstructor constr = new FocConstructor(focDesc, null);
			FocObject	tempObject = constr.newItem();
			
			StatementWrapper stmt = DBManagerServer.getInstance().lockStatement();
			try{
				stmt = DBManagerServer.getInstance().executeQuery_WithMultipleAttempts(stmt, "SELECT * FROM \""+tableName+"\"");
				ResultSet resultSet = stmt != null ? stmt.getResultSet() : null; 
		    //ResultSet resultSet = stmt.executeQuery("SELECT * FROM "+tableName);
		    
				Globals.logString("Starting table export for : "+tableName);
		    while(resultSet.next()){
		    	StringBuffer line = new StringBuffer(tableName+"|");
		    	ResultSetMetaData metaData= resultSet.getMetaData();
		    	for(int c=1; c<metaData.getColumnCount()+1; c++){
		      	String columnName = metaData.getColumnName(c);
		      	FField field = focDesc.getFieldByDBCompleteName_GetDBLevelField(columnName);//.toUpperCase());
		      	if(field == null) Globals.logString("Field not found : "+tableName+" "+columnName);
		      	FProperty prop = tempObject.getFocProperty(field.getID());//field.newProperty(null);
            if( prop == null ){
              prop = field.newProperty(null);
            }
		      	try {
//		      		 Globals.logString("Field : "+tableName+" "+columnName);
		      		if(!shouldSkipAttribute(tableName, columnName)) {
		      			if(prop != null && prop instanceof FBlobStringProperty) {
////		      				if(getProvider() == DBManager.PROVIDER_ORACLE){
//		      		  		sqlStr = new String(getString() != null ? getString() : "");
//		      		  		sqlStr = sqlStr.replaceAll("\"", "''");
//		      		  		sqlStr = "utl_raw.cast_to_raw(\'" + sqlStr + "\')";
////		      		  	}else{
////		      		  		sqlStr = super.getSqlString();
////		      		  	}
		      				prop.setSqlString("");
//		      			} else if (resultSet instanceof T4CBlobAccessor){
		      			}	else {
		      				prop.setSqlString(resultSet.getString(c));		      					      				
		      			}
		      		}
		      	} catch (Exception e) {
		      		Globals.logString("Field : "+tableName+" "+columnName);
		      		Globals.logException(e);
//		      		prop.setSqlString(resultSet.getString(c));
		      	} 
            
            String value = prop.getString();
		      	if(value.compareTo("") == 0){
		      		value = NULL_VALUE;
		      	}
		      	if(value.contains("VerticalLayout")){
							int i=0; 
							i++;
		      	}
		      	if(replaceNewLine) value = replaceNewLineInString(value);
		      	line.append(columnName+"|"+value+"|");
		    	}
		    	fileToPostWriter.write(line.toString());
		    	fileToPostWriter.newLine();
		    }
		    resultSet.close();
			}catch(Exception e){
				Globals.logException(e);
			}
			
	    DBManagerServer.getInstance().unlockStatement(stmt);

	    tempObject.dispose();
			tempObject = null;
		}
	}
	
	private boolean shouldSkipAttribute(String tableName, String columnName){
		boolean skip = false;
		if(attributesToSkip != null) {
			List<String> columns = attributesToSkip.get(tableName);
			if(columns != null) skip = columns.contains(columnName);
		}
		return skip;
	}
	
	private String replaceNewLineInString(String value) {
		if(!Utils.isStringEmpty(value)) value = value.replace("\r\n", FocLineReader.NEW_LINE_REPLACEMENT_STR).replace("\n",  FocLineReader.NEW_LINE_REPLACEMENT_STR).replace("\r",  FocLineReader.NEW_LINE_REPLACEMENT_STR);
		return value;
	}
	
	public Hashtable getTablesToCopy(){
		Hashtable allTables = null;
		if(isFabOnly()){
			allTables = new Hashtable<String, String>();
			FabModule module = FabModule.getInstance();
			if(module != null){
				for(int i=0; i<module.getDBTableCount(); i++){
					FocDesc desc = module.getDBTableAt(i);
					allTables.put(desc.getStorageName(), desc.getStorageName());
				}
			}
		}else{
			allTables = (Hashtable) DBManagerServer.getInstance().newAllRealTables();
		}
		return allTables;
	}
	
	private void copyDB2ASCII() throws Exception{
		File fileToPost = new File(fileName);
		File fileTableNameToPost = new File(fileTableName);
		
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
			
			fileTableNameToPost.delete();
			fileTableNameToPost.createNewFile();
	
			BufferedWriter fileToPostWriter = new BufferedWriter(new FileWriter(fileToPost, true));

			Hashtable allTables = getTablesToCopy();

			Iterator iter = allTables.values().iterator();
			List<String> tablenames = new ArrayList<String>();
			while(iter != null && iter.hasNext()){
				String tableName = (String) iter.next();
				if(tableName != null && tableName.trim().compareTo("") != 0){
					copyTable(tableName, fileToPostWriter);
					if(!tablenames.contains(tableName)) tablenames.add(tableName);
				}
			}
			
			fileToPostWriter.flush();
			fileToPostWriter.close();

			if(tablenames != null && tablenames.size() > 0) {
				Collections.sort(tablenames);
				BufferedWriter fileTableNameToPostWriter = new BufferedWriter(new FileWriter(fileTableNameToPost, true));
				for(int i=0; i < tablenames.size(); i++) {
					fileTableNameToPostWriter.write(tablenames.get(i));
					fileTableNameToPostWriter.newLine();
				}
				fileTableNameToPostWriter.flush();
				fileTableNameToPostWriter.close();					
			}
			
			if(Globals.getDisplayManager() != null){
				Globals.getDisplayManager().popupMessage("Backup finished for database : "+ConfigInfo.getJdbcURL()+"\nTo FOC Backup File : "+fileToPost);
			}
		}
	}	

	private void copyASCII2DB() throws Exception{
		InputStream inputStream = Globals.getInputStream(fileName);
		InputStream inputStreamTableName = Globals.getInputStream(fileTableName);

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

			
			// Clear existing tables
	    RestoreFileReader fileReader = new RestoreFileReader(inputStreamTableName, '~');
	    fileReader.setReadTableNamesOnly(true);
	    fileReader.readFile();
	    Iterator<String> iter = fileReader.newIterator_TablesInThisFile();
	    while(iter != null && iter.hasNext()){
	    	String tableName = iter.next();
        StatementWrapper stmt = DBManagerServer.getInstance().lockStatement();
        try {
          StringBuffer request = new StringBuffer("DELETE FROM \""+tableName + "\"");
          Globals.logString(request);
          stmt.executeUpdate(request.toString());
        } catch (SQLException e) {
          Globals.logException(e);
//          throw e;
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
	
	public void backupRestore() throws Exception{
		if(copyDirection == COPY_DIRECTION_DB_TO_ASCII){
			copyDB2ASCII();
		}else{
			copyASCII2DB();
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
					
					StringBuffer request = new StringBuffer("INSERT INTO \""+tableName+"\" ("+fields+") VALUES ("+values+")");
					
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
							fields.append("\"" + fieldName + "\"");
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

	public boolean isFabOnly() {
		return fabOnly;
	}

	public void setFabOnly(boolean fabOnly) {
		this.fabOnly = fabOnly;
	}
}
