package com.foc.list.filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.foc.Globals;
import com.foc.event.FValidationListener;
import com.foc.gui.FGButton;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTable;

public class FocListFilterValidationListener implements FValidationListener {
	
	private FocListFilterGuiBrowsePanel filterGuiBrowse = null;
	private FocListFilter               focListFilter   = null;
	private FValidationPanel            validationPanel = null;
	
  public static final String BUTTON_LABEL_APPLY  = "Apply" ;
  public static final String BUTTON_LABEL_CANCEL = "Cancel";
  public static final String BUTTON_LABEL_RESET  = "Reset" ;

	public FocListFilterValidationListener(FocListFilterGuiBrowsePanel filterGuiBrowse, FocListFilter focListFilter, FValidationPanel validationPanel){
		this.filterGuiBrowse = filterGuiBrowse;
		this.focListFilter   = focListFilter  ;
		this.validationPanel = validationPanel;
		
		validationPanel.setValidationListener(this);
		
    validationPanel.setValidationType(FValidationPanel.VALIDATION_OK_CANCEL);
    validationPanel.setAskForConfirmationForExit(false);
    
    validationPanel.setCancelationButtonLabel(BUTTON_LABEL_CANCEL);
    validationPanel.setValidationButtonLabel(BUTTON_LABEL_APPLY);
    if(!focListFilter.isAllwaysActive()){
      FGButton removeButton = new FGButton(BUTTON_LABEL_RESET);
      removeButton.setName("Remove");
      removeButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
          getFocListFilter().setActive(false);
          getValidationPanel().cancel();
        }
      });
      validationPanel.addButton(removeButton);
      if (focListFilter.isRevisionSupportEnabled()){
        removeButton.setVisible(false);
      }
    }    
	}
	
	public void dispose(){
		focListFilter   = null;
		validationPanel = null;
	}
	
	@Override
  public boolean proceedValidation(FValidationPanel panel) {
    return true;
  }

	@Override
  public boolean proceedCancelation(FValidationPanel panel) {
    return true;
  }

	@Override
  public void postValidation(FValidationPanel panel) {
  	if(focListFilter != null){
  		if(filterGuiBrowse != null){
  			FocListFilter srcFilter = (FocListFilter) filterGuiBrowse.getSelectedObject();
  			if(srcFilter != null){
  				focListFilter.copyPropertiesFrom(srcFilter);
  			}
  		}
  		focListFilter.setActive(true);
  	}
  }

	@Override
  public void postCancelation(FValidationPanel panel) {
    
  }

	public FocListFilter getFocListFilter() {
		return focListFilter;
	}

	public FValidationPanel getValidationPanel() {
		return validationPanel;
	}

	public FocListFilterGuiBrowsePanel getFilterGuiBrowse() {
		return filterGuiBrowse;
	}
}
