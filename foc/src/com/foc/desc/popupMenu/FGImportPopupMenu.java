package com.foc.desc.popupMenu;

import java.awt.event.ActionEvent;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FAbstractListPanel;

@SuppressWarnings("serial")
public class FGImportPopupMenu extends FGAbstractImportExportPopupMenu {

	public FGImportPopupMenu(FAbstractListPanel listPanel) {
		super("Import", Globals.getApp().getFocIcons().getImportIcon(), listPanel);
	}

	public FocDesc getFocDesc(){
		return getListPanel().getFocList().getFocDesc();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
