package com.foc.log;

public interface FocLogListener {
	public void addLogEvent(FocLogEvent event);
	public HashedDocument getLastHash(String entityName, long entityReference);
	
	public class HashedDocument {
		
		public int getVersion() {
			return version;
		}
		public void setVersion(int version) {
			this.version = version;
		}
		public String getHash() {
			return hash;
		}
		public void setHash(String hash) {
			this.hash = hash;
		}
		public String getDocument() {
			return document;
		}
		public void setDocument(String document) {
			this.document = document;
		}
		private int version = 0;
		private String hash = "";
		private String document = "";
		
		
	}
}