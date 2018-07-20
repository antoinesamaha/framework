package com.foc.vaadin.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.foc.web.server.FocWebServer;

@WebListener
public class FocVaadinServletContextListener implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ServletContextListener destroyed");
		
		FocWebServer server = FocWebServer.getInstance();
		if(server != null) {
			server.dispose();
			server = null;
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("ServletContextListener started");	
	}
	
}