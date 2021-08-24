
package com.foc.web.microservice.servlets;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.shared.json.B01JsonBuilder;
import com.foc.util.Utils;
import com.foc.web.microservice.FocRestAPIServer;
import com.foc.web.microservice.FocServletRequest;
import com.foc.web.microservice.IFocRestAPIServer;
import com.foc.web.microservice.entity.OpenGetServlet;

@SuppressWarnings("serial")
public class HealthCheckServlet extends OpenGetServlet { 

	private static MetricsComputer metrics = null;
	
	@Override
	public String getNameInPlural() {
		return "curfew_check";
	}

	private String lastReceivedSms(String provider) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis() - 30 * 60 * 1000);
		Date date = (Date) cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = sdf.format(date);
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT MAX(\"DateTime\") from \"SmsReceived\" where \"Provider\" = '"+provider+"' and \"SmsServerID\"=1 and \"DateTime\">'"+dateStr+"'");
		ArrayList<String> values = Globals.getApp().getDataSource().command_SelectRequest(buffer);
		
		return values.get(0);
	}
	
	@Override
	public void doGet_Core(FocServletRequest focRequest) throws Exception {
		HttpServletResponse response = focRequest.getResponse();
		if (response != null) {
			response.setStatus(HttpServletResponse.SC_OK);
			setCORS(response);
			
			if(metrics == null) {
				metrics = new MetricsComputer();
				Thread t = new Thread(metrics);
				t.start();
			}
			
			String result = "";
			
			HttpServletRequest req = focRequest.getRequest();
			String path = req.getContextPath();
			if (path.contains("json")) {
				B01JsonBuilder builder = new B01JsonBuilder();				
				
				builder.beginObject();
				
				builder.beginList();
				for (int i=0; i<MetricsComputer.MAX; i++) {
					builder.beginObject();
					builder.appendKeyValue("index", i);
					builder.appendKeyValue("threads", metrics.threadsArray[i]);
					builder.appendKeyValue("idle", metrics.idleArray[i]);
//					builder.appendKeyValue("sms_send_queue", metrics.smsQueue[i]);
					builder.endObject();				
				}
				builder.endList();

//				builder.appendKeyValue("last_reception_mtc", lastReceivedSms("MTC"));
//				builder.appendKeyValue("last_reception_mtc", lastReceivedSms("Alfa"));

				builder.endObject();

				result = builder.toString();
				
			} else {
				result = "<html><body>";
				
				result += "<b>Threads:</b> "+metrics.threadsArray[MetricsComputer.MAX-1]+"<br>";

				Globals.getDB
				result += "<b>DB Connections:</b> "+metrics.threadsArray[MetricsComputer.MAX-1]+"<br>";
				
				
				result += "<b>DB Connections:</b> "+metrics.smsQueue[MetricsComputer.MAX-1]+"<br>";
//				result += "<b>Last MTC message received:</b> "+lastReceivedSms("MTC")+"<br>";
//				result += "<b>Last Alfa message received:</b> "+lastReceivedSms("Alfa")+"<br>";
				
				result += "</body></html>";
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
			}
			
			response.getWriter().println(result);
			
			Globals.logString("  = Returned: "+result);				
		}
	}

	@Override
	public void afterPost(FocServletRequest focServletRequest, FocObject focObj, boolean created) {
	}
	
	public class MetricsComputer implements Runnable {

		public static final int MAX = 60;
		
		//Jetty threads metrics
		public int   maxThreads   = 0;
		public int   minThreads   = 0;
		public int[] threadsArray = new int[60]; 
		public int[] idleArray    = new int[60]; 
		
		@Override
		public void run() {
			while (true) {
				try {
					FocRestAPIServer server = FocRestAPIServer.getInstance();

					for(int i=0; i<MAX-1; i++) {
						threadsArray[i] = threadsArray[i+1];
						idleArray[i] = idleArray[i+1];
					}
					threadsArray[MAX-1] = server != null ? server.getThreads() : 0;
					idleArray[MAX-1] = server != null ? server.getIdleThreads() : 0;

					maxThreads = server != null ? server.getMaxThreads() : 0;
					minThreads = server != null ? server.getMinThreads() : 0;

					Thread.sleep(10 * 1000);
				} catch (Exception e) {
					Globals.logException(e);
				}
			}
		}
		
	}
}
