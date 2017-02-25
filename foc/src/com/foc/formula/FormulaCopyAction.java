package com.foc.formula;

import java.awt.event.ActionEvent;

import com.foc.gui.FAbstractListPanel;
import com.foc.gui.actions.CopyAction;
import com.foc.gui.table.FAbstractTableModel;
import com.foc.gui.table.FTable;
import com.foc.gui.table.FTableView;
import com.foc.property.FProperty;

@SuppressWarnings("serial")
public class FormulaCopyAction extends CopyAction {

  public FormulaCopyAction(FAbstractListPanel abstractListPanel, AbstractFormulaEnvironment formulaEnvironment) {
    super(abstractListPanel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    
    FTable table = abstractListPanel.getTable();
    
    int row = table.getSelectedRow();
    int col = table.getSelectedColumn();
    FAbstractTableModel tableModel = table.getTableModel();
    FTableView tableView = tableModel.getTableView();
    col = tableView.getVisibleColumnIndex(col);
    
    FProperty prop = tableModel.getFProperty(row, col);
    if( prop != null ){
      if( prop.isWithFormula() ){
        Formula formula = prop.getFormula();
        String expression = formula.getString();
        abstractListPanel.getCopyPasteContent().setFormulaExpression(expression);  
      }else{
        abstractListPanel.getCopyPasteContent().setFormulaExpression(null);
      }
    }
  }
  
}
