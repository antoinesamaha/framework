package com.foc.business.workflow.implementation;

import java.sql.Date;
import java.util.Comparator;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.access.FocDataMap;
import com.foc.admin.FocUser;
import com.foc.business.notifier.FocNotificationConst;
import com.foc.business.notifier.FocNotificationEvent;
import com.foc.business.notifier.FocNotificationManager;
import com.foc.db.DBManager;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FDateTimeField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FListField;
import com.foc.list.FocList;
import com.foc.list.FocListElement;
import com.foc.log.HashedDocument;
import com.foc.log.IFocLogLastHash;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.serializer.FSerializer;
import com.foc.serializer.FSerializerDictionary;
import com.foc.util.Encryptor;
import com.foc.util.Utils;

public class Loggable {
	private ILoggable iLoggable = null;

	public Loggable(ILoggable focObject){
		iLoggable = focObject;
	}

	public void dispose(){
		iLoggable = null;
	}
	
	public ILoggable getILoggable() {
		return iLoggable;
	}
	
	public FocObject getFocObject(){
		return (FocObject) iLoggable;
	}
	
	public FocDesc getFocDesc(){
		FocObject focObj = getFocObject();
		return focObj.getThisFocDesc();
	}
	
	public ILoggableDesc getILoggableDesc(){
		return (ILoggableDesc) getFocDesc();
	}
	
	public LoggableDesc getLoggableDesc(){
		return getILoggableDesc().iWorkflow_getWorkflowDesc();
	}
	
	public FocDesc getWFLogDesc() {
		FocDesc logFocDesc = null;
		LoggableDesc loggableDesc = getLoggableDesc();
		if(loggableDesc != null && getFocDesc() != null){
			FListField listField = (FListField) getFocDesc().getFieldByID(loggableDesc.getFieldID_LogList());
			if(listField != null) {
				logFocDesc = listField.getFocDesc();
			}
		}
		return logFocDesc;
	}

	public FocList getLogList(){
		return getLogList(false);
	}
	
	public FocList getLogList(boolean forceReload){
		FocList list = null;
		LoggableDesc loggableDesc = getLoggableDesc();
		if(loggableDesc != null && getFocObject() != null){
			list = (FocList) getFocObject().getPropertyList(loggableDesc.getFieldID_LogList());
			if(list != null){
				list.setDirectlyEditable(false);
				list.setDirectImpactOnDatabase(true);
				if(list.getOrderComparator() == null){
					list.setOrderComparator(new Comparator<FocListElement>(){
						@Override public int compare(FocListElement e1, FocListElement e2){
							WFLog o1 = (WFLog) e1.getFocObject();
							WFLog o2 = (WFLog) e2.getFocObject();
							long l = o1.getDateTime().getTime() - o2.getDateTime().getTime();
							int ret = 0;
							if(l < 0) ret = -1;
							if(l > 0) ret =  1;
							return ret;
						}
					});
				}
				if(forceReload) list.reloadFromDB();			
			}
		}
		return list;
	}

	public FocUser getLastModifUser(){
		LoggableDesc workflowDesc = getLoggableDesc();
		return (FocUser) getFocObject().getPropertyObject(workflowDesc.getFieldID_LastModificationUser());
	}
	
	public void setLastModifUser(FocUser user){
		LoggableDesc workflowDesc = getLoggableDesc();
		getFocObject().setPropertyObject(workflowDesc.getFieldID_LastModificationUser(), user);
	}
	
	public void setLastModifUserRef(long ref){
		LoggableDesc workflowDesc = getLoggableDesc();
		FObject objProp = (FObject) getFocObject().getFocProperty(workflowDesc.getFieldID_LastModificationUser());
		objProp.setLocalReferenceInt(ref);
	}
	
	public Date getLastModifDate(){
		LoggableDesc workflowDesc = getLoggableDesc();
		return getFocObject().getPropertyDate(workflowDesc.getFieldID_LastModificationDate());
	}

	public void setLastModifDate(Date date){
		LoggableDesc workflowDesc = getLoggableDesc();
		getFocObject().setPropertyDate_WithoutListeners(workflowDesc.getFieldID_LastModificationDate(), date);
	}

	public String getLastModifDateSQLString(){
		LoggableDesc workflowDesc = getLoggableDesc();
		FProperty prop = getFocObject().getFocProperty(workflowDesc.getFieldID_LastModificationDate());
		return prop != null ? prop.getSqlString() : null;
	}

	public void updateLastModified(FocUser user, Date dateTime) {
		if(getLoggableDesc() != null && getFocObject() != null) {
			long ref = getFocObject().getReferenceInt();
			if(ref > 0) {
				FocDesc focDesc = getFocDesc();
				long userRef = user != null ? user.getReferenceInt() : 0;
				
				setLastModifDate(dateTime);
				setLastModifUserRef(userRef);
	
				FDateTimeField lastModifDateFld = (FDateTimeField) focDesc.getFieldByID(getLoggableDesc().getFieldID_LastModificationDate());
				FField lastModifUserFld = focDesc.getFieldByID(getLoggableDesc().getFieldID_LastModificationUser());
				
				if(lastModifDateFld != null && lastModifUserFld != null) {
					StringBuffer buffer = null;
					if(focDesc.getProvider() == DBManager.PROVIDER_MYSQL) {
						buffer = new StringBuffer("UPDATE " + focDesc.getStorageName_ForSQL() + " ");
						buffer.append("set "+lastModifUserFld.getDBName()+" = "+userRef+" ");
						buffer.append(", "+lastModifDateFld.getDBName()+" = "+getLastModifDateSQLString()+" ");
						buffer.append(" where "+focDesc.getRefFieldName()+" = "+ref+" ");						
					} else {
						buffer = new StringBuffer("UPDATE \"" + focDesc.getStorageName_ForSQL() + "\" ");
						buffer.append("set \""+lastModifUserFld.getDBName()+"\" = "+userRef+" ");
						buffer.append(", \""+lastModifDateFld.getDBName()+"\" = "+getLastModifDateSQLString()+" ");
						buffer.append(" where \""+focDesc.getRefFieldName()+"\" = "+ref+" ");
					}					
					Globals.getApp().getDataSource().command_ExecuteRequest(buffer);
				}
			}else {
				setLastModifDate(dateTime);
				setLastModifUser(user);
			}
		}
	}
	
	protected void fillLogLine(WFLog log, int event){
		if(log != null){
			log.setEventType(event);
			
			if(log != null){
				log.setUser(Globals.getApp().getUser_ForThisSession());
				if(log.getUser() == null){
				}
				log.setDateTime(Globals.getDBManager().getCurrentTimeStamp_AsTime());
			}

			if(event == WFLogDesc.EVENT_MODIFICATION || event == WFLogDesc.EVENT_CREATION) {
				updateLastModified(log.getUser(), log.getDateTime());
			}
		}
	}

	public void addLogLine(int event){
		//setArea(workflow.iWorkflow_getComputedSite());
		FocList focList = getLogList();
		if(focList != null){
			WFLog   log     = (WFLog) focList.newEmptyItem();
			fillLogLine(log, event);
		}
	}
	
//	public void addLogLine(){
//		int event = WFLogDesc.EVENT_NONE;
//		if(getFocObject().isCreated()){
//			event = WFLogDesc.EVENT_CREATION;
//		}else if(getFocObject().isModified()) {// && !isCanceled()){
//			event = WFLogDesc.EVENT_MODIFICATION;
//		}
//		addLogLine(event);
//	}

	public long insertLogLine(int event) {
		return insertLogLine(event, null);
	}
		
	public long insertLogLine(int event, String comment) {//, String sqlRequest
		return insertLogLine(event, comment, null);
	}
	
	public long insertLogLine(int event, String comment, String changes) {//, String sqlRequest
		long ref = 0;
		FocDesc logFocDesc = getWFLogDesc();
		FocObject focObj = getFocObject();
		if(logFocDesc != null && focObj != null && focObj.hasRealReference()) {
			FocConstructor constr = new FocConstructor(logFocDesc, null); 
			WFLog log = (WFLog) constr.newItem();
			if(log != null) {
				log.setCreated(true);
				log.setLogSubjectReference(focObj.getReferenceInt());
				fillLogLine(log, event);
				if(!Utils.isStringEmpty(comment)) log.setComment(comment);
				if(!Utils.isStringEmpty(changes)) log.setChanges(changes);
				
				//Preparing the JSON with the latest version 
				StringBuffer buff = new StringBuffer();
				FSerializer ser = FSerializerDictionary.getInstance().newSerializer(focObj, buff, FSerializer.TYPE_JSON);
				if(ser != null) {
					ser.serializeToBuffer();
					String fullJson = buff.toString();
					if(!Utils.isStringEmpty(fullJson)) {
						log.setDocZip(fullJson);
						log.setDocVersion(ser.getVersion());
						log.setDocHash(Encryptor.encrypt_MD5(fullJson));
					}
				}
				
				//If the Event is open we check with the last HASH 
				if(log.getEventType() == WFLogDesc.EVENT_OPENED) {
					fetchLastDocHashForChecking(new LastHashHandler(log.getDocZip(), log.getDocHash(), log.getDocVersion()));
			  }
				
				log.validate(false);
				ref = log.getReferenceInt();
				if(Globals.getApp() != null) {
					log.setObjectLogged(focObj);
					Globals.getApp().logListenerNotification(log);
				}
				
				log.dispose();
			}
		} else {
			Globals.logString("Internal Exception: Could not insert log line");
		}
		return ref;
	}
	
	private void fetchLastDocHashForChecking(IFocLogLastHash lastHashHandler) {
		FocObject focObj = getFocObject();
		if(focObj != null && focObj.getThisFocDesc() != null && focObj.getReferenceInt() > 0) {
			Globals.getApp().logListenerGetLastHashedDocument(focObj.getThisFocDesc().getStorageName(), focObj.getReferenceInt(), lastHashHandler);
		}
	}
	
	public class LastHashHandler implements IFocLogLastHash {
		private String fullDocComputed = null;
		private String hashComputed = null;
		private int versionOfComputed = 0; 
		
		public LastHashHandler(String fullDocComputed, String hashComputed, int versionOfComputed) {
			this.fullDocComputed = fullDocComputed;
			this.hashComputed = hashComputed;
			this.versionOfComputed = versionOfComputed;
		}
		
		@Override
		public void lastLog(HashedDocument lastHashedDoc) {
			FocObject focObj = getFocObject();
			if(lastHashedDoc == null) {
				//If lastHashedDoc == null we compute from the log tables in FOC
				FocList list = getLogList(false);
				if(list != null) {
					list.loadIfNotLoadedFromDB();
					if(list.size() > 0) {
						WFLog log = (WFLog) list.getFocObject(list.size()-1);
						if(log != null && log.getDocVersion() > 0 && !Utils.isStringEmpty(log.getDocZip())) {
							lastHashedDoc = new HashedDocument();
							lastHashedDoc.setDocument(log.getDocZip());
							lastHashedDoc.setVersion(log.getDocVersion());
							lastHashedDoc.setHash(log.getDocHash());
						}
					}
				}
			}

			//If lastHashedDoc is null this means we will not check anything
			if(lastHashedDoc != null) {
				//If the last saved version is lower than the new computed one we need to compute another 
				if(lastHashedDoc.getVersion() != versionOfComputed && lastHashedDoc.getVersion() > 0) {
					StringBuffer buff = new StringBuffer();
					FSerializer ser = FSerializerDictionary.getInstance().newSerializer(focObj, buff, FSerializer.TYPE_JSON, lastHashedDoc.getVersion());
					if(ser != null) {
						ser.serializeToBuffer();
						fullDocComputed = buff.toString();
						if(!Utils.isStringEmpty(fullDocComputed)) {
							hashComputed = Encryptor.encrypt_MD5(fullDocComputed);
							versionOfComputed = lastHashedDoc.getVersion();
						}
					}
				}
				if(!hashComputed.equals(lastHashedDoc.getHash())){
					notifyForHashDiscrepancy(focObj, fullDocComputed, lastHashedDoc.getDocument(), 
							versionOfComputed, lastHashedDoc.getVersion(), 
							hashComputed, lastHashedDoc.getHash());
				}
			}		
		}
	}
	
	public static void notifyForHashDiscrepancy(FocObject focObj, String computedDoc, String storedDoc, int computedVersion, int storedVersion, String computedHash, String storedHash) {
	  FocDataMap focDataMap = new FocDataMap(focObj);
		focDataMap.putString("TABLE_NAME", focObj.getThisFocDesc().getStorageName());
		focDataMap.putString("COMPUTED_DOC", computedDoc);
		focDataMap.putString("EXPECTED_DOC", storedDoc);
		focDataMap.putString("COMPUTED_VERSION", String.valueOf(computedVersion));
		focDataMap.putString("EXPECTED_VERSION", String.valueOf(storedVersion));
		focDataMap.putString("COMPUTED_HASH", computedHash);
		focDataMap.putString("EXPECTED_HASH", storedHash);
	  FocNotificationManager.getInstance().fireEvent(new FocNotificationEvent(FocNotificationConst.EVT_DOC_HASH_MISSMATCH, focDataMap));
		Globals.showNotification("Illegal document modification detected", "This document is suspected of being tempered with", IFocEnvironment.TYPE_ERROR_MESSAGE);
	}
}
