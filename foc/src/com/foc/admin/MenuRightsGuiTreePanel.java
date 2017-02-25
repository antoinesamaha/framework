package com.foc.admin;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.gui.FPanel;
import com.foc.gui.FTreeTablePanel;
import com.foc.gui.table.FTableColumn;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class MenuRightsGuiTreePanel extends FPanel{
  
  public MenuRightsGuiTreePanel(FocList list, int viewID, boolean readOnly){
    super("Menu Rights", FPanel.FILL_BOTH);
    
    FocDesc desc = MenuRightsDesc.getInstance();
    if (desc != null) {
      if(list == null){
        list = MenuRightsDesc.getList(FocList.FORCE_RELOAD);
      }
      if (list != null) {
        MenuRightsObjectTree menuRightsTree = new MenuRightsObjectTree(list, viewID);
        
        FTreeTablePanel selectionPanel = new FTreeTablePanel(menuRightsTree);
        FTableView tableView = selectionPanel.getTableView();

        FTableColumn col = tableView.getColumnById(FField.TREE_FIELD_ID);
        col.setEditable(false);
        tableView.addColumn(list.getFocDesc(), MenuRightsDesc.FLD_MENU_TITLE, false);
        tableView.addColumn(list.getFocDesc(), MenuRightsDesc.FLD_CUSTOM_TITLE, !readOnly);
        tableView.addColumn(list.getFocDesc(), MenuRightsDesc.FLD_RIGHT, 5, !readOnly);
        
        selectionPanel.construct();

        //tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
        
        selectionPanel.showEditButton(false);
        selectionPanel.showAddButton(false);
        selectionPanel.showRemoveButton(false);
        
        add(selectionPanel,0,0);
      }
    }
  }
}
