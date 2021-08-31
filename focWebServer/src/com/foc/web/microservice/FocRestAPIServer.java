package com.foc.web.microservice;

public class FocRestAPIServer {
/*
	public static boolean noJWT = false;
	private static FocRestAPIServer server = null;
	private Server jettyServer = null;

	public static FocRestAPIServer getInstance() {
		return server;
	}
	
	public static void setInstance(FocRestAPIServer restApiServer) {
		server = restApiServer;
	}

	public void addServlets(ServletContextHandler contextHandler) {
		contextHandler.addServlet(WSLookupServlet.class, "/lookups");
		contextHandler.addServlet(ReloadServlet.class, "/reload");
	}

	private int port = 0;
	private int maxThread = 0;
	private String certificateAlias = null;
	private String firstCallService = "startup";
	private String logDir = null;

	public void start() throws Exception {
		initJsonFieldFilter();
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.setSessionHandler(new SessionHandler());
		addServlets(contextHandler);
		HandlerCollection handlers = new HandlerCollection();
		NCSARequestLog requestLog = new NCSARequestLog();
		requestLog.setFilename(getLogDir() + "/yyyy_mm_dd.request.log");
		requestLog.setFilenameDateFormat("yyyy_MM_dd");
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogCookies(false);
		requestLog.setLogTimeZone(TimeZone.getDefault().getID());
		RequestLogHandler requestLogHandler = new RequestLogHandler();
		requestLogHandler.setRequestLog(requestLog);
		handlers.addHandler(contextHandler);
		handlers.addHandler(requestLogHandler);
		ThreadPool queuedThreadPool = new QueuedThreadPool(getMaxThread());
		jettyServer = new Server(queuedThreadPool);
		jettyServer.setHandler(handlers);
		if (!Utils.isStringEmpty(certificateAlias)) {
			configureSSL(jettyServer);
		} else {
			ServerConnector connector = new ServerConnector(jettyServer);
			connector.setPort(port);
			jettyServer.setConnectors(new Connector[] { connector });
		}
		jettyServer.start();
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					firstCall();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();
		jettyServer.join();
	}

	public int getMinThreads() {
		QueuedThreadPool threadPool = jettyServer != null ? (QueuedThreadPool) jettyServer.getThreadPool() : null;
		return threadPool != null ? threadPool.getMinThreads() : 0;
	}

	public int getMaxThreads() {
		QueuedThreadPool threadPool = jettyServer != null ? (QueuedThreadPool) jettyServer.getThreadPool() : null;
		return threadPool != null ? threadPool.getMaxThreads() : 0;
	}

	public int getIdleThreads() {
		QueuedThreadPool threadPool = jettyServer != null ? (QueuedThreadPool) jettyServer.getThreadPool() : null;
		return threadPool != null ? threadPool.getIdleThreads() : 0;
	}

	public int getThreads() {
		QueuedThreadPool threadPool = jettyServer != null ? (QueuedThreadPool) jettyServer.getThreadPool() : null;
		return threadPool != null ? threadPool.getThreads() : 0;
	}

	public String getHtml() {
		QueuedThreadPool threadPool = jettyServer != null ? (QueuedThreadPool) jettyServer.getThreadPool() : null;
		String str = "";
		if (threadPool != null) {
			str = "<h3>";
			str += " State: " + threadPool.getState() + "<br>";
			str += " Threads: " + threadPool.getMinThreads() + " &lt; " + threadPool.getThreads() + " &lt; " + threadPool.getMaxThreads() + "<br>";
			str += " Idle thread: " + threadPool.getIdleThreads() + " / " + threadPool.getThreads() + "<br>";
			str += "</h3><br>";
			str += threadPool.toString();
		}
		return str;
	}

	private void configureSSL(Server server) {
		Globals.logString("checking : configureSSLIfRequired()");
		Globals.logString("https configuring");
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(port);
		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());
		SslContextFactory sslContextFactory = new SslContextFactory("/home/my_user/.keystore");
		sslContextFactory.setKeyStorePassword("changeit");
		sslContextFactory.setCertAlias(certificateAlias);
		ServerConnector httpsConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https_config));
		httpsConnector.setPort(port);
		server.setConnectors(new Connector[] { httpsConnector });
		Globals.logString("https setting server connector successfully");
	}

	@SuppressWarnings("unused")
	private void firstCall() {
		String url = "http://localhost:" + port + "/" + firstCallService;
		if (!Utils.isStringEmpty(certificateAlias)) {
			url = "https://localhost:" + port + "/" + firstCallService;
		}
		try {
			HttpGet someHttpGet = new HttpGet(url);
			URIBuilder uriBuilder = new URIBuilder(someHttpGet.getURI());
			uriBuilder.addParameter("uiclass", "siren.fenix.covid.main.FenixCovidUI");
			URI uri = uriBuilder.build();
			someHttpGet.setURI(uri);
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(someHttpGet);
		} catch (Exception e) {
			Globals.logException(e);
		}
	}

	public static void setCORS(HttpServletResponse response) {
		if (response != null) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS,  DELETE");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Date, X-Api-Version, X-File-Name, X-Pagination, Content-Disposition, showLoader");
			response.setHeader("Access-Control-Expose-Headers", "Content-Type, X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Date, X-Api-Version, X-File-Name, X-Pagination, Content-Disposition, showLoader");
			response.setHeader("Access-Control-Max-Age", "86400");
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getCertificateAlias() {
		return certificateAlias;
	}

	public void setCertificateAlias(String httpsAlias) {
		this.certificateAlias = httpsAlias;
	}

	public String getFirstCallService() {
		return firstCallService;
	}

	public void setFirstCallService(String firstCallService) {
		this.firstCallService = firstCallService;
	}

	public String getLogDir() {
		return logDir;
	}

	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}

	public int getMaxThread() {
		return maxThread;
	}

	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}
	
	public void initJsonFieldFilter() {
	}
*/
}
