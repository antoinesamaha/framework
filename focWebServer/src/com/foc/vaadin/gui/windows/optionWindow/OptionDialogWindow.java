package com.foc.vaadin.gui.windows.optionWindow;

import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVLabel;
import com.foc.vaadin.gui.layouts.FVHorizontalLayout;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.vaadin.ui.Window;

/** Use OptionDialog with Globals.popupDialog(optionDialog) instead.
 *
 */
@SuppressWarnings("serial")
@Deprecated
public class OptionDialogWindow extends Window {
	
	private String             optionSelected = null; 
	private FVHorizontalLayout buttonsLayout  = null; 
	private Object             contextObject  = null;
	
	public OptionDialogWindow(String message, Object contextObject){
		setContextObject(contextObject);
		FVVerticalLayout vLayout = new FVVerticalLayout(null);
		vLayout.setMargin(true);
		vLayout.setSpacing(true);
		vLayout.setSizeFull();
		setContent(vLayout);
		
		FVLabel messageLabel = new FVLabel(message);
		messageLabel.setStyleName("instruction");
		vLayout.addComponent(messageLabel);
		
		buttonsLayout = new FVHorizontalLayout(null);
		vLayout.addComponent(buttonsLayout);
		
		setModal(true);
	}
	
	public void dispose(){
		buttonsLayout = null;
	}

	public void addOption(String caption, IOption option){
		OptionButton button = new OptionButton(caption, option);
		buttonsLayout.addComponent(button);
	}
	
	public FVHorizontalLayout getButtonsLayout(){
	  return buttonsLayout;
	}
	
	public String getOptionSelected() {
		return optionSelected;
	}

	public void setOptionSelected(String optionSelected) {
		this.optionSelected = optionSelected;
	}

	public Object getContextObject() {
		return contextObject;
	}

	public void setContextObject(Object contextObject) {
		this.contextObject = contextObject;
	}
	
	public String popup(){
		getUI().addWindow(this);
		return getOptionSelected();
	}
	
	private class OptionButton extends FVButton implements com.vaadin.ui.Button.ClickListener {
		
		private String  option         = null;
		private IOption optionExecutor = null;
		
		public OptionButton(String option, IOption optionExecutor){
			super(option);
			this.option = option;
			this.optionExecutor = optionExecutor;
			
			addClickListener((com.vaadin.ui.Button.ClickListener)this);
		}

		public String getOption() {
			return option;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			optionExecutor.optionSelected(getContextObject());
			OptionDialogWindow.this.close();
		}
	}

}
