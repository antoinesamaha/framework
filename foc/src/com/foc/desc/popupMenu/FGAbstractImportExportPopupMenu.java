package com.foc.desc.popupMenu;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import com.foc.desc.FocDesc;
import com.foc.gui.FAbstractListPanel;
import com.foc.gui.FTreeTablePanel;
import com.foc.gui.table.FGPopupMenuItem;

@SuppressWarnings("serial")
public abstract class FGAbstractImportExportPopupMenu extends FGPopupMenuItem {

	private boolean                  forTree          = false;
	private AbstractImportExportFile importExportFile = null ;
	
	public FGAbstractImportExportPopupMenu(String text, Icon icon, FAbstractListPanel listPanel) {
		super(text, icon, listPanel);
		setForTree(listPanel instanceof FTreeTablePanel);
	}
	
	public void dispose(){
		super.dispose();
		if(importExportFile != null){
			importExportFile.dispose();
			importExportFile = null;
		}
	}

	public FocDesc getFocDesc(){
		return getListPanel().getFocList().getFocDesc();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public boolean isForTree() {
		return forTree;
	}

	public void setForTree(boolean forTree) {
		this.forTree = forTree;
	}
}
