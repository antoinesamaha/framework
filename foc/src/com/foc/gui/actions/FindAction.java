package com.foc.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.gui.FAbstractListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.findObject.FindObject;
import com.foc.gui.findObject.FindObjectGuiDetailsPanel;

@SuppressWarnings("serial")
public class FindAction extends AbstractAction {

  protected FAbstractListPanel abstractListPanel = null;
  
  public FindAction( FAbstractListPanel abstractListPanel ){
    this.abstractListPanel = abstractListPanel;
  }
  
  public void dispose(){
    abstractListPanel = null;
  }
  
  public void actionPerformed(ActionEvent e) {
    FindObject findObject = FindObject.getFindObject();
    if( findObject != null ){
      FPanel findObjectPanel = new FindObjectGuiDetailsPanel(findObject, FocObject.DEFAULT_VIEW_ID, abstractListPanel);
      if( findObjectPanel != null ){
        Globals.getDisplayManager().popupDialog(findObjectPanel, findObjectPanel.getFrameTitle(), true);  
      }  
    }
  }
}
