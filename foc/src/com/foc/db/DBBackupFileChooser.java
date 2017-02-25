package com.foc.db;

import javax.swing.JFileChooser;

import com.foc.ConfigInfo;
import com.foc.jasper.ExtensionFileFilter;

@SuppressWarnings("serial")
public class DBBackupFileChooser extends JFileChooser{

	public DBBackupFileChooser(){
		super(ConfigInfo.getLogDir());
		setDialogTitle("Select Backup File");
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setFileFilter(new ExtensionFileFilter(DBManager.BACKUP_FILE_EXTENSION, "FOC Backup File"));
	}

	public String choose(){
		String fileName = null;

	  int result = showDialog(null, "OK");
	  
	  if (result != JFileChooser.CANCEL_OPTION ){
	  	fileName = getSelectedFile().toString();
	  	fileName = fileName.replaceAll("\\\\","/");
      if(!fileName.endsWith("."+DBManager.BACKUP_FILE_EXTENSION)){
      	fileName += "."+DBManager.BACKUP_FILE_EXTENSION;	
      }
	  }

	  return fileName;
	}
}
