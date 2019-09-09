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
package com.foc.vaadin.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Element;

import com.foc.Globals;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;

@SuppressWarnings("serial")
public class FocVaadinServlet extends VaadinServlet {

	protected boolean hasAjaxWebCrawlerSupport(){
		return false;
	}
	
	protected String getFullURL(HttpServletRequest request) {
    StringBuffer requestURL = request.getRequestURL();
    String queryString = request.getQueryString();

    if (queryString == null) {
        return requestURL.toString();
    } else {
        return requestURL.append('?').append(queryString).toString();
    }
  }
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
//			Globals.logString("FOC VAADIN    IN    Service");
				super.service(request, response);
//			Globals.logString("FOC VAADIN    OUT   Service");
		}catch (Exception e){
			boolean withPopup = true;
			
			StackTraceElement[] stack = e.getStackTrace();
			if(stack != null && stack.length > 0){
				StackTraceElement element = stack[stack.length-1];
				if(element != null && element.getClassName() != null && element.getClassName().contains("com.vaadin.data.RpcDataProviderExtension")){
					withPopup = false;
				}
			}
			if(withPopup){
				Globals.logException(e);
			}else{
				Globals.logExceptionWithoutPopup(e);
			}
		}
	}
	
	@Override
	protected VaadinServletRequest createVaadinRequest(HttpServletRequest request) {
		return new FocVaadinServletRequest(request, getService());
	}

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().addSessionInitListener(new SessionInitListener() {

			@Override
			public void sessionInit(SessionInitEvent event) {
				event.getSession().addBootstrapListener(new BootstrapListener() {

					@Override
					public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
					}

					@Override
					public void modifyBootstrapPage(BootstrapPageResponse response) {
						response.getDocument().head().prependElement("meta").attr("name", "viewport").attr("content", "width=device-width");
						String robotKeywords = getRobotKeywords();
						if(robotKeywords != null && !robotKeywords.isEmpty()){
							response.getDocument().head().prependElement("meta").attr("name", "robots").attr("content", robotKeywords);;//robots" content="..., ..." /
						}
						if(hasAjaxWebCrawlerSupport()){
							Element elmt = response.getDocument().head().prependElement("meta");
							elmt.attr("name", "fragment");
							elmt.attr("content", "!");
						}
					}
				});
			}
		});
	}
	
	protected String getRobotKeywords(){
		return null;
	}

	// @Override
	// protected void writeAjaxPageHtmlHeader(BufferedWriter page, String title,
	// String themeUri) throws IOException {
	// super.writeAjaxPageHtmlHeader(page, title, themeUri);
	// page.append("<meta name=\"viewport\" content=\"width=320; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\">");
	// }
}
