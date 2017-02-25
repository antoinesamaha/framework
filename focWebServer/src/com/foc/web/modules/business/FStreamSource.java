package com.foc.web.modules.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource.StreamSource;

public class FStreamSource implements StreamSource{
	
	private byte[] bytes = null;
	
	public FStreamSource(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public void dispose(){
		bytes = null;
	}
	
	public InputStream getStream() {
		return new ByteArrayInputStream(bytes);
	}
}