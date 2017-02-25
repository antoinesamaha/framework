package com.foc.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.foc.formula.Formula;
import com.foc.gui.FAbstractListPanel;
import com.foc.gui.table.FAbstractTableModel;
import com.foc.gui.table.FTable;
import com.foc.gui.table.FTableView;
import com.foc.property.FProperty;

@SuppressWarnings("serial")
public class CopyAction extends AbstractAction {

  protected FAbstractListPanel abstractListPanel = null;
  
  public CopyAction( FAbstractListPanel abstractListPanel ){
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
    
    abstractListPanel.getCopyPasteContent().setTableDisplayObject(tableModel.getValueAt(row, col));
    
    FProperty prop = tableModel.getFProperty(row, col);
    if( prop != null && prop.isWithFormula() ){
      Formula formula = prop.getFormula();
      String expression = formula.getString();
      abstractListPanel.getCopyPasteContent().setFormulaExpression(expression);
    }
  }
  
}
