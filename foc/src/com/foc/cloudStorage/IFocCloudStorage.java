package com.foc.cloudStorage;

import java.io.InputStream;
import java.util.Date;

public interface IFocCloudStorage {

	//Buckets in the case of S3
	public Object      getDirectory() throws FocCloudStorageException;
	public void        setDirectory(String directory, boolean createIfNeeded) throws FocCloudStorageException;

	public void        uploadInputStream(String key, InputStream stream) throws FocCloudStorageException;
	public InputStream downloadFile(String key) throws FocCloudStorageException;
	public void        deleteFile(String key) throws FocCloudStorageException;
	
	public boolean     doesFileExist(String key) throws FocCloudStorageException;

}
