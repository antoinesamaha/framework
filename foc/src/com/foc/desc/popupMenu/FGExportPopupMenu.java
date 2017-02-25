package com.foc.desc.popupMenu;

import java.awt.event.ActionEvent;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FAbstractListPanel;

@SuppressWarnings("serial")
public class FGExportPopupMenu extends FGAbstractImportExportPopupMenu {

	public FGExportPopupMenu(FAbstractListPanel listPanel) {
		super("Export", Globals.getApp().getFocIcons().getExportIcon(), listPanel);
	}

	public FocDesc getFocDesc(){
		return getListPanel().getFocList().getFocDesc();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
