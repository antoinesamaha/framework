package com.foc.web.unitTesting.recording;

import java.util.StringTokenizer;

import com.foc.Globals;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class UnitTestingRecorder_CommonField<C extends FocXMLGuiComponent> implements ValueChangeListener {

	private FocXMLGuiComponent component = null;
	
	public UnitTestingRecorder_CommonField(C component) {
		this.component = component;
		if(component != null && component.getFormField() != null) {
			component.getFormField().addValueChangeListener(this);
		}
	}
	
	public void dispose() {
		if(component != null && component.getFormField() != null) {
			component.getFormField().removeValueChangeListener(this);
			component = null;
		}
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if(component != null) {
			FocXMLGuiComponentDelegate delegate = component.getDelegate(); 
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
					FocUnitRecorder.recordLine("cmd.componentInTable_SetValue(\""+tableName+"\", "+ref+", \""+columnName+"\", "+component.getValueString()+"\", null);");
				} else {
					FocUnitRecorder.recordLine("cmd.component_SetValue(\""+delegate.getNameInMap()+"\", \""+component.getValueString()+"\", false);");
				}
			}
		}
	}
}
