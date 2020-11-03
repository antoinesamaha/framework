package com.foc.web.microservice.consumer;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.db.IDBReloader;
import com.foc.util.Utils;

public class Cache_DBReloader implements IDBReloader {

	private boolean active = false;
	private String  url    = null;
	private HashMap<String, String> tablesToSend = null;
	
	private long       waitDuration   = 30 * 1000;
	private CallThread awaitingThread = null;

	public Cache_DBReloader() {
		tablesToSend = new HashMap<String, String>();
		
		url = ConfigInfo.getProperty("reloader.url");
		if (!Utils.isStringEmpty(url)) {
			String activeTxt = ConfigInfo.getProperty("reloader.active");
			if(			!Utils.isStringEmpty(activeTxt) 
					&& (activeTxt.equals("1") || activeTxt.toLowerCase().equals("true"))){
				active = true;
			}
		}
	}
	
	@Override
	public synchronized void reloadTable(String tableName) {
		tablesToSend.put(tableName, tableName);
		if (awaitingThread == null) {
			awaitingThread = new CallThread();
			awaitingThread.start();
		}
	}
	
	protected synchronized void disposeAwaitingThread() {
		awaitingThread = null;
	}

	private class CallThread extends Thread {
		
		private CallThread() {
		}
		
		@Override
		public void run() {
			try{
				Thread.sleep(waitDuration);

				if(active && Utils.isStringEmpty(url)) {
					HttpGet someHttpGet = new HttpGet(url);
					URIBuilder uriBuilder = new URIBuilder(someHttpGet.getURI());
					Iterator<String> iter = tablesToSend.keySet().iterator();
					while (iter != null && iter.hasNext()) {
						uriBuilder.addParameter("table_name", iter.next());
					}
					
					URI uri = uriBuilder.build();
					someHttpGet.setURI(uri);
					HttpClient client = HttpClientBuilder.create().build();
					HttpResponse response = client.execute(someHttpGet);
					if(response != null) {
						if(response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
							Globals.logString("Lists refresh failed for table");
						}
					}
				}
				disposeAwaitingThread();
				
			}catch (Exception e){
				Globals.logException(e);
			}
		}
	}
}
