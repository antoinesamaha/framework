package com.foc.web.unitTesting.recording;

import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UnitTestingRecorder_Button implements ClickListener {

	private FVButton button          = null;
	private String   buttonNameInMap = "";
	
	public UnitTestingRecorder_Button(FVButton button) {
		this.button = button;
		if(button != null) {
			button.addClickListener(this);
		}
	}
	
	public void dispose() {
		if(button != null && FocUnitRecorder.getInstance(false) != null) {
			//Sometimes we dispose before being called for listener  
			getNameFromButtonIfEmpty();
			button.removeClickListener(this);
			button = null;
		}
	}

	private void getNameFromButtonIfEmpty() {
		if(button != null && Utils.isStringEmpty(buttonNameInMap)) {
			FocXMLGuiComponentDelegate delegate = button.getDelegate(); 
			if(delegate != null){
				buttonNameInMap = delegate.getNameInMap();
			}
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		FocUnitRecorder recorder = FocUnitRecorder.getInstance(false);
		if(recorder != null) {
			getNameFromButtonIfEmpty();
			if(!Utils.isStringEmpty(buttonNameInMap)){
				FocUnitRecorder.recordLine("button_Click(\""+buttonNameInMap+"\");");
			}
		}
	}
}
