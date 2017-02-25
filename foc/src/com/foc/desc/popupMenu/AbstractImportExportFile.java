package com.foc.desc.popupMenu;

import java.io.File;

import com.foc.Globals;
import com.foc.gui.FGFileChooser;
import com.foc.property.FString;

public abstract class AbstractImportExportFile {

	private File file = null;
	
	public AbstractImportExportFile(){
		
	}
	
	public void dispose(){
		if(file != null){
			file = null;
		}
	}
	
	public void chooseFile(){
		file = null;
		FString       stringProp  = new FString(null, 1, "C:/");
		FGFileChooser fileChooser = new FGFileChooser(stringProp, Globals.getDisplayManager().getMainFrame(), "Choose a file to export to", FGFileChooser.MODE_FILES_AND_DIRECTORIES, null, null);
		fileChooser.popupFileChooser();
		fileChooser.dispose();
		
		String fullPath = stringProp.getString();
		if(fullPath != null && !fullPath.isEmpty()){
			file = new File(fullPath);
			if(file.isDirectory()) file = null;
		}
	}

}
