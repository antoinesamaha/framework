package com.foc.focDataSourceDB.db.adaptor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import com.fab.model.project.FabProject;
import com.fab.model.project.FabProjectDesc;
import com.fab.model.project.FabWorkspace;
import com.fab.model.project.FabWorkspaceDesc;
import com.fab.model.table.TableDefinition;
import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.admin.FocVersion;
import com.foc.db.DBIndex;
import com.foc.db.DBManager;
import com.foc.db.FocDBException;
import com.foc.db.SequenceDoesNotExistException;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocFieldEnum;
import com.foc.desc.FocModule;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.desc.field.FReferenceField;
import com.foc.focDataSourceDB.FocDataSource_DB;
import com.foc.focDataSourceDB.db.DBManagerServer;
import com.foc.focDataSourceDB.db.SQLAlterTable;
import com.foc.focDataSourceDB.db.SQLCreateIndex;
import com.foc.focDataSourceDB.db.SQLCreateTable;
import com.foc.focDataSourceDB.db.SQLDropIndex;
import com.foc.focDataSourceDB.db.SQLTableDetails;
import com.foc.focDataSourceDB.db.SQLTableIndexesDetails;
import com.foc.focDataSourceDB.db.connectionPooling.ConnectionPool;
import com.foc.focDataSourceDB.db.connectionPooling.StatementWrapper;
import com.foc.focDataSourceDB.db.util.DBUtil;
import com.foc.list.FocLink;
import com.foc.util.ASCII;
import com.foc.util.Utils;

public class DBAdaptor {

	private HashMap<String, Hashtable<String, String>> actualTablesByConnection = null;
	
//  private Hashtable<String, String> allTables      = null;
  private Hashtable<String, String> exeTables      = null;
  private boolean                   alterAllFields = false;
  private FocDataSource_DB          dataSource     = null;
  
  public DBAdaptor(FocDataSource_DB dataSource){
  	this.dataSource = dataSource;
  }

  public void dispose(){
  	if(exeTables != null){
  		exeTables.clear();
  		exeTables = null;
  	}
  	dataSource = null;
  }
  
  public void dispose_ActualTablesByConnection(){
  	if(actualTablesByConnection != null){
  		actualTablesByConnection.clear();
  		actualTablesByConnection = null;
  	}
  }
  
	public boolean adaptTable(FocDesc focDesc) {
		dispose_ActualTablesByConnection();
		actualTables_fillMap(true);
		boolean altered = adaptTable_DoNotGetActualTableNames(focDesc);
		dispose_ActualTablesByConnection();
    return altered;
	}
	
	public boolean adaptTable_DoNotGetActualTableNames(FocDesc focDesc) {
  	boolean altered = adaptTable_Internal(focDesc);
  	if(focDesc != null && focDesc.getLanguageValueFocDesc() != null){
  		adaptTable_Internal(focDesc.getLanguageValueFocDesc());
  	}
    return altered;		
	}

	private String adjustedFieldName(String name){
		String result = ASCII.convert_ShrinkName(name, 30);
		if(result.length() > 30){
			result = ASCII.convert_ShrinkName(name, 30);
		}
		return result;
	}
	
  public boolean adaptDataModel(boolean forceAlterTables, boolean schemaEmpty){
  	alterAllFields = forceAlterTables;
  	DBManager dbManager = Globals.getDBManager(); 
  	try{
  		actualTables_fillMap(true);
  		
	    Iterator<FocModule> iter1 = Globals.getApp().modules_Iterator();
	    while(iter1 != null && iter1.hasNext()){
	      FocModule module = iter1.next();
        module.beforeAdaptDataModel();
	    }
	    
	    // Just go and open all descriptions so that links with masters get created
	    Iterator<IFocDescDeclaration> iter = Globals.getApp().getFocDescDeclarationIterator();
	    while(iter != null && iter.hasNext()){
	    	IFocDescDeclaration focDescDeclaration = iter.next();
	    	if(focDescDeclaration != null){
	    		focDescDeclaration.getFocDescription();
	    	}
	    }
	
	    //Drop all FFK_ constraints on all Oracle connections
	    /*
	  	ConnectionPool defaultConnection = DBManagerServer.getInstance().getConnectionPool(null);
	  	if(defaultConnection.getProvider() == DBManager.PROVIDER_ORACLE){
	  		dropConstrains_Oracle(null);	
	  	}
	  	
	  	Iterator<ConnectionPool> auxConnIter = DBManagerServer.getInstance().auxPools_Iterator();
	  	while(auxConnIter != null && auxConnIter.hasNext()){
	  		ConnectionPool connectionPool = auxConnIter.next();
	  		if(connectionPool.getProvider() == DBManager.PROVIDER_ORACLE){
		  		String sourceKey = connectionPool.getDBSourceKey();
		  		if(connectionPool != null && sourceKey != null){
		  			dropConstrains_Oracle(sourceKey);		
		  		}
	  		}
	  	}
	  	*/
	  	//--------------------------------------------------
	    
	    
	    //Do the adapt for the 2 FAB tables
	    //If Altered we need to redo this process after Desc reload from FAB
	    boolean oneOrMoreTableOfFabBasicTableAltered = false;
	    for(int i=0; i<dataSource.getApp().fabDefTables_Size(); i++){
	    	String tableName = dataSource.getApp().fabDefTables_Get(i);
		    FocDesc fabFocDesc = Globals.getApp().getFocDescByName(tableName);
		    if(fabFocDesc != null && fabFocDesc.isAllowAdaptDataModel()){
		    	oneOrMoreTableOfFabBasicTableAltered = adaptTable_DoNotGetActualTableNames(fabFocDesc) || oneOrMoreTableOfFabBasicTableAltered;
	        adaptTableIndexes(fabFocDesc, false);
	        adaptTableSequence(fabFocDesc);
		    }
	    }
	    
	    if(oneOrMoreTableOfFabBasicTableAltered && !schemaEmpty){
	    	String message = 	"Adapt data model not completed. \n " +
	    										"Please relaunch application and adapt data modle another time.";
	    	if(Globals.getDisplayManager() != null){
		    	Globals.getDisplayManager().popupMessage(message);
	    	}else{
	    		Globals.logString(message);
	    	}
	    	Globals.getApp().exit(true);
	    }

//	    if(Globals.getDBManager().getProvider() == DBManager.PROVIDER_ORACLE){
		    iter = Globals.getApp().getFocDescDeclarationIterator();
		    while(iter != null && iter.hasNext()){
		    	IFocDescDeclaration focDescDeclaration = iter.next();
		    	if(focDescDeclaration != null){
			    	FocDesc focDesc =  focDescDeclaration.getFocDescription();
			    	if(focDesc != null && focDesc.isPersistent()){
			    		if(focDesc.getStorageName_ForSQL().length() > 30){
			    			Globals.logString("  --- TABLE:"+focDesc.getStorageName_ForSQL());
			    		}
			    		
			    		FocFieldEnum enumer = focDesc.newFocFieldEnum(FocFieldEnum.CAT_ALL_DB, FocFieldEnum.LEVEL_DB);
			    		while(enumer != null && enumer.hasNext()){
			    			FField fld = enumer.nextField();
			    			String dbName = enumer.getFieldCompleteName(focDesc);
			    			if(dbName.length() > 30){
//			    				Globals.logString("    - Field:"+focDesc.getStorageName()+"."+dbName);
//			    				dbName = adjustedFieldName(dbName);
//			    				Globals.logString("            "+focDesc.getStorageName()+"."+dbName+"    ("+dbName.length()+")");
			    				String newDBName = adjustedFieldName(dbName);
			    				Globals.logString(""+focDesc.getStorageName()+","+dbName+","+dbName.length()+","+newDBName+","+newDBName.length());
			    			}
			    		}
			    	}
		    	}
		    }
//	    }
	    
	    //Here we need to compare the contents of the table definition table with the memory table definition
	    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
	    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
	    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
	    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
	    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
	    
	    iter = Globals.getApp().getFocDescDeclarationIterator();
	    while(iter != null && iter.hasNext()){
	    	IFocDescDeclaration focDescDeclaration = iter.next();
	    	//if(focDescDeclaration != null && (!(focDescDeclaration instanceof DBFocDescDeclaration) || !ConfigInfo.isForDevelopment())){
	    	if(focDescDeclaration != null /*&& (!(focDescDeclaration instanceof DBFocDescDeclaration) || !ConfigInfo.isForDevelopment())*/){
		    	FocDesc focDesc =  focDescDeclaration.getFocDescription();
		    	if(focDesc != null && focDesc.isAllowAdaptDataModel()){
		    		try{
		    			adaptTable_DoNotGetActualTableNames(focDesc);
		        }catch(Exception e){
		        	Globals.logException(e);
		    		}
		        try{
		        	adaptTableIndexes(focDesc, false);
		        }catch(Exception e){
		        	Globals.logException(e);
		        }
		        try{
		        	adaptTableSequence(focDesc);
		        }catch(Exception e){
		        	Globals.logException(e);
		        }
		        
		        // Scan the descriptions fields and store the links tables
		        FocFieldEnum enumer = focDesc.newFocFieldEnum(FocFieldEnum.CAT_LIST, FocFieldEnum.LEVEL_PLAIN);
		        while (enumer.hasNext()) {
		          FField ffield = enumer.nextField();
		          if (ffield != null && ffield.isDBResident()) {
		            FocLink link = ffield.getLink();
		            FocDesc linkTablDesc = link != null ? link.getLinkTableDesc() : null;
		            if (linkTablDesc != null && linkTablDesc.isAllowAdaptDataModel()) {
		            	adaptTable_DoNotGetActualTableNames(linkTablDesc);
		              adaptTableIndexes(linkTablDesc, false);
		    	        adaptTableSequence(linkTablDesc);
		            }
		          }
		        }
		    	}
	      }
	    }

	    //Oracle Constraints
	    //------------------
	    /*
	    iter = Globals.getApp().getFocDescDeclarationIterator();
	    while(iter != null && iter.hasNext()){
	    	IFocDescDeclaration focDescDeclaration = iter.next();
	    	if(focDescDeclaration != null){
		    	FocDesc focDesc =  focDescDeclaration.getFocDescription();
		    	if(focDesc != null && focDesc.isAllowAdaptDataModel()){
		    		try{
		    			adaptDBTable_Constraints_Oracle(focDesc);
		        }catch(Exception e){
		        	Globals.logException(e);
		    		}
		    	}
	      }
	    }
	    */
	    //------------------
	    
	    //dbManager.endAdaptDataModel();
	    
    	if(DBManagerServer.getInstance() != null){//Logging unused tables
    		Globals.logString("Unused Tables:");
    		
    		Iterator<String> keyIter = actualTables_fillForOneConnection(DBManagerServer.getInstance().getConnectionPool(null)).keySet().iterator();
    		while(keyIter != null && keyIter.hasNext()){
    			String str = keyIter.next();
    			if(getExeTables().get(str) == null){
    				Globals.logString("DROP TABLE IF EXISTS "+str+";", false);
    			}
    		}
      }

	    iter1 = Globals.getApp().modules_Iterator();
	    while(iter1 != null && iter1.hasNext()){
	      FocModule module = (FocModule) iter1.next();
	      module.declareSP();
	    }
	    
	    if(dataSource != null){
	    	dataSource.setEmptyDatabaseJustCreated(false);
	    }

	    FocVersion.saveVersions();
	    
	    iter1 = Globals.getApp().modules_Iterator();
	    while(iter1 != null && iter1.hasNext()){
	      FocModule module = (FocModule) iter1.next();
	      module.afterAdaptDataModel();
	    }
	    
	    dispose_ActualTablesByConnection();
  	}catch(Exception e){
  		Globals.logException(e);
  		if(Globals.getDisplayManager() != null){
  			Globals.getDisplayManager().popupMessage("Adapt data model failed! and Aborted!");
  		}
  	}finally{
  		dispose_ActualTablesByConnection();
  	}
  	return false;
  }
  
  public void adaptTableSequence(FocDesc focDesc) throws Exception{
  	if(			focDesc.getProvider() == DBManager.PROVIDER_ORACLE
  			&&  focDesc.isPersistent()
  			&& 	focDesc.getFieldByID(FField.REF_FIELD_ID) != null){
  		StatementWrapper stm = DBManagerServer.getInstance().lockStatement(focDesc.getDbSourceKey());
  		FField indentifyerField = focDesc.getIdentifierField();
  		if(indentifyerField != null && indentifyerField.isDBResident()){
  			String refFieldName = indentifyerField.getDBName();
	  		String req = "SELECT MAX(\""+refFieldName+"\") FROM \""+focDesc.getStorageName_ForSQL()+"\"";
	
	  		stm = DBManagerServer.getInstance().executeQuery_WithMultipleAttempts(stm, req);
	  		
//	  		if(ConfigInfo.isLogDBRequestActive()) Globals.logString(req);
//	  		stm.executeQuery(req);
	  		
	  		long maxRef = 0;
	  		ResultSet rs = stm != null ? stm.getResultSet() : null;
	  		while(rs != null && rs.next()){
	  			maxRef = rs.getLong(1);
	  		}
        try{
          rs.close();
        }catch(Exception e){
          Globals.logException(e);
        }
	  		DBManagerServer.getInstance().unlockStatement(stm);
	
	  		long seq = 0;
	  		
	  		try{
	  			seq = DBUtil.getNextSequence(focDesc);//Cannot use the current value because we will get that it is not defined yet in this session
	  		}catch(SequenceDoesNotExistException e){
	        stm = DBManagerServer.getInstance().lockStatement(focDesc.getDbSourceKey());
	    		req = "CREATE SEQUENCE "+focDesc.getSequenceName()+" START WITH "+(maxRef+1)+" INCREMENT BY 1 NOMAXVALUE";
	    		if(ConfigInfo.isLogDBRequestActive()) Globals.logString(req);
	        stm.executeUpdate(req);
	        DBManagerServer.getInstance().unlockStatement(stm);
	        seq = maxRef+1;
	  		}catch(FocDBException e){
	  			Globals.logException(e);
	  		}catch(SQLException e){
	  			throw e;
	  		}
	
	  		if(seq < maxRef){
	  			stm = DBManagerServer.getInstance().lockStatement(focDesc.getDbSourceKey());
	    		req = "ALTER SEQUENCE "+focDesc.getSequenceName()+" INCREMENT BY "+(maxRef+1-seq);
	    		if(ConfigInfo.isLogDBRequestActive()) Globals.logString(req);
	        stm.executeUpdate(req);
	        DBManagerServer.getInstance().unlockStatement(stm);
	      }
  		}
  	}
  }
  //EAntoineS - AUTOINCREMENT

	//-----------------------------------------------------
	//-----------------------------------------------------
	// command_AdaptDataModel_SingleTable
	//-----------------------------------------------------
	//-----------------------------------------------------
  
  private Hashtable<String, String> getExeTables(){
  	if(exeTables == null){
  		exeTables = new Hashtable<String, String>();
  	}
  	return exeTables;
  }
  
  private Hashtable<String, String> actualTables_getTablesForOneConnection(String dbSourceKey){
  	Hashtable<String, String> hashTable = null;
  	if(actualTablesByConnection != null){
  		hashTable = actualTablesByConnection.get(dbSourceKey);
  	}
  	return hashTable;
  }
  
  private HashMap<String, Hashtable<String, String>> actualTables_fillMap(boolean create){
  	if(actualTablesByConnection == null && create){
	  	actualTablesByConnection = new HashMap<String, Hashtable<String, String>>();
	
	  	ConnectionPool defaultConnection = DBManagerServer.getInstance().getConnectionPool(null);
	  	Hashtable<String, String> actualTables = actualTables_fillForOneConnection(defaultConnection);
	  	actualTablesByConnection.put(null, actualTables);
	  	
	  	Iterator<ConnectionPool> auxConnIter = DBManagerServer.getInstance().auxPools_Iterator();
	  	while(auxConnIter != null && auxConnIter.hasNext()){
	  		ConnectionPool connectionPool = auxConnIter.next();
	  		String sourceKey = connectionPool.getDBSourceKey();
	  		if(connectionPool != null && sourceKey != null){
	  	  	actualTables = actualTables_fillForOneConnection(connectionPool);
	  	  	
	  	  	actualTablesByConnection.put(sourceKey, actualTables);
	  		}
	  	}
  	}
  			
  	return actualTablesByConnection;
  }
  
  private Hashtable<String, String> actualTables_fillForOneConnection(ConnectionPool connectionPool){
  	Hashtable<String, String> allTables = new Hashtable<String, String>();
    try {
    	Connection connection = connectionPool.getConnection();
    	if(connection != null){
//    		DatabaseMetaData dmt = DBManagerServer.getInstance().getConnection().getMetaData();
	      DatabaseMetaData dmt = connection.getMetaData();
	      //Globals.logString(dmt.getTimeDateFunctions());
	      if (dmt != null) {
	        ResultSet resultSet = null;
	        if(connectionPool.getProvider() == DBManager.PROVIDER_ORACLE){
	        	//Instead of Username we should use the TABLESPACE Name
	        	resultSet = dmt.getTables(null, connectionPool.getCredentials().getUsername(), null, new String[] { "TABLE" });
	        }else{
	        	resultSet = dmt.getTables(null, null, null, new String[] { "TABLE" });
	        }
	        
	        if (resultSet != null) {
	          while (resultSet.next()) {
	            String tableName = resultSet.getString(3);
	            
	            Globals.logString(" TABLE : "+resultSet.getString(1)+" | "+resultSet.getString(2)+" | "+resultSet.getString(3));
	            
	            //String upperTableName = tableName.toUpperCase();
	            allTables.put(tableName, tableName);
	            getExeTables().put(tableName, tableName);
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
  
  private void dropColumnConstrains_MSSQL(FocDesc focDesc, String columnName){
  	try{
			ArrayList<String> constraintsToDrop = new ArrayList<String>();
			StatementWrapper stmt = DBManagerServer.getInstance().lockStatement(focDesc.getDbSourceKey());
			String request = "SELECT df.name FROM sys.default_constraints df INNER JOIN sys.tables t ON df.parent_object_id = t.object_id INNER JOIN sys.columns c ON df.parent_object_id = c.object_id AND df.parent_column_id = c.column_id where t.name='"+focDesc.getStorageName_ForSQL()+"' and c.NAME='"+columnName+"'";
	    stmt = DBManagerServer.getInstance().executeQuery_WithMultipleAttempts(stmt, request);
	    ResultSet resSet = stmt.getResultSet();
	    if(resSet != null){
	    	while(resSet.next()){
	  			String value = resSet.getString(1);
	  			constraintsToDrop.add(value);
	    	}
	    	resSet.close();
	    }
	    DBManagerServer.getInstance().unlockStatement(stmt);
	    
	    for(int i=0; i<constraintsToDrop.size(); i++){
	    	String constraintName = constraintsToDrop.get(i);
	    	StatementWrapper constraintStmt = DBManagerServer.getInstance().lockStatement(focDesc.getDbSourceKey());
	    	String req = "alter table "+focDesc.getStorageName_ForSQL()+" drop constraint ["+constraintName+"]";
	    	constraintStmt = DBManagerServer.getInstance().executeQuery_WithMultipleAttempts(constraintStmt, req);
	    	DBManagerServer.getInstance().unlockStatement(constraintStmt);
	    }
  	}catch(Exception e){
  		Globals.logException(e);
  	}
  }
  
  private void dropConstrains_Oracle(String dbSourceKey){
    try{
    	ArrayList<String> constraintsDropRequests = new ArrayList<String>();
    	
			StatementWrapper stmt = DBManagerServer.getInstance().lockStatement(dbSourceKey);
			String request = "SELECT table_name, constraint_name FROM user_constraints where constraint_name like 'FFK_%'";
	    stmt = DBManagerServer.getInstance().executeQuery_WithMultipleAttempts(stmt, request);
	    ResultSet resSet = stmt.getResultSet();
	    if(resSet != null){
	    	while(resSet.next()){
	  			String tableName     = resSet.getString(1);
	  			String contraintName = resSet.getString(2);
	  			constraintsDropRequests.add("alter table \""+tableName+"\" drop constraint \""+contraintName+"\"");
	    	}
	    	resSet.close();
	    }
	    DBManagerServer.getInstance().unlockStatement(stmt);
	    
	    for(int i=0; i<constraintsDropRequests.size(); i++){
	    	request = constraintsDropRequests.get(i);
	    	StatementWrapper constraintStmt = DBManagerServer.getInstance().lockStatement(dbSourceKey);
	    	constraintStmt = DBManagerServer.getInstance().executeQuery_WithMultipleAttempts(constraintStmt, request);
	    	DBManagerServer.getInstance().unlockStatement(constraintStmt);
	    }
    }catch(Exception e){
    	Globals.logException(e);
    }
  }
  
  private void adaptDBTable_Constraints_Oracle(FocDesc focDesc){
  	if(focDesc != null && focDesc.isDbResident() && focDesc.getProvider() == DBManager.PROVIDER_ORACLE){
	    FocFieldEnum enumer = focDesc.newFocFieldEnum(FocFieldEnum.CAT_ALL_DB, FocFieldEnum.LEVEL_PLAIN);
	    while (enumer.hasNext()) {
	      FField field = (FField) enumer.next();
	  	
		  	if(field != null && field instanceof FObjectField && field.isDBResident()){
		      try{
		      	int tableNameHashCode = focDesc.getStorageName_ForSQL().hashCode();
		      	if(tableNameHashCode < 0) tableNameHashCode = -tableNameHashCode;
		      	String constraintName = "FFK_"+tableNameHashCode+"_"+field.getDBName();
		      	if(constraintName.endsWith("_REF")){
		      		constraintName = constraintName.substring(0, constraintName.length()-4);
		      	}else if(constraintName.endsWith("REF")){
		      		constraintName = constraintName.substring(0, constraintName.length()-3);
		      	}
		     
			  		FocDesc otherFocDesc = field.getFocDesc();
			  		if(Utils.isEqual_String(otherFocDesc.getDbSourceKey(), focDesc.getDbSourceKey())){
				  		FReferenceField refField = (FReferenceField) otherFocDesc.getFieldByID(FField.REF_FIELD_ID); 
				  		
				  		if(refField != null){
								StatementWrapper stmt = DBManagerServer.getInstance().lockStatement(focDesc.getDbSourceKey());
								String request = "alter table \""+focDesc.getStorageName_ForSQL()+"\"";
								request += " add constraint \""+constraintName+"\" foreign key (\""+field.getDBName()+"\")";
								request += " references \"" + otherFocDesc.getStorageName_ForSQL() +"\" (\""+refField.getDBName()+"\") novalidate";
						    stmt = DBManagerServer.getInstance().executeQuery_WithMultipleAttempts(stmt, request);
						    DBManagerServer.getInstance().unlockStatement(stmt);
				  		}
			  		}
		      }catch(Exception e){
		      	Globals.logException(e);
		      }
		  	}
	    }
  	}
//  	alter table PLACE
//    add constraint FK_PLACE_REF_15765_COUNTRY foreign key (SEQ_COUNTRY)
//    references COUNTRY (SEQ_COUNTRY);
  }
  
  private boolean adaptDBTableFields(FocDesc focDesc) {
    boolean adapted = false;
    SQLTableDetails table = new SQLTableDetails(focDesc);
    table.buildRequest();
    table.execute();

    Hashtable actualFields = table.getFieldsHashtable();

    FocFieldEnum enumer = focDesc.newFocFieldEnum(FocFieldEnum.CAT_ALL_DB, FocFieldEnum.LEVEL_DB);
    while (enumer.hasNext()) {
      FField focField = (FField) enumer.next();
      
      FField realField = null;
      String modelFieldName = enumer.getFieldCompleteName(focDesc);
//      if(Globals.getDBManager().getProvider() == DBManager.PROVIDER_ORACLE){
//      	modelFieldName = modelFieldName.toUpperCase();
//      }
      realField = (FField) actualFields.get(modelFieldName);
      try{
	      if (realField == null) {
//	        new SQLTableDetails(focDesc);
	        SQLAlterTable alter = new SQLAlterTable(focDesc, focField, enumer.getFieldCompleteName(focDesc), SQLAlterTable.ADD);
	        if(ConfigInfo.isAdaptEnabled()){
						alter.execute();
	      	}else{
	      		alter.buildRequest();
	      		String req = alter.getRequestAdaptedToProvider();
	      		if(req != null) Globals.logString("NOT EXECUTED: " + req);
	      	}
	        adapted = true;	        
	        /*
	      } else if (realField.getSqlType() != focField.getSqlType()) {
	        Exception e = new Exception("Field type conflict "+focDesc.getStorageName()+"."+focField.getDBName()+" db:" + realField.getName() + " " + realField.getClass().getName() + " app:" + enumer.getFieldCompleteName(focDesc) + " " + focField.getClass().getName());
	        Globals.logException(e);
	        */
	      } else if (isAlterAllFields() || focField.compareSQLDeclaration(realField) != 0) {
	        //focField.compareSQLDeclaration(realField);
	      	String fieldCompleteName = enumer.getFieldCompleteName(focDesc);
	        SQLAlterTable alter = new SQLAlterTable(focDesc, focField, fieldCompleteName, SQLAlterTable.MODIFY);
	        if(ConfigInfo.isAdaptEnabled()){
	        	
	  	      try{
	  	      	//We are Dropping Columns and in MSSQL we need to drop the constraints first
	  	      	if(focDesc.getProvider() == DBManager.PROVIDER_MSSQL){
	  	      		dropColumnConstrains_MSSQL(focDesc, fieldCompleteName);
	  	      	}
	  	      }catch(Exception e){
	  	      	Globals.logException(e);
	  	      }
	        	
						alter.execute();
	      	}else{
	      		alter.buildRequest();
	      		String req = alter.getRequestAdaptedToProvider();
	      		if(req != null) Globals.logString("NOT EXECUTED: " + req);
	      	}
	        //BAntoineS - AUTOINCREMENT
	      } else if (!realField.isAutoIncrement() && focField.isAutoIncrement() && realField.getName().equals(FField.REF_FIELD_NAME) && focDesc.getProvider() == DBManager.PROVIDER_MYSQL){
	        SQLAlterTable alter = new SQLAlterTable(focDesc, focField, enumer.getFieldCompleteName(focDesc), SQLAlterTable.MODIFY);
	        if(ConfigInfo.isAdaptEnabled()){
						alter.execute();
	      	}else{
	      		alter.buildRequest();
	      		String req = alter.getRequestAdaptedToProvider();
	      		if(req != null) Globals.logString("NOT EXECUTED: " + req);
	      	}
	        //EAntoineS - AUTOINCREMENT
	      }
      }catch(Exception e){
      	Globals.logException(e);
      }
      if (realField != null) {
        actualFields.remove(realField.getName());
      }
    }
    adapted = adapted || actualFields.values().size() > 0;
    for (Enumeration actEnum = actualFields.elements(); actEnum.hasMoreElements();) {
      FField actualField = (FField) actEnum.nextElement();
      SQLAlterTable alter = new SQLAlterTable(focDesc, actualField.getName());
      if(ConfigInfo.isAdaptEnabled()){
	      try{
	      	//We are Dropping Columns and in MSSQL we need to drop the constraints first
	      	if(focDesc.getProvider() == DBManager.PROVIDER_MSSQL){
	      		dropColumnConstrains_MSSQL(focDesc, actualField.getName());
	      	}
	      	alter.execute();
	      }catch(Exception e){
	      	Globals.logException(e);
	      }
    	}else{
    		alter.buildRequest();
    		String req = alter.getRequestAdaptedToProvider();
    		if(req != null) Globals.logString("NOT EXECUTED: " + req);
    	}
    }
    return adapted;
  }
  
  private void adaptTableIndexes(FocDesc focDesc, boolean reindex) {
    if (focDesc != null && focDesc.isPersistent()) {
      SQLTableIndexesDetails indexesDetails = new SQLTableIndexesDetails(focDesc);
            
      Iterator iter = focDesc.indexIterator();
      while(iter != null && iter.hasNext()){
        DBIndex index = (DBIndex) iter.next();
        if(index != null){
          DBIndex realIndex = indexesDetails.getIndex(index.getName());
          try{
          	Iterator<DBIndex> idxIter = indexesDetails.iterator();
          	while(idxIter != null && idxIter.hasNext()){
          		DBIndex indexDebug = idxIter.next();
          		Globals.logString("   - Index : "+indexDebug.getName());
          	}

	          if(realIndex == null){
	            SQLCreateIndex sqlIndex = new SQLCreateIndex(index);
	            sqlIndex.buildRequest();
	            if(ConfigInfo.isAdaptIndexesEnabled()){
	            	sqlIndex.execute();
	            }else{
	            	Globals.logString("NOT EXECUTED: "+sqlIndex.getRequestAdaptedToProvider());
	            }
	          }else if(realIndex.compareTo(index) != 0 || reindex){
	            SQLDropIndex dropIndex = new SQLDropIndex(realIndex);
	            dropIndex.buildRequest();
	            if(ConfigInfo.isAdaptIndexesEnabled()){
	            	dropIndex.execute();
	            }else{
	            	Globals.logString("NOT EXECUTED: "+dropIndex.getRequestAdaptedToProvider());
	            }
	
	            SQLCreateIndex createIndex = new SQLCreateIndex(index);
	            createIndex.buildRequest();
	            if(ConfigInfo.isAdaptIndexesEnabled()){
	            	createIndex.execute();
	            }else{
	            	Globals.logString("NOT EXECUTED: "+createIndex.getRequestAdaptedToProvider());
	            }
	          }
          }catch(Exception e){
          	Globals.logException(e);
          	Globals.logString("Creating the Index");
          }
          if(realIndex != null){
            indexesDetails.removeIndex(realIndex.getName());
          }
        }
      }
      
      if(ConfigInfo.isRemoveUndeclaredIndexesDuringAdaptDataModel()){
	      iter = indexesDetails.iterator();
	      while(iter != null && iter.hasNext()){
	        DBIndex indexToRemove = (DBIndex) iter.next();
	        
	        SQLDropIndex dropIndex = new SQLDropIndex(indexToRemove);
	        dropIndex.buildRequest();
	        try{
	        	dropIndex.execute();
	        }catch(Exception e){
	        	Globals.logException(e);
	        }
	      }
      }
    }
  }
  
  private boolean adaptTable_Internal(FocDesc focDesc){
  	boolean altered = false;
    if(focDesc != null && focDesc.isPersistent() && focDesc.isAllowAdaptDataModel()){
      String dbTableName = focDesc.getStorageName_ForSQL();
      if (dbTableName != null && DBManagerServer.getInstance() != null) {
      	
      	String dbSourceKey = focDesc.getDbSourceKey();
      	Hashtable<String, String> allTablesForThisConnection = actualTables_getTablesForOneConnection(dbSourceKey);
      	if(allTablesForThisConnection != null){
	      	
	        if (allTablesForThisConnection.get(dbTableName) == null) {
	        	//Logging the TableNames
	        	/*
		      	Iterator iter = allTablesForThisConnection.values().iterator();
		      	while(iter != null && iter.hasNext()){
		      		String str = (String)iter.next();
		      		Globals.logString("Table : "+str);
		      	}
		      	*/

	        	focDesc.beforeAdaptTableModel(false);
	          if(focDesc.isDbResident()){
	            Globals.getDBManager();
	            SQLCreateTable create = new SQLCreateTable(focDesc);
	            create.buildRequest();
		          if(ConfigInfo.isAdaptEnabled()){
		            try{
		            	create.execute();
		            }catch(Exception e){
		            	Globals.logException(e);
		            }
	            }else{
	            	create.buildRequest();
	  	      		String req = create.getRequestAdaptedToProvider();
	  	      		if(req != null) Globals.logString("NOT EXECUTED: " + req);
	            }
	          }
	          altered = true;
	        } else {
	        	focDesc.beforeAdaptTableModel(true);
	          altered = adaptDBTableFields(focDesc);
	        }
	        focDesc.afterAdaptTableModel();
	        getExeTables().put(dbTableName, dbTableName);
//	        allTablesForThisConnection.remove(dbTableName);
      	}
      }
    }
  	
    return altered;
  }

	public boolean isAlterAllFields() {
		return alterAllFields;
	}
	
	public String writeCode_FromDataModel(){
		String error = null;
		
		Hashtable allTables = actualTables_fillForOneConnection(DBManagerServer.getInstance().getConnectionPool(null));
		Iterator tableNames = allTables.values().iterator();
		while(tableNames != null && tableNames.hasNext()){
			String tableName = (String) tableNames.next();
			String nameToSearch = tableName.toUpperCase(); 

			FocDesc focDesc = Globals.getApp().getFocDescByName(nameToSearch);
			if(/*focDesc == null ||*/tableName.equals("Country")){ 
				focDesc = new FocDesc(FocObject.class, FocDesc.DB_RESIDENT, tableName, false);
				SQLTableDetails table = new SQLTableDetails(focDesc, true);//We are also getting the foreign keys
				table.buildRequest();
				table.execute();
				
				Hashtable<String, FField> actualFields = table.getFieldsHashtable();
				Hashtable<String, String> foreignKeys  = table.getForeignKeys();
				Iterator iter = actualFields.values().iterator();
				int fieldID = 1;
				while(iter.hasNext()){
					FField realFld = (FField) iter.next();
					
					if(realFld.isAutoIncrement()){//REF Field
						FField fld = focDesc.addReferenceField();
						fld.setName(realFld.getDBName());
					}else{
						realFld.setId(fieldID);
						focDesc.addField(realFld);
						fieldID++;
					}
				}
				
				TableDefinition tableDef = TableDefinition.getTableDefinitionForFocDesc(focDesc, foreignKeys);
				tableDef.setPackageName_ServerSide("siren.isf.fenix");
				
				FabProject   project   = new FabProject(new FocConstructor(FabProjectDesc.getInstance(), null));
				FabWorkspace workspace = new FabWorkspace(new FocConstructor(FabWorkspaceDesc.getInstance(), null));
				
				workspace.setWorkspacePath("f:/eclipseworkspace_everpro");
				project.setWorkspace(workspace);
				project.setProjectPath("fenix/src");
				tableDef.setProject(project);
				tableDef.setPackageName_ServerSide("siren.isf.fenix.station");
				
				tableDef.setName(tableName);
				tableDef.setClassName(tableName);
				tableDef.setCW_ClassName_FocObject(tableName);
				
				tableDef.writeCode_ServerCode();
			}
		}
		
		return error;
	}
}
