package com.foc.business.workflow;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.gui.FTreeTablePanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableColumn;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;
import com.foc.tree.objectTree.FObjectTreeDropTargetListener;

@SuppressWarnings("serial")
public class WFSiteGuiTreePanel extends FTreeTablePanel {
  
  public WFSiteGuiTreePanel(FocList list, int viewID){
    setMainPanelSising(FILL_VERTICAL);
    setFrameTitle("Site Hierarchy");

    FocDesc desc = WFSiteDesc.getInstance();

    if(desc != null){
    	WFSiteTree tree = WFSiteTree.newInstance();
      
      setTree(tree);
      expandAll();
      
      FTableView tableView = getTableView();
      
      FTableColumn col = null;
     	tableView.addColumn(desc, WFSiteDesc.FLD_DESCRIPTION, false);
      
      construct();

      getTable().setDropable(new FObjectTreeDropTargetListener(this));
      
      //getTable().setDropable(new Obje);
      //tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
      showAddButton(true);
      showRemoveButton(true);
      showEditButton(true);
      requestFocusOnCurrentItem();

      col = tableView.getColumnById(FField.TREE_FIELD_ID);
      col.setEditable(false);
      FValidationPanel valid = showValidationPanel(true);
      valid.addSubject(tree.getFocList());
    }
  }
}
