package com.foc.web.microservice.consumer;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.admin.FocUserHistoryDesc;
import com.foc.db.IDBReloader;
import com.foc.desc.FocObject;
import com.foc.util.Utils;

public class Cache_DBReloader implements IDBReloader {

	private boolean active = false;
	private String  url    = null;
	private HashMap<String, String> excludedTables = null;
	private HashMap<String, ArrayList<Long>> tablesToSend = null;
  public static final int TYPE_UPDATE =  2;
	
	private long       waitDuration   = 30 * 1000;
	private CallThread awaitingThread = null;

	public Cache_DBReloader() {
		tablesToSend = new HashMap<String, ArrayList<Long>>();
		url = ConfigInfo.getProperty("reloader.url");
		if (!Utils.isStringEmpty(url)) {
			String activeTxt = ConfigInfo.getProperty("reloader.active");
			if(			!Utils.isStringEmpty(activeTxt) 
					&& (activeTxt.equals("1") || activeTxt.toLowerCase().equals("true"))){
				active = true;
			}
		}
		excludedTables_Fill();
	}
	
	protected void excludedTables_Fill() {
		excludedTables_Put(FocUserHistoryDesc.DB_TABLE_NAME);
	}
	
	public void excludedTables_Put(String tablename) {
		if (excludedTables == null) {
			excludedTables = new HashMap<String, String>();
		}
		excludedTables.put(tablename, tablename);
	}

	public boolean excludedTables_IsExcluded(String tablename) {
		return excludedTables != null && excludedTables.containsKey(tablename);
	}
	
	@Override
	public synchronized void reloadTable(FocObject obj, int action) {
		if(obj != null && obj.getThisFocDesc() != null && obj.getThisFocDesc().isListInCache() && active) {
			if(action == TYPE_UPDATE) {
				if(tablesToSend.containsKey(obj.getThisFocDesc().getName())) {
					ArrayList<Long> list = tablesToSend.get(obj.getThisFocDesc().getName());
					if(list != null && !list.contains(obj.getReferenceInt())) list.add(obj.getReferenceInt());
				} else {
					ArrayList<Long> list = new ArrayList<Long>();
					list.add(obj.getReferenceInt());
					tablesToSend.put(obj.getThisFocDesc().getName(), list);
				}
			} else {
				tablesToSend.put(obj.getThisFocDesc().getName(), null);
			}			
			if (awaitingThread == null) {
				awaitingThread = new CallThread();
				awaitingThread.start();
			}
		}
	}
	
	protected synchronized void disposeAwaitingThread() {
		awaitingThread = null;
	}
	
	public synchronized JSONObject constructReloadJsonBody() throws JSONException {
		JSONObject json = new JSONObject();
		Iterator<String> iter = tablesToSend.keySet().iterator();
		ArrayList<JSONObject> jsonOjectList = new ArrayList<JSONObject>();
		while (iter != null && iter.hasNext()) {
			String tableName = iter.next();
			JSONObject innerJson = new JSONObject();
			innerJson.put("table_name", tableName);
			ArrayList<Long> list = tablesToSend.get(tableName);
			if(list != null) {
				innerJson.put("reload_all", false);
				innerJson.put("object_refs", list);
			} else {
				innerJson.put("reload_all", true);
			}
			jsonOjectList.add(innerJson);
		}
		tablesToSend.clear();
		json.put("tables", jsonOjectList);
		return json;
	}

	private class CallThread extends Thread {
		
		private CallThread() {
			
		}
		
		@Override
		public void run() {
			try{
				Thread.sleep(waitDuration);
				if(active && !Utils.isStringEmpty(url)) {
					HttpPost someHttpPost = new HttpPost(url);
					URIBuilder uriBuilder = new URIBuilder(someHttpPost.getURI());
					JSONObject json = constructReloadJsonBody();
					if(json != null) {
						Globals.logString("Call body is " +json.toString());
						StringEntity strEntity = new StringEntity(json.toString());
						strEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            someHttpPost.setEntity(strEntity);
					}
					URI uri = uriBuilder.build();
					someHttpPost.setURI(uri);		
//					String authHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJGT0NBRE1JTiIsImZ1bGxfbmFtZSI6IiIsImlzcyI6ImF1dGgwIiwiZXhwIjoxNjA0NzM5MTg3LCJpYXQiOjE2MDQ2NTI3ODd9.Ci3WQ75Umzs6w-vMJ8FWhNYC_IARBbYoxJgroPXGLU0"; // + new String(encodedAuth);
//					someHttpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
					HttpClient client = HttpClientBuilder.create().build();
					HttpResponse response = client.execute(someHttpPost);
					if(response != null) {
						if(response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
							Globals.logString("Lists refresh failed for table");
						}
					}
				}
				disposeAwaitingThread();
			}catch (Exception e){
				Globals.logException(e);
				disposeAwaitingThread();
			}
		}
	}
}
