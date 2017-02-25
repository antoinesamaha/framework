package com.foc.business.department;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.gui.FTreeTablePanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableColumn;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;
import com.foc.tree.objectTree.FObjectTreeDropTargetListener;

@SuppressWarnings("serial")
public class DepartmentGuiTreePanel extends FTreeTablePanel {
  
  public static final int VIEW_EMBEDDED_IN_SPLIT = 2;
	
  public DepartmentGuiTreePanel(FocList list, int viewID){
    setMainPanelSising(FILL_VERTICAL);
    setFrameTitle("Departments Hierarchy");

    FocDesc desc = DepartmentDesc.getInstance();

    if(desc != null){
    	DepartmentTree tree = DepartmentTree.getInstance();
      
      setTree(tree);
      expandAll();
      
      FTableView tableView = getTableView();
      tableView.addColumn(desc, FField.FLD_DESCRIPTION, true);
      tableView.addColumn(desc, DepartmentDesc.FLD_END_DEPARTMENT, true);
      
      construct();
      if(viewID != VIEW_EMBEDDED_IN_SPLIT){
      	getTable().setDropable(new FObjectTreeDropTargetListener(this));
      }
      
      //getTable().setDropable(new Obje);
      tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
      showAddButton(viewID != VIEW_EMBEDDED_IN_SPLIT);
      showRemoveButton(viewID != VIEW_EMBEDDED_IN_SPLIT);
      showEditButton(false);
      requestFocusOnCurrentItem();

      if(viewID != VIEW_EMBEDDED_IN_SPLIT){      
      	FTableColumn col = tableView.getColumnById(FField.TREE_FIELD_ID);
	      col.setEditable(true);
	      FValidationPanel valid = showValidationPanel(true);
	      valid.addSubject(tree.getFocList());
      }
    }
  }
}
