package com.foc.formula;

import java.awt.event.ActionEvent;

import com.foc.gui.FAbstractListPanel;
import com.foc.gui.actions.CopyPasteContent;
import com.foc.gui.actions.PasteAction;
import com.foc.gui.table.FAbstractTableModel;
import com.foc.gui.table.FTable;
import com.foc.gui.table.FTableView;
import com.foc.property.FProperty;

@SuppressWarnings("serial")
public class FormulaPasteAction extends PasteAction {

  private AbstractFormulaEnvironment formulaEnvironment = null;
  
  public FormulaPasteAction(FAbstractListPanel abstractListPanel, AbstractFormulaEnvironment formulaEnvironment) {
    super(abstractListPanel);
    this.formulaEnvironment = formulaEnvironment;
  }
  
  public void dispose(){
    super.dispose();
    formulaEnvironment = null;
  }
  
  public void actionPerformed(ActionEvent e) {
    
    FTable table = abstractListPanel.getTable();
    
    int row = table.getSelectedRow();
    int col = table.getSelectedColumn();
    FAbstractTableModel tableModel = table.getTableModel();
    FTableView tableView = tableModel.getTableView();
    col = tableView.getVisibleColumnIndex(col);
    
    FProperty property = tableModel.getFProperty(row, col);
    if(property != null && shouldPaste(property)){
      CopyPasteContent copyPasteContent = abstractListPanel.getCopyPasteContent();
      if(copyPasteContent.getFormulaExpression() != null){
        String expression = copyPasteContent.getFormulaExpression();
        formulaEnvironment.applyFormulaToProperty(property, expression);
      }else{
        //BAntoineS - To allow the past of a value upon a with formula cell
        if(property.isWithFormula()){
          PropertyFormulaContext context = property.getPropertyFormulaContext();
          PropertyFormula propFormula = context.getPropertyFormula();
          propFormula.setExpression("");
        }
        //EAntoineS
        super.actionPerformed(e);
      }
    }
  }
  
}
