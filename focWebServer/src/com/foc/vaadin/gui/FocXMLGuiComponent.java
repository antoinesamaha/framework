/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.vaadin.gui;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
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
