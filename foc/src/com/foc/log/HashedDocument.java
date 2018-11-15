package com.foc.log;

public class HashedDocument {
	private String entityName = null;
	private long entityReference = 0;
	private int version = 0;
	private String hash = "";
	private String document = "";
	
	public HashedDocument(String entityName, long entityReference) {
		this.entityName = entityName;
		this.entityReference = entityReference;
	}

	public String getEntityName() {
		return entityName;
	}

	public long getEntityReference() {
		return entityReference;
	}

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
}