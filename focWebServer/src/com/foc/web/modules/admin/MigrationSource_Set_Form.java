package com.foc.web.modules.admin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.db.migration.MigrationFileReader;
import com.foc.db.migration.MigrationSource;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.components.upload.FVUpload_Image;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Upload.SucceededEvent;

@SuppressWarnings("serial")
public class MigrationSource_Set_Form extends FocXMLLayout{

	private MigrationFileReader migrationFileReader = null; 
	
	public void dispose(){
		super.dispose();
		dispose_FileReader();
	}

	public void dispose_FileReader(){
		if(migrationFileReader != null){
			migrationFileReader.dispose();
			migrationFileReader = null;
		}
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		addUploadField();
	}
	
	private MigrationSource getMigrationSource(){
		return (MigrationSource) getFocData();
	}
	
	private void addUploadField(){
    FVUpload_Image importFile = new FVUpload_Image();
    
    FVImageReceiver fileReceiver = new FVImageReceiver() {
      public void imageReceived(SucceededEvent event, InputStream inputStream) {
      	if(getMigrationSource() != null){
	      	migrationFileReader = new MigrationFileReader(getMigrationSource(), inputStream, ',');
	      	if(migrationFileReader.readLine()){
	      		Globals.showNotification("File Error", "Please check the file, might be empty", IFocEnvironment.TYPE_ERROR_MESSAGE);
	      		dispose_FileReader();
	      	}else{
	      		Globals.showNotification("Uploaded successfully", "Please set the Column titles and click 'Continue import'", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
	      		addContinueReadingFileButton();
	      		
	      		ArrayList<String> titlesArray = new ArrayList<String>();
	      		Iterator<String> iter = migrationFileReader.getMapTitle2Position().keySet().iterator();
	      		while(iter != null && iter.hasNext()){
	      			String migFld = iter.next();
	      			titlesArray.add(migFld);
	      		}
	      		getMigrationSource().setColumnTitleArray(titlesArray);
	      		refresh();
	      	}
	      	
//	      	migrationFileReader.readFile();
//	      	dispose_FileReader();
      	}
      }
    };
    
    importFile.setImageReceiver(fileReceiver);
    addComponentAsFirst(importFile);
  }
	
	private void addContinueReadingFileButton(){
    FVButton importButton = new FVButton("Continue import");
    importButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(migrationFileReader != null){
					migrationFileReader.readFile();
					dispose_FileReader();
				}
			}
		});
    
    addComponentAsFirst(importButton);
  }
}
