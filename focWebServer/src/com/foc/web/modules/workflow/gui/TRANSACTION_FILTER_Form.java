package com.foc.web.modules.workflow.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.foc.business.department.Department;
import com.foc.business.department.DepartmentDesc;
import com.foc.business.department.DepartmentTree;
import com.foc.business.status.IStatusHolderDesc;
import com.foc.business.status.StatusHolderDesc;
import com.foc.dataWrapper.FocDataWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FMultipleChoiceItem;
import com.foc.desc.field.FMultipleChoiceItemInterface;
import com.foc.list.FocList;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.vaadin.gui.components.TableTreeDelegate;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.modules.workflow.WorkflowWebModule;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class TRANSACTION_FILTER_Form extends FocXMLLayout{

	private FVTableWrapperLayout      tableWrapperLayout        = null;
	private ACC_DEPARTMENT_Selection_Standard_Tree department_Selection_Tree = null;
	
	public void dispose(){
		super.dispose();
		tableWrapperLayout = null;
		department_Selection_Tree = null;
	}
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		initTransactionFilterForm();
//		copyExclusionsFrom_MemoryToGui();
	}
	
	public FocList getTransactionFocList(){
		return tableWrapperLayout != null && tableWrapperLayout.getTableOrTree() != null ?  tableWrapperLayout.getTableOrTree().getFocList() : null;
	}
	
	public FocDataWrapper getTransactionFocDataWrapper(){
		return tableWrapperLayout != null && tableWrapperLayout.getTableOrTree() != null ?  tableWrapperLayout.getTableOrTree().getFocDataWrapper() : null;
	}
	
	private FVTextField codeFrom = null;
	private FVTextField codeTo = null;
	
	private void initTransactionFilterForm(){
		FocList transactionLocList = (FocList) getTransactionFocList();
		FocDesc focDesc            = transactionLocList.getFocDesc();
		
		if(focDesc != null && focDesc.hasCodeField()){
			FVHorizontalLayout codeLayout = new FVHorizontalLayout(null);
			getTransactionFilterVerticalLayout().addComponent(codeLayout);
			
			codeFrom = new FVTextField();
			codeFrom.setCaption("From Code");
			codeFrom.setValue(tableWrapperLayout.getCodeFrom());
			codeLayout.addComponent(codeFrom);
			codeFrom.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					if(tableWrapperLayout != null && codeFrom != null){
						tableWrapperLayout.setCodeFrom(codeFrom.getValueString());
					}
				}
			});

			codeTo = new FVTextField();
			codeTo.setCaption("To Code");
			codeTo.setValue(tableWrapperLayout.getCodeTo());
			codeLayout.addComponent(codeTo);
			codeTo.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					if(tableWrapperLayout != null && codeFrom != null){
						tableWrapperLayout.setCodeTo(codeTo.getValueString());
					}
				}
			});

		}
		
		if(focDesc != null && focDesc instanceof IStatusHolderDesc){
			FocXMLAttributes attributes = new FocXMLAttributes();
			attributes.addAttribute(FXML.ATT_CAPTION_MARGIN, "0");
			attributes.addAttribute(FXML.ATT_SPACING, "true");
			attributes.addAttribute(FXML.ATT_MARGIN, "true");
			FVHorizontalLayout   statusVerticalLayout = new FVHorizontalLayout(attributes);
			IStatusHolderDesc    iStatusHolderDesc    = (IStatusHolderDesc) focDesc;
			FMultipleChoiceField fMultipleChoiceField = (FMultipleChoiceField) focDesc.getFieldByID(iStatusHolderDesc.getFLD_STATUS());
	    if(fMultipleChoiceField != null){
//		    Iterator<FMultipleChoiceItem> iterator = fMultipleChoiceField.getChoiceIterator();
		    
		    Collection<FMultipleChoiceItemInterface> collection = fMultipleChoiceField.getChoicesCollection();
		    if(collection != null){
		    	ArrayList<Integer> unselectedArray = getUnselectedStatusFilter(false);

		    	FVLabel label = new FVLabel("Status Filter");
					label.addStyleName("foc-f16");
					label.addStyleName("foc-bold");
		    	statusVerticalLayout.addComponent(label);
		    	statusVerticalLayout.addStyleName("border");
		    	
		    	Iterator<FMultipleChoiceItemInterface> iterator = collection.iterator();
		    	while(iterator.hasNext()){
			    	FMultipleChoiceItem multipleChoiceItem = (FMultipleChoiceItem) iterator.next();
			    	if(multipleChoiceItem != null && multipleChoiceItem.getId() >= StatusHolderDesc.STATUS_PROPOSAL){
			    		boolean value = true;
			    		if(unselectedArray != null){
			    			value = !unselectedArray.contains(multipleChoiceItem.getId());
			    		}
				    	StatusCheckBoxField fvCheckBox  = new StatusCheckBoxField(multipleChoiceItem.getId(), multipleChoiceItem.getTitle(), value);
				    	statusVerticalLayout.addComponent(fvCheckBox);
			    	}
			    }
		    }
		    /*
		    Iterator<FMultipleChoiceItem> iterator = fMultipleChoiceField.getChoiceIterator();
		    if(iterator != null){
		    	ArrayList<Integer> unselectedArray = getUnselectedStatusFilter(false);
		    	
			    while(iterator.hasNext()){
			    	FMultipleChoiceItem multipleChoiceItem = iterator.next();
			    	if(multipleChoiceItem != null){
			    		
			    		boolean value = true;
			    		if(unselectedArray != null){
			    			value = !unselectedArray.contains(multipleChoiceItem.getId());
			    		}
				    	StatusCheckBoxField fvCheckBox  = new StatusCheckBoxField(multipleChoiceItem.getId(), multipleChoiceItem.getTitle(), value);
				    	statusVerticalLayout.addComponent(fvCheckBox);
				    	
			    	}
			    }
		    }
		    */
	    }
			getTransactionFilterVerticalLayout().addComponent(statusVerticalLayout);
		}
		
		if(getTransactionFilterVerticalLayout() != null){
			DepartmentTree departmentTree = new DepartmentTree();
			XMLViewKey xmlViewKey = new XMLViewKey(DepartmentDesc.getInstance().getStorageName(), XMLViewKey.TYPE_TREE, WorkflowWebModule.CTXT_DEPARTMENT_SELECTION, XMLViewKey.VIEW_DEFAULT);
			department_Selection_Tree = (ACC_DEPARTMENT_Selection_Standard_Tree) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), xmlViewKey, departmentTree);
			
			department_Selection_Tree.setParentLayout(this);
			department_Selection_Tree.setFocDataOwner(true);
			
			getTransactionFilterVerticalLayout().addComponent((Component) department_Selection_Tree);
			
			//We set all to selected then we remove the selection from the lines in the exclusionArray
			//----------------------------------------------------------------------------------------
			department_Selection_Tree.getDepartmentSelectionTableTreeDelegate().selectionColumn_selectAll(true);
			ArrayList<Long> depArray = getUnselectedDepartmentFilter(false);
			if(depArray != null){
				for(int i=0; i<depArray.size(); i++){
					department_Selection_Tree.getDepartmentSelectionTableTreeDelegate().selectionColumn_setSelectionMemory(depArray.get(i), false);
				}
			}
			//----------------------------------------------------------------------------------------
		}
	}
	
	public ArrayList<Integer> getUnselectedStatusFilter(boolean create){
		return tableWrapperLayout != null ? tableWrapperLayout.excludedStatusArray_get(create) : null;
	}

	public ArrayList<Long> getUnselectedDepartmentFilter(boolean create){
		return tableWrapperLayout != null ? tableWrapperLayout.excludedDepartmentArray_get(create) : null;
	}

//	public void copyExclusionsFrom_MemoryToGui(){
//		ArrayList<Integer> statusList     = getUnselectedStatusFilter(false);
//		if(statusList != null){
//			for(int i=0; i<statusList.size(); i++){
//				int statusValue = statusList.get(i);
//				
//			}
//		}
//		
//		ArrayList<Integer> departmentList = getUnselectedDepartmentFilter(false);
//		if(departmentList != null){
//			for(int i=0; i<departmentList.size(); i++){
//				int departmentRef = departmentList.get(i);
//				TableTreeDelegate tableTreeDelegate = getDepartmentSelectionTableTreeDelegate();
//				if(tableTreeDelegate != null){
//					tableTreeDelegate.selectionColumn_setSelectionGUI(departmentRef, false);
//				}
//			}
//		}
//	}
	
	public void copyExclusionsFrom_GuiToMemory(){
		TableTreeDelegate departmentSelectionTableTreeDelegate = getDepartmentSelectionTableTreeDelegate();
		if(departmentSelectionTableTreeDelegate != null && departmentSelectionTableTreeDelegate.getTreeOrTable() != null && departmentSelectionTableTreeDelegate.getTreeOrTable().getFocDataWrapper() != null){
			ArrayList<Long> arrayDep = getUnselectedDepartmentFilter(false);
			if(arrayDep != null) arrayDep.clear();
			
			ArrayList<Object> selectedArrayList = departmentSelectionTableTreeDelegate.selectionColumn_getSelectedIdArrayList();
			FocDataWrapper focDataWrapper = departmentSelectionTableTreeDelegate.getTreeOrTable().getFocDataWrapper();
			for(int i=0; i<focDataWrapper.size(); i++){
				Department dep = (Department) focDataWrapper.getAt(i);
				if(selectedArrayList == null || !selectedArrayList.contains(dep.getReference().getLong())){
					getUnselectedDepartmentFilter(true).add(dep.getReference().getLong());
				}
			}
		}
	}
	
	/*
	public ArrayList<Integer> getUnselectedDepartmentFilter(){
		TableTreeDelegate departmentSelectionTableTreeDelegate = getDepartmentSelectionTableTreeDelegate();
		if(departmentSelectionTableTreeDelegate != null){
			ArrayList<Object> selectedArrayList = departmentSelectionTableTreeDelegate.selectionColumn_getSelectedIdArrayList();
			
			for(int i=0; i<departmentSelectionTableTreeDelegate.getTreeOrTable().getFocDataWrapper().size(); i++){
				Department dep = (Department) departmentSelectionTableTreeDelegate.getTreeOrTable().getFocDataWrapper().getAt(i);
				if(!selectedArrayList.contains(dep.getReference().getInteger())){
					getUnselectedDepartmentFilter(true).add(dep.getReference().getInteger());
				}
			}
		}
		return unselectedDepartmentFilter;
	}
	*/
	
	private FVVerticalLayout getTransactionFilterVerticalLayout(){
		return (FVVerticalLayout) getComponentByName("_TRANSACTION_FILTER_HORIZONTAL_FORM");
	}
	
	private TableTreeDelegate getDepartmentSelectionTableTreeDelegate(){
		return department_Selection_Tree != null ? department_Selection_Tree.getDepartmentSelectionTableTreeDelegate() : null;
	}
	
	@Override
	public boolean validationCheckData(FVValidationLayout validationLayout) {
		copyExclusionsFrom_GuiToMemory();
		return false;
	}

	public FVTableWrapperLayout getTableWrapperLayout() {
		return tableWrapperLayout;
	}

	public void setTableWrapperLayout(FVTableWrapperLayout tableWrapperLayout) {
		this.tableWrapperLayout = tableWrapperLayout;
	}
	
	private class StatusCheckBoxField extends CheckBox implements ValueChangeListener{
		
		private String title     = null;
		private int    statusRef = -1;
		
		public StatusCheckBoxField(int statusRef, String title, boolean value){
			this.title     = title;
			this.statusRef = statusRef;
			setCaption(title);
			setValue(value);
			addValueChangeListener(new ValueChangeListener() {
				
				public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
					boolean isSelected = (Boolean) event.getProperty().getValue();
					if(isSelected){
						ArrayList<Integer> unselectedStatusArrayList = getUnselectedStatusFilter(false);	
						if(unselectedStatusArrayList != null){
							unselectedStatusArrayList.remove((Integer)getStatusRef());
						}
					}else{
						ArrayList<Integer> unselectedStatusArrayList = getUnselectedStatusFilter(true);
						if(!unselectedStatusArrayList.contains(getStatusRef())){
							unselectedStatusArrayList.add(getStatusRef());
						}
					}
				}
			});
		}
		
		private int getStatusRef(){
			return statusRef;
		}
		
		private String getTitle(){
			return title;
		}
	}
}
