package com.foc.gui;

import javax.swing.SwingUtilities;

import com.foc.desc.FocObject;

public class EditAfterCreationData {
	private FAbstractListPanel listPanel       = null ;
	private FocObject          focObject       = null ;
	private boolean            disposeAfterRun = false;
	
	public EditAfterCreationData(FAbstractListPanel listPanel, FocObject focObject){
		this.listPanel = listPanel;
		this.focObject = focObject;
	}
	
	public void dispose(){
		if(!disposeAfterRun){
			dispose_Internal();
		}
	}
	
	public void dispose_Internal(){
		listPanel = null;
		focObject = null;
	}
	
	public void editCreatedItem(){
		disposeAfterRun = true;
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				if(focObject.hasRealReference()){//This condition is to disable edit popup if the user calcels the creation
					listPanel.setSelectedObject(focObject);
					listPanel.editCurrentItem(focObject);
				}
				dispose_Internal();
			}
		});
	}
}
