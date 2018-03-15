package com.foc.vaadin.gui.xmlForm;

import com.foc.business.printing.gui.PrintingAction;
import com.foc.desc.FocObject;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.ColumnGenerator;

public abstract class FocXMLLayout_ReportTable extends FocXMLLayout {
	
	private boolean selectionMode = false; 

	public boolean isSelectionMode() {
		return selectionMode;
	}

	public void setSelectionMode(boolean selectionMode) {
		this.selectionMode = selectionMode;
	}

	private FVTableWrapperLayout getTableWrapperLayout() {
		return (FVTableWrapperLayout) getComponentByName("_FILTER_TABLE");
	}

	private TableTreeDelegate getTableTreeDelegate() {
		return getTableWrapperLayout() != null ? getTableWrapperLayout().getTableTreeDelegate() : null;
	}
	
	private void addSelectColumnIfNeeded() {
		if (isSelectionMode()) {
			TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
//			tableTreeDelegate.setSelectionMode(TableTreeDelegate.SELECTION_MODE_SINGLE);
//			tableTreeDelegate.getcol
		}
	}

	@Override
  public ColumnGenerator table_getGeneratedColumn(String tableName, final FVTableColumn tableColumn) {
  	ColumnGenerator columnGenerator = null;

  	if(tableColumn.getName().equals("PRINT_REPORT")){
  		columnGenerator = new ColumnGenerator() {
  			public Object generateCell(Table source, Object itemId, Object columnId) {
  				final FocObject reportConfig = (FocObject) getFocList().searchByReference((Long) itemId);
  				String caption = tableColumn.getCaption();
  				if(isSelectionMode()) {
  					caption = "Select";
  				}
  				FVButton button = new FVButton(caption, new FVButton.ClickListener() {
						
						public void buttonClick(ClickEvent event) {
		  				if(isSelectionMode()) {
		  					
		  				} else {
								PrintingAction printingAction = reportConfig.getThisFocDesc().newPrintingAction();
								FVValidationLayout.popupPrintPdf_Table(null, getMainWindow(), printingAction, reportConfig);
		  				}
						}

					});
  				
  				return button;
  			}
  		};
  	}
  	
  	return columnGenerator;
  }
}