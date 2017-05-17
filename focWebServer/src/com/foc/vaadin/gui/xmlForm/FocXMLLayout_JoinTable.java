package com.foc.vaadin.gui.xmlForm;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.components.ITableTree;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

@SuppressWarnings("serial")
public abstract class FocXMLLayout_JoinTable extends FocXMLLayout {
	
	abstract public FocDesc getOriginalFocDesc();
	abstract public int getOriginalObjectReference(FocObject focObject);
	
	private ICentralPanel formLayout = null;

	@Override
	public void dispose() {
		super.dispose();
		
		formLayout = null;
	}

	public ICentralPanel getFormLayout() {
		return formLayout;
	}
	
	public void setFormLayout(ICentralPanel formLayout) {
		this.formLayout = formLayout;
	}
	
	@Override
	public ICentralPanel table_OpenItem(String tableName, ITableTree table, FocObject focObject, int viewContainer_Open) {
		FocConstructor constr = new FocConstructor(getOriginalFocDesc(), null);
		FocObject newObject = (FocObject) constr.newItem();
		newObject.setReference(getOriginalObjectReference(focObject));
		newObject.load();

		XMLViewKey xmlViewKey = new XMLViewKey(newObject.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
		ICentralPanel formLayout = XMLViewDictionary.getInstance().newCentralPanel(getParentNavigationWindow(), xmlViewKey, newObject);
		formLayout.setFocDataOwner(true);
		getParentNavigationWindow().changeCentralPanelContent(formLayout, true);
		
		formLayout.getValidationLayout().addValidationListener(new IValidationListener() {
			@Override
			public void validationDiscard(FVValidationLayout validationLayout) {
			}
			
			@Override
			public boolean validationCommit(FVValidationLayout validationLayout) {
				return false;
			}
			
			@Override
			public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
				if(getFocList() != null){
					getFocList().reloadFromDB();
				}
			}
		});
		
		return formLayout;
	}
	
	@Override
	public FocObject table_AddItem(String tableName, ITableTree table, FocObject fatherObject) {
		FocConstructor constr = new FocConstructor(getOriginalFocDesc(), null);
		FocObject newFocObject = (FocObject) constr.newItem();
		newFocObject.setCreated(true);
		newFocObject.setCompany(Globals.getApp().getCurrentCompany());
		
		XMLViewKey xmlViewKey = new XMLViewKey(newFocObject.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
		formLayout = XMLViewDictionary.getInstance().newCentralPanel(getParentNavigationWindow(), xmlViewKey, newFocObject);
		formLayout.setFocDataOwner(true);
		getParentNavigationWindow().changeCentralPanelContent(formLayout, true);
		
		formLayout.getValidationLayout().addValidationListener(new IValidationListener() {
			@Override
			public void validationDiscard(FVValidationLayout validationLayout) {
			}
			
			@Override
			public boolean validationCommit(FVValidationLayout validationLayout) {
				return false;
			}
			
			@Override
			public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
				if(getFocList() != null){
					getFocList().reloadFromDB();
				}
			}
		});
		
		return newFocObject;
	}
	
	@Override
	public void table_DeleteItem(ITableTree table, FocObject focObject) {
		FocConstructor constr = new FocConstructor(getOriginalFocDesc(), null);
		FocObject newObject = (FocObject) constr.newItem();
		newObject.setReference(getOriginalObjectReference(focObject));
		newObject.load();

		StringBuffer checkDeletion = newObject.checkDeletionWithMessage(); 
		if(checkDeletion != null && checkDeletion.length() > 0){
			Globals.showNotification("Cannot Delete", checkDeletion.toString(), IFocEnvironment.TYPE_WARNING_MESSAGE);
		}else{
			newObject.setDeleted(true);
			boolean error = !newObject.validate(true);
			
			if(!error){
				super.table_DeleteItem(table, focObject);
			}
		}
		focObject.dispose();
	}
	
}
