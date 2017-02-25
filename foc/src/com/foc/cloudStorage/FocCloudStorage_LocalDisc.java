package com.foc.cloudStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.foc.Globals;
import com.foc.IFocEnvironment;

public class FocCloudStorage_LocalDisc implements IFocCloudStorage {

	@Override
	public Object getDirectory() throws FocCloudStorageException {
		return Globals.getApp().getCloudStorageDirectory();//rootDirectory;
	}

	@Override
	public void setDirectory(String directory, boolean createIfNeeded) throws FocCloudStorageException {
//		this.rootDirectory = directory;
	}

	private String getFileName(String key){
		String dir = "";
		try{
			dir = "c:/"+((String)getDirectory())+"/"+key;
		}catch(Exception e){
			Globals.logException(e);
		}
		
		return dir;
	}
	
//	private static int DEBUG = 1;				//TO REMOVE
	
	@Override
	public void uploadInputStream(String key, InputStream stream) throws FocCloudStorageException {
		OutputStream outputStream = null;
	 
		try {
			String fileName = getFileName(key); 
			if(fileName != null){
//				//TO REMOVE
//				DEBUG++;
//				fileName = fileName.replace(".xlsx", DEBUG+".xlsx");
//				//TO REMOVE
				File theFile = new File(fileName);
				if(!theFile.isDirectory()){
					createDirectory(fileName);				
					outputStream = new FileOutputStream(fileName);
					
					int read = 0;
					byte[] bytes = new byte[1024];
					
					read = stream.read(bytes);
					while (read != -1) {
						outputStream.write(bytes, 0, read);
						read = stream.read(bytes);					
					}
				}else{
					Globals.showNotification("Error uploading", "File name not specified, could not upload", IFocEnvironment.TYPE_ERROR_MESSAGE);
				}
			}else{
				Globals.showNotification("Error uploading", "File name not specified, could not upload", IFocEnvironment.TYPE_ERROR_MESSAGE);
			}
		} catch (Exception e) {
			Globals.logException(e);
		}

		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				Globals.logException(e);
			}
		}
	}

	private File newDirectory(String fileName){
		File dirFile = null;
		int lastIdx = fileName.lastIndexOf('/');
		if(lastIdx > 0){
			String dirPath = fileName.substring(0, lastIdx);
			dirFile = new File(dirPath);
		}
		return dirFile;
	}

	private void createDirectory(String fileName){
		File dirFile = newDirectory(fileName);
		if(dirFile != null){
			dirFile.mkdirs();
		}
	}

	@Override
	public InputStream downloadFile(String key) throws FocCloudStorageException {
		String fileName = getFileName(key);
		File file = new File(fileName);
		InputStream inputStream = null;
		try{
			if(file != null && file.exists()){
//				createDirectory(fileName);				
//				file.createNewFile();
				
				//When we add a new item in table the FVColGen_FocProperty calls a method getDisplayObject_ForProperty_NonEditable the getObject method calls the 
				//downloadFile in this method we have the key (consists of directory and the file name) in case we are creating a new item in table the file name is empty
				//so we can create the directory since we have the path but we can't get the FileinputStream since the file name is empty so it will throw an exception
				//so i added a condition checking the path if it is a file or directory  
				if(file.isFile()){
					inputStream = new FileInputStream(file);
				}
			}
		}catch(Exception e){
			Globals.logException(e);
		}
		
		return inputStream;
	}

	@Override
	public void deleteFile(String key) throws FocCloudStorageException {
		String fileName = getFileName(key);
		File file = new File(fileName);
		if(file!=null){
			file.delete();
		}
	}

	@Override
	public boolean doesFileExist(String key) throws FocCloudStorageException {
		String fileName = getFileName(key);
		File file = new File(fileName);
		if(file!=null){
			return file.isFile() && file.exists();
		}
		return false;
	}
}
