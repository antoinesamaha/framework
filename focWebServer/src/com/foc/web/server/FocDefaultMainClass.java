package com.foc.web.server;

import com.foc.FocMainClass;

public class FocDefaultMainClass extends FocMainClass {
	
	private FocDefaultWebServer focDefaultWebServer = null;

	public FocDefaultMainClass(FocDefaultWebServer focDefaultWebServer, String[] args) {
		super(args);
		if(focDefaultWebServer != null) focDefaultWebServer.modules();
	}

	public FocDefaultWebServer getFocWebServer() {
		return focDefaultWebServer;
	}

	public void setFocWebServer(FocDefaultWebServer focDefaultWebServer) {
		this.focDefaultWebServer = focDefaultWebServer;
	}
}