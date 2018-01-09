package com.foc.web.unitTesting.recording;

import java.util.StringTokenizer;

import com.foc.Globals;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.FVTextField;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UnitTestingRecorder_TextField implements ValueChangeListener {

	private FVTextField textField = null;
	
	public UnitTestingRecorder_TextField(FVTextField textField) {
		this.textField = textField;
		if(textField != null) {
			textField.addValueChangeListener(this);
		}
	}
	
	public void dispose() {
		if(textField != null) {
			textField.removeValueChangeListener(this);
			textField = null;
		}
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if(textField != null) {
			FocXMLGuiComponentDelegate delegate = textField.getDelegate(); 
			if(delegate != null && !Utils.isStringEmpty(delegate.getNameInMap())){
				String compName = delegate.getNameInMap();
				String tableName = null;
				long ref = 0;
				String columnName = null;
				
				if(compName.contains("|")) {
					try {
						StringTokenizer tokenizer = new StringTokenizer(compName, "|") ;
						if(tokenizer.countTokens() == 3) {
							tableName = tokenizer.nextToken();
							ref = Long.valueOf(tokenizer.nextToken());
							columnName = tokenizer.nextToken();
						}
					}catch(Exception e) {
						tableName = null;
						Globals.logExceptionWithoutPopup(e);
					}
				}
				
				if(tableName != null) {
					FocUnitRecorder.recordLine("cmd.componentInTable_SetValue(\""+tableName+"\", "+ref+", \""+columnName+"\", "+textField.getValue()+"\", null);");
				} else {
					FocUnitRecorder.recordLine("cmd.component_SetValue(\""+delegate.getNameInMap()+"\", \""+textField.getValue()+"\", false);");
				}
			}
		}
	}
}
