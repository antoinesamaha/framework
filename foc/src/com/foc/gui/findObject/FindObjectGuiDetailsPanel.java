package com.foc.gui.findObject;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import com.foc.desc.FocObject;
import com.foc.event.FValidationListener;
import com.foc.gui.FAbstractListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTable;

@SuppressWarnings("serial")
public class FindObjectGuiDetailsPanel extends FPanel {
  
  private FindObject findObject = null;
  private FAbstractListPanel listPanel = null;
  
  public void dispose(){
    super.dispose();
    findObject = null;
    listPanel = null;
  }
  
  public FindObjectGuiDetailsPanel(FocObject fo, int view, FAbstractListPanel listPanel ){
    super("Find", FPanel.FILL_NONE);   
    this.findObject = (FindObject) fo;
    this.listPanel = listPanel;
    
    
    ButtonGroup group = new ButtonGroup();
    add(findObject, FindObjectDesc.FLD_FIND, 0, 0);
    group.add((AbstractButton)add(findObject, FindObjectDesc.FLD_STARTS_WITH, 0, 1));
    group.add((AbstractButton)add(findObject, FindObjectDesc.FLD_CONTAINS, 0, 2));
    
    FValidationPanel savePanel = showValidationPanel(true);
    if (savePanel != null) {
      savePanel.setValidationType(FValidationPanel.VALIDATION_OK_CANCEL);
      savePanel.setValidationButtonLabel("Find");
      savePanel.setCancelationButtonLabel("Close"); 
      savePanel.setValidationListener(new FValidationListener(){

        public void postCancelation(FValidationPanel panel) {
        }

        public void postValidation(FValidationPanel panel) {
        }

        public boolean proceedCancelation(FValidationPanel panel) {
          return true;
        }

        public boolean proceedValidation(FValidationPanel panel) {
            
          FTable table = FindObjectGuiDetailsPanel.this.listPanel.getTable();
          
          int foundRow = -1;
          int col = table.getSelectedColumn();
          int row = table.getSelectedRow();
            
          if(row+1 < table.getRowCount()){
            for(int i=row+1; i<table.getTableModel().getRowCount() && foundRow < 0; i++){
              Object obj = table.getTableModel().getValueAt(i, col);
              if(obj instanceof String){
                String str = (String) obj;
                
                if( str != null && str.length() > 0 ){
                  String findExpression = findObject.getFindExpression();
                  if(findObject.isStartsWith() && str.toUpperCase().startsWith(findExpression.toUpperCase())){
                    foundRow = i;
                  }else if(findObject.isContains() && str.toUpperCase().contains(findExpression.toUpperCase())){
                    foundRow = i;
                  }
                }
              }
            }
          }
          
          if(foundRow >= 0){
            table.setRowSelectionInterval(foundRow, foundRow);
          }
          
          return false;
        }
        
      });
    }
  }
}
