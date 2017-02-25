package com.foc.business.adrBook;

import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class AdrBookPartyGuiSelectionPanel extends FListPanel {
  
	public static final int VIEW_MULTIPLE_SELECTION = 1;
	
  public AdrBookPartyGuiSelectionPanel(FocList list, int viewID){
    super("Select a company", FPanel.FILL_VERTICAL);
    FocDesc desc = AdrBookPartyDesc.getInstance();
    if(desc != null){
      setFocList(list);
    }
    
    FTableView tableView = getTableView();
    
    if(viewID == VIEW_MULTIPLE_SELECTION){
    	tableView.addSelectionColumn();
    }
    
    tableView.addColumn(desc, AdrBookPartyDesc.FLD_CODE, false);
    tableView.addColumn(desc, AdrBookPartyDesc.FLD_NAME, false);
    tableView.addColumn(desc, AdrBookPartyDesc.FLD_EXTERNAL_CODE, false);
    construct();
    
    addFilterExpressionPanel();
    
    FValidationPanel selectionPanel = showValidationPanel(true);
    selectionPanel.addSubject(list);
    if(viewID == VIEW_MULTIPLE_SELECTION){
    	selectionPanel.setValidationButtonLabel("Ok");
    }

    requestFocusOnCurrentItem();
    showEditButton(false);
    showAddButton(false);
    showRemoveButton(false);
  }
}
