package com.foc.vaadin.gui.components.objectSelectorPopupView;

import com.foc.desc.FocObject;
import com.foc.desc.field.FObjectField;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.components.FVNativeButton;
import com.foc.vaadin.gui.components.FVObjectSelector;
import com.foc.vaadin.gui.components.FVTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FVObjectSelectorWindow extends Window implements IObjectSelectWindowListener{

	private HorizontalLayout mainHorizontalLayout = null;
	private FVObjectSelector objectSelector = null;
	private FVTextField newValueTextField = null;
	private NativeButton addValueButton = null;
	private FVNativeButton cancelButton = null;
	private String newValue = null;
	
	public FVObjectSelectorWindow(FVObjectSelector objectSelector, String newValue) {
		this.newValue = newValue;
		this.objectSelector = objectSelector;
		init();
	}
	
	private void init(){
		setModal(true);
		setResizable(false);
		setWidth("400px");
		setHeight("200px");
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		verticalLayout.addComponent(getMainHorizontalLayout());
		verticalLayout.setComponentAlignment(getMainHorizontalLayout(), Alignment.MIDDLE_CENTER);
		setContent(verticalLayout);
	}
	
	public void dispose(){
		mainHorizontalLayout = null;
		if(newValueTextField != null){
			newValueTextField.dispose();
			newValueTextField = null;
		}
		addValueButton = null;
		objectSelector = null;
		newValue = null;
	}
	
	public void show(){
		UI.getCurrent().addWindow(this);
	}
	
	private HorizontalLayout getMainHorizontalLayout(){
		if(mainHorizontalLayout == null){
			mainHorizontalLayout = new HorizontalLayout();
			mainHorizontalLayout.setSizeUndefined();
			mainHorizontalLayout.addComponent(getNewValueTextField());
			mainHorizontalLayout.addComponent(getAddNewValueButton());
			mainHorizontalLayout.setComponentAlignment(getAddNewValueButton(), Alignment.BOTTOM_LEFT);
			mainHorizontalLayout.addComponent(getCancelButton());
			mainHorizontalLayout.setComponentAlignment(getCancelButton(), Alignment.BOTTOM_LEFT);
		}
		return mainHorizontalLayout;
	}
	
	private FVTextField getNewValueTextField(){
		if(newValueTextField == null){
			newValueTextField = new FVTextField("Do you want to add this item?");
			newValueTextField.setValue(newValue);
		}
		return newValueTextField;
	}
	
	private NativeButton getAddNewValueButton(){
		if(addValueButton == null){
			addValueButton = new NativeButton();
			addValueButton.setIcon(FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_ADD));
			addValueButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					if(getObjectSelector() != null && getObjectSelector().getComboBox() != null){
						getObjectSelector().getComboBox().setiObjectSelectWindowListener(FVObjectSelectorWindow.this);
						getObjectSelector().getComboBox().addNewObject();
					}
				}
			});
		}
		return addValueButton;
	}
	
	@Override
	public void beforeOpenForm(FocObject focObject){
		if(getObjectSelector() != null && focObject != null){
			FObject fObject = getObjectSelector().getFocData();
			FObjectField fObjectField = (FObjectField) ((fObject != null && fObject.getFocField() instanceof FObjectField) ? fObject.getFocField() : null);
			if(fObjectField != null){
				int fieldId = fObjectField.getDisplayField();
				FProperty property = focObject.getFocProperty(fieldId);
				if(property == null && getObjectSelector() != null && getObjectSelector().getCaptionProperty() != null){
					fieldId = focObject.getThisFocDesc().getFieldByName(getObjectSelector().getCaptionProperty()).getID();
//					fObjectField = (FObjectField) focObject.getThisFocDesc().getFieldByName(getObjectSelector().getDelegate().getDataPath());
					property = focObject.getFocProperty(fieldId);
				}
				if(property != null){
					property.setValue(newValue);
				}
			}
			close();
		}
	}
	
	private FVNativeButton getCancelButton(){
		if(cancelButton == null){
			cancelButton = new FVNativeButton("");
			cancelButton.setIcon(FVIconFactory.getInstance().getFVIcon_24(FVIconFactory.ICON_CANCEL));
			cancelButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					close();					
				}
			});
		}
		return cancelButton;
	}
	
	private FVObjectSelector getObjectSelector(){
		return objectSelector;
	}
	
}
