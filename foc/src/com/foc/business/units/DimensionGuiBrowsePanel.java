package com.foc.business.units;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.event.FValidationListener;
import com.foc.gui.FGCurrentItemPanel;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class DimensionGuiBrowsePanel extends FListPanel {

	public DimensionGuiBrowsePanel(FocList list, int viewID){
		super("Dimension", FPanel.FILL_VERTICAL);
		FocDesc desc = DimensionDesc.getInstance();

		UnitDesc.getInstance().createSystemUnits();
		
    if(desc != null){
    	if(list == null){
    		list = DimensionDesc.getList(FocList.LOAD_IF_NEEDED);
    	}else{
    		list.loadIfNotLoadedFromDB();
    	}
      setFocList(list);
      FTableView tableView = getTableView();   
      
      tableView.addColumn(desc, DimensionDesc.FLD_NAME, true);
      
      construct();
      
      showEditButton(false);
      showDuplicateButton(false);
      requestFocusOnCurrentItem();      
    }

    FGCurrentItemPanel currentItemPanel = new FGCurrentItemPanel(this, DimensionGuiDetailsPanel.VIEW_UNIT_LIST);
    add(currentItemPanel, 2, 1);
       
  	FValidationPanel savePanel = showValidationPanel(true);
    if(savePanel != null){
      savePanel.addSubject(list);
      savePanel.setValidationListener(new FValidationListener(){
				@Override
				public void postCancelation(FValidationPanel panel) {
					UnitDesc.getList(FocList.FORCE_RELOAD);
				}

				@Override
				public void postValidation(FValidationPanel panel) {
					UnitDesc.getList(FocList.FORCE_RELOAD);
				}

				@Override
				public boolean proceedCancelation(FValidationPanel panel) {
					return true;
				}

				@Override
				public boolean proceedValidation(FValidationPanel panel) {
					return true;
				}
      });
    }
	}

	@Override
	public FPanel newDetailsPanelForFocObject(FocObject focObj, int viewID) {
		return new DimensionGuiDetailsPanel(focObj, viewID);
	}
}