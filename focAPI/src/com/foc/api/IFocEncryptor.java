package com.foc.api;

public interface IFocEncryptor {
	public byte[] encrypt(int algorithm, byte[] inputStream) throws Exception;
	public byte[] decrypt(int algorithm, byte[] inputStream) throws Exception;
}