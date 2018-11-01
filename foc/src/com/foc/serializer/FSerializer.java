package com.foc.serializer;

public interface FSerializer {
	public void serializeToBuffer();
	public int getVersion();
	public void dispose();
}
