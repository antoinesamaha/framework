package com.foc.web.modules.admin;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.foc.OptionDialog;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class OptionDialog_Form extends FocXMLLayout {
	
	private OptionDialog optionDialog = null;
	
	public void dispose(){
		super.dispose();
		optionDialog = null;
	}
	
	public void addButton(String buttonName, ClickListener clickListener){
		addButton(buttonName, buttonName, clickListener);
	}
	
	public FVVerticalLayout getComponentsLayout(){
		return (FVVerticalLayout) getComponentByName("_COMPONENTS_LAYOUT");
	}
	
	public FVButton addButton(String buttonName, String buttonTitle, ClickListener clickListener){
		FVButton button = null;
		FVHorizontalLayout horizontalLayout = (FVHorizontalLayout) getComponentByName("OPTION_DIALOG_WINDOW");
		
		if(horizontalLayout != null){
			button = new FVButton(buttonTitle, clickListener);
//			horizontalLayout.addComponent(button);
			addComponentToGuiAndMap(button, buttonName, null, horizontalLayout);
		}
		return button;
	}
		
	public void fillMessage(){
		FVLabel label = (FVLabel) getComponentByName("MESSAGE_LABEL");
		label.setValue(optionDialog.getMessage());
	}
	
	public void popup(){
	  fillMessage();
	  
		for(int i=0; i<optionDialog.getOptionCount(); i++){
			FVButton button = addButton(optionDialog.getOptionNameAt(i), optionDialog.getOptionCaptionAt(i), new OptionButtonListener(i));
			optionDialog.setOptionButtonAt(i, button);
		}
	  
		if(FocWebApplication.getInstanceForThread().isMobile()){
			getMainWindow().changeCentralPanelContent(OptionDialog_Form.this, true);
		}else{
			FocCentralPanel centralPanel = new FocCentralPanel();
			centralPanel.fill();
			centralPanel.changeCentralPanelContent(OptionDialog_Form.this, false);
			
			Window optionWindow = centralPanel.newWrapperWindow();
			
			optionWindow.setCaption(optionDialog.getTitle());
			optionWindow.setWidth(optionDialog.getWidth());
			optionWindow.setHeight(optionDialog.getHeight());
	
			FocWebApplication.getInstanceForThread().addWindow(optionWindow);
		}
	}

	public OptionDialog getOptionDialog() {
		return optionDialog;
	}

	public void setOptionDialog(OptionDialog optionDialog) {
		this.optionDialog = optionDialog;
	}

	public class OptionButtonListener implements ClickListener {
		int indexOfOption = -1;
		
		public OptionButtonListener(int indexOfOption){
			this.indexOfOption = indexOfOption;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			if(!optionDialog.executeOption(optionDialog.getOptionNameAt(indexOfOption))){
				goBack(null);
			}
		}
	}
}


