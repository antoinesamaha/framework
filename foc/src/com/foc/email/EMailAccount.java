package com.foc.email;

public class EMailAccount {

  public static final int ENCRYPTION_TYPE_NONE = 0;
  public static final int ENCRYPTION_TYPE_SSL  = 1;
  public static final int ENCRYPTION_TYPE_TLS  = 2;

  private int encryptionConnectionType = ENCRYPTION_TYPE_SSL;

  private String host = "email-smtp.us-east-1.amazonaws.com";
  // private int port = 587;
  private int port = 465;//  Outgoing server (SMTP) 25, 587, or 2587 (to connect using STARTTLS), or 465 or 2465 (to connect using TLS Wrapper).
  private String username = "AKIAIVMOV3G7F55RILIQ";
  private String password = "Ajmnx7ON3LoKYMtFdbx+LPqls3HnArYkGlJ96/42qFKu";
  private String sender   = "01barmaja@01barmaja.com";
  
  public EMailAccount(){
  }
  
	public int getEncryptionConnectionType() {
		return encryptionConnectionType;
	}
	
	public void setEncryptionConnectionType(int encryptionConnectionType) {
		this.encryptionConnectionType = encryptionConnectionType;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	private static EMailAccount account = null;
  public static EMailAccount getInstance(){
  	if(account == null){
  		account = new EMailAccount();
  	}
  	return account;
  }

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
