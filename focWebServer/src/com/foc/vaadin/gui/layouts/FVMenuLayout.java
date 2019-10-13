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

import java.util.ArrayList;
import java.util.LinkedList;

import org.xml.sax.Attributes;

import com.foc.ConfigInfo;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class FVMenuLayout extends FVVerticalLayout implements FVLayout {

  private String name;
  private String type;
  private boolean isDragDropEnabled;
  private Attributes attributes = null;
  
  private FocXMLGuiComponentDelegate delegate = null;

  private LinkedList<MenuLayoutItem> tabList  = null; 
  private HorizontalLayout buttonsLayout = null;
	private int              currentIndex  = -1;
	
	private ArrayList<MenuSelectionListener> listeners = null;
  
  public FVMenuLayout(Attributes attributes) {
    delegate = new FocXMLGuiComponentDelegate(this);
    setAttributes(attributes);
    
    addStyleName(FVLayout.DEFAULT_STYLE);
    
    buttonsLayout = new HorizontalLayout();
    buttonsLayout.setStyleName("foc-menuLayout");
//    addComponent(buttonsLayout);
  
    tabList = new LinkedList<FVMenuLayout.MenuLayoutItem>();
  }

  @Override
  public void dispose(){
    name         = null;
    type         = null;
    attributes   = null;
    if(delegate != null){
    	delegate.dispose();
    	delegate = null;
    }
    removeAllComponents();
    if(tabList != null) {
    	tabList.clear();
    	tabList = null;
    }
  }
  
  private boolean isGuiRTL() {
  	return ConfigInfo.isGuiRTL();
  }
  
  public HorizontalLayout getButtonsLayout() {
		return buttonsLayout;
	}

  @Override
  public void setWYSIWYGDropHandler() {
  }
  
  @Override
  public IFocData getFocData() {
    return null;
  }

  @Override
  public String getXMLType() {
  	return FXML.TAG_MENU_LAYOUT;
  }

  @Override
  public Field getFormField() {
    return null;
  }

  @Override
  public boolean copyGuiToMemory() {
  	return false;
  }

  @Override
  public void copyMemoryToGui() {

  }

  @Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
    
    if (attributes != null && attributes.getValue("border") != null) {
      addStyleName("border");
    }
    
    setCaption("");
  }
  
  public void changeSelection(int newSelection) {
  	if(newSelection != currentIndex && newSelection >= 0) {
  		if(currentIndex >=0 ) {
  			MenuLayoutItem oneLay = tabList.get(currentIndex);
  			if(oneLay != null) {
  				removeComponent(oneLay.getComponent());
	  			oneLay.setSelected(false);
  			}
  		}
  		currentIndex = newSelection;

			MenuLayoutItem oneLay = tabList.get(currentIndex);
			if(oneLay != null) {
				notifyListener(oneLay);
  			addComponent(oneLay.getComponent());
  			oneLay.setSelected(true);
			}
  	}
  }

  @Override
  public void addComponent(Component comp, Attributes attributes) {
    String title = attributes.getValue(FXML.ATT_TITLE);
    if(title == null){
    	title = attributes.getValue(FXML.ATT_NAME);
    }

    MenuLayoutItem lay = new MenuLayoutItem(tabList.size(), title, comp);
    tabList.add(lay);
    if(isGuiRTL()) {
    	buttonsLayout.addComponentAsFirst(lay.getButton());
    } else {
    	buttonsLayout.addComponent(lay.getButton());
    }
    
    if(tabList.size() == 1) {
    	changeSelection(0);
    }
    
  	/*
    comp.addStyleName("padding");
    
    String title = attributes.getValue(FXML.ATT_TITLE);
    if(title == null){
    	title = attributes.getValue(FXML.ATT_NAME);
    }
    
    String positionString = attributes.getValue(FXML.ATT_TAB_POSITION);
    if(positionString != null){
    	int position = 0;
    	try {
    		position = Integer.parseInt(positionString);
    	} catch (NumberFormatException e) {
    		Globals.logException(e);
    	}
    	
    	addTab(comp, title, null, position);
    }else{
    	addTab(comp, title);
    }
    
    if(delegate != null && delegate.isConstructionMode()){
      removeTab(getTab(addTabLayout));
      addTab(addTabLayout, "+");
    }
    */
  }
  
  public void addNewTab() {
//    getUI().addWindow(new AddTabWindow(this, addTabLayout));
  }

  @Override
  public void setDragDrop(boolean state) {
    if (state) {
      isDragDropEnabled = true;
    } else {
      isDragDropEnabled = false;
    }
  }

  @Override
  public boolean isDragDrop() {
    return isDragDropEnabled;
  }

  @Override
  public ComponentPosition getPosition(Component comp) {
    return null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public Attributes getAttributes() {
    return attributes;
  }

  @Override
  public boolean isXMLLeaf() {
    return false;
  }

  @Override
  public void fillXMLNodeContent(XMLBuilder builder) {
  }
  
  @Override
  public void setDelegate(FocXMLGuiComponentDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public FocXMLGuiComponentDelegate getDelegate() {
    return delegate;
  }

  @Override
  public void setFocData(IFocData focData) {
  }
  
  @Override
  public String getValueString() {
    return null;
  }

  @Override
  public void setValueString(String value) {
  }
  
	@Override
	public FocCentralPanel getWindow() {
		return findAncestor(FocCentralPanel.class);
	}
	
	@Override
	public void refreshEditable() {
	}
	
	public void addListener(MenuSelectionListener listener) {
		if(listener != null) {
			if(listeners == null) listeners = new ArrayList<FVMenuLayout.MenuSelectionListener>();
			listeners.add(listener);
		}
	}
	
	public void removeListener(MenuSelectionListener listener) {
		if(listeners != null && listener != null) {
			listeners.remove(listener);
		}
	}
	
	public void notifyListener(MenuLayoutItem oneLayout) {
		if(listeners != null && oneLayout != null) {
			for(int i=0; i<listeners.size(); i++) {
				MenuSelectionListener listener = listeners.get(i);
				listener.menuSelected(oneLayout.getIndex(), oneLayout.getTitle(), oneLayout.getComponent());
			}
		}
	}
	
	public interface MenuSelectionListener {
		public void menuSelected(int menu, String title, Component component);
	}
	
	private class MenuLayoutItem implements Button.ClickListener {
		private int       index     = 0;
		private String    title     = null;
		private Button    button    = null;
		private Component component = null;
		
		public MenuLayoutItem(int index, String title, Component component) {
			this.index     = index;
			this.title     = title;
			
			this.component = component;
	    button  = new Button(title);
	    button.setStyleName(BaseTheme.BUTTON_LINK);
	    button.addStyleName("foc-MenuLayoutButton");
	    button.addClickListener(MenuLayoutItem.this);
		}
		
		public void dispose() {
			component = null;
			button = null;
		}
		
		public int getIndex() {
			return index;
		}
		
		public String getTitle() {
			return title;
		}
		
		public Button getButton() {
			return button;
		}

		public Component getComponent() {
			return component;
		}

		public void setSelected(boolean selected) {
			if(button != null) {
				if(!selected) {
					button.removeStyleName("foc-MenuLayoutButton-Selected");
				} else {
					button.addStyleName("foc-MenuLayoutButton-Selected");
				}
			}
		}

		@Override
		public void buttonClick(ClickEvent event) {
			changeSelection(getIndex());
		}
		
	}
}
