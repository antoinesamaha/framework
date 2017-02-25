package com.foc.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import com.foc.gui.FAbstractListPanel;
import com.foc.gui.findObject.FindObjectGuiDetailsPanel_ForFooter;

@SuppressWarnings("serial")
public class FilterAction extends AbstractAction {

  protected FAbstractListPanel abstractListPanel = null;
  
  public FilterAction( FAbstractListPanel abstractListPanel ){
    this.abstractListPanel = abstractListPanel;
  }
  
  public void dispose(){
    abstractListPanel = null;
  }
  
  public void actionPerformed(ActionEvent e) {
  	FindObjectGuiDetailsPanel_ForFooter footerFilterPanel = abstractListPanel.getFilterExpressionPanel();
  	footerFilterPanel.requestFocusForExpression();  	
  	
  	/*
    FindObject findObject = FindObject.getFindObject();
    if( findObject != null ){
      FPanel findObjectPanel = new FindObjectGuiDetailsPanel(findObject, FocObject.DEFAULT_VIEW_ID, abstractListPanel);
      if( findObjectPanel != null ){
        Globals.getDisplayManager().popupDialog(findObjectPanel, findObjectPanel.getFrameTitle(), true);  
      }  
    }
    */
  }
}
