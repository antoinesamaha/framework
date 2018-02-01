package com.foc.vaadin.gui;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.web.unitTesting.recording.UnitTestingRecorder_CommonField;
import com.vaadin.ui.Field;

public interface FocXMLGuiComponent {
  public void       dispose();
  
	public IFocData   getFocData();
	public void       setFocData(IFocData focData);
	
  public String     getXMLType();
  public Field      getFormField();
  
	public boolean    copyGuiToMemory();
	public void       copyMemoryToGui();
	
  public Attributes getAttributes();
  public void       setAttributes(Attributes attributes);
  
  public String     getValueString();
  public void       setValueString(String value);
  
  public void       setDelegate(FocXMLGuiComponentDelegate delegate);
  public FocXMLGuiComponentDelegate getDelegate();
  
  public void       refreshEditable();
}
