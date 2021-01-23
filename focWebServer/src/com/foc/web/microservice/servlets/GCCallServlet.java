package com.foc.web.microservice.servlets;

import java.io.IOException;
import java.text.NumberFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.foc.Globals;
import com.foc.web.microservice.entity.FocSimpleMicroServlet;

public class GCCallServlet extends FocSimpleMicroServlet {

	public String getCustomHtmlSection() {
		return "";
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		extractUIClassname(request);
		SessionAndApplication sessionAndApp = pushSession(request, response);
		Globals.logString(" => GET Begin GCCallServlet /gc");

		NumberFormat numFmt = NumberFormat.getNumberInstance();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>");
		buffer.append("<body>");
		buffer.append("<h1>Garbage collector launched</h1>");
		
    long total1 = Runtime.getRuntime().totalMemory();
    long free1 = Runtime.getRuntime().freeMemory();
    long used1 = total1 - free1;
    System.gc();
    System.gc();
    long total2 = Runtime.getRuntime().totalMemory();
    long free2 = Runtime.getRuntime().freeMemory();
    long used2 = total2 - free2;
    
		buffer.append("<table cellspacing=\"10\">");
		
		buffer.append("<tr>");
		buffer.append("  <th> </th>");
		buffer.append("  <th>Total</th>");
		buffer.append("  <th>Free</th>");
		buffer.append("  <th>Used</th>");
		buffer.append("</tr>");
    
		buffer.append("<tr>");
		buffer.append("  <td>Before GC</td>");
		buffer.append("  <td>"+numFmt.format(total1)+"</td>");
		buffer.append("  <td>"+numFmt.format(free1)+"</td>");
		buffer.append("  <td>"+numFmt.format(used1)+"</td>");
		buffer.append("</tr>");

		buffer.append("<tr>");
		buffer.append("  <td>After GC</td>");
		buffer.append("  <td>"+numFmt.format(total2)+"</td>");
		buffer.append("  <td>"+numFmt.format(free2)+"</td>");
		buffer.append("  <td><b>"+numFmt.format(used2)+"<b></td>");
		buffer.append("</tr>");		
		
		buffer.append("</table>");

//		Globals.getApp()
//		
//		FenixCovidAPIServer server = FenixCovidAPIServer.getInstance();
//		if (server != null) {
//			buffer.append(server.getHtml());
//		}

		String cachedParam = request.getParameter("cached");
		boolean includeCached = cachedParam != null && cachedParam.equals("true") ? true : false;
		String htmlMemoryDump = Globals.getApp().dumpLivingFocObjectCounts(false, includeCached, true);
		buffer.append(htmlMemoryDump);

		String cutomHtml = getCustomHtmlSection();
		buffer.append(cutomHtml);
		
		buffer.append("</body>");
		buffer.append("</html>");
		
		//response.setContentType("application/json");
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(buffer.toString());
		
		if(sessionAndApp != null){
			sessionAndApp.logout();
		}
		Globals.logString(" <= GET End GCCallServlet /gc");
	}

}
