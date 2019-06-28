package com.foc.api;

public interface IFocFileEncryptor {
	public byte[] encrypt(int algorithm, byte[] inputStream) throws Exception;
	public byte[] decrypt(int algorithm, byte[] inputStream) throws Exception;
}