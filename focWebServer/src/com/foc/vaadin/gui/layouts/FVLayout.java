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
package com.foc.vaadin.gui.layouts;

import java.util.Iterator;
import java.util.Random;

import org.xml.sax.Attributes;

import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.XMLBuilder;
import com.vaadin.ui.Component;

public interface FVLayout extends FocXMLGuiComponent {
	public static final String RANDOM_BACKGROUND_PREFIX = "random-bg";
	public static final String DEFAULT_STYLE = "bg-white";
	public static final String BORDER_BACKGROUND_STYLE = "border-bg";
  public static final Random rand = new Random();
  
  public void addComponent(Component comp, Attributes attributes );
  public void addComponent(Component comp);
  public void setDragDrop(boolean state);
  public boolean isDragDrop();
  public Iterator<Component> getComponentIterator();
  public FocCentralPanel getWindow();
  public ComponentPosition getPosition(Component comp);
  public float getWidth();
  public float getHeight();
  public String getName();
  public String getType();
  public void setName(String name);
  public void setType(String type);
  public void setHeight(String height);
  public void setWidth(String width);
  public void setStyleName(String css);
  public void addStyleName(String styleName);
  public Attributes getAttributes();
  public boolean isXMLLeaf();
  public void fillXMLNodeContent(XMLBuilder builder);
  public void setWYSIWYGDropHandler();
}
