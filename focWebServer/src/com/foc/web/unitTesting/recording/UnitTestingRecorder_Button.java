package com.foc.web.unitTesting.recording;

import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UnitTestingRecorder_Button implements ClickListener {

	private FVButton button = null;
	
	public UnitTestingRecorder_Button(FVButton button) {
		this.button = button;
		if(button != null) {
			button.addClickListener(this);
		}
	}
	
	public void dispose() {
		if(button != null) {
			button.removeClickListener(this);
			button = null;
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if(button != null) {
			FocXMLGuiComponentDelegate delegate = button.getDelegate(); 
			if(delegate != null && !Utils.isStringEmpty(delegate.getNameInMap())){
				FocUnitRecorder.recordLine("button_Click(\""+delegate.getNameInMap()+"\")");
			}
		}
	}
}
