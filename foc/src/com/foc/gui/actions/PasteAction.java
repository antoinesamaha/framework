package com.foc.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import com.foc.gui.FAbstractListPanel;
import com.foc.gui.table.FAbstractTableModel;
import com.foc.gui.table.FTable;
import com.foc.gui.table.FTableView;
import com.foc.property.FProperty;

@SuppressWarnings("serial")
public class PasteAction extends AbstractAction {
  protected FAbstractListPanel abstractListPanel = null;
  
  public PasteAction( FAbstractListPanel abstractListPanel ){
    this.abstractListPanel = abstractListPanel;
  }
  
  public void dispose(){
    abstractListPanel = null;
  }
  
  public void actionPerformed(ActionEvent e) {
    FTable table = abstractListPanel.getTable();
    
    int row = table.getSelectedRow();
    int col = table.getSelectedColumn();
    FAbstractTableModel tableModel = table.getTableModel();
    FTableView tableView = tableModel.getTableView();
    col = tableView.getVisibleColumnIndex(col);
    
    FProperty property = tableModel.getFProperty(row, col);
    if( property != null && shouldPaste(property) ){
      CopyPasteContent copyPasteContent = abstractListPanel.getCopyPasteContent();
      if(!property.isValueLocked()){ //TEMP
        if( copyPasteContent.getTableDisplayObject() != null ){
          tableModel.setValueAt(copyPasteContent.getTableDisplayObject(), row, col);  
        }
      }
    }
  }
  
  public boolean shouldPaste(FProperty property){
    return true;
  }
  
}
