package com.foc.vaadin.gui.components.tableAndTree;

import java.util.ArrayList;

import com.foc.vaadin.gui.components.FVCheckBox;
import com.foc.vaadin.gui.components.FVTableColumn;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class FVColGen_Select extends FVColumnGenerator {

	public final static String SELECT_UNSELECT_ALL_CHECK_BOX_NAME = "SELECT_UNSELECT_ALL";
	
	public FVColGen_Select(FVTableColumn tableColumn) {
		super(tableColumn);
		if(getTableTreeDelegate() != null){
			int selectionMode = getTableTreeDelegate().getSelectionMode();
			if(selectionMode == TableTreeDelegate.SELECTION_MODE_NONE || selectionMode == TableTreeDelegate.SELECTION_MODE_MULTIPLE){
				boolean single = false;
				String singleSelection = tableColumn.getAttributes().getValue(FXML.ATT_SINGLE_SELECTION);
				if(singleSelection != null){
					singleSelection = singleSelection.trim().toLowerCase();
					single = singleSelection.equals("true") || singleSelection.equals("1");
					getTableTreeDelegate().setSelectionMode(TableTreeDelegate.SELECTION_MODE_SINGLE);
				}
				if(!single){
					getTableTreeDelegate().setSelectionMode(TableTreeDelegate.SELECTION_MODE_MULTIPLE);
					addSelectUnselectAllCheckBox();
				}
			}
		}
	}
	
	/*public FVColGen_Select(FVTableColumn tableColumn) {
		super(tableColumn);
		if(getTableTreeDelegate() != null){
			int selectionMode = getTableTreeDelegate().getSelectionMode();
			if(selectionMode == TableTreeDelegate.SELECTION_MODE_NONE || selectionMode == TableTreeDelegate.SELECTION_MODE_MULTIPLE){
				boolean single = false; 
				String singleSelection = tableColumn.getAttributes().getValue(FXML.ATT_SINGLE_SELECTION);
				if(singleSelection != null){
					singleSelection = singleSelection.trim().toLowerCase();
					single = singleSelection.equals("true") || singleSelection.equals("1");
					getTableTreeDelegate().setSelectionMode(TableTreeDelegate.SELECTION_MODE_SINGLE);
				}
				if(!single){
					addSelectUnselectAllCheckBox();
				}
			}
		}
	}*/
	
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Object object = null;
		FVCheckBox checkBox = new FVCheckBox("");
		checkBox.setImmediate(true);
		if(getTableTreeDelegate() != null){
			int selectionMode = getTableTreeDelegate().getSelectionMode();
						
			if(selectionMode == TableTreeDelegate.SELECTION_MODE_SINGLE){
				checkBox.addValueChangeListener(new FocSingleValueChangeListener(itemId));
			}else{
				checkBox.addValueChangeListener(new FocValueChangeListener(itemId));
			}
		}
		
		object = checkBox;
		
		String tableName = getTableTreeDelegate().getTableName();
		String objRef    = itemId.toString();
		String compName  = TableTreeDelegate.newComponentName(tableName, objRef, FVTableColumn.COL_ID_SELECT);
		if(getTableTreeDelegate() != null && getTableTreeDelegate().getFocXMLLayout()!= null){
			getTableTreeDelegate().getFocXMLLayout().putComponent(compName, checkBox);
			
			if(getTableTreeDelegate().selectionColumn_getSelectedIdArrayList().contains(itemId) && getTableTreeDelegate().selectionColumn_isSelectAll()){
				checkBox.setValue(true);
			}
		}
		return object;
	}
	
	public class FocValueChangeListener implements ValueChangeListener {

		private Object itemId = null;

		public FocValueChangeListener(Object itemId) {
			this.itemId = itemId;

		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			boolean isChecked = ((Boolean) event.getProperty().getValue()).booleanValue();
			TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
			if(tableTreeDelegate != null){
				ArrayList<Object> arrayList = tableTreeDelegate.selectionColumn_getSelectedIdArrayList();
				if(isChecked){
					Object itemId = getItemId();
					if(!arrayList.contains(itemId)){
						arrayList.add(itemId);
					}
				}else if(!isChecked){
					int idIndex = arrayList.indexOf(getItemId());
					if(idIndex >= 0){
						arrayList.remove(idIndex);
					}
				}
			}
		}

		public Object getItemId() {
			return itemId;
		}
	}

	public class FocSingleValueChangeListener implements ValueChangeListener {

		private Object itemId = null;

		public FocSingleValueChangeListener(Object itemId) {
			this.itemId = itemId;

		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			boolean isChecked = ((Boolean) event.getProperty().getValue()).booleanValue();
			TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
			if(tableTreeDelegate != null){
				ArrayList<Object> arrayList = tableTreeDelegate.selectionColumn_getSelectedIdArrayList();
				if(isChecked){
					Object itemId = getItemId();
					if(itemId != null){
						arrayList.clear();
						arrayList.add(itemId);
						tableTreeDelegate.selectionColumn_copyMemoryToGui();
					}
				}
			}
		}

		public Object getItemId() {
			return itemId;
		}
	}

	
	private void addSelectUnselectAllCheckBox(){
		FVTableWrapperLayout fvTableWrapperLayout = getTableTreeDelegate() != null ? getTableTreeDelegate().getWrapperLayout() : null;
		if(fvTableWrapperLayout != null){
			FocXMLAttributes attributes = new FocXMLAttributes();
			attributes.addAttribute(FXML.ATT_NAME, SELECT_UNSELECT_ALL_CHECK_BOX_NAME);
			FVCheckBox fvCheckBox = new FVCheckBox("Select / Unselect all");
			fvCheckBox.setAttributes(attributes);
			
			fvTableWrapperLayout.addHeaderComponent_ToLeft(fvCheckBox);
			fvCheckBox.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					TableTreeDelegate tableTreeDelegate = getTableTreeDelegate();
					if(tableTreeDelegate != null){
						boolean isSelected = (Boolean) event.getProperty().getValue();
						tableTreeDelegate.selectionColumn_selectAll_Internal(isSelected);
					}
				}
			});
		}
	}
}
