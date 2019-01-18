package com.foc.web.unitTesting.recording;

import java.util.StringTokenizer;

import com.foc.Globals;
import com.foc.util.Utils;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.unitTesting.FocUnitRecorder;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;

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
	
	private FocXMLLayout getParentFocXMLLayout() {
		FocXMLLayout xmlLayout = null;
		if(component instanceof AbstractComponent) {
			xmlLayout = ((AbstractComponent)component).findAncestor(FocXMLLayout.class);
		}		
		return xmlLayout;
	}
	
	private String getLayoutNameSection() {
		String layoutNameSection = "";
		FocXMLLayout xmlLayout = null;
		xmlLayout = getParentFocXMLLayout();
		if(xmlLayout != null) {
			FocXMLLayout xmlLayoutHigher = xmlLayout.findAncestor(FocXMLLayout.class);
			if(xmlLayoutHigher != null && xmlLayout.getDelegate() != null) {
				layoutNameSection = xmlLayout.getDelegate().getNameInMap();
				if(!Utils.isStringEmpty(layoutNameSection)) {
					layoutNameSection = "\""+layoutNameSection+"\", ";
				}
			}
		}

		return layoutNameSection; 
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		FocUnitRecorder recorder = FocUnitRecorder.getInstance(false);
		
		if(recorder != null && component != null) {
			FocXMLGuiComponentDelegate delegate = component.getDelegate(); 
			if(delegate != null && !Utils.isStringEmpty(delegate.getNameInMap())){
				String layoutNameSection = getLayoutNameSection();
				FocXMLLayout xmlLayout = getParentFocXMLLayout(); 
				if(xmlLayout == null || !xmlLayout.isProcessingCopyMemoryToGui()) {
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
						boolean treated = false;
						
						FVTableWrapperLayout tableWrapperLayout = getTableWrapperLayout();
						if(tableWrapperLayout != null) {
							boolean recordedWithRefSelecion = tableWrapperLayout.recordTableLineSelection("ref");
							if(recordedWithRefSelecion) {
								FocUnitRecorder.recordLine("componentInTable_SetValue("+layoutNameSection+"\""+tableName+"\", ref, \""+columnName+"\", \""+component.getValueString()+"\", null);");
								treated = true;
							}
						}
						
						if(!treated){
							FocUnitRecorder.recordLine("componentInTable_SetValue("+layoutNameSection+"\""+tableName+"\", "+ref+", \""+columnName+"\", \""+component.getValueString()+"\", null);");
						}
					} else {
						FocUnitRecorder.recordLine("component_SetValue("+layoutNameSection+"\""+delegate.getNameInMap()+"\", \""+component.getValueString()+"\", false);");
					}
				}
			}
		}
	}
	
	public FVTableWrapperLayout getTableWrapperLayout() {
		FVTableWrapperLayout tableWrapper = null;
		if(component instanceof AbstractComponent) {
			tableWrapper = ((AbstractComponent)component).findAncestor(FVTableWrapperLayout.class);
		}		
		return tableWrapper;
	}
}
